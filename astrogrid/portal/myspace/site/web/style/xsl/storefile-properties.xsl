<?xml version="1.0"?>

<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="/properties">
    <html>
      <head>
        <title>
          Properties: <xsl:value-of select="@path"/>
        </title>
      </head>
      
      <body>
        <h1>
          Properties: <xsl:value-of select="@path"/>
        </h1>
        
        <table>
          <tr>
            <td>name:&#160;</td>
            <td><xsl:value-of select="@name"/></td>
          </tr>
          <tr>
            <td>path:&#160;</td>
            <td><xsl:value-of select="@path"/></td>
          </tr>
          <tr>
            <td>created:&#160;</td>
            <td><xsl:value-of select="@created"/></td>
          </tr>
          <tr>
            <td>modified:&#160;</td>
            <td><xsl:value-of select="@modified"/></td>
          </tr>
          <tr>
            <td>owner:&#160;</td>
            <td><xsl:value-of select="@owner"/></td>
          </tr>
          <tr>
            <td>size:&#160;</td>
            <td><xsl:value-of select="@size"/></td>
          </tr>
          <tr>
            <td>MIME type:&#160;</td>
            <td><xsl:value-of select="@mime-type"/></td>
          </tr>
        </table>
      </body>
    </html>
  </xsl:template>
  
  <!-- Default, copy all and apply templates -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()" />
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
