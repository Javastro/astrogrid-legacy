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

<script language="javascript">
function openLeftTab(){
	var adql = document.getElementById("adqlSection");
	var vizy = document.getElementById("vizySection");
	var adqlTab = document.getElementById("adqlTab");
	var vizyTab = document.getElementById("vizyTab");
	var adqlTD = document.getElementById("adqlTD");
	var vizyTD = document.getElementById("vizyTD");
	var CS = document.getElementById("CSSection");
	var CSTab = document.getElementById("CSTab");
	var CSTD = document.getElementById("CSTD");
	adql.style.display = "";
	vizy.style.display = "none";
	vizyTab.className = "AGtITLEcLASS";
	adqlTab.className = "agTitleClass";
/*	vizyTD.style.backgroundColor = "#e0e0e0";*/
/*	adqlTD.style.backgroundColor = "white";*/
	adqlTD.className = "activeTab";
	vizyTD.className = "inactiveTab";
	CS.style.display = "none";
	CSTab.className = "AGtITLEcLASS";
	CSTD.className = "inactiveTab";
	var framy = top.document.getElementById('wholeFrame');
        if(framy != null){
                framy.cols = "*, 240";
        }
}

function openMidTab(){
	var adql = document.getElementById("adqlSection");
	var vizy = document.getElementById("vizySection");
	var adqlTab = document.getElementById("adqlTab");
	var vizyTab = document.getElementById("vizyTab");
	var adqlTD = document.getElementById("adqlTD");
	var vizyTD = document.getElementById("vizyTD");
	var CS = document.getElementById("CSSection");
	var CSTab = document.getElementById("CSTab");
	var CSTD = document.getElementById("CSTD");
	adql.style.display = "none";
	vizy.style.display = "";
	CS.style.display = "none";
	adqlTab.className = "AGtITLEcLASS";
	vizyTab.className = "agTitleClass";
	CSTab.className = "AGtITLEcLASS";
/*	adqlTD.style.backgroundColor = "#e0e0e0";*/
/*	vizyTD.style.backgroundColor = "white";*/
	adqlTD.className = "inactiveTab";
	vizyTD.className = "activeTab";
	CSTD.className = "inactiveTab";
	var topMark = document.getElementById('topMark');
	var lowMark = document.getElementById('lowMark');
	var goBar = document.getElementById('goBar');
	var	topo = findPosY(topMark);
	var	boto = findPosY(lowMark);
/*	window.status = "top: " + topo + " low: " + boto;*/
	var	alto = (boto - topo) - 145;
	goBar.height = alto;
	var framy = top.document.getElementById('wholeFrame');
        if(framy != null){
                framy.cols = "*, 0";
        }
	nofooter();
}

function openRightTab(){
	var adql = document.getElementById("adqlSection");
	var adqlTab = document.getElementById("adqlTab");
	var adqlTD = document.getElementById("adqlTD");
	var vizy = document.getElementById("vizySection");
	var vizyTab = document.getElementById("vizyTab");
	var vizyTD = document.getElementById("vizyTD");
	var CS = document.getElementById("CSSection");
	var CSTab = document.getElementById("CSTab");
	var CSTD = document.getElementById("CSTD");
	adql.style.display = "none";
	vizy.style.display = "none";
	CS.style.display = "";
	adqlTab.className = "AGtITLEcLASS";
	vizyTab.className = "AGtITLEcLASS";
	CSTab.className = "agTitleClass";
/*	adqlTD.style.backgroundColor = "#e0e0e0";*/
/*	vizyTD.style.backgroundColor = "white";*/
	adqlTD.className = "inactiveTab";
	vizyTD.className = "inactiveTab";
	CSTD.className = "activeTab";
	var topMark = document.getElementById('topMark');
	var lowMark = document.getElementById('lowMark');
	var goBar = document.getElementById('goBar');
	var	topo = findPosY(topMark);
	var	boto = findPosY(lowMark);
/*	window.status = "top: " + topo + " low: " + boto;*/
	var	alto = (boto - topo) - 145;
	goBar.height = alto;
	var framy = top.document.getElementById('wholeFrame');
        if(framy != null){
                framy.cols = "*, 0";
        }
	nofooter();
}

</script>

<div height="100px" class="LargeHint" id="scratchArea" style="display: none">
test
</div>

