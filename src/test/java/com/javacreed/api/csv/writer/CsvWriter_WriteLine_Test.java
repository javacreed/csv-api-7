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

import java.io.IOException;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.Assert;
import org.junit.Test;

public class CsvWriter_WriteLine_Test extends EasyMockSupport {

  @Test
  public void testClearAfterFailure() throws IOException {
    final Appendable appendable = createStrictMock(Appendable.class);

    /* This is the first line where the letter 'a' is written */
    EasyMock.expect(appendable.append(EasyMock.eq("a"))).andReturn(appendable);
    EasyMock.expect(appendable.append(EasyMock.eq("\n"))).andThrow(new IOException());

    /*
     * In the second line nothing is written (or better the value is not set, thus null) and this should be indicated by
     * '{NULL}'
     */
    EasyMock.expect(appendable.append(EasyMock.eq("{NULL}"))).andReturn(appendable);
    EasyMock.expect(appendable.append(EasyMock.eq("\n"))).andReturn(appendable);

    replayAll();

    try (CsvWriter csv = new CsvWriter(appendable)) {
      csv.columns(1);

      /* Write a value */
      CsvLine line = csv.line();
      line.setValue(0, "a");
      try {
        line.write();
        Assert.fail("An exception was expected to be thrown");
      } catch (final Exception e) {}

      /* Simply create a line without writing anything. This should produce a null value. */
      line = csv.line();
      line.write();
    }

    verifyAll();
  }

  /**
   * Make sure that a value is clear after this is written and that it does not stick around for the next line.
   *
   * @throws IOException
   *           if an error occurs while writing
   */
  @Test
  public void testClearAfterWrite() throws IOException {
    final Appendable appendable = createStrictMock(Appendable.class);

    /* This is the first line where the letter 'a' is written */
    EasyMock.expect(appendable.append(EasyMock.eq("a"))).andReturn(appendable);
    EasyMock.expect(appendable.append(EasyMock.eq("\n"))).andReturn(appendable);

    /*
     * In the second line nothing is written (or better the value is not set, thus null) and this should be indicated by
     * '{NULL}'
     */
    EasyMock.expect(appendable.append(EasyMock.eq("{NULL}"))).andReturn(appendable);
    EasyMock.expect(appendable.append(EasyMock.eq("\n"))).andReturn(appendable);

    replayAll();

    try (CsvWriter csv = new CsvWriter(appendable)) {
      csv.columns(1);

      /* Write a value */
      CsvLine line = csv.line();
      line.setValue(0, "a");
      line.write();

      /* Simply create a line without writing anything. This should produce a null value. */
      line = csv.line();
      line.write();
    }

    verifyAll();
  }

  @Test
  public void testWriteOneLine() throws IOException {
    final Appendable appendable = createStrictMock(Appendable.class);

    /* This is the first line where the letter 'a' is written */
    EasyMock.expect(appendable.append(EasyMock.eq("a"))).andReturn(appendable);
    EasyMock.expect(appendable.append(EasyMock.eq("\n"))).andReturn(appendable);

    replayAll();

    try (CsvWriter writer = new CsvWriter(appendable)) {
      writer.columns(1);

      /* Write a value */
      final CsvLine line = writer.line();
      line.setValue(0, "a");
      line.write();
    }

    verifyAll();
  }
}
