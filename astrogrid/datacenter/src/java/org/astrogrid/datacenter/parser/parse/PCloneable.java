package  org.astrogrid.datacenter.parser.parse;

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

public interface PCloneable
    extends Cloneable {

  /**
   * A object that can be clonable
   * @return Object
   */
  public Object clone();
}
