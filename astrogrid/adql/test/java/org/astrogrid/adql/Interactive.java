/*
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql;

import org.astrogrid.adql.v1_0.beans.* ;
import org.apache.xmlbeans.XmlOptions ;
import java.io.IOException;
import java.io.StringReader;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

/**
 * Interactive
 * 
 * Conducts a console based conversation. A query is entered in string form.
 * The query will be compiled into adql/x or an error returned.
 * 
 *  USAGE: Type an ADQL query and press the ENTER key.
 *         Ensure the query ends with a semicolon.
 *         Type "bye" to exit.
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Sep 18, 2006
 */
public class Interactive {
    
    class EOFException extends Exception {
        EOFException() {} ;
        
    } 
    
    class ExitException extends Exception {
        ExitException() {} ;
    }
    
    class ModeSwitchException extends Exception {
        
        int mode ;
        
        ModeSwitchException( int mode ) {
            this.mode = mode ;
        } ;
        
        int getMode() {
            return mode ;
        }
    }
    
    private static Log log = LogFactory.getLog( Interactive.class ) ;

    private static final boolean TRACE_ENABLED = true ;
    private static final boolean DEBUG_ENABLED = true ;
    
    public static final String BYE_TRIGGER = "Encountered \"bye\"" ;
    public static final String BYE = "BYE" ;
    public static final String FULL_MODE_TRIGGER = "compile_full" ;
    public static final String FRAGMENT_MODE_TRIGGER = "compile_fragment" ;
    public static final int FULL_MODE = 1 ;
    public static final int FRAGMENT_MODE = 2 ;
    public static int mode = FULL_MODE ;
    
    private static AdqlStoX COMPILER ;
    private static StringBuffer queryBuffer ;
    private static Interactive interactive ;
    private static String fragmentName ;
    
    
    public static final String WELCOME = 
        "Welcome to the command line tester for the AdqlStoX compiler...\n" +
        " USAGE: Type an ADQL query and press the ENTER key.\n" +
        "        Ensure the query ends with a semicolon.\n" +
        "        Type \"bye\" to exit.\n\n" +
        "        To compile fragments of ADQL, switch to fragment mode by typing \"compile_fragment\".\n" +
        "        Then prefix any ADQL string with the fragment name. For example:\n" +
        "        from: from catalogA as a inner join catalogB as b on a.col1 = b.col1;\n\n" +
        "        To return to compiling full queries, switch by typing \"compile_full\"." ;
    
    public Interactive() {
        
    }
    
