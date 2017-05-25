package com.beyondops.admin.util;

import java.util.List;

/**
 * Created by caiqinzhou@gmail.com on 2017/2/8.
 */
public class ListUtil {

  /**
   * Check if list is null or empty.
   */
  public static boolean isEmpty(List list) {
    if (null == list || list.size() < 1) {
      return true;
    }
    return false;
  }
}
