package org.astrogrid.security.myproxy;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;

/**
 * An input stream specialized for reading from a Myproxy service.
 * The stream can read general byte-sequences (e.g. to read in a certificate
 * chain) but can also read and parse textual responses to commands.
 *
 * @author Guy Rixon
 */
public class MyProxyInputStream extends BufferedInputStream {

  public MyProxyInputStream(InputStream i) {
    super(i);
  }

  /**
   * Checks the status response to a command.
   * Reads one null-terminated line from the service, then checks the
   * content. If the service has sent RESPONSE=0, returns silently. If the
   * service has sent, RESPONSE=1, throws an exception containing the
   * error message from the service, if any.
   *
   * @throws IOException If the response cannot be read.
   * @throws IOException If the reponse does not follow the MyProxy format.
   * @throws GeneralSecurityException If the service rejected the command.
   */
  public void checkCommandStatus() throws GeneralSecurityException, IOException {
    System.out.println("Checking command status.");

    // Read the command response into a byte array, stopping when a zero byte
    // is read; do not include the zero in the buffer.
    int nBytesRead = 0;
    int capacity = 11;
    byte[] buffer = rebuffer(capacity, null);
    for (;;) {
      int c = read();
      if (c == -1 || c == 0) {
        break;
      }
      if (nBytesRead == capacity) {
        capacity += 128;
        buffer = rebuffer(capacity, buffer);
      }
      buffer[nBytesRead] = (byte) c;
      nBytesRead++;
    }

    // Convert the byte buffer to an array of strings, one per line in the
    // response. Assume that MyProxy uses UTF-8 encoding and separates its lines
    // with an 0x0a character.
    String text = new String(buffer, "UTF-8");
    String[] lines = text.split("\012");
    assert lines.length > 0;

    // Check that this actually looks like MyProxy.
    if (!lines[0].equals("VERSION=MYPROXYv2")) {
      throw new IOException("This isn't responding like a MyProxy service;" +
                            " expected VERSION=MYPROXYv2");
    }
    if (lines.length < 2 || !lines[1].startsWith("RESPONSE=")) {
      throw new IOException("This isn't responding like a MyProxy service;" +
                            " expected RESPONSE=...");
    }

    // The first line should carry the status code in the form RESPONSE=s
    // where s is 0 for success or 1 for failure. The subsequent lines, if any
    // should be in the form ERROR=s where s is a line of the error message.
    if (lines[1].charAt(9) != '0') {
      StringBuilder message = new StringBuilder("A MyProxy command was rejected. ");
      for (int i = 2; i < lines.length; i++) {
        message.append(lines[i]);
        message.append(' ');
      }
      throw new GeneralSecurityException(message.toString());
    }

  }

  private byte[] rebuffer(int capacity, byte[] oldBuffer) {
    assert oldBuffer == null || oldBuffer.length < capacity;
    byte[] newBuffer = new byte[capacity];
    if (oldBuffer != null) {
      for (int i = 0; i < oldBuffer.length; i++) {
        newBuffer[i] = oldBuffer[i];
      }
    }
    return newBuffer;
  }


}
