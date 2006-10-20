<%@ page import="org.astrogrid.community.common.ivorn.CommunityIvornParser,
                 org.astrogrid.store.Ivorn,                 
                 org.astrogrid.community.server.database.manager.DatabaseManagerImpl"
    session="true" %>
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
        <title>Initializing the community database</title>
        <style type="text/css" media="all">
            @import url("../style/astrogrid.css");
        </style>
    </head>
    <body>
        <%@ include file="beans.xml" %>
        <%@ include file="header.xml" %>
        <%@ include file="navigation.xml" %>
        <div id='bodyColumn'>
          <h1>Initializing the community database</h1>
          
          <p>
          Use the "Initialize DB" button below to create empty database tables 
          for a <em>new</em> community.
          </p>
          
          <p>
          <strong>This will delete all data already in the database, destroying 
          all records of users. Don't press the button unless you are prepared
          to lose your user-data.</strong>
          </p>

          <p>
          If you have just installed the community
          software, initializing the database should check the "health"
          indication from "not useable" to "OK". If the "health" indication
          doesn't change to "OK" after initialization, then the connection
          between this web application and the database is suspect: check the
          configuration of the JEE DataSource.
          </p>
          
          <p>
            <form name="resetdatabaseform" method="post">
              <input type="hidden" name="resetdb" value="true" />
              <input type="submit" name="resetdbsubmit" value="Initialize DB"/>
            </form>
          </p>
        </div>
    </body>  
</html>    
