<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:util="http://apache.org/xsp/util/2.0"
	xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.9"
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
<agComponentTitle>Locate resources in the Virtual Observatory</agComponentTitle>


<ag-link href="/astrogrid-portal/mount/datacenter/test-results.css" rel="stylesheet" type="text/css"/>

<xsl:variable name="RL">
  <xsl:choose>
    <xsl:when test="//*/profile/metadataBox = 'Left'">
left
    </xsl:when>
    <xsl:otherwise>
right
    </xsl:otherwise>
  </xsl:choose>
</xsl:variable>
<!--
-->

<script language="javascript">
var currentTable = "<xsl:value-of select='/AstroGrid/DQtableID'/>";

function closeMe(){
	var scar = document.getElementById('scratchArea');
        scar.style.display = "none";
}

function tipsi(moi, adqlRefer, tipsiName, tipsiImage){
	var tn = document.getElementById(tipsiName);
	var ti = document.getElementById(tipsiImage);
	setHeight(moi,adqlRefer);
	if(tn != null){
		var xp = findPosX(ti)+30;
		var yp = findPosY(ti);
		tn.style.position = "absolute";
		tn.style.left = xp + "px";
		tn.style.top = yp + "px";
		tn.style.display = "";
		tn.style.width  = "400px";
		tn.style.height  = "200px";
		tn.style.backgroundColor  = "white";
	}
}

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

<xsl:variable name="tableID">
<xsl:choose>
 <xsl:when test="//*/OneTableID != 'NoSource'">
  <xsl:value-of select="//*/OneTableID"/>
 </xsl:when>
 <xsl:otherwise>null</xsl:otherwise>
</xsl:choose>
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

<div width="100" class="LargeHint" id="MDsummary" style="display: none">
test
</div>

<table width="100%" cellpadding="3" cellspacing="0" bgcolor="white" border="0px">
<tr>
<xsl:if test="//*/profile/metadataBox = 'Left'">
<td width="250" align="left" id="selMetadata">
<!--
<center>
<span name="lost" 
onClick="popupBrowser('/astrogrid-portal/lean/mount/datacenter/catalogueBrowser.xml')" class="agActionButton" title="Click to select a table agaisnt which to launch a query">Select a Table</span>
</center>
-->
<img src="/astrogrid-portal/x.gif" id="underSelect" width="1" />
</td>
</xsl:if>
<td id="adqlTD" class="activeTab">
<span class="agTitleClass" onClick="openLeftTab()" id="adqlTab">Resource Query Builder (in <a target="Help"
href="http://www.ivoa.net/twiki/bin/view/IVOA/IvoaVOQL">(s)ADQL</a>)</span><br/>
</td>
<!--
<xsl:if test="/AstroGrid/DQtableID != 'null'">
-->
<xsl:if test="//*/OneTableID != 'NoSource'">
<td id="vizyTD" class="inactiveTab">
<span onClick="openMidTab()" class="AGtITLEcLASS" id="vizyTab">
User friendly Table-Query Form
</span>
</td>
<!--
<xsl:if test="$HasRA = 'Yes' and $HasDec = 'Oui'">
<td id="CSTD" class="inactiveTab">
<span onClick="openRightTab()" class="AGtITLEcLASS" id="CSTab">
Cone Search<br />
</span>
</td>
</xsl:if>
-->
</xsl:if>
<xsl:if test="//*/profile/metadataBox = 'Right'">
<td width="250" align="left" id="selMetadata">
<!--
<center><span name="lost"
onClick="popupBrowser('/astrogrid-portal/lean/mount/datacenter/catalogueBrowser.xml')" class="agActionButton" title="Click to select a table agaisnt which to launch a query">Select a Table</span></center>
-->
<img src="/astrogrid-portal/x.gif" id="underSelect" width="1" />
</td>
</xsl:if>
</tr>
</table>

<!-- so far so good, this element is detached from others -->

<div width="100%" id="adqlSection" style="display: show">

<!-- reference image goes here -->
<img src="/astrogrid-portal/x.gif" width="0" id="b4mdframe" />
<!-- table to wrap the adql div -->
<table width="100%" border="0" cellspacing="1" cellpadding="1">
<tr>
<xsl:if test="//*/profile/metadataBox = 'Left'">
<td width="250" valign="bottom">
<!-- Frame goes here -->

<iframe name="metaframe"
src="/astrogrid-portal/bare//mount/datacenter/RBvariables07.i07"
width="100%" height="400" onload="setHeight(this,'b4mdframe')" border="0px" marginHeight="0px" marginWidth="0px"
style="border: solid 2px lime">
mu
</iframe>

