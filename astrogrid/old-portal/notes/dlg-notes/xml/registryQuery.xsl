<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<xsl:output method="html"/>

   <xsl:template match="queryResponse">
         <h2>Registry Query Response</h2>
         <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="responseRecord">
         <p> <table>
         <xsl:apply-templates/>
         </table> </p>
   </xsl:template>

   <xsl:template match="recordKeyPair">
         <tr> <td> <em> <xsl:value-of select="@item" /> </em> </td>
              <td> <strong><xsl:value-of select="@value" /> </strong> </td></tr>
   </xsl:template>

</xsl:stylesheet> 
