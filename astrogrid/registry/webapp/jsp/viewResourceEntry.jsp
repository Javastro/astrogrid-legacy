<%@ page import="org.astrogrid.registry.server.query.RegistryService,
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
  String ivornKey = request.getParameter("IVORN");
%>

<h1>Key <%=ivornKey%></h1>

<pre>
<%
    RegistryQueryService server = new RegistryQueryService();

         String selectQuery = "<query><selectionSequence>" +
           "<selection item='searchElements' itemOp='EQ' value='all'/>" +
           "<selectionOp op='$and$'/>" +
           "<selection item='*:Identifier/*:AuthorityID' itemOp='EQ' value='" + ivornKey + "'/>" +
         "</selectionSequence></query>";
   
   Document query = DomHelper.newDocument(selectQuery);
   
   Document entry = server.submitQuery(query);
   
   if (entry == null) {
      out.write("<p>No entry returned</p>");
   }
   else {
     DomHelper.DocumentToWriter(entry, out);
   }
%>
</pre>

<a href="index.html">Index</a>

</body>
</html>
