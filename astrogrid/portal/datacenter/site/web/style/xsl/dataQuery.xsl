<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.9"
	xmlns:vs="http://www.ivoa.net/xml/VODataService/v0.4"
	xmlns:exist="http://exist.sourceforge.net/NS/exist"
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
	if(CS != null){ CS.style.display = "none"; }
	if(CSTab != null){ CSTab.className = "AGtITLEcLASS"; }
	if(CSTab != null){ CSTD.className = "inactiveTab"; }
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
	adql.style.display = "none";
	vizy.style.display = "";
	adqlTab.className = "AGtITLEcLASS";
	vizyTab.className = "agTitleClass";
/*	adqlTD.style.backgroundColor = "#e0e0e0";*/
/*	vizyTD.style.backgroundColor = "white";*/
	adqlTD.className = "inactiveTab";
	vizyTD.className = "activeTab";
	var CS = document.getElementById("CSSection");
	var CSTab = document.getElementById("CSTab");
	var CSTD = document.getElementById("CSTD");
	if(CS != null){ CS.style.display = "none"; }
	if(CSTab != null){ CSTab.className = "AGtITLEcLASS"; }
	if(CSTab != null){ CSTD.className = "inactiveTab"; }
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
	adqlTab.className = "AGtITLEcLASS";
	vizyTab.className = "AGtITLEcLASS";
/*	adqlTD.style.backgroundColor = "#e0e0e0";*/
/*	vizyTD.style.backgroundColor = "white";*/
	adqlTD.className = "inactiveTab";
	vizyTD.className = "inactiveTab";
	CS.style.display = "";
	if(CS != null){ CS.style.display = ""; }
	if(CSTab != null){ CSTab.className = "agTitleClass"; }
	if(CSTD != null){ CSTD.className = "activeTab"; }
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

<xsl:variable name="HasAlpha">Ja</xsl:variable>
<xsl:variable name="HasRA">
    <xsl:for-each select="fakeRegistry/IVOA_TABLE/Columns/FIELD">
	<xsl:if test="@ucd = 'POS_EQ_RA_MAIN'">
<xsl:text>Yes</xsl:text>
	</xsl:if>
    </xsl:for-each>
</xsl:variable>

<xsl:variable name="HasDec">
    <xsl:for-each select="fakeRegistry/IVOA_TABLE/Columns/FIELD">
	<xsl:if test="@ucd = 'POS_EQ_DEC_MAIN'">
<xsl:text>Oui</xsl:text>
	</xsl:if>
    </xsl:for-each>
</xsl:variable>

<!--
<div id="itchy" style="position:absolute"><ilayer name="itchy4" bgcolor="#eeeeee">
</div>
</ilayer>
<div height="100px" class="LargeHint" id="scratchArea" style="position: absolute">
-->
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
<xsl:if test="$HasRA = 'Yes' and $HasDec = 'Oui'">
<td id="CSTD" class="inactiveTab">
<span onClick="openRightTab()" class="AGtITLEcLASS" id="CSTab">
Cone Search<br />
<!--
-->
</span>
</td>
</xsl:if>
</xsl:if>
</tr>
</table>

<div width="100%" id="adqlSection" style="display: show">
<form action="/astrogrid-portal/bare/mount/datacenter/forms/adql" name="main" method="POST" target="_top" >
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
	  <img src="/astrogrid-portal/x.gif" width="0" id="HMarker" />
	  <input class="agActionButton" name="action" type="submit" value="Load"/>
	  &#160;
	  <input class="agActionButton" name="action" type="submit" value="Save"/>
	  &#160;
	  <input id="myspace-agsl" onFocus="focusit(this)" onBlur="defocusit(this)"  name="myspace-name" type="text"/>
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

<li>
<p>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td><span class="agHnd" >FROM</span></td><td align="right"><span class="agHlp">...</span></td></tr></table></div></p>

Click on the name in the C&amp;P buttons to paste the name into the
textarea</li>

<li>Use the ellipsis ( ... ) to open/close a help area</li>

</ul>
</agHint>
</td></tr>
</table>

