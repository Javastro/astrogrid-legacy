<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns="http://www.w3.org/2001/XMLSchema">

   <xsl:template match="queryResponse">
     <html>
        <head>
        </head>
        <body>
            <h2>DataSets Available</h2>
            <form method="POST" action="GetUCDAction">
            <xsl:apply-templates/>
            <input type="submit" value="Submit" name="GetUCD"><input type="reset" value="Reset" name="GetUCDReset"></h2></form>
        </body>
     </html>
   </xsl:template>

   <xsl:template match="responseRecord">
         <select size="7" name="Dataset">
         <xsl:apply-templates/>
         </select> </p>
   </xsl:template>

   <xsl:template match="recordKeyPair">
         <xsl:apply-templates/>
   </xsl:template>

   <xsl:template match="id">
              <option><xsl:value-of select="@value" /></option>
   </xsl:template>

</xsl:stylesheet> 


