/**
 * $Id: TreeHtmlRenderer.java,v 1.1 2005/02/16 19:57:12 mch Exp $
 */
package org.astrogrid.storebrowser.html;

import java.io.IOException;
import java.io.Writer;
import java.security.Principal;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;

/**
 * Represents a TreeModel in HTML; doesn't display root
 */

public class TreeHtmlRenderer extends RendererSupport {
   TreeModel model = null;
   
   /** person viewing these files */
   private Principal user = null;
   
   /** Node renderer */
   NodeHtmlRenderer nodeRenderer = null;
   
   public static final String hdrClr = "#BBBBBB";

   String[] openPaths;
   
   /** Create a view onto files with the given root storefile */
   public TreeHtmlRenderer(TreeModel givenTree, Principal aUser) {
      this.model = givenTree;
      this.user = aUser;
   }

   public void setNodeRenderer(NodeHtmlRenderer newRenderer)
   {
      nodeRenderer = newRenderer;
   }
   
   /**
    * Returns the model */
   public TreeModel getModel() {
      return model;
   }

   public void setOpenPaths(String[] paths) {
      openPaths = paths;
   }
   

   /** Writes the panel that contains the list of files */
   public void writeView(String selectedPath, Writer out) throws IOException {
 
      out.write("\n<div id='files'>\n"+
                                    "<table align='full' class='tree'>\n");
      
      out.flush();

      if (openPaths == null) { openPaths = new String[] { }; }
      
      if ((model == null) || (model.getChildCount(model.getRoot()) == 0)) {
         out.write("</table><p align='center'>No entries to list</p></div>\n");
         return;
      }
      else {
         //write root's children
         for (int i=0;i<model.getChildCount(model.getRoot());i++) {
            nodeRenderer.writeNode((TreeNode) model.getChild(model.getRoot(), i), openPaths, selectedPath, out);
         }
      }
      
      out.write("</table></div>\n");
      
   }
   
   /** creates an indent string for the given file */
   public static String getIndent(String imageRef, TreeNode node)  {
      
      //don't display root, so don't need branchy bits for children of root
      if ((node.getParent() == null) || (node.getParent().getParent()==null)) {
         return "";
      }
      
      int level = 0;
      while (node.getParent() != null) {
         node = node.getParent();
         level++;
      }

      StringBuffer indent = new StringBuffer();
      for (int i = 0; i < level-2; i++)
      {
         indent.append("<img src='"+imageRef+"TreeTrunk.gif' alt='&nbsp;&nbsp;&nbsp;' border='0'/>");
        //indent.append("&nbsp;|&nbsp;");
      }
      return "<tt>"+
         indent.toString()+
         "<img src='"+imageRef+"TreeBranch.gif' alt='&nbsp;&nbsp;&nbsp;' border='0'/>"+
         "</tt>";
      /*
      return "<table cellpadding='"+i*6+"'><tr><td>&nbsp;</td></tr></table>";
       */
   }
   
   
}
