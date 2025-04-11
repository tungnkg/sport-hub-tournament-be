package com.example.billiard_management_be.shared.utils;


import java.util.HashMap;
import java.lang.reflect.Field;
import java.util.Map;

public class ObjectUtils {
  public static Map<String, Object> convertUsingReflection(Object object) throws IllegalAccessException {
    Map<String, Object> map = new HashMap<>();
    Field[] fields = object.getClass().getDeclaredFields();

    for (Field field : fields) {
      field.setAccessible(true);
      map.put(field.getName(), field.get(object));
    }

    return map;
  }
}
