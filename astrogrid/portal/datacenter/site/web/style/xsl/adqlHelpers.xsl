<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:util="http://apache.org/xsp/util/2.0"
	xmlns:jpath="http://apache.org/xsp/jpath/1.0" >

<!--
  <xsl:template name="noCR">
     <xsl:param name="text"/>
     <xsl:choose>
	<xsl:when test='contains($text, "&#10;")'>
	  <xsl:value-of select='substring-before($text,"&#10;")'/>
	  <xsl:text>~</xsl:text>
	  <xsl:call-template name="noCR">
	     <xsl:with-param name="text"
	    	 select='substring-after($text,"&#10;")'/>
	  </xsl:call-template>
	</xsl:when>
	<xsl:otherwise>
		<xsl:value-of select="$text"/>
	</xsl:otherwise>
     </xsl:choose>
  </xsl:template>
-->
  <xsl:template match="AstroGrid">
       <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="mainarea">
<ag-link href="/astrogrid-portal/mount/datacenter/test-results.css" rel="stylesheet" type="text/css"/>
<ag-link href="/astrogrid-portal/mount/datacenter/test-results.css" rel="stylesheet" type="text/css"/>



<xsl:variable name="tableID">
<xsl:choose>
 <xsl:when test="//*/OneTableID != 'NoSource'">
  <xsl:value-of select="//*/OneTableID"/>
 </xsl:when>
 <xsl:otherwise>null</xsl:otherwise>
</xsl:choose>
</xsl:variable>



<!-- so far so good, this element is detached from others -->

<body>
<div width="100%" id="adqlSection" style="display: show">

<script language="javascript" src="/astrogrid-portal/extras.js">
moo;
</script>
<script language="javascript">
var currentTable = "<xsl:value-of select='/AstroGrid/DQtableID'/>";
var F = new Array();
var X = new Array();
var M = new Array();
var fascia, example, neuExample, explanation, neuExplanation;

<!--
F['this'] = "phrase";
<xsl:text>M['</xsl:text><xsl:value-of select="@name"/>'] = "<xsl:call-template name="noCR"><xsl:with-param name="text" select="agADQLxample"/></xsl:call-template>";
  -->
function closeMe(){
	var scar = parent.document.getElementById('scratchArea');
	scar.style.display = "none";
}

function blueish(diese){
	diese.style.color = "#0000cc";
	diese.style.borderColor = "#0000cc";
}

function blackish(diese){
	diese.style.color = "black";
	diese.style.borderColor = "#ddddff";
}

function closeIt(){
	var scar = document.getElementById('errorArea');
	scar.style.display = "none";
}

function ButtonHelp(text){
	if(text != ""){
		var anchor = parent.document.getElementById('HMarker');
		var xfig = parent.document.getElementById('xfig');
		var yposo = findPosY(anchor) - 8;
		var xposo = findPosX(xfig) - 3;
		explanation = X[text];
		example = M[text];
		example = example.replace(/^\~/, "");
		neuExample = example.replace(/\~/g, "<br />");
		fascia = '<table width="100%" cellpadding="0" cellspacing="0" bgcolor="#dddddd" border="0"><tr valign="top"><td align="left">';
		fascia += '<font color="blue">' + F[text] + '</font>: ';
		fascia += explanation;
		fascia += '</td><td align="right">';
		fascia += '<img onClick="closeMe(this);" src="/astrogrid-portal/CloseWindow.gif"/>';
		fascia += '</td></tr></table>Usage:<br />';
		fascia += neuExample;
		var scar = parent.document.getElementById('scratchArea');
		var refim = parent.document.getElementById('guideImage');
		var qposy = findPosY(refim);
/*		window.status = text;*/
		scar.style.position="absolute";
/*		scar.style.left= "20px";*/
		scar.style.left= xposo+"px";
/*		scar.style.top= "2px";*/
/*		scar.style.top= qposy+"px";*/
		scar.style.top= yposo+"px";
		scar.innerHTML = fascia;
		scar.style.backgroundColor = "#ffffcc";
/*		scar.style.width= "300px";*/

		if(scar.style.display == "none"){
			scar.style.display = "";
		} else {
			scar.style.display = "none";
		}
	}
}

<xsl:for-each select="//*/agADQLkeys/agADQLkey">

<xsl:text>F['</xsl:text><xsl:value-of select="@name"/>'] = "<xsl:value-of select="@show"/>";
<xsl:text>X['</xsl:text><xsl:value-of select="@name"/>'] = "<xsl:value-of select='translate(agADQLxplain, "&#10;", string(" br "))'/>";
<xsl:text>M['</xsl:text><xsl:value-of select="@name"/>'] = "<xsl:value-of select='translate(agADQLxample, "&#10;", "~")'/>";
</xsl:for-each>
</script>

<center>ADQL Helpers</center>
<xsl:choose>
<xsl:when test="//*/profile/adqlHelpStyle = 'verbose'">

