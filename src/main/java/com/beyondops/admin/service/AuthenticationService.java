package com.beyondops.admin.service;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import com.beyondops.admin.common.AppConstants;
import com.beyondops.admin.model.AuthUser;
import com.beyondops.admin.util.StringUtil;
import com.beyondops.admin.util.TimeUtil;
import com.beyondops.jooq.model.Tables;
import com.beyondops.jooq.model.tables.records.SysUserRecord;
import com.beyondops.jooq.model.tables.records.SysUserRoleRecord;
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import javax.annotation.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.stereotype.Service;

/**
 * Created by caiqinzhou@gmail.com on 2017/1/13.
 */
@Service
public class AuthenticationService {

  private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

  @Autowired
  private DSLContext dsl;
  @Autowired
  LdapTemplate ldapTemplate;

  @Value("${app.jwt.key}")
  private String jwtKey;

  @Value("${app.sysuser.password-salt}")
  private String userPasswordSalt;

  @Value("${app.ldap.enable}")
  private boolean ldapEnable;

  @Value("${app.ldap.contextSource.query}")
  private String ldapQuery;

  @Resource(name = "stringRedisTemplate")
  ValueOperations<String, String> valueOperations;

  /**
   * User authentication, support local user and ldap user.
   */
  public SysUserRecord authentication(String username, String password) {
    if (Strings.isNullOrEmpty(username) || Strings.isNullOrEmpty(password)) {
      return null;
    }
    SysUserRecord sysUserRecord =
        dsl.selectFrom(Tables.SYS_USER).where(Tables.SYS_USER.USERNAME.eq(username)).fetchOne();
    if (null == sysUserRecord) {
      //Get user info from ldap
      sysUserRecord = ldapAuthenticationAndSync(username, password);
      return sysUserRecord;
    }

    if (AppConstants.AUTH_TYPE_LOCAL == sysUserRecord.getAuthType() && localAuthentication(
        sysUserRecord, password)) {
      //Local password check
      return sysUserRecord;
    } else if (AppConstants.AUTH_TYPE_LDAP == sysUserRecord.getAuthType() && ldapAuthentication(
        sysUserRecord, password)) {
      //LDAP validation
      return sysUserRecord;
    } else {
      logger.error("Authentication Failed! username=" + username);
    }
    return null;


  }

  /**
   * Generate security password in MD5.
   */
  public String generateSecurityPassword(String username, String password) {
    return DigestUtils.md5Hex(userPasswordSalt + username + password);
  }

  /**
   * Authentication with password stored in database.
   */
  private boolean localAuthentication(SysUserRecord sysUserRecord, String password) {
    String md5Password = generateSecurityPassword(sysUserRecord.getUsername(), password);
    return md5Password.equals(sysUserRecord.getPassword());
  }

  /**
   * Authentication with LDAP.
   */
  private boolean ldapAuthentication(SysUserRecord sysUserRecord, String password) {
    if (!ldapEnable) {
      return false;
    }
    try {
      ldapTemplate.authenticate(
          LdapQueryBuilder.query().where(ldapQuery).is(sysUserRecord.getUsername()),
          password);
      return true;
    } catch (Exception ex) {
      ex.printStackTrace();
      logger.debug("LDAP Authentication Failed! " + ex.getMessage());
    }
    return false;
  }

  /**
   * Check jwt token, get authentication user info.
   */
  public AuthUser checkJwtToken(String token) {
    final Claims claims = Jwts.parser().setSigningKey(jwtKey).parseClaimsJws(token).getBody();
    if (null == claims || !claims.containsKey(AppConstants.JWT_TOKEN)) {
      return null;
    }
    String jsonInfo = (String) claims.get(AppConstants.JWT_TOKEN);
    AuthUser authUser = StringUtil.jsonToObject(jsonInfo, AuthUser.class);
    if (validateAuthUser(authUser)) {
      return authUser;
    }
    return null;
  }

