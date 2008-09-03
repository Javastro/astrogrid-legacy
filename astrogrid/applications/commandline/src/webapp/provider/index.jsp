<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<!-- $Id: index.jsp,v 1.2 2008/09/03 14:19:07 pah Exp $ --><!-- this is a bit of a hack - it should be generated during the site build so
that the styles are correct -->
  <title>AstroGrid Project - AstroGrid Applications Integration</title>
<%@ include file="../inc/header.jsp" %>
      <div id="bodycol">
      <div class="app">
      <div class="h3">
      <h3> <a name="AstroGridApplicatons">AstroGrid Applications
Integration - Version $Name:  $</a> </h3>
      <h4>Configuration</h4>
      <p> From here you can: </p>
      <ul>
        <li> Check <a href="happyaxis.jsp">HappyAxis</a> to see if
you've installed the necessary jars in tomcat to run axis properly.<br/>
        </li>
        <li> See what <a href="servlet/AxisServlet">services</a> are
available. There should be an applicationController service...&nbsp;</li>
        <li>Before you can run the service you need to configure the
ApplicationController - see the <a href="Configuration.html">instructions</a>
which need to be followed
before trying to invoke the service. <br/>
        </li>
        <li><a
 href="/manager/html/reload?path=/astrogrid-applications">restart
          </a>the application in the <a
 href="/manager/html">tomcat manager</a>.<br/>
        </li>
        <li>You can then check your <a href="check_config.jsp">configuration</a> and some simple <a href="TestServlet?suite=org.astrogrid.applications.manager.InstallTest" >automated unit tests</a></li>
        <li>There is a test application in the <a href="test">test
folder</a> that can be quickly configured
to allow the installation to be&nbsp;<a href="maven/Troubleshooting.html">tested</a></li>
     </ul>
      <h5>Application Configuration</h5>
      <p>It does take some work work to <a
 href="ApplicationConfiguration.html">configure&nbsp; a new application</a>
that the command line application controller can run, however. <br/>
      </p>
      <p> Browse the <a
 href="maven/index.html">Maven
documentation</a> for this ApplicationController service. </p>
<h4>Using the Service</h4>
<p>It is possible to call the service directly, with a knowledge of the <a href="./services/CommonExecutionConnectorService?wsdl">wsdl</a>, but if you are programming in java, then you might find it easier to use the delegate contained in the <a href="CommonExecutionConnectorDelegate.jar">this jar</a>. 
	The main calling interface is described by the <a href="/astrogrid-applications/maven/apidocs/org/astrogrid/applications/delegate/CommonExecutionConnectorClient.html">CommonExecutionConnectorClient</a> </p>
	
<h4>Getting the Latest Version</h4>
<p>You can find the latest version of this web application from <a href="http://www.astrogrid.org/maven/docs/snapshot/applications/index.html">http://www.astrogrid.org/maven/docs/snapshot/applications/index.html</a></p>	
      </div>
      </div>
      </div>
<%@ include file="../inc/footer.jsp" %>
