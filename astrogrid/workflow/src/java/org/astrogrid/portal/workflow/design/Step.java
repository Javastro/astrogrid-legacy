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
import org.apache.log4j.Logger ;
import java.text.MessageFormat ;

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
    
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( Step.class ) ;  
    
    public static final NullTool
        NULLTOOL = new NullTool() ;
    
    private String
        name = null,
        description = null ;
    private JoinCondition
        joinCondition ;
    private Tool
        tool = NULLTOOL ;
    private Resources
        resources = null ;
    
    public Step() {
        super() ;
        if( TRACE_ENABLED ) trace( "Step() entry/exit") ;
        joinCondition = JoinCondition.ANY ; 
    }

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setJoinCondition( JoinCondition joinCondition ) {
		this.joinCondition = joinCondition;
	}
    
    public JoinCondition getJoinCondition() {
        return this.joinCondition ;
    }

	public boolean isJoinConditionTrue() {
		return joinCondition == JoinCondition.TRUE ;
	}
    
    public boolean isJoinConditionFalse() {
        return joinCondition == JoinCondition.FALSE ;
    }
    
    public boolean isJoinConditionAny() {
        return joinCondition == JoinCondition.ANY ;
    }

	public void setTool( Tool tool ) {
		this.tool = tool;
	}

	public Tool getTool() {
		return tool;
	}

	public void setResources( Resources resources ) {
		this.resources = resources;
	}
    public Resources getResources() {
        return this.resources ;
    }

	public Resources getInputResources() {
		return resources;
	}

    public String toXMLString() {
        if( TRACE_ENABLED ) trace( "Step.toXMLString() entry") ;  
          
        String 
           xmlTemplate = WorkflowDD.STEP_TEMPLATE,
           response = null ;
                                     
        try {
            
            Object []
               inserts = new Object[2] ;
            inserts[0] = this.getTool().toXMLString() ;
            inserts[1] = ( (this.resources == null)  ?  " "  :  this.resources.toXMLString() ) ;
            
            response = MessageFormat.format( response, inserts ) ;

        }
        finally {
            if( TRACE_ENABLED ) trace( "Step.toXMLString() exit") ;    
        }       
        
        return response ;        
        
    } // end toXMLString()
    

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}
    
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }
   
} // end of class Step
