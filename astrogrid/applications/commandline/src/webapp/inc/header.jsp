<!-- note that this file could be included with a jsp:include which then would allow parameters to be included which would in turn allow breadcrumbs etc to be added
perhaps do this via spring --> 
<% String contextPath = request.getContextPath();
%>
<style type="text/css" media="all">
@import url("<%=contextPath%>/style/maven-base.css");
@import url("<%=contextPath%>/style/maven-theme.css");
</style>
<link rel="stylesheet" href="<%=contextPath%>/style/print.css" type="text/css" media="print"/>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<meta name="author" content="Paul Harrison"/>
<meta name="email" content="pah@jb.man.ac.uk"/>
</head>
<body class="composite">
<div id="banner"><a href="http://www.astrogrid.org/" id="organizationLogo"><img alt="AstroGrid" src="http://www.astrogrid.org/images/AGlogo.png"></img></a>
<a href="<%=contextPath%>/doc/index.jsp" id="projectLogo"><span>CEA Common Execution Controller</span></a>
  <div class="clear">
    <hr/>
  </div>
</div>
<div id="breadcrumbs">
<!-- todo - need to get this updated -->
  <div class="xleft"> <%@ include file="/about.txt" %></div>
  <div class="xright"></div>
  <div class="clear">
    <hr/>
  </div>
</div>
<div id="leftColumn">
  <div id="navcolumn">
    <div id="menuAdministrator_documents">
      <h5>Administrator documents</h5>
      <ul>
        <li class="none"><a href="<%=contextPath%>/doc/index.jsp">About this component</a></li>
        <li class="none"><a href="<%=contextPath%>/doc/INSTALL.jsp">Installation</a></li>
        <li class="none"><a href="<%=contextPath%>/doc/CONFIGURE.jsp">Configuration</a></li>
        <li class="none"><a href="<%=contextPath%>/doc/SELF-TEST.jsp">Self-testing</a></li>
        <li class="none"><a href="<%=contextPath%>/doc/REGISTER.jsp">Registration</a></li>
        <li class="none"><a href="<%=contextPath%>/doc/EXTTEST.jsp">External testing</a></li>
        <li class="none"><a href="<%=contextPath%>/doc/Use.jsp">Using the Server</a></li>
        <li class="none"><a href="<%=contextPath%>/doc/UPGRADE.jsp">Upgrading</a></li>
        <li class="none"><a href="<%=contextPath%>/doc/UNINSTALL.jsp">Uninstallation</a></li>
        <li class="none"><a href="<%=contextPath%>/doc/REFERENCE.jsp">Reference manual</a></li>
      </ul>
    </div>
    <div id="menuLocal_controls_for_server">
      <h5>Local controls for server</h5>
      <ul>
        <li class="none"><a href="<%=contextPath%>/happyaxis.jsp">Axis test</a></li>
        <li class="none"><a href="<%=contextPath%>/TestServlet?suite=org.astrogrid.applications.component.ContainerInstallationTestSuite">Installation test</a></li>
        <li class="none"><a href="<%=contextPath%>/fingerprint.jsp">Fingerprint</a></li>
        <li class="none"><a href="<%=contextPath%>/uws/reg">Show registration documents</a></li>
        <li class="none"><a href="<%=contextPath%>/VOSI/capabilities">Show service capabilities</a></li>
        <li class="none"><a href="<%=contextPath%>/VOSI/availability">Show service availability</a></li>
<!--  
        <li class="none"><a href="../admin/RegistrationInstructions.jsp">Register</a></li>
-->
        <li class="none"><a href="<%=contextPath%>/admin/configuration-describe.jsp">Show current configuration</a></li>
        <li class="none"><a href="<%=contextPath%>/xforms/apptpl.xml">define application</a></li>
<!--
        <li class="none"><a href="../admin/clean.jsp">Clean old run files</a></li>
-->
      </ul>
    </div>
    <div>
      <h5>Application Execution</h5>
      <ul>
        <li class="none"><a href="<%=contextPath%>/app/chooseapp.jsp">Test run of application</a></li>
        <li class="none"><a href="<%=contextPath%>/admin/queue.jsp">Show queue of jobs</a></li>
        <li><a href="<%=contextPath%>/uws/jobs">UWS root</a></li>
      </ul>
    </div>
    <a href="http://www.astrogrid.org" title="Provided by Astrogrid" id="poweredBy"><img alt="Provided by Astrogrid" src="http://www.astrogrid.org/images/AGlogo50.png"></img></a></div>
</div>