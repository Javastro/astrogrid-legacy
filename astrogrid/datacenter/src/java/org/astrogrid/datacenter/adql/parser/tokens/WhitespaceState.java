package  org.astrogrid.datacenter.parser.tokens;

import java.io.*;

public class WhitespaceState
    extends TokenizerState {
  /**
   * This clases ignores white spaces. So, blankspaces and tabs
   * are not consider.  This can be noticed when a String is decompose
   * in token and store in a Vector... such vector will not contain spaces
   * in blank when this class is used.  Which is the case of Xadql parser
   */
  protected boolean whitespaceChar[] = new boolean[256];
  public WhitespaceState() {
    setWhitespaceChars(0, ' ', true);
  }

  /**
   *
   * @param r PushbackReader
   * @param aWhitespaceChar int
   * @param t Tokenizer
   * @throws IOException
   * @return Token
   */
  public Token nextToken(PushbackReader r, int aWhitespaceChar, Tokenizer t) throws
      IOException {
    int c;
    do {
      c = r.read();
    }
    while (
        c >= 0 &&
        c < whitespaceChar.length &&
        whitespaceChar[c]);

    if (c >= 0) {
      r.unread(c);
    }
    return t.nextToken();
  }

  /**
   *
   * @param from int
   * @param to int
   * @param b boolean
   */
  public void setWhitespaceChars(int from, int to, boolean b) {
    for (int i = from; i <= to; i++) {
      if (i >= 0 && i < whitespaceChar.length) {
        whitespaceChar[i] = b;
      }
    }
  }
}
