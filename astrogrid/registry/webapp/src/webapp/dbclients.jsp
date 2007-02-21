<%@ page import="org.astrogrid.config.SimpleConfig,
 	  		     org.astrogrid.registry.server.http.servlets.helper.JSPHelper,
                 org.w3c.dom.NodeList,
                 org.w3c.dom.Element,
                 org.w3c.dom.Document,   
                 org.astrogrid.util.DomHelper,
                 org.astrogrid.registry.server.http.servlets.Log4jInit,
                 org.astrogrid.xmldb.client.XMLDBManager,
                 org.astrogrid.registry.common.RegistryDOMHelper,                 
             	  org.astrogrid.registry.server.query.*"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>Connect to Database</title>
<style type="text/css" media="all">
	<%@ include file="/style/astrogrid.css" %>          
</style>
</title>
</head>

<body>
<%@ include file="/style/header.xml" %>
<%@ include file="/style/navigation.xml" %>

<div id='bodyColumn'>

<h1>Client and WebDav</h1>
<p>
If you are using the recommended xml database eXist (which also is bundled up with the registry).
Then you may like to know you can get right into the database.<br />
eXist provides you with a WebStartable client allowing you to login in via a GUI and perform
numerous amounts of tasks such as changing usernames, backups, reindex, xquery, add/remove everything 
possible to manipulate the database.<br />

Also it supports WebDav which allows you to connect various Applications like XMLSpy or Oxygen as well as
your typical File Manager normally support WebDav meaning you can view the whole database just like any other
network drive on your system.<br />
Below is the information to get you connected as well as some hints.<br />
</p>
<p>
<h1>Requirements</h1><br />
<ul>
<li>
If you are running in the default mode of eXist database embedded internally with your webapp. 
Because you would not wish to make your database public, you must make a change to a JNDI environment entry (property).  
Edit JNDI property called "reg.custom.restrict.ipaddresses" allowing you 
to restrict who has access to the WebDav and client Gui.  
Just make a comma seperated list of ip addresses you allow.  You may even do expressions if desired such as 128.9.*.<br />
As noted in the Configure docs this is typically in your WEB-INF/web.xml unless you have a {context}.xml file in your conf/Catalina/localhost and
requires a reload of the webapp via tomcat manager or restart of tomcat.
</li>
<li>
eXist database is not fully initialized until a query happens this can be done by simply going to almost any jsp page such as the home (index.jsp) page of your registry or
when any component calls the registry via a web service call.  Hence if you connect via WebDav or client GUI and says the 'eXist' db is not initialized then please go to the
home page of your registry which will initialize it.  This usually happens when there is a restart of your servlet container.
</li>
<li>
Many people like the use of XML editors such as Oxygen or XMLSpy to validate and edit the XML.  But it is easy
just to edit the XML directly in the Database with the Client GUI or general Text editor via WebDav.
<b><font color'red'>Please be sure to take caution and validate the XML.</font></b>  So be sure to
set schemaLocations in your XML Editor so you can validate or to take the XML and by using the simple "Enter Resource" jsp 
pages here at the registry put in the XML so it is validated and certain to go into the registry correctly.
</li>
</ul>
<hr />
<br /><br />
<h1>The Client</h1>
You can launch a web start client here:<br />
<a href="http://demo.exist-db.org/webstart/exist.jnlp"><img src="img/webstart.small.jpg"></img></a>
<br />
By default the username is "admin" password is empty.  <br />
Type: Remote<br />
URL: xmldb:exist://{host}:{port}/{context}/xmlrpc
<br />Sample url for embedded:<br />
URL: xmldb:exist://galahad.star.le.ac.uk:8080/astrogrid-registry/xmlrpc <br /> or most likely this: <br />
URL: xmldb:exist://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%>/xmlrpc
<br />Sample url for external:<br />
URL: xmldb:exist://mssl.ucl.ac.uk:6080/xmlrpc<br />

