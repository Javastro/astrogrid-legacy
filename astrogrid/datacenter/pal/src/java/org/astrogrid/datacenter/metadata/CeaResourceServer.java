/*
 * $Id: CeaResourceServer.java,v 1.3 2004/10/25 10:43:12 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.metadata;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.component.CEAComponentManagerFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.metadata.VoDescriptionServer;
import org.astrogrid.datacenter.service.ServletHelper;
import org.astrogrid.io.xml.XmlPrinter;
import org.astrogrid.io.xml.XmlTagPrinter;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Serves the CEA resources.  Bit of a mangled fudge at the moment to get
 * the right stuff from the right bit into the right bit.
 * <p>
 * @author M Hill
 */

public class CeaResourceServer implements VoResourcePlugin {

   protected static Log log = LogFactory.getLog(VoDescriptionServer.class);
   
   /**
    * Returns an array of VOResource elements of the metadata.  Returns a string (rather than
    * DOM element)
    * so that we can combine them easily; some DOMs do not mix well.
    */
   public String[] getVoResources() throws IOException {

      //add the voresource for the CEA access to the datacenter
      try {
         //I've wrapped this in a separate try/catch so that problems with CEA
         //don't stop the initialiser from working.. which is naughty
         String ceaVoDescription = CEAComponentManagerFactory.getInstance().getMetadataService().returnRegistryEntry();
         //extract the resource elements
         Document ceaDoc = DomHelper.newDocument(ceaVoDescription);
         NodeList ceaResources = ceaDoc.getElementsByTagName("Resource");
         if (ceaResources.getLength() ==0) {
            ceaResources = ceaDoc.getElementsByTagName("vr:Resource");
         }

         String[] returns = new String[ceaResources.getLength()];

         //each resource will need a little tweaking using the PALs configuration.
         for (int i = 0; i < ceaResources.getLength(); i++) {
            
            Element resource = (Element) ceaResources.item(i);

            String type = resource.getAttribute("xsi:type");
            resource.setAttribute("status", "active");
            resource.setAttribute("update", new Date().toString());

            if (type.indexOf("CeaApplicationType")>-1) {
               returns[i] = getApplicationType(resource);
            }
            else if (type.indexOf("CeaServiceType")>-1) {
               returns[i] = getServiceType(resource);
            }
            else {
               returns[i] = DomHelper.ElementToString(resource);
            }
         }

         return returns;
         
      } catch (Throwable th) {
        log.error(th+" getting CEA resources",th);
      }
      return new String[] {} ;
   }

   /** Application type seems to be some of the authority stuff plus some
    * application definition stuff.  the authority stuff is cut and pasted in
    * from the autority plugin :-( and the application stuff lifted from the given reosurce */
   public String getApplicationType(Element resource) throws IOException {
      StringWriter sw = new StringWriter();
      XmlPrinter printer = new XmlPrinter(sw, false);
      XmlTagPrinter appType = printer.newTag("Resource", new String[] {
               "xsi:type='cea:CeaApplicationType'",
               "status='active'",
               "update='"+new Date()+"'"
            });
      VoDescriptionServer.addIdentifier(appType, "/ceaApplication");

      VoDescriptionServer.writeSummary(appType);
      VoDescriptionServer.writeCuration(appType);

      NodeList appDefs = resource.getElementsByTagName("cea:ApplicationDefinition");
      if (appDefs.getLength() == 0) {
         throw new MetadataException("No cea:ApplicationDefinition in CEA resource "+resource);
      }
      for (int i=0;i<appDefs.getLength();i++) {
         appType.writeTag( (Element) appDefs.item(i));
      }
      appType.close();
      sw.flush();
      sw.close();
      
      return sw.toString();
   }

   public String getServiceType(Element resource) throws IOException {
      StringWriter sw = new StringWriter();
      XmlPrinter printer = new XmlPrinter(sw, false);
      XmlTagPrinter servType = printer.newTag("Resource", new String[] {
               "xsi:type='cea:CeaServiceType'",
               "status='active'",
               "update='"+new Date()+"'"
            });
      VoDescriptionServer.addIdentifier(servType , "/ceaService");

      VoDescriptionServer.writeSummary(servType );
      VoDescriptionServer.writeCuration(servType );

      XmlTagPrinter capabilityTag = servType.newTag("vr:Capability");
      capabilityTag.writeTag("vr:StandardURL",  "http://www.astrogrid.org/maven/build/applications/");
      XmlTagPrinter sidTag = capabilityTag.newTag("vr:StandardID");
      sidTag.writeTag("vr:AuthorityID", "astrogrid.org");
      sidTag.writeTag("vr:ResourceKey", "CommonExecutionArchitucture");

      XmlTagPrinter intTag = servType.newTag("vr:Interface");
      intTag.writeTag("vr:Invocation", "WebService");
      intTag.writeTag("vr:AccessURL",  new String[] { "use='base'"},
//         ServletHelper.getUrlStem()+"services/CommonExecutionConnectorService"
            SimpleConfig.getSingleton().getString("datacenter.url")+"services/CommonExecutionConnectorService"
         );

      XmlTagPrinter manappTag = servType.newTag("cea:ManagedApplications");
      XmlTagPrinter appRefTag = manappTag.newTag("cea:ApplicationReference");
      appRefTag.writeTag("AuthorityID", SimpleConfig.getSingleton().getString("datacenter.authorityId"));
      appRefTag.writeTag("ResourceKey", SimpleConfig.getSingleton().getString("datacenter.resourceKey"));

      /*
      <vr:Capability>
         <vr:StandardURL>http://www.astrogrid.org/maven/build/applications/</vr:StandardURL>
         <vr:StandardID>
            <vr:AuthorityID>astrogrid.og</vr:AuthorityID>
            <vr:ResourceKey>CommonExecutionArchitucture</vr:ResourceKey>
         </vr:StandardID>
      </vr:Capability>
      <vr:Interface>
         <vr:Invocation>WebService</vr:Invocation>
         <vr:AccessURL use="base">http://localhost:8080/astrogrid-cea-server/services/CommonExecutionConnectorService</vr:AccessURL>
      </vr:Interface>

      <cea:ManagedApplications>
         <cea:ApplicationReference>
            <vr:AuthorityID>org.astrogrid.localhost</vr:AuthorityID>
            <vr:ResourceKey>testdsa</vr:ResourceKey>
         </cea:ApplicationReference>
      </cea:ManagedApplications>
    */

      servType.close();
      sw.flush();
      sw.close();
      
      return sw.toString();

   }
}







