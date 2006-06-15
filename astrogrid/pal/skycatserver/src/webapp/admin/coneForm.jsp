<%@ page import="java.io.*,
         java.net.*,
         org.astrogrid.dataservice.metadata.*,
         org.astrogrid.dataservice.metadata.queryable.*,
         org.astrogrid.cfg.ConfigFactory,
         org.astrogrid.dataservice.metadata.*,
         org.astrogrid.dataservice.metadata.queryable.*,
         org.astrogrid.dataservice.service.*,
         org.astrogrid.tableserver.jdbc.*"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>Cone Query Form for <%=DataServer.getDatacenterName() %> </title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>


<body>
<%@ include file="../header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<body>
<%
//   if (ConfigFactory.getCommonConfig().getString(SqlWriter.CONE_SEARCH_TABLE_KEY, null) == null) {
//      out.write("This server is not configured for cone searches");
//   }
//   else
   {
%>
     <h1>Submit a Region Query to <%=DataServer.getDatacenterName() %></h1>
     <p>
<%
      
      ConeConfigQueryableResource queryable = new ConeConfigQueryableResource();
      
      if ((queryable == null) || (queryable.getSpatialGroups() == null)) {
         out.write("This server is not configured for cone searches");
      }
      else {
         out.write("This cone search will be applied to ");
         SearchGroup[] coneGroups = queryable.getSpatialGroups();
         for (int i = 0; i < coneGroups.length; i++) {
            SearchField[] coneFields = queryable.getSpatialFields(coneGroups[i]);
            for (int j = 0; j < coneFields.length; j++) {
               out.write(coneFields[j].getId()+" ");
            }
            out.write(", ");
         }
   %>
         <form action="../SubmitCone" method="get">
          <p>
            <table border='0'>
            <tr><td align='right'>Right Ascension in decimal degrees, J2000 <td><input type="text" name="RA"/></tr>
            <tr><td align='right'>Declination in decimal degrees, J2000   <td><input type="text" name="DEC" /></tr>
            <tr><td align='right'>Search radius in decimal degrees         <td><input type="text" name="SR" /></tr>
            </table>
          </p>
           <p>
             <%@ include file='resultsForm.xml' %>
             <input type="submit" name='Submit' value="Search Cone"  />
             <input type='hidden' name='UserName' value='JSP'>
             <input type='hidden' name='TargetResponse' value='true'>
           </p>
      </form>
<%
      }
   }
%>

</body>
</html>

