<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsp="http://apache.org/xsp"
	xmlns:util="http://apache.org/xsp/util/2.0"
	xmlns:jpath="http://apache.org/xsp/jpath/1.0" >

  <!--
  <xsl:param name="topico"/>
  <xsl:param name="title">Input Module</xsl:param>
  <xsl:param name="description"></xsl:param>
  <xsl:param name="fcuk" select="laconic"/>

  <xsl:variable name="fuck" select="laconic"/>
  -->


  <xsl:template match="break">
       <xsl:apply-templates/>
       <br />
  </xsl:template>

  <xsl:template match="AstroGlossary">
       <xsl:apply-templates/>
  </xsl:template>

<!--
  <xsl:template match="draconic">
  </xsl:template>

Laconic: <xsl:value-of select="." /><p />
-->

  <xsl:template match="Glossary">
  <!--
<para>
Stupid thing: <xsl:value-of select="/AstroGlossary/laconic"/>
</para>
	<xsl:when test="/AstroGlossary/laconic != 'null'">
-->
       <xsl:apply-templates/>

<!--
<br />
<div style="text-align: right">
<a href="javascript:window.close()">Close this window</a>
<br/>
</div>
-->
  </xsl:template>

  <xsl:template match="entry">
  	<xsl:choose>
	<xsl:when test="/AstroGlossary/laconic != ''">
	    <xsl:if test="/AstroGlossary/laconic = @name">
<div style="font-family: helvetica, sans-serif; background-color: #ffffcc; color: black; font-size: smaller">
<para>
<span style="font-weight: bold; color: green">
<xsl:value-of select="@name"/> :
</span>
</para>
       <xsl:apply-templates/>
</div>
	    </xsl:if>
	</xsl:when>
	<xsl:otherwise>
<div style="background-color: #ffffcc; color: black">
<para>
<span style="font-weight: bold; color: green">
<xsl:value-of select="@name"/>
</span>
</para>
       <xsl:apply-templates/>
</div>
	</xsl:otherwise>
	</xsl:choose>
  </xsl:template>

  <xsl:template match="laconic">
  </xsl:template>


  <xsl:template match="@*|node()" priority="-2"><xsl:copy><xsl:apply-templates
  select="@*|node()"/></xsl:copy></xsl:template>
  <xsl:template match="text()" priority="-1"><xsl:value-of select="."/></xsl:template>

</xsl:stylesheet>
