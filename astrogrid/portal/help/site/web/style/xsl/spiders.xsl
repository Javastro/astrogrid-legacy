<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsp="http://apache.org/xsp"
	xmlns:util="http://apache.org/xsp/util/2.0"
	xmlns:jpath="http://apache.org/xsp/jpath/1.0" >

  <xsl:template match="AstroGrid">
       <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="bugReport">
<center>
Astrogrid's portal Feedback form
</center>
<form name="bugso" action="nula" method="post">
<table width="90%">
<tr><td>
Origin: <xsl:value-of select="origin"/>
</td></tr>

<tr><td>
Reason to report: <select name="ReasonWhy">
<option value="suggestion"> Suggestion</option>
<option value="bugReport"> Bug report</option>
<option value="missingAid"> Disoriented</option>
<option value="positive"> You liked it!</option>
</select>
</td></tr>
<tr><td>
Previous step taken:
</td></tr>
<tr><td>
<textarea name="preStep" rows="4" cols="30">.</textarea>
</td></tr>

<tr><td>
Your Comments:
</td></tr>
<tr><td>
<textarea name="comments" rows="20" cols="30">.</textarea>
</td></tr>

<tr><td>
Steps you wished to take:
</td></tr>
<tr><td>
<textarea name="steps2come" rows="4" cols="30">.</textarea>
</td></tr>
<!--
       <xsl:apply-templates/>
-->
<tr><td align="right">
<input type="submit" value="Submit" />
</td></tr>
</table>
</form>
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

  <xsl:template match="origin">
  </xsl:template>

  <xsl:template match="laconic">
  </xsl:template>

  <xsl:template match="@*|node()" priority="-2"><xsl:copy><xsl:apply-templates
  select="@*|node()"/></xsl:copy></xsl:template>
  <xsl:template match="text()" priority="-1"><xsl:value-of select="."/></xsl:template>

</xsl:stylesheet>
