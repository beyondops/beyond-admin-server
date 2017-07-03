package com.beyondops.admin.service;

import static com.beyondops.jooq.model.Tables.SYS_API;

import com.beyondops.admin.common.AppConstants;
import com.beyondops.admin.util.TimeUtil;
import com.beyondops.jooq.model.tables.records.SysApiRecord;
import io.swagger.annotations.ApiOperation;
import io.swagger.jaxrs.PATCH;
import java.lang.reflect.Method;
import java.util.Set;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import org.glassfish.jersey.server.ResourceConfig;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by caiqinzhou@gmail.com on 2017/7/4.
 */
@Service
public class SysApiService {

  private static final Logger logger = LoggerFactory.getLogger(SysUserService.class);

  @Autowired
  private DSLContext dsl;


  /**
   * Scan the resource class, add resource api into sys_api table, and add to ADMIN role.
   *
   * @param resourceConfig jersey resource config.
   */
  public void initApiResource(ResourceConfig resourceConfig) {

    Set<Class<?>> resources = resourceConfig.getConfiguration().getClasses();
    for (Class<?> resource : resources) {
      String classResourcePath = "";
      Path classPath = resource.getAnnotation(Path.class);
      if (null != classPath) {
        classResourcePath = classPath.value();
      }
      for (Method method : resource.getMethods()) {
        String resourcePath = classResourcePath;
        Path methodPath = method.getAnnotation(Path.class);
        if (null != methodPath) {
          resourcePath += methodPath.value();
        } else {
          continue;
        }
        Integer apiMethod = null;
        String methodStr = null;

        //1:POST, 2:GET, 3:PUT, 4:PATCH, 5:DELETE, 6:HEAD, 7:OPTIONS
        GET get = method.getAnnotation(GET.class);
        if (null != get) {
          methodStr = "GET";
        }
        POST post = method.getAnnotation(POST.class);
        if (null != post) {
          methodStr = "POST";
        }
        PUT put = method.getAnnotation(PUT.class);
        if (null != put) {
          methodStr = "PUT";
        }
        PATCH patch = method.getAnnotation(PATCH.class);
        if (null != patch) {
          methodStr = "PATCH";
        }
        DELETE delete = method.getAnnotation(DELETE.class);
        if (null != delete) {
          methodStr = "DELETE";
        }
        HEAD head = method.getAnnotation(HEAD.class);
        if (null != head) {
          methodStr = "HEAD";
        }
        OPTIONS options = method.getAnnotation(OPTIONS.class);
        if (null != put) {
          methodStr = "OPTIONS";
        }
        apiMethod = AppConstants.HTTP_METHOD.get(methodStr);
        if (null == apiMethod) {
          logger.error("Can not find the api method for {}", resourcePath);
          continue;
        }

        SysApiRecord sysApiRecord = dsl.selectFrom(SYS_API)
            .where(SYS_API.API_URL.eq(resourcePath).and(SYS_API.API_METHOD.eq(apiMethod)))
            .fetchOne();
        if (null == sysApiRecord) {
          sysApiRecord = dsl.newRecord(SYS_API);
          sysApiRecord.setCreatedAt(TimeUtil.getCurrentTimestamp());
        }
        sysApiRecord.setApiMethod(apiMethod);

        sysApiRecord.setApiName(method.getName());

        ApiOperation apiOperation = method.getAnnotation(ApiOperation.class);
        if (null != apiOperation) {
          sysApiRecord.setApiName(apiOperation.value());
        }
        sysApiRecord.setApiUrl(resourcePath);
        sysApiRecord.setUpdatedAt(TimeUtil.getCurrentTimestamp());
        sysApiRecord.store();

        logger.info("Api resource[{}]: {}", methodStr, resourcePath);
      }
    }

    //Add api resource to ADMIN
    dsl.execute("INSERT INTO sys_role_api (\n"
        + "  api_id,\n"
        + "  role_id,\n"
        + "  created_at,\n"
        + "  updated_at\n"
        + ")\n"
        + "  SELECT\n"
        + "    s.id,\n"
        + "    1,\n"
        + "    now(),\n"
        + "    now()\n"
        + "  FROM sys_api s\n"
        + "  WHERE NOT exists(SELECT 0\n"
        + "                   FROM sys_role_api ra\n"
        + "                   WHERE ra.role_id = 1 AND ra.api_id = s.id)");


  }


}
