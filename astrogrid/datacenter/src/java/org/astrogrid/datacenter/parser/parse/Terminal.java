package  org.astrogrid.datacenter.parser.parse;

import java.util.*;

/**
 * Terminal is a final Parser
 */

public class Terminal
    extends Parser {

  protected boolean discard = false;

  /**
   * Constructs an unnamed terminal.
   */
  public Terminal() {
  }

  /**
   *
   * @param name String
   */
  public Terminal(String name) {
    super(name);
  }

  /**
   *
   * @param pv ParserVisitor
   * @param visited  Vector a collection of previously visited parsers
   */
  public void accept(ParserVisitorHierarchy pv, Vector visited) {
    pv.visitTerminal(this, visited);
  }

  /**
   *
   * @return Terminal
   */
  public Terminal discard() {
    return setDiscard(true);
  }

  /**
   *  returns a new collection of the assemblies
   * @param in Vector  a vector of assemblies
   * @return Vector  a vector of assemblies to match against
   */
  public Vector match(Vector in) {
    Vector out = new Vector();
    Enumeration e = in.elements();
    while (e.hasMoreElements()) {
      Assembly a = (Assembly) e.nextElement();
      Assembly b = matchOneAssembly(a);
      if (b != null) {
        out.addElement(b);
      }
    }
    return out;
  }

  /**
   *
   * @param in Assembly
   * @return Assembly
   */
  protected Assembly matchOneAssembly(Assembly in) {
    if (!in.hasMoreElements()) {
      return null;
    }
    if (qualifies(in.peek())) {
      Assembly out = (Assembly) in.clone();
      Object o = out.nextElement();
      if (!discard) {
        out.push(o);
      }
      return out;
    }
    return null;
  }

  /**
   *  The mechanics of matching are the same for many terminals
   * @param o Object
   * @return boolean true
   */
  protected boolean qualifies(Object o) {
    return true;
  }

  /**
   * By default, create a collection with this terminal's
   * string representation of itself. (Most subclasses
   * override this.)
   * @param maxDepth int
   * @param depth int
   * @return Vector
   */
  public Vector randomExpansion(int maxDepth, int depth) {
    Vector v = new Vector();
    v.addElement(this.toString());
    return v;
  }

  /**
   *
   * @param discard boolean  true, if this terminal should push  itself on a assembly's stack
   * @return Terminal
   */
  public Terminal setDiscard(boolean discard) {
    this.discard = discard;
    return this;
  }

  /*
   * Returns a textual description of this parser.
   */
  protected String unvisitedString(Vector visited) {
    return "any";
  }
}
