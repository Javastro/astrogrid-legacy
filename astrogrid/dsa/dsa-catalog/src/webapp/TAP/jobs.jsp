<%@ page import="java.util.List, org.astrogrid.dataservice.jobs.Job, org.astrogrid.cfg.ConfigFactory"
   isThreadSafe="false"
   session="false"
%>
<% String pathPrefix = ".."; // For the navigation include %>


<html>
<head>
<title>Job list</title>
<style type="text/css" media="all">@import url("../style/astrogrid.css");</style>
<style type="text/css" media="all">table, th, td { border-collapse: collapse; border: 1px solid black; }</style>
</head>
<body>
<%@ include file="../header.xml" %>
<%@ include file="../navigation.xml" %>
<div id='bodyColumn'>

<h1>Job list</h1>

<%
Boolean admin = (Boolean) request.getAttribute("uws.admin");
boolean isAdmin = (admin == null)? false : admin;
String caller = (String) request.getAttribute("uws.principal");
System.out.println("Caller: " + caller);
List<Job> jobs;
try {
  if (isAdmin) {
    jobs = Job.list(); // All jobs
  }
  else {
    jobs = Job.list(caller); // Anonymous jobs and ones owned by this caller
  }
}
catch (Exception e) {
  e.printStackTrace();
  jobs = null;
}

if (jobs == null) {
%>
  <p>The job database is not available. You may need to initialize it.</p>
<%
}
else {
%>
<p>
  The ID values of each job is linked to details of jobs and job controls.
  Refresh the page to see job progress.
</p>
<hr/>
<table>
  <tr>
    <th>Identifier</th>
    <th>Owner</th>
    <th>Phase</th>
    <th>Destruction time</th>
    <th>Results format</th>
    <th>Results go to</th>
  </tr>
<%
  for (Job j : jobs) {
    String href = pathPrefix + "/TAP/async/" + j.getId();
%>
<tr>
  <td><a href="<%=href%>"><%=j.getId()%></a></td>
  <td><%=j.getOwner()%></td>
  <td><%=j.getPhase()%></td>
  <td><%=j.getDestructionTime()%></td>
  <td><%=j.getFormat()%></td>
  <td><%=j.getDestination()%></td>
</tr>
<%
}
%>
</table>
<%
}
%>
<%
if (request.getAttribute("uws.admin") != null) {
%>
<hr/>
<p>
  Initializing the database deletes all records of the jobs.
  <form action="jobs" method="post">
    <input type="hidden" name="ACTION" value="INITIALIZE"/>
    <input type="submit" value="Destroy and recreate the database"/>
  </form>
</p>
<%
}
%>
</div>
</body>
</html>
