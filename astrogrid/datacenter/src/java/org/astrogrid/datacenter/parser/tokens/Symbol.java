package  org.astrogrid.datacenter.parser.tokens;

import java.util.*;

import  org.astrogrid.datacenter.parser.parse.*;

public class Symbol
    extends Terminal {
  //the literal to match
  protected Token symbol;

  /**
   * Constructor
   * @param c char
   */
  public Symbol(char c) {
    this(String.valueOf(c));
  }

  /**
   * Constructs a symbol that will match the specified sequence
   * of characters.
   * @param s String
   */
  public Symbol(String s) {
    symbol = new Token(Token.TT_SYMBOL, s, 0);
  }

  /**
   * Returns true if the symbol this object represents equals an
   * assembly's next element
   * @param o Object
   * @return boolean
   */
  protected boolean qualifies(Object o) {
    return symbol.equals( (Token) o);
  }

  /**
   * Returns a textual description of this parser
   * @param visited Vector
   * @return String
   */
  public String unvisitedString(Vector visited) {
    return symbol.toString();
  }
}
