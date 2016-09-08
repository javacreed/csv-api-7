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
public class DefaultDataColumnFormatterProvider implements DataColumnFormatterProvider {

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
      return new DefaultDataColumnFormatterProvider(defaultFormatter, list);
    }

    public Builder defaultFormatter(final DataColumnFormatter defaultFormatter) throws NullPointerException {
      this.defaultFormatter = Objects.requireNonNull(defaultFormatter);
      return this;
    }

    public Builder register(final Class<?> type, final DataColumnFormatter columnFormatter) {
      typeColumnFormatterProviderBuilder.register(type, columnFormatter);
      return this;
    }

    public Builder register(final DataColumnFormatter columnFormatter, final int... columns) {
      for (final int column : columns) {
        indexColumnFormatterProviderBuilder.register(column, columnFormatter);
      }
      return this;
    }

    public Builder register(final OrderedDataColumnFormatterProvider columnFormatterProvider) {
      list.add(Objects.requireNonNull(columnFormatterProvider));
      return this;
    }
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
