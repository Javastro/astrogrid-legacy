<?xml version="1.0"?>

<!-- CVS $Id: extras.xsl,v 1.3 2004/05/25 23:56:51 jdt Exp $ -->

<xsl:stylesheet version="1.0"
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:param name="ag-css-url" select="'default.css'"/>
    <xsl:param name="ag-js-url" />

  <xsl:template match="/">
<xsl:element name="link">
<xsl:attribute name="href"><xsl:value-of select="$ag-css-url"/></xsl:attribute>
<xsl:attribute name="rel">stylesheet</xsl:attribute>
<xsl:attribute name="type">text/css</xsl:attribute>
</xsl:element>
   <xsl:apply-templates/>
   </xsl:template>

  <xsl:template match="agComponentTitle">
<!--

<xsl:element name="script">
<xsl:attribute name="type">text/javascript</xsl:attribute>
<xsl:attribute name="src"><xsl:value-of select="$ag-js-url"/></xsl:attribute>
</xsl:element>
-->
  <!--
<div style="color: red; background-color: darkblue">
          <xsl:apply-templates/>
</div>
background="/AGPortal/sunprom_soho_strip.gif">
-->
<table width="100%" cellpadding="0" cellspacing="0"
background="/astrogrid-portal/hudf_hst_strip.jpg">
<tr><td align="left">
<span style="color: yellow; font-size: 12pt; font-weight: bold;"><xsl:value-of select="."/></span>
  </td></tr></table>
  </xsl:template>

  <xsl:template match="agSection">
          <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="agHint">
  <!--
<button type="button" id="{@id}" class="HintButton" onClick="hinter(this)"
title="Click to open or close a Hint"
value="Hint">Hint</button>
-->
<img src="/astrogrid-portal/icons/Help3.png"  id="{@id}" class="HintButton"
onClick="hinter(this)" title="Click to open or close a Hint" />

	  <div id="{@id}stick" class="agHint" style="display:none">
<span style="background-color: #ffc">Hint:</span><br />
          <xsl:apply-templates/>
	  </div>
  </xsl:template>

  <xsl:template match="@*|node()" priority="-2"><xsl:copy><xsl:apply-templates select="@*|node()"/></xsl:copy></xsl:template>
  <xsl:template match="text()" priority="-1"><xsl:value-of select="."/></xsl:template>

</xsl:stylesheet>