<table width="100%" cellpadding="3" cellspacing="0">
<tr><td id="adqlTD" class="activeTab">
<span class="agTitleClass" onClick="openLeftTab()" id="adqlTab">Data Query Builder (in <a target="Help"
href="http://www.ivoa.net/twiki/bin/view/IVOA/IvoaVOQL">(s)ADQL</a>)</span><br/>
</td>
<xsl:if test="/AstroGrid/DQtableID != 'null'">
<td id="vizyTD" class="inactiveTab">
<span onClick="openMidTab()" class="AGtITLEcLASS" id="vizyTab">
User friendly Table-Query Form
</span>
</td>
<td id="CSTD" class="inactiveTab">
<span onClick="openRightTab()" class="AGtITLEcLASS" id="CSTab">
Cone Search
</span>
</td>
</xsl:if>
</tr>
</table>

<div width="100%" id="adqlSection" style="display: show">
<form action="/astrogrid-portal/main/mount/datacenter/forms/adql" name="main" method="POST">
<center>
<table border="0" cellspacing="1" cellpadding="1" bgcolor="#ffffcc">
<tr valign="bottom"><td rowspan="2">

<agVerticalSubmitArea ID="lefty" onClick="submit()" title="Click me to proceed" height="20"/>
</td><td>
       <!--
style="border-left: solid 1px #69c; border-bottom: solid 1px #069;">
       <xsl:value-of select="QUERYAREA"/>
       -->
       <xsl:apply-templates select="QUERYAREA"/>
</td>
<td>
<table border="0" cellpadding="1" cellspacing="0">
<tr><td align="center"><span onclick="expandTextArea('ag_data-query',5);"
class="agActiveSpan">5</span></td></tr>
<tr><td align="center"><span onclick="expandTextArea('ag_data-query',10);"
class="agActiveSpan">10</span></td></tr>
<tr><td align="center"><span onclick="expandTextArea('ag_data-query',20);"
class="agActiveSpan">20</span></td></tr>
<tr><td align="center"><span onclick="expandTextArea('ag_data-query',30);"
class="agActiveSpan">30</span><img id="guideImage"
src="/astrogrid-portal/x.gif" width="0"/></td></tr>
<tr><td align="center"><span onclick="expandTextArea('ag_data-query',40);"
class="agActiveSpan">40</span></td></tr>
<tr><td align="center"><span onclick="expandTextArea('ag_data-query',50);"
class="agActiveSpan">50</span></td></tr>
<tr><td align="center"><span onclick="clearTextArea('ag_data-query');"
class="agResetButton">Clear</span></td></tr>
</table>
</td>
</tr>

<tr><td align="center">
	  <input class="agActionButton" name="action" type="submit" value="Load"/>
	  &#160;
	  <input class="agActionButton" name="action" type="submit" value="Save"/>
	  &#160;
	  <input id="myspace-agsl" name="myspace-name" type="text"/>
	  <input id="myspace-ivorn" name="myspace-name" type="hidden"/>
	  &#160;
	  <input class="agActionButton" name="myspace-name" type="button" value="Browse MySpace" onclick="popupBrowser('/astrogrid-portal/mount/myspace/myspace-micro?ivorn=myspace-ivorn&amp;agsl=myspace-agsl')"/>

</td><td bgcolor="yellow">
<!-- junkyard
	  <input class="agActionButton" name="myspace-name" type="button" value="Browse MySpace" onclick="javascript:void(window.open('/astrogrid-portal/mount/myspace/myspace-micro?ivorn=myspace-ivorn&amp;agsl=myspace-agsl', 'mySpaceMicro', 'toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, width=300, height=200'))"/>
-->
<agHint id="BoxUsage" iconSize="20">
<ul>
<li>The query should be entered in the white textarea</li>

<li>The textarea can be resized. (10 rows to 50 rows)</li>

<li>Check the examples for guidance</li>

<li>Use the "Click &amp; Paste" buttons located below (for ADQL) and to the
right for the variables in the catalogue/table</li>

<li>Click on the name in the C&amp;P buttons to paste the name into the
textarea</li>

<li>Click on the '...' to get help</li>

</ul>
</agHint>
</td></tr>
</table>

