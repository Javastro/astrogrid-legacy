/*
 * $Id: CeaResourceServer.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.metadata;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.component.CEAComponentManagerFactory;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.io.xml.XmlAsciiWriter;
import org.astrogrid.io.xml.XmlPrinter;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Serves the CEA resources.  Bit of a mangled fudge at the moment to get
 * the right stuff from the right bit into the right bit.
 * <p>
 * @author M Hill
 */

public class CeaResourceServer extends VoResourceSupport implements VoResourcePlugin {

   protected static Log log = LogFactory.getLog(VoDescriptionServer.class);
   
   /**
    * Returns an array of VOResource elements of the metadata.  Returns a string (rather than
    * DOM element)
    * so that we can combine them easily; some DOMs do not mix well.
    */
   public String[] getVoResources() throws IOException {

      //add the voresource for the CEA access to the datacenter
      try {
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
            resource.setAttribute("update", VoDescriptionServer.REGISTRY_DATEFORMAT.format(new Date()));

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
         
      } catch (SAXException th) {
        log.error(th+" getting CEA resources",th);
      }
      catch (ParserConfigurationException th) {
        log.error(th+" getting CEA resources",th);
      }
      catch (CeaException th) {
        log.error(th+" getting CEA resources",th);
      }
      return new String[] {} ;
   }

   /** Application type seems to be some of the authority stuff plus some
    * application definition stuff.  the authority stuff is cut and pasted in
    * from the autority plugin :-( and the application stuff lifted from the given reosurce */
   public String getApplicationType(Element resource) throws IOException {
      StringWriter sw = new StringWriter();
      XmlAsciiWriter printer = new XmlAsciiWriter(sw, false);
      XmlPrinter appType = printer.newTag("Resource", new String[] {
               "xsi:type='CeaApplicationType'",
               "status='active'",
               "updated='"+VoDescriptionServer.REGISTRY_DATEFORMAT.format(new Date())+"'"
            });
      writeIdentifier(appType, "/ceaApplication");

      writeSummary(appType);
      writeCuration(appType);

      appType.writeTag("Type", "Other"); //for the workflow builder

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
      XmlAsciiWriter printer = new XmlAsciiWriter(sw, false);
      XmlPrinter servType = printer.newTag("Resource", new String[] {
               "xsi:type='CeaServiceType'",
               "status='active'",
               "updated='"+VoDescriptionServer.REGISTRY_DATEFORMAT.format(new Date())+"'"
            });
      writeIdentifier(servType , "/ceaService");

      writeSummary(servType );
      writeCuration(servType );

      servType.writeTag("Type", "Other"); //for the workflow builder
      
      XmlPrinter capabilityTag = servType.newTag("vr:Capability");
      capabilityTag.writeTag("vr:StandardURL",  "http://www.astrogrid.org/maven/build/applications/");
      XmlPrinter sidTag = capabilityTag.newTag("vr:StandardID");
      sidTag.writeTag("vr:AuthorityID", "astrogrid.org");
      sidTag.writeTag("vr:ResourceKey", "CommonExecutionArchitucture");

      XmlPrinter intTag = servType.newTag("vr:Interface");
      intTag.writeTag("vr:Invocation", "WebService");
      intTag.writeTag("vr:AccessURL",  new String[] { "use='base'"},
//         ServletHelper.getUrlStem()+"services/CommonExecutionConnectorService"
            SimpleConfig.getSingleton().getString("datacenter.url")+"services/CommonExecutionConnectorService"
         );

      //reference to CeaApplication resource
      XmlPrinter manappTag = servType.newTag("cea:ManagedApplications");
      XmlPrinter appRefTag = manappTag.newTag("cea:ApplicationReference");
      appRefTag.writeTag("AuthorityID", SimpleConfig.getSingleton().getString("datacenter.authorityId"));
      appRefTag.writeTag("ResourceKey", SimpleConfig.getSingleton().getString("datacenter.resourceKey")+"/ceaApplication");

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







