<%
  String regBas = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
  String fmService = regBas + "/services/FileManagerPort";
  String fmServiceName = SimpleConfig.getSingleton().getString("org.astrogrid.filemanager.service.name");
  String fmivorn =  SimpleConfig.getSingleton().getString(fmServiceName + ".service.ivorn");  
  String fmAuthority = fmivorn;
  String  fmResourceKey = null;
  if(fmivorn.indexOf("/",6) != -1) {
    fmAuthority = fmivorn.substring(6,fmivorn.indexOf("/",6));
    if(fmivorn.length() > (fmAuthority.length() + 7)) {
    	fmResourceKey = fmivorn.substring((fmAuthority.length()+7));
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
 <title>FileManager Service</title>
 <identifier><%=fmivorn%></identifier>
 <curation>
  <publisher>File Manager Publisher</publisher>
  <contact>
   <name><%= request.getParameter("ContactName") %></name>
   <email><%= request.getParameter("ContactEmail") %></email>
  </contact>
 </curation>
 <content>
  <subject>File Manager</subject>
  <subject>VOStore</subject>
  <description>File Manager service</description>
  <referenceURL>http://www.astrogrid.org</referenceURL>
  <type>BasicData</type>
  <relationship>
    <relationshipType>derived-from</relationshipType>
        <relatedResource ivo-id="ivo://org.astrogrid/FileManagerKind">File Manager Kind</relatedResource>
  </relationship>  
 </content>
    <interface xsi:type="vs:WebService">
		<accessURL use="full"><%=fmService%></accessURL>
    </interface> 
</vor:Resource>
</vor:VOResources>