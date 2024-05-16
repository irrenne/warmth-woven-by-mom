package com.warmth.woven.by.mom.orderservice.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.RandomStringUtils;

@UtilityClass
public class OrderIdGeneratorUtil {

  public static String generateOrderId(LocalDateTime now) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddss");
    String dateTime = now.format(formatter);
    String randomString = generateRandomString(4);
    return dateTime + randomString;
  }

  private static String generateRandomString(int length) {
    return RandomStringUtils.randomAlphanumeric(length).toUpperCase();
  }
}
