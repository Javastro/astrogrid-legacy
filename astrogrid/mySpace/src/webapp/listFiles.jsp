<%@ page import="org.astrogrid.store.delegate.*,
                 org.astrogrid.store.*,
                 org.astrogrid.community.User,
                 org.astrogrid.store.delegate.myspaceItn05.*,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head><title>File List</title>
</head>

<body>

<FORM>
<INPUT TYPE="button" onClick="history.go(0)" VALUE="Refresh">
</FORM>
<a href="listFiles.jsp" target="files">Refresh</a>

<%
   User user = new User(request.getParameter("userId"), request.getParameter("communityId"), "dummyToken");

   URL serviceURL = new URL ("http", request.getServerName(),
                                 request.getServerPort(),
                                 request.getContextPath() + "/services/Manager");
%>

<table>
<tr>
  <td>Filename</td>
  <td>Size</td>
  <td>Created</td>
  <td>Actions</td>
</tr>
<%
   ManagerGenuine manager = new ManagerGenuine();

   KernelResults results = manager.getEntriesList("*", false);

   int resultsSize = results.getEntries().length;
// StoreClient client = StoreDelegateFactory.createDelegate(user, new Agsl("myspace:"+serviceURL));

//  StoreFile[] files = client.listFiles("*");

//  int resultsSize = files.length;

   
  if (resultsSize > 0)
  {
     for (int i=0; i<resultsSize; i++)
     {
         EntryRecord msnode = new EntryRecord ( (EntryResults) results.getEntries()[i]);
         
         out.println("<tr>");
         if (msnode.isFolder()) {
            out.println("<td>"+msnode.getName()+"</td>");
         }
         else {
         
         out.println("<td><a href='"+msnode.getEntryUri()+"'>"+ msnode.getName() + "</a></td>"+
                     "<td>"+ msnode.getSize() + "</a></td>"+
                     "<td>"+ msnode.getCreationDate().toGMTString() + "</a></td>"+
                     "<td>"+
                        "<a href='viewFile.jsp?file="+ msnode.getName()+"' target='actions'>View</a> "+
                        "<a href='deleteFile.jsp?file="+ msnode.getName()+"' target='actions'>Delete</a>"+
                     "</td>");
         }
         out.println("</tr>");
     }
  }
  else
  {
     out.print("No entries satisfied the query." + "<BR>");
  }

%>
</pre>

</body>
</html>
