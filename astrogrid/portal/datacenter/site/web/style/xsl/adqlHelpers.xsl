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
onMouseOut="blackish(this)" onClick="yTEK('{@show}\040')" >
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
onMouseOut="blackish(this)" onClick="yTEK('{@show}\040')" align="left">
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
<xsl:value-of select="@show"/>
</td>
Dot = <xsl:value-of select="."/>
What = <xsl:value-of select="$what"/>
name = <xsl:value-of select="@name"/>
<td><xsl:value-of select="."/></td>
-->

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

<!--
<td>
<img src="/astrogrid-portal/clickNpaste.jpg" width="20px" height="85px"/>
</td>
-->

<!--
<table>
<tr>
<td>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td><span class="agHnd" onClick="yTEK('FROM\040')">FROM</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('FROM')">...</span></td></tr></table></div>

<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="yTEK('As\040')">As</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('AS')">...</span></td></tr></table></div>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="yTEK('WHERE\040')">WHERE</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('WHERE')">...</span></td></tr></table></div>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="yTEK('SELECT\040')">SELECT</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('SELECT')">...</span></td></tr></table></div>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('REGION\040')">REGION</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('REGION')">...</span></td></tr></table></div>
</td>

<td>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Top\040')">Top</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('TOP')">...</span></td></tr></table></div>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Table\040')">Table</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('TABLE')">...</span></td></tr></table></div>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Name\040')">Name</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('NAME')">...</span></td></tr></table></div>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Alias\040')">Alias</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('ALIAS')">...</span></td></tr></table></div>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Circle\040')">Circle</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('CIRCLE')">...</span></td></tr></table></div>
</td>

<td>
<div class="AGwTwoEm" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('(\040')">(</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('LeftParenthesis')">...</span></td></tr></table></div>
<div class="AGwTwoEm" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK(')\040')">)</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('RightParenthesis')">...</span></td></tr></table></div>
<div class="AGwTwoEm" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('+\040')">+</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('PLUS')">...</span></td></tr></table></div>
<div class="AGwTwoEm" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('-\040')">-</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('MINUS')">...</span></td></tr></table></div>
<div class="AGwTwoEm" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('*\040')">*</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('TIMES')">...</span></td></tr></table></div>
</td><td>
<div class="AGwThreeEm" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('/\040')">/</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('OVER')">...</span></td></tr></table></div>
<div class="AGwThreeEm" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('=\040')">=</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('EQUALS')">...</span></td></tr></table></div>
<div class="AGwThreeEm" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('&lt;&gt;\040')">&lt;&gt;</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('NOTEQUAL')">...</span></td></tr></table></div>
<div class="AGwThreeEm" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('&lt;\040')">&lt;</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('LESSTHAN')">...</span></td></tr></table></div>
<div class="AGwThreeEm" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('&lt;=\040')">&lt;=</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('LESSTHANEQUAL')">...</span></td></tr></table></div>
</td><td>
<div style="width: 5em" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('&gt;\040')">&gt;</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('GREATERTHAN')">...</span></td></tr></table></div>
<div style="width: 5em" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('&gt;=\040')">&gt;=</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('GREATERTHANEQUAL')">...</span></td></tr></table></div>
<div style="width: 5em" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('And\040')">And</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('AND')">...</span></td></tr></table></div>
<div style="width: 5em" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Or\040')">Or</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('OR')">...</span></td></tr></table></div>
<div style="width: 5em" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Not\040')">Not</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('NOT')">...</span></td></tr></table></div>
</td>

<td>
<div style="width: 4em" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('SIN\040')">SIN</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('SINE')">...</span></td></tr></table></div>
<div style="width: 4em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('COS\040')">COS</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('COSINE')">...</span></td></tr></table></div>
<div style="width: 4em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('TAN\040')">TAN</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('TAN')">...</span></td></tr></table></div>
<div style="width: 4em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('COT\040')">COT</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('COT')">...</span></td></tr></table></div>
<div style="width: 4em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('LOG\040')">LOG</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('LOG')">...</span></td></tr></table></div>
</td>

<td>
<div style="width: 6em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('ASIN\040')">ASIN</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('ARCSINE')">...</span></td></tr></table></div>
<div style="width: 6em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('ACOS\040')">ACOS</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('ARCCOSINE')">...</span></td></tr></table></div>
<div style="width: 6em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('ATAN\040')">ATAN</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('ARCTANGENT')">...</span></td></tr></table></div>
<div style="width: 6em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('ATAN2\040')">ATAN2</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('ATAN2')">...</span></td></tr></table></div>
<div style="width: 6em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('LOG10\040')">LOG10</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('LOG10')">...</span></td></tr></table></div>
</td>

