<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsp="http://apache.org/xsp"
	xmlns:util="http://apache.org/xsp/util/2.0"
	xmlns:jpath="http://apache.org/xsp/jpath/1.0" >

  <xsl:template match="Scenary">
       <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="AGScenarios">
<center>
<table width="70%">
<tr><td align="left">Roadmap</td></tr>
<tr><td>
<pre>
  GOAL: 
	- to locate electronic version of catalogues available to AstroGrid or IVO

  FEATURES:
	- you set your own search criteria
	- flexible logics let you combine criteria
	- lists of catalogues can be stored in mySpace for future use.

  SEARCH CRITERIA
  	- table (resource) name
	- astronomical keywords
	- mission (especially space-born)
	- resource author
	- resource title
	- resource description
	- wavelength coverage
	- column content (name and/or UCDs)
</pre>
</td></tr>
<tr><td align="right">
<a class="compact" href="http://barbara.star.le.ac.uk/datoz-bin/catalogueLocator1">Do not
click here</a>

</td></tr>
</table>
</center>
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
