    <vr:Resource xsi:type="vg:AuthorityType"  updated="2004-11-20T15:34:22Z" status="active">
        <Identifier>
            <AuthorityID><%=fsAuthority%></AuthorityID>
            <ResourceKey xsi:nil="true" />
        </Identifier>
        <Title>File Store Authority Resource</Title>
        <ShortName>File Store</ShortName>
        <Summary>
            <Description>Authority Resource to define File Store</Description>
            <ReferenceURL>http://www.astrogrid.org/</ReferenceURL>
        </Summary>
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
    </vr:Resource>