<%@ page import="java.io.*,
       org.w3c.dom.*,
       org.astrogrid.io.*,
       org.astrogrid.config.SimpleConfig,
       org.astrogrid.util.DomHelper,
       org.astrogrid.registry.client.RegistryDelegateFactory,
       org.astrogrid.dataservice.metadata.*,
       org.astrogrid.dataservice.service.*"
   isThreadSafe="false"
   session="false"
%>
<html>
<head>
<title><%=DataServer.getDatacenterName() %> Metadata pushed</title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file='../header.xml' %>
<%@ include file='navigation.xml' %>

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
                        SimpleConfig.getSingleton().getString(RegistryDelegateFactory.ADMIN_URL_PROPERTY)+")\n"
                  ));
   }
%>


</div>

<%@ include file='../footer.xml' %>
</body>
</html>