<table border="1" class="examplesBar" cellpadding="1" cellspacing="0" width="100%">
<tr><td>
<table border="0" class="examplesBar" cellpadding="0" cellspacing="0"
width="100%">
<tr>
<td class="agInActiveSpan2" align="center" id="exampleExplanationTab0">
<span onclick="openUp('exampleExplanation', 0, 6);"
>Examples</span>
</td>
<td class="agInActiveSpan2" align="center" id="exampleExplanationTab1">
<span onclick="openUp('exampleExplanation', 1, 6);"
>Cone Search</span>
</td>
<td class="agInActiveSpan2" align="center" id="exampleExplanationTab2">
<span onclick="openUp('exampleExplanation', 2, 6);"
>Example 2</span>
</td>
<td class="agInActiveSpan2" align="center" id="exampleExplanationTab3">
<span onclick="openUp('exampleExplanation', 3, 6);"
>Example 3</span>
</td>
<td class="agInActiveSpan2" align="center" id="exampleExplanationTab4">
<span onclick="openUp('exampleExplanation', 4, 6);"
>Example 4</span>
</td>
<td class="agInActiveSpan2" align="center" id="exampleExplanationTab5">
<span onclick="openUp('exampleExplanation', 5, 6);"
>Example 5</span>
</td>
<!--
<td class="agInActiveSpan2" align="center" id="exampleExplanationTab6">
<span onclick="closeAllTabs('exampleExplanation', 6);"
>X</span>
</td>
-->
</tr>
<tr><td colspan="6" class="agActiveSpan2">
<div style="display: none" id="exampleExplanationBox0">
<pre>The following examples may help you to understand how queries in ADQL
are formed.

Just click on the corresponding tab to see the example.

Use the 'X' at the right hand side of the TABS to close
any open examples.

</pre>
</div>
<div style="display: none" id="exampleExplanationBox1">
<pre>
FROM USNO as u SELECT * where u.bmag1 &lt; 11 # this is a comment
this is a second line
</pre>
</div>
<div style="display: none" id="exampleExplanationBox2">
<pre>
FROM USNO as u SELECT * where u.bmag1 &lt; 12
</pre>
</div>
<div style="display: none" id="exampleExplanationBox3">
<pre>
FROM USNO as u SELECT * where u.bmag2 &lt; 13
</pre>
</div>
<div style="display: none" id="exampleExplanationBox4">
<pre>
FROM USNO as u SELECT * where u.bmag2 &lt; 14
</pre>
</div>
<div style="display: none" id="exampleExplanationBox5">
<pre>
FROM USNO as u SELECT * where u.bmag2 &lt; 15
</pre>
</div>
</td>
</tr>
</table>
</td></tr>
</table>
</center>
</form>
<center>

<script language="javascript">
var currentTable = "<xsl:value-of select='/AstroGrid/DQtableID'/>";
var F = new Array();
var X = new Array();
var M = new Array();
var fascia, example, neuExample, explanation, neuExplanation;

F['this'] = "phrase";

<xsl:for-each select="../agADQLkeys/agADQLkey">
<!--
<xsl:variable>
 <xsl:attribute name="name">
<xsl:value-of select="@name"/>
 </xsl:attribute>
<xsl:call-template name="noCR">
	<xsl:with-param name="text" select="agADQLxample"/>
</xsl:call-template>
</xsl:variable>
-->

<xsl:text>F['</xsl:text><xsl:value-of select="@name"/>'] = "<xsl:value-of select="@show"/>";
<xsl:text>X['</xsl:text><xsl:value-of select="@name"/>'] = "<xsl:value-of select='translate(agADQLxplain, "&#10;", string(" br "))'/>";
<xsl:text>M['</xsl:text><xsl:value-of select="@name"/>'] = "<xsl:value-of select='translate(agADQLxample, "&#10;", "~")'/>";
</xsl:for-each>
<!--
<xsl:text>M['</xsl:text><xsl:value-of select="@name"/>'] = "<xsl:call-template name="noCR"><xsl:with-param name="text" select="agADQLxample"/></xsl:call-template>";
  -->

function ButtonHelp(text){
	if(text != ""){
		explanation = X[text];
		example = M[text];
		example = example.replace(/^\~/, "");
		neuExample = example.replace(/\~/g, "<br />");
		fascia = '<font color="blue">' + F[text] + '</font>: ';
		fascia += explanation + "<br />Usage:<br />";
		fascia += neuExample;
		var scar = document.getElementById('scratchArea');
		var refim = document.getElementById('guideImage');
		var qposy = findPosY(refim);
/*		window.status = text;*/
		scar.style.position="absolute";
		scar.style.left= "20px";
		scar.style.top= qposy+"px";
		scar.innerHTML = fascia;
/*		scar.style.width= "300px";*/

		if(scar.style.display == "none"){
			scar.style.display = "";
		} else {
			scar.style.display = "none";
		}
	}
}


</script>

