<%@ page import="org.astrogrid.store.delegate.*,
                 org.astrogrid.community.User,
                 org.astrogrid.store.*,
                 org.astrogrid.mySpace.webapp.*,
                 java.net.*,
                 java.util.*,
                 java.io.*"
    session="false" %>

<html>
<head><title>Upload File</title></head>

<body>
<h1>Upload File</h1>

Uploading, please wait...

<%
  User user = new User(request.getParameter("userId"), request.getParameter("communityId"), "dummyToken");
  String target = request.getParameter("targetFile");

  URL serviceURL = new URL ("http", request.getServerName(),
    request.getServerPort(), request.getContextPath() +
    "/services/Manager");

   StoreClient client = StoreDelegateFactory.createDelegate(user, new Agsl("myspace:"+serviceURL.toString()));

   OutputStream targetOut = client.putStream(target, false);
   
   FileUploadExtractor uploader = new FileUploadExtractor();
   
   uploader.upload(request.getInputStream(), targetOut);

%>

...done

</body>
</html>
