package  org.astrogrid.datacenter.parser.tokens;

import java.io.*;

public class IgnoreAllState
    extends TokenizerState {

  /**
   * Ignores everything till EOL
   * @param r PushbackReader
   * @param theSlash int
   * @param t Tokenizer
   * @throws IOException
   * @return Token
   */
  public Token nextToken(PushbackReader r, int theSlash, Tokenizer t) throws
      IOException {

    int c;
    while ( (c = r.read()) != '\n' && c != '\r' && c >= 0) {
    }
    return t.nextToken();
  }
}
