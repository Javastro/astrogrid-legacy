<?xml version="1.0"?>

<xsl:stylesheet
   version="1.0"
   xmlns="http://www.astrogrid.org/portal"
   xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

   <xsl:output
      method="xml"
      version="1.0"
      indent="yes"/>

      <xsl:param name="action" />
      <xsl:param name="errormessage" />
      <xsl:param name="activity-key" />  
  
      <!--+
          | Match the root element.
          +-->
      <xsl:template match="/">
         <html>
            <head>
               <link rel="stylesheet" type="text/css" href="main.css" />              
               <title>AstroGrid Portal</title>
            </head>
            <body onload="verifyName(); ">
               <div> <!-- div encloses content to be displayed in main portal pages -->          
                  <xsl:apply-templates/>
               </div> <!-- End div encloses content to be displayed in main portal pages -->
            </body>
         </html>
      </xsl:template>
  
      <!--+
          | Match the workflow element.
          +-->
      <xsl:template match="workflow">          
         <table border="1" cellpadding="0" cellspacing="0">
            <tr>
               <td>Name</td>
               <td>Description</td>
               <td>Time</td>
               <td>Status</td>
               <td>Job ID</td> 
            </tr>
            <xsl:for-each select="//job">
               <tr>    
                  <td><xsl:value-of select="@name"/></td>
                  <td><xsl:value-of select="@description"/></td>
                  <td><xsl:value-of select="@time"/></td>
                  <td><xsl:value-of select="@status"/></td>
                  <td><xsl:value-of select="@jobid"/></td>               
               </tr>
            </xsl:for-each>
         </table>                                                                                                                
      </xsl:template>
    
    
     <!-- Default, copy all and apply templates -->
     <xsl:template match="@*|node()">
       <xsl:copy>
         <xsl:apply-templates select="@*|node()" />
       </xsl:copy>
     </xsl:template>
   </xsl:stylesheet>
