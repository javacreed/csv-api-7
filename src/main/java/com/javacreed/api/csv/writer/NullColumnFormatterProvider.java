package com.javacreed.api.csv.writer;

import java.util.Objects;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

@Immutable
@ThreadSafe
public class NullColumnFormatterProvider extends AbstractColumnFormatterProvider {

  public static final int DEFAULT_ORDER = 1;

  public static final OrderedDataColumnFormatterProvider DEFAULT = new NullColumnFormatterProvider(
      NullColumnFormatterProvider.DEFAULT_ORDER, NullColumnFormatter.INSTANCE);

  private final DataColumnFormatter columnFormater;

  public NullColumnFormatterProvider(final int order) {
    this(order, NullColumnFormatter.INSTANCE);
  }

  public NullColumnFormatterProvider(final int order, final DataColumnFormatter columnFormater)
      throws NullPointerException {
    super(order);
    this.columnFormater = Objects.requireNonNull(columnFormater);
  }

  public NullColumnFormatterProvider(final String nullEquivalent) throws NullPointerException {
    this(NullColumnFormatterProvider.DEFAULT_ORDER, new NullColumnFormatter(nullEquivalent));
  }

  @Override
  public DataColumnFormatter find(final int columnIndex, final Object value) {
    if (value == null) {
      return columnFormater;
    }
    return null;
  }
}