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

import java.io.File ;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream ;
import java.text.MessageFormat ;
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
        
        PrintStream
            pStream = null ;
        
        try {
            
            String
                xmlWorkflow = workflow.toXMLString(),
                filePath = this.generateFileName( workflow ) ;
 
/*JBL note. Not so sure about files that may already exist...
               
            File
                file = new File( fileName ) ;
                
            if( file.exists() ) {
                file.delete() ;
            }
*/
            
            // Write the xml workflow file to a local cache directory...
            OutputStream 
                out = new BufferedOutputStream( new FileOutputStream( filePath ) ) ;           
            pStream = new PrintStream( out ) ;
            pStream.print( xmlWorkflow ) ;
            pStream.flush() ;
            pStream.close() ;
            
            MySpaceManagerDelegate
                mySpace = new MySpaceManagerDelegate( WKF.getProperty( WKF.MYSPACE_URL, WKF.MYSPACE_CATEGORY ) ) ;
              
            // Format the MySpace request...
            String
               requestTemplate = WKF.getProperty( WKF.MYSPACE_REQUEST_TEMPLATE
                                                , WKF.MYSPACE_CATEGORY ) ;
                                                
            Object []
               inserts = new Object[4] ;
            inserts[0] = workflow.getUserid() ;
            inserts[1] = workflow.getCommunity() ;
            inserts[2] = this.generateFileName( workflow ) ;
            inserts[3] = filePath ;
            
            String
               xmlRequest = MessageFormat.format( requestTemplate, inserts );
            
            // Get the MySpaceManager to pick up the file...
            String
                responseXML = mySpace.upLoad( xmlRequest ) ;
        }
//        catch( FileNotFoundException fnfex ) {
//            ;
//        }
        catch( Exception ex ) {
            ;
        }
        finally {
            pStream.close() ;
            if( TRACE_ENABLED ) trace( "MySpace.saveWorkflow() exit") ; 
        }
        
        return true ;
           
    } // end of saveWorkflow()
    
    
    private String generateFullyQualifiedPathName( Workflow workflow ) {
        if( TRACE_ENABLED ) trace( "MySpace.generateFullyQualifiedPathName() entry") ; 
        
        StringBuffer
            nameBuffer = new StringBuffer( 64 );
        try {
            
            nameBuffer
                .append( WKF.getProperty( WKF.MYSPACE_CACHE_DIRECTORY
                                        , WKF.MYSPACE_CATEGORY ) )  
                .append( System.getProperty( "file.separator" ) )
                .append( this.generateFileName( workflow ) ) ;           
        }
        finally {
            if( TRACE_ENABLED ) trace( "MySpace.generateFullyQualifiedPathName() exit") ; 
        }
         
        return nameBuffer.toString() ;
        
    } // end of generateFullyQualifiedPathName()
    
    
    private String generateFileName( Workflow workflow ) {
        if( TRACE_ENABLED ) trace( "MySpace.generateFileName() entry") ; 
        
        StringBuffer
            nameBuffer = new StringBuffer( 64 );
        try {
            
            nameBuffer
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
        
    } // end of generateFileName()   
    
    
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }  

} // end of class MySpace
