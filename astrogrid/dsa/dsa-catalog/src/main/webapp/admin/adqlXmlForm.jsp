<%@ page import="java.io.*,
         java.net.*,
         org.astrogrid.dataservice.service.*,
         org.astrogrid.query.SimpleQueryMaker,
         org.astrogrid.cfg.ConfigFactory"
   isThreadSafe="false"
   session="false"
%>
<% String pathPrefix = ".."; // For the navigation include %>

<html>
<head>
<title><%=DataServer.getDatacenterName() %> Query Form</title>
<style type="text/css" media="all">
          @import url("../style/astrogrid.css");
</style>
</title>
</head>


<body>
<%@ include file="../header.xml" %>
<%@ include file="../navigation.xml" %>

<div id='bodyColumn'>

  <h1>Submit an ADQL/xml Query to <%=DataServer.getDatacenterName() %></h1>
  <p>
  <b>Enter your ADQL/xml query here:</b><br/>
  <em>(The default query selects all columns from the first 100 rows
  of the table specified by the <tt>datacenter.self-test.table</tt> property.)</em>
      <form action="BrowserAskQuery" method="POST">
        <input type='hidden' name='UserName' value='JSP'>
        <% 
            String adql = request.getParameter("AdqlXml");
            if ((adql == null) || adql.trim().equals("")) {
              adql = InstallationSyntaxCheck.getTestSuiteAdql(
                  "selectAllLimit.xml");
            }
        %>
        <!-- Don't get whitespace in the textbox! -->
        <textarea name="AdqlXml" rows="20" cols="100%"><%=adql%></textarea>
        <p/>
        <%@ include file='resultsForm.xml' %>
        <p>
        <input type="submit" value="Submit ADQL" />
        <input type='hidden' name='TargetResponse' value='true'>

        </p>
      </form>
    </p>
</div>
</body>
</html>

