<html>
<head>
      <title>FileManager</title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>AstroGrid FileManager Configure</h1>
            
         Below describes what is needed to install and setup of the file store webapp.<br />
          <p><strong><i>Install</i></strong>
            <ul>
               <li> Since your reading this jsp page; I will assume you did the Install; so skip to Setup.</li>
               <li> 1.) First download a filemanager war file. Here is the <a href="http://www.astrogrid.org/maven/org.astrogrid/wars/astrogrid-filemanager-SNAPSHOT.war">Latest File Manager</a> or
                        look into the wars directory for other versions of the war file. </li>
               <li> 2.) Deploy the war into your servlet container, give it the context path that you desire. Normally achieved by renaming the war to what you like and dropping into the webapps directory. </li>
               <li> 3.) The installation is complete, now for the  setup (below).</li>
            </ul>
          </p>
          <p>
          <strong><i>Setup</i></strong>
            <ul>
               <li> 1.) Go to the Administration gui for the servlet container.</li>
               <li> 2.) Go to the Environment Entries for your context.  In Tomcat this is Service->Host(LocalHost)->{context name}->Resources->Environment entries. </li>
               <li> 
               	  3.) Change all the environment entries, to have the filemanager ivorn, base directory, and the endpoints to the registry (Hit Save after each of your changes)
               	  <br />
               	  <i>
               	  Only the latest tomcat will let you change the value field past 70 characters.  You may go into your server.xml and change or in tomcat you may
               	  go to tomcat/conf/Catalina/localhost/{context}.xml file and change the properties manually.  A restart of tomcat is not required in this method but
               	  may take up to 30 seconds for tomcat to catch your new changes to this file.<br />
               	  *A final note; if you do not see your file in there, then you need to go to the Administration GUI and just hit commit changes, servlet containers
               	  such as tomcat only creates the file after a commit changes button is hit; only needs to be done this first time.
               	  </i>               
               </li>
               <li> 4.) Please read the properties section below to understand all properties. </li>
               <li> 5.) In Tomcat hit "Commit Changes" at the top (if you did everything through the Administration gui).</li>
               <li> 6.) Open up a web browser and go to your new file manager ex: http://localhost:8080/{context} </li>
               <li> 7.) Go to the Admin pages.</li>
               <li> 8.) Now click on "Register Metadata" Fill in the Form details and the version of the type of Registry you wish to publish/register to; By default registries are on 0.10. (hit Submit).</li>
               <li> 9.) Verify the XML looks correct and hit (Register). </li>
               <li> 10.) Your Setup is now complete.</li>
            </ul>
         </p>
         <p>
         <strong><i>Properties</i></strong>
         <p>
            Properties to set, be sure to set all of them.  Talk to your Admin person about the ivorn. The registry endpoints currently
            point to localhost; ask your admin the correct endpoint such as galahad or hydra (only need to change the host:port/{context).
            <strong><font color='red'>WARNING - the repository location is set to '.' which will be where ever you start tomcat please change this.</font></strong>
            <ul>
            <li>org.astrogrid.filemanager.service.name - The given name of the file manager.</li>
            <li>org.astrogrid.filemanager.basedir - The Base directory of the file manager. In windows use double '\\' ex: c:\\repository.</li>
            <li>org.astrogrid.filemanager.filestore.ivorn - The default filestore service identifier.</li>
            <li>org.astrogrid.filemanager.service.ivorn - The file manager service identifier.</li>            
            <li>org.astrogrid.registry.admin.endpoint - Publishing Metadata to Registry endpoint, Point to the Registry that has or will contain your authorityid.</li>
            <li>org.astrogrid.registry.query.endpoint - Used for querying the registry; point to a full registry.</li>
            <li>org.astrogrid.registry.result.version - DO NOT CHANGE. It will soon go away, it is the version of xml coming back from the registry.</li>
            </ul>
         </p>
         </p>
</body>
</html>