<?xml version="1.0"?>

<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:util="http://apache.org/xsp/util/2.0"
	xmlns:jpath="http://apache.org/xsp/jpath/1.0" >

  <xsl:template match="fakeRegistry">
  </xsl:template>

  <xsl:template match="AstroGrid">
       <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="mainArea">
<script language="javascript">
function openZideFrame(){
var framy = top.document.getElementById('wholeFrame');
if(framy != null){
framy.cols = "200, *";
}
var sframe = top.document.getElementById('sideFrame');
if(sframe != null){
sframe.src = "http://www.yahoo.de";
}
}

function moreOrLess(){
var more = document.getElementById('moreButtons');
var forMore = document.getElementById('askmore').firstChild;
if(more != null){
	if(more.style.display == "none"){
		more.style.display = "";
		forMore.nodeValue = "less...";
	} else {
		more.style.display = "none";
		forMore.nodeValue = "more...";
	}
}
}
</script>
       <center>
<xsl:choose>
<xsl:when test="OneTableID != 'NoSource'">
<span onload="openSideFrame()" onmouseout="cvbc(this)" onMouseOver="cabc(this)"
onclick="switchBetween('adqlORassisted','showADQL', 'Show ADQL form',
'showAssisted', 'Show assisted form')" class="switch"
id="adqlORassisted">Show assisted form</span>
</xsl:when>
<xsl:otherwise>
</xsl:otherwise>
</xsl:choose>
<div id="showADQL" style="display: show">
       <form name="main" action="null" method="post">
       <table width="90%">
       <tr valign="top"><td>
       (s)ADQL Entry area:
       </td></tr>
       <tr valign="top"><td>
       <xsl:apply-templates/>

  <!--
       <xsl:value-of select="QUERYAREA"/>
       </td><td align="right">
<xsl:value-of select="@name"/><break/>
<span class="compact">ADQL Helpers</span>
       -->
       </td></tr>
       <tr><td>
       <table cellpadding="0" cellspacing="3" border="0">
<tr valign="top">
<!--
<td align="center" rowspan="10">
<img src="/cocoon/AGimages/clicky.png" width="40"/>
</td>
-->
<td align="left">
<input class="AGFitButton" type="button" onClick="TEK('FROM\040')" value=" FROM " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('WHERE\040')" value=" WHERE " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('SELECT\040')" value=" SELECT " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Top\040')" value=" Top " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Xmatch\040')" value=" Xmatch " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Table\040')" value=" Table " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Name\040')" value=" Name " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Alias\040')" value=" Alias " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('As\040')" value=" As " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
</td>
</tr><tr>
<td align="left">
<input class="AGFitButton" type="button" onClick="TEK('(\040')" value=" ( " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK(')\040')" value=" ) " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>

<input class="AGFitButton" type="button" onClick="TEK('+\040')" value=" + " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('-\040')" value=" - " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('*\040')" value=" * " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('/\040')" value=" / " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>

