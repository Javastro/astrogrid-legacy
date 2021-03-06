<%@ page import="org.astrogrid.config.SimpleConfig"
   isThreadSafe="false"
   session="false"
%>
<!DOCTYPE HTML  PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Registry FAQ</title>
<meta http-equiv="Content-type" content="text/xhtml;charset=iso-8859-1">
<style type="text/css" media="all">
    <%@ include file="/style/astrogrid.css" %>
</style>
<%@ include file="/style/link_options.xml" %>
</head>
<body>
<%@ include file="/style/header.xml" %>
<%@ include file="/style/navigation.xml" %>
<div id='bodyColumn'>

<h1>Installation</h1>
<i>Definition 'Context Name' - is your webapp name (hence usually the
unpacked directory name in the webapps).  This will be used
a lot in various registry documentation usually with curly brackets.</i>
The installations below have mainly been tested on tomcat versions
5.0.19-5.0.28 java1.4, 5.5.17-5.5.19 with java.1.5. But the "Manual
Installation" could be carried out on other servlet/j2ee containers
and would welcome success or failure stories relating to other containers.
For the purpose of most instructions here the guide will refer to tomcat.
<br>
<br>
<i><b>Note</b> that tomcat 5.0 users using java1.5 has been done and
requires a system property to be set:
javax.xml.transform.TransformerFactory=com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl
</i>
<br>
<br>
<strong>Be sure to read the bottom section if you know your deploying
this in a servlet container that will "NOT" unpack the war file.</strong>
Unpacking is usually a term of unzipping/extracting the war and is
something typical that tomcat does when deploying a war file.
Download Location of war: http://developer.astrogrid.org

<h2>Installation Steps</h2>
Most of the work is in the configure page, for installation:
<ul>
<li>Take the war file and rename it to something you prefer e.g.
registry.war (be sure to keep the .war ending).
Typical names are "astrogrid-registry.war" or "registry.war"</li>
<li>Go to your Tomcat manager e.g. http://localhost:8080/manager/html
(this may ask for a username and password ask your admin or check
{tomcat}/conf/tomcat-users.xml for a user with the role of manager)</li>
<li>If you do not have access to Tomcat manager because it is
restricted on your server you may also copy the war file into the
{tomcat}/webapps directory to install.</li>
<li>Tomcat by default should unpack the war file into a directory in
that webapps directory. Directory Name should be the same as the war
file minus the ".war" part.</li>			
<li><strong>You should be done (for the installation part now for Configure)
you should be able to access the url: http://{server}:{port}/{context}
(optionally you can also check the webapps for a directory to
verify).</strong></li>
<li>The Configure will be on the menu at the left hand side of your
Registry web application.</li>
<li>If it does not unpack it then you may have it turned off in your
tomcat or your using a different servlet container that does not
unpack the war files "see bottom section if you know it does not
unpack wars".</li>
</ul>

<h2>Handling Packed wars</h2>
This section is for servlet containers that do not unpack the war
file. You may be able go to your Registry but it cannot make a
connection to the embedded database.  You must download an empty
eXist that can be unzipped, and like the above directions go to your
Configure page in the Registry to set your property to this new
location of the database. SEE THE 3RD POINT BELOW to set a property
to your database:
<ul>
<li>Download an empty eXist db zip located at:
<a href='http://software.astrogrid.org/eXist/empty_exist_for_internal_registry.zip'>Registry eXist Zip</a></li>
<li>Now unzip it in some directory where you want your database.</li>
<li>Now a property must be set "reg.custom.exist.configuration"
property and point to this directory plus conf.xml
ex: /exist/db/conf.xml.</li>
<li>Information on changing properties is in the Configure section.</li>
</ul>
</div>
<%@ include file="/style/footer.xml" %>
</body>
</html>
