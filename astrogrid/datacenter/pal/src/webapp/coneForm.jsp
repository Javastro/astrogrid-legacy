<%@ page import="java.io.*,
         org.astrogrid.config.SimpleConfig,
         org.astrogrid.datacenter.service.*,
         org.astrogrid.datacenter.queriers.sql.*"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>Cone Query Form for <%=DataServer.getDatacenterName() %> </title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</title>
</head>


<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<body>
<%
   if (SimpleConfig.getSingleton().getString(SqlMaker.CONE_SEARCH_TABLE_KEY, null) == null) {
      out.write("This server is not configured for cone searches");
   }
   else {
%>
     <h1>Submit a Region Query to <%=DataServer.getDatacenterName() %></h1>
     <p>
     This cone search will be applied to the RAs from
        <%= SimpleConfig.getSingleton().getString(SqlMaker.CONE_SEARCH_TABLE_KEY, "(ERROR: Not Configured)") %>.
        <%= SimpleConfig.getSingleton().getString(SqlMaker.CONE_SEARCH_RA_COL_KEY, "(ERROR: Not Configured") %>
        and the DECs from
        <%= SimpleConfig.getSingleton().getString(SqlMaker.CONE_SEARCH_TABLE_KEY, "(ERROR: Not Configured") %>.
        <%= SimpleConfig.getSingleton().getString(SqlMaker.CONE_SEARCH_DEC_COL_KEY, "(ERROR: Not Configured") %>
   
         <form action="SubmitCone" method="get">
          <p>
            <table border='0'>
            <tr><td align='right'>Right Ascension in decimal degrees, J2000 <td><input type="text" name="RA"/></tr>
            <tr><td align='right'>Declination in decimal degress, J2000   <td><input type="text" name="DEC" /></tr>
            <tr><td align='right'>Search radius in decimal degrees         <td><input type="text" name="SR" /></tr>
            </table>
          </p>
           <p>
             <%@ include file='resultsForm.xml' %>
             <input type="submit" name='Submit' value="Search Cone"  />
           </p>
      </form>
<%
}
%>

</body>
</html>

