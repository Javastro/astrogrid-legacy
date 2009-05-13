<%@ page import="
       org.astrogrid.cfg.ConfigFactory,
       org.astrogrid.cfg.ConfigReader,
       org.astrogrid.dataservice.service.DataServer,
       org.astrogrid.dataservice.service.ServletHelper,
       org.astrogrid.tableserver.metadata.TableInfo,
       org.astrogrid.tableserver.metadata.TableMetaDocInterpreter"
    session="false"
%>
<% String pathPrefix = ".."; // For the navigation include %>

<html>
<head>
<title>MultiCone Query Form for <%=DataServer.getDatacenterName() %></title>
<style type="text/css" media="all">
       @import url("../style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="../header.xml" %>
<%@ include file="../navigation.xml" %>

<div id="bodycolumn">

<h1>Submit a Multiple Cone Search Query to <%=DataServer.getDatacenterName() %>
</h1>
<p>
<%
    ConfigReader configRdr = ConfigFactory.getCommonConfig();
    String doesCone = configRdr.getString("datacenter.implements.conesearch");
    String doesMulti = configRdr.getString("datacenter.implements.multicone");
    if (doesCone == null || !"true".equals(doesCone.toLowerCase()) ||
        doesMulti == null || !"true".equals(doesMulti.toLowerCase())) {
        out.write("MultiCone is disabled in this DSA/catalog installation.");
    }
    else {
        TableInfo[] tables = TableMetaDocInterpreter.getConesearchableTables();
        if (tables.length == 0) {
            out.println("No conesearchable tables are specified " +
                        "in the DSA/catalog metadoc file.");
        }
        else {
            out.println("  <form " +
                        "action='../MultiCone'" +
                        "method='post'" +
                        "enctype='multipart/form-data'>");
            out.println("    <select name='DSACATTAB'>");
            for (int i = 0; i < tables.length; i++) {
                String fullTable = tables[i].getCatalogName() + "."
                                 + tables[i].getName();
                out.println("      <option value='" + fullTable + "'>" +
                            fullTable + "</option>");
            }
            out.println(    "</select>");
%>
  <p>
    <table border='0'>
    <tr>
      <td align="right">Input VOTable</td>
      <td><input type="file" name="TABLE" /></td>
    </tr>
    <tr>
      <td align="right">Table column giving RA in J2000 degrees</td>
      <td><input type="text" name="RA" />(leave blank to select by UCD)</td>
    </tr>
    <tr>
      <td align="right">Table column giving Declination in J2000 degrees</td>
      <td><input type="text" name="DEC" />(leave blank to select by UCD)</td>
    </tr>
    <tr>
      <td align="right">Search radius in degrees</td>
      <td><input type="text" name="SR" /></td>
    </tr>
    <tr>
      <td align="right">Matched rows to return</td>
      <td><input type="radio" name="FIND" value="BEST"
                 checked="checked" />Best
          <input type="radio" name="FIND" value="ALL"
                 />All</td>
    </tr>
    </table>
  </p>
  <p>
    <div id="resultsSpec">
    Results Format
    <select name="Format">
      <option selected="selected">HTML</option>
      <option>VOTable</option>
      <option>VOTable-binary</option>
      <option>CSV</option>
    </select>
    </div>
    <input type="submit" name="Submit" value="Submit Multicone" />
    <input type="hidden" name="UserName" value="JSP" />
    <input type="hidden" name="TargetResponse" value="true" />
  </p>
</form>
<%
        }
    }
%>

</div>
</body>
</html>
