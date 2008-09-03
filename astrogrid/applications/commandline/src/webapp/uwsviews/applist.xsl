<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
   version="1.0"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
>
   <xsl:template match="/apps">
      <html>
         <head>
            <title>CEA Applications</title>
         </head>
         <body>
            <h1>CEA Application list</h1>
            <ul>
               <xsl:apply-templates />
            </ul>
         </body>
      </html>
   </xsl:template>
   <xsl:template match="app">
      <li>
         <xsl:element name="a">
            <xsl:attribute name="href">
            <xsl:value-of select="concat(../@base,@ref,'/res')" />
            </xsl:attribute>
            <xsl:value-of select="@name" />
         </xsl:element>
         <xsl:value-of select="concat(' ',@id)" />
      </li>
   </xsl:template>
</xsl:stylesheet>
