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
package com.javacreed.api.csv.reader;

import java.io.IOException;
import java.io.StringReader;
import java.nio.CharBuffer;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test the {@link CharBufferUtils#fill(CharBuffer, Readable)} method
 *
 * @author Albert Attard
 * @see CharBufferUtils#fill(CharBuffer, Readable)
 */
public class CharBufferUtils_Fill_Test {

  /**
   * Test the {@link CharBufferUtils#fill(CharBuffer, Readable)} method by filling the buffer in chunks
   *
   * @throws IOException
   *           if an error occurs
   */
  @Test
  public void testReadTheStringInParts() throws IOException {
    final Readable readable = new StringReader("test string");

    final CharBuffer charBuffer = CharBuffer.allocate(4);
    /* Set the limit to 0 so it starts writing at the beginning of the buffer */
    charBuffer.limit(0);

    Assert.assertEquals(4, CharBufferUtils.fill(charBuffer, readable));
    Assert.assertEquals(0, charBuffer.position());
    Assert.assertEquals(4, charBuffer.length());
    Assert.assertEquals(4, charBuffer.limit());
    Assert.assertEquals(CharBuffer.wrap("test"), charBuffer);

    charBuffer.limit(0);
    Assert.assertEquals(4, CharBufferUtils.fill(charBuffer, readable));
    Assert.assertEquals(0, charBuffer.position());
    Assert.assertEquals(4, charBuffer.length());
    Assert.assertEquals(4, charBuffer.limit());
    Assert.assertEquals(CharBuffer.wrap(" str"), charBuffer);

    charBuffer.limit(0);
    Assert.assertEquals(3, CharBufferUtils.fill(charBuffer, readable));
    Assert.assertEquals(0, charBuffer.position());
    Assert.assertEquals(3, charBuffer.length());
    Assert.assertEquals(3, charBuffer.limit());
    Assert.assertEquals(CharBuffer.wrap("ing"), charBuffer);
  }

  /**
   * Test the {@link CharBufferUtils#fill(CharBuffer, Readable)} method by filling it completely in one read
   *
   * @throws IOException
   *           if an error occurs
   */
  @Test
  public void testReadTheWholeString() throws IOException {
    final Readable readable = new StringReader("test string");

    final CharBuffer charBuffer = CharBuffer.allocate(11);
    /* Set the limit to 0 so it starts writing at the beginning of the buffer */
    charBuffer.limit(0);

    Assert.assertEquals(11, CharBufferUtils.fill(charBuffer, readable));
    Assert.assertEquals(0, charBuffer.position());
    Assert.assertEquals(11, charBuffer.length());
    Assert.assertEquals(11, charBuffer.limit());
    Assert.assertEquals(CharBuffer.wrap("test string"), charBuffer);
  }
}
