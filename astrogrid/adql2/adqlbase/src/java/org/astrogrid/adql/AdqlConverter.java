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
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Converts Adql from earlier XML schema versions to version 2.0.
 * Replaces <code>ConvertADQL</code> which was suitable only
 * for a test environment.
 * 
 * @see org.astrogrid.adql.AdqlParserSV
 * @see org.astrogrid.adql.AdqlParserSVNC
 * 
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Sep 26, 2006
 */
public class AdqlConverter {
    
     private static Log log = LogFactory.getLog( AdqlConverter.class ) ;
     
     private static TransformerFactory sTransformerFactory = TransformerFactory.newInstance() ;
     
     
     /**
     * ADQL version 2.0 namespace. Note: for version 2, XML and schema are implementation
     * issues only. They are not part of the standard.
     */
     public static final String V2_NAMESPACE = "http://www.ivoa.net/xml/v2.0/adql" ;
     
     
     /**
     * Official ADQL version 1.0 namespace.
     */
     public static final String V1_NAMESPACE = "http://www.ivoa.net/xml/ADQL/v1.0" ;
     
     /**
     * An array of convertible name spaces covering earlier versions of ADQL.
     */
     public static final String[] CONVERTABLE_NAMESPACES = new String[] 
     {
         V1_NAMESPACE, 
         "http://adql.ivoa.net/v0.73",
         "http://www.ivoa.net/xml/ADQL/v0.7.4",          
         "http://www.ivoa.net/xml/ADQL/v0.8",
         "http://www.ivoa.net/xml/ADQL/v0.9", 
         "urn:astrogrid:schema:ADQL:v1.0a1" 
     } ;
    
     private Transformer adqlV1ToV2Transformer ;
     
     private static final String WELCOME = 
         "Welcome to the ADQL test program for converting ADQL/x between versions...\n" +
         " USAGE: Type a full path to an ADQL/x query and press the ENTER key.\n" +
         "        Type \"bye\" to exit.\n" ;
     
     public AdqlConverter() {} 
     
     /**
      * Converts from an earlier version of ADQL to version 2.0
      * 
      * @param reader
      * @return A version 2.0 ADQL query in xml format.
      * @throws TransformerException
      */
    public String convertV10ToV20( Reader reader ) throws TransformerException {
         StreamSource source = new StreamSource( reader ) ;
         StreamResult result = new StreamResult( new StringWriter() ) ;
         getTransformer().transform( source, result ) ;
         String retVal = ((StringWriter)result.getWriter()).toString() ;         
         return retVal ;
     }
     
    /**
     * Converts from an earlier version of ADQL to version 2.0
     * 
     * @param adqlString 
     * @return A version 2.0 ADQL query in xml format.
     * @throws TransformerException
     */
     public String convertToV20( String adqlString ) throws TransformerException {
    	 adqlString = tweakNamespace( adqlString ) ;
    	 return convertV10ToV20( new StringReader( adqlString ) ) ;
     }
          
     private static String tweakNamespace(String adqlString) {   	 
    	 for( int i=1; i<CONVERTABLE_NAMESPACES.length-1; i++ ) {
    		 if( adqlString.indexOf( CONVERTABLE_NAMESPACES[i] ) != -1 ) {
    			 adqlString = adqlString.replaceAll( CONVERTABLE_NAMESPACES[i], V1_NAMESPACE ) ;
    			 break ;
    		 }
    	 }
         return adqlString;
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
         //
         // Now search for a namespace we know we can convert (I hope) ...
         for( int i=0; i<CONVERTABLE_NAMESPACES.length; i++ ) {
             if( query.indexOf( CONVERTABLE_NAMESPACES[i] ) != -1 ) 
                 return CONVERTABLE_NAMESPACES[i] ;
         } 
         //
         // If none have been found...
         return null ;
     }
     
     /**
      * Tests whether a given xml formatted query is convertible to v2.0
      * 
     * @param query
     * @return Whether the given query is convertible.
     */
    public static boolean isConvertible( String query ) {
         return getCovertibleNameSpace( query ) != null ;
     }
    
     /**
      * A simple <code>main</code> method which can be used in a command line
      * interactive conversation to experiment with conversions.
      * <p><blockquote><pre>
      * USAGE: Type a full path to an ADQL/x query and press the ENTER key.
      *        Type "bye" to exit. 
      * </pre></blockquote><p>
      * @param args[]
      */
    public static void main(String args[]) {

         AdqlConverter converter = new AdqlConverter() ;     
         
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
   
    private static Source getV1ToV2StyleSheet() {
        InputStream is = AdqlConverter.class.getClassLoader().getResourceAsStream( "ADQLV10ToV20.xsl2" ) ;
        log.debug( "InputStream is " + is ) ;
        return new StreamSource( is ) ;
    }

    private Transformer getTransformer() throws TransformerException {
        if( adqlV1ToV2Transformer == null ) {
            adqlV1ToV2Transformer = sTransformerFactory.newTransformer( getV1ToV2StyleSheet() );
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
    
    /**
     * @return The fully qualified class name of the Transformer factory used.
     */
    public String getTransformerUsed() {
    	return sTransformerFactory.getClass().getName() ;
    }
    
}
	

