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
