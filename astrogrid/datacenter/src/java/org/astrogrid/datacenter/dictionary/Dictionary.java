package org.astrogrid.datacenter.dictionary;

import java.util.HashMap;
import java.util.Map;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class Dictionary extends HashMap implements Map {

  /**
   * @param initialCapacity
   * @param loadFactor
   */
  public Dictionary(int initialCapacity, float loadFactor) {
    super(initialCapacity, loadFactor);
  }

  /**
   * @param initialCapacity
   */
  public Dictionary(int initialCapacity) {
    super(initialCapacity);
  }

  /**
   * 
   */
  public Dictionary() {
    super();
  }

  /**
   * @param m
   */
  public Dictionary(Map m) {
    super(m);
  }

}
