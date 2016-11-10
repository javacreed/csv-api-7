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

/**
 * A version of {@link DataColumnFormatterProvider} which also takes a priority order. Providers with lower order value
 * are preferred over ones with higher order value.
 *
 * @author Albert Attard
 */
public interface OrderedDataColumnFormatterProvider
    extends DataColumnFormatterProvider, Comparable<OrderedDataColumnFormatterProvider> {

  /**
   * Returns the order of this formatter. Formatters with lower order values are tried before those with higher values.
   *
   * @return the order of this formatter
   */
  int getOrder();

}
