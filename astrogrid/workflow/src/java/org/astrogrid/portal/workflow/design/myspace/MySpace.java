/*
 * @(#)MySpace.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design.myspace;

import org.astrogrid.portal.workflow.design.* ;
import org.apache.log4j.Logger ;

import org.astrogrid.i18n.*;
import org.astrogrid.AstroGridException ;

import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate;
import org.astrogrid.portal.workflow.*;
import org.astrogrid.portal.workflow.design.activity.*;
import org.w3c.dom.Document ;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream ;
/**
 * The <code>MySpace</code> class represents... 
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
public class MySpace {
    
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( MySpace.class ) ; 
        
    private static final String
        ASTROGRIDERROR_SOMEMESSAGE = "AGWKFE00050" ; // none so far 
        
    private static MySpaceManagerDelegate
        mySpace ;
    
    public MySpace() {
    }
    
    public boolean saveWorkflow( Workflow workflow ) {
        if( TRACE_ENABLED ) trace( "MySpace.saveWorkflow() entry") ; 
        
        try {
            
            String
                xmlWorkflow = workflow.toXMLString(),
                fileName = this.generateFileName( workflow ) ;
            OutputStream 
                out = new BufferedOutputStream( new FileOutputStream( fileName ) ) ;
            PrintStream
                pStream = new PrintStream( out ) ;
            pStream.print( xmlWorkflow ) ;
            pStream.flush() ;
            pStream.close() ;
        }
        catch( FileNotFoundException fnfex ) {
            ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "MySpace.saveWorkflow() exit") ; 
        }
        
        return true ;
           
    }
    
    
    private String generateFileName( Workflow workflow ) {
        if( TRACE_ENABLED ) trace( "MySpace.generateFileName() entry") ; 
        
        StringBuffer
            nameBuffer = new StringBuffer( 64 );
        try {
            
            nameBuffer
                .append( WKF.getProperty( WKF.MYSPACE_CACHE_DIRECTORY
                                        , WKF.MYSPACE_CATEGORY ) )  
                .append( System.getProperty( "file.separator" ) )
                .append( "workflow_")
                .append( workflow.getUserid() )
                .append( "_" )
                .append( workflow.getCommunity() )
                .append( "_" )
                .append( workflow.getName() )
                .append( ".xml") ;
            
        }
        finally {
            if( TRACE_ENABLED ) trace( "MySpace.generateFileName() exit") ; 
        }
         
        return nameBuffer.toString() ;
        
    }
    
    
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }  

} // end of class MySpace
