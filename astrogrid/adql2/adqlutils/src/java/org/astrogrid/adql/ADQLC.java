/*
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql;

import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.io.FileNotFoundException;

/**
 * ADQLC
 * 
 * Given an instream ADQL/s query or the path to an ADQL/s query
 * will compile the query to ADQL/x on stdout, or print errors to stderr
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * July 6, 2007
 */
public class ADQLC {
   
    private ADQLC() {}
    
	 public static void main( String args[] ) {
                 
         if( args.length == 0 ) {
             System.err.print( "No ADQL/s query supplied." ) ;
             return ;
         }
         
         if( args.length > 1 ) {
             System.err.print( "Too many arguments supplied." ) ;
             return ;
         }
         
         String argOne = args[0].trim() ; 
         AdqlParser compiler ;
         //
         // Input can be a file path to a query or a query ...
         if( argOne.length() > 6 ) {
             if( argOne.substring(0,6).equalsIgnoreCase( "SELECT" ) ) {             
                 compiler = new AdqlParser( new StringReader( argOne ) ) ;
             }
             else {
                 compiler = getFileBasedCompiler( argOne ) ;            
             }
         }         
         else {
             //
             // It could be a very small path statement...
             compiler = getFileBasedCompiler( argOne ) ;   
         }
         
         if( compiler == null )
             return ;
                  
         try {
             System.out.print( compiler.parseToXmlText() ) ;
         }
         catch( AdqlException aex ) {
             System.err.println( "Following errors reported: " ) ;
             String[] messages = aex.getErrorMessages() ;
             for( int i=0; i<messages.length; i++ ) {
                 System.err.println( messages[i] ) ;
             }
             return ;
         }
         catch( Exception ex ) {
             System.err.println( "Possible internal compiler error: " ) ;
             ex.printStackTrace( System.err ) ;
             return ;
         }   
      
	 }
     
     private static AdqlParser getFileBasedCompiler( String path ) {
         AdqlParser compiler = null ;
         try {
             compiler = new AdqlParser( new FileReader( new File( path ) ) ) ;
         }
         catch( FileNotFoundException fnfex ) {
             System.err.println( "Input file does not exist: " + path ) ;
         }
         return compiler ;
     }
     
}
