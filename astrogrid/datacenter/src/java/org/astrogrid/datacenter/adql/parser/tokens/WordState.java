package  org.astrogrid.datacenter.parser.tokens;

import java.io.*;

public class WordState
    extends TokenizerState {
  //return a Word from a reader
  protected char charbuf[] = new char[16];
  protected boolean wordChar[] = new boolean[256];
  public WordState() {
    setWordChars('a', 'z', true);
    setWordChars('A', 'Z', true);
    setWordChars('0', '9', true);
    setWordChars('-', '-', true);
    setWordChars('_', '_', true);
    setWordChars('\'', '\'', true);
    setWordChars(0xc0, 0xff, true);
  }

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
   * Return a word token from a reader
   * @param r PushbackReader
   * @param c int
   * @param t Tokenizer
   * @throws IOException
   * @return Token
   */
  public Token nextToken(PushbackReader r, int c, Tokenizer t) throws
      IOException {

    int i = 0;
    do {
      checkBufLength(i);
      charbuf[i++] = (char) c;
      c = r.read();
    }
    while (wordChar(c));

    if (c >= 0) {
      r.unread(c);
    }
    String sval = String.copyValueOf(charbuf, 0, i);
    return new Token(Token.TT_WORD, sval, 0);
  }

  /**
   *
   * @param from int
   * @param to int
   * @param b boolean
   */
  public void setWordChars(int from, int to, boolean b) {
    for (int i = from; i <= to; i++) {
      if (i >= 0 && i < wordChar.length) {
        wordChar[i] = b;
      }
    }
  }

  /**
   * Just a test of the wordChar array
   * @param c int
   * @return boolean
   */
  protected boolean wordChar(int c) {
    if (c >= 0 && c < wordChar.length) {
      return wordChar[c];
    }
    return false;
  }
}
