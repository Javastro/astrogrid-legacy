<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xsp="http://apache.org/xsp"
	xmlns:util="http://apache.org/xsp/util/2.0"
	xmlns:jpath="http://apache.org/xsp/jpath/1.0" >

  <xsl:template match="AstroGrid">
       <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="ProducePage">
<script language="javascript" src="/astrogrid-portal/extras.js">
null;
</script>
<script language="javascript">
var T = new Array();
var D = new Array();
T[0] = "tgen";
T[1] = "tqbu";
T[2] = "twfl";
T[3] = "tjob";
T[4] = "tuts";
T[5] = "tnut";
T[6] = "ttor";

D[0] = "dgen";
D[1] = "dqbu";
D[2] = "dwfl";
D[3] = "djob";
D[4] = "duts";
D[5] = "dnut";
D[6] = "dtor";
var dle = D.length;

function pText(text){
	var update;
	var old = document.shuffle.order.value;
	if(old == ""){
		update = text;
	} else {
		update = old + "; " + text;
	}
	document.shuffle.order.value = update;
	glueButton("shuffled", text);
}

</script>
<style>
.buttonOrder {
	padding-left: 3px;
	padding-right: 3px;
	background-color: #ffffcc;
	font-family: arial, verdana;
	border: solid 1px black
}

.buttonBox {
	padding-right: 10px;
}

</style>
<agOtherWindowTitle>AstroGrid Preferences Tool</agOtherWindowTitle>
<center>
<p />
<xsl:variable name="PROFILER"><xsl:value-of select="//*/PROFILER" /></xsl:variable>
<xsl:choose>
<xsl:when test="//*/PROFILER != 'NONDYNAMIC'">
<table border="1" width="95%" cellpadding="0" cellspacing="0">
<tr><td align="center">
<!--
<iframe name="messagerie" src="http://barbara.star.le.ac.uk/datoz-bin/blankPage" width="100%" height="20" border="0px" marginHeight="0px" marginWidth="0px" style="border: solid 0px white">
-->
<iframe name="messagerie" src="{//*/blankPage}" width="100%" height="20" border="0px" marginHeight="0px" marginWidth="0px" style="border: solid 0px white">
la
</iframe>
</td></tr>
<tr><td>
<table border="0" width="100%" class="compact">
<tr>
<td bgcolor="#ffffcc" id="tgen" onClick="openTag('gen');" align="center"> General </td>
<td bgcolor="#cccccc" id="tqbu" onClick="openTag('qbu');" align="center"> Queries </td>
<td bgcolor="#cccccc" id="twfl" onClick="openTag('wfl');" align="center"> WorkFlows </td>
<td bgcolor="#cccccc" id="tjob" onClick="openTag('job');" align="center"> Jobs </td>
<td bgcolor="#cccccc" id="tuts" onClick="openTag('uts');" align="center"> User Tasks </td>
<td bgcolor="#cccccc" id="tnut" onClick="openTag('nut');" align="center"> New User Tasks </td>
<td bgcolor="#cccccc" id="ttor" onClick="openTag('tor');" align="center"> Tasks' Order</td>
</tr>
<tr valign="top">
<td colspan="7" height="350">
<div id="dgen" style="display: show">
<div class="agHint">
This feature will let you customize certain functions of the portal, for
instance:
<ul>
<li> areas with tips.  </li>
<li> lean or verbose help</li>
<li> location of certain elements</li>
<li> customized "bookmarks", particularly useful as shortcuts for AstroGrid
components</li>
</ul>
</div>

<form method="post" action="{$PROFILER}" target="messagerie">
<input type="hidden" name="act" value="modParams" />
<input type="hidden" name="z">
<xsl:attribute name="value"><xsl:value-of  select="//*/userID" /></xsl:attribute>
</input>
<table width="80%">
<xsl:choose>
<xsl:when test="//*/profile/hintsOn = 'true'">
<tr>
<td align="left">Tips:</td>
<td> <input type="radio" name="PARhintsOn" value="true" checked="checked" /> On</td>
<td> <input type="radio" name="PARhintsOn" value="false" /> Off</td>
</tr>
</xsl:when>
<xsl:otherwise>
<tr>
<td align="left">Tips:</td>
<td> <input type="radio" name="PARhintsOn" value="false" checked="checked" /> Off</td>
<td> <input type="radio" name="PARhintsOn" value="true" /> On</td>
</tr>
</xsl:otherwise>
</xsl:choose>
<tr>
<td colspan="3" align="right">
<input type="submit" class="submitButton" name="Update" value="OK" />
</td>
</tr>
</table>
</form>
</div>
<div id="dqbu" style="display: none">
<!--
Metadata panel: Left | Right vs <xsl:value-of select="//*/profile/metadataBox" /><p />
ADQL helps: Buttons | Verbose<p />
-->
<form method="post" action="{$PROFILER}"
target="messagerie">
<input type="hidden" name="act" value="modParams" />
<input type="hidden" name="z">
<xsl:attribute name="value"><xsl:value-of  select="//*/userID" /></xsl:attribute>
</input>
<table width="80%">