<input class="AGFitButton" type="button" onClick="TEK('=\040')" value=" = " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('!=\040')" value=" != " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('&lt;\040')" value="&lt; " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('&lt;=\040')" value=" &lt;= " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('&gt;\040')" value=" &gt; " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('&gt;=\040')" value=" &gt;= " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('And\040')" value=" And " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Or\040')" value=" Or " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Not\040')" value=" Not " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
</td></tr>
<tr><td>
<input class="AGFitButton" type="button" onClick="TEK('SIN\040')" value=" SIN " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('ASIN\040')" value=" ASIN " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('COS\040')" value=" COS " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('ACOS\040')" value=" ACOS " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('TAN\040')" value=" TAN " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('COT\040')" value=" 1/TAN " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('ATAN\040')" value=" ATAN " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('ATAN2\040')" value=" ATAN2 " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('ABS\040')" value=" |x| " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('CEILING\040')" value=" CEILING " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('FLOOR\040')" value=" FLOOR " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('EXP\040')" value=" EXP " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('LOG\040')" value=" LOG " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('LOG10\040')" value=" LOG10 " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('POWER\040')" value=" POWER " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('SQUARE\040')" value=" x^2 " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('SQRT\040')" value=" SQRT " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('MIN\040')" value=" MIN " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('AVG\040')" value=" AVG " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('MAX\040')" value=" MAX " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('SUM\040')" value=" SUM " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Sigma\040')" value=" Sigma " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<span onclick="moreOrLess()" class="fakeLink" id="askmore">more ...</span>
</td></tr>
<tr><td>
<div id="moreButtons" style="display: none">
<input class="AGFitButton" type="button" onClick="TEK('Aggregate\040')" value=" Aggregate " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('All\040')" value=" All " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Allow\040')" value=" Allow " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Archive\040')" value=" Archive " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('ArchiveTable\040')" value=" ArchiveTable " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('ASC\040')" value=" ASC " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Atom\040')" value=" Atom " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Between\040')" value=" Between " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Circle\040')" value=" Circle " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Closed\040')" value=" Closed " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Column\040')" value=" Column " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Compare\040')" value=" Compare " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Comparison\040')" value=" Comparison " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('COUNT\040')" value=" COUNT " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('DEGREES\040')" value=" DEGREES " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('DESC\040')" value=" DESC " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Direction\040')" value=" Direction " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('DISTINCT\040')" value=" DISTINCT " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Drop\040')" value=" Drop " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Function\040')" value=" Function " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('GroupBy\040')" value=" GroupBy " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Having\040')" value=" Having " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Include\040')" value=" Include " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Item\040')" value=" Item " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Like\040')" value=" Like " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Math\040')" value=" Math " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Nature\040')" value=" Nature " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('NotBetween\040')" value=" NotBetween " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('NotLike\040')" value=" NotLike " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Oper\040')" value=" Oper " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Option\040')" value=" Option " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Order\040')" value=" Order " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('OrderBy\040')" value=" OrderBy " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Pattern\040')" value=" Pattern " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('PI\040')" value=" PI " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('RADIANS\040')" value=" RADIANS " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('RAND\040')" value=" RAND " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Restrict\040')" value=" Restrict " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('ROUND\040')" value=" ROUND " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('SelectionList\040')" value=" SelectionList " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('TRUNCATE\040')" value=" TRUNCATE " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
</div>
</td></tr></table>
<!--
<input class="AGFitButton" type="button" onClick="TEK('Integer\040')" value=" Integer " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Real\040')" value=" Real " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('BinaryOperation\040')" value=" BinaryOperation " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Table\040')" value=" Table " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('Trignometric\040')" value=" Trignometric " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
<input class="AGFitButton" type="button" onClick="TEK('UnaryOperation\040')" value=" UnaryOperation " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
-->

       </td></tr>
       <tr><td>

<button type="button" onClick="openSideFrame();" onLoad="openSideFrame();">Open side Frame</button><break/>
Example:<break/>

<textarea name="ignore" cols="50" rows="2" class="compact">
SELECT * FROM USNOB1.0 where (DEC &gt; 85) AND (RA &gt; 12.5 AND RA &lt; 13.0)
</textarea>

       </td></tr>
       <!--
<tr><td colspan="2">
<xsl:choose>
<xsl:when test="OneTableID != 'NoSource'">
These buttons represent the variables in <xsl:value-of select="OneTableID"/><break/>
<xsl:for-each select="fakeRegistry/IVOA_TABLE/Columns/FIELD">
<input class="AGwideButton" type="button" onClick="TEK('{@name}\040')" value=" {@name} " onMouseOver="cabc(this)" onMouseOut="cvbc(this)"/>
</xsl:for-each>
</xsl:when>
<xsl:otherwise>
<input type="button" name="lost" class="submitButton" value="consult the registry"/> If you don't know which catalogue to use.
</xsl:otherwise>
</xsl:choose>
</td></tr>
-->
       </table>
       </form>
       </div>