</td>
</xsl:if>
<td align="center" valign="top">

<form action="/astrogrid-portal/bare/mount/datacenter/forms/adql" name="main" method="POST" target="_top">
<center>
<table border="0" cellspacing="1" cellpadding="1" bgcolor="#ffffcc">
<tr valign="bottom"><td rowspan="2">
<img src="/astrogrid-portal/x.gif" width="1" id="tipMarker" />
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
<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr><td align="left">
	  <img src="/astrogrid-portal/x.gif" width="1" id="HMarker" />
<span name="lost" 
onClick="popupBrowser('/astrogrid-portal/lean/mount/datacenter/catalogueBrowser.xml')" class="agActionButton" title="Click to select a table agaisnt which to launch a query">Select a Table</span>
</td>
<td align="left">
<input class="agActionButton" name="action" type="submit" value="Load from MySpace" title="Load an existing query from MySpace"/>
</td>
<td align="right">
<input class="agActionButton" name="action" type="submit" value="Save to MySpace"/>
</td>
<td align="right">
<span class="agActionButton">Execute Query</span>
</td>
</tr>
</table>
<!--
	  <input class="agActionButton" name="action" type="submit" value="Load"/>
	  &#160;
	  <input class="agActionButton" name="action" type="submit" value="Save"/>
	  &#160;
	  <input id="myspace-agsl" onFocus="focusit(this)" onBlur="defocusit(this)"  name="myspace-name" type="text"/>
	  <input id="myspace-ivorn" name="myspace-name" type="hidden"/>
	  &#160;
	  <input class="agActionButton" name="myspace-name" type="button" value="Browse MySpace" onclick="popupBrowser('/astrogrid-portal/mount/myspace/myspace-micro?ivorn=myspace-ivorn&amp;agsl=myspace-agsl')"/>
-->

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

<xsl:if test="//*/ErrorMsg != ''">
<div id="errorArea" class="agHint" style="text-align: left; width: 600px; position: absolute; left: 30px; top: 200">
<table width="100%">
<tr><td align="left">
<b>Syntax Error</b></td>
<td align="right">
<img onClick="closeIt();" src="/astrogrid-portal/CloseWindow.gif"/>
</td>
</tr></table>
<xsl:value-of select="//*/ErrorMsg"/>
</div>
</xsl:if>
<!--
<div style="width: 600; position: absolute; left: 20px; top: 300">
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
><img src="/astrogrid-portal/x.gif" width="0" id="xfig"/>Examples:</span>
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

<xsl:if test="//*/profile/hintsOn = 'true' and /AstroGrid/DQtableID = 'null'">
<div id="tipsi" height="100" width="300" style="border: solid 1px orange; display: none">
<table class="compact14" border="0" width="100%">
<tr><td align="left">
Tips tips tips
</td>
<td align="right">
<span class="agActionButton" title="Close this box" onclick="closeDynamic('tipsi')">X</span>
</td>
</tr>
<tr>
<td colspan="2">
Please read the instructions shown to the <xsl:value-of select="$RL"/>. 
</td>
</tr>
</table>
</div>
</xsl:if>

<center>
<div id="marker" style="display: show">
<img src="/astrogrid-portal/x.gif" width="1" id="b4Helpers" />
</div>

<iframe name="adqlHelpFrame"
src="/astrogrid-portal/bare//mount/datacenter/adqlHelpers.i07"
width="100%" height="100" onload="tipsi(this,'b4Helpers', 'tipsi', 'tipMarker')" border="0px" marginHeight="0px" marginWidth="0px"
style="border: solid 2px #ddddff">
moo
</iframe>
<!--
width="100%" height="200" border="0px" marginHeight="0px" marginWidth="0px"
-->

</center>

<!-- ending of the main table in the adql div -->
</td>
<xsl:if test="//*/profile/metadataBox = 'Right'">
<td width="250" valign="bottom">
<!-- Frame goes here -->

<iframe name="metaframe"
src="/astrogrid-portal/bare//mount/datacenter/RBvariables07.i07"
width="100%" height="400" onload="setHeight(this,'b4mdframe')" border="0px" marginHeight="0px" marginWidth="0px"
style="border: solid 2px lime">
mu
</iframe>

</td>
</xsl:if>
</tr></table>

</div>
<!--
<xsl:value-of select="//*/fakeRegistry/IVOA_TABLE/description"/>
<xsl:if test="/AstroGrid/DQtableID != 'null'">
-->