<xsl:choose>
<xsl:when test="//*/profile/metadataBox = 'Right'">
<tr>
<td align="left">MetaData box:</td>
<td> <input type="radio" name="PARmetadataBox" value="Right" checked="checked" /> Right</td>
<td> <input type="radio" name="PARmetadataBox" value="Left" /> Left</td>
</tr>
</xsl:when>
<xsl:otherwise>
<tr>
<td align="left">MetaData box:</td>
<td> <input type="radio" name="PARmetadataBox" value="Left" checked="checked" /> Left</td>
<td> <input type="radio" name="PARmetadataBox" value="Right" /> Right</td>
</tr>
</xsl:otherwise>
</xsl:choose>
<tr />

<xsl:choose>
<xsl:when test="//*/profile/adqlHelpStyle = 'Buttons'">
<tr>
<td align="left">ADQL help style:</td>
<td> <input type="radio" name="PARadqlHelpStyle" value="Buttons" checked="checked" /> Buttons</td>
<td> <input type="radio" name="PARadqlHelpStyle" value="verbose" /> Verbose</td>
</tr>
</xsl:when>
<xsl:otherwise>
<tr>
<td align="left">ADQL help style:</td>
<td> <input type="radio" name="PARadqlHelpStyle" value="verbose" checked="checked" /> Verbose</td>
<td> <input type="radio" name="PARadqlHelpStyle" value="Buttons" /> Buttons</td>
</tr>
</xsl:otherwise>
</xsl:choose>
<tr />

<xsl:choose>
<xsl:when test="//*/profile/mdWindow = 'Buttons'">
<tr>
<td align="left">Show Metadata :</td>
<td> <input type="radio" name="PARmdWindow" value="hide" checked="checked" /> Do not show</td>
<td> <input type="radio" name="PARmdWindow" value="view" /> in separate
window</td>
</tr>
</xsl:when>
<xsl:otherwise>
<tr>
<td align="left">Show Metadata in Window:</td>
<td> <input type="radio" name="PARmdWindow" value="view" checked="checked" /> in separate window</td>
<td> <input type="radio" name="PARmdWindow" value="hide" /> Do not show</td>
</tr>
</xsl:otherwise>
</xsl:choose>
<tr />

<xsl:choose>
<xsl:when test="//*/profile/queryStyle = 'ADQL'">
<tr>
<td align="left">Query style:</td>
<td> <input type="radio" name="PARqueryStyle" value="ADQL" checked="true" /> ADQL first</td>
<td> <input type="radio" name="PARqueryStyle" value="friendly" /> Friendly
version first</td>
</tr>
</xsl:when>
<xsl:otherwise>
<tr>
<td align="left">Query style:</td>
<td> <input type="radio" name="PARqueryStyle" value="friendly" checked="true" /> Friendly version first</td>
<td> <input type="radio" name="PARqueryStyle" value="ADQL" /> ADQL first</td>
</tr>
</xsl:otherwise>
</xsl:choose>

<tr />

