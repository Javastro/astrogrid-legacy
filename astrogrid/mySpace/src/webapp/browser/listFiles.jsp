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
<%!
  //prints out a single StoreFile
  String printFile(StoreFile file, StoreClient client) {
      String line = "";
      if (file.isFolder()) {
         line = line + "<tr><td>"+file.getPath()+"</td></tr>\n";
         for (int i=0;i<file.listFiles().length;i++) {
            line = line +printFile(file.listFiles()[i], client)+"\n";
         }
      }
      else {
         try {
            line = line +"<tr>"+
                        "<td><a href='"+ client.getUrl(file.getPath())+"'>"+ file.getPath() + "</a></td>"+
                        "<td>"+ file.getSize() + "</a></td>"+
                        "<td>"+ file.getCreated().toGMTString() + "</a></td>"+
                        "<td>"+
                           "<a href='viewFile.jsp?file="+ file.getPath()+"' target='actions'>View</a> "+
                           "<a href='deleteFile.jsp?file="+ file.getPath()+"' target='actions'>Delete</a>"+
                        "</td>";
            line = line + "</tr>\n";
         }
         catch (IOException ioe) {
            line = line + "<tr>Could  not display file "+file.getPath()+": "+ioe+"</tr>";
         }
      }

      return line;
  }
%>

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
   StoreClient manager = StoreDelegateFactory.createDelegate(user, new Agsl("myspace:"+serviceURL));

   StoreFile root = manager.getFiles("*");

   if ((root == null) || (root.listFiles() == null)) {
         out.println("No entries in myspace");
   }
   else {
      out.println(printFile(root, manager));
   }

%>
</pre>

</body>
</html>
