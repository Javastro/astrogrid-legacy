<%@ page import="java.io.*,
         org.astrogrid.datacenter.service.*"
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

  <h1>Submit an ADQL/sql Query to <%=DataServer.getDatacenterName() %></h1>
  <p>
  <b>Enter your ADQL/sql query here <a href='http://wiki.astrogrid.org/bin/view/Support/HelpAdqlSql'>(Help)</a>
</b>
  
    <form method="get" onSubmit="
      this.action=formAction;
      return true;
      ">
        <p>
        <textarea name="AdqlSql" rows="4" cols="100%"></textarea>
        </p>
        <p>
          <%@ include file='resultsForm.xml' %>
          <input type="submit" name='SubmitSql' value="Submit Query" onclick='formAction="SubmitAdqlSql";' />
          (If you get an invalid XML error, this is your browser being 'safe'.  Use the buttons below to
          translate the query then check the XML)
        </p>
        <p>
           <!-- <input type='submit' name='MakeAdql05' value='Make ADQL v0.5' onclick='formAction="adqlXmlFromSql.jsp";' /> -->
           <input type='submit' name='MakeAdql074' value='Make ADQL v0.7.4' onclick='formAction="adqlXmlFromSql.jsp";' />
        </p>
   </form>

</body>
</html>

