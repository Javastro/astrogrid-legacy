/*
 * $Id: JspHelper.java,v 1.1 2004/10/28 23:46:51 mch Exp $
 */

package org.astrogrid.registry.server;

import java.io.IOException;
import java.util.Hashtable;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.registry.server.query.RegistryQueryService;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * A helper class for common tasks by the JSPs.  Could do with being tidied
 * up into some other classes, but I've added it separtaely so it doesn't
 * disturb existing stuff.
 *
 * @author M Hill
 */
public class JspHelper {

   private static final Log log = LogFactory.getLog(JspHelper.class);

   /** Returns the resource identified by the given IVORN as a DOM document,
    or null if the IVORN can't be found */
   public static Document getResource(String ivorn) throws IOException {

      if ((ivorn == null) || (ivorn.trim().length()==0)) {
            return null;
      }
      
      //separate out authority ID - to first slash - and reousrceKey
      String authID = ivorn;
      String resKey = null;
      int slashIdx = authID.indexOf("/");
      if (slashIdx != -1) {
         resKey = authID.substring((slashIdx+1));
         authID = authID.substring(0,slashIdx);
      }
      return getResource(authID, resKey);
   }

   /** Returns the resource identified by the given Authority ID and ResourceKey
    * as a DOM document, or null if it cannot be found */
   public static Document getResource(String authorityId, String resourceKey) throws IOException {
         
      RegistryQueryService server = new RegistryQueryService();
      
      String selectQuery = "<query><selectionSequence>" +
         "<selection item='searchElements' itemOp='EQ' value='all'/>" +
         "<selectionOp op='$and$'/>" +
         "<selection item='vr:Identifier/vr:AuthorityID' itemOp='EQ' value='" + authorityId + "'/>";

      if(resourceKey != null) {
         selectQuery += "<selectionOp op='AND'/>" +
            "<selection item='vr:Identifier/vr:ResourceKey' itemOp='EQ' value='" + resourceKey + "'/>";
      }

      selectQuery += "</selectionSequence></query>";
      
      try {
         return server.submitQuery(DomHelper.newDocument(selectQuery));
      }
      catch (ParserConfigurationException e) {
         throw new RuntimeException("Server not configured properly: "+e,e);
      }
      catch (SAXException e) {
         throw new RuntimeException("Server software error; Constructed query "+selectQuery+" is invalid XML",e);
      }
      
   }
   
   /** Returns a list of IVORNs that match the given keyword search
    *
   public String[] getList(String keyword, String value) {
      
   }
    */
}
