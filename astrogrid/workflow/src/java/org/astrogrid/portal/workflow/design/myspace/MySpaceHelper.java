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
import org.apache.axis.utils.XMLUtils ;

import org.astrogrid.mySpace.delegate.mySpaceManager.MySpaceManagerDelegate;
import org.astrogrid.portal.workflow.*;
import org.astrogrid.portal.workflow.design.activity.*;
import org.w3c.dom.Document ;

import java.io.* ;
import java.io.StringReader;
import java.io.File ;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream ;
import java.text.MessageFormat ;
import java.util.ListIterator;

import java.io.InputStream ;
import java.io.BufferedInputStream ;
import java.io.FileInputStream ;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.rpc.ParameterMode;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.axis.encoding.XMLType;
import org.apache.log4j.Logger;
import org.astrogrid.Configurator ;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * The <code>MySpaceHelper</code> class represents... 
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
public class MySpaceHelper {
    
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( MySpaceHelper.class ) ; 
        
    private static final String
        ASTROGRIDERROR_MYSPACEMANAGER_RETURNED_AN_ERROR = "AGWKFE?????", 
        ASTROGRIDERROR_FAILED_TO_PARSE_MYSPACEMANAGER_RESPONSE = "AGWKFE?????", 
        ASTROGRIDERROR_SOMEMESSAGE = "AGWKFE00050" ; // none so far 
        
    private static String
        MYSPACE_SUCCESS = "success" ;
    
