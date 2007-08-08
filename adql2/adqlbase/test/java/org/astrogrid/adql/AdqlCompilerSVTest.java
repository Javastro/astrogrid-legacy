/*$Id: AdqlCompilerSVTest.java,v 1.4 2007/08/08 15:22:16 jl99 Exp $
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
import java.io.FileWriter;
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
    
    private static final String USAGE =
        "Usage: AdqlCompilerSVTest [options]" +
        "Options:\n" +
        "-s=style_sheet_name\n" +
        "-d=directory name\n" +
        "Where:\n" +
        "  \"style_sheet_name\" is the name of the style sheet used to convert ADQL/x to SQL variant.\n" +
        "  \"directory_name\" is the name of the directory where output files will be written.\n" +    
        "NOTE: Existing files will NOT be overwritten.\n" ;
    
    private static final String ADQL_HEADER =
        "+=====================================================+\n" +
        "|  Input ADQL:                                        |\n" +
        "+=====================================================+\n" ; 
    
    private static final String SQL_HEADER =
        "+=====================================================+\n" +
        "|  Output SQL:                                        |\n" +
        "+=====================================================+\n" ; 
           
    private final static String README = "README" ;
    private final static String BAD_INIT_MESSAGE = "AdqlCompilerSVTest. Initialization failed: " ;
    private final static int TEST_MAX_COMPILERS = 4 ;
    private final static int TEST_THREADS = 5 ;
    private final static String SVRECORDINGS = "SVRecordings" ;
    
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
    private int queryCount = 0 ;
    private String styleSheetName ;
    private String recordingDirectoryName ;
    
    public static void main(String args[]) {
        if( log.isTraceEnabled() ) log.trace( "entry: main()" ) ;
        
        AdqlCompilerSVTest svTest = new AdqlCompilerSVTest() ;
        
        if( svTest.retrieveArgs( args) == false ) {
            svTest.print( "Invalid arguments..." ) ;
            svTest.print( USAGE ) ;
        }
        else {
            svTest.print( "args: -d=" + svTest.recordingDirectoryName +
                          " -s=" +svTest.styleSheetName ) ;
            try {
                svTest.init() ;
                svTest.compileQueries() ;
                svTest.waitForCompletion() ;
                svTest.print( "Query count: " + svTest.queryCount ) ;
            }
            catch( Exception ex ) {
                svTest.print( "AdqlCompilerSVTest failed.", ex ) ;
            }
        }
                    
        if( log.isTraceEnabled() ) log.trace( "exit: main()" ) ;
        System.exit( 0 ) ;
    }
    
    private boolean retrieveArgs( String[] args ) {
        boolean retVal = false ;
        if( args != null && args.length >= 1 ) {
            for( int i=0; i<args.length; i++ ) {
                if( args[i].startsWith( "-s=" ) ) { 
                    this.styleSheetName = args[i].substring(3) ;
                    retVal = true ;
                }
                else if( args[i].startsWith( "-d=" ) ) { 
                    this.recordingDirectoryName = args[i].substring(3) ;
                }
                else {
                    break ;
                }
            }             
        }       
        return retVal ;
    }
    
    public AdqlCompilerSVTest() {
        super() ;
    }
    
    private void compileQueries() {
        if( log.isTraceEnabled() ) log.trace( "entry: compileQueries()" ) ;
        for( int i=0; i<TEST_THREADS; i++ ) {
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
            AdqlQueryUnit aqu = (AdqlQueryUnit)queryIterator.next() ;
            print( Thread.currentThread().getName() + " -> returning query: " + aqu.file.getName() ) ;
            queryCount++ ;
            return aqu ;
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
    
    private void waitForCompletion() {
        if( log.isTraceEnabled() ) log.trace( "entry: waitForCompletion()" ) ;
        Thread[] threads = new Thread[ Thread.activeCount() ] ;
        int tCount = Thread.enumerate( threads ) ;
        for( int i=0; i<tCount; i++ ) {
            if( threads[i] != Thread.currentThread() ) {
                log.debug( "Attempting to join " + threads[i].getName() ) ;
                while( true ) {
                    try {
                        threads[i].join() ;
                        log.debug( "Joined successfully: " + threads[i].getName() ) ;
                        break ;
                    }
                    catch( InterruptedException iex ) {
                        
                    }
                }               
            }
        }
        print( "Compilations complete." ) ;
        if( log.isTraceEnabled() ) log.trace( "exit: waitForCompletion()" ) ;
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
                    //
                    // Ignore fragments...
                    if( !fileArray[j].getName().startsWith( "Fragment" ) ) {
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
            compiler.setMaxCompilers( TEST_MAX_COMPILERS ) ;
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
        InputStream is = AdqlCompilerSV.class.getResourceAsStream( this.styleSheetName ) ;
        StringBuffer buffer =  new StringBuffer( 1024 ) ;
        int c = is.read() ;
        while( c != -1 ) {
            buffer.append( (char)c ) ;
            c = is.read() ;
        }
        String retVal = buffer.toString() ;
        return retVal ;
    }
    
    private void recordQueryResult( AdqlQueryUnit qu, String sql ) {
        if( this.recordingDirectoryName == null ) 
            return ;
        
        try {         
            URL readme = this.getClass().getResource( README ) ;
            File directoryOfREADME = 
                new File( new URI( readme.toString() ) ).getParentFile() ;
            String path = directoryOfREADME.getAbsolutePath() 
                        + File.separator 
                        + SVRECORDINGS
                        + File.separator
                        + this.recordingDirectoryName
                        + File.separator
                        + qu.file.getName().split( "\\.")[0]
                        + ".query" ;
            File file = new File( path ) ;
            if( file.exists() )
                return ;
            writeFile( file, qu, sql ) ;            
        }
        catch( Exception ex ) {
             print( "Failed to record query result.", ex );
        }
        
    }
    
    private void writeFile( File file, AdqlQueryUnit qu, String sql ) throws IOException {
        StringBuffer buffer = new StringBuffer( 1024 ) ;
        buffer
            .append( ADQL_HEADER )
            .append( qu.query )
            .append( "\n" ) 
            .append( SQL_HEADER )
            .append( sql ) ;
        
        StringReader reader = null ;
        FileWriter writer = null ;
        try {
            reader = new StringReader( buffer.toString() ) ;
            writer = new FileWriter( file ) ;
            int ch = reader.read() ;
            while( ch != -1 ) {
                writer.write( ch ) ;
                ch = reader.read() ;
            }
        }
        finally {
            try{ reader.close() ; } catch( Exception ex ) { ; }
            try{ writer.flush(); writer.close() ; } catch( Exception ex ) { ; } 
        }
    }
    
    public void run() {
        int threadQueryCount = 0 ;
        AdqlQueryUnit qu = getOneQuery() ;
        while( qu != null ) {
            try {
                String sql = compiler.compileToSQL( qu.query ) ;
                print( Thread.currentThread().getName() + " -> Compiled without mishap: " + qu.file.getName() ) ;
                recordQueryResult( qu, sql ) ;
            }
            catch( AdqlException aex ) {
                if( qu.valid == true ) {
                    String[] messages = aex.getMessages() ;
                    StringBuffer buffer = new StringBuffer( messages.length * 64 ) ;
                    for( int i=0; i<messages.length; i++ ) {
                        buffer
                            .append( Thread.currentThread().getName() )
                            .append( ": Compilation " )
                            .append( qu.file.getName() )
                            .append( " suffered an ADQL Exception. Messages follow... \n" )
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
            threadQueryCount++ ;
            qu = getOneQuery() ;
        } 
        log.debug( threadQueryCount + " queries completed: " + Thread.currentThread().getName() ) ;
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
            if( file.isDirectory() 
                &&
                !file.getName().equals( "SVRecordings" ) ) { 
                return true ;
            }
            return false ;
        }
    }
    
}
/*
$Log: AdqlCompilerSVTest.java,v $
Revision 1.4  2007/08/08 15:22:16  jl99
Some docs

Revision 1.3  2007/08/08 13:24:09  jl99
Recording results implemented

Revision 1.2  2007/08/08 09:31:04  jl99
Debugging test environment for AdqlCompilerSV

Revision 1.1  2007/08/07 17:37:05  jl99
Initial multi-threaded test environment for AdqlCompilerSV

*/