external eXist if downloaded is by default on port 8080, but if you download the external zip file astrogrid gives you
which has a few of the settings already set then default is 6080 port.<br />
This is all you need to know to get into the database and try reindexing, xqueries, backups, setting up users and other.  Do read
the next 2 important notes:
<br />Some important things:<br />
<ul>
<li>
Be certain if you change usernames and passwords around then you will need to change
the registry JNDI properties to reflect your changes in the database.</li>
<li>
Do not rename xml files in the astrogrid collections, the names are based on the identifier and
used for getting 1 single Resource directly (on the GetResource interface) instead of querying for a Resource.</li>
</ul>
<br />
<a href="http://exist.sourceforge.net/documentation.html">Other Documentation</a>
<hr />
</p>
<p>
<h1>WebDAV</h1><br />
WebDav can be used with your database to map your database like a regular network drive, where by you
can edit, delete, add various content.  Several popular apps that may interest you to connect via
WebDav are XMLSpy and Oxygen allowing you to get a more easy interface to the xml documents.<br />
All of this can be setup and is explained on the following link:
<a href="http://exist.sourceforge.net/webdav.html">eXist WebDav Doc</a>
<br />
*Just one small note.  Many XML Registry Resources will not have the schemaLocation stored in the database XML.
Which means for validation and to make an easy to use Interface with Oxygen or XMLSpy you will
need to set the schemalocation yourself.  Down below are a list of various places to obtain
the schemaLocation.<br />
<font color='red'>
0.10 Only - Editing 0.10 via WebDav <br />
If you use WebDav for editing 0.10 data because of a problem dealing with not able to reach the Resource element in the 0.1 
Registry Interface official ivoa schema, 2 unofficial schemas were created.<br />
Located <a hefef="unoffical_schema">here</a>.  The RegistryInterfaceUnofficial.xsd is equivelant to the official ivoa schema, but the Resource element can now be reached and the RegistryDBStore simply references this unofficial RegistryInterface.xsd.
</font>
<hr /> 

</p>
<p>
<br /><br />
<i>Sample Schema Location Places:</i><br />
The schemaLocations below can be found at:<br />
http://software.astrogrid.org <br />
ex: http://software.astrogrid.org/schema/vo-resource-types/VOResource/v1.0/VOResource.xsd
<br />
or <br />
your local registry at: <br />
http://{yourhost}:{yourport}/{registry context}  <br />
ex: http://<%=request.getServerName()%>:<%=request.getServerPort()%><%=request.getContextPath()%>/schema/vo-resource-types/VOResource/v1.0/VOResource.xsd
<br />--- <br />
/schema/vo-resource-types/VOResource/v1.0/VOResource.xsd
1.0 schemas.<br />
/schema/vo-resource-types/VOResource/v1.0/VOResource.xsd<br />
/schema/vo-resource-types/VORegistry/v1.0/VORegistry.xsd<br />
/schema/vo-resource-types/VODataService/v1.0/VODataService.xsd<br />
/schema/vo-resource-types/ConeSearch/v1.0/ConeSearch.xsd<br />
/schema/vo-resource-types/SIA/v1.0/SIA.xsd<br />
/schema/registry/RegistryUpdate/v1.0/RegistryDBStore.xsd<br />
.<br />
0.10 schemas.<br />
/schema/vo-resource-types/VOResource/v0.10/VOResource.xsd<br />
/schema/vo-resource-types/VORegistry/v0.3/VORegistry.xsd<br />
/schema/vo-resource-types/VODataService/v0.5/VODataService.xsd<br />
/schema/vo-resource-types/ConeSearch/v0.3/ConeSearch.xsd<br />
/schema/vo-resource-types/SIA/v0.7/SIA.xsd<br />
/schema/vo-resource-types/TabularDB/v0.3/TabularDB.xsd<br />
/schema/vo-resource-types/OpenSkyNode/v0.1/OpenSkyNode.xsd<br />
/schema/vo-resource-types/CEAService/v0.2/CEAService.xsd<br />
<br />
At the time of this writing these schemas were not published.<br />
/schema/vo-resource-types/OpenSkyNode/v1.0/OpenSkyNode.xsd<br />
/schema/vo-resource-types/CEAService/v1.0/CEAService.xsd<br />

</p>
</body>
</html>