    private MySpaceHelper() {
    }
    
    
    public static String readWorkflow( String userid, String community, String name ) {
        if( TRACE_ENABLED ) trace( "MySpaceHelper.readWorkflow() entry") ; 
        
        String
            workflowString = null;
         
        try {
            MySpaceManagerDelegate
                mySpace = new MySpaceManagerDelegate( WKF.getProperty( WKF.MYSPACE_URL, WKF.MYSPACE_CATEGORY ) ) ;
              
            // Format the MySpace request...
            String
               requestTemplate = WKF.getProperty( WKF.MYSPACE_REQUEST_TEMPLATE
                                                , WKF.MYSPACE_CATEGORY ) ;
/*                                               
            "<request>" +
                "<elements>" +
                    "<userID>clq</userID>" + //Mandatory
                    "<communityID>Leicester</communityID>" + //Mandatory
                    "<jobID>testIDIt2</jobID>" + //Mandatory
                    "<mySpaceAction>lookupDataHolderDetails</mySpaceAction>" +
                    "<dataItemID></dataItemID>" +
                    "<oldDataItemID></oldDataItemID>" +
                    "<newDataItemName></newDataItemName>" + 
                    "<newContainerName>x</newContainerName>" +
                    "<query>x</query>" +
                    "<newDataHolderName>xx</newDataHolderName>" +
                    "<serverFileName>/clq/serv2/</serverFileName>" + //Mandatory // dataholer need to look up
                    "<fileSize></fileSize>" +
                "</elements>" +
            "</request>"; 
*/                                                
                                                
            Object []
               inserts = new Object[11] ;
            inserts[0] = userid ;
            inserts[1] = community ;
            inserts[2] = name ;
            inserts[3] = "lookupDataHolderDetails" ;
            inserts[4] = "" ;
            inserts[5] = "" ;
            inserts[6] = "" ;
            inserts[7] = "" ;
            inserts[8] = "" ;
            inserts[9] = "" ;
            inserts[10] = "/" + userid + "/serv1/" + name ;
            inserts[11] = "" ;
            
            String
               xmlRequest = MessageFormat.format( requestTemplate, inserts );
            
            // Get the MySpaceManager to pick up the file...
            String
                responseXML = mySpace.upLoad( xmlRequest ) ;
                
            diagnoseResponse( responseXML ) ;
            
            // Once we've located the file ...
            workflowString = getFile( "" ) ;
            
        }
//        catch( FileNotFoundException fnfex ) {
//            ;
//        }
        catch( MySpaceException msex ) {
        }
        catch( Exception ex ) {
            ;         
        }
        finally {
            if( TRACE_ENABLED ) trace( "MySpaceHelper.readWorkflow() exit") ; 
        }
       
        return workflowString ;
        
    }
    
    
    public static boolean deleteWorkflow( String userid, String community, String name  ) {
        if( TRACE_ENABLED ) trace( "MySpaceHelper.deleteWorkflow() entry") ; 
        
        boolean
            retValue = true ;
         
        try {   

        }
        finally {
            if( TRACE_ENABLED ) trace( "MySpaceHelper.deleteWorkflow() exit") ; 
        }
        
        return retValue ;
        
    }
       
    
    public static void saveWorkflow( Workflow workflow ) throws WorkflowException {
        if( TRACE_ENABLED ) trace( "MySpace.saveWorkflow() entry") ; 
        
        PrintStream
            pStream = null ;
        
        try {
            
            String
                xmlWorkflow = workflow.toXMLString(),
                filePath = generateFileName( workflow ) ;
 
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
            pStream.print( xmlWorkflow ) ;  //JBL:  may need a loop here
            pStream.flush() ;
            pStream.close() ;
            
            MySpaceManagerDelegate
                mySpace = new MySpaceManagerDelegate( WKF.getProperty( WKF.MYSPACE_URL, WKF.MYSPACE_CATEGORY ) ) ;
              
            // Format the MySpace request...
            String
               requestTemplate = WKF.getProperty( WKF.MYSPACE_REQUEST_TEMPLATE
                                                , WKF.MYSPACE_CATEGORY ) ;
/*                                               
            "<userID>clq</userID>" + //Mandatory
            "<communityID>Leicester</communityID>" + //Mandatory
            "<jobID>testIDIt2</jobID>" + //
            "<mySpaceAction>upLoad</mySpaceAction>" + 
            "<dataItemID></dataItemID>" +
            "<oldDataItemID></oldDataItemID>" +
            "<newDataItemName></newDataItemName>" +
            "<newContainerName</newContainerName>" +
            "<query></query>" +
            "<newDataHolderName>/clq/serv2/tablexx</newDataHolderName>" + //Mandatory
            "<serverFileName>/tmp/test</serverFileName>" + //Mandatory //this is the file datacentre will upload
            "<fileSize>1</fileSize>" + //Mandatory
*/                                                
                                                
            Object []
               inserts = new Object[11] ;
            inserts[0] = workflow.getUserid() ;
            inserts[1] = workflow.getCommunity() ;
            inserts[2] = " " ;
            inserts[3] = "upLoad" ;
            inserts[4] = " " ;
            inserts[5] = " " ;
            inserts[6] = " " ;
            inserts[7] = " " ;
            inserts[8] = " " ;
            inserts[9] = generateDataHolderName( workflow.getUserid()
                                               , generateFileName( workflow ) ) ;
            inserts[10] = filePath ;
            inserts[11] = "1" ;
            
            String
               xmlRequest = MessageFormat.format( requestTemplate, inserts );
            
            // Get the MySpaceManager to pick up the file...
            String
                responseXML = mySpace.upLoad( xmlRequest ) ;
                
            diagnoseResponse( responseXML ) ;
        }
//        catch( FileNotFoundException fnfex ) {
//            ;
//        }
        catch( MySpaceException msex ) {
        }
        catch( Exception ex ) {
            ;
        }
        finally {
            pStream.close() ;
            if( TRACE_ENABLED ) trace( "MySpace.saveWorkflow() exit") ; 
        }
        
        return ;
           
    } // end of saveWorkflow()
    
    
    public static ListIterator readWorkflowList( String userid, String community ) {
        return null ;
    }
    
    
    public static Query readQuery( String userid, String community, String name ) {
        if( TRACE_ENABLED ) trace( "MySpace.readQuery() entry") ; 
        
        Query
            query = null;
         
        try {
                  
        }
        catch ( Exception ex ) {
        }
        finally {
            if( TRACE_ENABLED ) trace( "MySpace.readQuery() exit") ; 
        }
       
        return query ;
        
    }
    
    
    public static ListIterator readQueryList( String userid, String community ) {
        return null ;
    }
    
    
    private static String generateFullyQualifiedPathName( Workflow workflow ) {
        if( TRACE_ENABLED ) trace( "MySpace.generateFullyQualifiedPathName() entry") ; 
        
        StringBuffer
            nameBuffer = new StringBuffer( 64 );
        try {
            
            nameBuffer
                .append( WKF.getProperty( WKF.MYSPACE_CACHE_DIRECTORY
                                        , WKF.MYSPACE_CATEGORY ) )  
                .append( System.getProperty( "file.separator" ) )
                .append( generateFileName( workflow ) ) ;           
        }
        finally {
            if( TRACE_ENABLED ) trace( "MySpace.generateFullyQualifiedPathName() exit") ; 
        }
         
        return nameBuffer.toString() ;
        
    } // end of generateFullyQualifiedPathName()
    
    
    private static String generateFileName( Workflow workflow ) {
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
    
    
    private static String generateDataHolderName( String userid, String fileName ) {
        if( TRACE_ENABLED ) trace( "MySpaceHelper.generateDataHolderName() entry") ;
        
        StringBuffer
            retVal = new StringBuffer( 64 ) ;
        
        try {
            retVal
                .append( "/" )
                .append( userid )
                .append( "/" )
                .append( "serv1")
                .append( "/")
                .append( fileName ) ;
        }
        finally {
            
        }
        
        return retVal.toString() ;
        
    }
    
    
    private static void diagnoseResponse( String responseXML ) throws MySpaceException {    
        if( TRACE_ENABLED ) trace( "diagnoseResponse() entry") ;
        
        Document 
           responseDoc = null;
        DocumentBuilderFactory 
           factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder 
           builder = null;
           
        try {       
           builder = factory.newDocumentBuilder();
           logger.debug( responseXML ) ;           
           InputSource
              responseSource = new InputSource( new StringReader( responseXML ) );
            responseDoc = builder.parse( responseSource );      
            
           NodeList
              nodeList1 = responseDoc.getChildNodes() ;
              
           Element
               element = null ;
               
           // Focus on the results element...   
           for( int i=0 ; i < nodeList1.getLength() ; i++ ) {
                if( nodeList1.item(i).getNodeType() != Node.ELEMENT_NODE )
                    continue ;                      
                element = (Element) nodeList1.item(i) ;                         
                if( element.getTagName().equals( MySpaceManagerResponseDD.RESULTS_ELEMENT ) ) 
                    break ;
           } // end for 

           nodeList1 = element.getChildNodes() ;            

           // Focus on top level status element...   
           for( int i=0 ; i < nodeList1.getLength() ; i++ ) {
               if( nodeList1.item(i).getNodeType() != Node.ELEMENT_NODE )
                   continue ;                       
               element = (Element) nodeList1.item(i) ;                                  
               if( element.getTagName().equals( MySpaceManagerResponseDD.STATUS_ELEMENT_01 ) ) 
                   break ;
           } // end for 

           NodeList
              nodeList2 = element.getChildNodes() ; 
             
           // Focus on second level status element... 
           for( int i=0 ; i < nodeList2.getLength() ; i++ ) {
                if( nodeList2.item(i).getNodeType() != Node.ELEMENT_NODE )
                    continue ;                      
                element = (Element) nodeList2.item(i) ;                                         
                if( element.getTagName().equals( MySpaceManagerResponseDD.STATUS_ELEMENT_02 ) ) 
                    break ;
           } // end for 
   
           String
                status = element.getFirstChild().getNodeValue(),  // retrieve the status value
                details = null ;
           if (status != null)
               status.trim() ;
            
           // If it is not a success, we also need the reason 
           // (the value of the details element)...
           if( !status.equalsIgnoreCase( MYSPACE_SUCCESS ) ) {                      
               for( int i=0 ; i < nodeList2.getLength() ; i++ ) {           
                  if( nodeList2.item(i).getNodeType() != Node.ELEMENT_NODE )
                      continue ;                        
                  element = (Element) nodeList2.item(i) ;                           
                  if( element.getTagName().equals( MySpaceManagerResponseDD.DETAILS_ELEMENT_02 ) ) 
                      break ;
               } // end for 
           
               details = element.getFirstChild().getNodeValue() ;
               if (details != null)
                   details = details.trim() ;
            
               AstroGridMessage
                  message = new AstroGridMessage( ASTROGRIDERROR_MYSPACEMANAGER_RETURNED_AN_ERROR
                                                , WKF.getClassName( MySpaceHelper.class )
                                                , status
                                                , details ) ; 
               logger.error( message.toString() ) ;
               throw new MySpaceException( message );    
                           
           } // end for
           
        }
        catch ( ParserConfigurationException pe ) {
            AstroGridMessage
                message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_PARSE_MYSPACEMANAGER_RESPONSE
                                              , WKF.getClassName( MySpaceHelper.class ) ) ; 
            logger.error( message.toString(), pe ) ;
            throw new MySpaceException( message, pe );
        } 
        catch ( SAXException se ) {
            AstroGridMessage
                message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_PARSE_MYSPACEMANAGER_RESPONSE
                                              , WKF.getClassName( MySpaceHelper.class ) ) ; 
            logger.error( message.toString(), se ) ;
            throw new MySpaceException( message, se );
        }   
        catch ( IOException ie ) {
            AstroGridMessage
                message = new AstroGridMessage( ASTROGRIDERROR_FAILED_TO_PARSE_MYSPACEMANAGER_RESPONSE 
                                              , WKF.getClassName( MySpaceHelper.class ) ) ; 
            logger.error( message.toString(), ie ) ;
            throw new MySpaceException( message, ie );
        }           
        finally {
            if( TRACE_ENABLED ) trace( "diagnoseResponse() exit") ;  
        }

    } // end of diagnoseResponse()
    
    
    
    private static String getFile( String filePath ) {
        if( TRACE_ENABLED ) trace( "MySpaceHelper.getFile() entry") ;  
           
        StringBuffer
             sBuffer = new StringBuffer( 1024 ) ;
         String
             line = null ;
              
        try { 
                    
           BufferedReader
               bufferedReader = new BufferedReader( new FileReader( filePath ) ) ;
                    
           line = bufferedReader.readLine() ;
           while( line != null ){
               sBuffer.append( line ) ;
               line = bufferedReader.readLine() ;
           }

        }
        catch( IOException ioex ) {
            debug( "IOException: " + ioex.getLocalizedMessage() );
        }
        finally {
            if( TRACE_ENABLED ) trace( "MySpaceHelper.getFile() exit") ;  
        }
  
        return sBuffer.toString() ;
        
    } // end getFile()
    
      
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }  

    private static void errorlog( String logString ){
        System.out.println( logString ) ;
        // logger.error( logString ) ;
    }  

} // end of class MySpace
