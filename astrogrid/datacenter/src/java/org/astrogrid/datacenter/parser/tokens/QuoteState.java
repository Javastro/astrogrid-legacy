package  org.astrogrid.datacenter.parser.tokens;

import java.io.*;

public class QuoteState
    extends TokenizerState {
  protected char charbuf[] = new char[16];

  /**
   *
   * @param i int
   */
  protected void checkBufLength(int i) {
    if (i >= charbuf.length) {
      char nb[] = new char[charbuf.length * 2];
      System.arraycopy(charbuf, 0, nb, 0, charbuf.length);
      charbuf = nb;
    }
  }

  /**
   *
   * @param r PushbackReader
   * @param cin int
   * @param t Tokenizer
   * @throws IOException
   * @return Token
   */
  public Token nextToken(
      PushbackReader r, int cin, Tokenizer t) throws IOException {

    int i = 0;
    charbuf[i++] = (char) cin;
    int c;
    do {
      c = r.read();
      if (c < 0) {
        c = cin;
      }
      checkBufLength(i);
      charbuf[i++] = (char) c;
    }
    while (c != cin);
    String sval = String.copyValueOf(charbuf, 0, i);
    return new Token(Token.TT_QUOTED, sval, 0);
  }
}
