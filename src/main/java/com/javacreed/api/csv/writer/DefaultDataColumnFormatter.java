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

import net.jcip.annotations.Immutable;
import net.jcip.annotations.ThreadSafe;

@Immutable
@ThreadSafe
public class DefaultDataColumnFormatter implements DataColumnFormatter {

  public static final DataColumnFormatter INSTANCE = new DefaultDataColumnFormatter();

  private DefaultDataColumnFormatter() {}

  @Override
  public String format(final int columnIndex, final Object value) {
    if (value == null) {
      return NullColumnFormatter.DEFAULT_NULL_VALUE;
    }
    return value.toString();
  }
}
