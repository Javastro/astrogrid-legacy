package  org.astrogrid.datacenter.parser.tokens;

/**
 *
 * @author Pedro Contreras <mailto:p.contreras@qub.ac.uk><p>
 * @see School of Computer Science   <br>
 * The Queen's University of Belfast <br>
 * {@link http://www.cs.qub.ac.uk}
 * <p>
 * ASTROGRID Project {@link http://www.astrogrid.org}<br>
 * Data Centre Group
 *  <p>
 * This token is based on description given by the book
 * "Building parser with java"
 */

public class Token {
  protected TokenType ttype;
  protected String sval;
  protected double nval;

  //End of Stream File
  public static final TokenType TT_EOF = new TokenType("eof");

  //end Of tokens
  public static final Token EOF = new Token(TT_EOF, "", 0);

  //Tokens is a number
  public static final TokenType TT_NUMBER = new TokenType("number");

  //token is a Word
  public static final TokenType TT_WORD = new TokenType("word");

  //Token is a Symbol
  public static final TokenType TT_SYMBOL = new TokenType("symbol");

  //Token is a quote
  public static final TokenType TT_QUOTED = new TokenType("quoted");

  /**
   * This method construct a token from a flexible char
   * @param c char
   */
  public Token(char c) {
    this(TT_SYMBOL, new String(new char[] {c}), 0);
  }

  /**
   * Construct a token from a given number
   * @param nval double
   */
  public Token(double nval) {
    this(TT_NUMBER, "", nval);
  }

  /**
   * Contruct a token from a String
   * @param sval String
   */
  public Token(String sval) {
    this(TT_WORD, sval, 0);
  }

  /**
   * Contrucct a token which contains difrent types
   * @param ttype TokenType
   * @param sval String
   * @param nval double
   */
  public Token(TokenType ttype, String sval, double nval) {
    this.ttype = ttype;
    this.sval = sval;
    this.nval = nval;
  }

  /**
   *
   * @param o Object
   * @return boolean
   */
  public boolean equals(Object o) {
    if (! (o instanceof Token)) {
      return false;
    }
    Token t = (Token) o;

    if (ttype != t.ttype) {
      return false;
    }
    if (ttype == TT_NUMBER) {
      return nval == t.nval;
    }
    if (sval == null || t.sval == null) {
      return false;
    }
    return sval.equals(t.sval);
  }

  /**
   *
   * @param o Object
   * @return boolean
   */
  public boolean equalsIgnoreCase(Object o) {
    if (! (o instanceof Token)) {
      return false;
    }
    Token t = (Token) o;

    if (ttype != t.ttype) {
      return false;
    }
    if (ttype == TT_NUMBER) {
      return nval == t.nval;
    }
    if (sval == null || t.sval == null) {
      return false;
    }
    return sval.equalsIgnoreCase(t.sval);
  }

  /**
   *
   * @return boolean  true if the token is a number
   */
  public boolean isNumber() {
    return ttype == TT_NUMBER;
  }

  /**
   *
   * @return boolean
   */
  public boolean isQuotedString() {
    return ttype == TT_QUOTED;
  }

  /**
   *
   * @return boolean
   */
  public boolean isSymbol() {
    return ttype == TT_SYMBOL;
  }

  /**
   *
   * @return boolean
   */
  public boolean isWord() {
    return ttype == TT_WORD;
  }

  /**
   *
   * @return double
   */
  public double nval() {
    return nval;
  }

  /**
   *
   * @return String
   */
  public String sval() {
    return sval;
  }

  /**
   *
   * @return String
   */
  public String toString() {
    if (ttype == TT_EOF) {
      return "EOF";
    }
    return value().toString();
  }

  /**
   *
   * @return TokenType
   */
  public TokenType ttype() {
    return ttype;
  }

  /**
   *
   * @return Object
   */
  public Object value() {
    if (ttype == TT_NUMBER) {
      return new Double(nval);
    }
    if (ttype == TT_EOF) {
      return EOF;
    }
    if (sval != null) {
      return sval;
    }
    return ttype;
  }
}
