<%-- $Id:$--%>
<%-- (c) Astrogrid etc, but written by JDT --%>

<%@ taglib uri="http://jakarta.apache.org/taglibs/input-1.0" prefix="input" %>
<%@ page import="org.astrogrid.mySpace.mySpaceManager.MMC,
                 org.astrogrid.AstroGridException,
                 java.net.URL,
                 javax.naming.Context,
                 javax.naming.NamingException,
                 javax.naming.InitialContext"
   session="false" %>

<jsp:useBean id="defaultsBean" class="org.astrogrid.mySpace.webapp.ConfigurationDefaultsBean" />
<jsp:useBean id="registryBean" class="org.astrogrid.mySpace.webapp.DatabaseConfigurationBean" />
<%--Override the bean properties with any which may have been set in the form--%> 
<jsp:setProperty name="defaultsBean" property="*"/>
<jsp:setProperty name="registryBean" property="*"/>

<%
//First check the properties file has been found and where from:
boolean loadedConfig = false;
boolean loadedConfigFromURL = false;
String message;
String jndiName = MMC.getInstance().getJNDIName();
String exportLocation="ASTROGRID_myspacemanagerconfig.xml";

String baseURL = new URL ("http", request.getServerName(),
                                  request.getServerPort(), 
                                  request.getContextPath()).toString();
String hostURL = new URL ("http", request.getServerName(),
                                  request.getServerPort(),
																	"").toString();
//try to guess where Tomcat is located
String tomcatHome = System.getProperty("catalina.home");
if (tomcatHome==null) {
	java.io.File file = new java.io.File("");
	tomcatHome = file.getAbsolutePath();//this ain't going to work unless Tomcat started from its root
}																	
try {
   MMC.getInstance().checkPropertiesLoaded();
   loadedConfig = true;
   // Obtain our environment naming context
   Context initCtx = new InitialContext();
   Context envCtx = (Context) initCtx.lookup("java:comp/env");   
   String url = (String) initCtx.lookup(jndiName);
   loadedConfigFromURL = true;
   message="Configuration file located at URL <BR>" + url + "<BR>Amongst other things it contained the following values:<BR>";
	 // This is a guess, and depends on the config file being on localhost:8080
	 if (url.indexOf("http://localhost:8080/")!=-1) {
	 		exportLocation = tomcatHome+"/webapps/ROOT/"+url.substring(22);
	 }
} catch (NamingException ne) {
   message="No URL bound in the JNDI naming service under " + jndiName + 
              ".  If you expected one, please check the installation instructions.   However, a config file " + MMC.getInstance().getConfigFileName() + " was found on your classpath, so all is well.<BR>Amongst other things it contained the following values:<BR>";
	 //guess that the location is the usual place on the classpath for a webapp
	 exportLocation=tomcatHome+"/webapps/"+request.getContextPath()+"/WEB-INF/classes/ASTROGRID_myspacemanager.xml";					
} catch (AstroGridException e) {
   message="<span style='color: #ff0066;'>Failed to locate the configuration file.  Please refer to the installation instructions on how to configure mySpace.  <BR></span>";
}


																				

//Names for buttons and flags to determine whether any have been pressed
String changeBtn = "change";
String exportBtn = "export";
String guessBtn = "guess";
String createBtn = "create";
String action = "action";

String[] actions = request.getParameterValues(action);

boolean changePressed = false;
boolean exportPressed = false;
boolean guessPressed = false;
boolean createPressed = false;
boolean initialise = false;
if (actions==null) {
	 initialise = true;
} else {
 changePressed = changeBtn.equals(actions[0]);
 exportPressed = exportBtn.equals(actions[0]);
 guessPressed = guessBtn.equals(actions[0]);
 createPressed = createBtn.equals(actions[0]);
}


																	
//set up some educated guesses about where to put the myspace registry database
registryBean.setPathToRegistry(defaultsBean.getRegistryconf());  //location of *.db.* files

if (!createPressed) {
	 registryBean.setHost(hostURL);

	 registryBean.setTomcatRoot(tomcatHome);
}

//Now if guess has been pressed override those with our best guesses...

   if (guessPressed) {

      defaultsBean.setMsmUrl(baseURL+"/services/Manager");
      defaultsBean.setMssUrl("NotRequired");
      defaultsBean.setMsmsUrl(baseURL+"/services/Manager,");
      message="Based on the location of the webapp, these are the best guesses for the URLs.  Press change to apply them, followed by export if you wish to save them to the config file.";
   } 
     
