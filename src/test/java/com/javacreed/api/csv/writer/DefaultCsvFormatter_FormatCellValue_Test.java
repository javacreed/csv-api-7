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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests the functionality of the {@link DefaultCsvFormatter#formatCellValue(int, Object)} method
 *
 * @author Albert Attard
 * @see DefaultCsvFormatter#formatCellValue(int, Object)
 */
public class DefaultCsvFormatter_FormatCellValue_Test {

  /**
   * Ensures that all defaults are properly formatted
   */
  @Test
  public void testDefaults() {
    final Map<String, String> values = new LinkedHashMap<>();
    values.put("", "");
    values.put(null, "{NULL}");
    values.put("hello,world", "hello\\,world");
    values.put("hello\tworld", "hello\\tworld");
    values.put("hello\bworld", "hello\\bworld");
    values.put("hello\fworld", "hello\\fworld");
    values.put("hello\nworld", "hello\\nworld");
    values.put("hello\rworld", "hello\\rworld");
    values.put("hello\r\nworld", "hello\\r\\nworld");
    values.put("hello\'world", "hello\\'world");
    values.put("hello\"world", "hello\\\"world");
    values.put("hello\\world", "hello\\\\world");

    final DefaultCsvFormatter formatter = DefaultCsvFormatter.DEFAULT;
    for (final Entry<String, String> entry : values.entrySet()) {
      Assert.assertEquals(entry.getValue(), formatter.formatCellValue(0, entry.getKey()));
    }
  }
}