<!--
<td class="leftColumn" onMouseOver="blueish(this)"
onMouseOut="blackish(this)" onClick="yTEK('{@show}\040')" align="left">
<span class="agHnd"><xsl:value-of select="@show"/></span>
</td>
<td class="rightColumn" align="right" onmouseover="blueish(this)"
onmouseout="blackish(this)" onClick="ButtonHelp('{@name}')">
<span class="agHlp">...</span>
</td>
<xsl:attribute name="onClick" >yTEK('<xsl:value-of select="@name"/>\040');</xsl:attribute>
<xsl:attribute name="onClick">ButtonHelp('<xsl:value-of select="@name"/>');</xsl:attribute>
-->

<table class="compactb" border="0" cellpadding="0" cellspacing="0">
<xsl:for-each select="../ADQLHelp/agADQLkeys/agADQLkey">
<tr>
<td class="leftColumn" align="left" onMouseOver="blueish(this)"
onMouseOut="blackish(this)" onClick="yTEK('{@paste}\040')" >
<span class="agHnd" ><xsl:value-of select="@show"/></span>
</td>
<td class="rightColumn" align="right"
onmouseover="blueish(this)"
onmouseout="blackish(this)" onClick="ButtonHelp('{@name}')"
>
<span class="agHnd">...</span>
</td>
<td class="paddedColumn">
</td>
<td>
<xsl:value-of select="agADQLxplain"/>
</td>
</tr>
</xsl:for-each>
</table>
</xsl:when>	<!-- That was the verbose mode -->

<xsl:otherwise>	<!-- This is the compact mode -->
<center>
<table><tr><td>
<xsl:for-each select="../ADQLHelp/ADQLlayout/TABLA">
<table class="compactb" bgcolor="{@bgcolor}" border="0" cellpadding="0" cellspacing="1">
<xsl:for-each select="row">
<tr>
<xsl:for-each select="col">
<xsl:variable name="what"><xsl:value-of select="."/></xsl:variable>
<xsl:for-each select="//*/agADQLkeys/agADQLkey">
<xsl:if test="$what = @name">
<td class="leftColumn" onMouseOver="blueish(this)"
onMouseOut="blackish(this)" onClick="yTEK('{@paste}\040')" align="left">
<span class="agHnd"><xsl:value-of select="@show"/></span>
</td>
<td class="rightColumn" align="right" onmouseover="blueish(this)"
onmouseout="blackish(this)" onClick="ButtonHelp('{@name}')">
<span class="agHlp">...</span>
</td>
<td class="paddedColumn">
</td>
</xsl:if>
</xsl:for-each>
</xsl:for-each>
</tr>
</xsl:for-each>
</table>
</xsl:for-each>
<!--
<table class="compact" border="0" cellpadding="0" cellspacing="0">
<tr>
<td class="leftColumn" onMouseOver="blueish(this)"
onMouseOut="blackish(this)" align="left">
<span class="agHnd" onClick="yTEK('FROM\040')">From</span>
</td>
<td class="rightColumn" onMouseOver="blueish(this)"
onMouseOut="blackish(this)" align="right">
<span class="agHlp" onClick="ButtonHelp('FROM')">...</span>
</td>
<td class="paddedColumn">
</td>
<td class="leftColumn" align="left">
<span class="agHnd" onClick="yTEK('FROM\040')">As</span>
</td>
<td class="rightColumn" align="right">
<span class="agHlp" onClick="ButtonHelp('AS')">...</span>
</td>
</tr>
<tr>
<td class="leftColumn" align="left">
<span class="agHnd" onClick="yTEK('FROM\040')">As</span>
</td>
<td class="rightColumn" align="right">
<span class="agHlp" onClick="ButtonHelp('AS')">...</span>
</td>
</tr>
<tr>
<td class="leftColumn" align="left">
<span class="agHnd" onClick="yTEK('FROM\040')">Where</span>
</td>
<td class="rightColumn" align="right">
<span class="agHlp" onClick="ButtonHelp('WHERE')">...</span>
</td>
</tr>
<tr>
<td class="leftColumn" align="left">
<span class="agHnd" onClick="yTEK('FROM\040')">Select</span>
</td>
<td class="rightColumn" align="right">
<span class="agHlp" onClick="ButtonHelp('SELECT')">...</span>
</td>
</tr>
</table>
-->
</td></tr></table>
</center>
</xsl:otherwise>
</xsl:choose>


<!-- ending of the main table in the adql div -->

<!--
</center>
-->
</div>
</body>

  </xsl:template>


  <xsl:template match="QUERYAREA">
       <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="laconic">
<!--
       <xsl:apply-templates/>
-->
  </xsl:template>

  <xsl:template match="ADQLlayout">
  </xsl:template>

  <xsl:template match="ADQLHelp">
  </xsl:template>

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

  <xsl:template match="profile">
  </xsl:template>

  <xsl:template match="ADQLHelp">
  </xsl:template>

  <xsl:template match="Action">
  </xsl:template>

  <xsl:template match="ADQLlayout">
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
