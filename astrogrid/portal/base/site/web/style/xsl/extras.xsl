<?xml version="1.0"?>

<!-- CVS $Id: extras.xsl,v 1.6 2004/12/03 21:16:00 clq2 Exp $ -->

<xsl:stylesheet version="1.0"
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:param name="ag-css-url" />
    <xsl:param name="ag-js-url" />

  <xsl:template match="/">
   <xsl:apply-templates/>
   </xsl:template>

<!--
  <xsl:template match="agComponentTitle">
<xsl:variable name="bugURL">/astrogrid-portal/bare/mount/help/bugsy.bug?item=<xsl:value-of select="."/></xsl:variable>
<table width="100%" cellpadding="0" cellspacing="0"
background="/astrogrid-portal/hudf_hst_strip.jpg">
<tr><td align="left">
<span style="color: yellow; font-size: 12pt; font-weight: bold;"><xsl:value-of select="."/></span>
  </td>
  <td width="100" align="right">
  <a style="color: #ffff00; font-size: 12pt; padding-right: 20px;
  text-decoration: none; font-weight: bold"
  target="_top"
  href="/astrogrid-portal/main/mount/login/myHome">myAstroGrid</a>
  </td>
  <td width="40" align="center">
<img src="/astrogrid-portal/Spider2.gif" title="Submit a bug report"
onClick="popupBrowser('{$bugURL}',400,700)"/>
  </td>
  <td width="100" align="right"> 
  <a style="color: #ffff00; padding-right: font-size: 12pt; 20px;" href="/astrogrid-portal/main/mount/login/logout">Log out</a>
  </td></tr></table>
  </xsl:template>
  -->

  <xsl:template match="agComponentTitle">
<xsl:variable
name="bugURL">/astrogrid-portal/bare/mount/help/bugsy.bug?item=<xsl:value-of
select="."/></xsl:variable>

<table width="100%" cellpadding="0" cellspacing="0" bgcolor="#eeffff">
<tr><td align="left" height="20px">
<span class="agCoolishTitle"><xsl:value-of select="."/></span>
  </td>
  <td width="100" align="right">
  <a style="colour: #000000; padding-right: 20px; font-size: 12pt;" href="/astrogrid-portal/main/mount/login/logout">Log out</a>
  </td>
  </tr></table>
<script language="javascript">
document.title = '<xsl:value-of select="."/>';
</script>
  </xsl:template>

  <xsl:template match="agComponentMessage">
<table width="100%" cellpadding="0" cellspacing="0"
background="/astrogrid-portal/hudf_hst_strip.jpg">
<tr><td align="left">
<span style="color: yellow; font-size: 12pt; font-weight: bold;"><xsl:value-of select="."/></span>
  </td></tr></table>
  </xsl:template>

  <xsl:template match="agOtherWindowTitle">
<table width="100%" cellpadding="0" cellspacing="0" bgcolor="#eeffff">
<tr><td align="left" height="20px">
<span class="agCoolishTitle"><xsl:value-of select="."/></span>
  </td>
  <td width="100" align="right">
  <span onClick="javascript:window.close();" style="background-color: transparent; cursor: pointer; color: #000088; font-variant: small-caps; font-size: 12pt; font-weight: bold; padding-right: 40px">Close</span>
  </td>
  </tr></table>
<script language="javascript">
top.document.title = "<xsl:value-of select="."/>";
</script>
  </xsl:template>

  <xsl:template match="agPageMessage">
<table width="100%" cellpadding="0" cellspacing="0" bgcolor="#eeffff">
<tr><td align="left" height="20px">
<span class="agCoolishTitle"><xsl:value-of select="."/></span>
  </td>
  </tr></table>
  </xsl:template>

  <xsl:template match="agPUBMessage">
<table width="100%" cellpadding="0" cellspacing="0"
background="/astrogrid-portal/hudf_hst_strip.jpg">
<tr><td align="left">
<span style="color: yellow; font-size: 12pt; font-weight: bold;"><xsl:value-of select="."/></span>
  </td>
  <td align="right">
  <span onClick="javascript:window.close();" style="background-color: black; cursor: pointer; color: yellow; font-variant: small-caps; font-size: 12pt; font-weight: bold; padding-right: 40px">Close</span>
  </td>
  </tr></table>
  </xsl:template>

  <xsl:template match="agSection">
          <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="para">
	  <p>
          <xsl:apply-templates/>
	  </p>
  </xsl:template>

  <xsl:template match="agVerticalSubmitArea">
<table width="20" cellpadding="0" cellspacing="0" border="0">
<tr><td> 
<img align="center" onclick="{@onClick}"
src="/astrogrid-portal/VerClick.png" width="20" title="{@title}"
style="cursor: pointer" id="{@ID}Fix"/>
</td></tr><tr><td>
<img align="center" onclick="{@onClick}"
src="/astrogrid-portal/SubmitGreen.gif" width="20" height="{@height}"
title="{@title}" style="cursor: pointer" id="{@ID}"/>
</td></tr></table>
  </xsl:template>

  <xsl:template match="agInstructions">
<div class="agInstructions">
	<xsl:apply-templates/>
</div>
  </xsl:template>


  <xsl:template match="profile">
  </xsl:template>

  <xsl:template match="agExplain">
<span class="agExplain" onclick="popupGlossary(this,
'{@item}')"><xsl:value-of select="@item"/></span>
  </xsl:template>

  <xsl:template match="agHint">
      <xsl:variable name="iconWidth">
      <xsl:choose>
         <xsl:when test="@iconSize != ''"><xsl:value-of select="@iconSize"/></xsl:when>
         <xsl:otherwise>20</xsl:otherwise>
      </xsl:choose>
      </xsl:variable>
<img src="/astrogrid-portal/icons/Help3.png"  id="{@id}" class="HintButton"
onClick="hinter(this)" width="{$iconWidth}" title="Click to open or close a Hint" />

<div id="{@id}stick" class="agHint" style="display:none;">
<span style="background-color: #ffc">Hint:</span><br />
          <xsl:apply-templates/>
	  </div>
  </xsl:template>

  <xsl:template match="@*|node()" priority="-2"><xsl:copy><xsl:apply-templates select="@*|node()"/></xsl:copy></xsl:template>
  <xsl:template match="text()" priority="-1"><xsl:value-of select="."/></xsl:template>

</xsl:stylesheet>
