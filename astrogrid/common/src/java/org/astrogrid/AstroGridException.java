/*
 * @(#)AstroGridException.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid;

import org.astrogrid.i18n.AstroGridMessage;

/**
 * Generic astrogrid exception
 * @author unknown
 *
 */
public class AstroGridException extends Exception {
  /**
   * embedded message
   */
  private AstroGridMessage message = null;

  /**
   * ctor
   * @param message message to wrap
   */
  public AstroGridException(final AstroGridMessage message) {
    this.message = message;
  }

  /**
   * ctor
   * @param message to wrap
   * @param throwable underlying exception
   */
  public AstroGridException(
    final AstroGridMessage message,
    final Throwable throwable) {
    super(throwable);
    this.message = message;
  }

  /**
   * ctor
   * @param throwable underlying exception
   */
  public AstroGridException(final Throwable throwable) {
    super(throwable);
  }

  /**
   * getter
   * @return wrapped message
   */
  public final AstroGridMessage getAstroGridMessage() {
    return message;
  }

}
