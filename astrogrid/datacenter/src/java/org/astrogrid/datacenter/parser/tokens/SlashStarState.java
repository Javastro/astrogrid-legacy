package  org.astrogrid.datacenter.parser.tokens;

import java.io.*;

public class SlashStarState
    extends TokenizerState {
  /**
   *
   * @param r PushbackReader
   * @param theStar int
   * @param t Tokenizer
   * @throws IOException
   * @return Token
   */
  public Token nextToken(PushbackReader r, int theStar, Tokenizer t) throws
      IOException {
    int c = 0;
    int lastc = 0;
    while (c >= 0) {
      if ( (lastc == '*') && (c == '/')) {
        break;
      }
      lastc = c;
      c = r.read();
    }
    return t.nextToken();
  }
}
