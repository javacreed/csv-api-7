package com.javacreed.api.csv.writer;

import java.util.Objects;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

@Immutable
@ThreadSafe
public class NullColumnFormatter implements DataColumnFormatter {

  public static final String DEFAULT_NULL_VALUE = "{NULL}";

  public static final DataColumnFormatter INSTANCE = new NullColumnFormatter(NullColumnFormatter.DEFAULT_NULL_VALUE);

  private final String nullEquivalent;

  public NullColumnFormatter(final String nullEquivalent) throws NullPointerException {
    this.nullEquivalent = Objects.requireNonNull(nullEquivalent);
  }

  @Override
  public String format(final int columnIndex, final Object object) {
    return nullEquivalent;
  }
}