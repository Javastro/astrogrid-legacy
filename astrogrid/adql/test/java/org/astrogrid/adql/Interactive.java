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
 *         To compile fragments of ADQL, switch to fragment mode by typing "compile_fragment" 
 *         and pressing the ENTER key. Once in fragment mode, compile a fragment of ADQL by
 *         prefixing any ADQL string with the fragment name or an XPath like statement followed 
 *         by a colon. Examples:
 *         
 *         from: from catalogA as a inner join catalogB as b on a.col1 = b.col1 ;
 *         Select/Column: a.ra
 *         Select/Item[@type="aggregateFunctionType"]/Arg: * ;
 *         
 *         To return to compiling full queries, switch mode by typing "compile_full" 
 *         and pressing the ENTER key.
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
        
        String mode ;
        
        ModeSwitchException( String mode ) {
            this.mode = mode ;
        } ;
        
        String getMode() {
            return mode ;
        }
    }
    
    private static Log log = LogFactory.getLog( Interactive.class ) ;

//    private static final boolean TRACE_ENABLED = true ;
//    private static final boolean DEBUG_ENABLED = true ;
    
    public static final String BYE_TRIGGER = "Encountered \"bye\"" ;
    public static final String BYE = "BYE" ;
    public static final String FULL_MODE = "compile_full" ;
    public static final String FRAGMENT_MODE = "compile_fragment" ;
   
    public static boolean bFullMode = true ;
 
    private static AdqlCompiler COMPILER ;
    private static StringBuffer queryBuffer ;
    private static Interactive interactive ;
    private static String fragmentName ;
    private static AdqlTransformer TRANSFORMER ;
    
    
    public static final String WELCOME = 
        "Welcome to the command line tester for the AdqlStoX compiler...\n" +
        " USAGE: Type an ADQL query and press the ENTER key.\n" +
        "        Ensure the query ends with a semicolon.\n" +
        "        Type \"bye\" to exit.\n\n" +
        "        To compile fragments of ADQL, switch to fragment mode by typing \"compile_fragment\".\n" +
        "        Then prefix any ADQL string with the fragment name. Examples:\n\n" +
        "        from: from catalogA as a inner join catalogB as b on a.col1 = b.col1;\n" +
        "        Select/Column: a.ra;\n" +
        "        Select/Item[@type=\"aggregateFunctionType\"]/Arg: * ; \n\n" +
        "        To return to compiling full queries, switch by typing \"compile_full\".\n" +
        " COMMAND LINE OPTIONS: \n" +
        "        -s=<style sheet file name> \n" +
        "           Will attempt to load style sheet from the classpath.\n" +
        "           The style sheet is used to echo back to the user the output XML\n" +
        "           after it has been passed through the style sheet. Can be used to\n" +
        "           check the translation to some variant of SQL." ;
    
    public Interactive() {
        
    }
    
	 public static void main(String args[]) {
         
         interactive = new Interactive() ;
         
         if( args.length > 0 ) {
             if( args[0].startsWith( "-s=" )
                 &&
                 args[0].length() > 3 ) {
                 TRANSFORMER = new AdqlTransformer( args[0].substring(3) ) ;
             }
         }
         
		 // AdqlStoX compiler = new AdqlStoX(System.in);
         String queryXml = null ;
            
         print( WELCOME );
         if( TRANSFORMER != null ) {
             print( "Using style sheet " + args[0].substring(3) ) ;
         }
         while( true ) {
             initQueryBuffer() ;
             print("\nEnter an SQL-like expression :");	

             try {
                 StringReader source = getQuerySource() ;
                 if( bFullMode == true ) {
                     queryXml = getCompiler( source ).compileToXmlText() ; 
                     print( "\nCompilation produced:" ) ;
                     print( "\n" + queryXml ) ;
                     if( TRANSFORMER != null ) {
                         print( "\n\nTransformer produced:" ) ;
                         print( "\n" + TRANSFORMER.transformToAdqls( queryXml ) ) ;
                     }                    
                 }
                 else {
                     print( "\nfragment name: " + fragmentName ) ;
//                     queryXml = getCompiler( source ).compileFragmentToXmlText( fragmentName ) ;
                     String[] comments = new String[2] ;
                     XmlObject xo = getCompiler( source ).execFragment( fragmentName, comments ) ;
                     XmlOptions opts = new XmlOptions();
                     opts.setSaveOuter() ;                    
                     opts.setSaveAggressiveNamespaces() ;
                     opts.setSavePrettyPrint() ;
                     opts.setSavePrettyPrintIndent(4) ;
                     queryXml = xo.xmlText( opts ) ;
                     print( "\nCompilation produced:" ) ;
                     if( comments[0] != null ) {
                         print( "header comment: \n" + SimpleNode.prepareComment( comments[0] ) ) ;
                     }
                     print( "\n" + queryXml ) ;
                     if( comments[1] != null ) {
                         print( "trailer comment: \n" + SimpleNode.prepareComment( comments[1] ) ) ;
                     }
                 }
                 	
             }
             catch( ExitException eof ) {
                 print( "Goodbye!" ) ;
                 System.exit(0) ;
             }
             catch( ModeSwitchException mse ) {
                 String mseMode = mse.getMode() ;
                 if( mseMode.equalsIgnoreCase( FULL_MODE ) ) {
                     print( "Switching to compiling full queries..." ) ;
                     bFullMode = true ;
                 }
                 else if( mseMode.equalsIgnoreCase( FRAGMENT_MODE ) ) {
                     print( "Switching to compiling fragments..." ) ;
                     bFullMode = false ;
                 }
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

     
     private static void initQueryBuffer() {
         if( queryBuffer == null ) {
             queryBuffer = new StringBuffer( 1024 ) ;
         }
         else if( queryBuffer.length() > 0 ) {
             queryBuffer.delete( 0, queryBuffer.length() ) ;
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
                 if( line.endsWith( ";" ) && !line.startsWith( "--" ) ) {
                     queryBuffer.append( line.substring( 0, line.length()-1 ) ) ;
                     break ;
                 }
                 else if( line.startsWith( FRAGMENT_MODE ) ) {
                     throw interactive.new ModeSwitchException( FRAGMENT_MODE ) ;
                 }
                 else if( line.startsWith( FULL_MODE ) ) {
                     throw interactive.new ModeSwitchException( FULL_MODE ) ;
                 }
                 else if( line.startsWith( BYE ) || line.startsWith( BYE.toLowerCase() ) ) {
                     throw interactive.new ExitException() ;
                 }
                 queryBuffer.append( lineBuffer ) ;
                 lineBuffer.delete( 0, lineBuffer.length() ) ;
             }
             else if( bFullMode == false && c == ':' ) {
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
     
     private static AdqlCompiler getCompiler( StringReader source) {
         if( COMPILER == null ) {
             COMPILER = new AdqlCompiler( source );
         }
         else {
             COMPILER.ReInit( source ) ;
         }
         return COMPILER ;
     }
	 
     private static void print( String message ) {
         if( log.isDebugEnabled() ) {
             log.debug( message ) ;
         }
         else {
             System.out.println( message ) ;
         }
     }
     
     private static void print( String message, Exception ex ) {
         if( log.isDebugEnabled() ) {
             log.debug( message, ex ) ;
         }
         else {
             System.out.println( message ) ;
             System.out.println( ex.getStackTrace() ) ;
         }
     }
     
}
