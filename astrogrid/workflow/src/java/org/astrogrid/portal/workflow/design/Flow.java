/*
 * @(#)Flow.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.portal.workflow.design;

import java.text.MessageFormat ;
import org.apache.log4j.Logger ;
import org.astrogrid.i18n.*;
import org.astrogrid.portal.workflow.*;
import org.astrogrid.portal.workflow.design.activity.*;
import org.astrogrid.workflow.*;
import org.astrogrid.workflow.design.activity.*;

/**
 * The <code>Flow</code> class represents... 
 * <p>
 *
 * <p>
 * The class... 
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 21-Aug-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.3
 */
public class Flow extends ActivityContainer {
 
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( Flow.class ) ;  
    
    public Flow() {
        super() ;
    }
    
    
    public String toXMLString() {
        if( TRACE_ENABLED ) logger.debug( "toXMLString() entry") ;   
              
        String 
           response = WKF.getProperty( WKF.FLOW_CATEGORY
                                     , WKF.FLOW_XML_TEMPLATE ) ;
                                     
        try {
            
            Object []
               inserts = new Object[1] ;
            inserts[0] = super.toXMLString() ;
            
            response = MessageFormat.format( response, inserts ) ;

        }
        catch ( Exception ex ) {
            AstroGridMessage
                message = new AstroGridMessage( "" // ASTROGRIDERROR_FAILED_TO_FORMAT_RESPONSE
                                              , WKF.getClassName( this.getClass() ) ) ; 
            logger.error( message.toString(), ex ) ;
        } 
        finally {
            if( TRACE_ENABLED ) logger.debug( "toXMLString() exit") ;    
        }       
        
        return response ;   
         
    }

} // end of class Flow
