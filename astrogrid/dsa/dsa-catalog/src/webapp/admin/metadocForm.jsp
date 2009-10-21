<%@ page import="java.io.*,
         java.net.*,
         org.astrogrid.dataservice.metadata.*,
         org.astrogrid.dataservice.metadata.queryable.*,
         org.astrogrid.cfg.ConfigFactory,
         org.astrogrid.dataservice.metadata.*,
         org.astrogrid.dataservice.metadata.queryable.*,
         org.astrogrid.dataservice.service.*,
         org.astrogrid.tableserver.metadata.*,
         org.astrogrid.tableserver.jdbc.*"
   isThreadSafe="false"
   session="false"
%>
<% String pathPrefix = ".."; // For the navigation include %>

<html>
<head>
<title>Configure automatic metadoc generator for <%=DataServer.getDatacenterName() %> </title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</head>


<body>
<%@ include file="../header.xml" %>
<%@ include file="../navigation.xml" %>

<div id='bodyColumn'>

<h1><font color="RED"><strong>THIS FORM IS NOT IN USE YET!</strong></font></h1>
     <h1>Choose catalogues to use:</h1>
     <p>
<%

   out.write("<form action=\"./GenerateMetaDoc\" method=\"get\">");
   RdbmsTableMetaDocGenerator generator = new RdbmsTableMetaDocGenerator();
   String[] cats = generator.getAvailableCatalogs();
   if (cats.length == 0) {
      out.write("Automatic catalog detection failed.<br/>");
      out.write("I will try to use the default catalog on your JDBC connection.<br/>");
         /*
      out.write("Please specify the full schema name for the catalog you wish to use, or leave this box blank to use the default catalogue(s) on your JDBC connection:<br/>");
      out.write("<input type=\"text\" name=\"DSACAT_MANUAL\" value=\"\"/>");
      */
   }
   else {
      out.write("Please select the catalog(s) for which to create a template metadoc.<br/>");
      out.write("(Do not include system catalogs etc - just the astronomy catalogs you wish to publish using this DSA.)");
      out.write("<font size=\"+1\">");
      for (int i = 0; i < cats.length; i++) {
         out.write("<input type=\"checkbox\" name=\"DSACAT_" + 
               cats[i] + "\" checked>&nbsp;&nbsp;" + cats[i] + "</input><br/>");
      }
      out.write("</font>");
   }
%>
   <input type="submit" name='Submit' value="Generate Metadoc"  />
   <input type='hidden' name='UserName' value='JSP'>
   <input type='hidden' name='TargetResponse' value='true'>
   </p>
   </form>
<%
%>
</div>

</body>
</html>