<table>
<tr>
<!--
<td>
<img src="/astrogrid-portal/clickNpaste.jpg" width="20px" height="85px"/>
</td>
-->
<td>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer; margin-right: 18px" onClick="TEK('FROM\040')">FROM</span> <span style="cursor: help" onClick="ButtonHelp('FROM')">...</span></div>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer; margin-right: 30px" onClick="TEK('As\040')">As</span> <span style="cursor: help" onClick="ButtonHelp('AS')">...</span></div>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer; margin-right: 3px" onClick="TEK('WHERE\040')">WHERE</span> <span style="cursor: help" onClick="ButtonHelp('WHERE')">...</span></div>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer; margin-right: 8px" onClick="TEK('SELECT\040')">SELECT</span> <span style="cursor: help" onClick="ButtonHelp('SELECT')">...</span></div>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('REGION\040')">REGION</span> <span style="cursor: help" onClick="ButtonHelp('REGION')">...</span></div>
</td>

<td>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Top\040')">Top</span> <span style="cursor: help; margin-left: 20px" onClick="ButtonHelp('TOP')">...</span></div>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Table\040')">Table</span> <span style="cursor: help; margin-left: 10px" onClick="ButtonHelp('TABLE')">...</span></div>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Name\040')">Name</span> <span style="cursor: help; margin-left: 12px" onClick="ButtonHelp('NAME')">...</span></div>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Alias\040')">Alias</span> <span style="cursor: help; margin-left: 15px" onClick="ButtonHelp('ALIAS')">...</span></div>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Circle\040')">Circle</span> <span style="cursor: help; margin-left: 10px" onClick="ButtonHelp('CIRCLE')">...</span></div>
</td>

<td>
<div class="AGwTwoEm" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('(\040')">(</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('LeftParenthesis')">...</span></div>
<div class="AGwTwoEm" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK(')\040')">)</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('RightParenthesis')">...</span></div>
<div class="AGwTwoEm" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('+\040')">+</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('PLUS')">...</span></div>
<div class="AGwTwoEm" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('-\040')">-</span> <span style="cursor: help; margin-left: 5px" onClick="ButtonHelp('MINUS')">...</span></div>
<div class="AGwTwoEm" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('*\040')">*</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('TIMES')">...</span></div>
</td><td>
<div class="AGwThreeEm" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('/\040')">/</span> <span style="cursor: help; margin-left: 10px" onClick="ButtonHelp('OVER')">...</span></div>
<div class="AGwThreeEm" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('=\040')">=</span> <span style="cursor: help; margin-left: 9px" onClick="ButtonHelp('EQUALS')">...</span></div>
<div class="AGwThreeEm" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('&lt;&gt;\040')">&lt;&gt;</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('NOTEQUAL')">...</span></div>
<div class="AGwThreeEm" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('&lt;\040')">&lt;</span> <span style="cursor: help; margin-left: 10px" onClick="ButtonHelp('LESSTHAN')">...</span></div>
<div class="AGwThreeEm" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('&lt;=\040')">&lt;=</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('LESSTHANEQUAL')">...</span></div>
</td><td>
<div style="width: 5em" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('&gt;\040')">&gt;</span> <span style="cursor: help; margin-left: 18px" onClick="ButtonHelp('GREATERTHAN')">...</span></div>
<div style="width: 5em" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('&gt;=\040')">&gt;=</span> <span style="cursor: help; margin-left: 10px" onClick="ButtonHelp('GREATERTHANEQUAL')">...</span></div>
<div style="width: 5em" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('And\040')">And</span> <span style="cursor: help; margin-left: 8px" onClick="ButtonHelp('AND')">...</span></div>
<div style="width: 5em" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Or\040')">Or</span> <span style="cursor: help; margin-left: 14px" onClick="ButtonHelp('OR')">...</span></div>
<div style="width: 5em" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Not\040')">Not</span> <span style="cursor: help; margin-left: 10px" onClick="ButtonHelp('NOT')">...</span></div>
</td>

<td>
<div style="width: 4em" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('SIN\040')">SIN</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('SINE')">...</span></div>
<div style="width: 4em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('COS\040')">COS</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('COSINE')">...</span></div>
<div style="width: 4em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('TAN\040')">TAN</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('TAN')">...</span></div>
<div style="width: 4em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('COT\040')">COT</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('COT')">...</span></div>
<div style="width: 4em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('LOG\040')">LOG</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('LOG')">...</span></div>
</td>

<td>
<div style="width: 6em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('ASIN\040')">ASIN</span> <span style="cursor: help; margin-left: 10px" onClick="ButtonHelp('ARCSINE')">...</span></div>
<div style="width: 6em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('ACOS\040')">ACOS</span> <span style="cursor: help; margin-left: 10px" onClick="ButtonHelp('ARCCOSINE')">...</span></div>
<div style="width: 6em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('ATAN\040')">ATAN</span> <span style="cursor: help; margin-left: 10px" onClick="ButtonHelp('ARCTANGENT')">...</span></div>
<div style="width: 6em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('ATAN2\040')">ATAN2</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('ATAN2')">...</span></div>
<div style="width: 6em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('LOG10\040')">LOG10</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('LOG10')">...</span></div>
</td>

