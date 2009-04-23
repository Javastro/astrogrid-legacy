package org.astrogrid.security.myproxy;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * An output stream specialized for writing commands to a MyProxy service.
 * The stream can send general byte sequences (e.g. to send a certificate-
 * signing request) but can also send character data encoded as UTF-8, MyProxy's
 * standard encoding. MyProxy requires a specific line-ending character that
 * may not be the norm for the software platform, so a method is provided
 * that writes the correct character.
 *
 * @author Guy Rixon
 */
public class MyProxyOutputStream extends BufferedOutputStream {

  /**
   * Constructs a MyProxyOutputStream based on a given stream.
   *
   * @param o The base stream.
   */
  public MyProxyOutputStream(OutputStream o) {
    super(o);
  }

  /**
   * Writes a given string to the stream in UTF-8 encoding.
   *
   * @param s The string to be written.
   * @throws IOException If the string cannot be written.
   */
  public void write(String s) throws IOException {
    write(s.getBytes("UTF-8"));
  }

  /**
   * Writes to the stream MyProxy's specified line-ending character.
   * MyProxy ends lines in its commands with a new-line (0x0a) character,
   * even when this is not the normal line-ending for the platform.
   *
   * @throws java.io.IOException If the line-ending cannot be written.
   */
  public void endLine() throws IOException {
    write(0x0a);
  }

  /**
   * Starts a command.
   * MyProxy requires a '0' (0x30) character after the end of
   * the GSI delegation (or after the SSL handshake if GSI is not used)
   * and before a command is sent. This method flushes the stream.
   *
   * @throws IOException If the character cannot be written.
   */
  public void startCommand() throws IOException {
    write(0x30);

    // Make sure the start-of-command byte goes to the server alone in
    // its SSL record. The server discards trailing data in that record,
    // so the stream must be flushed before the command is written.
    flush();
  }

  /**
   * Write the character that ends a command.
   * MyProxy ends its commands with a nul (0x00) character.
   *
   * @throws IOException If the character cannot be written.
   */
  public void endCommand() throws IOException {
    write(0x00);
    flush();
  }

}
