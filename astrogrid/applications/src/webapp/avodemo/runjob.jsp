<%@ page import="org.astrogrid.applications.avodemo.*" session="true" %>
<jsp:useBean id="runner" scope="request" class="org.astrogrid.applications.avodemo.AVODemoRunner" /> 
<%  
   runner.setToAddress(request.getParameter("email"));
   runner.setHemi(request.getParameter("hemi"));
   runner.setSector(request.getParameter("sector"));
   runner.runit();
%>

<html>
<head>
<title>Job Run</title>
</head>
<body bgcolor='#ffffff'>
<h1>Job Submitted</h1>
<p>The workflow for discovery of sources in GOODS field <%= request.getParameter("sector") %><%= request.getParameter("hemi") %> and the determination of their photometric redshifts using hyperz
  has been started.</p>
<p>The job should take approximately 5-10minutes. You will be emailed at <%= request.getParameter("email") %> with a url where you can pick up the results as a VOTable.</p>
<p><a href="index.jsp">Return to submission page</a></p>
</body>
</html>