<td>
<div style="width: 5em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('ABS\040')">ABS</span> <span style="cursor: help; margin-left: 8px" onClick="ButtonHelp('ABS')">...</span></div>
<div style="width: 5em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('CEILING\040')">CEIL</span> <span style="cursor: help; margin-left: 8px" onClick="ButtonHelp('CEILING')">...</span></div>
<div style="width: 5em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('FLOOR\040')">FLOOR</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('FLOOR')">...</span></div>
<div style="width: 5em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('EXP\040')">EXP</span> <span style="cursor: help; margin-left: 8px" onClick="ButtonHelp('EXP')">...</span></div>
<div style="width: 5em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('POWER\040')">POW</span> <span style="cursor: help; margin-left: 3px" onClick="ButtonHelp('POWER')">...</span></div>
</td>
</tr>
<tr>

<td colspan="8">
<table width="100%">
<tr>
<td>
<div style="width: 5em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('SQRT\040')">SQRT</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('SQRT')">...</span></div>
</td><td>
<div style="width: 4em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('SQUARE\040')">x^2</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('SQUARE')">...</span></div>
</td><td>
<div style="width: 4em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('MIN\040')">MIN</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('MIN')">...</span></div>
</td><td>
<div style="width: 4em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('AVG\040')">AVG</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('AVG')">...</span></div>
</td><td>
<div style="width: 4em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('MAX\040')">MAX</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('MAX')">...</span></div>
</td><td>
<div style="width: 4em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('SUM\040')">SUM</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('SUM')">...</span></div>
</td><td>
<div style="width: 5em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Sigma\040')">Sigma</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('SIGMA')">...</span></div>
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
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Order\040')">Order</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('ORDER')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('OrderBy\040')">OrderBy</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('ORDERBY')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Direction\040')">Direction</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('DIRECTION')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('DESC\040')">DESC</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('DESC')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('ASC\040')">ASC</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('ASC')">...</span></div>

<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('DISTINCT\040')">DISTINCT</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('DISTINCT')">...</span></div>
</div>

<div style="background-color: blue; padding-top: 2px; padding-bottom: 2px">
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Between\040')">Between</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('BETWEEN')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('NotBetween\040')">NotBetween</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('NOTBETWEEN')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Like\040')">Like</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('LIKE')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('NotLike\040')">NotLike</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('NOTLIKE')">...</span></div>
</div>
</td>
<td>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Aggregate\040')">Aggregate</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('AGGREGATE')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('All\040')">All</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('ALL')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Allow\040')">Allow</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('ALLOW')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Archive\040')">Archive</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('ARCHIVE')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('ArchiveTable\040')">ArchiveTable</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('ARCHIVETABLE')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Atom\040')">Atom</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('ATOM')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Closed\040')">Closed</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('CLOSED')">...</span></div>

<div style="background-color: #880000; padding-top: 2px; padding-bottom: 2px">
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('RAND\040')">RAND</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('RAND')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('ROUND\040')">ROUND</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('ROUND')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('TRUNCATE\040')">TRUNCATE</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('TRUNCATE')">...</span></div>
</div>
</td>
<td>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Column\040')">Column</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('COLUMN')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Compare\040')">Compare</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('COMPARE')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Comparison\040')">Comparison</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('COMPARISON')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('COUNT\040')">COUNT</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('COUNT')">...</span></div>
<div style="background-color: #008800; padding-top: 2px; padding-bottom: 2px">
<div style="width: 10em" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Xmatch\040')">Xmatch</span> <span style="cursor: help" onClick="ButtonHelp('XMATCH')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('PI\040')">PI</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('PI')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('DEGREES\040')">DEGREES</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('DEGREES')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('RADIANS\040')">RADIANS</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('RADIANS')">...</span></div>
</div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Nature\040')">Nature</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('NATURE')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Option\040')">Option</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('OPTION')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Pattern\040')">Pattern</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('PATTERN')">...</span></div>
</td>
<td>

