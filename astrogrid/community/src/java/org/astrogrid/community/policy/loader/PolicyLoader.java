/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/policy/loader/Attic/PolicyLoader.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/24 15:47:38 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: PolicyLoader.java,v $
 *   Revision 1.1  2003/09/24 15:47:38  dave
 *   Added policy database loader tools.
 *
 * </cvs:log>
 *
 *
 */
package org.astrogrid.community.policy.loader ;

import java.io.File ;

import org.apache.tools.ant.Task ;
import org.apache.tools.ant.BuildException ;

import org.astrogrid.community.common.util.xml.sax.SAXElementHandler ;
import org.astrogrid.community.common.util.xml.sax.SAXDocumentHandler ;

import org.astrogrid.community.common.util.ant.FileHandler ;

import org.astrogrid.community.policy.server.PolicyManager ;
import org.astrogrid.community.policy.server.PolicyManagerService ;
import org.astrogrid.community.policy.server.PolicyManagerServiceLocator ;

/**
 * An Ant task to process an set of files.
 * 
 */
public class PolicyLoader
	extends FileHandler
	{
	/**
	 * Our debug flag.
	 *
	 */
	private static final boolean DEBUG_FLAG = false ;

	/**
	 * Our manager locator.
	 *
	 */
	private PolicyManagerService locator ;

	/**
	 * Our manager.
	 *
	 */
	private PolicyManager manager ;

	/**
	 * Public constructor.
	 *
	 */
	public PolicyLoader()
		{
		super() ;
		}

	/**
	 * Public constructor.
	 *
	 */
	public PolicyLoader(Task parent)
		{
		super(parent) ;
		}

	/**
	 * Process a File.
	 *
	 */
	public void processFile(File file)
		throws BuildException
		{
		if (DEBUG_FLAG) log("") ;
		if (DEBUG_FLAG) log("----\"----") ;
		if (DEBUG_FLAG) log("PolicyLoader::processFile()");
		if (DEBUG_FLAG) log("  file : " + file);

		try {
			//
			// Initialise our PolicyManager.
			if (null == manager)
				{
				//
				// Create our manager locator.
				locator = new PolicyManagerServiceLocator() ;
				//
				// Create our manager.
				manager = locator.getPolicyManager() ;
				}

			//
			// Create a commnity data parser.
			SAXDocumentHandler parser = new SAXDocumentHandler() ;
			parser.addElementHandler(
				new PolicyDataParser(manager)
				) ;
			//
			// Parse our data file.
			parser.parse(file) ;
			}
		catch (Exception ouch)
			{
			throw new BuildException(ouch) ;
			}

		if (DEBUG_FLAG) log("----\"----") ;
		}
	}
