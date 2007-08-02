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

import org.astrogrid.adql.metadata.*;

/**
 * Interactive
 * 
 * Conducts a console based conversation. A query is entered in string form.
 * The query will be compiled into adql/x or an error returned.
 * <p>
 * Type an ADQL query and press the ENTER key.<br/>
 * Ensure the query ends with a semicolon.<br/>
 * Type "bye" to exit.<br/>
 * <p>
 * To compile fragments of ADQL, switch to fragment mode by typing "compile_fragment" 
 * and pressing the ENTER key. Once in fragment mode, compile a fragment of ADQL by
 * prefixing any ADQL string with the fragment name or an XPath like statement followed 
 * by a colon. Examples:
 * <p>
 * from: from catalogA as a inner join catalogB as b on a.col1 = b.col1 ;<br/>
 * Select/Column: a.ra ;<br/>
 * Select/Item[@type="aggregateFunctionType"]/Arg: * ;<br/>
 * <p>
 * To return to compiling full queries, switch mode by typing "compile_full" 
 * and pressing the ENTER key.
 * <p>
 * There is one command line option -m, which will attempt to load test metadata from
 * the test-metadata.properties file.
 *
 * @author Jeff Lusted jl99@star.le.ac.uk Sep 18, 2006
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

    public static final String BYE_TRIGGER = "Encountered \"bye\"" ;
    public static final String BYE = "BYE" ;
    public static final String FULL_MODE = "compile_full" ;
    public static final String FRAGMENT_MODE = "compile_fragment" ;
   
    public static boolean bFullMode = true ;
 
    private static AdqlCompiler COMPILER ;
    private static StringBuffer queryBuffer ;
    private static Interactive interactive ;
    private static String fragmentName ;
    private static Container metadata ;  
    private static boolean useTestMetaData = false ;
       
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
        "        -m    Will attempt to load test metadata from the test-metadata.properties file." ;
   
    public Interactive() {}
    
	 public static void main(String args[]) {
         interactive = new Interactive() ;   
         
         if( args.length > 0 ) {
             if( args[0].equals( "-m") ) {
                 useTestMetaData = true ;
             }
         }
            
         print( WELCOME );
         while( true ) {
             initQueryBuffer() ;
             print("\nEnter an SQL-like expression :");	

             try {
                 StringReader source = getQuerySource() ;
                 String queryXml ;
                 if( bFullMode == true ) {
                     queryXml = getCompiler( source ).compileToXmlText() ; 
                     print( "\nCompilation produced:" ) ;
                     print( "\n" + queryXml ) ;         
                 }
                 else {
                     print( "\nfragment name: " + fragmentName ) ;
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
             initMetadata() ;
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
     
     private static void initMetadata() {
         if( useTestMetaData == true )
             metadata = TestMetaDataLoader.getMetaData() ;
         else 
             metadata = new Container() ;
         COMPILER.setMetadata( metadata ) ;
     }
}