<!--
<div id="exemplar"><ilayer name="exemplar2" bgcolor="#eeeeee">
<div>
-->
<table border="1" class="examplesBar" cellpadding="1" cellspacing="0" width="100%">
<tr><td>
<table border="0" class="examplesBar" cellpadding="0" cellspacing="0"
width="100%">
<tr>
<td class="agInActiveSpan2" align="center" id="exampleExplanationTab0">
<span onclick="openUp('exampleExplanation', 0, 6, 'paddington');"
>Examples:</span>
</td>
<td class="agInActiveSpan2" align="center" id="exampleExplanationTab1">
<span onclick="openUp('exampleExplanation', 1, 6, 'paddington');"
>Cone Search</span>
</td>
<td class="agInActiveSpan2" align="center" id="exampleExplanationTab2">
<span onclick="openUp('exampleExplanation', 2, 6, 'paddington');"
>Example 2</span>
</td>
<td class="agInActiveSpan2" align="center" id="exampleExplanationTab3">
<span onclick="openUp('exampleExplanation', 3, 6, 'paddington');"
>Example 3</span>
</td>
<td class="agInActiveSpan2" align="center" id="exampleExplanationTab4">
<span onclick="openUp('exampleExplanation', 4, 6, 'paddington');"
>Example 4</span>
</td>
<td class="agInActiveSpan2" align="center" id="exampleExplanationTab5">
<span onclick="openUp('exampleExplanation', 5, 6, 'paddington');"
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
<div style="display: none" class="courier" id="exampleExplanationBox0">
The following examples may help you to understand how queries in ADQL
are formed.<p />

Just click on the corresponding tab to see the example.<p />
</div>
<div style="display: none" class="courier" id="exampleExplanationBox1">
<pre>
SELECT * FROM USNO AS u WHERE REGION('CIRCLE J2000 12.34 -1.23 0.01')
which retrieves all the data in that region, or 

SELECT COUNT(*) FROM USNO AS u WHERE REGION('CIRCLE J2000 12.34 -1.23 0.01')
which retrieves a count of the number of sources located in the region
</pre>
</div>
<div style="display: none" class="courier" id="exampleExplanationBox2">
FROM USNO as u SELECT * where u.bmag1 &lt; 12
</div>
<div style="display: none" class="courier" id="exampleExplanationBox3">
FROM USNO as u SELECT * where u.bmag2 &lt; 13
</div>
<div style="display: none" class="courier" id="exampleExplanationBox4">
FROM USNO as u SELECT * where u.bmag2 &lt; 14
</div>
<div style="display: none" class="courier" id="exampleExplanationBox5">
FROM USNO as u SELECT * where u.bmag2 &lt; 15
</div>
</td>
</tr>
</table>
</td></tr>
</table>

<div id="paddington" style="display: show">
<img id="IMpaddington" src="/astrogrid-portal/x.gif" height="40" />
</div>

<!--
</div>
</ilayer>
</div>
-->
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
function closeMe(){
	var scar = document.getElementById('scratchArea');
	scar.style.display = "none";
}

