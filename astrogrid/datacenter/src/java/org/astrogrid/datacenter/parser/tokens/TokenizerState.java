package  org.astrogrid.datacenter.parser.tokens;

import java.io.*;

public abstract class TokenizerState {
  /**
   * Return a token from a reader
   * @param r PushbackReader a reader
   * @param c int
   * @param t Tokenizer
   * @throws IOException
   * @return Token
   */
  public abstract Token nextToken(PushbackReader r, int c, Tokenizer t) throws
      IOException;
}
