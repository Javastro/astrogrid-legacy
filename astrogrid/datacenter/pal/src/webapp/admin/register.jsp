<%@ page import="java.io.*,
      java.net.URL,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.astrogrid.config.SimpleConfig,
       org.astrogrid.util.DomHelper,
       org.astrogrid.registry.client.RegistryDelegateFactory,
       org.astrogrid.datacenter.metadata.*,
       org.astrogrid.datacenter.service.*"
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
            Registry URL <input type="text" name="RegistryUrl"/>
            (leave blank to register at <%= SimpleConfig.getSingleton().getString(RegistryDelegateFactory.ADMIN_URL_PROPERTY, "[WARNING: Property "+RegistryDelegateFactory.ADMIN_URL_PROPERTY+" not set]") %>)<br />
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
   URL registerUrl = SimpleConfig.getSingleton().getUrl(RegistryDelegateFactory.ADMIN_URL_PROPERTY, null);
   if (( registerUrl != null)) {
      String context = registerUrl.getPath().substring(1);
      context = context.substring(0, context.indexOf("/"));
      //I think the port gets shortcut because it's the same as the hosts....
      //URL indexUrl = new URL(registerUrl.getProtocol(), registerUrl.getHost(), registerUrl.getPort(), context);
      //URL entryFormUrl = new URL(registerUrl.getProtocol(), registerUrl.getHost(), registerUrl.getPort(), context+"/admin/entryForm.html");
      String indexUrl = registerUrl.getProtocol()+"://"+registerUrl.getAuthority()+":"+registerUrl.getPort()+"/"+context;
      String entryFormUrl = registerUrl.getProtocol()+"://"+registerUrl.getAuthority()+":"+registerUrl.getPort()+"/"+context+"/admin/entryForm.html";
      
      out.write("<p>Default Registry ");
      out.write("<a href='"+indexUrl+"'>Main Page</a> and <a href='"+entryFormUrl+"'>Entry Form</a>");
      out.write("</p>");
   }
%>
</body>

</div>

<%@ include file='../footer.xml' %>
</body>
</html>


