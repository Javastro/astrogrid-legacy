<%
  String regBas = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
  String fsService = regBas + "/services/FileStore";
  String fsServiceName = SimpleConfig.getSingleton().getString("org.astrogrid.filestore.service.name");
  String fsivorn =  SimpleConfig.getSingleton().getString(fsServiceName + ".service.ivorn");
  String fsAuthority = fsivorn;
  String  fsResourceKey = null;
  if(fsivorn.indexOf("/",6) != -1) {
    fsAuthority = fsivorn.substring(6,fsivorn.indexOf("/",6));
    if(fsivorn.length() > (fsAuthority.length() + 7)) {
    	fsResourceKey = fsivorn.substring((fsAuthority.length()+7));
    }
  }
%>
<vor:VOResources xmlns:vor="http://www.ivoa.net/xml/RegistryInterface/v0.1"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
   xmlns:xs="http://www.w3.org/2001/XMLSchema" 
   xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.10" 
   xmlns:vs="http://www.ivoa.net/xml/VODataService/v0.5" 
   xmlns:vg="http://www.ivoa.net/xml/VORegistry/v0.3" xmlns="http://www.ivoa.net/xml/VOResource/v0.10">
   <%if("on".equals(request.getParameter("AuthorityResourceAdd"))) {%><%@ include file="AuthorityXML0.10.jsp" %><%}%>
<vor:Resource xsi:type="vr:Service"  updated="2004-11-20T15:34:22Z" status="active">
 <title>FileStore Service</title>
 <identifier><%=fsivorn%></identifier>
 <curation>
  <publisher>File Store Publisher</publisher>
  <contact>
   <name><%= request.getParameter("ContactName") %></name>
   <email><%= request.getParameter("ContactEmail") %></email>
  </contact>
 </curation>
 <content>
  <subject>File Store</subject>
  <subject>VOStore</subject>
  <description>File Store service</description>
  <referenceURL>http://www.astrogrid.org</referenceURL>
  <type>BasicData</type>
 </content>
    <interface xsi:type="vs:WebService">
		<accessURL use="full"><%=fsService%></accessURL>
    </interface> 
</vor:Resource>
</vor:VOResources>