<tr>
<td colspan="3" align="right">
<input type="submit" class="submitButton" name="Update" value="OK" />
</td>
</tr>
</table>
</form>
</div>
<div id="dwfl" style="display: none">
Workflow prefs
<form method="post" action="{$PROFILER}" target="messagerie">
<input type="hidden" name="act" value="modParams" />
<input type="hidden" name="z">
<xsl:attribute name="value"><xsl:value-of  select="//*/userID" /></xsl:attribute>
</input>
</form>
</div>
<div id="djob" style="display: none">
Job prefs
<form method="post" action="{$PROFILER}" target="messagerie">
<input type="hidden" name="act" value="modParams" />
<input type="hidden" name="z">
<xsl:attribute name="value"><xsl:value-of  select="//*/userID" /></xsl:attribute>
</input>
</form>
</div>
<div id="duts" style="display: none">
Your already defined tasks
<xsl:for-each select="profile/taskBar/task">
<xsl:if test="fascia != ''">
<form method="post" action="{$PROFILER}" target="messagerie">
<input type="hidden" name="act" value="modTask" />
<input type="hidden" name="z">
<xsl:attribute name="value"><xsl:value-of  select="//*/userID" /></xsl:attribute>
</input>
<input type="hidden" name="tag">
<xsl:attribute name="value"><xsl:value-of  select="tag" /></xsl:attribute>
</input>
<!--
<xsl:for-each select="//*/profile/taskBar/task">
<xsl:value-of  select="fascia" />
-->
<table width="80%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td class="cellLT">
<input name="fascia" size="20" >
<xsl:attribute name="value"><xsl:value-of  select="fascia" /></xsl:attribute>
</input>
</td>
<td width="65%" class="cellBT">
<!--
<span onclick="toggleDiv(this, 'D{fascia}')">more...</span>
-->
</td>
</tr>
<tr><td colspan="2" class="cellLBR">
<div style="display: show">
<xsl:attribute name="id">D<xsl:value-of  select="fascia" /></xsl:attribute>
<table>
<tr>
<td> URL to link: </td>
<td colspan="2"> <input name="link" size="60" >
<xsl:attribute name="value"><xsl:value-of  select="link" /></xsl:attribute>
</input>
</td>
</tr>
<!--
<tr>
<td> Face: </td>
<td>
<input name="fascia" size="20" >
<xsl:attribute name="value"><xsl:value-of  select="fascia" /></xsl:attribute>
</input>
</td>
</tr>
-->
<tr>
<td> Hint: </td>
<td colspan="2">  <input name="hover" size="60" >
<xsl:attribute name="value"><xsl:value-of  select="hover" /></xsl:attribute>
</input>
</td>
</tr>
<tr>
<td> description: </td>
<td colspan="2"> <input name="descri" size="60">
<xsl:attribute name="value"><xsl:value-of  select="description" /></xsl:attribute>
</input>
</td>
</tr>
<tr>
<td> Target: </td>
<td align="left"> <input name="target" size="10" value="{target}"/> </td>
<td align="right">
<input type="submit" class="deleteButton" name="Update" value="Delete" />

<input type="submit" class="submitButton" name="Update" value="Update" />
</td>
</tr>
</table>
</div>

</td></tr></table>
</form>
</xsl:if>
</xsl:for-each>
</div>

<div id="dnut" style="display: none">
<form method="post" action="{$PROFILER}" target="messagerie">
<input type="hidden" name="act" value="addTask" />
<input type="hidden" name="z">
<xsl:attribute name="value"><xsl:value-of  select="userID" /></xsl:attribute>
</input>
Define a shortcut button:<br />
<table>
<tr>
<td> Face: </td>
<td colspan="2"> <input name="fascia" size="20" /> </td>
</tr>
<tr>
<td> URL to link: </td>
<td colspan="2"> <input name="link" size="60" /> </td>
</tr>
<tr>
<td> Hint: </td>
<td colspan="2"> <input name="hover" size="60" /> </td>
</tr>
<tr>
<td> description: </td>
<td colspan="2"> <input name="descri" size="80" />
</td>
</tr>
<tr>
<td> Target: </td>
<td align="left"> <input name="target" size="10" value="top"/> </td>
<td align="right">
<input type="submit" class="submitButton" name="Create" value="Create" />
</td>
</tr>
</table>
</form>
</div>
<div id="dtor" style="display: none">
Task order...<p />
Click the buttons with the task names in the order you want now.

<form method="post" name="shuffle" action="{$PROFILER}" target="messagerie">
<input type="hidden" name="act" value="shuffle" />
<input type="hidden" name="z">
<xsl:attribute name="value"><xsl:value-of  select="//*/userID" /></xsl:attribute>
</input>

<!--
<table class="compact" border="0" cellspacing="0" cellpadding="0">
<tr>
<td class="cellLT">
</td>
</tr>
</table>
-->
<xsl:for-each select="profile/taskBar/task">
<xsl:if test="fascia != ''">
<!--
<span style="padding-right: 10px" onclick="pText('{fascia}')">
<span style="padding-left: 3px; padding-right: 3px; background-color:
#ffffcc; font-family: arial, verdana; border: solid 1px black" ><xsl:value-of  select="fascia" /></span>
</span>
-->
<span class="buttonBox" onclick="pText('{fascia}')">
<span class="buttonOrder"><xsl:value-of  select="fascia" /></span>
</span>
</xsl:if>
</xsl:for-each>

