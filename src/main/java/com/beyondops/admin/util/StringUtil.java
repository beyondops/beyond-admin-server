package com.beyondops.admin.util;

import com.google.gson.Gson;
import java.util.Map;
import org.yaml.snakeyaml.Yaml;

/**
 * String tools.
 */
public class StringUtil {

  /**
   * Transform yaml to json.
   */
  public static String yamlToJson(String yamlStr) {
    Yaml yaml = new Yaml();
    Map<String, Object> jsonObj = (Map<String, Object>) yaml.load(yamlStr);
    return new Gson().toJson(jsonObj);
  }

  /**
   * Transform json to map.
   */
  public static Map fromJsonToMap(String json) {
    try {
      Gson gson = new Gson();
      Map map = gson.fromJson(json, Map.class);
      return map;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return null;
  }

  /**
   * Transform json to object.
   */
  public static <T> T jsonToObject(String json, Class<T> objClass) {
    Gson gson = new Gson();
    return gson.fromJson(json, objClass);
  }

  /**
   * Transform object to json.
   */
  public static String objectToJson(Object object) {
    Gson gson = new Gson();
    return gson.toJson(object);
  }
}
