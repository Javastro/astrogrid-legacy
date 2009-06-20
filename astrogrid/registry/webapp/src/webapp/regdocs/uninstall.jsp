<%@ page import="org.astrogrid.config.SimpleConfig"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>AstroGrid Registry FAQ</title>
<style type="text/css" media="all">
	<%@ include file="/style/astrogrid.css" %>          
</style>
</title>
</head>

<body>
<%@ include file="/style/header.xml" %>
<%@ include file="/style/navigation.xml" %>
<div id='bodyColumn'>
    <p>
    <h1>Uninstallation</h1><br />
    If you are uninstalling the registry to not be used again and Resources have potentially been harvested to
    other external Registries in the IVOA community then advise to either
    	a.) Let another Registry handle/manage your authorityID to maintain those resources.<br />
    	b.) Or set the status of your resources to 'deleted' and wait a few days for those Resources to be harvested.
    	<br />    	
      Now to uninstall:
         <ul>
            <li>First go into your Tomcat Manager</li>
            <li>Find your registry context and click on "undeploy".  This should delete the war file and the directory.
               <i>*If it doesn't then shutdown tomcat and delete it war file and directory, then start tomcat back up.</i></li>
            <li>Your registry is uninstalled, but your eXist xml database area may still be around if it is outside your webapp.  If the xml database is not going to be used anymore then simply remove the main directory and all its files.</li>
         </ul>
      </p>
   </body>
</html>