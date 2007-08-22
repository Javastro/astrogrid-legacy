/*
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql;

import org.apache.xmlbeans.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import org.astrogrid.adql.AdqlCompilerSVTest.SVTestInitializationException;
import org.astrogrid.adql.metadata.*;

/**
 * InteractiveSV
 * 
 * Conducts a console based conversation. A ADQL query is entered in string form.
 * The query will be compiled into an SQL variant or an error returned.
 * <p>
 * Type an ADQL query and press the ENTER key.<br/>
 * Ensure the query ends with a semicolon.<br/>
 * Type "bye" to exit.<br/>
 * <p>
 * <p><blockquote><pre>
 * COMMAND LINE OPTIONS: 
 *    -m    Will attempt to load test metadata from the test-metadata.properties file.
 *    -s=style_sheet_name
 *    -u=function_prefix
 * Where:
 *    "style_sheet_name" is the name of the style sheet used to convert ADQL/x to SQL variant.
 *    "function_prefix" is the prefix to be used for user defined functions.
 * NOTE: If you wish to enter new command line options, type "reinit" and the program will put
 *       out a prompt allowing new parameters to be entered. Press the ENTER key when finished.
 *       If parameters are OK, the compiler will be reinitialized accordingly.
 * </pre></blockquote>
 *
 * @author Jeff Lusted jl99@star.le.ac.uk Sep 18, 2006
 */
public class InteractiveSV {
    
    class SVInitException extends Exception {
        /**
         * @param message
         * @param cause
         */
        public SVInitException(String message, Throwable cause) {
            super(message, cause);
            // TODO Auto-generated constructor stub
        }

        /**
         * @param message
         */
        public SVInitException(String message) {
            super(message);
            // TODO Auto-generated constructor stub
        }
    } 

    class SVExitException extends Exception {
        SVExitException() {} ;
    }
    
    class SVReinitRequestException extends Exception {
        SVReinitRequestException() {} ;
    }
    
    private static Log log = LogFactory.getLog( InteractiveSV.class ) ;

    public static final String BYE_TRIGGER = "Encountered \"bye\"" ;
    public static final String BYE = "BYE" ;
    public static final String REINIT = "REINIT" ;
 
    private AdqlCompilerSV compiler ;
    private StringBuffer queryBuffer ;
    private boolean useTestMetaData = false ;
    private String styleSheetName ;
    private String userDefinedPrefix ;
       
    public static final String USAGE = 
        "Welcome to the command line tester for AdqlCompilerSV ...\n" +
        " USAGE: Type an ADQL query and press the ENTER key.\n" +
        "        Ensure the query ends with a semicolon.\n" +
        "        The program will echo back an SQL variant or an error message.\n" +
        "        Type \"bye\" to exit.\n\n" +
        " COMMAND LINE OPTIONS: \n" +
        "        -m    Will attempt to load test metadata from the test-metadata.properties file.\n" +
        "        -s=style_sheet_name\n" +
        "        -u=function_prefix\n" +
        " Where:\n" +
        "  \"style_sheet_name\" is the name of the style sheet used to convert ADQL/x to SQL variant.\n" +
        "  \"function_prefix\" is the prefix to be used for user defined functions.\n" +
        " NOTE: If you wish to enter new command line options, type \"reinit\" and the program will put\n" +
        "       out a prompt allowing new parameters to be entered. Press the ENTER key when finished.\n" +
        "       If parameters are OK, the compiler will be reinitialized accordingly."  ;
   
    public InteractiveSV() {}
    
	 public static void main(String args[]) {
         
         InteractiveSV isv = new InteractiveSV() ;         
         isv.print( USAGE ) ;
         
         if( isv.retrieveArgs( args) == false ) {
             isv.print( "Invalid arguments." ) ;
         }
         else {
             try {
                 isv.converse() ;
             }
             catch( Exception ex ) {
                 isv.print( "InteractiveSV failure.", ex ) ;
             }
         }
            
     }
     
     private void converse() throws Exception {
         initCompiler() ;
         while( true ) {
             initQueryBuffer() ;
             print("\nEnter an ADQL expression :"); 

             try {
                 String adqls = getQuerySource() ;
                 String sql = compiler.compileToSQL( adqls ) ; 
                 print( "\nCompilation produced:" ) ;
                 print( "\n" + sql ) ;         
             }
             catch( SVExitException eof ) {
                 print( "Goodbye!" ) ;
                 System.exit(0) ;
             }
             catch( SVReinitRequestException requestNewParameters ) {
                 reinitConversation() ;
             }
             catch( AdqlException adlqException ) {
                 print( "\nCompilation failed:" ) ;
                 String[] messages = adlqException.getMessages() ;
                 for( int i=0; i<messages.length; i++ ) {
                     print( messages[i] ) ;
                 }
             }
             catch( IOException iox ) {
                 print( iox.getMessage() ) ; 
             }
             catch( Exception ex ) {
                 print( "\nPossible error within compiler:", ex ) ;
             }
         }              
     }
     
