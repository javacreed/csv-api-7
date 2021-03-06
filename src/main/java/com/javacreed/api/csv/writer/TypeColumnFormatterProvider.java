/*
 * #%L
 * JavaCreed CSV API
 * %%
 * Copyright (C) 2012 - 2016 Java Creed
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
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

    public boolean isNotEmpty() {
      return false == wrappers.isEmpty();
    }

    public Builder order(final int order) {
      this.order = order;
      return this;
    }

    /**
     * Registers a column formatter for the given type. This will affect not only this type but also any objects that
     * inherit from this type. When selecting the right formatter, the closest implementation is preferred over a less
     * generic one. For example, The {@link String} class implements the {@link CharSequence} interface. Therefore when
     * formatting strings, the formatter register for {@link String} type is preferred over the one for
     * {@link CharSequence}.
     *
     * @param type
     *          the object type (which cannot be {@code null})
     * @param columnFormatter
     *          the column formatter (which cannot be {@code null})
     * @return this (for method chaining)
     * @throws NullPointerException
     *           if any of the given parameters are {@code null}
     */
    public Builder register(final Class<?> type, final DataColumnFormatter columnFormatter)
        throws NullPointerException {
      wrappers.add(new Wrapper(type, columnFormatter));
      return this;
    }
  }

  private static class Wrapper {
    private final Class<?> type;
    private final DataColumnFormatter columnFormatter;

    private Wrapper(final Class<?> type, final DataColumnFormatter columnFormatter) throws NullPointerException {
      this.type = Objects.requireNonNull(type);
      this.columnFormatter = Objects.requireNonNull(columnFormatter);
    }

    private DataColumnFormatter getColumnFormatter() {
      return columnFormatter;
    }

    private Class<?> getType() {
      return type;
    }
  }

  public static final int DEFAULT_ORDER = 5;

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
        return closestWrapper.getColumnFormatter();
      }
    }

    return null;
  }

  public void register(final Class<?> type, final DataColumnFormatter columnFormatter) throws NullPointerException {
    wrappers.add(new Wrapper(type, columnFormatter));
  }
}
