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
<head><title>List of Registry Entries</title>
</head>

<body>

<h1>Registry Entries</h1>


<pre>
<%
   RegistryService server = new RegistryService();

         String selectQuery = "<query><selectionSequence>" +
         "<selection item='searchElements' itemOp='EQ' value='all'/>" +
         "<selectionOp op='$and$'/>" +
         "<selection item='AuthorityID' itemOp='NE' value=''/>"+
         "</selectionSequence></query>";

   
   Document query = DomHelper.newDocument(selectQuery);
   
   Document entry = server.submitQuery(query);
   
   if (entry == null) {
      out.write("<p>No entries?!</p>");
   }
   else {
     DomHelper.DocumentToWriter(entry, out);
   }

%>
</pre>

</body>
</html>
