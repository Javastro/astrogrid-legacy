<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:util="http://apache.org/xsp/util/2.0"
	xmlns:jpath="http://apache.org/xsp/jpath/1.0" >

  <xsl:template match="AstroGrid">
       <xsl:apply-templates/>
  </xsl:template>


  <xsl:template match="agVaribleDisplayFrame">

<!--
<script type="text/javascript">
SET_DHTML(CURSOR_MOVE, RESIZABLE, NO_ALT, SCROLL, "scratchArea2", "itchy4", "esemplar", "esemplar2");
</script>
<xsl:element name="script">
<xsl:attribute name="src">/astrogrid-portal/extras.js</xsl:attribute>
<xsl:attribute name="type">text/javascript</xsl:attribute>
<xsl:attribute name="language">javascript</xsl:attribute>
</xsl:element>
<script src="/astrogrid-portal/extras.js" langage="javascript">
var i=0;
-->
<script src="/astrogrid-portal/extras.js" langage="javascript">
null;
</script>

<script langage="javascript">
var afgColour = "#000066";
var vfgColour = "#000000";
var abgColour = "#ddddff";
var vbgColour = "#ffffff";
var target = null;
var upperPart = null;
var newtext, ogreen, cgreen, breik;

function locateTarget(helpy){
	var turgid = parent.document.getElementById(helpy);
/*	var upper = parent.varsAid.document.getElementById("tablePicker");*/
	var refImg = parent.document.getElementById("underSelect");
	getOzSize();
	if(refImg == null){
		var riX = winH-255;
		var riY = 180;
/*		alert("underSelect not found");*/
	} else {
		var riX = findPosX(refImg);
		var riY = findPosY(refImg);
	}
	var w = winH - riX;
	var h = riY;
/*	if(upper != null){ upperPart = upper; }*/
	if(turgid != null){
		target = turgid;
		target.style.position="absolute";
		target.style.left= riX + "px";
		target.style.top= "0px";
		target.style.textAlign="left";
		target.style.height = h + "px";
		target.style.width= "240px";
	}
	ogreen = '<b><font color="blue">';
	cgreen = "</font></b>";
	breik = "<br />";
}

function xTEK(i){
var update, old, ass;
old = parent.document.main.adqlQuery.value;
ass = document.getElementById("derriere");
var	aus = ass.value;
if(aus == ""){
	alert("Please give an ALIAS (AS) to the table. eg, T1");
} else {
update = old + ass.value + "." + i ;
parent.document.main.adqlQuery.value = update;
parent.document.main.adqlQuery.focus();
}
}

function xTEK2(i){
var update, old, ass;
old = parent.document.main.adqlQuery.value;
ass = document.getElementById("derriere");
var	aus = ass.value;
if(aus == ""){
	alert("Please give an ALIAS (AS) to the table. eg, T1");
} else {
/*update = old + "FROM " + i + " AS " + ass.value;*/
update = old + i + " AS " + ass.value + " ";
parent.document.main.adqlQuery.value = update;
parent.document.main.adqlQuery.focus();
}
}

function cabc(linkObj, name, explanation){
	linkObj.style.backgroundColor = abgColour;
	linkObj.style.color = afgColour;
	newtext = name + " ";
/*	newtext = name + " | " + units + " |\n";*/
	newtext += breik + explanation;
	if(target != null){
/*		target.firstChild.nodeValue = newtext;*/
		target.innerHTML = newtext;
		target.style.display = "";
/*		upperPart.style.display = "none";*/
	}
}

function cvbc(linkObj){
	linkObj.style.backgroundColor = vbgColour;
	linkObj.style.color = vfgColour;
	if(target != null){
		target.style.display = "none";
/*		upperPart.style.display = "";*/
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
<body style="font-size: 90%" onLoad="locateTarget('MDsummary')">
<agComponentMessage>Table: <xsl:value-of select="DQtableID"/></agComponentMessage>

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
<td align="left"><input onFocus="focusit(this)" onBlur="defocusit(this)"  id="derriere" name="derriere" size="5" value="T1"/></td>
<td align="right">
<span class="agActionButton">
<xsl:attribute name="title">Click here to paste <xsl:value-of select="DQtableID"/> As .. to the main box</xsl:attribute>
<xsl:attribute name="onClick">xTEK2('<xsl:value-of select="DQtableID"/>');</xsl:attribute>
C&amp;P
</span></td></tr>
</table>
</td></tr></table>

<img width="150px" src="/astrogrid-portal/ClickUndPaste.jpg" border="2" />

<xsl:for-each select="Registry/VODescription/Resource/Table/Column">
<div class="AGRegistryButton" type="button" onClick="xTEK('{Name}\040')" onMouseOver="cabc(this, '{Name}', '{Description}')" onMouseOut="cvbc(this)"><xsl:value-of select="Name"/></div>
</xsl:for-each>
</form>
</center>

<!--
<input class="AGRegistryButton" type="button" onClick="xTEK('{Name}\040')" value=" {Name} " onMouseOver="cabc(this, '{Name}', '{Description}')" onMouseOut="cvbc(this)"/>
-->
       <xsl:apply-templates/>
</body>
</xsl:when>
<xsl:otherwise>
<body style="font-size: 90%; background: #ffffcc;" onLoad="locateTarget('helpy')">
<agComponentMessage>No table selected</agComponentMessage>


<br />

<!--
<li> No table metadata is present in this form.<p /></li>
-->
<div class="agTip">
<ul>
<li> No query is loaded in this page.<p /></li>
<li> Existing queries: you can cut and paste them or <b>load</b> them from
MySpace.<p /></li>
<li> You may add table metadata, to click and paste column names
into the query area.<p /></li>
<li> Add table metadata by either entering a full table identifier above or by
selecting a table interactively.</li>
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

  <xsl:template match="Action">
  </xsl:template>

  <xsl:template match="Registry">
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
