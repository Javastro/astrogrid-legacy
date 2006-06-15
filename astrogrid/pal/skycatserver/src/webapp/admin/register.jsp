<%@ page import="java.io.*,
      java.net.URL,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.astrogrid.cfg.ConfigFactory,
       org.astrogrid.registry.client.RegistryDelegateFactory,
       org.astrogrid.dataservice.metadata.*,
       org.astrogrid.dataservice.service.*"
   isThreadSafe="false"
   session="false"
%>
<html>
<head>
<title>Register <%=DataServer.getDatacenterName() %></title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file='../header.xml' %>
<%@ include file='navigation.xml' %>

<div id='bodyColumn'>

<body>

  <h1>Registering <%= DataServer.getDatacenterName() %></h1>
  <p>
  You can send the Registry details from this datacenter to a remote Registry using this form:
         <form action="Register" method="POST">
          <p>
            Registry URL <input type="text" name="RegistryUrl" size="60"/>
            (leave blank to register on the
            <a href='<%= ConfigFactory.getCommonConfig().getString(RegistryDelegateFactory.ADMIN_URL_PROPERTY, "[WARNING: Property "+RegistryDelegateFactory.ADMIN_URL_PROPERTY+" not set]") %>'>Default Registry</a>)
            <br />
          </p>
           <p>
             <input type="submit" name='Submit' value="Register"  />
           </p>
      </form>
      <p>
      However if there are problems with this, you can click on the XML link to
      the left to get the XML document, copy it into your clip board, then go to
      the entry forms of the relevent Registry and paste it in.
      <p>
<%
   //put a link to the default registry page
   URL registerUrl = ConfigFactory.getCommonConfig().getUrl(RegistryDelegateFactory.ADMIN_URL_PROPERTY, null);
   if (( registerUrl != null)) {
      String context = registerUrl.getPath().substring(1);
      context = context.substring(0, context.indexOf("/"));
      //I think the port gets shortcut because it's the same as the hosts....
      //URL indexUrl = new URL(registerUrl.getProtocol(), registerUrl.getHost(), registerUrl.getPort(), context);
      //URL entryFormUrl = new URL(registerUrl.getProtocol(), registerUrl.getHost(), registerUrl.getPort(), context+"/admin/entryForm.html");
      String indexUrl = registerUrl.getProtocol()+"://"+registerUrl.getAuthority()+":"+registerUrl.getPort()+"/"+context+"/";
      String entryFormUrl = registerUrl.getProtocol()+"://"+registerUrl.getAuthority()+":"+registerUrl.getPort()+"/"+context+"/admin/entryForm.html";
      
      out.write("<p>Default Registry ");
      out.write("<a href='"+indexUrl+"'>Main Page</a> and <a href='"+entryFormUrl+"'>Entry Form</a>");
      out.write("</p>");
   }
%>
<!-- links to test/standard registries -->
<hr>
<table border='0'>
   <tr>
   <td><a href='http://hydra.star.le.ac.uk:8080/astrogrid-registry'>Hydra</a>
   <td><a href='http://hydra.star.le.ac.uk:8080/astrogrid-registry/editEntry.jsp'>Entry Form</a>
   <td><a href='Register?RegistryUrl=http://hydra.star.le.ac.uk:8080/astrogrid-registry/services/AdminService'>Push</a>
   <td><a href='http://hydra.star.le.ac.uk:8080/astrogrid-registry/pullResources.jsp?ResourceUrl=<%=ServletHelper.getUrlStem() %>GetMetadata'>Pull</a>
   </tr>

   <tr>
   <td><a href='http://twmbarlwm.star.le.ac.uk:8888/astrogrid-registry-SNAPSHOT'>Twmbarlwm:8888</a>
   <td><a href='http://twmbarlwm.star.le.ac.uk:8888/astrogrid-registry-SNAPSHOT/editEntry.jsp'>Entry Form</a>
   <td><a href='Register?RegistryUrl=http://twmbarlwm.star.le.ac.uk:8888/astrogrid-registry-SNAPSHOT/services/AdminService'>Push</a>
   <td><a href='http://twmbarlwm.star.le.ac.uk:8888/astrogrid-registry-SNAPSHOT/pullResources.jsp?ResourceUrl=<%=ServletHelper.getUrlStem() %>GetMetadata'>Pull</a>
   </tr>

   <tr>
   <td><a href='http://grendel12.roe.ac.uk:8080/astrogrid-registry'>ROE</a>
   <td><a href='http://grendel12.roe.ac.uk:8080/astrogrid-registry/editEntry.jsp'>Entry Form</a>
   <td><a href='Register?RegistryUrl=http://grendel12.roe.ac.uk:8080/astrogrid-registry/services/AdminService'>Push</a>
   <td><a href='http://grendel12.roe.ac.uk:8080/astrogrid-registry/pullResources.jsp?ResourceUrl=<%=ServletHelper.getUrlStem() %>GetMetadata'>Pull</a>
   </tr>

   <tr>
   <td><a href='http://katatjuta.star.le.ac.uk:8080/astrogrid-registry-SNAPSHOT'>Katatjuta:8080</a>
   <td><a href='http://katatjuta.star.le.ac.uk:8080/astrogrid-registry-SNAPSHOT/editEntry.jsp'>Entry Form</a>
   <td><a href='Register?RegistryUrl=http://katatjuta.star.le.ac.uk:8080/astrogrid-registry-SNAPSHOT/services/AdminService'>Push</a>
   <td><a href='http://katatjuta.star.le.ac.uk:8080/astrogrid-registry-SNAPSHOT/pullResources.jsp?ResourceUrl=<%=ServletHelper.getUrlStem() %>GetMetadata'>Pull</a>
   </tr>

   <tr>
   <td><a href='http://twmbarlwm.star.le.ac.uk:8080/astrogrid-registry-SNAPSHOT'>Twmbarlwm:8080</a>
   <td><a href='http://twmbarlwm.star.le.ac.uk:8080/astrogrid-registry-SNAPSHOT/editEntry.jsp'>Entry Form</a>
   <td><a href='Register?RegistryUrl=http://twmbarlwm.star.le.ac.uk:8080/astrogrid-registry-SNAPSHOT/services/AdminService'>Push</a>
   <td><a href='http://twmbarlwm.star.le.ac.uk:8080/astrogrid-registry-SNAPSHOT/pullResources.jsp?ResourceUrl=<%=ServletHelper.getUrlStem() %>GetMetadata'>Pull</a>
   </tr>

   <tr>
   <td><a href='http://zhumulangma.star.le.ac.uk:8080/astrogrid-registry-SNAPSHOT'>Zhumulangma:8080</a>
   <td><a href='http://zhumulangma.star.le.ac.uk:8080/astrogrid-registry-SNAPSHOT/editEntry.jsp'>Entry Form</a>
   <td><a href='Register?RegistryUrl=http://zhumulangma.star.le.ac.uk:8080/astrogrid-registry-SNAPSHOT/services/AdminService'>Push</a>
   <td><a href='http://zhumulangma.star.le.ac.uk:8080/astrogrid-registry-SNAPSHOT/pullResources.jsp?ResourceUrl=<%=ServletHelper.getUrlStem() %>GetMetadata'>Pull</a>
   </tr>

</table>

</body>

</div>

<%@ include file='../footer.xml' %>
</body>
</html>


