<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsp="http://apache.org/xsp"
	xmlns:jpath="http://apache.org/xsp/jpath/1.0" >

<xsl:template match="AstroGrid">
<ag-div>
    <ag-script type="text/javascript" src="/astrogrid-portal/sticky.js"/>
    <ag-script type="text/javascript" src="/astrogrid-portal/extras.js"/>
    <ag-script type="text/javascript" src="/astrogrid-portal/paneltab.js"/>
  
  <!--
  background="/astrogrid-portal/solar_strip.jpg">
  background="/astrogrid-portal/zodiacal_seip_strip.jpg">
  -->
  <div id="agmsg" style="display: none">
<iframe style="border: solid 0px white; background-color: transparent" marginWidth="0px" scrolling="no" hspace="0px" vspace="0px" frameborder="no"
allowtransparency="true"
src="/astrogrid-portal/clear/mount/help/agMessage.i07"
marginHeight="0px" border="0px" height="20" width="220"
name="agMsgFrame">ag</iframe>
  </div>
<xsl:if test="userID != 'nadie'">

<xsl:if test="//*/profile/taskBar/task/fascia != ''">
<table width="100%" cellpadding="0" cellspacing="0" bgcolor="#aacccc">
<tr>
<td align="left" height="20px">
<span class="compact" style="color: #000088"> <xsl:value-of select="userID"/> Tasks: </span>
<xsl:for-each select="//*/profile/taskBar/task">
   <xsl:if test="fascia != ''">
   <span style="padding-left: 3px">
   <a class="myTaskButton" href="{link}"
   title="{hover}">
   <xsl:if test="target != 'top'">
<xsl:attribute name="target"><xsl:value-of select="target"/></xsl:attribute>
   </xsl:if>
   <xsl:value-of select="fascia"/></a>
   </span>
   </xsl:if>
</xsl:for-each>
</td>
</tr>
</table>
</xsl:if>

</xsl:if>

  </ag-div>
       <xsl:apply-templates/>
</xsl:template>

      <!--
      <h1 class="ag-h1">AstroGrid Portal Page</h1>
      -->
  <xsl:template match="Scenary">
       <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="AGScenarios">
<center>
List of available scenarios
</center>
<table width="90%">
<!--
<tr><td align="center">
Scenario
</td><td align="center" colspan="2">
Explanation
</td></tr>
<td><a href="{@link}/step1.scn"><div class="agActionButton" ><xsl:value-of select="@name"/></div></a></td>
-->
       <xsl:apply-templates/>
</table>
  </xsl:template>

<xsl:template match="AGscenario">
<tr>
<td><a href="{link}"><div class="agActionButton" ><xsl:value-of select="@name"/></div></a></td>
<td><agHint id="{@name}"><xsl:value-of select="inExtenso"/></agHint></td>
<td><xsl:value-of select="inBrief"/></td>
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

  <xsl:template match="ag-profile">
       <xsl:apply-templates/>
  </xsl:template>


<!--	leave untouched -->
  <xsl:template match="break">
       <xsl:apply-templates/>
       <br />
  </xsl:template>

  <xsl:template match="AGScenario">
  </xsl:template>

<!--
  <xsl:template match="userID">
  </xsl:template>

  <xsl:template match="profile">
  </xsl:template>
  -->

  <xsl:template match="laconic">
  </xsl:template>

  <xsl:template match="@*|node()" priority="-2"><xsl:copy><xsl:apply-templates
  select="@*|node()"/></xsl:copy></xsl:template>
  <xsl:template match="text()" priority="-1"><xsl:value-of select="."/></xsl:template>

</xsl:stylesheet>
