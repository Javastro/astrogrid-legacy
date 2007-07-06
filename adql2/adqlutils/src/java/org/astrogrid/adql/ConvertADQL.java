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
import java.io.StringReader;
import java.io.StringWriter;
import java.io.InputStream;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException ;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

/**
 * ConvertADQL
 * 
 * 
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Sep 26, 2006
 */
public class ConvertADQL {
    
     private static Log log = LogFactory.getLog( Interactive.class ) ;
    
     private static Transformer adqlV1ToV2Transformer ;
     
     public static final String WELCOME = 
         "Welcome to the ADQL test program for converting ADQL/x between versions...\n" +
         " USAGE: Type a full path to an ADQL/x query and press the ENTER key.\n" +
         "        Type \"bye\" to exit.\n" ;
     
     
     public synchronized String convertV10ToV20( Reader reader ) throws TransformerException {
         StreamSource source = new StreamSource( reader ) ;
         StreamResult result = new StreamResult( new StringWriter() ) ;
         if( log.isDebugEnabled() ) {
             getTransformer().getOutputProperties().list( System.out ) ;
         }
         getTransformer().transform( source, result ) ;
         String retVal = ((StringWriter)result.getWriter()).toString() ;         
         return retVal ;
     }
    
     public static void main(String args[]) {
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
        InputStream is = ConvertADQL.class.getResourceAsStream( "ADQLV10ToV20.xsl2" ) ;
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
	

