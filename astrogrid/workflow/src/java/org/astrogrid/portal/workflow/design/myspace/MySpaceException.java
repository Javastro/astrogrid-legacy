/*
 * @(#)MySpaceException.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design.myspace;

import org.astrogrid.i18n.* ;
import org.astrogrid.portal.workflow.* ;

/**
 * The <code>MySpaceException</code> class represents... 
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
public class MySpaceException extends WorkflowException {
    
    public MySpaceException( AstroGridMessage message ) { 
      super( message ) ;
  }

  public MySpaceException( AstroGridMessage message, Exception exception ) {
      super( message, exception ) ;
  }

}