<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Drop\040')">Drop</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('DROP')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Function\040')">Function</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('FUNCTION')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('GroupBy\040')">GroupBy</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('GROUPBY')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Having\040')">Having</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('HAVING')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Include\040')">Include</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('INCLUDE')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Item\040')">Item</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('ITEM')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Math\040')">Math</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('MATH')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Oper\040')">Oper</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('OPER')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('Restrict\040')">Restrict</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('RESTRICT')">...</span></div>
<div style="width: 10em;" class="AGFitButton" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"> <span style="cursor: pointer" onClick="TEK('SelectionList\040')">SelectionList</span> <span style="cursor: help; margin-left: 1px" onClick="ButtonHelp('SELECTIONLIST')">...</span></div>
</td>
</tr>
</table>

</div>
</center>

</div>
<div width="100%" id="vizySection" style="display: none">
<xsl:if test="/AstroGrid/DQtableID != 'null'">
<form name="assisted" action="easyADQL" method="post">
<table width="95%" class="compact" cellpadding="0" cellspacing="0" border="0">
<tr style="border-bottom: solid 2px #0000ff; margin: 1px; ">
<td align="left">
<xsl:value-of select="fakeRegistry/IVOA_TABLE/description"/>
</td>
<td bgcolor="#ffff00" width="30" align="right">
<agHint id="FriendlyFire" iconSize="20">
<ul>
<li>Each variable is represented in a horizontal line</li>

<li>Click the box next to it if you want to retrieve it (SELECT)</li>

<li>Write some condition in the input box if it must satisfy certain
conditions.</li>

<li>Conditions are ANDed.</li>

<li>Once ready, click the green bar on the left anywhere to proceed. </li>

<li>You'll be back to the ADQL form, with the requests you made pasted in
the Query Area</li>
</ul>
</agHint>
</td></tr></table>
<img src="/astrogrid-portal/x.gif" width="0" id="topMark"/>
<table width="100%" bgcolor="white" cellpadding="0" cellspacing="0" border="0">
<tr valign="top" ><td align="center" width="25">

<agVerticalSubmitArea ID="goBar" onClick="makeADQL(this)" title="Click me to proceed" height="20"/>

<!--
<table width="20" cellpadding="0" cellspacing="0" border="0">
<tr><td>
<img align="center" onclick="makeADQL(this)" src="/astrogrid-portal/VerClick.png" width="20" title="Click me to proceed to the checkout point" style="cursor: pointer" id="goBarFix"/>
</td></tr><tr><td>
<img align="center" onclick="makeADQL(this)" src="/astrogrid-portal/SubmitGreen.gif" width="20" title="Click me to proceed to the checkout point" style="cursor: pointer" id="goBar"/>
</td></tr></table>
-->

</td>
<td align="left">
<table class="compact" bgcolor="white" width="100%">
<tr valign="bottom">
<td colspan="2">
<input type="checkbox" id="selectAll" name="selectAll" value="Alle"
onClick="rotweiller(this, 'show')"/>
<span onClick="cabc(this)">Show all</span>
<br />
Show/Name
</td>
<td>WHERE (Constraint)</td>
<td>
<span class="agShowUnits">Units</span>
<span class="agShowUCDs">UCD</span>
<span class="agShowExplanation">Explanation</span>
</td>
</tr>
<xsl:for-each select="fakeRegistry/IVOA_TABLE/Columns/FIELD">
<tr valign="top">
<td>
<input type="checkbox" name="show" value="{@name}"/>
</td>
<td>
<xsl:value-of select="@name"/>
</td>
<td>
<input size="20" name="zelect_{@name}"/>
</td>
<td>
<!--
</td>
<td>
-->
<xsl:choose>
<xsl:when test="@Units != ''">
<span class="agShowUnits"><xsl:value-of select="@Units"/></span>
</xsl:when>
<xsl:otherwise>
<span class="agShowUnits">_</span>
</xsl:otherwise>
</xsl:choose>
<span class="agShowUCDs"><xsl:value-of select="@ucd"/></span>
<span class="agShowExplanation"><xsl:value-of select="Explanation"/></span>
</td>
</tr>
</xsl:for-each>
<tr><td colspan="4">
</td></tr>
</table>
</td></tr>
</table>
<img src="/astrogrid-portal/x.gif" width="0" id="lowMark"/>
</form>
</xsl:if>
</div>
<!--
-->
<div width="100%" id="CSSection" style="display: none">
<xsl:if test="/AstroGrid/DQtableID != 'null'">
<form name="coneSearch" action="ConeSearch" method="post">
Cone oearch
</form>
</xsl:if>
</div>
  </xsl:template>

  <xsl:template match="agVaribleDisplayFrame">

<!--
<xsl:element name="script">
<xsl:attribute name="src">/astrogrid-portal/extras.js</xsl:attribute>
<xsl:attribute name="type">text/javascript</xsl:attribute>
<xsl:attribute name="language">javascript</xsl:attribute>
</xsl:element>
<script src="/astrogrid-portal/extras.js" langage="javascript">
var i=0;
-->

