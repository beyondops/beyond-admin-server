package com.beyondops.admin.util;

import com.beyondops.admin.common.AppConstants;
import com.beyondops.admin.common.AppResponse;
import com.beyondops.admin.model.AuthUser;
import com.google.common.base.Strings;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by caiqinzhou@gmail.com on 2017/1/13.
 */
public class WebUtil {

  /**
   * Get user client ip address.
   */
  public static String getClientIp(HttpServletRequest request) {
    String ip = request.getHeader("x-forwarded-for");
    if (Strings.isNullOrEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("Proxy-Client-IP");
    }
    if (Strings.isNullOrEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getHeader("WL-Proxy-Client-IP");
    }
    if (Strings.isNullOrEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
      ip = request.getRemoteAddr();
    }
    return ip;
  }

  /**
   * Generate jwt token response.
   */
  public static AppResponse generateJwtToken(AuthUser authUser, String signingKey, int expTime) {
    AppResponse result = new AppResponse();
    result.put(AppConstants.REQUEST_AUTH_USER, authUser);

    Calendar expDate = Calendar.getInstance();
    expDate.add(Calendar.SECOND, expTime);
    String compactJws =
        Jwts.builder().claim(AppConstants.JWT_TOKEN, StringUtil.objectToJson(authUser))
            .setIssuer(authUser.getUsername()).setSubject(authUser.getUsername())
            .setExpiration(expDate.getTime()).setIssuedAt(new Date())
            .signWith(SignatureAlgorithm.HS512, signingKey).compact();
    result.put(AppConstants.JWT_TOKEN, compactJws);

    result.setOk();
    return result;
  }

  /**
   * Generate jwt token with remember me.
   *
   * @param rememberMe The total expired time = rememberMe * expired time
   */
  public static AppResponse generateJwtToken(AuthUser authUser, String signingKey, int expTime,
      int rememberMe) {
    if (rememberMe > 0) {
      expTime *= rememberMe;
    }
    return generateJwtToken(authUser, signingKey, expTime);
  }
}
