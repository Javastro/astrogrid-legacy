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

  <xsl:template match="questions">
       <center>
       <table width="90%">
       <!--
       <tr><td>
       laconic is: <xsl:value-of select="$fuck"/>
       </td></tr>
       -->
       <tr><td>
       <ol>
       <xsl:for-each select="Question">
	 <xsl:variable name="qq" select="position()"/>
         <xsl:variable name="tropico" select="../../../laconic"/>
       <xsl:choose>
          <xsl:when test="$tropico = ''">
       	<li> 
	<xsl:text>Q:</xsl:text> <xsl:value-of select="question"/></li>
	<!--
	<xsl:text>Q:</xsl:text> <xsl:value-of select="$qq"/><xsl:value-of select="question"/></li>
	-->
          </xsl:when>
          <xsl:when test="$tropico = @subject">
       	<li>
	<xsl:text>Q:</xsl:text> <xsl:value-of select="question"/></li>
          </xsl:when>
	  <xsl:otherwise>
	  </xsl:otherwise>
       </xsl:choose>
       </xsl:for-each>
       </ol>
       <xsl:apply-templates/>
       </td></tr>
       </table>
       </center>
  </xsl:template>

  <xsl:template match="Question">
     <xsl:variable name="tropico" select="/AstroGrid/laconic"/>
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
       <!--
       <xsl:apply-templates/>
       -->
  </xsl:template>

  <xsl:template match="break">
       <xsl:apply-templates/>
       <br />
  </xsl:template>

  <xsl:template match="AstroGrid">
       <xsl:apply-templates/>
  </xsl:template>

<!--
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
