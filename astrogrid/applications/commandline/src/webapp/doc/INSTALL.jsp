<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>Command-line-application server - Installation</title>
<style type="text/css" media="all">
@import url("../style/maven-base.css");
 @import url("../style/maven-theme.css");
</style>
<link rel="stylesheet" href="../style/print.css" type="text/css" media="print">
</link>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
</meta>
<meta name="author" content="Guy Rixon">
</meta>
<meta name="email" content="gtr@ast.cam.ac.uk">
</meta>
</head>
<body class="composite">
<div id="banner"><a href="http://www.astrogrid.org/" id="organizationLogo"><img alt="AstroGrid" src="http://www.astrogrid.org/images/AGlogo"></img></a><a href="" id="projectLogo"><span>CEA command-line-application server</span></a>
  <div class="clear">
    <hr>
    </hr>
  </div>
</div>
<div id="breadcrumbs">
  <div class="xleft"> Last published: Tue 12 February 2008 11:13 GMT
    | Doc for 2008.0a2</div>
  <div class="xright"></div>
  <div class="clear">
    <hr>
    </hr>
  </div>
</div>
<div id="leftColumn">
  <div id="navcolumn">
    <div id="menuAdministrator_documents">
      <h5>Administrator documents</h5>
      <ul>
        <li class="none"><a href="../about.txt">About this component</a></li>
        <li class="none"><a href="../doc/DOWNLOAD.html">Download</a></li>
        <li class="none"><a href="INSTALL.jsp">Installation</a></li>
        <li class="none"><a href="CONFIGURE.jsp">Configuration</a></li>
        <li class="none"><a href="SELF-TEST.jsp">Self-testing</a></li>
        <li class="none"><a href="REGISTER.jsp">Registration</a></li>
        <li class="none"><a href="EXTTEST.jsp">External testing</a></li>
        <li class="none"><a href="UPGRADE.jsp">Upgrading</a></li>
        <li class="none"><a href="UNINSTALL.jsp">Uninstallation</a></li>
        <li class="none"><a href="REFERENCE.jsp">Reference manual</a></li>
      </ul>
    </div>
    <div id="menuLocal_controls_for_server">
      <h5>Local controls for server</h5>
      <ul>
        <li class="none"><a href="../happyaxis.jsp">Axis test</a></li>
        <li class="none"><a href="../TestServlet?suite=org.astrogrid.applications.component.CEAComponentManagerFactory">Installation test</a></li>
        <li class="none"><a href="../fingerprint.jsp">Fingerprint</a></li>
        <li class="none"><a href="../cec-http?method=returnRegistryEntry">Show registration document</a></li>
        <li class="none"><a href="../VOSI/capabilities">Show service capabilities</a></li>
        <li class="none"><a href="../VOSI/availability">Show service availability</a></li>
        <li class="none"><a href="../admin/RegistrationInstructions.jsp">Register</a></li>
        <li class="none"><a href="../admin/configuration-describe.jsp">Show current configuration</a></li>
        <li class="none"><a href="../chooseapp.jsp">Test run of application</a></li>
        <li class="none"><a href="../queue.jsp">Show queue of jobs</a></li>
      </ul>
    </div>
    <a href="http://www.astrogrid.org" title="Provided by Astrogrid" id="poweredBy"><img alt="Provided by Astrogrid" src="../images/logos/http://www.astrogrid.org/images/AGlogo"></img></a></div>
