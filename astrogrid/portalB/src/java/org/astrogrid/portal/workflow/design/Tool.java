/*
 * @(#)Tool.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design;

/**
 * The <code>Tool</code> interface represents... 
 * <p>
 *
 * <p>
 * The interface... 
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 25-Aug-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.3
 */
public interface Tool {
    
    public String toXMLString() ;
    public String toJESXMLString() ;
    public String getName() ;
    public String getDescription() ;
    
    public String getToolType() ; 

}
