package  org.astrogrid.datacenter.parser.parse;

import java.util.*;

/**
 * matches its parser against a assembly.
 */

public class Repetition
    extends Parser {

  protected Parser subparser;
  protected static final int EXPWIDTH = 4;
  protected Assembler preAssembler;

  /**
   * Construct a repetition
   *
   * @param parser Parser
   */
  public Repetition(Parser parser) {
    this(parser, null);
  }

  /**
   * Construct a repetition of a parser with a given name
   * @param subparser Parser
   * @param name String
   */
  public Repetition(Parser subparser, String name) {
    super(name);
    this.subparser = subparser;
  }

  /**
   *
   * @param pvh ParserVisitor
   * @param visited Vector
   */
  public void accept(ParserVisitorHierarchy pvh, Vector visited) {
    pvh.visitRepetition(this, visited);
  }

  /**
   *
   * @return Parser
   */
  public Parser getSubparser() {
    return subparser;
  }

  /**
   *
   * @param preAssembler Assembler
   * @return Parser
   */
  public Parser setPreAssembler(Assembler preAssembler) {
    this.preAssembler = preAssembler;
    return this;
  }

  /**
   *
   * @param maxDepth int
   * @param depth int
   * @return Vector
   */
  protected Vector randomExpansion(int maxDepth, int depth) {
    Vector ve = new Vector();
    if (depth >= maxDepth) {
      return ve;
    }

    int n = (int) (EXPWIDTH * Math.random());
    for (int j = 0; j < n; j++) {
      Vector v = subparser.randomExpansion(maxDepth, depth++);
      Enumeration enu = v.elements();
      while (enu.hasMoreElements()) {
        ve.addElement(enu.nextElement());
      }
    }
    return ve;
  }

  /**
   *
   * @param inp Vector
   * @return Vector
   */
  public Vector match(Vector inp) {
    if (preAssembler != null) {
      Enumeration e = inp.elements();
      while (e.hasMoreElements()) {
        preAssembler.workOn( (Assembly) e.nextElement());
      }
    }
    Vector out = elementClone(inp);
    Vector ve = inp;
    while (!ve.isEmpty()) {
      ve = subparser.matchAndAssemble(ve);
      add(out, ve);
    }
    return out;
  }

  /**
   *
   * @param visited Vector
   * @return String
   */
  protected String unvisitedString(Vector visited) {
    return subparser.toString(visited) + "*";
  }
}