function ButtonHelp(text){
	if(text != ""){
		var anchor = document.getElementById('HMarker');
		var yposo = findPosY(anchor) - 8;
		explanation = X[text];
		example = M[text];
		example = example.replace(/^\~/, "");
		neuExample = example.replace(/\~/g, "<br />");
		fascia = '<table width="100%" cellpadding="0" cellspacing="0" bgcolor="#dddddd" border="0"><tr valign="top"><td align="left">';
		fascia += '<font color="blue">' + F[text] + '</font>: ';
		fascia += explanation;
		fascia += '</td><td align="right">';
		fascia += '<img onClick="closeMe();" src="/astrogrid-portal/CloseWindow.gif"/>';
		fascia += '</td></tr></table>Usage:<br />';
		fascia += neuExample;
		var scar = document.getElementById('scratchArea');
		var refim = document.getElementById('guideImage');
		var qposy = findPosY(refim);
/*		window.status = text;*/
		scar.style.position="absolute";
		scar.style.left= "20px";
/*		scar.style.top= "2px";*/
/*		scar.style.top= qposy+"px";*/
		scar.style.top= yposo+"px";
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

<!--
if (!document.layers)
{
    document.write('<style type="text/css">#reldiv {position:relative;background-color:#ffffff;border:1px solid #000099;}<\/style>');
}

<script type="text/javascript" src="/astrogrid-portal/wz_dragdrop.js">
var i=0;
</script>
-->

<table>
<tr>
<!--
<td>
<img src="/astrogrid-portal/clickNpaste.jpg" width="20px" height="85px"/>
</td>
-->
<td>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td><span class="agHnd" onClick="TEK('FROM\040')">FROM</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('FROM')">...</span></td></tr></table></div>

<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('As\040')">As</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('AS')">...</span></td></tr></table></div>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('WHERE\040')">WHERE</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('WHERE')">...</span></td></tr></table></div>
<div class="AGwSixty" onMouseOver="cabc(this)" onMouseOut="cvbc(this)"><table border="0" cellpadding="0" width="100%" cellspacing="0"><tr><td align="left"><span class="agHnd" onClick="TEK('SELECT\040')">SELECT</span></td><td align="right"><span class="agHlp" onClick="ButtonHelp('SELECT')">...</span></td></tr></table></div>
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
<input onFocus="focusit(this)" onBlur="defocusit(this)" size="20" name="zelect_{@name}"/>
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
<xsl:if test="/AstroGrid/DQtableID != 'null' and $HasRA = 'Yes' and $HasDec = 'Oui'">
<div width="100%" id="CSSection" style="display: none">
<form name="coneSearch" action="ConeSearch" method="post">
Cone search<br />
</form>
</div>
</xsl:if>

<!--
<script type="text/javascript">

SET_DHTML(CURSOR_MOVE, RESIZABLE, NO_ALT, SCROLL, "scratchArea2", "itchy4", "esemplar", "esemplar2");

</script>
-->

  </xsl:template>

  <xsl:template match="agVaribleDisplayFrame">

	<xsl:apply-templates/>

<!--
  <xsl:template match="vr:Resource">
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

function focusit(a){
	a.style.background = "#ffff00";
}

function defocusit(a){
	a.style.background = "#ffffff";
}


<!--
-->

</script>
<xsl:choose>
<xsl:when test="DQtableID != 'null'">
<body style="font-size: 90%" onLoad="locateTarget('helpy')">
<agComponentMessage>Table: <xsl:value-of select="DQtableID"/></agComponentMessage>

       <center>
       <form name="fake">
<table border="2" bgcolor="#FFFFcc">
<tr><td>
<table class="compact">
<tr>
<td align="left">FROM:</td>
<td align="left" colspan="2">
<xsl:value-of select="vs:Name"/>
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
<td align="left"><input onFocus="focusit(this)" onBlur="defocusit(this)"  id="derriere" name="derriere" size="5"/></td>
<td align="right">
<span class="agActionButton">
<xsl:attribute name="title">Click here to paste <xsl:value-of select="DQtableID"/> As .. to the main box</xsl:attribute>
<xsl:attribute name="onClick">xTEK2('<xsl:value-of select="DQtableID"/>');</xsl:attribute>
C&amp;P
</span></td></tr>
</table>
</td></tr></table>

<img width="150px" src="/astrogrid-portal/ClickUndPaste.jpg" border="2" />
<!--
<xsl:for-each select="fakeRegistry/IVOA_TABLE/Columns/FIELD">
<xsl:for-each select="*/vr:Resource/vs:Table/vs:Column">
-->
<!--
<xsl:for-each select="child::node()">
<xsl:value-of select="name"/>
Hi <br />
</xsl:for-each>
<xsl:for-each select="vodescription/Resource/Table/Column">
Hi! Ho!<br />
<xsl:for-each select="vs:Table/vs:Column">
<xsl:for-each select="resultsFromRegistry/exist:result/vr:Resource/vs:Table/vs:Column">
-->
<br />
<xsl:for-each select="//*/vr:Resource/vs:Table/vs:Column">
<input class="AGwideButton" type="button" onClick="xTEK('{Name}\040')" value=" {Name} " onMouseOver="cabc(this, '{Name}', '{vs:UCD}', '{vs:Unit}', '{Description}')" onMouseOut="cvbc(this)"/>
</xsl:for-each>
</form>
</center>

<!--
       <xsl:apply-templates/>
-->
</body>
</xsl:when>
<xsl:otherwise>
<body style="font-size: 90%; background: #ffffcc;" onLoad="locateTarget('helpy')">
<agComponentMessage>No table selected</agComponentMessage>


<br />

<div class="agTip">
<ul>
<li> No table metadata is present in this form.<p /></li>
<li> You can cut and paste old queries or <b>load</b> them from
MySpace.<p /></li>
<li> You may add table metadata, so you can click and paste column names
to the query area.<p /></li>
<li> Add table metadata by either entering a direct table identifier or by
selecting an available table. Use the area above this one to do so.</li>
</ul>
</div>
<!--
<table border="0" width="95%" cellpadding="0" cellspacing="0">
<tr valign="center">
<td rowspan="3" width="20">
<img src="/astrogrid-portal/leftarrow.gif" width="15" /></td>
<td align="left" style="border-left: solid 2px black">
<img src="/astrogrid-portal/x.gif" width="0" height="5" />
</td>
</tr>
<tr valign="center">
<td align="left" style="border-top: solid 2px black; border-bottom: solid 2px
black; border-right: solid 2px black">
This is the query area. You are supposed to type in your query in order to
save it in MySpace and use it in one or more workflows.
</td>
</tr>
<tr valign="center">
<td style="border-left: solid 2px black"><img src="/astrogrid-portal/x.gif"
width="0" height="5" /></td>
</tr>
</table>
-->

<!--
Ha Ha Ha
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
<!--
<center>
<p class="Title">Catalogue Search Results</p>
<table width="90%">
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

-->
  </xsl:template>

  <xsl:template match="resultsFromRegistry">
  </xsl:template>

  <xsl:template match="uniqueID">
  </xsl:template>

  <xsl:template match="vodescription">
<!--
	<xsl:apply-templates/>
	<xsl:apply-templates/>
-->
  </xsl:template>

<!--
<xsl:for-each select="./vs:Table/vs:Column">
HI! <br />
<input class="AGwideButton" type="button" onClick="xTEK('{Name}\040')" value=" {Name} " onMouseOver="cabc(this, '{Name}', '{vs:UCD}', '{vs:Unit}', '{Description}')" onMouseOut="cvbc(this)"/>
</xsl:for-each>
-->
<!--
  <xsl:template match="vr:Resource">
  </xsl:template>

  <xsl:template match="vs:Table">
	<xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="vs:Column">
  </xsl:template>
-->

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
<table border="1" cellpadding="2" cellspacing="1"><tr><td align="left">
	<table border="0" class="blackCompact" id="{$TableNo}">
<tr><td>Table <xsl:value-of select="$tableNo"/></td>
<td colspan="2"><a class="compact" href="DataQuery?src={$tableID}"><xsl:value-of select="@id"/></a> &lt;-- click the link to query this catalogue.</td>
</tr>
	<tr valign="top"><td>Title</td><td colspan="2"><xsl:value-of select="description"/> </td></tr>
	<tr><td>Author</td><td colspan="2"><xsl:value-of select="author"/> </td></tr>
	<tr><td>Bibcode</td><td colspan="2"><xsl:value-of select="bibcode"/> </td></tr>
	<tr><td>Equinox</td><td><xsl:value-of select="equinox"/> </td>
<td rowspan="4" align="right">
<span class="compact" onclick="backToParentDC('{$tableID}')">LUV</span>
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

<!--	leave untouched -->
  <xsl:template match="break">
       <xsl:apply-templates/>
       <br />
  </xsl:template>


  <xsl:template match="@*|node()" priority="-2"><xsl:copy><xsl:apply-templates
  select="@*|node()"/></xsl:copy></xsl:template>
  <xsl:template match="text()" priority="-1"><xsl:value-of select="."/></xsl:template>

</xsl:stylesheet>
