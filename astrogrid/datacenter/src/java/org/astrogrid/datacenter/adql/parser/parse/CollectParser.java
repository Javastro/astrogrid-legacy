package  org.astrogrid.datacenter.parser.parse;

import java.util.*;

/**
 *
 * <p>Title: SQL to ADQL Parser</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Queen's University Belfast</p>
 * @author Pedro Contreras
 * @version 1.0
 */
public abstract class CollectParser
    extends Parser {
  protected Vector subparsers = new Vector();

  /**
   * Constructor
   */
  public CollectParser() {
  }

  /**
   * Copnstructor 1
   * @param name String
   */
  public CollectParser(String name) {
    super(name);
  }

  /**
   *
   * @param p Parser
   */
  public CollectParser(Parser p) {
    subparsers.add(p); ;
  }

  /**
   *
   * @param p1 Parser
   * @param p2 Parser
   */
  public CollectParser(Parser p1, Parser p2) {
    subparsers.add(p1);
    subparsers.add(p2);
  }

  /**
   *
   * @param p1 Parser
   * @param p2 Parser
   * @param p3 Parser
   */
  public CollectParser(Parser p1, Parser p2, Parser p3) {
    subparsers.add(p1);
    subparsers.add(p2);
    subparsers.add(p3);
  }

  /**
   *
   * @param p1 Parser
   * @param p2 Parser
   * @param p3 Parser
   * @param p4 Parser
   */
  public CollectParser(Parser p1, Parser p2, Parser p3, Parser p4) {
    //
    subparsers.add(p1);
    subparsers.add(p2);
    subparsers.add(p3);
    subparsers.add(p4);
  }

  /**
   *
   * @param p1 Parser
   * @param p2 Parser
   * @param p3 Parser
   * @param p4 Parser
   * @param p5 Parser
   */
  public CollectParser(Parser p1, Parser p2, Parser p3, Parser p4, Parser p5) {
    subparsers.add(p1);
    subparsers.add(p2);
    subparsers.add(p3);
    subparsers.add(p4);
    subparsers.add(p5);
  }

  /**
   * Add more parser to the collection
   * @param e Parser
   * @return CollectParser
   */
  public CollectParser add(Parser e) {
    subparsers.addElement(e);
    return this;
  }

  /**
   *
   * @return Vector subparser
   */
  public Vector getSubparsers() {
    return subparsers;
  }

  /**
   *
   * @return String
   */
  protected abstract String toStringSeparator();

  /**
   * return a parser's description
   * @param visited Vector
   * @return String
   */
  protected String unvisitedString(Vector visited) {
    StringBuffer sb = new StringBuffer("<");
    boolean separator = false;
    Enumeration e = subparsers.elements();
    while (e.hasMoreElements()) {
      if (separator) {
        sb.append(toStringSeparator());
      }
      Parser next = (Parser) e.nextElement();
      sb.append(next.toString(visited));
      separator = true;
    }
    sb.append(">");
    return sb.toString();
  }
}
