

package org.astrogrid.portal.cocoon.registry.readers;

import org.apache.cocoon.reading.AbstractReader;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.CacheValidity;
import org.apache.cocoon.caching.Cacheable;
import org.apache.cocoon.caching.TimeStampCacheValidity;
import org.apache.cocoon.environment.Context;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Response;
import org.apache.cocoon.environment.Source;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.util.HashUtil;
import org.xml.sax.SAXException;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.*;

import java.io.InputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Enumeration;
import java.net.URL;

import org.w3c.dom.Document;
import org.astrogrid.registry.client.query.RegistryService;
import org.astrogrid.registry.common.RegistryConfig;
import org.apache.axis.utils.XMLUtils;



/*
 * Returns an OAI compatible xml format of a query of the registry based around changes from a particular Date.
 * 
 * @author Kevin Benson
 *
 */

public class OAIHarvestReader extends AbstractReader {

      /** The  source */
      private Source      inputSource;


      /**
       * Setup the reader.
       * The resource is opened to get an <code>InputStream</code>,
       * the length and the last modification date
       */
      public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par)
      throws ProcessingException, SAXException, IOException {
         super.setup(resolver, objectModel, src, par);
         this.inputSource = this.resolver.resolve(super.source);
      }

      public void recycle() {
         super.recycle();
         if (this.inputSource != null) {
            this.inputSource.recycle();
            this.inputSource = null;
         }
      }

      /**
       * Generate the unique key.
       * This key must be unique inside the space of this component.
       *
       * @return The generated key hashes the src
       */
      public long generateKey() {
         if (this.inputSource.getLastModified() != 0) {
            return HashUtil.hash(this.inputSource.getSystemId());
         }
         return 0;
      }

      /**
       * Generate the validity object.
       *
       * @return The generated validity object or <code>null</code> if the
       *         component is currently not cacheable.
       */
      public CacheValidity generateValidity() {
         if (this.inputSource.getLastModified() != 0) {
            return new TimeStampCacheValidity(this.inputSource.getLastModified());
         }
         return null;
      }

      /**
       * @return the time the read source was last modified or 0 if it is not
       *         possible to detect
       */
      public long getLastModified() {
         return this.inputSource.getLastModified();
      }

      /**
       * Generates the requested resource.
       */
      public void generate()
      throws IOException, ProcessingException {
         final Response response = ObjectModelHelper.getResponse(this.objectModel);
         Request request = ObjectModelHelper.getRequest(objectModel);
         //load the config.
         RegistryConfig.loadConfig();
         Enumeration enum = request.getParameterNames();
         String param = null;
         //OAI requires having an element with all the requests passed in.
         //this will put all the request and values into a hashmap to be 
         //placed on an DOM element.
         HashMap hm = new HashMap();
         while(enum.hasMoreElements()) {
           param = (String)enum.nextElement();
           hm.put(param,request.getParameter(param));
         }
         //Get the date and if it does not exist then create a very old date.
         String dateSince = request.getParameter("from");
         if(dateSince == null || dateSince.trim().length() <= 0) {
            dateSince = "1950-02-02T00:00:00";   
         }
         
         //A verg and DateFrom are requried request variables, if they are not their
         //then default it to a required variables.
         if(request.getParameter("verb") == null) {
            hm.put("verb","ListRecords");
         }
         if(request.getParameter("from") == null) {
            hm.put("from",dateSince);
         }


         //Instantiate a new Registry Service for the query
         String url = RegistryConfig.getProperty("publish.registry.query.url");
         RegistryService rs = new RegistryService(url);
         //String resultDoc = XMLUtils.DocumentToString(harvestDoc);
                     
         try {
            //Now go and harvest the registry for changes since this date.
            Document tempDoc = rs.harvestQuery(dateSince);
            System.out.println("the source = " + source);
            //The returned document is still in the IVOA schema xml format.
            //go and build the OAI result around it.
            Document harvestDoc = RegistryService.buildOAIDocument(tempDoc,source,dateSince,hm);
            //Writ it out to the outputstream for display.
            XMLUtils.DocumentToStream(harvestDoc,out);
         }catch(Exception e) {
            e.printStackTrace();
            System.out.println(e.toString());
         }
      }

      /**
       * Returns the mime-type of the resource in process.
       */
      public String getMimeType () {
         Context ctx = ObjectModelHelper.getContext(this.objectModel);

         if (ctx != null) {
            return ctx.getMimeType(this.source);
         } else {
            return null;
         }
      }
}