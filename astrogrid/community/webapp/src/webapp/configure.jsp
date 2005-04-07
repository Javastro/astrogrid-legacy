<html>
<head>
      <title>Community</title>
<style type="text/css" media="all">
          @import url("./style/astrogrid.css");
</style>
</head>

<body>
<%@ include file="header.xml" %>
<%@ include file="navigation.xml" %>

<div id='bodyColumn'>

<h1>AstroGrid Community Configure</h1>
            
         Below describes what is needed to install and setup of the community.  The install and setup below is an
          example with a HSQLDB database.<br />
          <p><strong><i>Install</i></strong>
            <ul>
               <li> 1.) First download a comunity war file. Here is the <a href="http://www.astrogrid.org/maven/org.astrogrid/wars/astrogrid-community-SNAPSHOT.war">Latest Community</a> or
                        look into the wars directory for other versions of the war file. </li>
               <li> 2.) Deploy the war into your servlet container, give it the context path that you desire. </li>
               <li> 3.) Find the hsqldb database driver. You can download from here: 
                  <a href="http://www.astrogrid.org/maven/hsqldb/jars/hsqldb-1.7.1.jar">HSQLDB 1.7.1</a> </li>
               <li> 4.) Place the hsqldb jar into a place that can be located by the servlet container, ex: for tomcat a common place is the common/lib directory.</li>
               <li> 5.) The installation is complete, if this is the first time putting in the hsqldb.jar you will need to restart the servlet container for it to be picked up, now for the  setup (below).</li>
            </ul>
          </p>
          <p>
          <strong><i>Setup</i></strong>
            <ul>
               <li> 1.) Go to the Administration gui for the servlet container.</li>
               <li> 2.) Go to the Environment Entries for your context.  In Tomcat this is Service->Host(LocalHost)->{context name}->Resources->Environment entries. </li>
               <li> 3.) Change all the environment entries, to have the community id, and the endpoints to the registry, also define the registry identifier for a vospace/myspace used for accounts. (Hit Save after each of your changes)</li>
               <li> 4.) After Environment Entries are changed go to the "DataSources" (same area as Environment Entries) <strong>(see the sample DataSource config below.)</strong> <i>Because of a resource-ref element inside the web.xml a DataSource with only a name filled out may be present</i>. </li>
               <li> 5.) Change the URL to not have "@WORKDIR@" but to a appropriate directory that will keep your database data and information. (Hit Save)</li>
               <li> 6.) In Tomcat hit "Commit Changes" at the top.</li>
               <li> 7.) Open up a web browser and go to your new community ex: http://localhost:8080/{context} </li>
               <li> 8.) Go to the Admin pages, there it will report if your database is healthy or not, if this is the first time then it should NOT be healthy.</li>
               <li> 9.) If not healthy go to "Reset DB" this essentially deletes everything from the db and recreates all tables and a small amount of test data to check if it is healthy.</li>
               <li> 10.) Reset the db and verify it is healthy, a message should say if it is healthy or not.</li>
               <li> 11.) Now click on "Register Metadata" Fill in the Form details and the version of the type of Registry you wish to publish/register to. (hit Submit).</li>
               <li> 12.) Verify the XML looks correct and hit (Register). </li>
               <li> 13.) Your Setup is now complete you may begin adding accounts, groups, and others. </li>
            </ul>
               	  <i>
               	  Only the latest tomcat will let you change the value field past 70 characters.  You may go into your server.xml and change or in tomcat you may
               	  go to tomcat/conf/Catalina/localhost/{context}.xml file and change the properties manually.  A restart of tomcat is not required in this method but
               	  may take up to 30 seconds for tomcat to catch your new changes to this file.<br />
               	  *A final note; if you do not see your file in there, then you need to go to the Administration GUI and just hit commit changes, servlet containers
               	  such as tomcat only creates the file after a commit changes button is hit; only needs to be done this first time.
               	  </i>               
         </p>
         <p>
           <strong><i>Properties</i></strong>
           <ul>
           		<li>org.astrogrid.community.ident - community identifier, must be changed.</li>
           		<li>org.astrogrid.community.default.vospace - default vospace identifier (normally a filemanger)</li>
           		<li>org.astrogrid.registry.query.endpoint - regustry query endpoint normally.</li>
           		<li>org.astrogrid.registry.admin.endpoint - your publishing registry to send your registry. (may be or may not be the same as the query endpoint)</li>
           </ul>         
         </p>
         <p>
         <strong><i>Customize</i></strong>
         <p>
            It has been untested on other databases, but presumbly you may setup community to work with any database.  See the "{context app location}/WEB-INF/classes" directory
              for configuration files if you need to change the sql used to create tables, or possibly even the JDO mapping.  
            There are 3 files.
            <ul>
               <li> astrogrid-community-database.sql -- SQL used to create table data. </li>
               <li> astrogrid-community-database.xml -- no real need to change it points to the mapping file in the same directory. </li>
               <li> astrogrid-community-mapping.xml -- JDO mapping for Castor.  See <a href="http://castor.exolab.org/">Castor</a> for more information on the mapping.</li>
            </ul>
         </p>
         </p>
         <p>
           <strong>Sample DataSource Config</strong><br />
           <table>
           	<tr>
           		<td>Data Source Property</td>
           		<td>Example Value</td>
           	</tr>
           	<tr>
           		<td>JNDI Name:</td>
           		<td>jdbc/org.astrogrid.community.database</td>
           	</tr>
           	<tr>
           		<td>Data Source URL:</td>
           		<td>jdbc:hsqldb:/data/workarea/community/org.astrogrid.community</td>
           	</tr>
           	<tr>
           		<td>JDBC Driver Class:</td>
           		<td>org.hsqldb.jdbcDriver</td>
           	</tr>
           	<tr>
           		<td>User Name:</td>
           		<td>sa</td>
           	</tr>
           	<tr>
           		<td>Password:</td>
           		<td>&nbsp;</td>
           	</tr>
           </table>         
         </p>
</body>
</html>