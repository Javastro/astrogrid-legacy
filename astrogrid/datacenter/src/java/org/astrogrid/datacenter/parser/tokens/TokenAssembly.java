package  org.astrogrid.datacenter.parser.tokens;

import  org.astrogrid.datacenter.parser.parse.*;

public class TokenAssembly
    extends Assembly {
  protected TokenString tokenString;

  /**
   *
   * @param s String
   */
  public TokenAssembly(String s) {
    this(new TokenString(s));
  }

  /**
   *
   * @param t Tokenizer
   */
  public TokenAssembly(Tokenizer t) {
    this(new TokenString(t));
  }

  /**
   *
   * @param tokenString TokenString
   */
  public TokenAssembly(TokenString tokenString) {
    this.tokenString = tokenString;
  }

  /**
   *
   * @param delimiter String
   * @return String
   */
  public String consumed(String delimiter) {
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < elementsConsumed(); i++) {
      if (i > 0) {
        buf.append(delimiter);
      }
      buf.append(tokenString.tokenAt(i));
    }
    return buf.toString();
  }

  /**
   *
   * @return String
   */
  public String defaultDelimiter() {
    return "/";
  }

  /**
   *
   * @return int
   */
  public int length() {
    return tokenString.length();
  }

  /**
   *
   * @return Object
   */
  public Object nextElement() {
    return tokenString.tokenAt(index++);
  }

  /**
   *
   * @return Object
   */
  public Object peek() {
    if (index < length()) {
      return tokenString.tokenAt(index);
    }
    else {
      return null;
    }
  }

  /**
   *
   * @param delimiter String
   * @return String
   */
  public String remainder(String delimiter) {
    StringBuffer buf = new StringBuffer();
    for (int i = elementsConsumed();
         i < tokenString.length();
         i++) {

      if (i > elementsConsumed()) {
        buf.append(delimiter);
      }
      buf.append(tokenString.tokenAt(i));
    }
    return buf.toString();
  }
}
