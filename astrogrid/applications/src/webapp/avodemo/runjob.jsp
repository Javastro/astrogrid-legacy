<%@ page import="org.astrogrid.applications.avodemo.*" session="true" %>
<jsp:useBean id="runner" scope="request" class="org.astrogrid.applications.avodemo.AVODemoRunner" /> 
<%! boolean isworkflow = false; %>
<%  
   runner.setToAddress(request.getParameter("email"));
   runner.setHemi(request.getParameter("hemi"));
   runner.setSector(request.getParameter("sector"));
   isworkflow = request.getParameterMap().containsKey("workflow");
   if(isworkflow)
   {
   runner.runit();
   }
   else
   {
   runner.createWorkflow();
   }
%>

<html>
<head>
<title>Job Run</title>
</head>
<body bgcolor='#ffffff'>
<% if(isworkflow){ %>
<h1>Workflow Created</h1>
<p>The workflow for discovery of sources in GOODS field <%= request.getParameter("sector") %><%= request.getParameter("hemi") %> and the determination of their photometric redshifts using hyperz
  has been created.</p>
  
<p>You can log in to the <a href="http://vm06.astrogrid.org:8080/astrogrid-portal/main/mount/login/login-form">Astrogrid portal</a> as the avodemo@test.astrogrid.org user to view and run this workflow</p>

<% }else{ %>
<h1>Job Submitted</h1>
<p>The workflow for discovery of sources in GOODS field <%= request.getParameter("sector") %><%= request.getParameter("hemi") %> and the determination of their photometric redshifts using hyperz
  has been started.</p>
<p>The job should take approximately 5-10minutes. You will be emailed at <%= request.getParameter("email") %> with a url where you can pick up the results as a VOTable.</p>
<p><a href="index.jsp">Return to submission page</a></p>

<% } %>
</body>
</html>

