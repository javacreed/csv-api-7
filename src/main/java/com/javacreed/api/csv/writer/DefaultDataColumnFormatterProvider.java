package com.javacreed.api.csv.writer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;

@Immutable
@ThreadSafe
public class DefaultDataColumnFormatterProvider implements DataColumnFormatterProvider {

  @NotThreadSafe
  public static class Builder {
    private DataColumnFormatter defaultFormatter = DefaultDataColumnFormatter.INSTANCE;

    private final List<OrderedDataColumnFormatterProvider> list = new ArrayList<>();

    public DataColumnFormatterProvider build() {
      Collections.sort(list);
      return new DefaultDataColumnFormatterProvider(defaultFormatter, list);
    }

    public Builder defaultFormatter(final DataColumnFormatter defaultFormatter) throws NullPointerException {
      this.defaultFormatter = Objects.requireNonNull(defaultFormatter);
      return this;
    }

    // private <T extends OrderedDataColumnFormatterProvider> T find(final Class<T> type) {
    // for (final OrderedDataColumnFormatterProvider provider : list) {
    // if (type.equals(provider.getClass())) {
    // return type.cast(provider);
    // }
    // }
    //
    // return null;
    // }

    public Builder register(final OrderedDataColumnFormatterProvider columnFormatterProvider) {
      list.add(Objects.requireNonNull(columnFormatterProvider));
      return this;
    }

    // public void registerTypeFormatter(final Class<?> type, final DataColumnFormatter dataColumnFormatter) {
    // TypeColumnFormatterProvider provider = find(TypeColumnFormatterProvider.class);
    // if (provider == null) {
    // register(new TypeColumnFormatterProvider(order));
    // }
    // }
  }

  public static final DataColumnFormatterProvider DEFAULT = new DefaultDataColumnFormatterProvider.Builder().build();

  private final DataColumnFormatter defaultFormatter;

  private final List<OrderedDataColumnFormatterProvider> list;

  public DefaultDataColumnFormatterProvider(final DataColumnFormatter defaultFormatter,
      final List<OrderedDataColumnFormatterProvider> list) throws NullPointerException {
    this.defaultFormatter = Objects.requireNonNull(defaultFormatter);
    this.list = Collections.unmodifiableList(new ArrayList<>(list));
  }

  @Override
  public DataColumnFormatter find(final int columnIndex, final Object value) {
    for (final DataColumnFormatterProvider provider : list) {
      final DataColumnFormatter cellFormatter = provider.find(columnIndex, value);
      if (cellFormatter != null) {
        return cellFormatter;
      }
    }

    return defaultFormatter;
  }
}