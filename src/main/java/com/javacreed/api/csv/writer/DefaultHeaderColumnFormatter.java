package com.javacreed.api.csv.writer;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

@Immutable
@ThreadSafe
public class DefaultHeaderColumnFormatter implements HeaderColumnFormatter {

  public static final HeaderColumnFormatter INSTANCE = new DefaultHeaderColumnFormatter();

  private DefaultHeaderColumnFormatter() {}

  @Override
  public String format(final int columnIndex, final String value) {
    return value.toString();
  }
}