Portal README Document
----------------------------------------------

Description:
Portal is the viewer for the users to interface with the various Astrogrid components.   This is done by using Apache Cocoon as our main tool for displaying to the browser.  Every portal is tied to a community name or better called a community branch.  The community branch is server location that contains the various users, groups, resources and permission/authorization policies.  The portal may or may not live at the same location as the Community.

Currently in iteration 3 the various pages consist of:
Login
Registry
Data Query
Administration
Credentials
Myspace
-------------------------------------
Configuration:
After building the portal (Please see the "Build the Portal" section below).  Go to your portals webserver cocoon/WEB-INF/web.xml file.  Located normally at the bottom will be an <env-entry> for ther "org.astrogrid.community.config" which points to a configuration file for reading in all the various properties needed by the portal.  The configuration file can live anywhere on the system or even a url.  When the portal is started it will read the configuration file using jConfig.

	Inside the Config file:
		REQUIRED:
		community.name = This is your local community name.  Which is a domain name of your community branch location. ex: "mssl.ucl.ac.uk".
                portal.security = "on"/"off" In a production environment this should always be set to "on" this tells the portal to redirect to a secure ssl port during password transactions.
		portal.secure.port = This is the port number of your secure ssl port.  Normally for tomcat it is "8443"
		portal.nonsecure.prot = This is the port number of your non-secure (regular) port number.  Normally for tomcat it is "8080"
		OPTIONAL:
		policy.manager.url = If you are on a system that is not using axis or using special port numbers for you webservice.  Then define the Policy Manager Web service url here.  The default is http://community.name:8080/axis/services/PolicyManager.  Please define it to the webservice and port number that is holding your Policy Manager webservice.
		policy.service.url = see the policy.manager.url it is the same in dealing with the policy.service.url except the web service name is PolicyService
		astrogrid.admin = This is the Administrator of your portal site. (The community branch may have a different admin)
		astrogrid.adminEmail = This is the Administrator's e-mail of your portal site. (The community branch may have a different admin's e-mail)
------------------------------------

Description of Various pages:
Login - This is the page for logging into the Astrogrid portal.  Consist of a Username and password only.  Will call the community branch's Authententication service to validate the login.  If your new to the portal you may go to the Administration page dealing with inserting an account for registration.

Credentials - This page holds the credentials of why a user can perform a particular task.  A user can view his/her groups they are a member of and select that group as the current credentials for performing a task.  If desired the user may choose to get groups he is assoicated to at another trusted community.

Administration - Is a page mainly for the Administrators.  Regular or Guest users may View groups or change their password.  But this is mainly used for Administrators setting up various permissions, groups, and resources for their Community.  An Administrator had to assign you as part of the "Admin" group.
-------------------------------------------------
How to Build the Portal

This is assuming you are running ant and are in the portalB directory.
For building from scratch:
ant axis.REBUILD  //this builds a new axis install. Not really used except for referencing jar files.
ant junit.REBUILD //used only for junit tasks.  And is needed for reference to jar files.
ant cocoon.REBUILD //this builds a new cocoon install.
ant config.BUILD  //still in the works.  This builds a config file and puts the environment entry into the cocoons web.xml file for pointing to the config file.
ant portal.REBUILD  //this builds a new portal install.
ant cocoon.release  //This build a new cocoon war file.

The above ant tasks are for building the portal from scratch on your system.  You may define certain entries into your "ant.properties" for bypassing.
"ant.properties" file lives in your home directory (normally the location where you log in at on unix platform or where your DOS prompt starts at for Windows platform)
You may define a axis.home directory for bypass the axis.REBUILD ant task.  You may define a junit.home for bypassing the junit tasks.

-------------------------------------------------
How to Setup:
After you run the cocoon.release ant task a cocoon.war file is placed in your portalB/build/release/cocoon directory.  Take this cocoon.war file and copy it to your web server's webapps directory such as tomcat/webapps.

Restart your webserver.  This will install the cocoon directory and all the neeeded components for the portal in the webapps directory.  Please verify after startup that in the cocoon/WEB-INF/web.xml file that the "org.astrogrid.community.config" environment entry is their and is pointing to a config file that is in the right location.



web.xml:
Until the config.BUILD ant task is working appropriatly please put this in your webapps/cocoon/WEB-INF/web.xml file at the bottom then restart your server.
example of entry normally at the bottom right before </web-app>
<env-entry>
<!--
JNDI property for config file right now this has community.config probably will be changed to portal.config
The evn-entry-value may be point to anywhere on your system or even a url.
-->
<env-entry-name>org.astrogrid.community.config</env-entry-name>
<env-entry-value>C:\astro_community_client.xml</env-entry-value>
<env-entry-type>java.lang.String</env-entry-type>
</env-entry>  

---------------------------------------------------
How to check if a user is authorized to perform a task (with portal).  I don't go into detail on the Administration page because it is very basic user friendly.  Keep in mind that all the session references below are formed on the login of a user.

This is for your local community system.

1.) First you must be an Administrator meaning belong to the Administrator group.
2.) Once logged in go to the Administration page.
3.) Insert/Remove various resources.
4.) If you are assigning a policy/permission to an individual user or a group that already exists then go to the insert permission admin page. else go to "Insert Groups" page and insert the groups.  Then go to "Insert Member" page and assign users to that group.
5.) At the Insert permission select your groups/account, resource, and the action/policy string you are wanting to authorize. ex: "Guest" group has "read" action for "myspace" resource.

On the portal page where you want to check the authorization.
1.) First import in your Custom Action object preferbly (or xsp if you want to)
import org.astrogrid.community.delegate.policy.PolicyServiceDelegate;
import org.astrogrid.community.delegate.policy.PolicyPermission;