<script langage="javascript">
var afgColour = "#ffff00";
var vfgColour = "#000000";
var abgColour = "#000080";
var vbgColour = "#ffffff";
var target = null;
var upperPart = null;
var newtext, ogreen, cgreen, breik;

function locateTarget(helpy){
	var turgid = parent.varsAid.document.getElementById(helpy);
	var upper = parent.varsAid.document.getElementById("tablePicker");
/*	var turgid = parent.varsAid.document.main.helpy;*/
	if(turgid != null){ target = turgid; }
	if(upper != null){ upperPart = upper; }
	ogreen = '<b><font color="blue">';
	cgreen = "</font></b>";
	breik = "<br />";
}

function xTEK(i){
var update, old, ass;
old = parent.deploy.document.main.adqlQuery.value;
ass = document.getElementById("derriere");
var	aus = ass.value;
if(aus == ""){
	alert("Please give an ALIAS (AS) to the table. eg, T1");
} else {
update = old + ass.value + "." + i ;
parent.deploy.document.main.adqlQuery.value = update;
parent.deploy.document.main.adqlQuery.focus();
}
}

function xTEK2(i){
var update, old, ass;
old = parent.deploy.document.main.adqlQuery.value;
ass = document.getElementById("derriere");
var	aus = ass.value;
if(aus == ""){
	alert("Please give an ALIAS (AS) to the table. eg, T1");
} else {
/*update = old + "FROM " + i + " AS " + ass.value;*/
update = old + i + " AS " + ass.value + " ";
parent.deploy.document.main.adqlQuery.value = update;
parent.deploy.document.main.adqlQuery.focus();
}
}

function cabc(linkObj, name, ucd, units, explanation){
	linkObj.style.background = abgColour;
	linkObj.style.color = afgColour;
	newtext = name + " | " + ogreen + units + cgreen + " | " + breik;
/*	newtext = name + " | " + units + " |\n";*/
	newtext += ucd + breik + explanation;
	if(target != null){
/*		target.firstChild.nodeValue = newtext;*/
		target.innerHTML = newtext;
		target.style.display = "";
		upperPart.style.display = "none";
	}
}

function cvbc(linkObj){
	linkObj.style.background = vbgColour;
	linkObj.style.color = vfgColour;
	if(target != null){
		target.style.display = "none";
		upperPart.style.display = "";
	}
}	
<!--
-->

</script>
<xsl:choose>
<xsl:when test="DQtableID != 'null'">
<body style="font-size: 90%" onLoad="locateTarget('helpy')">
<agComponentTitle>Table: <xsl:value-of select="DQtableID"/></agComponentTitle>

       <center>
       <form name="fake">
<table border="2" bgcolor="#FFFFcc">
<tr><td>
<table class="compact">
<tr>
<td align="left">FROM:</td>
<td align="left" colspan="2">
<xsl:value-of select="DQtableID"/>
<!--
<input name="none" size="20">
<xsl:attribute name="value">
</xsl:attribute>
</input>
-->
</td>
</tr>
<tr>
<td align="right">AS:</td>
<td align="left"><input onFocus="focusit(this)" onBlur="defocusit(this)" id="derriere" name="derriere" size="5"/></td>
<td align="right">
<span class="agActionButton">
<xsl:attribute name="title">Click here to paste <xsl:value-of select="DQtableID"/> As .. to the main box</xsl:attribute>
<xsl:attribute name="onClick">xTEK2('<xsl:value-of select="DQtableID"/>');</xsl:attribute>
C&amp;P
</span></td></tr>
</table>
</td></tr></table>

<img width="150px" src="/astrogrid-portal/ClickUndPaste.jpg" border="2" />
<xsl:for-each select="fakeRegistry/IVOA_TABLE/Columns/FIELD">
<input class="AGwideButton" type="button" onClick="xTEK('{@name}\040')" value=" {@name} " onMouseOver="cabc(this, '{@name}', '{@ucd}', '{@Units}', '{Explanation}')" onMouseOut="cvbc(this)"/>
</xsl:for-each>
</form>
</center>

       <xsl:apply-templates/>
</body>
</xsl:when>
<xsl:otherwise>
<body style="font-size: 90%" onLoad="locateTarget('helpy')">
<agComponentTitle>No table selected</agComponentTitle>

<!--
       <form name="fake">
