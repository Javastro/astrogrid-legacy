<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html"/>

   <xsl:template match="agtemplate">
   <head>
      <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1"/>
      <title>
         <xsl:value-of select="@title" />
      </title>
   </head>
   <body>
   	<!-- Logo and Banner -->
      <table cellpadding="2" cellspacing="0" border="0" style="width: 100%;">
         <tbody>
            <tr align="center" valign="middle">
               <td style="height: 100px; width: 140px;">
                  <a href="www.astrogrid.org">
                  	<img src="aglogo.png" title="AstroGrid Home" alt="AstroGrid" 
                  style="border: 0px solid ; width: 140px; height: 100px;"/> 
                  </a>
               </td>
               <td bgcolor="#CCCCCC" style="color: #0000FF;">
                     <font size="+4" face="arial, helvetica, sans-serif">
                     	AstroGrid Virtual Observatory
                     </font> 
                  <br/>
                     <font size="+3" face="arial, helvetica, sans-serif">
                     	<xsl:value-of select="@title"/>
                     </font>
                  <br/>
               </td>
            </tr>
         </tbody>
      </table>
      <!-- Separator bar -->
            <table cellpadding="2" cellspacing="0" border="0"
                   style="text-align: left; width: 100%;">
         <tbody>
            <tr bgcolor="#0000FF">
               <td>
               	<br/>
               </td>
            </tr>
         </tbody>
      </table>
      <!-- Menu and main page-->
      <table cellpadding="2" cellspacing="0" border="0"
             style="height: 80%; width: 100%;">
         <tbody>
            <tr align="center" valign="top" style="color: rgb(51,51,255);">
            	<!-- Menu -->
               <td style="background-color: rgb(204,204,204); width: 140px;">
                  <a href="index.html"><br/> <b>Home</b></a>
                  <br/><br/>
                  <a href="agbrowseregistry.html"><b>Browse registry</b></a>
                  <br/><br/>
                  <a href="agdataquery.html"><b>Data Query</b></a>
                  <br/><br/>
                  <a href="agmyspace.html"><b>Browse MySpace</b></a>
                  <br/><br/>
                  <a href="agjobmonitor.html"><b>Job Monitor</b></a>
                  <br/><br/>
                  <a href="agtools.html"><b>Tools</b></a>
                  <br/><br/>
                  <a href="aglogout.html"><b>Logout</b></a>
                  <br/><br/>
               </td>
               <!-- Main page -->
               <td style="vertical-align: top;" rowspan="2">
                  <table cellpadding="2" cellspacing="2" border="0" 
                   style="width: 100%; margin-left: auto; margin-right: auto;">
                     <tbody>
                        <tr align="center" valign="middle" bgcolor="#000000">
                           <td width="0" height="0" colspan="0" rowspan="0" 
         style="color: white; font-family: arial, helvetica, sans-serif; 
         font-size-adjust: +2; font-weight: bold; height: 30px; width: 200px; ">
                                 <xsl:value-of select="@title"/>
                           </td>
                           <td width="0" height="0" colspan="0" rowspan="0" 
         style="color: white; font-family: arial, helvetica, sans-serif; 
         font-size-adjust: +2; font-weight: bold; height: 30px; width: 200px; ">
                                 <a href="agregistryhelp.html"
                                    style="color: #FFFFFF; ">Help</a>
                           </td>
                           <td>
                           </td>
                        </tr>
                        <tr align="left" valign="top">
                           <td rowspan="1" colspan="3">
<!-- Real  pages go here --> 
     <xsl:apply-templates/>
<!-- End of - Real  pages go here --> 
                           </td>
                        </tr>
                     </tbody>
                  </table>
                  <br/>
               </td>
            </tr>
            <!-- IVOA logo and link -->
            <tr bgcolor="#CCCCCC">
               <td align="center" style="color: #FF9900;
                         font-size-adjust: -2; width: 140px;">
                  	Member of the
                  <br/>
                  <a href="www.ivoa.net">
                  	<img src="ivoalogo.png" title="IVOA Alliance"
                      alt="IVOA Alliance" 
                      style="border: 0px solid ; width: 140px; height: 77px;"/> 
                  </a>
                  <br/>
                        International Virtual Observatory Alliance
               </td>
            </tr>
         </tbody>
      </table>
   </body>
   </xsl:template>

   <xsl:template match="@*|node()">
     <xsl:copy>
       <xsl:apply-templates select="@*|node()"/>
     </xsl:copy>
   </xsl:template>

</xsl:stylesheet> 
