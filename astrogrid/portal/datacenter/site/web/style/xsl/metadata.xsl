<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:util="http://apache.org/xsp/util/2.0"
	xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.9"
        xmlns:vs="http://www.ivoa.net/xml/VODataService/v0.4"
	xmlns:jpath="http://apache.org/xsp/jpath/1.0" >

<!--+
    |
    | Page created by PFO, Tue Nov 30 12:58:34 GMT 2004
    | it uses now a filtered system to handle registry entries
    |
    +-->
  <xsl:template match="AstroGrid">
       <xsl:apply-templates/>
  </xsl:template>


  <xsl:template match="agVaribleDisplayFrame">

<script src="/astrogrid-portal/extras.js" langage="javascript">
null;
</script>


<xsl:choose>
<xsl:when test="DQtableID != 'null'">
<xsl:variable name="requestedTable"><xsl:value-of select="DQtableID"/></xsl:variable>

<body style="font-size: 90%" >
<agOtherWindowTitle>Table: <xsl:value-of select="DQtableID"/></agOtherWindowTitle>

<center>

<table width="90%" cellpadding="0" cellspacing="3">
<xsl:for-each select="//*/agResource">
  <xsl:for-each select="agTable">
     <xsl:if test="agName = $requestedTable">
        <xsl:for-each select="agColumn">
<tr >
<td align="left">
<span class="agCoolTitle"><xsl:value-of select="agName"/></span>
</td>
<td bgcolor="#ffffcc" align="left">
<xsl:value-of select="agUnit"/>
</td>
<td bgcolor="#ccffcc" align="left">
<xsl:value-of select="agUCD"/>
</td>
</tr>
<tr>
<td bgcolor="#ccffff" align="left" colspan="3">
<xsl:value-of select="agDescription"/>
</td>
<!--
<input class="AGwideButton" type="button" onClick="xTEK('{agName}\040')" value=" {agName} " onMouseOver="cabc(this, '{agName}', '{agUCD}', '{agUnit}', '{agDescription}')" onMouseOut="cvbc(this)"/>
-->
</tr>
        </xsl:for-each>
     </xsl:if>
  </xsl:for-each>
</xsl:for-each>
</table>

<!--
<xsl:for-each select="//*/vr:Resource">
  <xsl:for-each select="vs:Table">
     <xsl:if test="vr:Name = $requestedTable or Name = $requestedTable">
        <xsl:for-each select="vs:Column">
<input class="AGwideButton" type="button" onClick="xTEK('{vr:Name}\040')" value=" {vr:Name} " onMouseOver="cabc(this, '{vr:Name}', '{vs:UCD}', '{vs:Unit}', '{vr:Description}')" onMouseOut="cvbc(this)"/>
        </xsl:for-each>

        <xsl:for-each select="vr:Column">
<input class="AGwideButton" type="button" onClick="xTEK('{vr:Name}\040')" value=" {vr:Name} " onMouseOver="cabc(this, '{vr:Name}', '{vr:UCD}', '{vr:Unit}{vr:Units}', '{vr:Description}')" onMouseOut="cvbc(this)"/>
        </xsl:for-each>

     </xsl:if>
  </xsl:for-each>

  <xsl:for-each select="vr:Table">
     <xsl:if test="vr:Name = $requestedTable or Name = $requestedTable">
        <xsl:for-each select="vs:Column">
<input class="AGwideButton" type="button" onClick="xTEK('{vr:Name}\040')" value=" {vr:Name} " onMouseOver="cabc(this, '{vr:Name}', '{vs:UCD}', '{vs:Unit}', '{vr:Description}')" onMouseOut="cvbc(this)"/>
        </xsl:for-each>

        <xsl:for-each select="vr:Column">
<input class="AGwideButton" type="button" onClick="xTEK('{vr:Name}\040')" value=" {vr:Name} " onMouseOver="cabc(this, '{vr:Name}', '{vr:UCD}', '{vr:Unit}{vr:Units}', '{vr:Description}')" onMouseOut="cvbc(this)"/>
        </xsl:for-each>

     </xsl:if>
  </xsl:for-each>
</xsl:for-each>


</form>
-->
</center>

       <xsl:apply-templates/>
</body>
</xsl:when>

