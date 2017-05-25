package com.beyondops.admin.service;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * AuthenticationService Tester.
 * <p>
 * Created by caiqinzhou@gmail.com.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthenticationServiceTest {

  private static final Logger logger = LoggerFactory.getLogger(AuthenticationServiceTest.class);

  @Autowired
  AuthenticationService authenticationService;

  @Before
  public void before() throws Exception {
  }

  @After
  public void after() throws Exception {
  }

  /**
   * Method: authenticaton(String username, String password)
   */
  @Test
  public void testAuthentication() throws Exception {
    //TODO: Test goes here...
    String username = "admin";
    String password = "beyondops";
    logger.info("Admin security password: {}",
        authenticationService.generateSecurityPassword(username, password));
  }

  /**
   * Method: generateSecurityPassword(String username, String password)
   */
  @Test
  public void testGenerateSecurityPassword() throws Exception {
    //TODO: Test goes here...
  }

  /**
   * Method: checkJwtToken(String token)
   */
  @Test
  public void testCheckJwtToken() throws Exception {
    //TODO: Test goes here...
  }

  /**
   * Method: mapFromContext(Object ctx)
   */
  @Test
  public void testMapFromContext() throws Exception {
    //TODO: Test goes here...
  }


  /**
   * Method: localAuthentication(SysUserRecord sysUserRecord, String password)
   */
  @Test
  public void testLocalAuthentication() throws Exception {
    //TODO: Test goes here...
/* 
try { 
   Method method = AuthenticationService.getClass().getMethod("localAuthentication", SysUserRecord.class, String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
  }

  /**
   * Method: ldapAuthentication(SysUserRecord sysUserRecord, String password)
   */
  @Test
  public void testLdapAuthentication() throws Exception {
    //TODO: Test goes here...
/* 
try { 
   Method method = AuthenticationService.getClass().getMethod("ldapAuthentication", SysUserRecord.class, String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
  }

  /**
   * Method: ldapAuthenticationAndSync(String username, String password)
   */
  @Test
  public void testLdapAuthenticationAndSync() throws Exception {
    //TODO: Test goes here...
/* 
try { 
   Method method = AuthenticationService.getClass().getMethod("ldapAuthenticationAndSync", String.class, String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
  }

} 
