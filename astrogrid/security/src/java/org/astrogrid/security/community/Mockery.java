/*
 * Copyright (c) 2004-2008 AstroGrid. All rights reserved.
 */

package org.astrogrid.security.community;

import org.astrogrid.config.SimpleConfig;

/**
 * A base class to ease testing with mock objects. Instances of this class
 * may call isMock() to find out if they should be using mock objects. The
 * testing evironment sets configuration key to true to indicate the use of
 * mock objects. The key is the fully-qualified name of the tested class with
 * the suffix .mock, e.g. "org.astrogrid.security.community.Foo.mock".
 *
 * @author Guy Rixon
 */
public class Mockery {

  /**
   * Determines whether mock objects should be used for unit testing.
   *
   * @return True iff mock objects should be used.
   */
  protected boolean isMock() {
    String key = this.getClass().getName() + ".mock";
    System.out.println(key + " = " + SimpleConfig.getSingleton().getProperty(key, "(unknown)"));
    return SimpleConfig.getSingleton().getBoolean(key, false);
  }
 
}
