<%@ page import="java.io.*,
         org.astrogrid.config.SimpleConfig,
         org.astrogrid.datacenter.service.*,
         org.astrogrid.datacenter.queriers.sql.*,
         org.astrogrid.datacenter.sqlparser.*"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>SQL Query Form for <%=DataServer.getDatacenterName() %> </title>
<style type="text/css" media="all">
          @import url("./style/maven-base.css");
          @import url("./style/maven-theme.css");
</style>
</title>
</head>


<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<body>

  <h1>Submit a Region Query to <%=DataServer.getDatacenterName() %></h1>
  <p>
  This cone search will be applied to the RAs from
     <%= SimpleConfig.getSingleton().getString(SqlMaker.CONE_SEARCH_TABLE_KEY, "(ERROR: Not Configured") %>.
     <%= SimpleConfig.getSingleton().getString(SqlMaker.CONE_SEARCH_RA_COL_KEY, "(ERROR: Not Configured") %>
     and the DECs from
     <%= SimpleConfig.getSingleton().getString(SqlMaker.CONE_SEARCH_TABLE_KEY, "(ERROR: Not Configured") %>.
     <%= SimpleConfig.getSingleton().getString(SqlMaker.CONE_SEARCH_DEC_COL_KEY, "(ERROR: Not Configured") %>

      <form action="submitCone.jsp" method="POST">
       <p>
         Right Ascension in decimal degrees, J2000 <input type="text" name="RA"/><br />
         Decliniation in decimal degress, J2000 <input type="text" name="DEC" /><br />
         Search radius in decimal degrees <input type="text" name="SR" /><br />
       </p>
        <p>
          <%@ include file='resultsForm.xml' %>
          <input type="submit" name='Submit' value="Search Cone"  />
        </p>
   </form>

</body>
</html>

