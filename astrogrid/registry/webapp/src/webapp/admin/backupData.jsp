<%@ page import="org.astrogrid.registry.server.query.*,
					  org.astrogrid.registry.server.*,
                 org.astrogrid.store.Ivorn,
                 org.w3c.dom.Document,
                 org.astrogrid.io.Piper,
                 org.astrogrid.util.DomHelper,
                 org.apache.axis.utils.XMLUtils,
                 java.net.*,
                 java.util.*,
                 java.io.*"
   isThreadSafe="false"
   contentType="text/xml"                 
   session="false" %>
<%

   String version = request.getParameter("version");
   String regVersion = version.replace('.','_');
   String collectionName = "astrogridv" + regVersion;
	HashMap mgAuth = RegistryServerHelper.getManagedAuthorities(collectionName,regVersion);
	String xmlns = "xmlns:vr=\"http://www.ivoa.net/xml/VOResource/v" + version + "\"";
	String mgAuthPath = "vg:managedAuthority";
	if("0_9".equals(regVersion))
	  mgAuthPath = "vg:ManagedAuthority";
	  
	String adqlString = " <Where xmlns=\"http://www.ivoa.net/xml/ADQL/v0.7.4\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " + xmlns + ">" +
    "<Condition xsi:type=\"unionSearchType\">";
   
   Object []keys = mgAuth.keySet().toArray();     
	for (int j = 0;j < keys.length;j++) {
       adqlString += "<Condition xsi:type=\"comparisonPredType\" Comparison=\"=\">" +
       "<Arg xsi:type=\"columnReferenceType\" Table=\"a\" Name=\"" + mgAuthPath + "\" />" + 
       "<Arg xsi:type=\"atomType\">" +
       "<Literal xsi:type=\"stringType\" Value=\"" + 	keys[j] + "\" />" +
       "</Arg>" +
       "</Condition>";
	}//for    
   adqlString += "</Condition>" + "</Where>";
	System.out.println("the adqlstring = " + adqlString);   
   RegistryQueryService server = new RegistryQueryService();
   Document entry = null;
   entry = server.Search(DomHelper.newDocument(adqlString));
   if (entry == null) {
       out.write("<Error>No entry returned</Error>");
   }
   else {
      XMLUtils.ElementToWriter(entry.getDocumentElement(),out);
   }
%>