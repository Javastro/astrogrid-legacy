<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

   <xsl:template match="/">
      <html>
         <title><xsl:value-of select="page/page-title"/></title>
      <body>
    <table cellspacing="0" border="0" width="100%">
      <tr>
        <td align="left" width="30%"><img alt="Home" src="AGlogo.gif"/></td>
        <td align="left">
          <h1>Astrogrid development portal</h1>
        </td>
      </tr>
    </table>
         <xsl:if test="contains(page/content/valid, 'false')">
             <center>
             <h1>Login failed - invalid usename and/or password!</h1>
             </center>
         </xsl:if>
         <xsl:if test="contains(page/content/valid, 'true')">
             <center>
             <h1>Welcome to Astrogrid!</h1>
             </center>
             <p>
             <h3>Functionality of this system is under rapid and
                 continuous development!</h3>
             </p>
             <p>
             Select <a href="registry.html">here </a> for Registry Query
             from Iteration 1 ported to Cocoon (live to Web-service on
             stargrid1). Also the current Registry Query live to either
             MSSL or RAL.
             </p>
             <p>
             Select <a href="workflow.html">here </a> to start Workflow
             (incomplete!)
             </p>
             <p>
             Select <a href="msexp.html">here </a> for Myspace
             Explorer (dummy page!)
             </p>
         </xsl:if>

      </body>
      </html>

   </xsl:template>
</xsl:stylesheet>
