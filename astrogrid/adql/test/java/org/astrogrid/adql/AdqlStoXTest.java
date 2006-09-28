/*$Id: AdqlStoXTest.java,v 1.3 2006/09/28 15:08:08 jl99 Exp $
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
import java.lang.reflect.*;
import java.util.Hashtable;
import java.util.Enumeration;


import org.astrogrid.xml.DomHelper;

import org.astrogrid.adql.v1_0.beans.*;
import org.apache.xmlbeans.*;

/**
 * AdqlStoXTest
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Sep 26, 2006
 */
public class AdqlStoXTest extends XMLTestCase {
	
	final static String README = "README" ;
	final static String BAD_INIT_MESSAGE = "AdqlStoXTest. Initialization failed: " ;
	final static String BAD_SETUP_MESSAGE = "AdqlStoXTest. Method setUp() failed: " ;

	private static AdqlStoX sCompiler = null ;
	private static boolean sInitialized = false ; 
	private static boolean sBadInitializedStatus = false ;
	private static File sDirectoryOfREADME = null ;
	
	private File currentSFile ; 
	private File currentXFile ;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		if( sBadInitializedStatus == true ) {
			String message = BAD_INIT_MESSAGE + "see message from earlier initialization failure" ;
			throw new InitializationException( message ) ;
		}
		if( sInitialized == false ) {
			init() ;
		}
		locateFilesForTest() ;
	}
	

	public void testOf_v10_BADemptyFrom() throws Exception { execTest() ; }
	public void testOf_v10_BADemptyWhere() throws Exception { execTest() ; }
	public void testOf_v10_BADselectEmptyAlias() throws Exception { execTest() ; }
	public void testOf_v10_BADselectOrderByDirOnly() throws Exception { execTest() ; }
	public void testOf_v10_selectAggregateFuncs() throws Exception { execTest() ; }
	public void testOf_v10_selectAliasExpr() throws Exception { execTest() ; } 
	public void testOf_v10_selectAllAllow() throws Exception { execTest() ; } 
	public void testOf_v10_selectAllLimit() throws Exception { execTest() ; } 
	public void testOf_v10_selectBetweenOps() throws Exception { execTest() ; } 
	public void testOf_v10_selectBinaries() throws Exception { execTest() ; }
	public void testOf_v10_selectBoolOps() throws Exception { execTest() ; }

	
	private void execTest() throws Exception {
		SelectDocument sd = null ;
		printHelpfulStuff( currentSFile.getName() ) ;
		System.out.println( "== From: ===>" ) ;
		printFile( currentSFile ) ;
		sd = getCompiler( currentSFile ).exec() ;		
		if( sd != null ) {
			System.out.println( "\nCompilation suceeded..." ) ;			
			System.out.println( "==== To: ===>" ) ;
			printCompilation( sd ) ;			
			assertTrue( currentSFile.getName() + ": Compilation succeeded when not expected.", currentXFile != null ) ;	
			compareCompilations( sd, currentXFile ) ;
		}
		else {
			assertTrue( currentSFile.getName() + ": Compilation failed when not expected.", currentXFile == null ) ;
		}
	}
	
	private void locateFilesForTest() throws InitializationException {
		currentSFile = null ;
		currentXFile = null ;
		String testMethodName = this.getName() ;
		String[] parts = testMethodName.split("_") ;
		String path = sDirectoryOfREADME.getAbsolutePath() 
		            + File.separator 
		            + parts[1]
		            + File.separator
		            + parts[2] 
		            + ".adqls" ;
		File file = new File( path ) ;
		if( file.exists() ) {
			currentSFile = file ;
		}
		else {
			throw new InitializationException( BAD_INIT_MESSAGE 
					                         + "Method "
					                         + testMethodName
					                         + ": corresponding .adqls file not found" ) ;
		}
		path = sDirectoryOfREADME.getAbsolutePath() 
             + File.separator 
             + parts[1]
             + File.separator
             + parts[2] 
             + ".adqlx" ;
		file = new File( path ) ;
		if( file.exists() ) {
			currentXFile = file ;
		}
	}
	
	private void compareCompilations( SelectDocument selDoc, File xmlFile ) throws Exception {

		Document compiledDom = null ;
		Document fileDom = null ;
		
		compiledDom = DomHelper.newDocument( selDoc.toString() ) ;
		fileDom = DomHelper.newDocument( SelectDocument.Factory.parse( xmlFile ).toString() ) ;

		// Normalize just to be sure 
		compiledDom.normalize();
		fileDom.normalize();

		// Using xmlunit to compare documents
		setIgnoreWhitespace(true);
		assertXMLEqual("Adql/s does not compile to what is expected!",compiledDom, fileDom);
	}
	
	private AdqlStoX getCompiler( File file ) throws FileNotFoundException {
		if( sCompiler == null ) {
			sCompiler = new AdqlStoX( new FileReader( file ) ) ;
		}
		else {
			sCompiler.ReInit( new FileReader( file ) ) ;
		}
		return sCompiler ;
	}
	
	private void init() throws InitializationException {
		// Assume the worst...
		sBadInitializedStatus = true ;
		
		try {
			//
			// Work out the number of tests...	
			// And also attempt to extract the directories involved...
			Method[] methodArray = this.getClass().getMethods() ;
			int testMethodCount = 0 ;
			Hashtable directories = new Hashtable() ;
			for( int i=0; i<methodArray.length; i++ ) {
				String methodName = methodArray[i].getName() ;
				if( methodName.startsWith( "testOf_" ) ) {
					testMethodCount++ ;
					String[] paths = methodName.split( "_" ) ;
					directories.put( paths[1], paths[1] ) ;
				}
			}
			
			ArrayList fileList = new ArrayList() ;
			File[] fileArray = null ;
			URL readme = null ;
			
			//
			// Locate the directory that holds the README file...			
			readme = this.getClass().getResource( README ) ;
			sDirectoryOfREADME = new File( new URI( readme.toString() ) ).getParentFile() ;
			
			//
			// Count all of the files with an .adql file extension...
			Enumeration en = directories.elements() ;
			while( en.hasMoreElements() ) {
				String directory = (String)en.nextElement() ;
				fileArray = new File( sDirectoryOfREADME.getAbsolutePath() 
						            + File.separator 
						            + directory ).listFiles( new AdqlsFilter() ) ;
				for( int j=0; j<fileArray.length; j++ ) {
					fileList.add( fileArray[j] ) ;
				}
			}
			
			//
			// If the number of methods doesn't match the number of files,
			// Then issue a warning message...
			if( testMethodCount != fileList.size() ) {
				System.out.println( "\nAdqlStoXTest. WARNING. Number of test methods does not match available test files." ) ;
				System.out.println( "   Methods count: " + testMethodCount ) ;
				System.out.println( "   File count: " + fileList.size() ) ;
				System.out.println( 
						"   You may encounter failures within a test or complete absence of some tests.\n"
					+   "   Please inspect AdqlStoXTest for test methods and your test files.\n"					
				) ;
			}			
			sBadInitializedStatus = false ;
			sInitialized = true ;
		}
		catch( Exception ex ) {
			throw new InitializationException( BAD_INIT_MESSAGE + ex.getLocalizedMessage() ) ;
		}
	
	}
	
	protected void printHelpfulStuff(String filename) {
		System.out.println("------------------------------------------------");
		System.out.println("Compiling " + filename);
		System.out.println("------------------------------------------------");
	}
	
	private void printCompilation( SelectDocument sd ) {
		XmlOptions opts = new XmlOptions();
		opts.setSavePrettyPrint();
		opts.setSavePrettyPrintIndent(4);
		System.out.println(sd.xmlText(opts));
	}
	
	private void printFile( File file ) {
		FileReader reader = null ;
		try {
			reader = new FileReader( file ) ;
			int ch = reader.read() ;
			while( ch != -1 ) {
				System.out.print( (char)ch ) ;
				ch = reader.read() ;
			}
		    System.out.print( "\n" ) ;
		}
		catch( Exception iox ) {
			;
		}
		finally {
			try{ reader.close() ; } catch( Exception ex ) { ; }
		}
	}
	
	public class InitializationException extends Exception {

		/**
		 * @param message
		 * @param cause
		 */
		public InitializationException(String message, Throwable cause) {
			super(message, cause);
		}

		/**
		 * @param message
		 */
		public InitializationException(String message) {
			super(message);
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
	
}


/* $Log: AdqlStoXTest.java,v $
 * Revision 1.3  2006/09/28 15:08:08  jl99
 * New unit tests added.
 *
/* Revision 1.2  2006/09/28 13:35:15  jl99
/* Unit test harness established.
/*
/* Revision 1.1  2006/09/26 19:53:03  jl99
/* Initial unit test framework for AdqlStoX
/* 
 * 
*/