  /**
   * Validate user token, if the user's roles or info have been changed, the validationToken will
   * change.
   */
  public boolean validateAuthUser(AuthUser authUser) {
    String key = AppConstants.USER_VALIDATION_TOKEN_PREFIX + authUser.getUid();
    String token = valueOperations.get(key);
    logger
        .debug("Validate token: {}, expect: {}, actual: {}", key, authUser.getValidationToken(),
            token);
    if (!Strings.isNullOrEmpty(token) && token.equals(authUser.getValidationToken())) {
      return true;
    }
    logger.warn("Validate token failed! {} expect: {}, actual: {}", key,
        authUser.getValidationToken(), token);
    return false;
  }

  /**
   * LDAP user info transformation.
   */
  private static class SysUserInfoContextMapper implements ContextMapper<SysUserRecord> {

    public SysUserRecord mapFromContext(Object ctx) {
      DirContextAdapter context = (DirContextAdapter) ctx;
      SysUserRecord sysUserRecord = new SysUserRecord();
      sysUserRecord.setFullname(context.getStringAttribute("displayName"));
      sysUserRecord.setAuthType(AppConstants.AUTH_TYPE_LDAP);
      sysUserRecord.setEmail(context.getStringAttribute("mail"));
      sysUserRecord.setPhoneNumber(context.getStringAttribute("telephoneNumber"));
      return sysUserRecord;
    }
  }

  /**
   * LDAP authentication and get ldap user info to store to database.
   */
  private SysUserRecord ldapAuthenticationAndSync(String username, String password) {
    if (!ldapEnable) {
      return null;
    }
    try {
      LdapQuery query = query().where(ldapQuery).is(username);
      logger.debug("LDAP query: " + query.toString());
      ldapTemplate.authenticate(query, password);
      logger.info("LDAP authentication successfully! Start to sync user info: " + username);

      SysUserRecord ldapUser =
          ldapTemplate.searchForObject(query, new SysUserInfoContextMapper());

      //Set user info from ldap
      SysUserRecord sysUserRecord = dsl.newRecord(Tables.SYS_USER);
      sysUserRecord.setFullname(ldapUser.getFullname());
      sysUserRecord.setAuthType(ldapUser.getAuthType());
      sysUserRecord.setEmail(ldapUser.getEmail());
      sysUserRecord.setPhoneNumber(ldapUser.getPhoneNumber());

      //Set user default info
      sysUserRecord.setUsername(username);
      sysUserRecord.setPassword("");
      sysUserRecord.setStatus(AppConstants.USER_STATUS_VALID);
      sysUserRecord.setCreatedAt(TimeUtil.getCurrentTimestamp());
      sysUserRecord.setUpdatedAt(TimeUtil.getCurrentTimestamp());
      sysUserRecord.store();

      //Create user role, set user default role.
      SysUserRoleRecord sysUserRoleRecord = dsl.newRecord(Tables.SYS_USER_ROLE);
      sysUserRoleRecord.setUserId(sysUserRecord.getId());
      sysUserRoleRecord.setRoleId(AppConstants.SYS_ROLE_USER);
      sysUserRoleRecord.setCreatedAt(TimeUtil.getCurrentTimestamp());
      sysUserRoleRecord.setUpdatedAt(TimeUtil.getCurrentTimestamp());
      sysUserRoleRecord.store();
      return sysUserRecord;
    } catch (Exception ex) {
      ex.printStackTrace();
      logger.debug("LDAP Authentication Failed! " + ex.getMessage());
    }
    return null;
  }

  /**
   * Update user token to redis.
   */
  public String updateUserToken(int userId, boolean force) {
    String token = "" + TimeUtil.getUnixTime();
    String key = AppConstants.USER_VALIDATION_TOKEN_PREFIX + userId;
    logger.debug("Update user({}) token: {}", userId, token);
    if (force) {
      valueOperations.set(key, token);
      return token;
    } else {
      valueOperations.setIfAbsent(key, token);
      return valueOperations.get(key);
    }
  }
}
