<%@ page import="org.astrogrid.registry.server.query.*,
                 org.astrogrid.store.Ivorn,
                 org.w3c.dom.Document,
                 org.astrogrid.io.Piper,
                 org.astrogrid.util.DomHelper,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head><title>Viewing Registry Entry</title>
</head>

<body>

<%
  String authID = request.getParameter("IVORN");
  String resKey = null;
  int temp = 0;
  if((temp = authID.indexOf("/")) != -1) {
  	resKey = authID.substring((temp+1));
  	authID = authID.substring(0,temp);
  }
%>

<h1>Key <%=request.getParameter("IVORN")%></h1>
<br />
<strong>Only a max of 25 entries will be shown:</strong><br />

<pre>
<%
	    RegistryQueryService server = new RegistryQueryService();

         String selectQuery = "<query><selectionSequence>" +
           "<selection item='searchElements' itemOp='EQ' value='all'/>" +
           "<selectionOp op='$and$'/>" +
           "<selection item='vr:Identifier/vr:AuthorityID' itemOp='EQ' value='" + authID + "'/>";
         if(resKey != null) {
	       selectQuery += "<selectionOp op='AND'/>" +
	       "<selection item='vr:Identifier/vr:ResourceKey' itemOp='EQ' value='" + resKey + "'/>";
         }
         selectQuery += "</selectionSequence></query>";
   
   Document query = DomHelper.newDocument(selectQuery);
   
   Document entry = server.submitQuery(query);
   
   if (entry == null) {
      out.write("<p>No entry returned</p>");
   }
   else {
      out.write("The xml<br />");
      String testxml = DomHelper.DocumentToString(entry);
      testxml = testxml.replaceAll("<","&lt;");
      testxml = testxml.replaceAll(">","&gt;");
      out.write(testxml);
   }
%>
</pre>

<a href="registry_index.html">Index</a>

</body>
</html>
