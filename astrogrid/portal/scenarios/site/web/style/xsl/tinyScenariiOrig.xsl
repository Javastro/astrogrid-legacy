<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsp="http://apache.org/xsp"
	xmlns:util="http://apache.org/xsp/util/2.0"
	xmlns:jpath="http://apache.org/xsp/jpath/1.0" >

  <xsl:template match="Scenary">
       <xsl:apply-templates/>
  </xsl:template>

<xsl:variable name="userID"><xsl:value-of select="//*/userID" /></xsl:variable>
  <xsl:template match="AGScenarios">
  <script language="javascript" src="/astrogrid-portal/extras.js">null;</script>
<table >
<!--
<table width="90%">
<tr><td align="center">
Scenario
</td><td align="center" colspan="2">
Explanation
</td></tr>
<td><a href="{@link}/step1.scn"><div class="agActionButton" ><xsl:value-of select="@name"/></div></a></td>

<td><agHint id="{@name}"><xsl:value-of select="inExtenso"/></agHint></td>
<td><xsl:value-of select="inBrief"/></td>

-->
       <xsl:apply-templates/>
</table>
  </xsl:template>

<xsl:template match="AGscenario">
<tr>
<td><a target="_top" href="{link}?z={$userID}" title="{inBrief}"><div class="agActionButton" ><xsl:value-of select="@name"/></div></a></td>
</tr>
</xsl:template>

  <xsl:template match="thisContent">
       <center>
       <table width="90%">
       <tr><td>
       <xsl:apply-templates/>
       </td></tr>
       </table>
       </center>
  </xsl:template>

<!--	This is just an example, where a get parameter is used. -->
  <xsl:template match="Question">
     <xsl:variable name="tropico" select="../../../laconic"/>
       <xsl:choose>
          <xsl:when test="$tropico = ''">
             <xsl:apply-templates/>
          </xsl:when>
          <xsl:when test="$tropico = @subject">
             <xsl:apply-templates/>
          </xsl:when>
	  <xsl:otherwise>
	  </xsl:otherwise>
       </xsl:choose>
  </xsl:template>

<!--	leave untouched -->
  <xsl:template match="break">
       <xsl:apply-templates/>
       <br />
  </xsl:template>

  <xsl:template match="AGScenario">
  </xsl:template>

  <xsl:template match="userID">
  </xsl:template>

  <xsl:template match="origin">
  </xsl:template>

  <xsl:template match="laconic">
  </xsl:template>

  <xsl:template match="@*|node()" priority="-2"><xsl:copy><xsl:apply-templates
  select="@*|node()"/></xsl:copy></xsl:template>
  <xsl:template match="text()" priority="-1"><xsl:value-of select="."/></xsl:template>

</xsl:stylesheet>
