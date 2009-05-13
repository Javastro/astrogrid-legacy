/*
   $Id: NodeWriter.java,v 1.1 2009/05/13 13:20:25 gtr Exp $

   (c) Copyright...
*/
package org.astrogrid.dataservice.out.tree;

import java.io.IOException;

/**
 * Defines the basic methods required in order to be able to write to a heirarchical
 * data set, eg XML documents or HDF
 */

public interface NodeWriter
{
   
   /**
    * Called to take the given node and start writing out to it as a child of
    * this node.  If there is an existing child, it should close that child before
    * opening this one.
    */
   public NodeWriter newNode(NodeWriter tag) throws IOException;

   /**
    * Returns true if this node has an unclosed child node */
   public boolean hasOpenChild();

   /**
    * Close this node and its children
    */
   public void close() throws IOException;
   
}

/*
 $Log: NodeWriter.java,v $
 Revision 1.1  2009/05/13 13:20:25  gtr
 *** empty log message ***

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.1.2.1  2004/11/25 18:33:43  mch
 more status (incl persisting) more tablewriting lots of fixes


 */

   