//If export pressed let's save 'em

  final String filenameTxt="fileName"; //name of textbox in form
   if (exportPressed) {
       String fileName=request.getParameter(filenameTxt);
       MMC.getInstance().save(fileName);
       String managerURL = new URL ("http", request.getServerName(),
                                    request.getServerPort(), "/manager/html").toString();
       message="The values have been saved to <em>"+fileName+"</em>. " +
               "You <em>may</em> now need to move this file to the location specified in the "+
               "installation instructions and <a href='"+managerURL+"'>reload</a> the webapp or restart the webserver.  Alternatively press change for your changes to effect now.";
   }

//If change pressed let's change 'em
   if (changePressed) {
       defaultsBean.update();
       message="The values have been set as below for this session only.  If you reload "+
               "the webapp or restart the webserver they will revert to their saved values.";
   }
	 
	 if (createPressed) {
	 		registryBean.createRegistry();
			String path = registryBean.getPathToRegistry();
			message="A new registry has been created at " + path +".  The files " + path +".db.properties and "+path+".db.script have been created.  MySpace files are stored in "+
			registryBean.getDirectory() + " which is served from URL <a href='" + registryBean.getURL() +"'>" + registryBean.getURL() + "</a>.";
	 }
%>   

   <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

    <html>
      <head>
        
        
          <title>
            AstroGrid Project - 
          MySpace Configuration Page
        
          </title>
        
        
        
        <style type="text/css">
          @import url("maven/style/tigris.css");
          @import url("maven/style/maven.css");
        </style>
        
        
        
        
        
        <link rel="stylesheet" href="maven/style/print.css" type="text/css" media="print"></link>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"></meta>
        
          <meta name="author" content="A C Davenhall"></meta>
          <meta name="email" content="acd@roe.ac.uk"></meta>
        
          <meta name="author" content="J D Taylor"></meta>
          <meta name="email" content="jdt@roe.ac.uk"></meta>
        
        
        
        
      </head>

      <body class="composite">
        <div id="banner" >
          <table border="0" width="100%" cellpadding="8" cellspacing="0" >
            <tr>
              
              <td>
                
                
                  
                  
                  
                    
                  
                  
                  
                  
                  
                  <a href="http://www.astrogrid.org">
                    <img border="0" alt="AstroGrid" src="http://www.astrogrid.org/images/AGlogo" align="left"></img>
                  </a>
                
              </td>

              
              <td>
                <div id="login" align="right">
                  
                  
                    
                    
                    

                    <a href="">
                      <img border="0" alt="AstroGrid MySpace" src="http://www.astrogrid.org/images/AGlogo" align="right"></img>
                    </a>
                  
                </div>
              </td>
            </tr>
          </table>
        </div>
        <div id="breadcrumbs">
          <table border="0" width="100%" cellpadding="4" cellspacing="0">
            <tr>
              
                
                <td>
                  
                </td>
              
              <td>
                <div align="right">
                  
                  
                  
                    
    
    
      
      
      
      <a href="http://www.astrogrid.org"><img class="handle" src="maven/images/none.png" alt=""></img>AstroGrid</a>
      
    
      |
      
      
      <a href="http://www.astrogrid.org/maven/build/index.html"><img class="handle" src="maven/images/none.png" alt=""></img>Projects</a>
      
    
      |
      
      
      <a href="http://www.astrogrid.org/maven/build/mySpace/index.html"><img class="handle" src="maven/images/none.png" alt=""></img>mySpace</a>
      
    
  
                  
                  
                </div>
              </td>
            </tr>
          </table>
        </div>
        
        <table border="0" width="100%" cellpadding="8" cellspacing="0"> 
          <tr valign="top">
            <td width="20%" id="leftcol">
              <div id="navcolumn">
                

                
                  
    <div>
  

                
                
                
                
                   <div style="margin-top: 20px; width: 100%; text-align: center;">
                      <a href="http://maven.apache.org/" title="Powered by Maven"><img style="border: 1px solid black" alt="Powered by Maven" src="maven/images/logos/maven-propaganda.png"></img></a>
                   </div>
                
              
                
              </div>
            </td>
            <td rowspan="2">
              <div id="bodycol">
                
                <div class="app">
                  
                  
    <div class="h3">
      
      
        <h3>
          <a name="Configuration">Configuration</a>
        </h3>
      
    <%=message%>
    <div class="h4">
      
      
        <h4>
          <a name="MySpace Manager">MySpace Manager</a>
        </h4>
      
      
    <P>
