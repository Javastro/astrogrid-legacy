package  org.astrogrid.datacenter.parser.tokens;

import java.io.*;
import java.util.*;

public class TokenString {
  protected Token tokens[];

  /**
   *
   * @param tokens Token[]
   */
  public TokenString(Token[] tokens) {
    this.tokens = tokens;
  }

  /**
   *
   * @param s String
   */
  public TokenString(String s) {
    this(new Tokenizer(s));
  }

  /**
   *
   * @param t Tokenizer
   */
  public TokenString(Tokenizer t) {
    Vector v = new Vector();
    try {
      while (true) {
        Token tok = t.nextToken();
        if (tok.ttype() == Token.TT_EOF) {
          break;
        }
        v.addElement(tok);
      }
      ;
    }
    catch (IOException e) {
      throw new InternalError(
          "Problem tokenizing string: " + e);
    }
    tokens = new Token[v.size()];
    v.copyInto(tokens);
  }

  /**
   *
   * @return int
   */
  public int length() {
    return tokens.length;
  }

  /**
   *
   * @param i int
   * @return Token
   */
  public Token tokenAt(int i) {
    return tokens[i];
  }

  /**
   *
   * @return String
   */
  public String toString() {
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < tokens.length; i++) {
      if (i > 0) {
        buf.append(" ");
      }
      buf.append(tokens[i]);
    }
    return buf.toString();
  }
}