<xsl:choose>
<xsl:when test="OneTableID != 'NoSource'">
<div id="showAssisted" style="display: none">
<form name="assisted" action="null" method="post">
<table class="compact" width="90%">
<tr>
<td>Show</td>
<td>Name</td>
<td>WHERE (Constraint)</td>
<td>Unit/UCD</td>
<td>Explanation</td>
</tr>
<xsl:for-each select="fakeRegistry/IVOA_TABLE/Columns/FIELD">
<tr valign="top">
<td>
<input type="checkbox" name="select" value="{@name}"/>
</td>
<td>
<xsl:value-of select="@name"/>
</td>
<td>
<input size="20" name="{@name}"/>
</td>
<td>
<xsl:value-of select="@Units"/><break/>
<span class="ultraCompact"><xsl:value-of select="@ucd"/></span>
</td>
<td>
<xsl:value-of select="Explanation"/>
</td>
</tr>
</xsl:for-each>
</table>
</form>
</div>
</xsl:when>
<xsl:otherwise>
</xsl:otherwise>
</xsl:choose>
       </center>
  </xsl:template>

<!--
<input class="AGwideButton" type="button" onClick="TEK('{@name}\040')" value=" {@name} " onMouseOver="mousyOverLink(this,'#0000ff','#ffffcc')" onMouseOut="mouseOutLink(this,'transparent')"/>
-->

  <xsl:template match="thisContent">
  </xsl:template>

  <xsl:template match="dieseContent">
       <center>
       <table width="90%">
       <tr><td>
       <xsl:apply-templates/>
       </td></tr>
       </table>
       </center>
  </xsl:template>

  <xsl:template match="FakeRegistry">
<center>
<p class="Title">Catalogue Search Results</p>
<table width="90%">
<tr><td>
<span class="Title">Query: <xsl:value-of select="RegQuery"/></span>
</td></tr>
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
<tr>
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
	<xsl:variable name="TableNo">hideShow<xsl:value-of select="$tableNo"/></xsl:variable>
	<table border="0" class="blackCompact">
<tr><td>Table <xsl:value-of select="$tableNo"/></td>
<td><a class="compact" href="DataQuery?src={$tableID}"><xsl:value-of select="@id"/></a> &lt;-- click the link to query this catalogue.</td>
</tr>
	<tr valign="top"><td>Title</td><td><xsl:value-of select="description"/> </td></tr>
	<tr><td>Author</td><td><xsl:value-of select="author"/> </td></tr>
	<tr><td>Bibcode</td><td><xsl:value-of select="bibcode"/> </td></tr>
	<tr><td>Equinox</td><td><xsl:value-of select="equinox"/> </td></tr>
	<tr><td>Epoch</td><td><xsl:value-of select="epoch"/> </td></tr>
	<tr><td>N-Records</td><td><xsl:value-of select="numberOfRecords"/> </td></tr>
	<tr><td>N-Variables</td><td><xsl:value-of select="numberOfVariables"/> </td></tr>
<xsl:choose>
   <xsl:when test="Columns != ''">
<tr><td colspan="2">
<span id="hideShow{$tableNo}-switch" class="switch" onclick="toggle('hideShow{$tableNo}')" onMouseOver="mousyOverLink(this,'#0000ff','#ffffcc')" onmouseout="mouseOutLink(this,'transparent')">[show]</span>
<div style="display: none" id="hideShow{$tableNo}">
<!--
<xsl:attribute name="id">
<xsl:value-of select="$TableNo"/>
</xsl:attribute>
<xsl:value-of select="$../tableNo"/>
<span type="help">View Column Information</span><break/>
<hideItem show="View Column Information">
<hiddenLabel>hideShow<xsl:value-of select="$tableNo"/></hiddenLabel>
</hideItem>
-->
	<xsl:apply-templates select="Columns"/>
</div>
</td></tr>
   </xsl:when>
   <xsl:otherwise>
   </xsl:otherwise>
</xsl:choose>
	</table><break/>
     <!--
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
       <xsl:apply-templates/>
	  -->
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
