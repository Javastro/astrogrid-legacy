<%
  String regBas = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
  String polManager = regBas + "/services/PolicyManager";
  String polService = regBas + "/services/PolicyService";
  String secManager = regBas + "/services/SecurityManager";
  String secService = regBas + "/services/SecurityService";
  String communityid =  SimpleConfig.getSingleton().getString("org.astrogrid.community.ident");
%>
<VODescription
    xmlns="http://www.ivoa.net/xml/VOResource/v0.9"
    xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.9"
    xmlns:vg="http://www.ivoa.net/xml/VORegistry/v0.2"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    <vr:Resource xsi:type="vg:AuthorityType"  updated="2004-11-20T15:34:22Z" status="active">
        <Identifier>
            <AuthorityID><%=communityid%></AuthorityID>
            <ResourceKey>org.astrogrid.community</ResourceKey>
        </Identifier>
        <Title>Community Authority Resource</Title>
        <ShortName>Community Authority</ShortName>
        <Summary>
            <Description>Authority Resource to define Community, Policy, and Security services</Description>
            <ReferenceURL>http://www.astrogrid.org/</ReferenceURL>
        </Summary>
        <Curation>
            <Publisher>
                <Title>Community Publisher</Title>
            </Publisher>
            <Contact>
                <Name><%= request.getParameter("ContactName") %></Name>
                <Email><%= request.getParameter("ContactEmail") %></Email>
            </Contact>
        </Curation>
        <Subject>Community</Subject>
    </vr:Resource>
 <vr:Resource xsi:type="vr:ServiceType"  updated="2004-11-20T15:34:22Z" status="active">
    <Identifier>
      <AuthorityID><%=communityid%></AuthorityID>
      <ResourceKey>org.astrogrid.community.common.policy.manager.PolicyManager</ResourceKey>
    </Identifier>
    <Title>Policy Manager Community Service</Title>
    <ShortName>Policy Manager</ShortName>
    <Summary>
      <Description/>
      <ReferenceURL>http://www.astrogrid.org</ReferenceURL>
    </Summary>
    <Type>BasicData</Type>
		<vr:RelatedResource>
			<vr:Relationship>derived-from</vr:Relationship>
			<vr:RelatedTo>
				<vr:Identifier>
					<vr:AuthorityID>org.astrogrid</vr:AuthorityID>
					<vr:ResourceKey>CommunityServerKind</vr:ResourceKey>
				</vr:Identifier>
				<vr:Title>Community Main Relation</vr:Title>
			</vr:RelatedTo>
		</vr:RelatedResource>
    <Curation>
      <Publisher>
         <Title>Policy Manager Community</Title>
      </Publisher>
      <Contact>
                <Name><%= request.getParameter("ContactName") %></Name>
                <Email><%= request.getParameter("ContactEmail") %></Email>
      </Contact>
    </Curation>
    <Subject>Policy Manager</Subject>
    <Interface>
      <Invocation>WebService</Invocation>
      <AccessURL use="base"><%=polManager%></AccessURL>
    </Interface>
  </vr:Resource>
 <vr:Resource xsi:type="vr:ServiceType"  updated="2004-11-20T15:34:22Z" status="active">
    <Identifier>
      <AuthorityID><%=communityid%></AuthorityID>
      <ResourceKey>org.astrogrid.community.common.policy.service.PolicyService</ResourceKey>
    </Identifier>
    <Title>Policy Service Community</Title>
    <ShortName>Policy Service</ShortName>
    <Summary>
      <Description/>
      <ReferenceURL>http://www.astrogrid.org</ReferenceURL>
    </Summary>
    <Type>BasicData</Type>
    <Curation>
      <Publisher>
         <Title>Policy Service</Title>
      </Publisher>
      <Contact>
                <Name><%= request.getParameter("ContactName") %></Name>
                <Email><%= request.getParameter("ContactEmail") %></Email>
      </Contact>
    </Curation>
    <Subject>Policy Service</Subject>
    <Interface>
      <Invocation>WebService</Invocation>
      <AccessURL use="base"><%=polService%></AccessURL>
    </Interface>
  </vr:Resource>
<vr:Resource xsi:type="vr:ServiceType"  updated="2004-11-20T15:34:22Z" status="active">
    <Identifier>
      <AuthorityID><%=communityid%></AuthorityID>
      <ResourceKey>org.astrogrid.community.common.security.manager.SecurityManager</ResourceKey>
    </Identifier>
    <Title>Security Manager</Title>
    <ShortName>Security Manager</ShortName>
    <Summary>
      <Description/>
      <ReferenceURL>http://www.astrogrid.org</ReferenceURL>
    </Summary>
    <Type>BasicData</Type>
    <Curation>
      <Publisher>
         <Title>Security Manager</Title>
      </Publisher>
      <Contact>
                <Name><%= request.getParameter("ContactName") %></Name>
                <Email><%= request.getParameter("ContactEmail") %></Email>
      </Contact>
    </Curation>
    <Subject>Security Manager</Subject>
    <Interface>
      <Invocation>WebService</Invocation>
      <AccessURL use="base"><%=secManager%></AccessURL>
    </Interface>
  </vr:Resource>
<vr:Resource xsi:type="vr:ServiceType"  updated="2004-11-20T15:34:22Z" status="active">
    <Identifier>
      <AuthorityID><%=communityid%></AuthorityID>
      <ResourceKey>org.astrogrid.community.common.security.service.SecurityService</ResourceKey>
    </Identifier>
    <Title>Security Service</Title>
    <ShortName>Security Service</ShortName>
    <Summary>
      <Description/>
      <ReferenceURL>http://www.astrogrid.org</ReferenceURL>
    </Summary>
    <Type>BasicData</Type>
    <Curation>
      <Publisher>
         <Title>Security Service</Title>
      </Publisher>
      <Contact>
                <Name><%= request.getParameter("ContactName") %></Name>
                <Email><%= request.getParameter("ContactEmail") %></Email>
      </Contact>
    </Curation>
    <Subject>Security Service</Subject>
    <Interface>
      <Invocation>WebService</Invocation>
      <AccessURL use="base"><%=secService%></AccessURL>
    </Interface>
  </vr:Resource>
</VODescription>