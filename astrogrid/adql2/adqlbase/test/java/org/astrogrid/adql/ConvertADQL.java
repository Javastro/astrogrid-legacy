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
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException ;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

/**
 * A class specifically designed to support conversions within a test environment.
 * Conversions here cover Adql xml v0.73, v0.7.4, v0.8, v0.9, v1.0 and v1.0a1 to v2.0.
 * The function is now undertaken by <code>AdqlConverter</code>, which is suitable
 * for a live, pooled environment; ie: the functionality has been moved out of the test 
 * environment.
 * 
 * @author Jeff Lusted jl99@star.le.ac.uk
 *  Sep 26, 2006
 * @see org.astrogrid.adql.AdqlConverter
 * @deprecated 
 */
public class ConvertADQL {
    
     private static Log log = LogFactory.getLog( ConvertADQL.class ) ;
     
     public static final String[] CONVERTABLE_NAMESPACES = new String[] 
     {
         "http://adql.ivoa.net/v0.73",
         "http://www.ivoa.net/xml/ADQL/v0.7.4",          
         "http://www.ivoa.net/xml/ADQL/v0.8",
         "http://www.ivoa.net/xml/ADQL/v0.9",
         "http://www.ivoa.net/xml/ADQL/v1.0",  
         "urn:astrogrid:schema:ADQL:v1.0a1" 
     } ;
    
     private static Transformer adqlV1ToV2Transformer ;
     
     public static final String WELCOME = 
         "Welcome to the ADQL test program for converting ADQL/x between versions...\n" +
         " USAGE: Type a full path to an ADQL/x query and press the ENTER key.\n" +
         "        Type \"bye\" to exit.\n" ;
     
     public ConvertADQL() {} 
     
     public synchronized String convertV10ToV20( Reader reader ) throws TransformerException {
         StreamSource source = new StreamSource( reader ) ;
         StreamResult result = new StreamResult( new StringWriter() ) ;
//         if( log.isDebugEnabled() ) {
//             getTransformer().getOutputProperties().list( System.out ) ;
//         }
         getTransformer().transform( source, result ) ;
         String retVal = ((StringWriter)result.getWriter()).toString() ;         
         return retVal ;
     }
     
     
     /**
      * Searches for an ADQL namespace in a query that we think is convertible.
      * The method is suitable provided there are no misleading comments prior
      * to encountering namespaces.
      * 
      * @param query
      * @return Found convertible namespace string or a null if none is found.
      */
    public static String getCovertibleNameSpace( String query ) {
        //
        // Defensive stuff first...
         if( query == null )
             return null ;
         query = query.trim() ;
         if( !query.startsWith( "<?xml" ) )
             return null ;
         if( query.length() < 100 )
             return null ;         
         //
         // Get an adequate portion of the document.
         // If anything goes wrong here, the document is unsuitable for conversion...
         String part ;
         try {
             int start = query.indexOf( "?>" )+2 ;
             int end = query.indexOf( "</", start ) ;
             if( end == -1 ) {
                 end = query.indexOf( "/>", start ) ;
             }
             part= query.substring( start, end ) ;
         }
         catch( Exception ex ) {
             return null ;
         }
         //
         // Now search for a namespace we know we can convert (I hope) ...
         for( int i=0; i<CONVERTABLE_NAMESPACES.length; i++ ) {
             if( part.indexOf( CONVERTABLE_NAMESPACES[i], 0 ) != -1 ) 
                 return CONVERTABLE_NAMESPACES[i] ;
         } 
         //
         // If none have been found...
         return null ;
     }
     
     public static boolean isConvertible( String query ) {
         return getCovertibleNameSpace( query ) != null ;
     }
    
     public static void main(String args[]) {
         System.setProperty( "javax.xml.transform.TransformerFactory"
                            ,"com.icl.saxon.TransformerFactoryImpl" ) ; 
         
         ConvertADQL converter = new ConvertADQL() ;     
         
         print( WELCOME );
         while( true ) {
             
             try {

                 String userInput = getUserInput().trim() ;

                 if( "BYE".equalsIgnoreCase( userInput ) ) {
                     print( "Goodbye!" ) ;
                     return ;
                 }
                 File file = new File( userInput ) ;

                 if( file.exists() == false ) {
                     print( "Target file does not exist: " + userInput ) ;
                     print( "Type a full path to an ADQL/x query and press the ENTER key...\n" ) ;
                     continue ;
                 }


                 String converted = converter.convertV10ToV20( new FileReader( file ) ) ;
                 print( "Converted file: \n" + converted ) ;
             }
             catch( Exception ex ) {
                 print( "Conversion failed.", ex ) ;
             }
             
         }
        
     }
   
    public static Source getV1ToV2StyleSheet() {
        InputStream is = ConvertADQL.class.getClassLoader().getResourceAsStream( "ADQLV10ToV20.xsl2" ) ;
        log.debug( "InputStream is " + is ) ;
        return new StreamSource( is ) ;
    }

    /**
     * 
     */
    private static Transformer getTransformer() throws TransformerException {
        if( adqlV1ToV2Transformer == null ) {
            adqlV1ToV2Transformer = TransformerFactory.newInstance().newTransformer( getV1ToV2StyleSheet() );
        }
        return adqlV1ToV2Transformer ;      
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
    
    private static String getUserInput() throws IOException {
        StringBuffer buffer = new StringBuffer() ;

        char c = (char)System.in.read() ;
        while( true ) {
            buffer.append( c ) ;
            if( c == '\n' ) {
                break ;
            }
            c = (char)System.in.read() ;
        }
        return buffer.toString() ;
    }
    
}
	