	 public static void main(String args[]) {
         interactive = new Interactive() ;
		 // AdqlStoX compiler = new AdqlStoX(System.in);
         String queryXml = null ;
            
         print( WELCOME );
         while( true ) {
             initQueryBuffer() ;
             print("\nEnter an SQL-like expression :");	

             try {
                 StringReader source = getQuerySource() ;;
                 if( mode == FULL_MODE ) {
                     queryXml = getCompiler( source ).compileToXmlText() ; 
                 }
                 else {
                     print( "\nfragment name: " + fragmentName ) ;
                     queryXml = getCompiler( source ).compileFragmentToXmlText( fragmentName ) ;          
                 }
                 print( "\nCompilation produced:" ) ;
                 print( "\n" + queryXml ) ;				
             }
             catch( ExitException eof ) {
                 print( "Goodbye!" ) ;
                 System.exit(0) ;
             }
             catch( ModeSwitchException mse ) {
                 mode = mse.getMode() ;
                 if( mode == FULL_MODE ) {
                     print( "Switching to compiling full queries..." ) ;
                 }
                 else if( mode == FRAGMENT_MODE ) {
                     print( "Switching to compiling fragments..." ) ;
                 }
             }
             catch( ParseException pex ) {
                 print( "\nCompilation failed:" ) ;
                 print( pex.getMessage() ) ;
             }
             catch( IOException iox ) {
                 print( iox.getMessage() ) ; 
             }
             catch( Exception ex ) {
                 print( "\nPossible error within compiler:", ex ) ;
             }
         }				
	 }

     
     private static void initQueryBuffer() {
         if( queryBuffer == null ) {
             queryBuffer = new StringBuffer( 1024 ) ;
         }
         else if( queryBuffer.length() > 0 ) {
             queryBuffer.delete( 0, queryBuffer.length()-1 ) ;
         }            
     }
     
     
     private static StringReader getQuerySource() 
                    throws ModeSwitchException
                         , ExitException
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
                 if( line.endsWith( ";" ) ) {
                     queryBuffer.append( line.substring( 0, line.length()-1 ) ) ;
                     break ;
                 }
                 else if( line.startsWith( FRAGMENT_MODE_TRIGGER ) ) {
                     throw interactive.new ModeSwitchException( FRAGMENT_MODE ) ;
                 }
                 else if( line.startsWith( FULL_MODE_TRIGGER ) ) {
                     throw interactive.new ModeSwitchException( FULL_MODE ) ;
                 }
                 else if( line.startsWith( BYE ) || line.startsWith( BYE.toLowerCase() ) ) {
                     throw interactive.new ExitException() ;
                 }               
             }
             else if( mode == FRAGMENT_MODE && c == ':' ) {
                 fragmentName = lineBuffer.toString().substring(0,lineBuffer.length()-1).trim() ;
                 lineBuffer.delete( 0, lineBuffer.length() ) ;
             }
             c = (char)System.in.read() ;
         }
            
         print( "query source:" ) ;
         print( queryBuffer.toString() ) ;
         StringReader source = new StringReader( queryBuffer.toString() ) ;
         
         return source ;
     }
     
     private static AdqlStoX getCompiler( StringReader source) {
         if( COMPILER == null ) {
             COMPILER = new AdqlStoX( source ); 
         }
         else {
             COMPILER.ReInit( source ) ;
         }
         return COMPILER ;
     }
	 
//     private static boolean exitRequested( String message ) {
//         String firstLine = message ;
//         int i = message.indexOf('\n') ;
//         if( i != -1 ) {
//             firstLine = message.substring( 0, i ) ;          
//         }
//         if( firstLine.indexOf( BYE_TRIGGER ) != -1 )
//             return true ;
//         return false ;       
//     }
     
//     private static boolean modeRequest( String message ) {
//         boolean retVal = false ;
//         String firstLine = message ;
//         int i = message.indexOf('\n') ;
//         if( i != -1 ) {
//             firstLine = message.substring( 0, i ) ;          
//         }
//         if( firstLine.indexOf( FRAGMENT_MODE_TRIGGER ) != -1 )  {
//             mode = FRAGMENT_MODE ;             
//             retVal = true ;
//         }
//         else if(firstLine.indexOf( FULL_MODE_TRIGGER ) != -1 )  {
//             mode = FULL_MODE ; 
//             retVal = true ;
//         }
//         return retVal ;      
//     }
     
     private static void print( String message ) {
         if( DEBUG_ENABLED ) {
             log.debug( message ) ;
         }
         else {
             System.out.println( message ) ;
         }
     }
     
     private static void print( String message, Exception ex ) {
         if( DEBUG_ENABLED ) {
             log.debug( message, ex ) ;
         }
         else {
             System.out.println( message ) ;
             System.out.println( ex.getStackTrace() ) ;
         }
     }
     
//     private static String getFragmentName() throws ParseException, IOException {
//         StringBuffer buffer = new StringBuffer() ;
//         String line = null ;
//         boolean triggerFound = false ;
//         int lineCount = 0 ;  
//         char c = (char)System.in.read() ;
//         while( true) {   
//             if( c == ':' ) {
//                 triggerFound = true ;
//                 break ;
//             }
//             if( c == '\n' ) {
//                 lineCount ++ ;
//                 if( lineCount >= 100 ) {
//                     break ;
//                 }
//                 continue ;
//             }
//             buffer.append( c ) ;
//             c = (char)System.in.read() ;
//         }       
//         if( triggerFound == false ) {
//             throw new ParseException( "Incorrect trigger for Fragment Mode." ) ;
//         }    
//         line = buffer.toString().trim() ;
//         return line ;
//     }
}
