package com.simplesdental.product.logging;

import com.simplesdental.product.auth.UserContext;
import java.util.function.BiConsumer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Slf4j
public class AppLogger {

  private static void log(BiConsumer<String, Throwable> logger, String msg, Throwable throwable,
      boolean isCritical) {
    try {
      MDC.put("userEmail", safe(UserContext.getUserEmail()));
      MDC.put("userRole", safe(UserContext.getUserRole()));
      MDC.put("userId", safe(UserContext.getUserId()));

      if (isCritical) {
        MDC.put("severity", "CRITICAL");
      }

      logger.accept(msg, throwable);

    } finally {
      MDC.remove("userId");
      MDC.remove("userEmail");
      if (isCritical) {
        MDC.remove("severity");
      }
    }
  }

  public static void debug(String msg) {
    log((m, t) -> log.debug(m), msg, null, false);
  }

  public static void info(String msg) {
    log((m, t) -> log.info(m), msg, null, false);
  }

  public static void warn(String msg) {
    log((m, t) -> log.warn(m), msg, null, false);
  }

  public static void error(String msg) {
    log((m, t) -> log.error(m), msg, null, false);
  }

  public static void error(String msg, Throwable throwable) {
    log(log::error, msg, throwable, false);
  }

  public static void critical(String msg) {
    log((m, t) -> log.error(m), msg, null, true);
  }

  public static void critical(String msg, Throwable throwable) {
    log(log::error, msg, throwable, true);
  }

  private static String safe(Object value) {
    return value == null ? "null" : value.toString();
  }
}
