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
import org.apache.xmlbeans.*;
/**
 * Interactive
 * 
 * Conducts a console based conversation.
 * 
 * Enter a query in string form.
 * The query will be compiled into adql/x or an error returned.
 * 
 * To end the session. Type in a ";" and press Enter.
 * The query will be errored. Then type in another ";" to exit.
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Sep 18, 2006
 */
public class Interactive {
	
	 public static void main(String args[]) {
		 AdqlStoX compiler = new AdqlStoX(System.in);
		 SelectDocument queryDoc = null ;

			System.out.println("Reading from standard input...");
			while( true ) {
				System.out.println("Enter an SQL-like expression :");	
				
				try {
					queryDoc = compiler.exec();  
					System.out.println( queryDoc.toString() ) ;				
				}
				catch( ParseException pex ) {
					System.out.println( pex.getMessage() ) ;
					if( exitRequested( compiler ) ) {					
						System.out.println( "Goodbye!" ) ;
						System.exit(0) ;
					}
				}
				catch( Exception ex ) {
					ex.printStackTrace() ;
					if( exitRequested( compiler ) ) {					
						System.out.println( "Goodbye!" ) ;
						System.exit(0) ;
					}	
				}
			}
					
	 }
	 
	 private static boolean exitRequested( AdqlStoX compiler ) {
		 char ch;
		 try {
			 ch = (char)System.in.read() ;
			 if( ch != ';' ) {
				 compiler.ReInit( System.in ) ;
				 return false ;
			 }
		 }
		 catch( Exception ex ) {
			 ;
		 }
		 return true ;		 
	 }

}
