/*$Id: AdqlCompilerSVTest.java,v 1.1 2007/08/07 17:37:05 jl99 Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql;


import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.io.InputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Properties;
import java.util.ListIterator;
import java.util.Iterator;

import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;

import org.astrogrid.adql.metadata.*;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.astrogrid.adql.AdqlCompilerSV.InvalidStateException;
import org.astrogrid.adql.beans.SelectDocument;
import org.astrogrid.adql.beans.SelectType;
import org.astrogrid.xml.DomHelper;
import org.custommonkey.xmlunit.XMLTestCase;
import org.w3c.dom.Document;
import org.apache.xmlbeans.XmlCursor;

import org.apache.log4j.Logger ;
import org.apache.log4j.Level ;
import org.apache.log4j.spi.LoggerRepository;
import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

/**
 * AdqlCompilerSVTest
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Aug 7, 2007
 */
public class AdqlCompilerSVTest implements Runnable {
    
    private static Log log = LogFactory.getLog( AdqlCompilerSVTest.class ) ;
    
    final static String README = "README" ;
    final static String BAD_INIT_MESSAGE = "AdqlCompilerSVTest. Initialization failed: " ;
    
    
    private class AdqlQueryUnit {
        File file ;
        String query ;
        boolean valid ;
    }
    
    private HashMap queries = new HashMap( 256 ) ;
    private Iterator queryIterator ;
    private AdqlCompilerSV compiler ;
   
    private static long accumulatedTime ;
    private long startTime ;
    private long endTime ;
    
    public static void main(String args[]) {
        if( log.isTraceEnabled() ) log.trace( "entry: main()" ) ;
        
        AdqlCompilerSVTest svTest = new AdqlCompilerSVTest() ;     
              
        try {
            svTest.init() ;
            svTest.compileQueries() ;
            Thread.currentThread().sleep( 8000 ) ;
        }
        catch( Exception ex ) {
            svTest.print( "AdqlCompilerSVTest failed.", ex ) ;
        }
        
        if( log.isTraceEnabled() ) log.trace( "exit: main()" ) ;
        System.exit( 0 ) ;
    }
    
    public AdqlCompilerSVTest() {
        super() ;
    }
    
    private void compileQueries() {
        if( log.isTraceEnabled() ) log.trace( "entry: compileQueries()" ) ;
        for( int i=0; i<5; i++ ) {
            Thread qThread = new Thread( this ) ;
            qThread.setName( "SVTest worker thread " + (i+1) ) ;
            qThread.start() ;
        }  
        if( log.isTraceEnabled() ) log.trace( "exit: compileQueries()" ) ;
    }
    
    private synchronized AdqlQueryUnit getOneQuery() {
        if( this.queryIterator == null ) {
            queryIterator = queries.values().iterator() ;
        }
        if( queryIterator.hasNext() ) {
            print( Thread.currentThread().getName() + " -> returning query" ) ;
            return (AdqlQueryUnit)queryIterator.next() ;
        }
        print( Thread.currentThread().getName() + " -> returning null query" ) ;
        return null ;
    }
 
    private Container getMetaData() { 
        return TestMetaDataLoader.getMetaData() ;
    }
    
    private void init() throws SVTestInitializationException {
        initFiles() ;
        initCompiler() ;
    }
    
    private void initFiles() throws SVTestInitializationException {
        if( log.isTraceEnabled() ) log.trace( "entry: initFiles()" ) ;
        try {
            //
            // Locate the directory that holds the README file
            // retrieve all the Adql/s queries and save them in a collection...         
            URL readme = this.getClass().getResource( README ) ;
            File directoryOfREADME = 
                new File( new URI( readme.toString() ) ).getParentFile() ;
            File[] directories = directoryOfREADME.listFiles( new DirectoryFilter() ) ;
            File[] fileArray = null ;
            //
            // For each directory...
            for( int i=0; i<directories.length; i++ ) {
                fileArray = new File( directories[i].getAbsolutePath() ).listFiles( new AdqlsFilter() ) ;
                //
                // For each query...
                for( int j=0; j<fileArray.length; j++ ) {
                    AdqlQueryUnit qu = new AdqlQueryUnit() ;
                    qu.file = fileArray[j] ;
                    qu.query = retrieveFile( fileArray[j] ) ;
                    //
                    // For the moment, assume each query is invalid...
                    qu.valid = false ;
                    //
                    // Save the query unit using the file name as key...
                    queries.put( fileArray[j].getName().split("\\.")[0], qu ) ;
                }
            }
            //
            // Now go back and try to pair each ADQL/s query against an ADQL/x file.
            // If there is a pair, the query should be valid...
            
            //
            // For each directory...
            for( int i=0; i<directories.length; i++ ) {
                fileArray = new File( directories[i].getAbsolutePath() ).listFiles( new AdqlxFilter() ) ;
                //
                // For each ADQL/x file mark the corresponding query as valid...
                for( int j=0; j<fileArray.length; j++ ) {
                   AdqlQueryUnit qu = (AdqlQueryUnit)queries.get( fileArray[j].getName().split("\\.")[0] ) ;
                   if( qu != null ) {
                       qu.valid = true ;
                   }
                }
            }
        }
        catch( Exception ex ) {
            throw new SVTestInitializationException( BAD_INIT_MESSAGE, ex ) ;
        }
        if( log.isTraceEnabled() ) log.trace( "exit: initFiles()" ) ;
    }
    
