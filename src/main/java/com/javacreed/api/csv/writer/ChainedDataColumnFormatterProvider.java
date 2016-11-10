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
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;

@Immutable
@ThreadSafe
public class ChainedDataColumnFormatterProvider implements DataColumnFormatterProvider {

  @NotThreadSafe
  public static class Builder {
    private DataColumnFormatter defaultFormatter = DefaultDataColumnFormatter.INSTANCE;

    private final List<OrderedDataColumnFormatterProvider> list = new ArrayList<>();

    private final IndexColumnFormatterProvider.Builder indexColumnFormatterProviderBuilder = new IndexColumnFormatterProvider.Builder()
        .order(4);

    private final TypeColumnFormatterProvider.Builder typeColumnFormatterProviderBuilder = new TypeColumnFormatterProvider.Builder()
        .order(6);

    public DataColumnFormatterProvider build() {
      final List<OrderedDataColumnFormatterProvider> list = new ArrayList<>(this.list);
      if (indexColumnFormatterProviderBuilder.isNotEmpty()) {
        list.add(indexColumnFormatterProviderBuilder.build());
      }
      if (typeColumnFormatterProviderBuilder.isNotEmpty()) {
        list.add(typeColumnFormatterProviderBuilder.build());
      }
      Collections.sort(list);
      return new ChainedDataColumnFormatterProvider(defaultFormatter, list);
    }

    public Builder defaultFormatter(final DataColumnFormatter defaultFormatter) throws NullPointerException {
      this.defaultFormatter = Objects.requireNonNull(defaultFormatter);
      return this;
    }

    /**
     * Returns {@code true} if this builder is not empty, {@code false} otherwise
     *
     * @return {@code true} if this builder is not empty, {@code false}
     */
    public boolean isNotEmpty() {
      return list.isEmpty() == false;
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
      typeColumnFormatterProviderBuilder.register(type, columnFormatter);
      return this;
    }

    /**
     * Registers the given formatter at the given column indices, replacing any existing formatter at the same column.
     *
     * @param columns
     *          the column indices (and each index needs to be greater than or equal to 0)
     * @param columnFormatter
     *          the column formatter (which cannot be {@code null})
     * @return this (for method chaining)
     * @throws NullPointerException
     *           if the given column formatter is {@code null}
     * @throws IllegalArgumentException
     *           if any of the given varagrs column index are negative, that is, less than 0
     */
    public Builder register(final DataColumnFormatter columnFormatter, final int... columns) {
      for (final int column : columns) {
        indexColumnFormatterProviderBuilder.register(column, columnFormatter);
      }
      return this;
    }

    /**
     * Adds a column formatter provider to the list of providers
     *
     * @param columnFormatterProvider
     *          the column formatter provider (which cannot be {@code null})
     * @return this (for method chaining)
     * @throws NullPointerException
     *           if the given provider is {@code null}
     */
    public Builder register(final OrderedDataColumnFormatterProvider columnFormatterProvider)
        throws NullPointerException {
      list.add(Objects.requireNonNull(columnFormatterProvider));
      return this;
    }

    /**
     * Returns the number column formatter providers
     *
     * @return the number column formatter providers
     */
    public int size() {
      return list.size();
    }
  }

  public static final DataColumnFormatterProvider DEFAULT = new ChainedDataColumnFormatterProvider.Builder().build();

  private final DataColumnFormatter defaultFormatter;

  private final List<OrderedDataColumnFormatterProvider> list;

  public ChainedDataColumnFormatterProvider(final DataColumnFormatter defaultFormatter,
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
