package  org.astrogrid.datacenter.parser.tokens;

import java.io.*;

public class SlashState
    extends TokenizerState {
  protected SlashStarState slashStarState = new SlashStarState();
  protected IgnoreAllState ignoreAllState = new IgnoreAllState();

  /**
   *
   * @param r PushbackReader
   * @param theSlash int
   * @param t Tokenizer
   * @throws IOException
   * @return Token
   */
  public Token nextToken(PushbackReader r, int theSlash, Tokenizer t) throws
      IOException {

    int c = r.read();
    if (c == '*') {
      return slashStarState.nextToken(r, '*', t);
    }
    if (c == '/') {
      return ignoreAllState.nextToken(r, '/', t);
    }
    if (c >= 0) {
      r.unread(c);
    }
    return new Token(Token.TT_SYMBOL, "/", 0);
  }
}