    private void initCompiler() throws SVTestInitializationException {
        if( log.isTraceEnabled() ) log.trace( "entry: initCompiler()" ) ;
        try {
            this.compiler = new AdqlCompilerSV() ;
            compiler.setMaxCompilers( 4 ) ;
            compiler.setMetadata( getMetaData() ) ;
            compiler.setStyleSheet( getStyleSheet() ) ;
        }
        catch( Exception ex ) {
            throw new SVTestInitializationException( BAD_INIT_MESSAGE, ex ) ;
        } 
        if( log.isTraceEnabled() ) log.trace( "exit: initCompiler()" ) ;
    }
        
    private synchronized void print( String message ) {
        if( log.isDebugEnabled() ) {
            log.debug( message ) ;
        }
        else {
            System.out.println( message ) ;
        }
    }
    
    private synchronized void print( String message, Throwable th ) {
        if( log.isDebugEnabled() ) {
            log.debug( message, th ) ;
        }
        else {
            System.out.println( message ) ;
            System.out.println( th.getStackTrace() ) ;
        }
    }
    
    private String retrieveFile( File file ) {
        FileReader reader = null ;
        StringBuffer buffer = new StringBuffer( 1024 ) ;
        try {
            reader = new FileReader( file ) ;
            int ch = reader.read() ;
            while( ch != -1 ) {
                buffer.append( (char)ch ) ;
                ch = reader.read() ;
            }
        }
        catch( Exception iox ) {
            ;
        }
        finally {
            try{ reader.close() ; } catch( Exception ex ) { ; }
        }
        return buffer.toString() ;
    }
       
    public class SVTestInitializationException extends Exception {

        /**
         * @param message
         * @param cause
         */
        public SVTestInitializationException(String message, Throwable cause) {
            super(message, cause);
        }

        /**
         * @param message
         */
        public SVTestInitializationException(String message) {
            super(message);
        }
        
    }
    
    private String getStyleSheet() throws IOException {
        InputStream is = AdqlCompilerSV.class.getResourceAsStream( "ADQL20_MYSQL.xsl" ) ;
        StringBuffer buffer =  new StringBuffer( 1024 ) ;
        int c = is.read() ;
        while( c != -1 ) {
            buffer.append( (char)c ) ;
            c = is.read() ;
        }
        String retVal = buffer.toString() ;
        return retVal ;
    }
    
    public void run() {
        AdqlQueryUnit qu = getOneQuery() ;
        while( qu != null ) {
            try {
                compiler.compileToSQL( qu.query ) ;
                print( Thread.currentThread().getName() + " -> Compiled without mishap: " + qu.file.getName() ) ;
            }
            catch( AdqlException aex ) {
                if( qu.valid == true ) {
                    String[] messages = aex.getMessages() ;
                    StringBuffer buffer = new StringBuffer( messages.length * 64 ) ;
                    for( int i=0; i<messages.length; i++ ) {
                        buffer
                            .append( Thread.currentThread().getName() )
                            .append( ": Compilation suffered an ADQL Exception. Messages follow... \n" )
                            .append( "[" + i + "] " + messages[i] + "\n" ) ;
                    }
                    print( Thread.currentThread().getName() + " ->\n" +buffer.toString() ) ;
                }
                
            }
            catch( InvalidStateException isex ) {
                print( Thread.currentThread().getName(), isex ) ;
            }
            catch( TransformerException tex ) {
                print( Thread.currentThread().getName(), tex ) ;
            }
            catch( Throwable th ) {
                print( Thread.currentThread().getName(), th ) ;
            }
            qu = getOneQuery() ;
        }      
    }
    
    private class AdqlsFilter implements FileFilter {
    
        public boolean accept( File file ) {
            if( file.getName().endsWith( "adqls" ) ) {
                return true ;
            }
            return false ;
        }
    }
    
    private class AdqlxFilter implements FileFilter {
        
        public boolean accept( File file ) {
            if( file.getName().endsWith( "adqlx" ) ) {
                return true ;
            }
            return false ;
        }
    }
    
    private class DirectoryFilter implements FileFilter {
        
        public boolean accept( File file ) {
            if( file.isDirectory() ) {
                return true ;
            }
            return false ;
        }
    }
    
}
/*
$Log: AdqlCompilerSVTest.java,v $
Revision 1.1  2007/08/07 17:37:05  jl99
Initial multi-threaded test environment for AdqlCompilerSV

*/