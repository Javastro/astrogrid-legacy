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
      <h1>Upgrading</h1><br />
         <ul>
            <li>First go into your Tomcat Manager.</li>
            <li>Note your current properties by looking at the 'Fingerprint' page or checking out the 'Edit properties' page, a reset of your properties will be needed.</li>
            <li>If you have never moved the eXist database outside of your current web application then you MUST make a backup of the WEB-INF/data to put the data back.  Or see the 
            <a href='eXist_reference.jsp'>eXist_reference</a> to see how to move the data out before the undeploy.</li>
            <li>Find your registry context and click on "undeploy".  This should delete the war file and the directory.            </li>
            <li>Now deploy the new war file via tomcat manager or by simply dropping in the war file into the webapps directory (if using tomcat).</li>
            <li>Now set the enviornment entries like you had before.  </li>
         </ul>       
		<strong>
          By default registry uses an xml database known as eXist embedded into its webapp and data is stored in the webapp.  
          If you have not done so already it is RECOMMENDED to read the <a href='eXist_reference.jsp'>eXist Reference Guide</a> 
          on how to move the data out or at least know where the data is stored for coping back over if needed when your using the embedded eXist.  
          </strong>         
      </p>
   </body>
</html>