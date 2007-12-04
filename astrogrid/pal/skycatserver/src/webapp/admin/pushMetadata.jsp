<%@ page import="java.io.*,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.astrogrid.cfg.ConfigFactory,
       org.astrogrid.registry.client.RegistryDelegateFactory,
       org.astrogrid.dataservice.service.*"
   isThreadSafe="false"
   session="false"
%>
<% String pathPrefix = ".."; // For the navigation include %>

<html>
<head>
<title><%=DataServer.getDatacenterName() %> Metadata pushed</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file='../header.xml' %>
<%@ include file='../navigation.xml' %>

<div id='bodyColumn'>

<%
   try {
      String[] regList = VoDescriptionServer.pushToRegistry();
      out.write("Metadata for "+DataServer.getDatacenterName()+" Pushed to <li>");
      for (int i = 0; i < regList.length; i++) {
         out.write("<li>"+regList[i]);
      }
   } catch (Exception e) {
      out.println(ServletHelper.exceptionAsHtml(
                     "",
                     e,
                     "Failed to send metadata (Config target "+
                        //ConfigFactory.getCommonConfig().getString(DelegateProperties.ADMIN_URL_PROPERTY)+")\n"
                        ConfigFactory.getCommonConfig().getString(RegistryDelegateFactory.ADMIN_URL_PROPERTY)+")\n"
                  ));
   }
%>


</div>

<%@ include file='../footer.xml' %>
</body>
</html>