<div width="100%" id="vizySection" style="display: none">
<xsl:if test="//*/OneTableID != 'NoSource'">
<form name="assisted" action="easyADQL" method="post">
<table width="95%" class="compact" cellpadding="0" cellspacing="0" border="0">
<tr style="border-bottom: solid 2px #0000ff; margin: 1px; ">
<td align="left">
<!--
<xsl:value-of select="/Astrogrid/Registry/VODescription/vr:Resource/vr:Description"/>
<xsl:value-of select="/Registry/VODescription/Resource/Description"/>
<xsl:value-of select="/Astrogrid/Registry/vr:VODescription/vr:Resource/vr:Description"/>
<xsl:value-of select="//*/Resource/vr:Description"/>
shhh
-->
<xsl:value-of select="/AstroGrid/Registry/VODescription/Resource/Description"/>
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
<!--
<xsl:for-each select="fakeRegistry/IVOA_TABLE/Columns/FIELD">
<xsl:for-each select="/AstroGrid/Registry/VODescription/Resource/table/Column">
-->
<xsl:for-each select="/AstroGrid/Registry/VODescription/Resource/Table/Column">
<tr valign="top">
<td>
<input type="checkbox" name="show" value="{Name}"/>
</td>
<td>
<xsl:value-of select="Name"/>
</td>
<td>
<input onFocus="focusit(this)" onBlur="defocusit(this)" size="20" name="zelect_{Name}"/>
</td>
<td>
<!--
</td>
<td>
-->
<!--
<xsl:choose>
<xsl:when test="Unit != ''">
<span class="agShowUnits"><xsl:value-of select="Unit"/></span>
</xsl:when>
<xsl:otherwise>
<span class="agShowUnits">_</span>
</xsl:otherwise>
</xsl:choose>
<span class="agShowUCDs"><xsl:value-of select="UCD"/></span>
-->
<span class="agShowExplanation"><xsl:value-of select="Description"/></span>
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
<script language="javascript">
function panel(menuName, msg){
	var minimenu = document.getElementById(menuName);
	var figtag = "fig" + msg;
	var refim = document.getElementById(figtag);
	var vposy = findPosY(refim);
	var hposy = findPosX(refim);
	minimenu.style.position="absolute";
	minimenu.style.left = hposy + "px";
	minimenu.style.top = vposy + "px";

	if(minimenu.style.display == "none"){
		minimenu.style.display = "";
	} else {
		minimenu.style.display = "none";
	}
}
</script>
<form name="coneSearch" action="ConeSearch" method="post">
Cone search<br />
Using experimental menu<br />

<experimentalMenu title="expo"
  aspect="vertical"
  menuID="firstExp"
  visibleClass="VACTIVA"
  invisibleClass="VINACTIVA"
  visibleHelp="VACTIVH"
  invisibleHelp="VINACTIVH"
  menuBarAspect="vertical"
  menuType="floating"
  menuBarClass="transparent"
  look="anchor"
  width="100"
  height="18"
  containerWidtho="150"
  containerPadding="1"
  containerSpacing="0"
  containerBorder="1"
  containerStyle="background: #ccffff; border-color: #000080"
  containerStyly="position: fixed; left: 20px; top: 10px"
  positioning="fixed"
  styly="text-weight: bold"
  style=""
  >
	<!--
	helpURL="http://what.is.this"
	helpText="?"
	-->
<menu_Item   id="File"
	title="File"
	content="File"
	url="http://www.yahoo.com"
	target="_self"
	hover="Ciao bella!"
	type="anchor"
	menuBarAspect="vertical"
	menuBarClass="nicebar"
	look="button"
	>
	<menu_Item  
		id="Save Me"
		title="Save"
		content="Save"
		url="http://www.yahoo.de"
		target="_self"
		hover="Meine Liebe!"
		type="anchor"
		look="button"
		menuBarAspect="vertical"
		menuBarClass="invisible"
		>
	</menu_Item>
	<menu_Item   id="guten Tag" title="Save As"
		content="Save As"
		url="http://www.kakoo.de"
		target="_self"
		hover="Meine Liebe!"
		type="anchor"
		>
	</menu_Item>
	<menu_Item
		id="Properties Tag"
		title="Properties"
		content="Properties"
		url="http://www.msn.com"
		target="other"
		hover="Meine Liebe!"
		type="anchor"
		>
	</menu_Item>
	<menu_Item
		id="Testing Text"
		title="Text.test"
		content="Text.test"
		url="http://www.msn.com"
		target="other"
		hover="Oh mein Got!"
		type="text"
		>
Hola, <input name="hi" size="5" />
	</menu_Item>
