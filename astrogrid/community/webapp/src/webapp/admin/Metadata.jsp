<%@ page import="org.astrogrid.community.common.policy.data.AccountData,
                 org.astrogrid.community.common.ivorn.CommunityIvornParser,
                 org.astrogrid.store.Ivorn,                 
                 org.astrogrid.community.server.security.manager.SecurityManagerImpl,
                 org.astrogrid.community.server.policy.manager.PolicyManagerImpl,                 
                 org.astrogrid.community.server.policy.manager.AccountManagerImpl"
    session="true" %>

    <html>
<head>
     <title>Metadata information</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="beans.xml" %>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>
	<p>
		Below is a link to a Metadata template, it needs to have the COMMUNITYID changed, 
		the web service endpoints, and Contact information put in appropriately and submitted to a registry.
	</p>
    <a href="Metadata.xml">Metadata</a>
    
   </body>  
</html>    