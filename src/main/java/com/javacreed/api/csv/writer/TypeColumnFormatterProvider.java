package com.javacreed.api.csv.writer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.jcip.annotations.NotThreadSafe;

@NotThreadSafe
public class TypeColumnFormatterProvider extends AbstractColumnFormatterProvider {

  public static class Builder {

    private int order = TypeColumnFormatterProvider.DEFAULT_ORDER;

    private final List<Wrapper> wrappers = new ArrayList<>();

    public OrderedDataColumnFormatterProvider build() {
      return new TypeColumnFormatterProvider(order, wrappers);
    }

    public Builder order(final int order) {
      this.order = order;
      return this;
    }

    public Builder register(final Class<?> type, final DataColumnFormatter columnFormater)
        throws NullPointerException, IllegalArgumentException {
      wrappers.add(new Wrapper(type, columnFormater));
      return this;
    }
  }

  private static class Wrapper {
    private final Class<?> type;
    private final DataColumnFormatter columnFormater;

    public Wrapper(final Class<?> type, final DataColumnFormatter columnFormater) throws NullPointerException {
      this.type = Objects.requireNonNull(type);
      this.columnFormater = Objects.requireNonNull(columnFormater);
    }

    public DataColumnFormatter getColumnFormater() {
      return columnFormater;
    }

    public Class<?> getType() {
      return type;
    }
  }

  public static final int DEFAULT_ORDER = 3;

  private final List<Wrapper> wrappers;

  private TypeColumnFormatterProvider(final int order, final Collection<Wrapper> wrappers) {
    super(order);
    this.wrappers = Collections.unmodifiableList(new ArrayList<>(wrappers));
  }

  @Override
  public DataColumnFormatter find(final int columnIndex, final Object value) {
    if (value != null) {
      Wrapper closestWrapper = null;
      for (final Wrapper wrapper : wrappers) {
        if (wrapper.getType().isAssignableFrom(value.getClass())
            && (closestWrapper == null || closestWrapper.getType().isAssignableFrom(wrapper.getType()))) {
          closestWrapper = wrapper;
        }
      }

      if (closestWrapper != null) {
        return closestWrapper.getColumnFormater();
      }
    }

    return null;
  }

  public void register(final Class<?> type, final DataColumnFormatter columnFormater) throws NullPointerException {
    wrappers.add(new Wrapper(type, columnFormater));
  }
}