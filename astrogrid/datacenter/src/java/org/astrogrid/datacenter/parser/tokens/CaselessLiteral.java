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
 *
 */

public class CaselessLiteral
    extends Literal {
  /**
   *
   * @param literal String
   */
  public CaselessLiteral(String literal) {
    super(literal);
  }

  /**
   *
   * @param o Object
   * @return boolean
   */
  protected boolean qualifies(Object o) {
    return literal.equalsIgnoreCase( (Token) o);
  }
}