<xsl:otherwise>
<body style="font-size: 90%; background: #ffffcc;" onLoad="locateTarget('MDsummary')">
<agComponentMessage>I n s t r u c t i o n s:</agComponentMessage>


<br />

<!--
<agComponentMessage>No table selected</agComponentMessage>
<li> No table metadata is present in this form.<p /></li>
-->
<div class="agTip">
This is an empty query page. You can: <p />
<ul>
<li> Load existing queries from MySpace.<p /></li>
<li> Cut and paste queries from a file.<p /></li>
<li> Create new queries from scratch.<p /></li>
<li> Create new queries locating a suitable resource first, then using its
metadata to create Click and paste buttons. <p />
Use the "Select a Table" button.<p /></li>
<li> "ADQL helpers" let you click and paste ADQL keywords to the entry area.</li>
</ul>
</div>
       <xsl:apply-templates/>
</body>
</xsl:otherwise>
</xsl:choose>
  </xsl:template>

  <xsl:template match="QUERYAREA">
       <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="laconic">
<!--
       <xsl:apply-templates/>
-->
  </xsl:template>

<!--
-->
  <xsl:template match="fakeRegistry">
  </xsl:template>

  <xsl:template match="uniqueID">
  </xsl:template>

  <xsl:template match="IVOA_TABLE">
  </xsl:template>

  <xsl:template match="author">
  </xsl:template>

  <xsl:template match="globalID">
  </xsl:template>

  <xsl:template match="description">
  </xsl:template>

  <xsl:template match="epoch">
  </xsl:template>

  <xsl:template match="numberOfVariables">
  </xsl:template>

  <xsl:template match="equinox">
  </xsl:template>

  <xsl:template match="numberOfRecords">
  </xsl:template>

  <xsl:template match="releaseDate">
  </xsl:template>

  <xsl:template match="Columns">
<!--
<tr><td colspan="2">
<xsl:value-of select="$../tableNo"/>
<span type="help">View Column Information</span><break/>
-->
<table class="compact">
<tr>
<td>Name</td>
<td>Units</td>
<td>UCD</td>
<td>Explanation</td>
</tr>
	<xsl:for-each select="FIELD">
		<xsl:variable name="fieldNo" select="position()"/>
<tr valign="top">
<td><xsl:value-of select="@name"/></td>
<td><xsl:value-of select="@Units"/></td>
<td><xsl:value-of select="@ucd"/></td>
<td><xsl:value-of select="Explanation"/></td>
</tr>
	</xsl:for-each>
</table>
<!--
	<xsl:apply-templates select="Columns"/>
</td></tr>
-->
  </xsl:template>

  <xsl:template match="OneTableID">
  <!--
  Value of OneTableID is <xsl:apply-templates/>
  -->
  </xsl:template>

  <xsl:template match="agADQLkeys">
	<xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="agADQLkey">
	<!--
<para> word: <xsl:value-of select="@name"/></para>
	-->
	<xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="agADQLxample">
  </xsl:template>

  <xsl:template match="agADQLxplain">
  </xsl:template>

  <xsl:template match="DQtableID">
  </xsl:template>

  <xsl:template match="FIELD">
  </xsl:template>

  <xsl:template match="Explanation">
  </xsl:template>

  <xsl:template match="RegQuery">
  </xsl:template>

  <xsl:template match="ValidatedAction">
  </xsl:template>

  <xsl:template match="NumberOfTables">
  </xsl:template>

  <xsl:template match="NumberOfShownTables">
  </xsl:template>

  <xsl:template match="bibcode">
  </xsl:template>

  <xsl:template match="uniqueID">
  </xsl:template>

  <xsl:template match="uniqueID">
  </xsl:template>

  <xsl:template match="QUERYAREA">
       <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="resultsFromRegistry">
  </xsl:template>


  <xsl:template match="Action">
  </xsl:template>

<!--	This is just an example, where a get parameter is used. -->

<!--	leave untouched -->
  <xsl:template match="break">
       <xsl:apply-templates/>
       <br />
  </xsl:template>


  <xsl:template match="@*|node()" priority="-2"><xsl:copy><xsl:apply-templates
  select="@*|node()"/></xsl:copy></xsl:template>
  <xsl:template match="text()" priority="-1"><xsl:value-of select="."/></xsl:template>

</xsl:stylesheet>
