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
public abstract class ParserVisitorHierarchy {

  /**
   *
   * @param em Empty
   * @param visited Vector
   */
  public abstract void visitEmpty(Empty em, Vector visited);

  /**
   *
   * @param a Alternation
   * @param visited Vector
   */
  public abstract void visitAlternation(Alternation alt, Vector visited);

  /**
   *
   * @param r Repetition
   * @param visited Vector
   */
  public abstract void visitRepetition(Repetition rep, Vector visited);

  /**
   *
   * @param s Sequence
   * @param visited Vector
   */
  public abstract void visitSequence(Sequence seq, Vector visited);

  /**
   *
   * @param t Terminal
   * @param visited Vector
   */
  public abstract void visitTerminal(Terminal ter, Vector visited);
}