<table width="80%">
<tr><td align="left">
<input type="hidden" name="order" size="90"/>
</td></tr>
<tr><td align="left">
<div id="shuffled">
</div>
</td></tr>
<tr><td align="right">
<input type="submit" class="submitButton" name="Create" value="Make new Order" />
</td></tr>
</table>
</form>
</div>

</td>
</tr>
</table>
</td></tr>
</table>

</xsl:when>
<xsl:otherwise>
<table border="1" width="95%" cellpadding="0" cellspacing="0">
<tr><td align="center">
<span class="compact">Preferences, Non updatable</span>
</td></tr>
<tr><td>
<table border="0" width="100%" class="compact">
<tr>
<td bgcolor="#ffffcc" id="tgen" onClick="openTag('gen');" align="center"> General </td>
<td bgcolor="#cccccc" id="tqbu" onClick="openTag('qbu');" align="center"> Queries </td>
<td bgcolor="#cccccc" id="twfl" onClick="openTag('wfl');" align="center"> WorkFlows </td>
<td bgcolor="#cccccc" id="tjob" onClick="openTag('job');" align="center"> Jobs </td>
<td bgcolor="#cccccc" id="tuts" onClick="openTag('uts');" align="center"> User Tasks </td>
<td bgcolor="#cccccc" id="tnut" onClick="openTag('nut');" align="center"> New User Tasks </td>
<td bgcolor="#cccccc" id="ttor" onClick="openTag('tor');" align="center"> Tasks' Order</td>
</tr>
<tr valign="top">
<td colspan="7" height="350">
<div id="dgen" style="display: show">
<div class="agHint">
This feature will let you customize certain functions of the portal, for
instance:
<ul>
<li> areas with tips.  </li>
<li> lean or verbose help</li>
<li> location of certain elements</li>
<li> customized "bookmarks", particularly useful as shortcuts for AstroGrid
components</li>
</ul>
</div>

<input type="hidden" name="act" value="modParams" />
<input type="hidden" name="z">
<xsl:attribute name="value"><xsl:value-of  select="//*/userID" /></xsl:attribute>
</input>
<table width="80%">
<xsl:choose>
<xsl:when test="//*/profile/hintsOn = 'true'">
<tr>
<td align="left">Tips:</td>
<td> <input type="radio" name="PARhintsOn" value="true" checked="checked" /> On</td>
<td> <input type="radio" name="PARhintsOn" value="false" /> Off</td>
</tr>
</xsl:when>
<xsl:otherwise>
<tr>
<td align="left">Tips:</td>
<td> <input type="radio" name="PARhintsOn" value="false" checked="checked" /> Off</td>
<td> <input type="radio" name="PARhintsOn" value="true" /> On</td>
</tr>
</xsl:otherwise>
</xsl:choose>
<tr>
<td colspan="3" align="right">
<span class="submitButton">OK</span>
</td>
</tr>
</table>
</div>
<div id="dqbu" style="display: none">
<!--
</form>
<input type="submit" class="submitButton" name="Update" value="OK" />
Metadata panel: Left | Right vs <xsl:value-of select="//*/profile/metadataBox" /><p />
ADQL helps: Buttons | Verbose<p />
-->
<input type="hidden" name="act" value="modParams" />
<input type="hidden" name="z">
<xsl:attribute name="value"><xsl:value-of  select="//*/userID" /></xsl:attribute>
</input>
<table width="80%">

<xsl:choose>
<xsl:when test="//*/profile/metadataBox = 'Right'">
<tr>
<td align="left">MetaData box:</td>
<td> <input type="radio" name="PARmetadataBox" value="Right" checked="checked" /> Right</td>
<td> <input type="radio" name="PARmetadataBox" value="Left" /> Left</td>
</tr>
</xsl:when>
<xsl:otherwise>
<tr>
<td align="left">MetaData box:</td>
<td> <input type="radio" name="PARmetadataBox" value="Left" checked="checked" /> Left</td>
<td> <input type="radio" name="PARmetadataBox" value="Right" /> Right</td>
</tr>
</xsl:otherwise>
</xsl:choose>
<tr />

