<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
>
  <xsl:output method="text" omit-xml-declaration="yes" indent="no" />

<xsl:template match="/">
   <xsl:value-of select="test-document/an-element/@attr" />
</xsl:template>

	  
</xsl:stylesheet>