/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/src/java/org/astrogrid/community/common/util/ant/Attic/FileHandler.java,v $</cvs:source>
 * <cvs:author>$Author: dave $</cvs:author>
 * <cvs:date>$Date: 2003/09/24 15:47:38 $</cvs:date>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 *   $Log: FileHandler.java,v $
 *   Revision 1.1  2003/09/24 15:47:38  dave
 *   Added policy database loader tools.
 *
 * </cvs:log>
 *
 * A set of tools to make XML parsing easier.
 * Donated to AstroGrid by dave.morris@codon.demon.co.uk
 *
 */
package org.astrogrid.community.common.util.ant ;

import org.apache.tools.ant.Task ;
import org.apache.tools.ant.BuildException ;
import org.apache.tools.ant.DirectoryScanner ;
import org.apache.tools.ant.types.FileSet ;

import java.util.Vector ;
import java.util.Iterator ;
import java.util.Collection ;

import java.io.File ;

/**
 * An Ant task to process an set of files.
 * 
 */
public class FileHandler extends Task
	{
	/**
	 * Our debug flag.
	 *
	 */
	private static final boolean DEBUG_FLAG = false ;

	/**
	 * Construct a file handler.
	 *
	 */
	public FileHandler()
		{
		//
		// Initialise our base class.
		super() ;
		}

	/**
	 * Construct a nested file handler.
	 *
	 */
	public FileHandler(Task parent)
		{
		//
		// Initialise our base class.
		super() ;
		//
		// Set our project.
		setProject(parent.getProject()) ;
		}

	/**
	 * Our list of FileSets.
	 *
	 */
	private Collection filesets = new Vector();

	/**
	 * Add a FileSet to our list.
	 */
	public void addFileset(FileSet set)
		{
		if (DEBUG_FLAG) log("FileHandler::addFileset()");
		//
		// Add the FileSet to our list.
		filesets.add(set) ;
		}

	/**
	 * Our list of Files.
	 *
	 */
	private Collection files = new Vector();

	/**
	 * Set a specific File.
	 *
	 */
	public void setFile(File file)
		{
		if (DEBUG_FLAG) log("FileHandler::setFile()");
		//
		// Add the file to our list.
		addFile(file) ;
		}

	/**
	 * Add a File to our list.
	 *
	 */
	public void addFile(File file)
		{
		if (DEBUG_FLAG) log("FileHandler::addFile()");
		//
		// Add the file to our list.
		files.add(file) ;
		}

	/**
	 * Check if our list is empty.
	 *
	 */
	public boolean isEmpty()
		{
		return files.isEmpty() ;
		}

	/**
	 * Check how many Files we have in our list.
	 *
	 */
	public int size()
		{
		return files.size() ;
		}

	/**
	 * Initialise our Task.
	 *
	 */
	public void init()
		throws BuildException
		{
		if (DEBUG_FLAG) log("FileHandler::init()");
		}

	/**
	 * Execute our Task.
	 *
	 */
	public void execute()
		throws BuildException
		{
		if (DEBUG_FLAG) log("FileHandler::execute()");
		//
		// Initialise our list of files.
		initFiles() ;
		//
		// Process our list of files.
		processFiles() ;
		}

	/**
	 * Initialise our list of Files.
	 *
	 */
	protected void initFiles()
		{
		if (DEBUG_FLAG) log("FileHandler::initFiles()");
		//
		// For each of our FileSets.
		Iterator iter = filesets.iterator() ;
		while (iter.hasNext())
			{
			FileSet set = (FileSet) iter.next() ;
			DirectoryScanner scanner = set.getDirectoryScanner(getProject()) ;
			//
			// Get a list of file names.
			String[] names = scanner.getIncludedFiles() ;
			for (int i = 0 ; i < names.length ; i++)
				{
				//
				// Add the File to our list.
				addFile(new File(set.getDir(getProject()), names[i])) ;
				}
			}
		}

	/**
	 * Process our list of Files.
	 *
	 */
	protected void processFiles()
		{
		if (DEBUG_FLAG) log("FileHandler::processFiles()");
		//
		// If we have some Files.
		if (false == files.isEmpty())
			{
			//
			// Process each of our Files.
			Iterator iter = files.iterator() ;
			while (iter.hasNext())
				{
				processFile((File) iter.next()) ;
				}
			}
		}

	/**
	 * Process a File.
	 *
	 */
	public void processFile(File file)
		throws BuildException
		{
		if (DEBUG_FLAG) log("FileHandler::processFile()");
		if (DEBUG_FLAG) log("  file : " + file);
		}
	}