<xsl:choose>
<xsl:when test="//*/profile/adqlHelpStyle = 'Buttons'">
<tr>
<td align="left">ADQL help style:</td>
<td> <input type="radio" name="PARadqlHelpStyle" value="Buttons" checked="checked" /> Buttons</td>
<td> <input type="radio" name="PARadqlHelpStyle" value="verbose" /> Verbose</td>
</tr>
</xsl:when>
<xsl:otherwise>
<tr>
<td align="left">ADQL help style:</td>
<td> <input type="radio" name="PARadqlHelpStyle" value="verbose" checked="checked" /> Verbose</td>
<td> <input type="radio" name="PARadqlHelpStyle" value="Buttons" /> Buttons</td>
</tr>
</xsl:otherwise>
</xsl:choose>
<tr />

<xsl:choose>
<xsl:when test="//*/profile/mdWindow = 'Buttons'">
<tr>
<td align="left">Show Metadata :</td>
<td> <input type="radio" name="PARmdWindow" value="hide" checked="checked" /> Do not show</td>
<td> <input type="radio" name="PARmdWindow" value="view" /> in separate
window</td>
</tr>
</xsl:when>
<xsl:otherwise>
<tr>
<td align="left">Show Metadata in Window:</td>
<td> <input type="radio" name="PARmdWindow" value="view" checked="checked" /> in separate window</td>
<td> <input type="radio" name="PARmdWindow" value="hide" /> Do not show</td>
</tr>
</xsl:otherwise>
</xsl:choose>
<tr />

<xsl:choose>
<xsl:when test="//*/profile/queryStyle = 'ADQL'">
<tr>
<td align="left">Query style:</td>
<td> <input type="radio" name="PARqueryStyle" value="ADQL" checked="true" /> ADQL first</td>
<td> <input type="radio" name="PARqueryStyle" value="friendly" /> Friendly
version first</td>
</tr>
</xsl:when>
<xsl:otherwise>
<tr>
<td align="left">Query style:</td>
<td> <input type="radio" name="PARqueryStyle" value="friendly" checked="true" /> Friendly version first</td>
<td> <input type="radio" name="PARqueryStyle" value="ADQL" /> ADQL first</td>
</tr>
</xsl:otherwise>
</xsl:choose>

<tr />

<tr>
<td colspan="3" align="right">
<span class="submitButton">OK</span>
</td>
</tr>
</table>
</div>
<div id="dwfl" style="display: none">
Workflow prefs
<input type="hidden" name="act" value="modParams" />
<input type="hidden" name="z">
<xsl:attribute name="value"><xsl:value-of  select="//*/userID" /></xsl:attribute>
</input>
</div>
<div id="djob" style="display: none">
Job prefs
<input type="hidden" name="act" value="modParams" />
<input type="hidden" name="z">
<xsl:attribute name="value"><xsl:value-of  select="//*/userID" /></xsl:attribute>
</input>
</div>
<div id="duts" style="display: none">
Your already defined tasks
<xsl:for-each select="profile/taskBar/task">
<xsl:if test="fascia != ''">
<input type="hidden" name="act" value="modTask" />
<input type="hidden" name="z">
<xsl:attribute name="value"><xsl:value-of  select="//*/userID" /></xsl:attribute>
</input>
<input type="hidden" name="tag">
<xsl:attribute name="value"><xsl:value-of  select="tag" /></xsl:attribute>
</input>
<!--
<xsl:for-each select="//*/profile/taskBar/task">
<xsl:value-of  select="fascia" />
-->
<table width="80%" border="0" cellspacing="0" cellpadding="0">
<tr>
<td class="cellLT">
<input name="fascia" size="20" >
<xsl:attribute name="value"><xsl:value-of  select="fascia" /></xsl:attribute>
</input>
</td>
<td width="65%" class="cellBT">
<!--
<span onclick="toggleDiv(this, 'D{fascia}')">more...</span>
-->
</td>
</tr>
<tr><td colspan="2" class="cellLBR">
<div style="display: show">
<xsl:attribute name="id">D<xsl:value-of  select="fascia" /></xsl:attribute>
<table>
<tr>
<td> URL to link: </td>
<td colspan="2"> <input name="link" size="60" >
<xsl:attribute name="value"><xsl:value-of  select="link" /></xsl:attribute>
</input>
</td>
</tr>
<!--
<tr>
<td> Face: </td>
<td>
<input name="fascia" size="20" >
<xsl:attribute name="value"><xsl:value-of  select="fascia" /></xsl:attribute>
</input>
</td>
</tr>
-->
<tr>
<td> Hint: </td>
<td colspan="2">  <input name="hover" size="60" >
<xsl:attribute name="value"><xsl:value-of  select="hover" /></xsl:attribute>
</input>
</td>
</tr>
<tr>
<td> description: </td>
<td colspan="2"> <input name="descri" size="60">
<xsl:attribute name="value"><xsl:value-of  select="description" /></xsl:attribute>
</input>
</td>
</tr>
<tr>
<td> Target: </td>
<td align="left"> <input name="target" size="10" value="{target}"/> </td>
<td align="right">
<span class="deleteButton">Delete</span>

