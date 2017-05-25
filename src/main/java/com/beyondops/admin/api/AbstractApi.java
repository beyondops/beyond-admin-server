package com.beyondops.admin.api;

import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * Created by caiqinzhou@gmail.com on 2017/2/20.
 */
public class AbstractApi {

  @Autowired
  protected MessageSource messageSource;

  protected String i10n(String code, Object... args) {
    Locale locale = LocaleContextHolder.getLocale();
    String message = messageSource.getMessage(code, args, locale);
    return message;
  }
}
