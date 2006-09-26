/*$Id: AdqlStoXTest.java,v 1.1 2006/09/26 19:53:03 jl99 Exp $
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import org.custommonkey.xmlunit.XMLTestCase;
import java.util.ArrayList ;
import java.util.ListIterator ;
import java.io.File ;
import java.net.URL ;
import java.net.URI ;
import java.io.FileFilter ;
import java.io.FileReader ;
import java.io.FileNotFoundException ;

import org.astrogrid.adql.v1_0.beans.*;

/**
 * AdqlStoXTest
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Sep 26, 2006
 */
public class AdqlStoXTest extends XMLTestCase {
	
	final static String README = "README" ;
	final static String[] versions = { "074", "1_0" } ;

	static File[][] sFileArray = null ;
	static int sOffset = -1 ;
	static AdqlStoX sCompiler = null ;
	
	static File sCurrentSFile ;
	static File sCurrentXFile ;
	

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		if( sFileArray == null ) {
			URL readme = this.getClass().getResource( README ) ;
			File directory = new File( new URI( readme.toString() ) ).getParentFile() ;		
			for( int i=0 ; i<versions.length ; i++ ) {
				sFileArray[i] = new File( directory.getAbsolutePath() + File.separator + versions[i] ).listFiles( new AdqlsFilter() ) ;
			}
		}
	}
	
	public void testEverything() throws Exception {
		
		SelectDocument sd = null ;
		
		for( int i=0; i<sFileArray.length; i++ ) {
			System.out.println( "found file: " + sFileArray[i].getName() ) ;
			sd = getCompiler( sFileArray[i] ).exec() ;
		}
		
		if( sd != null )
			System.out.println( sd.toString() ) ;
		else 
			System.out.println( "compilation failed" ) ;
		
	}
	
//	private void suiteTest(String name, String version, boolean expectIdentical)   throws Exception
//	   {
//	      AdqlTestHelper helper = new AdqlTestHelper();
//	      printHelpfulStuff(name);
//
//	      String adql = helper.getSuiteAdqlString(name, version); 
//	      String compareAdql;
//	      if (expectIdentical) {
//	         compareAdql = helper.getSuiteAdqlString(name, "v1_0");
//	      }
//	      else {
//	         compareAdql = helper.getSuiteAdqlStringExpected(name, "v1_0");
//	      }
//	      roundParse(adql, compareAdql);
//	   }
	
	private AdqlStoX getCompiler( File file ) throws FileNotFoundException {
		if( sCompiler == null ) {
			sCompiler = new AdqlStoX( new FileReader( file ) ) ;
		}
		else {
			sCompiler.ReInit( new FileReader( file ) ) ;
		}
		return sCompiler ;
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
 * Revision 1.1  2006/09/26 19:53:03  jl99
 * Initial unit test framework for AdqlStoX
 * 
 * 
*/