package  org.astrogrid.datacenter.parser.parse;

import java.util.*;

public class Alternation
    extends CollectParser {
  /**
   * Constructor
   */
  public Alternation() {
  }

  /**
   * Cconstructor
   * @param name String
   */
  public Alternation(String name) {
    super(name);
  }

  /**
   * Constructor
   * @param p Parser
   */
  public Alternation(Parser p) {
    super(p);
  }

  /**
   * constructor
   * @param p1 Parser
   * @param p2 Parser
   */
  public Alternation(Parser p1, Parser p2) {
    super(p1, p2);
  }

  /**
   * Constructor
   * @param p1 Parser
   * @param p2 Parser
   * @param p3 Parser
   */
  public Alternation(Parser p1, Parser p2, Parser p3) {
    super(p1, p2, p3);
  }

  /**
   * Constructor
   * @param p1 Parser
   * @param p2 Parser
   * @param p3 Parser
   * @param p4 Parser
   */
  public Alternation(Parser p1, Parser p2, Parser p3, Parser p4) {
    super(p1, p2, p3, p4);
  }

  /**
   * Constructor
   * @param p1 Parser
   * @param p2 Parser
   * @param p3 Parser
   * @param p4 Parser
   * @param p5 Parser
   */
  public Alternation(Parser p1, Parser p2, Parser p3, Parser p4, Parser p5) {
    super(p1, p2, p3, p4, p5);
  }

  /**
   *
   * @param pvh ParserVisitorHierarchy
   * @param visited Vector
   */
  public void accept(ParserVisitorHierarchy pvh, Vector visited) {
    pvh.visitAlternation(this, visited);
  }

  /**
   *
   * @param ve Vector
   * @return Vector
   */
  public Vector match(Vector ve) {
    Vector out = new Vector();
    Enumeration enu = subparsers.elements();
    while (enu.hasMoreElements()) {
      Parser p = (Parser) enu.nextElement();
      add(out, p.matchAndAssemble(ve));
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
    if (depth >= maxDepth) {
      return randomSettle(maxDepth, depth);
    }
    double n = (double) subparsers.size();
    int i = (int) (n * Math.random());
    Parser j = (Parser) subparsers.elementAt(i);
    return j.randomExpansion(maxDepth, depth++);
  }

  /**
   *
   * @param maxDepth int
   * @param depth int
   * @return Vector
   */
  protected Vector randomSettle(int maxDepth, int depth) {
    Vector terms = new Vector();
    Enumeration enu = subparsers.elements();
    while (enu.hasMoreElements()) {
      Parser j = (Parser) enu.nextElement();
      if (j instanceof Terminal) {
        terms.addElement(j);
      }
    }
    Vector which = terms;
    if (terms.isEmpty()) {
      which = subparsers;
    }
    double n = (double) which.size();
    int i = (int) (n * Math.random());
    Parser p = (Parser) which.elementAt(i);
    return p.randomExpansion(maxDepth, depth++);
  }

  /**
   *
   * @return String
   */
  protected String toStringSeparator() {
    return "|";
  }
}
