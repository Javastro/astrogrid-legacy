<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

   <xsl:param name="credential"/>

   <xsl:output method="xml" indent="yes"/>

  <xsl:template match="/">
    <page>
      <content>
        <xsl:apply-templates/>
      </content>
    </page>
  </xsl:template>

  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()" />
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
