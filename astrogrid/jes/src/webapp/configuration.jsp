<%-- $Id:$--%>
<%-- (c) Astrogrid etc, but written by JDT --%>

<%@ taglib uri="http://jakarta.apache.org/taglibs/input-1.0" prefix="input" %>
<%@ page import="org.astrogrid.jes.JES,
                 org.astrogrid.AstroGridException,
                 java.net.URL,
                 javax.naming.Context,
                 javax.naming.NamingException,
                 javax.naming.InitialContext"
   session="false" %>

<jsp:useBean id="defaultsBean" class="org.astrogrid.jes.webapp.ConfigurationDefaultsBean" />
<%--Override the bean properties with any which may have been set in the form--%> 
<jsp:setProperty name="defaultsBean" property="*"/>

<%
//First check the properties file has been found and where from:
boolean loadedConfig = false;
boolean loadedConfigFromURL = false;
String message;
String jndiName = JES.getInstance().getJNDIName();
try {
   JES.getInstance().checkPropertiesLoaded();
   loadedConfig = true;
   // Obtain our environment naming context
   Context initCtx = new InitialContext();
   Context envCtx = (Context) initCtx.lookup("java:comp/env");   
   String url = (String) initCtx.lookup(jndiName);
   loadedConfigFromURL = true;
   message="Configuration file located at URL <BR>" + url + "<BR>Amongst other things it contained the following values:<BR>";
} catch (NamingException ne) {
   message="No URL bound in the JNDI naming service under " + jndiName + 
              ".  If you expected one, please check the installation instructions.   However, a config file " + JES.getInstance().getConfigFileName() + " was found on your classpath, so all is well.<BR>Amongst other things it contained the following values:<BR>";
} catch (AstroGridException e) {
   message="<span style='color: #ff0066;'>Failed to locate the configuration file.  Please refer to the installation instructions on how to configure mySpace.  <BR></span>";
}

//Names for buttons and flags to determine whether any have been pressed
String changeBtn = "change";
String exportBtn = "export";
String guessBtn = "guess";
boolean changePressed = request.getParameterValues(changeBtn)!=null;
boolean exportPressed = request.getParameterValues(exportBtn)!=null;
boolean guessPressed = request.getParameterValues(guessBtn)!=null;

//Now if guess has been pressed override those with our best guesses...

   if (guessPressed) {
      String baseURL = new URL ("http", request.getServerName(),
                                        request.getServerPort(), 
                                        request.getContextPath()).toString();
      defaultsBean.setControllerUrl(baseURL+"/JobControllerService");
      defaultsBean.setMonitorUrl(baseURL+"/JobMonitorService");
      defaultsBean.setSchedulerUrl(baseURL+"/JobSchedulerService");
      message="Based on the location of the webapp, these are the best guesses for the URLs.  Press change to apply them, followed by export if you wish to save them to the config file.  The exported file will need to be moved into the correct location and the webapp restarted.";
   } 
     
//If export pressed let's save 'em

  final String filenameTxt="fileName"; //name of textbox in form
   if (exportPressed) {
       String fileName=request.getParameter(filenameTxt);
       JES.getInstance().save(fileName);
       String managerURL = new URL ("http", request.getServerName(),
                                    request.getServerPort(), "/manager/html").toString();
       message="The values have been saved to <em>"+fileName+"</em>. " +
               "You now need to move this file to the location specified in the "+
               "installation instructions and <a href='"+managerURL+"'>reload</a> the webapp or restart the webserver.";
   }

//If change pressed let's change 'em
   if (changePressed) {
       defaultsBean.update();
       message="The values have been set as below for this session only.  If you reload "+
               "the webapp or restart the webserver they will revert to their saved values.";
   }
%>   

   <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

    <html>
      <head>
        
        
          <title>
            AstroGrid Project - 
          Job Entry System Configuration Page
        
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
                      <img border="0" alt="AstroGrid Job Entry System" src="http://www.astrogrid.org/images/AGlogo" align="right"></img>
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
      
      
      <a href="http://www.astrogrid.org/maven/build/Job Entry System/index.html"><img class="handle" src="maven/images/none.png" alt=""></img>Job Entry System</a>
      
    
  
                  
                  
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
      
      
    <div class="h4">
      
    <%=message%>  
        <h4>
          <a name="Job Entry System Manager">Job Entry System Manager</a>
        </h4>
      
      
    <P>
<!----------------------------------------------------------------------------->
<!----------------The Guts of the page go here--------------------------------->
<!----------------------------------------------------------------------------->      





<%
if (loadedConfig) {
%>
<input:form  bean="defaultsBean" method="post">
  Version: 
  <input:text name="version"  attributesText='size="15"' /><br>
  <span style="color: #800080;">Job Controller Url:</span> 
  <input:text name="controllerUrl" attributesText='size="100"'/> <br> 
	  <span style="color: #800080;">Job Scheduler Url:</span> 
  <input:text name="schedulerUrl" attributesText='size="100"'/> <br> 
	  <span style="color: #800080;">Job Monitor Url:</span> 
  <input:text name="monitorUrl" attributesText='size="100"'/> <br>
	<button type="submit" name="<%=guessBtn%>"  onmouseover="javascript:window.status='The URLs in purple can be guessed from the webapps installation location (need to press change use the values)';" onmouseout="javascript:window.status='';">Guess URLs</button> 
  <h4>Tools:</h4><P>
		  HyperZ URL: 
  <input:text name="hyperZUrl" attributesText='size="100"'/> <br> 
		  Data Federation URL: 
  <input:text name="dataFederationUrl" attributesText='size="100"'/> <br> 
		  Query Tool URL: 
  <input:text name="queryToolUrl" attributesText='size="100"'/> <br> 
		  S-Extractor URL: 
  <input:text name="sExtractorUrl" attributesText='size="100"'/> <br> 

	
	
  <button type="submit" name="<%=changeBtn%>" onmouseover="javascript:window.status='Update these values for this session only (will reset to values in config file after server is bounced)';" onmouseout="javascript:window.status='';">Change</button>
  

  <BR>
  <button type="submit" name="<%=exportBtn%>" onmouseover="javascript:window.status='Export these values to the named config file.';" onmouseout="javascript:window.status='';">Export</button> to 
<input:text name="<%=filenameTxt%>" attributesText='size="50"' default="../ASTROGRID_jesconfig.export"/>
</input:form>
<%  } %>
<p>Run the <a href="TestServlet?suite=org.astrogrid.Job Entry System.installationTests.DeploymentTests">Installation Tests</a> to see if the configuration is correct.<BR>
Back to <a href="index.html">index page</a>.</p>
   
     
   
<!----------------------------------------------------------------------------->
<!----------------------------------------------------------------------------->     
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
                
                  
                    
                    
                     (c) 2002-2004, AstroGrid
                    
                  
                  
                

                
              </td>
              
            </tr>
          </table>
        </div>
      </body>
    </html>
  