package com.beyondops.admin.service;

import com.beyondops.admin.model.AuthUser;
import com.beyondops.admin.model.ResourceApi;
import com.beyondops.admin.util.ListUtil;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by caiqinzhou@gmail.com on 2017/2/8.
 */
@Service
public class AuthorizationService {

  private static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);

  private static final String REDIS_ROLE_AUTHORIZATION_KEY = "auth:role:api";
  private static final String ROLE_KEY_SEP = ":";

  private boolean loadRedisAuthorization = false;

  @Resource(name = "stringRedisTemplate")
  SetOperations<String, String> setOperations;

  @Autowired
  StringRedisTemplate stringRedisTemplate;

  @Autowired
  private DSLContext dsl;

  /**
   * Determine whether the user has authorization for the resource.
   */
  public boolean authorization(AuthUser authUser, ResourceApi resourceApi) {
    if (null == authUser || null == resourceApi || Strings.isNullOrEmpty(resourceApi.getPath())
        || null == authUser.getRoles()) {
      return false;
    }
    loadRoleAuthorizationToRedis(false);
    String authKey = null;
    for (Integer roleId : authUser.getRoles()) {
      authKey =
          roleId + ROLE_KEY_SEP + resourceApi.getMethod().ordinal() + ROLE_KEY_SEP + resourceApi
              .getPath();
      if (setOperations.isMember(REDIS_ROLE_AUTHORIZATION_KEY, authKey)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Load user authorization info into redis.
   *
   * @param reload true:force to reload, false: load if is's not exists.
   */
  public synchronized void loadRoleAuthorizationToRedis(boolean reload) {

    //Skip if it has loaded and not force to reload.
    if (!reload && loadRedisAuthorization) {
      return;
    }
    loadRedisAuthorization = true;

    //Skip if redis has loaded and not force to reload.
    if (!reload && stringRedisTemplate.hasKey(REDIS_ROLE_AUTHORIZATION_KEY)) {
      return;
    }

    //Query role's api from db.
    String sql = "SELECT\n"
        + "  sra.role_id,\n"
        + "  sa.api_method,\n"
        + "  sa.api_url\n"
        + "FROM sys_role_api sra\n"
        + "  JOIN sys_api sa ON sra.api_id = sa.id\n";
    Result<Record> results = dsl.fetch(sql);
    List<String> roleList = new ArrayList<>();
    String authKey;
    for (Record row : results) {
      authKey =
          row.getValue("role_id") + ROLE_KEY_SEP + row.getValue("api_method") + ROLE_KEY_SEP + row
              .getValue("api_url");
      roleList.add(authKey);
    }
    if (ListUtil.isEmpty(roleList)) {
      logger.error("Role api is empty!");
      return;
    }
    stringRedisTemplate.delete(REDIS_ROLE_AUTHORIZATION_KEY);
    setOperations.add(REDIS_ROLE_AUTHORIZATION_KEY, roleList.toArray(new String[0]));
  }

}
