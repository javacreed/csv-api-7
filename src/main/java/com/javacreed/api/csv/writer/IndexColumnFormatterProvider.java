package com.javacreed.api.csv.writer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;

@Immutable
@ThreadSafe
public class IndexColumnFormatterProvider extends AbstractColumnFormatterProvider {

  @NotThreadSafe
  public static class Builder {

    private int order = IndexColumnFormatterProvider.DEFAULT_ORDER;

    private final Map<Integer, DataColumnFormatter> byIndex = new HashMap<>();

    public OrderedDataColumnFormatterProvider build() {
      return new IndexColumnFormatterProvider(order, byIndex);
    }

    public Builder order(final int order) {
      this.order = order;
      return this;
    }

    public Builder register(final int index, final DataColumnFormatter columnFormater)
        throws NullPointerException, IllegalArgumentException {
      if (index < 0) {
        throw new IllegalArgumentException("The index cannot be negative");
      }
      byIndex.put(index, Objects.requireNonNull(columnFormater));
      return this;
    }
  }

  public static final int DEFAULT_ORDER = 2;

  private final Map<Integer, DataColumnFormatter> byIndex;

  public IndexColumnFormatterProvider(final int order, final Map<Integer, DataColumnFormatter> byIndex) {
    super(order);
    this.byIndex = Collections.unmodifiableMap(new HashMap<>(byIndex));
  }

  @Override
  public DataColumnFormatter find(final int columnIndex, final Object value) {
    return byIndex.get(columnIndex);
  }
}