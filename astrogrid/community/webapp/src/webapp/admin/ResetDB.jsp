<%@ page import="org.astrogrid.community.common.ivorn.CommunityIvornParser,
                 org.astrogrid.store.Ivorn,                 
                 org.astrogrid.community.server.database.manager.DatabaseManagerImpl"
    session="false" %>
<%
	DatabaseManagerImpl dmi = new DatabaseManagerImpl();
String resetdb = request.getParameter("resetdb");

if(resetdb != null && "true".equals(resetdb)) {
	System.out.println("resetting db tables");
	dmi.resetDatabaseTables();
}
boolean health = dmi.checkDatabaseTables();

%>

    <html>
<head>
     <title>Account Administration</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<h1>
<font color="red">WARNING:DANGER THIS WILL RESET AND CLEAN OUT THE DATABASE ONLY EXPECT TO USE THE FIRST TIME DURING SETUP.</font>
</h1>


<div id='bodyColumn'>
	<i>*The health check is by looking for a small amount of test data in the database, if the sql scripts have been changed to not insert this test data, then the health check will always be false.</i>
   <p>
     Current Health Check of database (true=health,false=bad) = <%=health%>
   </p>
	<p>
	<form name="resetdatabaseform">
		<input type="hidden" name="resetdb" value="true" />
		<input type="submit" name="resetdbsubmit" value="Reset"/>
	</form>
	
	</p>
    
    
   </body>  
</html>    