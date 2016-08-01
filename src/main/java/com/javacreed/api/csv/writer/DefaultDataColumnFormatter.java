package com.javacreed.api.csv.writer;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

@Immutable
@ThreadSafe
public class DefaultDataColumnFormatter implements DataColumnFormatter {

  public static final DataColumnFormatter INSTANCE = new DefaultDataColumnFormatter();

  private DefaultDataColumnFormatter() {}

  @Override
  public String format(final int columnIndex, final Object value) {
    if (value == null) {
      return NullColumnFormatter.DEFAULT_NULL_VALUE;
    }
    return value.toString();
  }
}