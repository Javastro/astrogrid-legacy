/*
 * @(#)WorkflowException.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow;

import org.astrogrid.AstroGridException;
import org.astrogrid.i18n.*;

/**
 * The <code>WorkflowException</code> class represents... 
 * <p>
 *
 * <p>
 * The class... 
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 08-Sep-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.3
 */
public class WorkflowException extends AstroGridException {

  public WorkflowException(AstroGridMessage message) {
    super(message);
  }

  public WorkflowException(AstroGridMessage message, Exception exception) {
    super(message, exception);
  }

}
