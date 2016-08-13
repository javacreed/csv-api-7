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

    public boolean isNotEmpty() {
      return false == byIndex.isEmpty();
    }

    public Builder order(final int order) {
      this.order = order;
      return this;
    }

    public Builder register(final int index, final DataColumnFormatter columnFormatter)
        throws NullPointerException, IllegalArgumentException {
      if (index < 0) {
        throw new IllegalArgumentException("The index cannot be negative");
      }
      byIndex.put(index, Objects.requireNonNull(columnFormatter));
      return this;
    }
  }

  public static final int DEFAULT_ORDER = 3;

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
