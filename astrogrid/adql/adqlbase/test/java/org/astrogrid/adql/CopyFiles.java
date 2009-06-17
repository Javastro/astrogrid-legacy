/*
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql;

import org.w3c.dom.Document;
import org.custommonkey.xmlunit.XMLTestCase;
import java.util.ArrayList ;
import java.io.File ;
import java.net.URL ;
import java.net.URI ;
import java.io.FileFilter ;
import java.io.FileReader ;
import java.io.FileNotFoundException ;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.io.FileWriter;

import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


import org.astrogrid.xml.DomHelper;

import org.astrogrid.adql.v1_0.beans.*;
import org.apache.xmlbeans.*;

/**
 * AdqlStoXTest
 * 
 * Usage: CopyFiles directory=file-path
 *     
 *   CopyFiles is a unit-test file generator.
 *   The programme takes queries in adql/xml format with a .xml extension
 *   contained in the given directory. It duplicates each file, changing
 *   the extension to .adqlx. Then it creates a second equivalent file in adql/s
 *   format with an extension of .adqls. The files are written to the given 
 *   directory.
 *   WARNING: Existing files with the same name will be overwritten.
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Sep 26, 2006
 */
public class CopyFiles {
    
     private static final String USAGE =
        "Usage: CopyFiles directory=file-path\n\n" +
        "CopyFiles is a unit-test file generator.\n" +
        "The programme takes queries in adql/xml format with a .xml extension\n" +
        "contained in the given directory. It duplicates each file, changing\n" +
        "the extension to .adqlx. Then it creates a second equivalent file in adql/s\n" +
        "format with an extension of .adqls. The files are written to the given\n" +
        "directory.\n" +     
        "WARNING: Existing files with the same name will be overwritten.\n" ;
    
     private static Transformer textTransformer ;
    
     public static void main(String args[]) {
         
         if( args.length != 1 ) {
             System.out.println() ;
             System.out.print( USAGE ) ;
             return ;
         }
         
         String directoryPath = args[0].split( "=" )[1] ;        
         File directory = new File( directoryPath ) ;
         
         if( directory.exists() == false ) {
             System.out.println( "Target directory does not exist:\n" +
                                 "    " + args[0]
                               ) ;
             return ;
         }
        
         File[] fileArray = null ;
         try {

             fileArray = directory.listFiles( new FileFilter() {

                                 public boolean accept( File file ) {
                                     if( file.getName().endsWith( "xml" ) ) {
                                         return true ; 
                                     }
                                     return false ;
                                 } 
                             }
                        ) ;


             for( int j=0; j<fileArray.length; j++ ) {
                 System.out.println( "copying file: " + fileArray[j].getName() ) ;
                 CopyFiles.copyFile( fileArray[j], "adqlx" ) ;
                 ProduceAdqlFile( fileArray[j] ) ;
             }
         }
         catch( Exception ex ) {
             ex.printStackTrace() ;
         }
         
         System.out.println( "CopyFiles completed." ) ;
         
     }
     
     private static void copyFile( File inFile, String extension ) {
        FileReader reader = null ;
        FileWriter writer = null ;
        File outFile = null ;
        try {
            reader = new FileReader( inFile ) ;
            File parent = inFile.getParentFile() ;
            String[] name = inFile.getName().replace('.','_').split("_") ;
            String newFileName = name[0] + '.' + extension ;  
            System.out.println( "newFileName: " + newFileName ) ;
            outFile = new File( parent, newFileName) ; 
            // System.out.println( outFile.getAbsolutePath() ) ;
            writer = new FileWriter( outFile ) ;
            int ch = reader.read() ;
            while( ch != -1 ) {
                writer.write( ch ) ;
                ch = reader.read() ;
            }
        }
        catch( Exception iox ) {
            System.out.println( iox.getLocalizedMessage() ) ;
        }
        finally {
            try{ reader.close() ; } catch( Exception ex ) { ; }
            try{ writer.flush(); writer.close() ; } catch( Exception ex ) { ; } 
        }
    }
     
    private static void ProduceAdqlFile( File inFile ) {
        StreamSource source ;
        StreamResult result = new StreamResult( new StringWriter() ) ;
        FileWriter writer = null ;
        FileReader reader = null ;
        try {
            File parent = inFile.getParentFile() ;
            String[] name = inFile.getName().replace('.','_').split("_") ;
            String newFileName = name[0] + '.' + "adqls" ;  
            System.out.println( "newFileName: " + newFileName ) ;
            File outFile = new File( parent, newFileName) ; 
            reader = new FileReader( inFile ) ;
            source = new StreamSource( reader ) ;
            writer = new FileWriter( outFile ) ;
            result = new StreamResult( writer ) ;
            getTransformer().transform( source, result ) ;
        }
        catch( Exception ex ) {
            ex.printStackTrace() ;
        }
        finally {
            try{ reader.close() ; } catch( Exception ex ) { ; }
            try{ writer.flush(); writer.close() ; } catch( Exception ex ) { ; } 
        }
     
    }
   
    
    
    public static Source getFullTextStyleSource() {
        return new StreamSource( CopyFiles.class.getResourceAsStream( "ADQL10toSQL_TEXT_FULL.xsl" ) ) ;
    }

    /**
     * 
     */
    private static Transformer getTransformer() {
        if( textTransformer == null ) {
            try {
                textTransformer = TransformerFactory.newInstance().newTransformer( getFullTextStyleSource() );
            } catch(TransformerConfigurationException tce ) {
               // Error generated by the parser
               tce.printStackTrace() ;
            } 
        }
        return textTransformer ;      
    }
    
}
	

