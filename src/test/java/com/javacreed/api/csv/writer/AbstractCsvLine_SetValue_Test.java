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

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.javacreed.api.csv.common.DefaultHeaders;
import com.javacreed.api.csv.common.Headers;

/**
 * Tests the {@link AbstractCsvLine#setValue(int, Object)} method with various inputs including invalid header indices
 *
 * @author Albert Attard
 * @see AbstractCsvLine#setValue(int, Object)
 */
public class AbstractCsvLine_SetValue_Test {

  private static class TestCsvLine extends AbstractCsvLine {
    public TestCsvLine(final Headers headers) throws NullPointerException {
      super(headers);
    }

    @Override
    public void write() throws CsvWriteException {}
  }

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testInvalidHeaderIndexLowerBound() {
    thrown.expect(CsvHeadersIndexOutOfBoundsException.class);
    thrown.expectMessage("The index -1 is out of range [0,1)");

    final CsvLine line = new TestCsvLine(new DefaultHeaders(1));
    line.setValue(-1, "");
  }

  @Test
  public void testInvalidHeaderIndexUpperBound() {
    thrown.expect(CsvHeadersIndexOutOfBoundsException.class);
    thrown.expectMessage("The index 1 is out of range [0,1)");

    final CsvLine line = new TestCsvLine(new DefaultHeaders(1));
    line.setValue(1, "");
  }

  @Test
  public void testValidHeaderIndex() {
    final CsvLine line = new TestCsvLine(new DefaultHeaders(1));
    line.setValue(0, "");
  }
}
