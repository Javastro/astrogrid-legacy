package  org.astrogrid.datacenter.parser.parse;

import java.util.*;

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
 * This parser is based on description given by the book
 * "Building parser with java"
 */

public abstract class Parser {
  protected String name;
  protected Assembler assembler;

  /**
   * constructor
   */
  public Parser() {
  }

  /**
   * Constructor
   * @param name String
   */
  public Parser(String name) {
    this.name = name;
  }

  /**
   *
   * @param pvh ParserVisitorHierarchy
   */
  public void accept(ParserVisitorHierarchy pvh) {
    accept(pvh, new Vector());
  }

  /**
   *
   * @param pv ParserVisitorHierarchy
   * @param visited Vector
   */
  public abstract void accept(ParserVisitorHierarchy pv, Vector visited);

  /**
   *
   * @param v1 Vector
   * @param v2 Vector
   */
  public static void add(Vector v1, Vector v2) {
    Enumeration e = v2.elements();
    while (e.hasMoreElements()) {
      v1.addElement(e.nextElement());
    }
  }

  /**
   *
   * @param v Vector
   * @return Assembly
   */
  public Assembly best(Vector v) {
    Assembly best = null;
    Enumeration e = v.elements();
    while (e.hasMoreElements()) {
      Assembly a = (Assembly) e.nextElement();
      if (!a.hasMoreElements()) {
        return a;
      }
      if (best == null) {
        best = a;
      }
      else
      if (a.elementsConsumed() >
          best.elementsConsumed()) {

        best = a;
      }
    }
    return best;
  }

  /**
   *
   * @param a Assembly
   * @return Assembly
   */
  public Assembly bestMatch(Assembly a) {
    Vector in = new Vector();
    in.addElement(a);
    Vector out = matchAndAssemble(in);
    return best(out);
  }

  /**
   *
   * @param a Assembly
   * @return Assembly
   */
  public Assembly completeMatch(Assembly a) {
    Assembly best = bestMatch(a);
    if (best != null && !best.hasMoreElements()) {
      return best;
    }
    return null;
  }

  /**
   *
   * @param v Vector
   * @return Vector
   */
  public static Vector elementClone(Vector v) {
    Vector copy = new Vector();
    Enumeration e = v.elements();
    while (e.hasMoreElements()) {
      Assembly a = (Assembly) e.nextElement();
      copy.addElement(a.clone());
    }
    return copy;
  }

  /**
   *
   * @return String parser's Name
   */
  public String getName() {
    return name;
  }

  /**
   *
   * @param in Vector
   * @return Vector
   */
  public abstract Vector match(Vector in);

  /**
   *
   * @param in Vector
   * @return Vector
   */
  public Vector matchAndAssemble(Vector in) {
    Vector out = match(in);
    if (assembler != null) {
      Enumeration e = out.elements();
      while (e.hasMoreElements()) {
        assembler.workOn( (Assembly) e.nextElement());
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
  protected abstract Vector randomExpansion(int maxDepth, int depth);

  /**
   *
   * @param maxDepth int
   * @param separator String
   * @return String
   */
  public String randomInput(int maxDepth, String separator) {
    StringBuffer buf = new StringBuffer();
    Enumeration e = randomExpansion(maxDepth, 0).elements();
    boolean first = true;
    while (e.hasMoreElements()) {
      if (!first) {
        buf.append(separator);
      }
      buf.append(e.nextElement());
      first = false;
    }
    return buf.toString();
  }

  /**
   *
   * @param assembler Assembler
   * @return Parser
   */
  public Parser setAssembler(Assembler assembler) {
    this.assembler = assembler;
    return this;
  }

  /**
   *
   * @return String  return description of the parser
   */
  public String toString() {
    return toString(new Vector());
  }

  /**
   *
   * @param visited Vector
   * @return String
   */
  protected String toString(Vector visited) {
    if (name != null) {
      return name;
    }
    else if (visited.contains(this)) {
      return "...";
    }
    else {
      visited.addElement(this);
      return unvisitedString(visited);
    }
  }

  /**
   *
   * @param visited Vector
   * @return String
   */
  protected abstract String unvisitedString(Vector visited);
}
