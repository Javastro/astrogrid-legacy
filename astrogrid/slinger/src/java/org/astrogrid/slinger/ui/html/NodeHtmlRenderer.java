/**
 * $Id: NodeHtmlRenderer.java,v 1.2 2004/12/07 01:33:36 jdt Exp $
 */
package org.astrogrid.slinger.ui.html;
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
