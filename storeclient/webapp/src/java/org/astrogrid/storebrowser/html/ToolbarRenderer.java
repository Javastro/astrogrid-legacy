/**
 * $Id: ToolbarRenderer.java,v 1.1 2005/02/16 15:02:46 mch Exp $
 */
package org.astrogrid.storebrowser.html;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Hashtable;
import org.astrogrid.storebrowser.swing.models.StoreFileNode;

/**
 * An object representing a set of 'actions' that are displayed as a 'toolbar'
 * html table.  Actually the actions are hardwired just now...
 */
public class ToolbarRenderer extends RendererSupport  {
   
   String refRoot = ".";
  
   /** Where to find the images */
   String imagesRef = "";

   Hashtable actions = new Hashtable();

   /** Standard actions */
   public final static Action BACK = new Action("Back", null, "Back.gif", "Back");
   public final static Action FORWARD = new Action("Forward", null, "Forward.gif", "Next");
   public final static Action UP = new Action("Up", null, "Up.gif", "Up");

   public final static Action COPY = new Action("Copy", null, "Copy.gif", "Copy");
   public final static Action MOVE = new Action("Move", null, "Move.gif", "Move");
   public final static Action DELETE = new Action("Delete", null, "Delete.gif", "Delete");

   public final static Action NEWFILE = new Action("NewFile", null, "NewFile.gif", "New File");
   public final static Action NEWFOLDER = new Action("NewFolder", null, "NewFolder.gif", "New Folder");
   public final static Action ADDSTORE = new Action("AddStore", null, "AddStore.gif", "Add Store");
   
   public final static Action REFRESH = new Action("Refresh", null, "Refresh.gif", "Refresh");
   
   
   /** Servlet name is required for the links */
   public ToolbarRenderer(String servletName, String imageDir) {
      refRoot = servletName;
      imagesRef = imageDir;
   }

   
   /** Actions are just wrappers to group properties together */
   public static class Action {
      String name;
      String href;
      String imgName;
      String text;
      boolean enabled = true;
      
      private Action(String aName, String aRef, String anImageName, String someText) {
         name = aName;
         href = aRef;
         imgName = anImageName;
         text = someText;
      }
      
      /** convenience constructor where imagename=name.gif, text=name, ref =null */
      private Action(String aName) {
         this(aName, null, aName+".gif", aName);
      }
   }

   /*
   private static Action makeAction(String aName) {
      return new Action(aName, null, aName+".gif", aName);
   }
    */
   
   /** Adds an 'action' - ie description of an action, which will be used to
    * create the button */
   public void addAction(String name, String href, String imgName, String text) {
      actions.put(name, new Action(name, href, imgName, text));
   }

   /** Adds an 'action' - ie description of an action, which will be used to
    * create the button */
   public void addAction(Action action) {
      actions.put(action.name, action);
   }

   /** disables/enables an action */
   public void setEnabled(String actionName, boolean enable) {
      ((Action) actions.get(actionName)).enabled = enable;
   }
   
   /** disables/enables an action */
   public void setEnabled(Action action, boolean enable) {
      action.enabled = enable;
   }
   
   /** Sets the link of an action */
   public void setLink(String actionName, String link) {
      ((Action) actions.get(actionName)).href = link;
   }

   /** Sets the link of an action */
   public void setLink(Action action, String link) {
      action.href = link;
   }
   
   /** Creates suitable html for a toolbar button - basically an image with
    * a link if enabled */
   public String renderToolButton(Action action) {
      if (action == null) {
         return "";
      }
      if (action.enabled) {
         //if it's enabled, include a link around it
         return "<a href='"+action.href+"'>"+
            "<img src='"+imagesRef+action.imgName+"' alt='"+action.text+"' border='0'/>"+
         "</a>";
      }
      else {
         return "<img src='"+imagesRef+action.imgName+"' alt='"+action.text+"' border='0'/>";
      }
   }

   /** Goes through the actions rendering them; it does so against 'standard names' which
    * isn't quite right, but it's useful for grouping them... If the name is missing (ie the owner
    * hasn't done an addAction() for them, they just won't appear */
   public String renderToolBar(String selectedPath, StoreFileNode selectedNode) throws IOException, URISyntaxException {
      StringBuffer toolbarhtml = new StringBuffer();

      boolean isFolderSelected = false;
      if ((selectedNode != null) && (selectedNode.getFile() != null) && (selectedNode.getFile().isFolder())) {
         isFolderSelected = true;
      }
      
      String encodedPath = null;
      String upPath = null;
      
      if (selectedPath != null) {
         encodedPath = URLEncoder.encode(selectedPath, "UTF-8");
         if (selectedPath.indexOf("/")>-1) {
            upPath = URLEncoder.encode(selectedPath.substring(0, selectedPath.lastIndexOf("/")), "UTF-8");
         }
      }
      
      toolbarhtml.append(
         "<div id='toolbar' style='top'>"+
            "<table><tr><td>"+
            "<table><tr>"+
            "  <td class='toolbarButton'>"+renderToolButton(BACK)+"</td>"+
            "  <td class='toolbarButton'>"+renderToolButton(FORWARD)+"</td>"+
            "  <td class='toolbarButton'>"+renderToolButton(UP)+"</td>"+
            "</tr></table>  "+
            "</td><td>"+
            "<table><tr>"+
//            "  <td class='toolbarButton'>"+renderToolButton(refRoot+"?copy='"+encodedPath+"'", "Copy.gif", "Copy", (selectedPath != null) && (!isFolderSelected))+"</td>"+
//            "  <td class='toolbarButton'>"+renderToolButton(refRoot+"?move='"+encodedPath+"'", "Move.gif", "Move", (selectedPath != null) && (!isFolderSelected))+"</td>"+
            "  <td class='toolbarButton'>"+renderToolButton(DELETE)+"</td>"+
            "  <td class='toolbarButton'>"+renderToolButton(NEWFILE)+"</td>"+
            "  <td class='toolbarButton'>"+renderToolButton(NEWFOLDER)+"</td>"+
            "  <td class='toolbarButton'>"+renderToolButton(ADDSTORE)+"</td>"+
            "</tr></table>  "+
            "</td><td>"+
            "<table><tr>"+
            "  <td class='toolbarButton'>"+renderToolButton(REFRESH)+"</td>"+
            "</tr></table>  "+
            "</td></tr></table>"+
            "</div>");
      return toolbarhtml.toString();
   }
   
}
