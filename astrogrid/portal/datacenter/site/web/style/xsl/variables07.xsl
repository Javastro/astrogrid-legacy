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
	var hand = parent.document.getElementById("hand").innerHTML;
	var w;
/*	getOzSize();*/
	getParentSize();
	if(hand == "right"){
		riY = 0;
		w = winW - 250;
	} else {
		riY = 0;
		w = 5;
	}
	var h = riY;
/*	if(upper != null){ upperPart = upper; }*/
	if(turgid != null){
		target = turgid;
		target.style.position="absolute";
/*		target.style.left= riX + "px";*/
		target.style.left= w + "px";
		target.style.top= "0px";
		target.style.textAlign="left";
/*		target.style.height = h + "px";*/
		target.style.height = "100px";
		target.style.width= "240px";
		target.style.backgroundColor= "#ffffcc";
/*		alert("target color: " + target.style.backgroundColor);*/
	}
	ogreen = '<b><font color="blue">';
	cgreen = "</font></b>";
	breik = "<br />";
}

function xTEK(i){
var update, old, ass;
old = parent.document.qb_form.adqlQuery.value;
ass = document.getElementById("derriere");
var	aus = ass.value;
if(aus == ""){
	alert("Please give an ALIAS (AS) to the table. eg, T1");
} else {
update = old + ass.value + "." + i ;
parent.document.qb_form.adqlQuery.value = update;
parent.document.qb_form.adqlQuery.focus();
}
}

function xTEK2(i){
var update, old, ass;
old = parent.document.qb_form.adqlQuery.value;
ass = document.getElementById("derriere");
var	aus = ass.value;
if(aus == ""){
	alert("Please give an ALIAS (AS) to the table. eg, T1");
} else {
/*update = old + "FROM " + i + " AS " + ass.value;*/
update = old + i + " AS " + ass.value + " ";
parent.document.qb_form.adqlQuery.value = update;
parent.document.qb_form.adqlQuery.focus();
}
}

function cabc(linkObj, name, ucd, units, explanation){
/*	var afgColour = "#000066";*/
/*	var vfgColour = "#000000";*/
/*	var abgColour = "#ddddff";*/
/*	var vbgColour = "#ffffff";*/
	linkObj.style.backgroundColor = abgColour;
	linkObj.style.color = afgColour;
	newtext = name + " | " + ogreen + units + cgreen + " | " + breik;
/*	newtext = name + " | " + units + " |\n";*/
	newtext += ucd + breik + explanation;
	if(target != null){
/*		target.firstChild.nodeValue = newtext;*/
/*		target.style.backgroundColor= "#ffffcc";*/
/*		target.backgroundColor= "#ffffcc";*/
		target.innerHTML = newtext;
		target.style.display = "";
/*		alert("target color: " + target.style.backgroundColor);*/
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
</script>


<!--
<body style="font-size: 90%" onLoad="locateTarget('MDsummary')" >
-->


<xsl:choose>
<xsl:when test="DQtableID != 'null'">
<xsl:variable name="requestedTable"><xsl:value-of select="DQtableID"/></xsl:variable>

<body style="font-size: 90%" >
<script language="javascript">
locateTarget("MDsummary");
</script>
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
<xsl:value-of select="DQtableID2"/>
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


<xsl:for-each select="//*/agResource">
  <xsl:for-each select="agTable">
     <xsl:if test="agName = $requestedTable">
        <xsl:for-each select="agColumn">
<input class="AGwideButton" type="button" onClick="xTEK('{agName}\040')" value=" {agName} " onMouseOver="cabc(this, '{agName}', '{agUCD}', '{agUnit}', '{agDescription}')" onMouseOut="cvbc(this)"/>
        </xsl:for-each>
     </xsl:if>
  </xsl:for-each>
</xsl:for-each>

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
-->


</form>
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
<!--
<li> Load existing queries : you can cut and paste them or <b>load</b> them from
MySpace.<p /></li>
<li> You may add table metadata, to click and paste column names
into the query area.<p /></li>
<li> Add table metadata by either entering a full table identifier above or by
selecting a table interactively.</li>
-->
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
