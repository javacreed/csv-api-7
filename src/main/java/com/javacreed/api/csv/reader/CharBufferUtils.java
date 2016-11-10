/*
 * #%L
 * JavaCreed CSV API
 * %%
 * Copyright (C) 2012 - 2015 Java Creed
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
import java.nio.CharBuffer;

/**
 * Utilities methods related to {@link CharBuffer}
 *
 * @author Albert Attard
 */
public class CharBufferUtils {

  /**
   * Read from the given source and appends as much as possible into the given {@code charBuffer}. It starts writing at
   * the current {@code charBuffer}'s limit ({@link CharBuffer#limit()}) until either the source is exhausted or the
   * buffer is filled.
   * <p>
   * Note that this method makes use the {@link CharBuffer#mark()} method and thus any existing mark is replaced and
   * lost.
   *
   * @param charBuffer
   *          the char buffer to be filled
   * @param in
   *          the source (which cannot be {@code null})
   * @return the number of char values added to the buffer, or -1 if this source of characters is at its end
   * @throws NullPointerException
   *           if any of the parameters are {@code null}
   * @throws IOException
   *           if an IO error occurs while reading
   */
  public static int fill(final CharBuffer charBuffer, final Readable in) throws NullPointerException, IOException {
    /* Cannot use mark as this is used by the read method */
    final int position = charBuffer.position();
    charBuffer.position(charBuffer.limit());
    final int read = CharBufferUtils.read(charBuffer, in);
    charBuffer.position(position);
    return read;
  }

  /**
   * Read from the given source and appends as much as possible into the given {@code charBuffer}. It starts writing at
   * the current {@code charBuffer}'s position until either the source is exhausted or the buffer is filled.
   * <p>
   * Note that this method makes use the {@link CharBuffer#mark()} method and thus any existing mark is replaced and
   * lost.
   *
   * @param charBuffer
   *          the char buffer to be filled
   * @param in
   *          the source (which cannot be {@code null})
   * @return the number of char values added to the buffer, or -1 if this source of characters is at its end
   * @throws NullPointerException
   *           if any of the parameters are {@code null}
   * @throws IOException
   *           if an IO error occurs while reading
   */
  private static int read(final CharBuffer charBuffer, final Readable in) throws NullPointerException, IOException {
    /*
     * Update the buffers to accommodate as much as possible. This should be done before the mark as otherwise the mark
     * may be lost (if the buffer was read until the limit the mark will be lost).
     */
    charBuffer.limit(charBuffer.capacity());
    charBuffer.mark();
    final int read = in.read(charBuffer);
    charBuffer.reset();
    /* Set the limit to cover only the bytes read ignoring any empty spaces */
    charBuffer.limit(charBuffer.position() + Math.max(0, read));
    return read;
  }

  /**
   * Shifts the buffer by one and returns the shifted character (as a {@link String}).
   *
   * @param charBuffer
   *          the char buffer which will be shifted (which cannot be {@code null})
   * @return the shifted character (as a {@link String})
   * @throws NullPointerException
   *           if any of the given char buffer is {@code null}
   */
  public static String shift(final CharBuffer charBuffer) throws NullPointerException {
    return CharBufferUtils.shift(charBuffer, 1);
  }

  /**
   * Shifts the buffer by the given number of characters and returns the shifted characters (as a {@link String}). Note
   * that if the buffer is empty, or does not have enough characters, it will return a string padded with char 0s. The
   * returned string's length will always have the same length as the given {@code offset}.
   *
   * @param charBuffer
   *          the char buffer which will be shifted (which cannot be {@code null})
   * @param offset
   *          the number of characters to be shifted (which needs to be greater than 0)
   * @return the shifted characters (as a {@link String}) possibly padded with char 0s
   * @throws NullPointerException
   *           if any of the given char buffer is {@code null}
   * @throws IllegalArgumentException
   *           if the given offset is less than 1
   */
  public static String shift(final CharBuffer charBuffer, final int offset)
      throws NullPointerException, IllegalArgumentException {
    final int limit = charBuffer.limit();
    if (offset < 1 || offset > limit) {
      throw new IllegalArgumentException(
          "The offset value must be between 1 and the buffer's limit of " + limit + " both inclusive");
    }

    /* Read the characters that will be shifted */
    final char[] temp = new char[limit];
    charBuffer.rewind();
    charBuffer.get(temp);
    final String shiftedChars = new String(temp, 0, offset);

    /* Shift the characters */
    for (int i = offset; i < temp.length; i++) {
      temp[i - offset] = temp[i];
    }

    /* Fill the rest with 0s */
    for (int i = temp.length - offset; i < temp.length; i++) {
      temp[i] = 0;
    }

    /* Update the buffer */
    charBuffer.clear();
    charBuffer.put(temp);
    charBuffer.position(limit - offset);

    return shiftedChars;
  }

  public static String shiftRead(final CharBuffer charBuffer, final int length, final Readable in) throws IOException {
    final String shiftedChars = charBuffer.limit() == 0 ? null : CharBufferUtils.shift(charBuffer, length);
    CharBufferUtils.read(charBuffer, in);
    return shiftedChars;
  }

  public static String shiftReadRewind(final CharBuffer charBuffer, final int length, final Readable in)
      throws IOException {
    final String shiftedChars = CharBufferUtils.shiftRead(charBuffer, length, in);
    charBuffer.rewind();
    return shiftedChars;
  }

  public static String shiftReadRewind(final CharBuffer charBuffer, final Readable in) throws IOException {
    return CharBufferUtils.shiftReadRewind(charBuffer, 1, in);
  }

  /**
   * Returns {@code true} if the given {@code charBuffer} starts with the given {@code token}, {@code false} otherwise.
   * The given {@code charBuffer}'s position is not affected.
   *
   * @param charBuffer
   *          the char buffer (which cannot be {@code null})
   * @param token
   *          the criteria
   * @return {@code true} if the given {@code charBuffer} starts with the given {@code token}, {@code false} otherwise
   * @throws NullPointerException
   *           if any of the parameters are {@code null}
   */
  public static boolean startsWith(final CharBuffer charBuffer, final String token) throws NullPointerException {
    charBuffer.mark();
    try {
      for (int i = 0; i < token.length(); i++) {
        if (false == charBuffer.hasRemaining()) {
          return false;
        }

        final char cb = charBuffer.get();
        final char ct = token.charAt(i);
        if (cb != ct) {
          return false;
        }
      }

      return true;
    } finally {
      charBuffer.reset();
    }
  }

  /** Cannot create an instance of this class */
  private CharBufferUtils() {}
}
