<%@ taglib uri="http://jakarta.apache.org/taglibs/input-1.0" prefix="input" %>
<%@ page import="org.astrogrid.mySpace.mySpaceManager.MMC,
				 org.astrogrid.mySpace.mySpaceServer.MSC,
				 org.astrogrid.AstroGridException,
                 java.io.PrintWriter,
								 java.util.*,
								 java.net.URL,
                 javax.naming.Context,
                 javax.naming.spi.NamingManager,
                 javax.naming.NamingException,
                 javax.naming.InitialContext"
   session="false" %>
<jsp:useBean id="defaultsBean" class="org.astrogrid.mySpace.webapp.ConfigurationDefaultsBean" />
<%

String changeBtn = "change";
String exportBtn = "export";
String guessBtn = "guess";
boolean changePressed = request.getParameterValues(changeBtn)!=null;
boolean exportPressed = request.getParameterValues(exportBtn)!=null;
boolean guessPressed = request.getParameterValues(guessBtn)!=null;

%>	 

<%
//First check the properties file has been found and where from:
boolean loadedConfig = false;
boolean loadedConfigFromURL = false;
String message;
String jndiName = MMC.getInstance().getJNDIName();
try {
  MMC.getInstance().checkPropertiesLoaded();
	loadedConfig = true;
	// Obtain our environment naming context
	Context initCtx = new InitialContext();
	Context envCtx = (Context) initCtx.lookup("java:comp/env");   
  String url = (String) initCtx.lookup(jndiName);
	loadedConfigFromURL = true;
	message="Configuration file located at URL <BR>" + url;
} catch (NamingException ne) {
	message="No URL bound in the JNDI naming service under " + jndiName + 
				  ".  If you expected one, please check the installation instructions.   However, a config file " + MMC.getInstance().getConfigFileName() + " was found on your classpath, so all is well.<BR>";
} catch (AstroGridException e) {
	message="<span style='color: #ff0066;'>Failed to locate the configuration file.  Please refer to the installation instructions on how to configure mySpace.  <BR></span>";
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
			<%
Enumeration enum = request.getParameterNames();
while(enum.hasMoreElements()) {
			out.print(enum.nextElement()+"<BR>");
			}%>
			<BR>Change pressed:<%=changePressed%>
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
      
      
    <div class="h4">
      
      
        <h4>
          <a name="MySpace Manager">MySpace Manager</a>
        </h4>
      
      
    <P>
<!----------------------------------------------------------------------------->
<!----------------The Guts of the page go here--------------------------------->
<!----------------------------------------------------------------------------->      


<%--Override the bean properties with any which may have been set in the form--%> 
<jsp:setProperty name="defaultsBean" property="*"/>
<%--Now if guess has been pressed override those with our best guesses...--%>
<%
	if (guessPressed) {
	   String baseURL = new URL ("http", request.getServerName(),
		 							    request.getServerPort(), request.getContextPath()).toString();
		 defaultsBean.setMsmUrl(baseURL+"/MySpaceManager");
 		 defaultsBean.setMssUrl("NotRequired");
 		 defaultsBean.setMsmsUrl(baseURL+"/MySpaceManager,");
	} 
%>     
<%--If export pressed let's save 'em--%>
<%
	if (exportPressed) {
		 MMC.getInstance().save();
	}
	%>
<%=message%>


<input:form  bean="defaultsBean">
Version: 
<input:text name="version"  attributesText='size="15"' /><br>
Location of Registry DB file: 
<input:text name="registryconf" attributesText='size="100"'/> <br> 
MySpaceManagerURL: 
<input:text name="msmUrl"  attributesText='size="100"'/><br>
MySpaceServerURL: 
<input:text name="mssUrl"  attributesText='size="100"'/><br>
MySpaceMangerURLs: 
<input:text name="msmsUrl" attributesText='size="100"'/><br>
<button type="submit" name="<%=changeBtn%>">Change</button>

<button type="submit" name="<%=guessBtn%>" >Guess URLs</button>

<button type="submit" name="<%=exportBtn%>">Export</button>
</input:form>

<p>Back to <a href="index.html">index page</a>.</p>
   
     
	
<!----------------------------------------------------------------------------->
<!----------------------------------------------------------------------------->     
    </P>
   
    </div>
  
    <div class="h4">
      
      
        <h4>
          <a name="MySpace Server">MySpace Server</a>
        </h4>
      
      
    <P>
      
     
Not currently required.
   
     
	
   
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
                
                  
                    
                    
                      © 2002-2004, AstroGrid
                    
                  
                  
                

                
              </td>
              
            </tr>
          </table>
        </div>
      </body>
    </html>
  