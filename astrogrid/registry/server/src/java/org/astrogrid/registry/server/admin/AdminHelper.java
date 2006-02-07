package org.astrogrid.registry.server.admin;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Date;
import java.text.DateFormat;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.astrogrid.util.DomHelper;

public class AdminHelper {
    
    /**
     * Logging variable for writing information to the logs
     */
   private static final Log log = 
                               LogFactory.getLog(AdminHelper.class);    
    

    
    /**
     * Create statistical data to store in the eXist database when each 
     * managed or registry Resource entry is either created or updated. This
     * will shortly be used to drive Harvesting so that only appropriate
     * entries will be extracted.
     *
     * @param tempIdent The identifier for this Resource
     * @return Node representing the <ResourceStat> Element
     */     
    public Node createStats( String tempIdent ) {
        return createStats(tempIdent,true);
    }    
    
    public Node createStats( String tempIdent, boolean addMillis) {
        log.debug("start createStats");
        Date statsTimeMillis = new Date();
        DateFormat shortDT = DateFormat.getDateTimeInstance();
        String statsXML = "<ResourceStat><Identifier>ivo://" + tempIdent +
                                 "</Identifier>";
        if(addMillis) {
            statsXML += "<StatsDateMillis>" +
                        statsTimeMillis.getTime() +
                        "</StatsDateMillis>";
        }
        statsXML += "<StatsDate>" +
                         shortDT.format(statsTimeMillis) +
                     "</StatsDate></ResourceStat>";
        try {
           log.debug("end createStats");
           return DomHelper.newDocument(statsXML).getDocumentElement();
        }
        catch ( Exception e ) {
        // This will be improved shortly with other Exception handling!
           e.printStackTrace();
           log.error(e);
       }
       return null;
    }    
    
    /**
     * Need to use RegistryServerHelper class to get the NodeList.
     * But leave for the moment.
     * @param doc
     * @return
     */
    public Node getManagedAuthorityID(Document doc) {
       log.debug("start getManagedAuthorityID");
           NodeList nl = doc.getElementsByTagNameNS("*","ManagedAuthority");
           
           if(nl.getLength() == 0) {
               nl = doc.getElementsByTagNameNS("*","managedAuthority");
           }          
           
           //NodeList nl = DomHelper.getNodeListTags(doc,"ManagedAuthority","vg");
           if(nl.getLength() > 0)
              return nl.item(0);
           log.debug("end getManagedAuthorityID");
       return null;
    }
    
    public NodeList getManagedAuthorities(Element regNode) {
        log.debug("start getManagedAuthorityID");
        NodeList nl = regNode.getElementsByTagNameNS("*","ManagedAuthority");
        if(nl.getLength() == 0) {
            nl = regNode.getElementsByTagNameNS("*","managedAuthority");
        }
        return nl;
     }  
    
}