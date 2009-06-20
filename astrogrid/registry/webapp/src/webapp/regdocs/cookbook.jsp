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

<h1>Welcome</h1>
<p>
Below are the most common questions and answers to the registry.
</p>
<p>

<h3>What is the differences of Publishing, Full, (or even Special) type Registries?</h3>
<p>Publishing - is the smallest of all the types of registries and contains Resources managed by this Registry and only this registry.  Other FULL Registries will harvest the publishing registries.  It's primary
purpose is to publish its contents when the Full Registries ask for the data.  It does not have to have a Query interface but all Astrogrid Registries have both a Query and Harvest(Publishing) type interfaces.  A publishing registry
can also be part of a Full Registry but it is not recommended, we strongly encourage to not mix FULL and Publishing keep them separate.
Full - this is a registry that contains all known resources and harvests all publishing registries known to the RofR (Registry of Registries) maintained by the IVOA.  Note in the past we have found having a external eXist database
to be more of an advantage for Full registries.
Special - Rarely done but a registry that is setup to hold many resources but not ALL resources. Hence it usually harvests particular Registries of interest.</p>


<h3>What is an Authority ID and what is this error about Authority ID not managed?</h3>
<p>
All XML/Resources in a registry have a unique identifier which is a URI.  An identifier example is 'ivo://{authorityid}/{some unique string}' A 
registry may manage many authority id's and they will be owned by that particular registry.  When creating this registry initially a main authority id
was created, you may click on the menu to create more Authority id's.  If you received an error about Authority ID not managed then you tried to enter XML with
authority id you do not manage and you need to begin managing it.  Note an authority id can be owned by only one registry, the IVOA Registry of Registries validator
may detect that another registry already owns the authority id.  In the past an authority id tends to go to a domain like syntax with the institute or project such as
ucl.ac.uk or mssl.ucl.ac.uk
</p>


<h3>Other Departments or even Institutes want to Register Resources do they all need a publishing Registry?</h3>
<p>It can be nice but no it is not necessary and in fact it is much easier to set it up with a registry that is already
functioning.  You can click on the 'Manage New Authority' to add a new authority id then they will be allow to submit
resources under that authority into the registry.</p>

<h3>Is the web application the only way to upload new Resources?</h3>
No but is the most common.  You can use the registry client library or your own library to connect to the Registry Admin
endpoint to submit new/update Resources located at endpoint .../services/RegistryUpdatev1_0.
  You will need to change the property 'reg.custom.restrict.ipaddresses.updates' to a comma separated list of ip addresses you
  may use wildcards '*'.  This is needed so everybody is not allowed to update the Registry.  By default localhost(127.0.0.1) is allowed.

	
<h3>How do I move the internal Database?</h3>
<p>
	Most common install of the registry has an xml database named eXist installed inside the webapp and could
	potentially be dangerous when uninstalling or upgrading the registry may lose the data requiring a restore.  
	See <a href='regdocs/eXist_reference.jsp'>eXist_reference</a> on how to move the internal database out. 
</p>

<h3>How to do backups and restore?</h3>
<p>
There are two particular backups described below you may elect to do none, one, or both.  Note the database does have a built in recovery system though
uncertain how much should be relied on this recovery.
A.) Binary - this method is the easiest because you are only copying one directory.
The database eXist will store all the xml in a binary format inside a directory called 'data'  
<%
String xmldbURI = SimpleConfig.getSingleton().getString("xmldb.uri");
String existLoc = SimpleConfig.getSingleton().getString("reg.custom.contextFile", "");
if(existLoc != null && existLoc.trim().length() > 0) {
%>
which is located at <%=existLoc%>.
<%	
}else {
	if(xmldbURI.equals("xmldb:exist://")) {
%>
which is currently still located in your webapp '$CATALINA_HOME/webapps/<%=request.getContextPath()%>/WEB-INF/' you may wish to read
the above information about moving your database outside the webapp.
<%
	}else {
		%>
		which is connected to an external eXist that you have setup, possibly check $EXIST_HOME/webapp/WEB-INF.
		<%
	}
}
%>
Since all the data is located in this directory you can backup and restore this directory if needed.
<br /><br />
B.) XML - A script is used to make backups and restores of your database xml content.  This has a small advantage if you are moving to an external database that
is a different version or if the above A. somehow still got corrupted.  At the same location as your data directory you will see a backupandrestore.{sh/bat} at the top of this
file is directions and options on how to run the script.
</p>

