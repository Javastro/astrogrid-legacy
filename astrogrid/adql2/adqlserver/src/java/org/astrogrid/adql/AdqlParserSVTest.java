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
import java.io.FileFilter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import javax.xml.transform.TransformerException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.adql.InvalidStateException;

/**
 * A command line/batch testing programme used in the Astrogrid development project for ADQL
 * to test pooling of parsers, converters and transformers in a multi-threading environment.
 * Unfortunately this does not use any JUnit testing facilities, but does use the ADQL queries
 * defined for the purposes of unit testing from the Astrogrid project, and is therefore
 * specific to the Astrogrid development environment for this project.
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Aug 7, 2007
 */
public class AdqlParserSVTest implements AdqlParserSV.ParserInitCallBack
                                       , AdqlParserSV.PostParseCallBack
                                       , Runnable {
    
    private static Log log = LogFactory.getLog( AdqlParserSVTest.class ) ;
    
    private static final String USAGE =
        "Usage: AdqlParserSVTest [options]" +
        "Options:\n" +
        "-s=style_sheet_name\n" +
        "-d=directory_path\n" +
        "-c=y[es] or n[o]\n" +
        "Where:\n" +
        "  -s is the only mandatory option.\n" +
        "  \"style_sheet_name\" is the name of the style sheet used to convert ADQL/x to SQL variant.\n" +
        "  \"directory_path\" is a fully qualified path of a directory where output files will be written.\n" +
        "  Existing files will not be overwritten.\n" +
        "  -c option indicates testing the version with callbacks [yes] or the version without callbacks [no].\n" +
        "  -c defaults to without callbacks if not specified" ;
    
    private static final String ADQL_HEADER =
        "+=====================================================+\n" +
        "|  Input ADQL:                                        |\n" +
        "+=====================================================+\n" ; 
    
    private static final String SQL_HEADER =
        "+=====================================================+\n" +
        "|  Output SQL:                                        |\n" +
        "+=====================================================+\n" ; 
           
    private final static String README = "README_FOR_QUERIES" ;
    private final static String BAD_INIT_MESSAGE = "AdqlParserSVTest. Initialization failed: " ;
    private final static int TEST_MAX_PARSERS = 4 ;
    private final static int TEST_THREADS = 4 ;
    private final static String SVRECORDINGS = "SVRecordings" ;
    
    private class AdqlQueryUnit {
        File file ;
        String query ;
        boolean valid ;
    }
    
    private HashMap<String, AdqlQueryUnit> queries = new HashMap<String, AdqlQueryUnit>( 256 ) ;
    private Iterator<AdqlQueryUnit> queryIterator ;
    private IAdqlParser parser ;
    private int queryCount = 0 ;
    private String styleSheetName ;
    private String recordingDirPath ;
    private boolean bCallBackTest = false ;
    
    /**
     *  Usage: AdqlParserSVTest [options] <br/>
     *  Options: <br/>
     *	-s=style_sheet_name <br/>
     *  -d=directory_path <br/>
     *  -c=y[es] or n[o] <br/>
     *  Where: <br/>
     *    -s is the only mandatory option. <br/>
     *    "style_sheet_name" is the name of the style sheet used to convert ADQL/x to SQL variant. <br/>
     *    "directory_path" is a fully qualified path of a directory where output files will be written. <br/>
     *    Existing files will not be overwritten. <br/>
     *    -c option indicates testing the version with callbacks [yes] or the version without callbacks [no]. <br/>
     *    -c defaults to without callbacks if not specified <br/> 
     * 
     * @param args The command line arguments
     */
    public static void main(String args[]) {
        if( log.isTraceEnabled() ) log.trace( "entry: main()" ) ;
        
        AdqlParserSVTest svTest = new AdqlParserSVTest() ;
        
        if( svTest.retrieveArgs( args) == false ) {
            svTest.print( "Invalid arguments..." ) ;
            svTest.print( USAGE ) ;
        }
        else {
            svTest.print( "args:\n " +
            		      "  -d=" + svTest.recordingDirPath +
            		   "\n   -s=" + svTest.styleSheetName +
            		   "\n   -c=" + svTest.getCallbackArg() ) ;
            try {
                svTest.init() ;              
                svTest.print( "Started parsing: " + new Date() ) ;
                svTest.parseQueries() ;
                svTest.waitForCompletion() ;
                svTest.print( "Finished parsing: " + new Date()) ;
                svTest.print( "Query count: " + svTest.queryCount ) ;                
                svTest.print( "Transformer used for conversion: " + svTest.getTransformerUsed() ) ;
            }
            catch( Exception ex ) {
                svTest.print( "AdqlParserSVTest failed.", ex ) ;
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
                    this.recordingDirPath = args[i].substring(3) ;
                }
                else if( args[i].startsWith( "-c=" ) ) {
                	String cArg = args[i].substring(3).toLowerCase() ;
                	if( cArg.length() >= 1 ) {
                		if( cArg.startsWith( "y" ) ) {
                			this.bCallBackTest = true ;
                		}
                	}
                }
                else {
                    break ;
                }
            }             
        }       
        return retVal ;
    }
    
    private String getCallbackArg() {
    	if( bCallBackTest == true ) {
    		return "yes" ;
    	}
    	return "no" ;
    }
    
    public AdqlParserSVTest() {
        super() ;
    }
    
    private void parseQueries() {
        if( log.isTraceEnabled() ) log.trace( "entry: parseQueries()" ) ;
        for( int i=0; i<TEST_THREADS; i++ ) {
            Thread qThread = new Thread( this ) ;
            qThread.setName( "SVTest worker thread " + (i+1) ) ;
            qThread.start() ;
        }  
        if( log.isTraceEnabled() ) log.trace( "exit: parseQueries()" ) ;
    }
    
    private synchronized AdqlQueryUnit getOneQuery() {
        if( this.queryIterator == null ) {
            queryIterator = queries.values().iterator() ;
        }
        if( queryIterator.hasNext() ) {        	
        	AdqlQueryUnit aqu = queryIterator.next() ;
        	print( Thread.currentThread().getName() + " -> returning query: " + aqu.file.getName() ) ;
            queryCount++ ;
        	return aqu ;
        }
        print( Thread.currentThread().getName() + " -> returning null query" ) ;
        return null ;
    }
    
    private void init() throws SVTestInitializationException {
        initFiles() ;
        initParser() ;
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
                        ;
                    }
                }               
            }
        }
        if( log.isTraceEnabled() ) log.trace( "exit: waitForCompletion()" ) ;
    }
    
    private void initFiles() throws SVTestInitializationException {
        if( log.isTraceEnabled() ) log.trace( "entry: initFiles()" ) ;
        try {
            //
            // Locate the directory that holds the README file
            // retrieve all the Adql/s queries and save them in a collection...         
            URL readme = this.getClass().getClassLoader().getResource( README ) ;
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
    
    private void initParser() throws SVTestInitializationException {
        if( log.isTraceEnabled() ) log.trace( "entry: initParser()" ) ;
        try {
        	
        	if( bCallBackTest == true ) {
        	    AdqlParserSV parserWithCallbacks = new AdqlParserSV() ;
                parserWithCallbacks.setMaxParsers( TEST_MAX_PARSERS ) ;
                parserWithCallbacks.setStyleSheet( getStyleSheet() ) ;
                parserWithCallbacks.registerParseInitCallBack( this ) ;
                parserWithCallbacks.registerPostParseCallBack( this ) ;
                this.parser = parserWithCallbacks ;
        	}
        	else {
        	    AdqlParserSVNC parserWithoutCallbacks = new AdqlParserSVNC() ;
        		parserWithoutCallbacks.setMaxParsers( TEST_MAX_PARSERS ) ;
        		parserWithoutCallbacks.setStyleSheet( getStyleSheet() ) ;        		
        		parserWithoutCallbacks.setStrictSyntax( AdqlParser.V20_AGX ) ;
        		parserWithoutCallbacks.setUserDefinedFunctionPrefix( "" ) ;  
        		this.parser = parserWithoutCallbacks ;
        	}
        	 print( "Using: " + parser.getClass().getSimpleName() ) ;
        }
        catch( Exception ex ) {
            throw new SVTestInitializationException( BAD_INIT_MESSAGE, ex ) ;
        } 
        if( log.isTraceEnabled() ) log.trace( "exit: initParser()" ) ;
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
		 * 
		 */
		private static final long serialVersionUID = 2574764618980363675L;

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
        InputStream is = AdqlParserSV.class.getClassLoader().getResourceAsStream( this.styleSheetName ) ;
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
        if( this.recordingDirPath == null ) 
            return ;
        try {         
            String path = recordingDirPath 
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
    
    /**
     * Basic invocation of a set of queries to be parsed into SQL.
     * Used by multiple threads.
     * 
     * @see java.lang.Runnable#run()
     */
    public void run() {
        int threadQueryCount = 0 ;
        AdqlQueryUnit qu = getOneQuery() ;
        while( qu != null ) {
            try {
                String sql ;
                if( bCallBackTest ) {
                	sql = parser.parseToSQL( qu.query ) ;
                }
                else {
                	sql = parser.transformToSQL( parser.parseToXML( qu.query ) ) ;
                }
                print( Thread.currentThread().getName() + " -> Parsed without mishap: " + qu.file.getName() ) ;
                recordQueryResult( qu, sql ) ;
            }
            catch( AdqlException aex ) {
                if( qu.valid == true ) {
                    String[] messages = aex.getErrorMessages() ;
                    StringBuffer buffer = new StringBuffer( messages.length * 64 ) ;
                    for( int i=0; i<messages.length; i++ ) {
                        buffer
                            .append( Thread.currentThread().getName() )
                            .append( ": Parse " )
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
                th.printStackTrace() ;
            }
            threadQueryCount++ ;
        	Thread.yield() ;
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
                !file.getName().equals( SVRECORDINGS ) ) { 
                return true ;
            }
            return false ;
        }
    }

    /**
     * @see org.astrogrid.adql.AdqlParserSV.PostParseCallBack#execMidPoint(org.apache.xmlbeans.XmlObject)
     */
    public XmlObject execMidPoint(XmlObject xo) {
        return xo;
    }

    /**
     * @see org.astrogrid.adql.AdqlParserSV.ParserInitCallBack#execInit(org.astrogrid.adql.AdqlParser)
     */
    public void execInit(AdqlParser ap) {
        ap.setSyntax( AdqlParser.V20_AGX ) ;
        ap.setUserDefinedFunctionPrefix( "" ) ;    
    }
    
    private String getTransformerUsed() {
    	if( this.parser instanceof AdqlParserSV ) {
    		return ((AdqlParserSV)this.parser).getTransformerUsed() ;
    	}
    	return ((AdqlParserSVNC)this.parser).getTransformerUsed() ;
    }
    
}