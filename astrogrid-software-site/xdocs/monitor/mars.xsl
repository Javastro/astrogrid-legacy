<?xml version="1.0" encoding="ISO-8859-1"?>

<xsl:stylesheet version="1.0"
xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:m="http://www.altara.org/mars/xmlns/model/">

<xsl:template match="/" >
  <html>
     <head>
     	<title>AstroGrid Host Status</title>
     	<meta http-equiv="REFRESH" content="10"/>
     </head>
  <body>
    <h2>AstroGrid Hosts</h2>
    <h3>These hosts are down</h3>
    <table border="1">
    <tr bgcolor="#ff7732">
      <th align="left">Name</th>
      <th align="left">Address</th>
      <th align="left">Port</th>
      <th align="left">Path</th>
      <th align="left">Last checked</th>
      <th align="left">Status</th>
    </tr>
    <xsl:for-each select="m:model/m:hostlist/m:host/m:service[m:status/@status!='up']">
    <tr>
      <td><xsl:value-of select="../@name"/>(<xsl:value-of select="@name"/>)</td>
      <td><xsl:value-of select="../@address"/></td>
      <td><xsl:value-of select="@port"/></td>
      <td><xsl:for-each select="m:parameter[@name='url']">
            	<xsl:value-of select="."/>
      </xsl:for-each></td>
      <td><xsl:value-of select="m:status/@timestamp"/></td>
      <td><xsl:value-of select="m:status/@status"/></td>      
    </tr>
    </xsl:for-each>
    </table>    
    <h3>And these hosts are up</h3>
    <table border="1">
    <tr bgcolor="#9acd32">
      <th align="left">Name</th>
      <th align="left">Address</th>
      <th align="left">Port</th>
      <th align="left">Path</th>
      <th align="left">Last checked</th>
      <th align="left">Status</th>
    </tr>
    <xsl:for-each select="m:model/m:hostlist/m:host/m:service[m:status/@status='up']">
    <tr>
      <td><xsl:value-of select="../@name"/>(<xsl:value-of select="@name"/>)</td>
      <td><xsl:value-of select="../@address"/></td>
      <td><xsl:value-of select="@port"/></td>
      <td><xsl:for-each select="m:parameter[@name='url']">
            	<xsl:value-of select="."/>
      </xsl:for-each></td>
      <td><xsl:value-of select="m:status/@timestamp"/></td>
      <td><xsl:value-of select="m:status/@status"/></td>      
    </tr>
    </xsl:for-each>
    </table>

    <hr/>
    To add your host to this list, please email <a href="mailto:jdt@roe.ac.uk">John Taylor</a>.
  </body>
  </html>
</xsl:template>

</xsl:stylesheet>