<vor:Resource xsi:type="vg:Authority"  updated="2004-11-20T15:34:22Z" status="active">
 <title>FileStore Authority</title>
 <identifier>ivo://<%=fsAuthority%></identifier>
 <curation>
  <publisher>FileStore Publisher</publisher>
  <contact>
  	<name><%= request.getParameter("ContactName") %></name>
   	<email><%= request.getParameter("ContactEmail") %></email>
  </contact>
 </curation>
 <content>
  <subject>The file store authority for astrogrid.org</subject>
  <description>This authority ID will be used to identify generic resources related to the Astrogrid consortium</description>
  <referenceURL>http://www.astrogrid.org/</referenceURL>
  <type>Archive</type>
 </content>
</vor:Resource>