<td>
<div style="width: 5em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('ABS\040')">ABS</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('ABS')">...</span></td></tr></table></div>
<div style="width: 5em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('CEILING\040')">CEIL</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('CEILING')">...</span></td></tr></table></div>
<div style="width: 5em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('FLOOR\040')">FLOOR</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('FLOOR')">...</span></td></tr></table></div>
<div style="width: 5em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('EXP\040')">EXP</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('EXP')">...</span></td></tr></table></div>
<div style="width: 5em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('POWER\040')">POW</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('POWER')">...</span></td></tr></table></div>
</td>
</tr>
<tr>

<td colspan="8">
<table width="100%">
<tr>
<td>
<div style="width: 5em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('SQRT\040')">SQRT</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('SQRT')">...</span></td></tr></table></div>
</td><td>
<div style="width: 4em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('SQUARE\040')">x^2</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('SQUARE')">...</span></td></tr></table></div>
</td><td>
<div style="width: 4em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('MIN\040')">MIN</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('MIN')">...</span></td></tr></table></div>
</td><td>
<div style="width: 4em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('AVG\040')">AVG</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('AVG')">...</span></td></tr></table></div>
</td><td>
<div style="width: 4em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('MAX\040')">MAX</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('MAX')">...</span></td></tr></table></div>
</td><td>
<div style="width: 4em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('SUM\040')">SUM</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('SUM')">...</span></td></tr></table></div>
</td><td>
<div style="width: 5em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Sigma\040')">Sigma</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('SIGMA')">...</span></td></tr></table></div>
</td>
<td>
<span onclick="moreOrLess()" class="fakeLink" id="askmore">more ...</span>
</td>
</tr>
</table>

</td>
</tr>
</table>
<div style="display: none" id="moreButtons">

<table border="0" cellpadding="1" cellspacing="1">
<tr valign="top">
<td>
<div style="background-color: wheat; padding-top: 2px; padding-bottom: 2px">
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Order\040')">Order</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('ORDER')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('OrderBy\040')">OrderBy</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('ORDERBY')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Direction\040')">Direction</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('DIRECTION')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('DESC\040')">DESC</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('DESC')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('ASC\040')">ASC</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('ASC')">...</span></td></tr></table></div>

<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('DISTINCT\040')">DISTINCT</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('DISTINCT')">...</span></td></tr></table></div>
</div>

<div style="background-color: blue; padding-top: 2px; padding-bottom: 2px">
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Between\040')">Between</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('BETWEEN')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('NotBetween\040')">NotBetween</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('NOTBETWEEN')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Like\040')">Like</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('LIKE')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('NotLike\040')">NotLike</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('NOTLIKE')">...</span></td></tr></table></div>
</div>
</td>
<td>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Aggregate\040')">Aggregate</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('AGGREGATE')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('All\040')">All</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('ALL')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Allow\040')">Allow</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('ALLOW')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Archive\040')">Archive</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('ARCHIVE')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('ArchiveTable\040')">ArchiveTable</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('ARCHIVETABLE')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Atom\040')">Atom</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('ATOM')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Closed\040')">Closed</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('CLOSED')">...</span></td></tr></table></div>

<div style="background-color: #880000; padding-top: 2px; padding-bottom: 2px">
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('RAND\040')">RAND</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('RAND')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('ROUND\040')">ROUND</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('ROUND')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('TRUNCATE\040')">TRUNCATE</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('TRUNCATE')">...</span></td></tr></table></div>
</div>
</td>
<td>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Column\040')">Column</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('COLUMN')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Compare\040')">Compare</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('COMPARE')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Comparison\040')">Comparison</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('COMPARISON')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('COUNT\040')">COUNT</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('COUNT')">...</span></td></tr></table></div>
<div style="background-color: #008800; padding-top: 2px; padding-bottom: 2px">
<div style="width: 10em" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Xmatch\040')">Xmatch</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('XMATCH')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('PI\040')">PI</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('PI')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('DEGREES\040')">DEGREES</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('DEGREES')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('RADIANS\040')">RADIANS</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('RADIANS')">...</span></td></tr></table></div>
</div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Nature\040')">Nature</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('NATURE')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Option\040')">Option</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('OPTION')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Pattern\040')">Pattern</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('PATTERN')">...</span></td></tr></table></div>
</td>
<td>

<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Drop\040')">Drop</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('DROP')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Function\040')">Function</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('FUNCTION')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('GroupBy\040')">GroupBy</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('GROUPBY')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Having\040')">Having</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('HAVING')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Include\040')">Include</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('INCLUDE')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Item\040')">Item</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('ITEM')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Math\040')">Math</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('MATH')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Oper\040')">Oper</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('OPER')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('Restrict\040')">Restrict</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('RESTRICT')">...</span></td></tr></table></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('SelectionList\040')">SelectionList</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('SELECTIONLIST')">...</span></td></tr></table></div>
</td>
</tr>
</table>

</div>
-->

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
