/**
 * $Id: StoresView.java,v 1.1 2004/11/08 23:15:38 mch Exp $
 */
package org.astrogrid.store.browser;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Hashtable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.datacenter.DsaDomHelper;
import org.astrogrid.registry.RegistryException;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.store.browser.Browser;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * An object that represents the list of stores in the 'navbar'; generates
 * HTML for the browser servlet
 */
public class StoresView  {

   /** map of store names to uris */
   Hashtable stores = new Hashtable();
   
   Browser browser = null;
   
   protected static final Log log = LogFactory.getLog(Browser.class);

   
   /** Constructor - populates nav bar for given user */
   public StoresView(Browser parent) {
      this.browser = parent;
//    stores.put("Disk", "file://");
      stores.put("HomeSpace", parent.getUser().getIvorn());
//      stores.put("Local", "myspace:"+new URL("http", request.getServerName(), request.getServerPort(), request.getContextPath()).toString() +"/services/Manager");
      stores.put("Twmbarlwm:8080", "myspace:http://twmbarlwm.star.le.ac.uk:8888/astrogrid-mySpace-SNAPSHOT/services/Manager");
      populateRegisteredStores();
   }
      
    /** Writes out view to response writer */
   public void writeView(HttpServletRequest request, HttpServletResponse response) throws IOException {
      response.getWriter().print(
         "<div id='storesView'>"+
            "<table border=2 color='#AAAAAA'><tr><td>"+
            "   <table border=0 cellspacing=4>");
      
      Enumeration storeKey = stores.keys();
      while (storeKey.hasMoreElements()) {
         String name = (String) storeKey.nextElement();
         String uri = (String) stores.get(name);
         
         response.getWriter().print(
            "   <tr><td align='center'><a href='"+Browser.refRoot+"store="+URLEncoder.encode(uri)+"&storeName="+URLEncoder.encode(name)+"'>"+name+"</a></td></tr>\n"
         );
      }

      //add special manual enter-your-own form
      response.getWriter().print(
         "   <tr><td><form action='Browser'>"+
                  "<input type='text' name='store'/>"+
                  "<input type='submit' value='Go'/>"+
               "</form></td></tr>");

      //close table
      response.getWriter().print(
            "</table>"+
            "</td></tr></table>"+
        "</div>");
   }

   //adds stores in the default registry to the hashtable
   public void populateRegisteredStores() {

      Element[] myspaceResources;
      try {
         myspaceResources = getMySpaces();
      }
      catch (RegistryException re) {
         log.error(re);
         return;
      }
         
      //for each of the ones found, add to list
      for (int i = 0; i < myspaceResources.length; i++) {
         stores.put(DomHelper.getValue(myspaceResources[i], "Title"),
                    DomHelper.getValue(DsaDomHelper.getSingleChildByTagName(myspaceResources[i], "Interface"), "AccessURL"));
      }
         
   }
   
   /** Queries Registry for myspaces */
   public Element[] getMySpaces() throws RegistryException {
      String selectQuery = "<query><selectionSequence>" +
             "<selection item='searchElements' itemOp='EQ' value='all'/>" +
             "<selectionOp op='$and$'/>" +
             "<selectionSequence>"+
               "<selection item='vr:Identifier/vr:ResourceKey' itemOp='CONTAINS' value='myspace'/>"+
               "<selectionOp op='OR'/>" +
               "<selection item='vr:Title' itemOp='CONTAINS' value='myspace'/>"+
             "</selectionSequence>"+
           "</selectionSequence></query>";
      
      RegistryService client = RegistryDelegateFactory.createQuery();

      Document response = client.submitQuery(selectQuery);

      String s = DomHelper.DocumentToString(response); //for debug

      /*
      NodeList resources = response.getElementsByTagNameNS("*", "Resource");
      Vector myspaces = new Vector();
      for (int i = 0; i < resources.getLength(); i++) {
         if ( ((Element) resources.item(i)).getAttribute("xsi:type").equals("CeaServiceType")) {
            myspaces.add(resources.item(i));
         }
      }
       */
      return new Element[] {};
   }
   

   /**
    *
    */
   public static void main(String[] args) throws RegistryException {
        
         new StoresView(null).getMySpaces();
   }
}
