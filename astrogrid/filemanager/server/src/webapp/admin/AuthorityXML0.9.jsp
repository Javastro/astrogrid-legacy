    <vr:Resource xsi:type="vg:AuthorityType"  updated="2004-11-20T15:34:22Z" status="active">
        <Identifier>
            <AuthorityID><%=fmAuthority%></AuthorityID>
            <ResourceKey xsi:nil="true" />
        </Identifier>
        <Title>File Manager Authority Resource</Title>
        <ShortName>File Manager</ShortName>
        <Summary>
            <Description>Authority Resource to define File Manager</Description>
            <ReferenceURL>http://www.astrogrid.org/</ReferenceURL>
        </Summary>
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
    </vr:Resource>