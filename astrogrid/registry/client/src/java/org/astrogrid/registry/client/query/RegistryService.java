package org.astrogrid.registry.client.query;


import java.net.URL; 
import java.util.Vector; 
import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory; 
import javax.xml.parsers.ParserConfigurationException; 
import org.apache.axis.client.Call; 
import org.apache.axis.client.Service; 
import org.apache.axis.message.SOAPBodyElement; 
import org.apache.axis.utils.XMLUtils; 
import org.w3c.dom.Document; 
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import java.io.Reader;
import java.io.StringReader;
import org.xml.sax.InputSource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.Calendar;
import java.util.Set;
import java.util.Iterator;
import org.astrogrid.registry.common.RegistryConfig;

/**
 * 
 * The RegistryService class is a delegate to a web service that submits an XML formatted
 * registry query to the to the server side web service also named the same RegistryService.
 * This delegate helps the user browse the registry.  Queries should be formatted according to
 * the schema at IVOA schema version 0.9.  This class also uses the common RegistryInterface for
 * knowing the web service methods to call on the server side.
 * 
 * @see org.astrogrid.registry.common.RegistryInterface
 * @link http://www.ivoa.net/twiki/bin/view/IVOA/IVOARegWp03
 * @author Kevin Benson
 */
public class RegistryService implements
                             org.astrogrid.registry.common.RegistryInterface { 
 
   /**
    * target end point  is the location of the webservice. 
    */
   private String endPoint = null;
   
   private boolean dummyMode = false;
   
   private boolean useCache = false;
   
     

   /**
    * Empty constructor that defaults the end point to local host.
    * @author Kevin Benson
    */
   public RegistryService() {
      this(null);
   }
    
   /**
    * Main constructor to allocate the endPoint variable.
    * @param endPoint location to the web service.
    * @author Kevin Benson
    */     
   public RegistryService(String endPoint) {
      this.endPoint = endPoint;
      RegistryConfig.loadConfig();
      dummyMode = Boolean.valueOf(RegistryConfig.getProperty("dummy.mode.on","false")).booleanValue();
      if(this.endPoint == null) {
         useCache = true;
      }
   }
    

   /**
    * Method to establish a Service and a Call to the server side web service.
    * @return Call object which has the necessary properties set for an Axis message style.
    * @throws Exception
    * @author Kevin Benson
    */     
   private Call getCall() throws Exception {
      Service  service = new Service();
      Call _call = (Call) service.createCall();
      _call.setTargetEndpointAddress(new URL(endPoint));
      _call.setSOAPActionURI("");
      _call.setOperationStyle(org.apache.axis.enum.Style.MESSAGE);
      _call.setOperationUse(org.apache.axis.enum.Use.LITERAL);        
      _call.setEncodingStyle(null);
      return _call;       
   }
   
   private Document getDummyDocument() throws Exception {
      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      return registryBuilder.parse(RegistryConfig.getDummyTemplate());     
   }
   
   /**
      * Small convenience method to allow clients to pass in xml strings to be converted to DOM before
      * sending it along the web service.
      * 
      * @param query string to be converted to a DOM object
      * @return XML docuemnt object representing the result of the query.
      * @author Kevin Benson 
      */   
   public Document submitQueryString(String query) throws Exception {
      if(dummyMode) return getDummyDocument();
      if(useCache) {
         throw new IllegalAccessException("This method cannot be accessed when no registry location is defined.");   
      }
      Reader reader2 = new StringReader(query);
      InputSource inputSource = new InputSource(reader2);
      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.parse(inputSource);
      return submitQuery(doc);         
   }
    
   /**
   * submitQuery queries the registry with the same XML document used as fullNodeQuery, but
   * the response comes back in a different record key pair XML formatted document object.
   * Current implementation uses the fullNodeQuery.  fullNodeQuery may be deprecated at a
   * later date and this method reestablished as the main method to use.
   * 
   * @param query XML document object representing the query language used on the registry.
   * @return XML docuemnt object representing the result of the query.
   * @author Kevin Benson 
   */        
   public Document submitQuery(Document query) throws Exception {
      if(dummyMode) return getDummyDocument();
      if(useCache) {
         throw new IllegalAccessException("This method cannot be accessed when no registry location is defined.");   
      }
      String requestQuery =   XMLUtils.ElementToString(query.getDocumentElement());
      requestQuery = "<submitQuery xmlns='http://query.server.registry.astrogrid.org'>" + requestQuery + "</submitQuery>";
      
      Reader reader2 = new StringReader(requestQuery);
      InputSource inputSource = new InputSource(reader2);
        
      Call call = getCall();

      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.parse(inputSource);
      
      SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());      
      sbeRequest.setName("submitQuery");
      sbeRequest.setNamespaceURI("http://query.server.registry.astrogrid.org");
      System.out.println("sending " + XMLUtils.DocumentToString(doc));
            
      Vector result = (Vector) call.invoke (new Object[] {sbeRequest});
      SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);

      System.out.println("received " + XMLUtils.DocumentToString(sbe.getAsDocument()));
      return sbe.getAsDocument();       
   }

   public Document harvestQuery(String dateSince) throws Exception {
      if(dummyMode) return getDummyDocument();
      if(useCache) {
         throw new IllegalAccessException("This method cannot be accessed when no registry location is defined.");   
      }      
      SimpleDateFormat sdf = null;
      Date dat = null;
      if(dateSince.indexOf("T") == -1) {
         sdf = new SimpleDateFormat("yyyy-MM-dd");
         dat = sdf.parse(dateSince);
         dateSince = dateSince.trim();
         dateSince += "T00:00:00";   
      }
      sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      dat = sdf.parse(dateSince);
      return harvestQuery(dat);
   }
   
   public Document harvestQuery(Date dateSince) throws Exception {
      if(dummyMode) return getDummyDocument();
      if(useCache) {
         throw new IllegalAccessException("This method cannot be accessed when no registry location is defined.");   
      }      
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.newDocument();
      
      Element elemDate = doc.createElement("date_since");
      elemDate.appendChild(doc.createTextNode(sdf.format(dateSince)));
      
            
      doc.appendChild(elemDate);
      
      
      return harvestQuery(doc);         

   }
   
   public Document harvestQuery(Document query) throws Exception {
      if(dummyMode) return getDummyDocument();
      if(useCache) {
         throw new IllegalAccessException("This method cannot be accessed when no registry location is defined.");   
      }      
      String requestQuery =   XMLUtils.ElementToString(query.getDocumentElement());
      requestQuery = "<harvestQuery xmlns='http://query.server.registry.astrogrid.org'>" + requestQuery + "</harvestQuery>";

      Reader reader2 = new StringReader(requestQuery);
      InputSource inputSource = new InputSource(reader2);

      //get a call object operation to the web service.
      Call call = getCall();
      
      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.parse(inputSource);
      
      SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());
      sbeRequest.setName("harvestQuery");
      sbeRequest.setNamespaceURI("http://query.server.registry.astrogrid.org");
      
      System.out.println("sending " + XMLUtils.DocumentToString(doc));
      Vector result = (Vector) call.invoke (new Object[] {sbeRequest});

      SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
      System.out.println("received " + XMLUtils.DocumentToString(sbe.getAsDocument()));
      return sbe.getAsDocument();         
   }
   
   public Document loadRegistry(Document query) throws Exception {
      if(dummyMode) return getDummyDocument();
      /*
       * Actually take these next few lines out
       * It swhould get the value for the default authority id then
       * lookup if it has a xml file for that autority id as the key.
       * 
       */
       //TODO redo this area.
      if(useCache) {
         throw new IllegalAccessException("This method cannot be accessed when no registry location is defined.");   
      }
      Call call = getCall();
      DocumentBuilder registryBuilder = null;
      registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
      Document doc = registryBuilder.newDocument();
      Element root = doc.createElementNS("http://query.server.registry.astrogrid.org","loadRegistry");
      doc.appendChild(root);

      SOAPBodyElement sbeRequest = new SOAPBodyElement(doc.getDocumentElement());
      sbeRequest.setName("loadRegistry");
      sbeRequest.setNamespaceURI("http://query.server.registry.astrogrid.org");
      
      System.out.println("sending " + XMLUtils.DocumentToString(doc));
      Vector result = (Vector) call.invoke (new Object[] {sbeRequest});

      SOAPBodyElement sbe = (SOAPBodyElement) result.get(0);
      System.out.println("received " + XMLUtils.DocumentToString(sbe.getAsDocument()));
      return sbe.getAsDocument();         
   }
   
   public HashMap ManagedAuthorities() throws Exception {
      Document doc = loadRegistry(null);
      NodeList nl = doc.getElementsByTagName("ManagedAuthority");
      HashMap hm = new HashMap();
      for(int i = 0;i < nl.getLength();i++) {
         hm.put(nl.item(i).getFirstChild().getNodeValue(),null);   
      }
//      System.out.println("Size of mgauthority = " + hm.size());
      return hm;      
   }
   
   public Document getResourceByIdentifier(String ident) throws Exception {
      if(dummyMode) return null;
      String returnVal = null;
      boolean checkConfig = true;
      if(!useCache) {
         int iTemp = 0;
         iTemp = ident.indexOf("/");
         if(iTemp == -1) iTemp = ident.length();
         String selectQuery = "<query><selectionSequence>" +
         "<selection item='searchElements' itemOp='EQ' value='all'/>" +
         "<selectionOp op='$and$'/>" +
         "<selection item='AuthorityID' itemOp='EQ' value='" + ident.substring(0,iTemp) + "'/>";
         if(iTemp < ident.length()) {
            selectQuery += "<selectionOp op='AND'/>" + 
            "<selection item='ResourceKey' itemOp='EQ' value='" + ident.substring((iTemp+1)) + "'/>";
         }
         selectQuery += "</selectionSequence></query>";
         Document doc = submitQueryString(selectQuery);
         return doc;
      }else {
         //okay look up this ident in the config file.
      }
      return null;
   }
   
   public String getEndPointByIdentifier(String ident) throws Exception {
      Document doc = getResourceByIdentifier(ident);
      //check for an AccessURL
      //if AccessURL is their and it is a web service then get the wsdl
      //into a DOM object and run an XSL on it to get the endpoint.
      return XMLUtils.DocumentToString(doc);   
   }
      
   public static Document buildOAIDocument(Document responseDoc,String accessURL, String dateStamp,Map requestVars) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
      String xmlDoc = "<OAI-PMH xmlns='http://www.openarchives.org/OAI/2.0/' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:schemaLocation='http://www.openarchives.org/OAI/2.0/ http://www.openarchives.org/OAI/2.0/OAI-PMH.xsd'>";
      Calendar rightNow = Calendar.getInstance();
      xmlDoc += "<responseDate>" + sdf.format(rightNow.getTime()) + "</responseDate>";
      if(requestVars != null && requestVars.size() > 0) {
         Set keySet = requestVars.keySet();
         Iterator iter = keySet.iterator();
         String key = null;
         xmlDoc += "<request ";         
         while(iter.hasNext()) {
            key = (String)iter.next();
            //(String)requestVars.get(key);
            xmlDoc += " " + key + "='" + (String)requestVars.get(key) + "'";
         }
         xmlDoc += ">" + accessURL + "</request>";
      }
      if(requestVars.containsKey("verb")) {
         xmlDoc += "<" + requestVars.get("verb") + ">";
         xmlDoc += "<record><header><identifier>ivo_vor://</identifier><dateStamp>" + dateStamp + "</dateStamp></header><metadata>";
         xmlDoc += XMLUtils.ElementToString(responseDoc.getDocumentElement());
         xmlDoc += "</metadata></record>" + "</" + requestVars.get("verb") + ">";
         xmlDoc += "</OAI-PMH>";
      }else {
        //error there must be a verb to oai.  
      }
      System.out.println("here is the xmldoc = " + xmlDoc);
      Reader reader2 = new StringReader(xmlDoc);
      InputSource inputSource = new InputSource(reader2);
      Document doc = null;
      try {
         DocumentBuilder registryBuilder = null;
         registryBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
         doc = registryBuilder.parse(inputSource);
      }catch(Exception e) {
         e.printStackTrace();
         doc = null;
      }
      return doc;
   }
}