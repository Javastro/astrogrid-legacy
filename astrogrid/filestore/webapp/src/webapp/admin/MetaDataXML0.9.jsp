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
  };
%>
<VODescription
    xmlns="http://www.ivoa.net/xml/VOResource/v0.9"
    xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.9"
    xmlns:vg="http://www.ivoa.net/xml/VORegistry/v0.2"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <%if("on".equals(request.getParameter("AuthorityResourceAdd"))) {%><%@ include file="AuthorityXML0.9.jsp" %><%}%><vr:Resource xsi:type="vr:ServiceType"  updated="2004-11-20T15:34:22Z" status="active">
    <Identifier>
      <AuthorityID><%=fsivorn%></AuthorityID>
      <%if(fsResourceKey != null) { %>
	      <ResourceKey><%=fsivorn%></ResourceKey>
	   <%} else { %>
		   <ResourceKey xsi:nil="true" />
	   <%}%>
    </Identifier>
    <Title>FileStore Service</Title>
    <ShortName>File Store</ShortName>
    <Summary>
      <Description/>
      <ReferenceURL>http://www.astrogrid.org</ReferenceURL>
    </Summary>
    <Type>BasicData</Type>	
    <Curation>
      <Publisher>
         <Title>File Store Publisher</Title>
      </Publisher>
      <Contact>
                <Name><%= request.getParameter("ContactName") %></Name>
                <Email><%= request.getParameter("ContactEmail") %></Email>
      </Contact>
    </Curation>
    <Subject>File Store</Subject>
    <Interface>
      <Invocation>WebService</Invocation>
      <AccessURL use="base"><%=fsService%></AccessURL>
    </Interface>
  </vr:Resource>
</VODescription>