</div>
<div id="bodyColumn">
  <div class="contentBox">
    <div class="section"><a name="Installation"></a>
      <h2>Installation</h2>
      <p>This document describes the standard installation. Please see the reference guide for
        possible variations.</p>
      <div class="subsection"><a name="Java_Runtime_Environment"></a>
        <h3>Java Runtime Environment</h3>
        <p>Use SUN Microsystem's implementation of Java 5.
          You only need the Java Runtime Environment package; you don't 
          need the full Java Development Kit.</p>
      </div>
      <div class="subsection"><a name="Web_container__Apache-Tomcat_"></a>
        <h3>Web container (Apache-Tomcat)</h3>
        <p>Use Apache-Tomcat 5.5 as the web-container.</p>
      </div>
      <div class="subsection"><a name="Tomcat_users_and_roles"></a>
        <h3>Tomcat users and roles</h3>
        <p>In Tomcat's <i>tomcat-users.xml</i> file, make sure that
          you have at least one user with the <i>manager</i> role.</p>
      </div>
      <div class="subsection"><a name="Naming_the_web-application_context"></a>
        <h3>Naming the web-application context</h3>
        <p> You must choose a name for the "context" in which the web-application runs. This name will 
          become part of the URLs for your CL-CEC web-service and the web pages of your CL-CEC web-application. 
          E.g., if you name the web-application context "CL-CEC-1", the URLs will be of the form
        </p>
        <pre>
http://your.server.address:8080/CL-CEC-1/
http://your.server.address:8080/CL-CEC-1/services/CommonExecutionConnectorService </pre>
        respectively.
        
      </div>
      <div class="subsection"><a name="The_applications"></a>
        <h3>The applications</h3>
        <p> You must have the applications installed locally in a directory where the CEC can read
          and run them. If the applications have their own configuration then you should complete
          this and test it before installing the CEC. </p>
      </div>
      <div class="subsection"><a name="AstroGrid_MySpace"></a>
        <h3>AstroGrid MySpace</h3>
        <p> Your CEC works with AstroGrid's <i>FileManager</i> component to read and write files in
          AstroGrid MySpace. The details of which stores to use are set by the clients of the web-application
          in requests to the CEC web-service. You do not need to supply your own FileManager to operate
          the CEC web-application.</p>
        <p>This version of the web-application cannot use IVOA-standard VOSpace.</p>
      </div>
      <div class="subsection"><a name="Registry"></a>
        <h3>Registry</h3>
        <p> Your web application uses an IVOA resource-registry to publicize its capabilities. You
          must have access to a registry in which you can publish your resources and a registry from
          which your CEC can search for resources at run-time. </p>
        <p> The publishing registry and the searchable registry may be the same service. In this
          case, you will typically use an external registry. As an alternative, you may choose to
          run a local publishing registry. This makes it easier to publish and maintain groups of
          services on your site and also helps you establish a "brand" for your services by
          associating them with your own publishing authority. If you want to run a publishing
          registry, then you should install and set up the AstroGrid registry component <em>before</em> setting up your CEC. </p>
      </div>
      <div class="subsection"><a name="Installation_procedure"></a>
        <h3>Installation procedure</h3>
        <ol>
          <li>Obtain a copy of the WAR file for the CL-CEC from the AstroGrid software site. See the <a href="DOWNLOAD.html">guide to downloading</a> for details. </li>
          <li>Rename the WAR file to match the chosen name of the web-application context; e.g.
            CL-CEC-1.war if the context is CL-CEC-1 as in the example above. </li>
          <li>Copy your renamed WAR file into the <i>webapps</i> sub-directory of your Tomcat
            installation. Wait a few seconds. Tomcat notices the WAR file, unpacks the web-application
            into a sub-directory tree in the <i>webapps</i> directory and starts the
            web-application.</li>
          <li> Check that the web-application has started correctly by going, with a web-browser, to the
            root of the web application (http://your.server.address:8080/CL-CEC-1/ in the example above).
            If no web-page is returned, then there is a problem with the WAR. </li>
          <li>Now set up your web-application by following the instructions in the configuration guide.</li>
        </ol>
      </div>
    </div>
  </div>
</div>
<div class="clear">
  <hr>
  </hr>
</div>
<div id="footer">
  <div class="xright">© 2003-2008, AstroGrid</div>
  <div class="clear">
    <hr>
    </hr>
  </div>
</div>
</body>
</html>
