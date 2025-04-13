package com.simplesdental.product.logging;


public class CriticalLevel extends Level {

  public static final int CRITICAL_INT = ERROR_INT + 100;
  public static final Level CRITICAL = new Level(CRITICAL_INT, "CRITICAL", 7);
}