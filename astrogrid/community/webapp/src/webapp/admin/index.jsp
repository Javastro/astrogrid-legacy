<%@ page import="org.astrogrid.community.common.ivorn.CommunityIvornParser,
                 org.astrogrid.store.Ivorn,                 
                 org.astrogrid.community.server.database.manager.DatabaseManagerImpl"
    session="false" %>
<%
	DatabaseManagerImpl dmi = new DatabaseManagerImpl();
	boolean health = dmi.checkDatabaseTables();
%>
<html>
<head>
      <title>Community Administration</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

This section is for the Administration of the Community.  See the menus on the left for particular administration tasks.
<br />
<i>*The health check is by looking for a small amount of test data in the database, if the sql scripts have been changed to not insert this test data, then the health check will always be false.</i>
<br />
<%
  if(!health) {
  	out.write("<font color='red'>A health check on the database is not good, if this is your first time please go to Reset DB for setting up the database.</font>");
  }else {
  	out.write("<font color='blue'>A current health check on the database says the database is good.</font>");
  }

%>

</body>
</html>