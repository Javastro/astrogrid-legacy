<%@ page import="org.astrogrid.store.delegate.*,
                 org.astrogrid.store.*,
                 org.astrogrid.community.User,
                 org.astrogrid.store.delegate.myspaceItn05.*,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<%!
   //creates an indent string for the given file
   String getIndent(StoreFile file) {
      StringBuffer indent = new StringBuffer("<pre>");
      while (file.getParent() != null) {
         indent.append("&nbsp;&nbsp;&nbsp;");
         file=file.getParent();
      }
      return indent.toString()+"</pre>";
   }
%>
<%!
   //Creates the GET parameters for the given openpaths and selectedstring
   String selectPath(String[] openPaths, String newPath) {
      String s="";
      for (int i = 0; i < openPaths.length; i++) {
         s=s+"open="+openPaths[i]+"&";
      }
      if (newPath != null) {
         s = s +"path="+newPath;
      }
      return s;
   }
%>

<%!
   //Creates the GET parameters for the given openpaths, selectedstring and new open
   String openNew(String[] openPaths, String selectedPath, String newOpen) {
      String s="";
      for (int i = 0; i < openPaths.length; i++) {
         s=s+"open="+openPaths[i]+"&";
      }
      s=s+"open="+newOpen;
      if (selectedPath != null) {
         s = s +"path="+selectedPath;
      }
      return s;
   }
%>

<%!
   //Creates the GET parameters for the given openpaths, selectedstring but
   //removing the given closePath from the open ones
   String closePath(String[] openPaths, String selectedPath, String closePath) {
      String s="";
      for (int i = 0; i < openPaths.length; i++) {
         if (!openPaths[i].equals(closePath)) {
            s=s+"open="+openPaths[i]+"&";
         }
      }
      if (selectedPath != null) {
         s = s +"path="+selectedPath;
      }
      return s;
   }
%>

<%!
 //prints out a single StoreFile
 void writeFile(String refRoot, StoreFile file, String[] openPaths, String selectedPath, Writer out) throws IOException {

    String cellColour = "#FFFFFF";
    if ((selectedPath != null) && (selectedPath.startsWith(file.getPath()))) {
         cellColour = "#0000FF";
    }

    if (file.isFolder()) {
       
       //is it open?
       boolean isOpen = false;
       if (file.getPath()==null) {
          //root
          isOpen = true;
       }
       else {
          for (int i = 0; i < openPaths.length; i++) {
             if (openPaths[i].startsWith(file.getPath())) {
                isOpen = true;
             }
          }
       }
       if (isOpen) {
          if (file.getPath() != null) {
             out.write("<tr>"+
                          "<td color='"+cellColour+"'>"+
                          "<a href=\""+refRoot+closePath(openPaths, selectedPath, file.getPath())+"\">"+
                          "<pre><img ref='OpenFolder.gif' alt='(-)' border='0'/></pre>"+
                          "</a>"+
                          getIndent(file)+
                          "<a href=\""+refRoot+selectPath(openPaths, file.getPath())+"\">"+
                          file.getName()+
                          "</a></td>"+
                      "</tr>\n");
          }
          for (int i=0;i<file.listFiles().length;i++) {
             writeFile(refRoot, file.listFiles()[i], openPaths, selectedPath, out);
             out.write("\n");
          }
       }
       else {
          //folder is closed
          out.write("<tr>"+
                       "<td color='"+cellColour+"'>"+
                       "<a href=\""+refRoot+openNew(openPaths, selectedPath, file.getPath())+"\">"+
                       "<pre><img ref='ClosedFolder.gif' alt='(+)' border='0'/></pre>"+
                       "</a>"+
                       getIndent(file)+" <a href=\""+refRoot+selectPath(openPaths, file.getPath())+"\">"+
                       file.getName()+
                       "</a>"+
                       "</td></tr>\n");
          
       }
    }
    else {
       //it's a file
       out.write("<tr color='"+cellColour+"'>"+
                    "<td>"+"<a href=\""+refRoot+selectPath(openPaths, file.getPath())+"\">"+
                         getIndent(file)+file.getName() +
                    "</a></td>"+
                    "<td>"+ file.getSize() + "</td>"+
                    "<td>"+ file.getCreated().toGMTString() + "</td>"+
                    "<td>"+ file.getMimeType()+"</td>"+
                    "<td>"+ file.getModified()+"</td>"+
                    "</tr>\n");
    }
 }
%>

<table>
<tr>
  <td>Filename</td>
  <td>Size</td>
  <td>Created</td>
  <td>Type</td>
  <td>Modified</td>
</tr>
<%
    out.flush();
    String[] openPaths = request.getParameterValues("open");
    if (openPaths == null) { openPaths = new String[] { }; }
    String selectedPath = request.getParameter("path");
    String storepoint = request.getParameter("store");
    User user = User.ANONYMOUS;
    
    //reference root for links
    String refRoot = "browser.jsp?";
   Enumeration e = request.getParameterNames();
   while (e.hasMoreElements()) {
      String key = (String)e.nextElement();
      String[] values = request.getParameterValues(key);
      if (!key.equals("open") && !key.equals("path")) {
         for(int i = 0; i < values.length; i++) {
            refRoot=refRoot+key+"="+values[i]+"&";
         }
      }
   }
    
    
//    if (storepoint == null) {
//       storepoint = new Msrl(new URL("http", request.getServerName(), request.getServerPort(),
//                                    request.getContextPath())).toString();
//    }
    StoreFile root = null;

    if (storepoint != null) {
    
       if (Msrl.isMsrl(storepoint)) {
          try {
            StoreClient client = StoreDelegateFactory.createDelegate(user, new Agsl(new Msrl(storepoint)));
            root = client.getFiles("*");
          }
          catch (Exception ex) {
            out.write("Error getting files from "+storepoint+": "+ex+"<p>");
            out.write("<pre>");
            ex.printStackTrace(new PrintWriter(out));
            out.write("</pre><p>");
          }
       }
       else if (Ivorn.isIvorn(storepoint)) {
         //VoSpaceClient client = new VoSpaceClient(user);
         //root = client.getFiles("*");
          out.write("get files not supported from "+storepoint+"<br>");
       }
       else {
         out.write("Unknown store type "+storepoint+"\n"+"<br>");
       }
      
       if ((root == null) || (root.listFiles() == null)) {
         out.println("No entries to list from "+storepoint+"<br>");
       }
       else {
         writeFile(refRoot, root, openPaths, selectedPath, out);
       }
    }
    else {
       out.println("No Store Given"+"<br>");
    }
%>
</table>

</body>
</html>