     private void reinitConversation() {
         print( "Type in new command line parameters and press ENTER:" ) ;
         StringBuffer lineBuffer = new StringBuffer() ;     
         try {
             char c = (char)System.in.read() ;
             while( true ) {
                 lineBuffer.append( c ) ;
                 if( c == '\n' ) {
                     break ;
                 }
                 c = (char)System.in.read() ;
             }
             String[] args = lineBuffer.toString().trim().split( " " ) ;
             if( retrieveArgs( args ) == false ) {
                 print( "Invalid arguments." ) ;
             }
             else {
                 this.initCompiler() ;
             }           
         }
         catch( Exception ex ) {
             print( "Initialization failed." ) ;
         }
         
     }
     
     private boolean retrieveArgs( String[] args ) {
         this.styleSheetName = null ;
         this.userDefinedPrefix = null ;
         this.useTestMetaData = false ;
         boolean retVal = false ;
         if( args != null && args.length >= 1 ) {
             for( int i=0; i<args.length; i++ ) {
                 if( args[i].startsWith( "-s=" ) ) { 
                     this.styleSheetName = args[i].substring(3) ;
                     retVal = true ;
                 }
                 else if (args[i].startsWith( "-u=" ) ) { 
                     this.userDefinedPrefix = args[i].substring(3) ;
                 }
                 else if( args[i].equals( "-m" ) ) { 
                     this.useTestMetaData = true ;
                 }
                 else {
                     break ;
                 }
             }             
         }       
         return retVal ;
        }
    
     private void initQueryBuffer() {
         if( queryBuffer == null ) {
             queryBuffer = new StringBuffer( 1024 ) ;
         }
         else if( queryBuffer.length() > 0 ) {
             queryBuffer.delete( 0, queryBuffer.length() ) ;
         }            
     }
     
     private void initCompiler() throws SVInitException {
         if( log.isTraceEnabled() ) log.trace( "entry: initCompiler()" ) ;
         try {
             this.compiler = new AdqlCompilerSV() ;
             compiler.setMaxCompilers( 1 ) ;
             compiler.setMetadata( getMetaData() ) ;
             compiler.setStyleSheet( getStyleSheet() ) ;
             if( this.userDefinedPrefix != null ) {
                 compiler.setUserDefinedFunctionPrefix( this.userDefinedPrefix ) ;
             }
         }
         catch( Exception ex ) {
             throw new SVInitException( "Failed to initialize AdqlCompilerSV", ex ) ;
         } 
         if( log.isTraceEnabled() ) log.trace( "exit: initCompiler()" ) ;
     }
     
     private String getStyleSheet() throws IOException {
         InputStream is = AdqlCompilerSV.class.getResourceAsStream( this.styleSheetName ) ;
         StringBuffer buffer =  new StringBuffer( 1024 ) ;
         int c = is.read() ;
         while( c != -1 ) {
             buffer.append( (char)c ) ;
             c = is.read() ;
         }
         String retVal = buffer.toString() ;
         return retVal ;
     }
     
     private Container getMetaData() { 
         if( this.useTestMetaData ) {
             return TestMetaDataLoader.getMetaData() ;
         }
         return new Container() ;
     }    
     
     private String getQuerySource() throws SVExitException
                                          , SVReinitRequestException
                                          , IOException {
         StringBuffer lineBuffer = new StringBuffer() ;
         //
         // Force a query to begin with a new line....
         queryBuffer.append( '\n' ) ;       
         String line ;
         char c = (char)System.in.read() ;
         while( true ) {
             lineBuffer.append( c ) ;
             if( c == '\n' ) {
                 line = lineBuffer.toString().trim() ;
                 if( line.endsWith( ";" ) && !line.startsWith( "--" ) ) {
                     queryBuffer.append( line.substring( 0, line.length()-1 ) ) ;
                     break ;
                 }
                 else if( line.startsWith( BYE ) || line.startsWith( BYE.toLowerCase() ) ) {
                     throw new SVExitException() ;
                 }
                 else if( line.startsWith( REINIT ) || line.startsWith( REINIT.toLowerCase() ) ) {
                     throw new SVReinitRequestException() ;
                 }
                 queryBuffer.append( lineBuffer ) ;
                 lineBuffer.delete( 0, lineBuffer.length() ) ;
             }
             c = (char)System.in.read() ;
         }
         return queryBuffer.toString() ;
     }
	 
     private void print( String message ) {
         if( log.isDebugEnabled() ) {
             log.debug( message ) ;
         }
         else {
             System.out.println( message ) ;
         }
     }
     
     private void print( String message, Exception ex ) {
         if( log.isDebugEnabled() ) {
             log.debug( message, ex ) ;
         }
         else {
             System.out.println( message ) ;
             System.out.println( ex.getStackTrace() ) ;
         }
     }
     
}
