
  

  
  

  
  

  

  
  

  
  

  
  

  

  

  

  

  

  
  
  
  
      
  

      
      
      
      
      
      
        
         
      
      

      
      
<%@ page import="org.astrogrid.mySpace.mySpaceManager.MMC,
				 org.astrogrid.mySpace.mySpaceServer.MSC,
                 java.io.PrintWriter,
                 javax.naming.Context,
                 javax.naming.spi.NamingManager,
                 javax.naming.NamingException,
                 javax.naming.InitialContext"
   session="false" %>

<%
//Do something here to see if called from configuration.jsp
//and set vars in MMC appropriately.  Make sure you distinguish between MMC
//and MSC
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

        <div id="banner">
          <table border="0" width="100%" cellpadding="8" cellspacing="0">
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
      
     
<%
//First check the properties file has been found:
try {
  MMC.getInstance().checkPropertiesLoaded();
  %>MySpaceManager Configuration file located<%
  } catch (Exception e) {
  %>Exception locating MySpaceManager configuration file:
<p><%
  e.printStackTrace(new PrintWriter(out));
  }
%></p>

<form action="configuration.jsp" method="post">Version: <input
type="text" name="version" value=
"<%=MMC.getProperty(MMC.GENERAL_VERSION_NUMBER, MMC.GENERAL_CATEGORY) %>"
 size="15"><br>
MySpaceManagerURL: <input type="text" name="msm_url" value=
"<%=MMC.getProperty(MMC.mySpaceManagerLoc,MMC.CATLOG)%>" size=
"70"><br>
MySpaceServerURL: <input type="text" name="mss_url" value=
"<%=MMC.getProperty(MMC.serverManagerLoc,MMC.CATLOG)%>" size=
"70"><br>
MySpaceServerURLs: <input type="text" name="msss_url" value=
"<%=MMC.getProperty(MMC.MYSPACEMANAGERURLs,MMC.CATLOG)%>" size=
"70"><br>
<button type="submit">Change</button></form>
(button is a dummy at the moment - these props are readonly....) 
<!--
Version in property file is: "<%=MMC.getProperty(MMC.GENERAL_VERSION_NUMBER, MMC.GENERAL_CATEGORY) %>" - should be "1.2".<P>
MySpaceManagerURL :<%=MMC.getProperty(MMC.mySpaceManagerLoc,MMC.CATLOG)%><P>
MySpaceServerURL :<%=MMC.getProperty(MMC.serverManagerLoc,MMC.CATLOG)%><P>
MySpaceServerURLs :<%=MMC.getProperty(MMC.MYSPACEMANAGERURLs,MMC.CATLOG)%><P>
<P>-->
<p>Back to <a href="index.html">index page</a>.</p>
   
     
	
   
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
  