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

import java.util.Objects;

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

@Immutable
@ThreadSafe
public class NullColumnFormatterProvider extends AbstractColumnFormatterProvider {

  public static final int DEFAULT_ORDER = 1;

  public static final OrderedDataColumnFormatterProvider DEFAULT = new NullColumnFormatterProvider(
      NullColumnFormatterProvider.DEFAULT_ORDER, NullColumnFormatter.INSTANCE);

  private final DataColumnFormatter columnFormatter;

  public NullColumnFormatterProvider(final int order) {
    this(order, NullColumnFormatter.INSTANCE);
  }

  public NullColumnFormatterProvider(final int order, final DataColumnFormatter columnFormatter)
      throws NullPointerException {
    super(order);
    this.columnFormatter = Objects.requireNonNull(columnFormatter);
  }

  public NullColumnFormatterProvider(final int order, final String nullEquivalent) throws NullPointerException {
    this(order, new NullColumnFormatter(nullEquivalent));
  }

  public NullColumnFormatterProvider(final String nullEquivalent) throws NullPointerException {
    this(NullColumnFormatterProvider.DEFAULT_ORDER, new NullColumnFormatter(nullEquivalent));
  }

  @Override
  public DataColumnFormatter find(final int columnIndex, final Object value) {
    if (value == null) {
      return columnFormatter;
    }
    return null;
  }
}
