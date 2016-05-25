package com.emc.logservice.server.core;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

/**
 * Unit tests for FixedByteArrayOutputStream class.
 */
public class FixedByteArrayOutputStreamTests {
    /**
     * Tests that FixedByteArrayOutputStream works as advertised.
     * @throws IOException
     */
    @Test
    public void testWrite() throws IOException {
        final int ArraySize = 100;
        final int StartOffset = 10;
        final int Length = 50;
        final int SecondHalfFillValue = 123;

        byte[] buffer = new byte[ArraySize];
        Arrays.fill(buffer, (byte) 0);

        try (FixedByteArrayOutputStream os = new FixedByteArrayOutputStream(buffer, StartOffset, Length)) {

            // First half: write 1 by 1
            for (int i = 0; i < Length / 2; i++) {
                os.write(i);
            }

            // Second half: write an entire array.
            byte[] secondHalf = new byte[Length / 2];
            Arrays.fill(secondHalf, (byte) SecondHalfFillValue);
            os.write(secondHalf);

            try {
                os.write(255);
                Assert.fail("write(byte) allowed writing beyond the end of the stream.");
            }
            catch (IOException ex) {
                //OK.
            }

            try {
                os.write(new byte[10]);
                Assert.fail("write(byte[]) allowed writing beyond the end of the stream.");
            }
            catch (IOException ex) {
                //OK.
            }
        }

        for (int i = 0; i < buffer.length; i++) {
            if (i < StartOffset || i >= StartOffset + Length) {
                Assert.assertEquals("write() wrote data outside of its allocated bounds.", 0, buffer[i]);
            }
            else {
                int streamOffset = i - StartOffset;
                if (streamOffset < Length / 2) {
                    Assert.assertEquals("Unexpected value for stream index " + streamOffset, streamOffset, buffer[i]);
                }
                else {
                    Assert.assertEquals("Unexpected value for stream index " + streamOffset, SecondHalfFillValue, buffer[i]);
                }
            }
        }
    }
}