<!----------------------------------------------------------------------------->
<!----------------The Guts of the page go here--------------------------------->
<!----------------------------------------------------------------------------->      





<%
if (loadedConfig) {
%>
<input:form  bean="defaultsBean" method="get">
  Version: 
  <input:text name="version"  attributesText='size="15"' /><br>
  Location of Registry DB file: 
  <input:text name="registryconf" attributesText='size="100"'/> <br> 
  <span style="color: #800080;">MySpaceManagerURL:</span> 
  <input:text name="msmUrl"  attributesText='size="100"'/><br>
  <span style="color: #800080;">MySpaceServerURL:</span> 
  <input:text name="mssUrl"  attributesText='size="100"'/><br>
  <span style="color: #800080;">MySpaceMangerURLs:</span> 
  <input:text name="msmsUrl" attributesText='size="100"'/><br>
  <button type="submit" name="<%=action%>" value="<%=changeBtn%>" onmouseover="javascript:window.status='Update these values for this session only (will reset to values in config file after server is bounced)';" onmouseout="javascript:window.status='';">Change</button>
  
  <button type="submit" name="<%=action%>" value="<%=guessBtn%>"  onmouseover="javascript:window.status='The items in purple can be guessed from the webapp&#39;s installation location (need to press change to apply the values)';" onmouseout="javascript:window.status='';">Guess URLs</button>
  <BR>
  <button type="submit" name="<%=action%>" value="<%=exportBtn%>" onmouseover="javascript:window.status='Export these values to the named config file.';" onmouseout="javascript:window.status='';">Export</button> to 
<input:text name="<%=filenameTxt%>" attributesText='size="150"' default="<%=exportLocation%>"/>
</input:form>
<%  } %>
<p>Run the <a href="TestServlet?suite=org.astrogrid.mySpace.installationtest.DeploymentTests">Installation Tests</a> to see if the configuration is correct.<BR>
Back to <a href="index.html">index page</a>.</p>

<!----------------------------------------------------------------------------->
<!----------------------------------------------------------------------------->

<h4>
          <a name="MySpace Registry">Initialise the MySpace Registry Database</a>
</h4>     
<P>Instead of following the instructions in <a href="INSTALL">INSTALL</a> you can initialise the mySpace registry from here.  This need only be done once.</P>
<input:form  bean="registryBean" method="get">
  Location of MySpace database file to be created: 
  <%=registryBean.getPathToRegistry()%><br> 
	<hr>
  <span style="color: #800080;">Root directory of your Tomcat installaton:</span> 
  <input:text name="tomcatRoot"  attributesText='size="100"'/><br>
  <span style="color: #800080;">Tomcat hostname:</span> 
  <input:text name="host"  attributesText='size="100"'/><br>
	<hr>
	MySpace needs a folder in which to store files which is accessible by http from the webserver.  For simplicity, this 
	form chooses to put the files in the ROOT webapp.  	You need to choose
	a location under the ROOT webapp, in which to store them.  These defaults are probably fine.<BR>
  <span style="color: #800080;">Folder in which files are stored:</span> 
  <input:text name="folderName" attributesText='size="100"'/><br>
	<span style="color: #800080;">Server name</span> 
	<input:text name="serverName" attributesText='size="100"'/><br>
	<span style="color: #800080;">Expiry Time (days)</span> 
  <input:text name="expiry" attributesText='size="100"'/><br>
  <button type="submit" name="<%=action%>" value="<%=createBtn%>" onmouseover="javascript:window.status='Create the myspace registry" onmouseout="javascript:window.status='';">Create</button>
  
</input:form>
   
<!----------------------------------------------------------------------------->
<!----------------------------------------------------------------------------->     
    </P>
   
    </div>
  
    <div class="h4">
      
      
        <h4>
          <a name="MySpace Server">MySpace Server</a>
        </h4>
      
      
    <P>
      
     
For future use.
   
     
   
   
    </P>
   
    </div>
  
    </div>
  
                  
                  
                  
                  
                  
                </div>
              </div>
            </td>
          </tr>
        </table>
        <div id="footer">
          <table border="0" style="width:100%" cellpadding="4" cellspacing="0">
            
            <tr>
              <td>
                
                  
                    
                    
                      &copy; 2002-2004, AstroGrid
                    
                  
                  
                

                
              </td>
              
            </tr>
          </table>
        </div>
      </body>
    </html>
  