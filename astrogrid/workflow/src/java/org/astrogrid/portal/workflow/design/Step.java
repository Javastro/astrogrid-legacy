/*
 * @(#)Step.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design;

import org.astrogrid.portal.workflow.design.activity.*;
import org.astrogrid.workflow.design.activity.*;

/**
 * The <code>Step</code> class represents... 
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
public class Step extends Activity {
    
    public static final NullTool
        NULLTOOL = new NullTool() ;
    
    private String
        name = null ;
    private boolean
        joinCondition = true ;
    private Tool
        tool = NULLTOOL ;
    private Resources
        inputResources = null ,
        outputResources = null ;
    
    public Step() {}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setJoinCondition( boolean joinCondition ) {
		this.joinCondition = joinCondition;
	}

	public boolean isJoinCondition() {
		return joinCondition;
	}

	public void setTool( Tool tool ) {
		this.tool = tool;
	}

	public Tool getTool() {
		return tool;
	}

	public void setInputResources( Resources inputResources ) {
		this.inputResources = inputResources;
	}

	public Resources getInputResources() {
		return inputResources;
	}

	public void setOutputResources( Resources outputResources ) {
		this.outputResources = outputResources;
	}

	public Resources getOutputResources() {
		return outputResources;
	}
    
    public String toXMLString() {
        return null ;
    }
   
} // end of class Step