<table border="0" cellpadding="1" cellspacing="1">
<tr><td bgcolor="#ffffcc">
Load a known table:<br />
<input name="tableID" size="20"/>
</td></tr>

<tr><td bgcolor="#ccffff">
If you don't know which table to use.
<input type="button" name="lost" class="submitButton" value="Select a Table"/>
</td></tr>
</table>
</form>
-->
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

  <xsl:template match="fakeRegistry">
<center>
<p class="Title">Catalogue Search Results</p>
<table width="90%">
<!--
<tr><td>
<span class="Title">Query: <xsl:value-of select="RegQuery"/></span>
</td></tr>
-->
<tr><td>
<span class="Title">Number of selected tables: <xsl:value-of select="NumberOfTables"/></span>
</td></tr>
<tr><td>
<span class="Title">Number of Displayed tables: <xsl:value-of select="NumberOfShownTables"/></span>
</td></tr>
<tr>
<td>
<xsl:apply-templates/>
</td>
</tr>
</table>
</center>

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

  <xsl:template match="Action">
  </xsl:template>

<!--	This is just an example, where a get parameter is used. -->
  <xsl:template match="IVOA_TABLES">
     <xsl:for-each select="IVOA_TABLE">
	<xsl:variable name="tableNo" select="position()"/>
	<xsl:variable name="tableID" select="@id"/>
	<xsl:variable name="TableNo">hideShow<xsl:value-of select="$tableNo"/>T</xsl:variable>
<table border="1" cellpadding="2" cellspacing="1" width="96%"><tr><td align="left">
	<table border="0" class="blackCompact"  width="100%" id="{$TableNo}">
<tr><td>Table <xsl:value-of select="$tableNo"/></td>
<td colspan="2">
<!--
<a class="compact" href="DataQuery?src={$tableID}"><xsl:value-of select="@id"/></a> &lt;__ click the link to query this catalogue.
-->
</td>
</tr>
	<tr valign="top"><td>Title</td><td colspan="2"><xsl:value-of select="description"/> </td></tr>
	<tr><td>Author</td><td colspan="2"><xsl:value-of select="author"/> </td></tr>
	<tr><td>Bibcode</td><td colspan="2"><xsl:value-of select="bibcode"/> </td></tr>
	<tr><td>Equinox</td><td><xsl:value-of select="equinox"/> </td>
<td rowspan="4" align="right">
<!--
<span class="compact" onclick="backToParentDC('{$tableID}')">LUV</span>
<span class="compact" onclick="backToRightFrameDC('{$tableID}')">VUL</span>
-->
<xsl:choose>
<xsl:when test="/getTablesFromRegistry/agReference = 'ShortcutAlley'">
Shortcut...
</xsl:when>
<xsl:otherwise>
<table width="100%">
<tr valign="center"><td align="right">
<img style="cursor: pointer" title="Divert the output to the main window" src="/astrogrid-portal/AGfullScreen.gif" onclick="backToParentDC('{$tableID}')"/>
</td><td align="center" width="30">
OR
</td><td align="left">
<img style="cursor: pointer" title="Divert the output to the right hand size frame" src="/astrogrid-portal/AGRightFrame.gif" onclick="backToRightFrameDC('{$tableID}')"/>
</td>
</tr></table>
</xsl:otherwise>
</xsl:choose>

</td>
</tr>
	<tr><td>Epoch</td><td><xsl:value-of select="epoch"/> </td></tr>
	<tr><td>N-Records</td><td><xsl:value-of select="numberOfRecords"/> </td></tr>
	<tr><td>N-Variables</td><td><xsl:value-of select="numberOfVariables"/> </td></tr>
<xsl:choose>
   <xsl:when test="Columns != ''">
<tr><td colspan="3">
<span id="hideShow{$tableNo}" class="switch" onclick="hideOrShow(this)">[show]</span>
<div style="display: none" id="hideShow{$tableNo}BODY">
	<xsl:apply-templates select="Columns"/>
</div>
</td></tr>
   </xsl:when>
   <xsl:otherwise>
   </xsl:otherwise>
</xsl:choose>
	</table>
</td></tr></table><br />
    </xsl:for-each>
  </xsl:template>

  <xsl:template match="agReference">
  </xsl:template>

<!--	leave untouched -->
  <xsl:template match="break">
       <xsl:apply-templates/>
       <br />
  </xsl:template>


  <xsl:template match="@*|node()" priority="-2"><xsl:copy><xsl:apply-templates
  select="@*|node()"/></xsl:copy></xsl:template>
  <xsl:template match="text()" priority="-1"><xsl:value-of select="."/></xsl:template>

</xsl:stylesheet>
