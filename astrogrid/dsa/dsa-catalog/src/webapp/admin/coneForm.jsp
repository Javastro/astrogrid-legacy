<%@ page 
  import="org.astrogrid.dataservice.Configuration,
          org.astrogrid.tableserver.metadata.TableInfo"
  isThreadSafe="false"
  session="false"
  contentType="text/html"
  pageEncoding="UTF-8"
%>
<% String pathPrefix = ".."; // For the navigation include %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title>Cone-search Query Form</title>
<meta http-equiv="Content-type" content="text/html;charset=UTF-8">
<style type="text/css" media="all">@import url("../style/astrogrid.css");</style>
</head>


<body>
<%@ include file="../header.xml" %>
<%@ include file="../navigation.xml" %>

<div id='bodyColumn'>

  <h1>Run a cone search</h1>
<%
  if (!Configuration.isConeSearchEnabled()) {
%>
  <p>Cone search is disabled in this installation.</p>
<%
  }
  else if (!Configuration.hasConesearchableTables()) {
%>
  <p>No cone-searchable tables are specified in the database-configuration ("metadoc") file.</p>
<%
   }
   else {
%>
<form action='../SubmitCone' method='get'>
   <table border='0'>
     <tr>
       <td>Database table</td>
       <td>
         <select name='DSACATTAB'>
<%
            TableInfo[] tables = Configuration.getConesearchableTables();
            for (int i = 0; i < tables.length; i++) {
                String fullTable = tables[i].getCatalogName() + "."
                                 + tables[i].getName();
                out.println("      <option value='" + fullTable + "'>" +
                            fullTable + "</option>");
            }
%>
        </select>
      </td>
    </tr>
    <tr>
      <td>RA in J2000 degrees</td>
      <td><input type="text" name="RA"></td>
    </tr>
    <tr>
      <td>Declination in J2000 degrees</td>
      <td><input type="text" name="DEC"></td>
    </tr>
    <tr>
      <td>Search radius in degrees</td>
      <td><input type="text" name="SR"></td>
    </tr>
    <tr>
      <td>Results Format</td>
      <td>
        <select name="Format">
          <option selected="selected">HTML</option>
          <option>VOTable</option>
          <option>VOTable-binary</option>
          <option>CSV</option>
        </select>
      </td>
    </tr>
    <tr>
      <td></td>
      <td><input type="submit" name="Submit" value="Run the cone search"></td>
    </tr>
  </table>
  <p>
    <input type="hidden" name="UserName" value="JSP">
    <input type="hidden" name="TargetResponse" value="true">
  </p>
</form>
<%
    }
%>

</div>
</body>
</html>
