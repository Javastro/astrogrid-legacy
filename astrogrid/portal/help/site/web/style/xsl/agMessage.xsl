<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	>

  <!--
	xmlns:util="http://apache.org/xsp/util/2.0"
	xmlns:jpath="http://apache.org/xsp/jpath/1.0"
	xmlns:xsp="http://apache.org/xsp"
  <xsl:param name="topico"/>
  <xsl:param name="title">Input Module</xsl:param>
  <xsl:param name="description"></xsl:param>
  <xsl:param name="fcuk" select="laconic"/>

  <xsl:variable name="fuck" select="laconic"/>
  -->

  <xsl:template match="AstroGrid">
<script language="javascript" src="/astrogrid-portal/extras.js">null;</script>
<body bgcolor="black" onLoad="transparentBody();">
<span class="agTitle007">AstroGrid</span>
</body>
</xsl:template>

<!--
if (parseInt(navigator.appVersion)>3){
	if (navigator.appName.indexOf("Microsoft")!=-1){
		document.body.bgcolor = "transparent";
	}
}
  <xsl:template match="draconic">
  </xsl:template>

-->
  <xsl:template match="laconic">
  </xsl:template>

  <xsl:template match="question">
  <p>
  <span class="question"> Q: 
       <xsl:apply-templates/>
  </span>
  </p>
  </xsl:template>

  <xsl:template match="answer">
  <p>
  <span class="answer"> A: 
       <xsl:apply-templates/>
  </span>
  </p>
  </xsl:template>

  <xsl:template match="mainArea">
  <!--
       <xsl:param name="flip" select="draconic"/>
       <xsl:variable name="topico" select="tropico"/>
       <h3>title <xsl:value-of select="$title"/></h3>
       <p>description <xsl:value-of select="$description"/></p>
       <p>name: <xsl:value-of select="name"/></p>
       <p>value: <xsl:value-of select="value"/></p>
       <p>variable value = <xsl:value-of select="$flip"/></p>
       -->
       <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="@*|node()" priority="-2"><xsl:copy><xsl:apply-templates
  select="@*|node()"/></xsl:copy></xsl:template>
  <xsl:template match="text()" priority="-1"><xsl:value-of select="."/></xsl:template>

</xsl:stylesheet>
