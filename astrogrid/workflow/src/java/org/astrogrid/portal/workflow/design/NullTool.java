/*
 * @(#)NullTool.java   1.0
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
 * The <code>NullTool</code> class represents... 
 * <p>
 *
 * <p>
 * The class... 
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 25-Aug-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.3
 */
public final class NullTool implements Tool {
    
    public static final String 
        xmlString = "<tool><nulltool/></tool>";

	/* (non-Javadoc)
	 * @see org.astrogrid.workflow.Tool#toXMLString()
	 */
	public String toXMLString() {
		return xmlString ;
	}

} // end of class NullTool
