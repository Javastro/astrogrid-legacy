<%@ page import="org.astrogrid.config.SimpleConfig,
                 org.w3c.dom.NodeList,
                 org.w3c.dom.Element,
                 org.w3c.dom.Document,   
                 org.astrogrid.util.DomHelper,
                 org.astrogrid.registry.server.RegistryServerHelper,                 
             org.astrogrid.registry.server.query.*"
   isThreadSafe="false"
   session="false"
%>

<html>
<head>
<title>AstroGrid Registry Configuration</title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</title>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>Welcome</h1>
<p>
Below you will find various configuration and installation instructions for your registry.
</p>
<p>			
    	   	Here we list the steps involved to deploy the registry (and eXist) onto a servlet container.  An 
    	   	assumption is made that you know basic knowledge of servlet containers such as tomcat. <br />
    	    Now for the Standard Installation:
    	   	Steps involved:
    	   	<ul>
    			<li>1.) Download the latest copy of astrogrid-registry from <a href="http://www.astrogrid.org/maven/org.astrogrid/wars/">Astrogrid repository</a>
    	            You may grab the latest copy as: <a href="http://www.astrogrid.org/maven/org.astrogrid/wars/astrogrid-registry-SNAPSHOT.war">Latest Registry</a></li>
    	        <li>2.) By default the registry uses the eXist XML database internally as default.  Below are other options on using eXist or other XMLDB databases.
    	        <li>3.) You may rename the registry war file to anything you like.</li>
    	        <li>4.) Deploy the registry war file into your servlet container normally by dropping them into the webapps directory or through
    	            some type of manager interface.</li>
    	        <li>5.) Go to your contexts environment entries in the Administration GUI of your servlet container (hence what was read from the web.xml).  
    	                All that really needs changing is the reg.amend.authorityid to your main AuthorityID used on this registry.  But you may wish to read the Properties section below to customize your installation.</li>
    	        <li>6.) In general the installation of the registry is complete, but read below for the setup.</li>
    	   	</ul>
    	    Steps involved for Setup of Registry:
    	    <ul>
    			<li>1.) Go to the registry jsp page of "{context}/admin/index.jsp" and do the first registration process to register
    	        this registry.
    			</li>
		      <li>
		        If you are setting up a local Registry and do not need a Full Registry and DO NOT WANT TO BE HARVESTED BY OTHER REGISTRIES then stop here, all your
			        entries are complete and other client apps may submit Resources to this registry as long as it is a AuthorityID
			        managed by this Registry.
    			</li>
	         <li>Now it is time to register your new Registry to the rest of the world.</li>
     			<li>2.) Do the Second link on "/admin/index.jsp" for registering your registry to a full registry.</li>
        		<li>Now if your not planning on being a Full Registry then stop here.</li>
        		<li>3.)Finally do the Third link on "/admin/index.jsp" to become a full registry by grabbing all the
    	               registry types of a full registry.
    			</li>
        		<li>4.) Now the next time your harvest cycle happens it will begin harvesting these Registries, the first time
        			it will harvest with no Date, and then after that it will harvest by date.  Optionally there is a "/admin/harvestResource.jsp" to perform
    	            harvests on individual Registries; or optionally kick-off a full harvest at "/RegistryHarvest" servlet.
    			</li>
        		<li>You're done.</li>
    	    </ul>
			<br />
			</p>
			<p>
			
			Now more on the XML Databases:<br />
			The registry uses the XMLDB:API to connect and query xml databases.  By default it uses an internal (in-process) xml databases known as eXist. eXist puts all the 
			xml stored into the "{context}/WEB-INF/data" directory by default and reads this information from the "{context}/WEB-INF/exist.xml.  
			Below are other setup abilities that you might wish to do and has certain advantages.
			<br />
			<strong>Using eXist internally but different  configuration and data storage location</strong><br />
			This has a big advantage in that your exist.xml and data directory are outside your deployed webapp; meaning you don't have to copy an older data directory into your webapp on a update of the registry.
			Steps:
			<ul>
			  <li> Copy your current {context}/WEB-INF/exist.xml and {context}/WEB-INF/data (directory) into some other location on your computer.</li>
			  <li> Now in your servlet containers GUI for Enviornment entries of the registry change "reg.custom.exist.configuration" to point to your exist.xml.</li>
			  <li> That is all be sure you commit your changes on your servlet containers GUI. You might want to read the other property settings below.</li>
			</ul>
			<br />
			</p>
			<p>
			<strong>Using an external eXist server instance.</strong><br />
			This has a few advantages:
			<ul>
				<li>Again your storage mechanism is outside your deployed registry.</li>
				<li>Restarts of your servlet container has no effect on the database because they are seperate.</li>
				<li>You can use the eXist xml database for your other projects.</li>
				<li>The server instance has other GUI and client shell tools for going into the database for manipulation and other tasks.</li>
			</ul>
			Now for the steps:
			<ul>
				<li>Go to your Enviornment entries in your admin gui for your servlet container.</li>
				<li>Change the "xmldb.uri" to something like xmldb:exist://host:port/exist/xmlrpc ex: xmldb:exist://localhost:9080/exist/xmlrpc</li>
	 		  	<li>That is all be sure you commit your changes on your servlet containers GUI. You might want to read the other property settings below. Espcially if you setup up users and passwords.</li>
			</ul>
			<br /><i>There is also a war version of eXist that you might want to use, but I have seen the war version not scaling as well with a servlet container with a lot of webapps.</i>
			<br /><i>You are currently expected to setup the eXist external instance your self, you may get it from <a href='http://exist.sourceforge.net'>eXist</a> or if you want a preconfigured port such as 9080 you can get it from Astrogrid at
			<a href='http://www.astrogrid.org/maven/exist'>eXist at Astrogrid</a> Go to the zips directory or alterntively the wars directory.</i>
			<br />
			</p>
			<p>
			<strong>Using another XMLDB:API database.</strong> <br />
			This is untested at the moment and will add more here later.  But presumbly you can point to another XMLDB, but would require you to change the
			"xmldb.uri" property.  And currently requires "/db" to be the main root collection after your "xmldb.uri"
