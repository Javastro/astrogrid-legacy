<%@ page import="org.astrogrid.registry.server.query.*,
					  org.astrogrid.registry.server.*,
  					  org.astrogrid.registry.server.admin.*,
					  org.astrogrid.config.SimpleConfig,
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
   String collectionName = "astrogridv" + regVersion.replace('.','_');
	HashMap mgAuth = RegistryServerHelper.getManagedAuthorities(collectionName,version);
	String xmlns = "xmlns:vr=\"http://www.ivoa.net/xml/VOResource/v" + version + "\"";
	String mgAuthPath = "vg:managedAuthority";
	if("0.9".equals(regVersion))
	  mgAuthPath = "vg:ManagedAuthority";
	  
	String adqlString = " <Where xmlns=\"http://www.ivoa.net/xml/ADQL/v0.7.4\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " + xmlns + ">" +
    "<Condition xsi:type=\"unionSearchType\">";
   
   String authorityID = SimpleConfig.getSingleton().getString("reg.amend.authorityid");
   Object []values = mgAuth.values().toArray();
   AuthorityList al = null;
	for (int j = 0;j < values.length;j++) {
	    al = values[j];
	    if(authorityID.equals(al.getOwner())) {
	       adqlString += "<Condition xsi:type=\"comparisonPredType\" Comparison=\"=\">" +
	       "<Arg xsi:type=\"columnReferenceType\" Table=\"a\" Name=\"" + mgAuthPath + "\" />" + 
	       "<Arg xsi:type=\"atomType\">" +
	       "<Literal xsi:type=\"stringType\" Value=\"" + 	al.getAuthorityID() + "\" />" +
	       "</Arg>" +
	       "</Condition>";
	    }//if
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