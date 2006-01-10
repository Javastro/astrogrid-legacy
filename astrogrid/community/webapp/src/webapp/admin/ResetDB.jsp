<%@ page import="org.astrogrid.community.common.ivorn.CommunityIvornParser,
                 org.astrogrid.store.Ivorn,                 
                 org.astrogrid.community.server.database.manager.DatabaseManagerImpl"
    session="false" %>
<%
	DatabaseManagerImpl dmi = new DatabaseManagerImpl();
    String resetdb = request.getParameter("resetdb");

    if (resetdb != null && "true".equals(resetdb))
        {
        System.out.println("resetting db tables");
        dmi.resetDatabaseTables();
        }

    String status = dmi.checkDatabaseTables() ? "<font color=\"green\">Ok</font>" : "<font color=\"red\">Fail</font>" ;
%>
<html>
    <head>
        <title>Database admin</title>
        <style type="text/css" media="all">
            @import url("../style/astrogrid.css");
        </style>
    </head>
    <body>
        <%@ include file="header.xml" %>
        <%@ include file="navigation.xml" %>
        <div id='bodyColumn'>
            <p>
                <h1>
                    <font color="red">WARNING</font>
                </h1>
                <font color="red">This will reset the database tables, deleting any existing data in the database.</font>
                <br>
                <font color="red">This should only be used during initial installation and setup.</font>
            </p>
            <p>
                Current database status : [<%= (status) %>]
                <br>
                <font size="-2">
                    <i>*The health check looks for a known set of test data in the database, if the test data has not been added, then the health check will fail.</i>
                </font>
            </p>
            <p>
                <form name="resetdatabaseform" method="post">
                    <input type="hidden" name="resetdb" value="true" />
                    <input type="submit" name="resetdbsubmit" value="Reset"/>
                </form>
            </p>
        </div>
    </body>  
</html>    
