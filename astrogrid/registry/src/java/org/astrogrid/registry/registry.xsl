<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <xsl:template match="RESULTS">
      <table>
         <caption>Registry Details (from XSL)</caption>
         <tr> <th><strong>Id</strong></th> <th><strong>Title</strong></th> </tr>
         <tr> <th>-----------------------------</th>
              <th>-----------------------------</th> </tr>
         <xsl:apply-templates/>
      </table>
   </xsl:template>

   <xsl:template match="basicMetadata">
      <tr> <td> <xsl:value-of select="id"/> </td>
           <td> <xsl:value-of select="title"/> </td>
      </tr>
   </xsl:template>

</xsl:stylesheet> 
