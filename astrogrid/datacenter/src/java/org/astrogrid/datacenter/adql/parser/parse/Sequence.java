package  org.astrogrid.datacenter.parser.parse;

import java.util.*;

/**
 * Sequence object is a collection of parsers
 */

public class Sequence
    extends CollectParser {

  //constructor
  public Sequence() {
  }

  /**
   * Constructs a sequence
   * @param name String
   */
  public Sequence(String name) {
    super(name);
  }

  /**
   * Construct a CollectParser
   * @param p Parser
   */
  public Sequence(Parser p) {
    super(p);
  }

  /**
   * Construct a CollectParser with 2 parser's names
   * @param p1 Parser
   * @param p2 Parser
   */
  public Sequence(Parser p1, Parser p2) {
    super(p1, p2);
  }

  /**
   * Construct a CollectParser with 3 parser's names
   * @param p1 Parser
   * @param p2 Parser
   * @param p3 Parser
   */
  public Sequence(Parser p1, Parser p2, Parser p3) {
    super(p1, p2, p3);
  }

  /**
   * Construct a CollectParser with 4 parser's names
   * @param p1 Parser
   * @param p2 Parser
   * @param p3 Parser
   * @param p4 Parser
   */
  public Sequence(Parser p1, Parser p2, Parser p3, Parser p4) {
    super(p1, p2, p3, p4);
  }

  /**
   * Construct a CollectParser with 5 parser's names
   * @param p1 Parser
   * @param p2 Parser
   * @param p3 Parser
   * @param p4 Parser
   * @param p5 Parser
   */
  public Sequence(Parser p1, Parser p2, Parser p3, Parser p4, Parser p5) {
    super(p1, p2, p3, p4, p5);
  }

  /**
   *
   * @param pv ParserVisitor
   * @param visited Vector visited parsers
   */
  public void accept(ParserVisitorHierarchy pvh, Vector visited) {
    pvh.visitSequence(this, visited);
  }

  /**
   *
   * @param inp Vector
   * @return Vector vector of assemblies
   */
  public Vector match(Vector inp) {
    Vector out = inp;
    Enumeration elem = subparsers.elements();
    while (elem.hasMoreElements()) {
      Parser p = (Parser) elem.nextElement();
      out = p.matchAndAssemble(out);
      if (out.isEmpty()) {
        return out;
      }
    }
    return out;
  }

  /**
   *
   * @param maxDepth int
   * @param depth int
   * @return Vector
   */
  protected Vector randomExpansion(int maxDepth, int depth) {
    Vector v = new Vector();
    Enumeration enu = subparsers.elements();
    while (enu.hasMoreElements()) {
      Parser p = (Parser) enu.nextElement();
      Vector w = p.randomExpansion(maxDepth, depth++);
      Enumeration f = w.elements();
      while (f.hasMoreElements()) {
        v.addElement(f.nextElement());
      }
    }
    return v;
  }

  /**
   *
   * @return String this parser is a sequence of
   */
  protected String toStringSeparator() {
    return "";
  }
}