</menu_Item>
<menu_Item
	id="there"
	title="Edit"
	content="Edit"
	url="http://www.yahoo.co.uk"
	target="_self"
	hover="Hola hermosura!"
	type="anchor"
	look="button"
	>
	<menu_Item   id="Copy"
		title="Copy"
		content="Copy"
		url="/astrogrid-portal/main/mount/myspace/myspace-explorer"
		target="_self"
		hover="Explore your MySpace "
		look="button"
		type="anchor"
		>
	</menu_Item>
	<menu_Item   id="Paste"
		title="Paste"
		content="Paste"
		url="/astrogrid-portal/main/mount/myspace/myspace-explorer"
		target="_self"
		hover="Explore your MySpace "
		looko="button"
		typeo="anchor"
		>
	</menu_Item>
	<menu_Item   id="Edit"
		title="Edit"
		content="Edit"
		url="/astrogrid-portal/main/mount/myspace/myspace-explorer"
		target="_self"
		hover="Explore your MySpace "
		looko="button"
		typeo="anchor"
		>
	</menu_Item>
	<menu_Item   id="Move"
		title="Move"
		content="Move"
		url="/astrogrid-portal/main/mount/myspace/myspace-explorer"
		target="_self"
		hover="Explore your MySpace "
		looko="button"
		typeo="anchor"
		>
	</menu_Item>
</menu_Item>
</experimentalMenu>
<br />
and testbench for a silly menu
<br />
<div style="display: none" id="minimenu">
<table class="compact" style="border: 1px solid" cellspacing="0"
cellpadding="1" bgcolor="#ffffcc">
<!--
<tr>
<td align="left">File</td>
<td align="left">Edit</td>
</tr>
-->
<tr>
<td align="left">
<div style="background: lime; width:  90px; height: 15px;
text-align: center; vertical-align: baseline; border: 1px solid">Save</div>
</td>
<td align="left">
<div style="background: orange; width:  90px; height: 15px;
text-align: center; vertical-align: baseline; border: 1px solid">Cut</div>
</td>
</tr>
<tr>
<td align="left">
<div style="background: lime; width:  90px; height: 15px; text-align: center; vertical-align: baseline; border: 1px solid">Save As</div>
</td>
<td align="left">
<div style="background: orange; width:  90px; height: 15px;
text-align: center; vertical-align: baseline; border: 1px solid">Paste</div>
</td>
</tr>
<tr>
<td align="left">
<div style="background: lime; width:  90px; height: 15px; text-align: center; vertical-align: baseline; border: 1px solid">Open</div>
</td>
<td align="left">
<div style="background: orange; width:  90px; height: 15px; text-align: center; vertical-align: baseline; border: 1px solid">Insert
Step</div>
</td>
</tr>

</table>
</div>
<br />
<table bgcolor="white" width="100%">
<tr valign="bottom">
<td colspan="2">
<!--
<input type="checkbox" id="selectAll" name="selectAll" value="Alle"
onClick="rotweiller(this, 'show')"/>
<span onClick="cabc(this)">Show all</span>
<br />
-->
Show/Name
</td>
<!--
<td>WHERE (Constraint)</td>
<td>
<span class="agShowUnits">Units</span>
<span class="agShowUCDs">UCD</span>
<span class="agShowExplanation">Explanation</span>
</td>
-->
</tr>
<xsl:for-each select="fakeRegistry/IVOA_TABLE/Columns/FIELD">
<tr valign="top">
<!--
<td>
<input type="checkbox" name="show" value="{@name}"/>
</td>
-->
<td>
<span onClick="panel('firstExp', '{@name}')" style="color: red">
<xsl:value-of select="@name"/>
</span>
<img src="x.gif" width="0" id="fig{@name}"/>
</td>
<!--
<td>
<input onFocus="focusit(this)" onBlur="defocusit(this)" size="20" name="zelect_{@name}"/>
</td>
<td>
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
-->
</tr>
</xsl:for-each>
<tr><td colspan="4">
</td>
</tr>
</table>
</form>
</div>
</xsl:if>


  </xsl:template>


  <xsl:template match="QUERYAREA">
       <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="ADQLlayout">
  </xsl:template>

  <xsl:template match="laconic">
<!-- <xsl:apply-templates/> -->
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

<!--
  <xsl:template match="profile">
  </xsl:template>
  -->

  <xsl:template match="QUERYAREA">
       <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="Action">
  </xsl:template>

  <xsl:template match="Registry">
  </xsl:template>

<!--
  <xsl:template match="ADQLHelp">
  </xsl:template>
  -->

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