Then where you want to check if the user has permissions do.
PolicyserviceDelegate ps = new PolicyServiceDelegate();
boolean authorized = ps.checkPermission(session.getAttribute("community_account"),session.getAttribute("credential"),resource,action);
if(!authorized) {
  PolicyPermission pp = ps.getPolicyPermission();
  //now you their is a pp.getReason if you wish to show an error message.
}
----------------------------------------------------------
How to check if a user is authorized to perform a task (for server side system, external or local to the community)

First grap the policy-client-1.0.jar file from the community/dist directory in cvs.  And have it as part of your classpath in your build and part of your WEB-INF/lib directory or where
ever you want to part the jar file as lons as your server side code knows about it.

Now back to the portal page.  It is assumed the portal will pass the necessary information in a xml type message. 
This is done by on your portal page before you call your server side system do this:
import org.astrogrid.community.common.util.CommunityMessage;
String xmlsnippet = CommunityMessage.getMessage(session.getAttribute("community_token"),session.getAttribute("community_account"),session.getAttribute("credential");

//Now the portal side needs to pass the xml snippet to the server side system.  Probably concatenated on the end of your xml message to your system.

The server side system now does this after it receives the xmlsnippet of code from the portal:
import org.astrogrid.community.common.util.CommunityMessage;
import org.astrogrid.community.delegate.policy.PolicyServiceDelegate;
import org.astrogrid.community.delegate.policy.PolicyPermission;

String account = CommunityMessage.getAccount(community_xmlsnippet_received);
String credentail = CommunityMessage.getGroup(community_xmlsnippet_received);

PolicyserviceDelegate ps = new PolicyServiceDelegate();
boolean authorized = ps.checkPermission(session.getAttribute("community_account"),session.getAttribute("credential"),resource,action);
if(!authorized) {
  PolicyPermission pp = ps.getPolicyPermission();
  //now you their is a pp.getReason() if you wish to show an error message.
}
-------------------------------------------------------
How to Integrate pages into Astrogrid Cocoon

First let me state that currently the portals sitemap.xmap file used for Astrogrid is in portalB/src/ral this is a side 
affect from iteration 2 and we will be moving into using the one in portalB/src/cocoon/sitemap.xmap at the end of September.
So when you want your pages working in the portal change the ral/sitemap.xmap and advise duplicating your entry in the 
src/cocoon/sitemap.xmap.

First create your own page hopefully in a xsp and xsl pages for cocoon.  With only the content no menus or anything that is common to astrogrid.
Now once you have your page working then in your xsl page put a surrounding page and content tags around your stuff.
<page>
 	<content>
 		//Now here is where you put all your content templates.
 	</content
 </page>
 
 Now it is time to wrap all the common astrogrid stuff into it.  This is done by the astrogrid.xsl file.  Look at the example below.
 So just add the lines xml element <map:act type="astrogrid"> below your cocoon sitemap entry.
 Be sure to add all the map:parameter names. And be sure to change the curent-page,help-page and title-name to your page.
 current-page normally matches your pattern another words the page name the users used to get to the page.
 help-page is the link to your help page.
 title-name is used for showing the name in the Astrogrid header.
          <map:match pattern="agadministration.html">
            <!-- Call our query action -->
            <map:act type="astrogrid-admin">
               <!-- Set the session attribute to check for the user name -->
               <map:parameter name="user-param"  value="user"/>
               <!-- Generate our page XML -->
               <map:generate src="community/xsp/administration.xsp" type="serverpages" label="000, 001"/>
               <!-- Convert the data into a page -->
               <map:transform src="community/xsl/administration.xsl" label="002">
                  <map:parameter name="action" value="{action}"/>
                  <map:parameter name="errormessage" value="{errormessage}"/>
                  <map:parameter name="message" value="{message}"/>
               </map:transform>
            </map:act>
            <!-- Wrap the page in our HTML template -->
            <map:act type="astrogrid">
            <map:transform src="common/xsl/astrogrid.xsl" label="003">
		              <map:parameter name="credential" value="{credential}" />
		              <map:parameter name="current-page"  value="agdataquery.html"/>
            
               <!-- Add the rest of the page names -->
               <map:parameter name="current-page"  value="agadministration.html"/>
               <map:parameter name="query-page"    value="agdataquery.html"/>
               <map:parameter name="myspace-page"  value="agmyspace.html"/>
               <map:parameter name="home-page"     value="agindex.html"/>
               <map:parameter name="help-page"     value="agdataqueryhelp.html"/>
               <map:parameter name="registry-page" value="agregistry.html"/>
               <map:parameter name="monitor-page"  value="agjobmonitor.html"/>
               <map:parameter name="tools-page"    value="agtools.html"/>
               <map:parameter name="title-name"  value="Administration"/>               
               <map:parameter name="admin-page"    value="agadministration.html"/>
               <map:parameter name="logout-page"   value="aglogout.html"/>
            </map:transform>
            </map:act>
            <!-- Serialize as HTML -->
            <map:serialize type="html"/>
         </map:match>
 ---------------------------------------------------
 Configuration.
 In the portalB/src/config their is a Config.xml file that can be added to if necessary.  This config file is loaded with jConfig during the login
 stages.  If you need to read things out of the config file use the statements below in your xsp or Custom Cocoon action objects.
 import org.astrogrid.community.common.CommunityConfig
 
String val CommunityConfig.getProperty("configpropertyname")  Their is also 2 other getProperty methods if you wish to use them
they are getProperty(propname,defaultValue) and getProperty(propname,defaultValue,jconfigCategory)
--------------------------------------------