<span class="submitButton">Update</span>
</td>
</tr>
</table>
</div>

</td></tr></table>
</xsl:if>
</xsl:for-each>
</div>

<div id="dnut" style="display: none">
<input type="hidden" name="act" value="addTask" />
<input type="hidden" name="z">
<xsl:attribute name="value"><xsl:value-of  select="userID" /></xsl:attribute>
</input>
Define a shortcut button:<br />
<table>
<tr>
<td> Face: </td>
<td colspan="2"> <input name="fascia" size="20" /> </td>
</tr>
<tr>
<td> URL to link: </td>
<td colspan="2"> <input name="link" size="60" /> </td>
</tr>
<tr>
<td> Hint: </td>
<td colspan="2"> <input name="hover" size="60" /> </td>
</tr>
<tr>
<td> description: </td>
<td colspan="2"> <input name="descri" size="80" />
</td>
</tr>
<tr>
<td> Target: </td>
<td align="left"> <input name="target" size="10" value="top"/> </td>
<td align="right">
<span class="submitButton">Create</span>
</td>
</tr>
</table>
</div>
<div id="dtor" style="display: none">
Task order...<p />
Click the buttons with the task names in the order you want now.

<input type="hidden" name="act" value="shuffle" />
<input type="hidden" name="z">
<xsl:attribute name="value"><xsl:value-of  select="//*/userID" /></xsl:attribute>
</input>

<!--
<table class="compact" border="0" cellspacing="0" cellpadding="0">
<tr>
<td class="cellLT">
</td>
</tr>
</table>
-->
<xsl:for-each select="profile/taskBar/task">
<xsl:if test="fascia != ''">
<!--
<span style="padding-right: 10px" onclick="pText('{fascia}')">
<span style="padding-left: 3px; padding-right: 3px; background-color:
#ffffcc; font-family: arial, verdana; border: solid 1px black" ><xsl:value-of  select="fascia" /></span>
</span>
-->
<span class="buttonBox" onclick="pText('{fascia}')">
<span class="buttonOrder"><xsl:value-of  select="fascia" /></span>
</span>
</xsl:if>
</xsl:for-each>

<table width="80%">
<tr><td align="left">
<input type="hidden" name="order" size="90"/>
</td></tr>
<tr><td align="left">
<div id="shuffled">
</div>
</td></tr>
<tr><td align="right">
<span class="submitButton">Make new Order</span>
</td></tr>
</table>
</div>

</td>
</tr>
</table>

</td></tr>
</table>

</xsl:otherwise>
</xsl:choose>

<!--
<table width="95%">
<tr><td align="right">
<span style="font-size: 12px" class="linkLike" onclick="javascript:window.close()">Close</span>
</td></tr>
</table>
-->
</center>
  </xsl:template>

  <xsl:template match="Scenary">
       <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="AGScenarios">
<center>
List of available scenarios
</center>
<table width="90%">
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

<!--	leave untouched -->
  <xsl:template match="break">
       <xsl:apply-templates/>
       <br />
  </xsl:template>

  <xsl:template match="AGScenario">
  </xsl:template>

  <xsl:template match="userID">
  </xsl:template>

  <xsl:template match="origin">
  </xsl:template>

  <xsl:template match="laconic">
  </xsl:template>

  <xsl:template match="@*|node()" priority="-2"><xsl:copy><xsl:apply-templates
  select="@*|node()"/></xsl:copy></xsl:template>
  <xsl:template match="text()" priority="-1"><xsl:value-of select="."/></xsl:template>

</xsl:stylesheet>