<h3>Can I use an external database?</h3>
<p>
Yes you can use an external eXist database.  And does have an advantage because it can be on a different server, clustered, up to date version with bug fixes, along with other features.  To do this
go to <a href="http://exist.sourceforge.net">eXist</a> and learn how to install the database.  The only property that must change in the registry is to change
the xmldb.uri to 'xmldb:exist://{server}:{port}/exist/xmlrpc'  If your using tomcat you can change the proerty <a href="admin/environment-edit.jsp">here</a>.
</p>


<h3>Where can I find Schema for validation?</h3>
<p>
There are two places to find XML schemas that the registry can use:
	a.) http://software.astrogrid.org/schema/ --- IVOA official schemas and any extensions used at Astrogrid.
	b.) http://ivoa.net/xml/index.html --- IVOA official schemas. 
</p>

<h3>Can I extend the registry and how with my own XML?</h3>
<p>
This question assumes you have knowledge of schema and XML.  There 4 steps:
	a.) Create your extension schema.
	b.) Place the schema on a public location so schemaLocation validation can occur.
	c.) Now create your XML instance document and try to upload it to the registry. YOU MUST ENSURE you define the 
	schemaLocation attribute (and namespaces) on each Resource element (or one of it's sub elements). Hence do not define it at the 
top on 'VOResources' element those namespaces are not copied into the individual 'Resource' elements.
	d.) For the Search and keywordSearch queries you must edit the WEB-INF/classes/astrogrid.properties and add your declaration of namespace at the property 'reg.custom.declareNS.1.0'.
</p>

<h3>What is VOSI and the harvesting of VOSI?</h3>
VOSI is a standard interface defined by the IVOA.  The registry much like harvesting other registries can contact another service that has a VOSI interface
and ask the service for any new information it may have to go into the registry.  A good example is a component such as Data Set Access 'DSA' or VOSpace may
have a new capability with the Registry you can go to Harvest VOSI link and click on that service to get any new information.  Currently though it only
harvests services that are managed by this registry and it does NOT automatically harvest the services.
	

<h3>What is this link low level Back Door?</h3>
Editing the XML in the registry can be done by html forms, upload raw xml, or pasting straight raw xml into a text box but all these updates must be on
resources that you manage hence the authority id is managed by this registry.  BUT there are times when you really need to make an
edit on another particular resource this is mainly in FULL registries.  This link allows you to bypass extra checks and submit edits to resources
you do not manage; this web page though requires you to submit the raw xml (usually by copying the xml from the browser and pasting).  NOTE that if automatic
harvests are on then there is a potential this resource will be overwritten when a change happens to the other harvestee registry.

<h3>Can I harvest just certain Registries?</h3>
This is handy if you want to be what is known as a Special registry hence you cater to certain areas of Resources and do not have
all resources in your registry.
The easiest thing to do is read instructions on the 'Get/Find New Registries' link in the menu.  Note one small trick is to get
everything for Registry of Registries (RofR) then using Remove Resource to remove the ones you do not wish to have.  Then you can go to
the Harvest page to do harvesting or let the automatic harvester pick up entries.

<h3>How do I know if a harvest succeeded?</h3>
Check your menus you will see a 'Harvest Status' link to check the Registries and see information of when harvests took
place and any errors or validation problems.  NOTE: the information is probably at the very bottom of the page for the last
harvest.  In the Admin Section is another similiar 'Harvest' page but this is not public and allows you to carry out manual harvests.
<br />
	

</body>
</html>