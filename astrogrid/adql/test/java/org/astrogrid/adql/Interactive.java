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
    public static final String WELCOME = 
        "Welcome to the command line tester for the AdqlStoX compiler...\n" +
        " USAGE: Type an ADQL query and press the ENTER key.\n" +
        "        Ensure the query ends with a semicolon.\n" +
        "        Type \"bye\" to exit." ;
    
	 public static void main(String args[]) {
		 AdqlStoX compiler = new AdqlStoX(System.in);
         String queryXml = null ;
            
         System.out.println( WELCOME );
         while( true ) {
             System.out.println("\nEnter an SQL-like expression :");	

             try {
                 queryXml = compiler.compileToXmlText() ; 
                 System.out.println( "\nCompilation produced:" ) ;
                 System.out.println( queryXml ) ;				
             }
             catch( ParseException pex ) {
                 if( exitRequested( pex.getMessage() ) ) {
                     System.out.println( "Goodbye!" ) ;
                     System.exit(0) ;
                 }  
                 System.out.println( "\nCompilation failed:" ) ;
                 System.out.println( pex.getMessage() ) ;
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
         String firstLine = message.substring( 0, message.indexOf('\n') ) ;
         if( firstLine.indexOf( BYE_TRIGGER ) != -1 )
             return true ;
         return false ;       
     }
}
