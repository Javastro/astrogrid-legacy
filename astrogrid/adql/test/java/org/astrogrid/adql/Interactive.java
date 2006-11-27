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
    
    public static final String BYE_TRIGGER = "Encountered \"bye\"" ;
    public static final String FULL_MODE_TRIGGER = "compile_full" ;
    public static final String FRAGMENT_MODE_TRIGGER = "compile_fragment" ;
    public static final int FULL_MODE = 1 ;
    public static final int FRAGMENT_MODE = 2 ;
    public static int mode = FULL_MODE ;
    
    
    public static final String WELCOME = 
        "Welcome to the command line tester for the AdqlStoX compiler...\n" +
        " USAGE: Type an ADQL query and press the ENTER key.\n" +
        "        Ensure the query ends with a semicolon.\n" +
        "        Type \"bye\" to exit.\n\n" +
        "        To compile fragments of ADQL, switch to fragment mode by typing \"compile_fragment\".\n" +
        "        Then prefix any ADQL string with the fragment name. For example:\n" +
        "        from: from catalogA as a inner join catalogB as b on a.col1 = b.col1;\n\n" +
        "        To return to compiling full queries, switch by typing \"compile_full\"." ;
    
	 public static void main(String args[]) {
		 AdqlStoX compiler = new AdqlStoX(System.in);
         String queryXml = null ;
            
         System.out.println( WELCOME );
         while( true ) {
             System.out.println("\nEnter an SQL-like expression :");	

             try {
                 if( mode == FULL_MODE ) {
                     queryXml = compiler.compileToXmlText() ; 
                 }
                 else {
                     String fragmentName = getFragmentName() ;
                     System.out.println( "fragment name: " + fragmentName ) ;
                     queryXml = compiler.compileFragmentToXmlText( fragmentName ) ;          
                 }
                 System.out.println( "\nCompilation produced:" ) ;
                 System.out.println( queryXml ) ;				
             }
             catch( ParseException pex ) {
                 if( exitRequested( pex.getMessage() ) ) {
                     System.out.println( "Goodbye!" ) ;
                     System.exit(0) ;
                 } 
                 else if( modeRequest( pex.getMessage() ) ) {
                     if( mode == FRAGMENT_MODE ) {
                         System.out.println( "Switching to compiling fragments..." ) ;
                     }
                     else if( mode == FULL_MODE ) {
                         System.out.println( "Switching to compiling full queries..." ) ;
                     }
                     compiler.ReInit( System.in ) ;
                 }
                 else {
                     System.out.println( "\nCompilation failed:" ) ;
                     System.out.println( pex.getMessage() ) ;
                     compiler.ReInit( System.in ) ;
                 }
             }
             catch( IOException iox ) {
                 compiler.ReInit( System.in ) ;
             }
             catch( Exception ex ) {
                 System.out.println( "\nPossible error within compiler:" ) ;
                 ex.printStackTrace() ;
                 compiler.ReInit( System.in ) ;
             }
         }				
	 }
	 
     private static boolean exitRequested( String message ) {
         String firstLine = message ;
         int i = message.indexOf('\n') ;
         if( i != -1 ) {
             firstLine = message.substring( 0, i ) ;          
         }
         if( firstLine.indexOf( BYE_TRIGGER ) != -1 )
             return true ;
         return false ;       
     }
     
     private static boolean modeRequest( String message ) {
         boolean retVal = false ;
         String firstLine = message ;
         int i = message.indexOf('\n') ;
         if( i != -1 ) {
             firstLine = message.substring( 0, i ) ;          
         }
         if( firstLine.indexOf( FRAGMENT_MODE_TRIGGER ) != -1 )  {
             mode = FRAGMENT_MODE ;             
             retVal = true ;
         }
         else if(firstLine.indexOf( FULL_MODE_TRIGGER ) != -1 )  {
             mode = FULL_MODE ; 
             retVal = true ;
         }
         return retVal ;      
     }
     
     private static String getFragmentName() throws ParseException, IOException {
         StringBuffer buffer = new StringBuffer() ;
         String line = null ;
         boolean triggerFound = false ;
         int lineCount = 0 ;  
         char c = (char)System.in.read() ;
         while( true) {   
             if( c == ':' ) {
                 triggerFound = true ;
                 break ;
             }
             if( c == '\n' ) {
                 lineCount ++ ;
                 if( lineCount >= 100 ) {
                     break ;
                 }
                 continue ;
             }
             buffer.append( c ) ;
             c = (char)System.in.read() ;
         }
         line = buffer.toString().trim() ;
         if( line.indexOf( "bye" ) != -1 ) {
             throw new ParseException( BYE_TRIGGER ) ;
         }
         else if( line.indexOf( FULL_MODE_TRIGGER ) != -1 ) {
             throw new ParseException( FULL_MODE_TRIGGER ) ;
         }
         else if( triggerFound == false ) {
             throw new ParseException( "Incorrect trigger for Fragment Mode." ) ;
         }             
         return line ;
     }
}
