/*
 *
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/java/org/astrogrid/portal/services/myspace/client/data/Attic/DataNodeWalker.java,v $</cvs:source>
 * <cvs:date>$Author: dave $</cvs:date>
 * <cvs:author>$Date: 2003/06/24 10:43:25 $</cvs:author>
 * <cvs:version>$Revision: 1.1 $</cvs:version>
 *
 * <cvs:log>
 * $Log: DataNodeWalker.java,v $
 * Revision 1.1  2003/06/24 10:43:25  dave
 * Fixed bugs in DataTreeWalker and tree page
 *
 * Revision 1.1  2003/06/22 04:03:41  dave
 * Added actions and parsers for MySpace messages
 *
 * <cvs:log>
 *
 *
 */
package org.astrogrid.portal.services.myspace.client.data ;

import java.util.Iterator ;

/**
 * A gadget to recrsively walk down a tree of DataNodes.
 *
 */
public class DataNodeWalker
	{
	/**
	 * Switch for our debug statements.
	 *
	 */
	public static boolean DEBUG_FLAG = true ;

	/**
	 * Step down a tree of nodes.
	 *
	 */
	public void walk(DataNode node)
		{
		step(node, false, 0, 0) ;
		}

	/**
	 * Step down a node.
	 *
	 */
	protected void step(DataNode node, boolean more, int index, int level)
		{
		//
		// Open our node
		open(node) ;
		//
		// Step through each of the child nodes.
		Iterator iter = node.iterator() ;
		for (int i = 0 ; iter.hasNext() ; i++)
			{
			//
			// Step down the next node in the tree.
			DataNode next = (DataNode) iter.next() ;
			step(next, (iter.hasNext()), i, (level + 1)) ;
			}
		//
		// Close our node
		close(node) ;
		}

	/**
	 * The method to call at the start of each level.
	 *
	 */
	public void open(DataNode node)
		{
		}

	/**
	 * The method to call at the end of each level.
	 *
	 */
	public void close(DataNode node)
		{
		}
	}

