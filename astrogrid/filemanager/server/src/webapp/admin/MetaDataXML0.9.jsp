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
  };
%>
<VODescription
    xmlns="http://www.ivoa.net/xml/VOResource/v0.9"
    xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.9"
    xmlns:vg="http://www.ivoa.net/xml/VORegistry/v0.2"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <%if("on".equals(request.getParameter("AuthorityResourceAdd"))) {%><%@ include file="AuthorityXML0.9.jsp" %><%}%><vr:Resource xsi:type="vr:ServiceType"  updated="2004-11-20T15:34:22Z" status="active">
    <Identifier>
      <AuthorityID><%=fmivorn%></AuthorityID>
      <%if(fmResourceKey != null) { %>
	      <ResourceKey><%=fmivorn%></ResourceKey>
	   <%} else { %>
		   <ResourceKey xsi:nil="true" />
	   <%}%>
    </Identifier>
    <Title>FileManager Service</Title>
    <ShortName>File Manager</ShortName>
    <Summary>
      <Description/>
      <ReferenceURL>http://www.astrogrid.org</ReferenceURL>
    </Summary>
    <Type>BasicData</Type>	
    <RelatedResource>
        <Relationship>derived-from</Relationship>
            <RelatedTo>
                <Identifier>
                    <AuthorityID>org.astrogrid</AuthorityID>
                    <ResourceKey>FileManagerKind</ResourceKey>
                </Identifier>
                <Title>FileManager Kind</Title>
            </RelatedTo>
    </RelatedResource>    
    <Curation>
      <Publisher>
         <Title>File Manager Publisher</Title>
      </Publisher>
      <Contact>
                <Name><%= request.getParameter("ContactName") %></Name>
                <Email><%= request.getParameter("ContactEmail") %></Email>
      </Contact>
    </Curation>
    <Subject>File Manager</Subject>
    <Interface>
      <Invocation>WebService</Invocation>
      <AccessURL use="base"><%=fmService%></AccessURL>
    </Interface>
  </vr:Resource>
</VODescription>