<%@ page import="java.io.*,
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
          @import url("../style/maven-base.css");
          @import url("../style/maven-theme.css");
</style>
</title>
</head>

<body>
<%@ include file='../header.xml' %>
<%@ include file='navigation.xml' %>

<div id='bodyColumn'>

<body>

  <h1>Register this datacenter</h1>
  <p>
         <form action="Register" method="POST">
          <p>
            Registry URL <input type="text" name="RegistryUrl"/> <br />
            (leave blank to register at <%= SimpleConfig.getSingleton().getString(RegistryDelegateFactory.ADMIN_URL_PROPERTY) %>)<br />
          </p>
           <p>
             <input type="submit" name='Submit' value="Register"  />
           </p>
      </form>

</body>

</div>

<%@ include file='../footer.xml' %>
</body>
</html>