<br /><br />			
		<strong>
		Below are a list of all the properties of the registry, most are custom and require no changing, but you might want to have a read through in case of another version of the registry schema or small tweaks you wisht to perform.:
		</strong>
		<ul>
			<li>reg.amend.authorityid -- 
		The Main Authority ID for this Registry (Authority ID used for the Registry type)</li>
			<li>xmldb.uri -- 		
			The XMLDB server location url. By default it is set to use the internal eXist db.  
			You may set it to xmldb:exist://host:port/exist/xmlrpc for external eXist db. e.g. xmldb:exist://localhost:8080/exist/xmlrpc.  
			In general you can switch to another db but this is untested read configuration page from your context on that.</li>
			<li>xmldb.driver -- 
		The XMLDB driver to be the registered xml database.</li>
			<li>xmldb.query.service -- 
		What type of query service, XQueryService or XPathQueryService.</li>
			<li>xmldb.admin.user -- 
		The user used to create Resources or collections, and shutdown the internal eXist database if it is internally being ran.</li>
			<li>xmldb.admin.password -- 
		The admin password used to create Resources or collections, and shutdown the internal eXist database if it is internally being ran.</li>
			<li>xmldb.query.user -- 
		The user used to create Resources or collections, and shutdown the internal eXist database if it is internally being ran.</li>
			<li>xmldb.query.password -- 
		The query password used to create Resources or collections, and shutdown the internal eXist database if it is internally being ran.</li>
			<li>reg.amend.defaultversion -- 
		The default registry version used, May be overriden on updates and queries based on vr namespace.</li>
			<li>reg.amend.validate -- 
		Have validation on or off.</li>
			<li>reg.amend.quiton.invalid -- 
		Quit on validation.</li>
			<li>reg.amend.returncount -- 
		The maximum number of results to return from queries.</li>
			<li>reg.amend.harvest -- 
		Does this registry need to do harvesting.</li>
			<li>reg.oaipublish.0.9 -- 
		The url to the oai publishing url for 0.9, default is set to astrogrid servlet, but is not required to use.</li>
			<li>reg.amend.oaipublish.0.10 -- 
		The url to the oai publishing url for 0.10, default is set to astrogrid servlet, but is not required to use.</li>
			<li>reg.custom.exist.configuration -- 
		Point to another exist configuration file on your disk hence a different data directory as well.  (only for internal db use).  This might be handly if you want to use the internal database, but not have the configuration file and data directory of eXist db with the war file so undeploying/updating wars will not lose data. Also use if servlet container does not unpack war file.</li>
			<li>reg.custom.harvest.interval_hours -- 
		Number of hours between harvest (if harvest is enabled)</li>
			<li>reg.amend.identify.repositoryName -- 
		Used for the Identify verb in OAI.  The repository Name.</li>
			<li>reg.amend.identify.adminEmail -- 
		Used for the Identify verb in OAI.  The administration Email of this OAI repository.</li>
			<li>reg.custom.identify.earliestDatestamp -- 
		Used for the Identify verb in OAI.  The earliest date stamp for records/metadata being published.</li>
			<li>reg.custom.updatestylesheet.0.9 -- 
		(Advanced) Specify stylesheets to be used on updates for particular versions.</li>
			<li>reg.custom.harveststylesheet.0.9 -- 
		(Advanced) Specify stylesheets to be used on harvest updates for particular versions.</li>
			<li>reg.custom.harveststylesheet.0.10 -- 
		(Advanced) Specify stylesheets to be used on harvest updates for particular versions.</li>
			<li>reg.custom.keywordxpaths.0.9 -- 
		(Advanced) Specify xpath for keyword search based on various versions in the registry.</li>
			<li>reg.custom.keywordxpaths.0.10 -- 
		(Advanced) Specify xpath for keyword search based on various versions in the registry.</li>
			<li>reg.custom.identifier.hasauthorityid.0.9 -- 
		(Advanced) Does the Identifier split up with an AuthorityID element based on version in registry.</li>
			<li>reg.custom.rootNode.default -- 
		(Advanced) The default root node for elements in the Registry. Can override on various versions in the registry.</li>
			<li>reg.custom.rootNode.0.10 -- 
		(Advanced) The default root node for elements in the Registry. Can override on various versions in the registry.</li>
			<li>reg.custom.rootNode.0.9 -- 
		(Advanced) The default root node for elements in the Registry. Can override on various versions in the registry.</li>
			<li>reg.custom.declareNS.0.9 -- 
		(Advanced) Declare the namespaces used on the registry, based on registry versions.</li>
			<li>reg.custom.declareNS.0.10 --
		(Advanced) Declare the namespaces used on the registry, based on registry versions.</li>
	</ul>

</p>

</body>
</html>