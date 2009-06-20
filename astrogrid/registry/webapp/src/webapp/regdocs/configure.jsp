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
    <h1>Configuration</h1>
    
    <br />
	    <b>BackGround:</b> For webapps properties are stored in the WEB-INF/web.xml but this xml contains a lot more than just 
	    properties and can be a bit cumbersome to edit by hand.  These properties are known as JNDI environment entries.  
	    Typically servlet containers will have a special GUI for changing these properties and this is what is advised for you to use.
	    Tomcat GUI by default is no longer installed with tomcat 5.5 and the GUI is not known to be reliable.<br />
	    Tomcat will also allow 'you' to place a special xml properties file in the {tomcat}/conf/Catalina/localhost named {context}.xml.
	    <br />Click on the below link to take you to the property editor page which will write the properties in the conf/Catalina/localhost.
	    Click on this link (advise open in a new window) <a href="../admin/environment-main.jsp">here</a> to edit JNDI properties.	    

<hr />
</p>
<p>
<b>Changing Properties</b>
         First we need to change some properties in the environment entries. Click on the above link if you haven't already to change properties.<br />
         <ul>
            <li>reg.amend.authorityid - Your main authority id for this registry, normally an institution domain name ex: mssl.ucl.ac.uk</li>
            <li>reg.amend.harvest - *Only if you want registry to automatically harvest then set to "true"</li>
            <li>reg.amend.identify.adminEmail - to your contact e-mail.</li>
            <li>reg.amend.oaipublish.1.0 - *Only if your {context name} is not "astrogrid-registry" then change it or on a different port.</li>
            <li>reg.custom.restrict.ipaddresses.updates - Restrict to certain ip address for Update web service current setting is localhost(127.0.0.1). Must do this for external clients to update via the Update web service call.</li>
            <li>reg.custom.restrict.ipaddresses - Restrict to certain ip addresses to webdav and xmlrpc servlets current setting is localhost(127.0.0.1). Must do for WebStart Gui or connecting an app via WebDav.</li>
            <li>You can verify properties are changed with <a href="../admin/fingerprint.jsp">Fingerprint</a>.  Tomcat 5.5 seems to not catch changes automatically and requires a restart of tomcat.</li>
            <li>That is all the primary properties to be changed for the registry.</li>
            <li>reg.custom.exist.configuration - Optional though recommended is moving your embedded database ouside the web application which requires setting this property.  See <a href='eXist_reference.jsp'>eXist_Reference</a>.
         </ul>
<hr />
</p>
<p>
<b>Setting up for data</b><br />

            <strong>DO NOT NEED TO DO WHEN UPGRADING</strong>
            <br />
            <strong>Advise to click on the <a href="../admin/fingerprint.jsp">Fingerprint</a> page to confirm properties are changed.   (might want to open in a new tab/window so you can still read docs).</strong>
          <ul>
            <li> 1.) Go to the 'Admin Home' (click <a href="../admin/index.jsp">here</a> advise open up in new tab/window) 
             and do the first link for adding Indexes.
              <br />*If your asked for a username and password then it is currently the same as your tomcat manager.             
            </li>
            <li>2.) Now do the second link for "Self Registration" to register
              this registry. This will in setup your first entry into your registry which is your Registry Type describing about this registry.
            </li>
            <li>Look at the XML produced in the Self Registration this can be done by clicking the 'here' link on your Registry home page.  This
            Registration shows all the endpoints needed for querying the registry by other components along with the OAI publishing url.</li>
            <li>
              If you are setting up a local publishing Registry and DO NOT WANT TO BE HARVESTED BY OTHER REGISTRIES then stop here, all your
                 entries are complete and other client apps may submit Resources to this registry as long as it is a AuthorityID
                 managed by this Registry. <i>Notice that Registry Type you entered in step one had {managedAuthority} element tags this is what is read to 
determine what authority ids you are allowed to use.</i>
            </li>
            <li><font color='blue'>This part is New. Registry of Registries is a new type of Registry for 1.0 spec and 1.0 VOResources.  It is a Registry
            thats sole purposes is about handling other Registry types so other Registries can discover Registries.  It is hosted by
            IVOA, read the admin/index.jsp about e-mailing the registry group the link to your Registry.</font></li>            
            <li>Now it is time to register your new Registry to the rest of the world.</li>
            <li>3.)Do the second main link on "/admin/index.jsp" "Register with other Registries" for registering your installed registry to a known full registry.  </li>
            <li>Now if your not planning on being a Full Registry then stop here.</li>
            <li>4.)Finally do the third main link on "/admin/index.jsp" to become a full registry by grabbing all the
                     registry types of a full registry.
            </li>
            <li>5.) Now the next time your harvest cycle happens it will begin harvesting these Registries, the first time
               it will harvest with no Date, and then after that it will harvest by date.  Optionally there is a "/admin/harvestResource.jsp" to perform
                  harvests on individual Registries.
            </li>
            <li>You're done.</li>
          </ul>
         </p>
   </body>
</html>