/**
 * $Id: NodeHtmlRenderer.java,v 1.1 2005/02/16 19:57:10 mch Exp $
 */
package org.astrogrid.storebrowser.html;
import java.io.IOException;
import java.io.Writer;
import javax.swing.tree.TreeNode;

/**
 * Renders a StoreNode in HTML
 */

public interface NodeHtmlRenderer  {

   /** renders the given TreeNode in html */
   public void writeNode(TreeNode node, String[] openPaths, String selectedPath, Writer out) throws IOException;
   
}
