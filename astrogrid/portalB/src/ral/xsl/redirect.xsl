<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

   <xsl:template match="redirect">
      <html>
         <head>
            <meta http-equiv="Refresh" content="0;URL={@url}"/>
         </head>
      </html>
    </xsl:template>
</xsl:stylesheet>
