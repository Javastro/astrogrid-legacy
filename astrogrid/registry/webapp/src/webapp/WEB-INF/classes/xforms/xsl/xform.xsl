<?xml version="1.0" encoding="utf-8" ?>
<!--
Project name : xslt2Xforms
Last update : $LastChangedDate$
Version : 0.7.5.$Rev$
Author : Sebastien CRAMATTE <contact@zeninteractif.com> with the help of the community
Site : http://xforms.zeninteractif.com
Abstract : "xslt2xforms" is a cross browser W3C Xforms processor. This xsl stylesheet add the W3C Xforms support in your web project using Xhtml, Javascript and Css.
License: GNU GPL (GNU General Public License. See LICENSE file) for non profit use / Commercial license for business use
Platform  : Independent
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:ev="http://www.w3.org/2001/xml-events" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xforms="http://www.w3.org/2002/xforms" xmlns="http://www.w3.org/1999/xhtml">
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xforms="http://www.w3.org/2002/xforms" xmlns:docbook="http://www.docbook.org/xml/4.4CR1/" xmlns:ev="http://www.w3.org/2001/xml-events" xmlns="http://www.w3.org/1999/xhtml" exclude-result-prefixes="xforms xsd ev docbook xsl">

<xsl:output method="html" version="1.0" encoding="utf-8" indent="yes" omit-xml-declaration="yes" doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd" doctype-public="-//W3//DTD XHTML 1.0 Strict//EN" />
<xsl:strip-space elements="*" />

<!-- main template -->
<xsl:template match="/">
<html>
<head>
<title>xslt2Xforms experiment</title>
<link type="text/css" rel="StyleSheet" href="style/styles.css" />
<link type="text/css" rel="StyleSheet" href="style/xforms.css" />
<script type="text/javascript" src="js/xform.js" ><!--// dummy--></script>
<script type="text/javascript">
function onLoadHandler() {
   <xsl:call-template name="xforms" />
}

window.addEventListener('load',onLoadHandler,false);
</script>
</head>
<body>
<h1>Xforms experiment</h1>
<div id="debug"></div>
<div id="main">
   <xsl:apply-templates />
</div>
</body>
</html>
</xsl:template>


<xsl:template name="id">
   <xsl:choose><xsl:when test="../@id"><xsl:value-of select="../@id" /></xsl:when>
   <xsl:otherwise><xsl:value-of select="generate-id(..)" /></xsl:otherwise></xsl:choose>
</xsl:template>

<!-- Form controls module -->
<!-- generic template -->
<xsl:template match="xforms:*" >
   <xsl:apply-templates select="xforms:*" />
</xsl:template>

<xsl:template match="xforms:input" >
   <xsl:call-template name="label" />
   <div class="control">
   <input type="text" id="{generate-id(.)}" xmlns="http://www.w3.org/2002/xforms">
   <xsl:copy-of select="@*" /></input>
   </div>
</xsl:template>

<!-- secret input field -->
<xsl:template match="xforms:secret">
   <xsl:call-template name="label" />
   <div class="control"><input type="password" id="{generate-id(.)}" xmlns="http://www.w3.org/2002/xforms">
   <xsl:copy-of select="@*" /></input>
   </div>
</xsl:template>

<!-- textarea field -->
<xsl:template match="xforms:textarea">
   <xsl:call-template name="label" />
   <div class="control"><textarea id="{generate-id(.)}" xmlns="http://www.w3.org/2002/xforms">&#160;<xsl:copy-of select="@*" /></textarea>
   </div>
</xsl:template>

<!-- radio buttons -->
<xsl:template match="xforms:select1">
   <xsl:call-template name="label" />
   <div class="control"><xsl:apply-templates select="*" /></div>
</xsl:template>


<xsl:template match="xforms:choices/xforms:item" >
   <xsl:choose>
   <xsl:when test="../../@appearance='full' or ../../@appearance=false()" >
      <xsl:choose>
         <xsl:when test="name(../..)='xforms:select'" >
            <input class="checkbox" type="checkbox" id="{generate-id(.)}" name="{generate-id(../..)}" xmlns="http://www.w3.org/2002/xforms" value="{xforms:value/text()}" ><xsl:copy-of select="../../@*" /></input> <xsl:value-of select="xforms:label/text()" />
         </xsl:when>
         <xsl:when test="name(../..)='xforms:select1'" >
            <input class="radio" type="radio"  id="{generate-id(.)}" name="{generate-id(../..)}" xmlns="http://www.w3.org/2002/xforms" value="{xforms:value/text()}" ><xsl:copy-of select="../../@*" /></input> <xsl:value-of select="xforms:label/text()" />
         </xsl:when>
      </xsl:choose>        
   </xsl:when>
   <xsl:otherwise>
      <option value="{xforms:value/text()}"><xsl:value-of select = "xforms:label/text()" /></option>
   </xsl:otherwise>
   </xsl:choose>
</xsl:template>

<!-- item -->
<xsl:template match="xforms:item" >
   <xsl:choose>
   <xsl:when test="../@appearance='full' or ../@appearance=false()" >
      <xsl:choose>
         <xsl:when test="name(..)='xforms:select'" >
            <input class="checkbox" type="checkbox"  id="{generate-id(.)}" name="{generate-id(..)}" xmlns="http://www.w3.org/2002/xforms" value="{xforms:value/text()}"><xsl:copy-of select="../@*" /></input> <xsl:value-of select="xforms:label/text()" />
         </xsl:when>
         <xsl:when test="name(..)='xforms:select1'" >
            <input class="radio" type="radio"  id="{generate-id(.)}" name="{generate-id(..)}" xmlns="http://www.w3.org/2002/xforms" value="{xforms:value/text()}"><xsl:copy-of select="../@*" /></input> <xsl:value-of select="xforms:label/text()" />
         </xsl:when>
      </xsl:choose>        
   </xsl:when>
   <xsl:otherwise>
      <option value="{xforms:value/text()}"><xsl:value-of select = "xforms:label/text()" /></option>
   </xsl:otherwise>
   </xsl:choose>
</xsl:template>

<!-- radio/checkbox/options group -->
<xsl:template match="xforms:choices">
   <xsl:choose>
   <xsl:when test="../@appearance='full' or ../@appearance=false()" >
      <fieldset>
      <xsl:if test="xforms:label"><legend><xsl:value-of select = "xforms:label/text()" /></legend></xsl:if>
         <xsl:apply-templates select="*" />
      </fieldset>
   </xsl:when>
   <xsl:otherwise>
      <optgroup label="{xforms:label/text()}">
         <xsl:apply-templates select="*" />
      </optgroup>
   </xsl:otherwise>
   </xsl:choose>
</xsl:template>

<!-- checkboxes -->
<xsl:template match="xforms:select|xforms:select[@appearance='full']">
   <xsl:call-template name="label" />
   <div class="control"><xsl:apply-templates select="*" /></div>
</xsl:template>

<!-- multilines box-->
<xsl:template match="xforms:select[@appearance='compact']|xforms:select1[@appearance='compact']">
    <xsl:call-template name="label" />
    <div class="control"><select id="{generate-id(.)}" size="4" multiple="true" xmlns="http://www.w3.org/2002/xforms"><xsl:copy-of select="@*" />
    <xsl:apply-templates select="*" />
    </select></div>
</xsl:template>

<xsl:template match="xforms:select[@appearance='minimal']">
    <xsl:call-template name="label" />
    <div class="control"><select id="{generate-id(.)}" size="{count(child::xforms:item)}" xmlns="http://www.w3.org/2002/xforms"><xsl:copy-of select="@*" />
        <xsl:apply-templates select="*" />
    </select></div>
</xsl:template>

<!-- combo box -->
<xsl:template match="xforms:select1[@appearance='minimal']">
    <xsl:call-template name="label" />
    <div class="control"><select id="{generate-id(.)}" xmlns="http://www.w3.org/2002/xforms"><xsl:copy-of select="@*" />
        <xsl:apply-templates select="*" />
    </select></div>
</xsl:template>

<!-- file -->
<xsl:template match="xforms:upload">
   <xsl:call-template name="label" />
   <div class="control"><input type="file" id="{generate-id(.)}" xmlns="http://www.w3.org/2002/xforms">
   <xsl:copy-of select="@*" /></input>
   </div>
</xsl:template>

<!-- range -->
<xsl:template match="xforms:range">
   <xsl:call-template name="label" />
   <div class="control">
      <div id="{generate-id(.)}" xmlns="http://www.w3.org/2002/xforms"><xsl:copy-of select="@*" /></div>
   </div>
   <xsl:apply-templates select="*" />  
</xsl:template>

<!-- fieldset -->
<xsl:template match="xforms:group">
   <fieldset id="{generate-id(.)}">
   <xsl:apply-templates select="*" />
   </fieldset>
</xsl:template>

<xsl:template match="xforms:output">
   <xsl:call-template name="label" />
   <div id="{generate-id(.)}" class="control" xmlns="http://www.w3.org/2002/xforms"><xsl:copy-of select="@*" /></div>
</xsl:template>


<!-- trigger -->
<xsl:template match="xforms:trigger">
<button id="{generate-id(.)}"><xsl:copy-of select="@*" /><xsl:value-of select = "xforms:label/text()" /></button>
</xsl:template>

<!-- submit -->
<xsl:template match="xforms:submit">
<button id="{@submission}" ><xsl:copy-of select="@*" /><xsl:value-of select = "xforms:label/text()" /></button>
</xsl:template>

<!-- label -->
<xsl:template name="label">
<xsl:param name="id" select="generate-id(.)" />
<xsl:if test="xforms:label"><label for="{$id}" id="{$id}.label"><xsl:value-of select = "xforms:label/text()" /></label></xsl:if>
</xsl:template>   

<!-- values attributes-->
<xsl:template name="val_attributes" >{<xsl:for-each select="@*"><xsl:choose><xsl:when test="position()=last()" >"<xsl:value-of select="name()" />": "<xsl:value-of select="normalize-space(.)" />"</xsl:when><xsl:otherwise>"<xsl:value-of select="name()" />":"<xsl:value-of select="normalize-space(.)" />",</xsl:otherwise></xsl:choose></xsl:for-each>}</xsl:template>



<!-- serialize and escaping XML datas -->
<xsl:template name="startTag">
<xsl:text disable-output-escaping="yes">&lt;</xsl:text><xsl:value-of select="name()" /> xmlns="<xsl:value-of select="namespace-uri()" />"<xsl:call-template name="attributes" />
<xsl:if test="not(*|text()|comment()|processing-instruction())"> /</xsl:if><xsl:text disable-output-escaping="yes">&gt;</xsl:text>
</xsl:template>

<xsl:template name="endTag">
<xsl:text disable-output-escaping="yes">&lt;/</xsl:text><xsl:value-of select="name()" /><xsl:text disable-output-escaping="yes">&gt;</xsl:text>
</xsl:template>

<xsl:template name="attributes">
<xsl:for-each select="@*">
<xsl:text> </xsl:text>
<xsl:value-of select="name()" />
<xsl:text>="</xsl:text>
<xsl:value-of select="." />
<xsl:text>"</xsl:text>
</xsl:for-each>
</xsl:template>

<xsl:template match="*" mode="escape-xml">
<xsl:call-template name="startTag" />
<xsl:apply-templates mode="escape-xml" />
<xsl:if test="*|text()|comment()|processing-instruction()"><xsl:call-template name="endTag" /></xsl:if>
</xsl:template>


<!-- Xforms actions -->

<xsl:template match="xforms:trigger" mode="actions" >
   // trigger
   <xsl:apply-templates mode="actions" />
</xsl:template>

<xsl:template match="*|text()" mode="actions" >
   <xsl:apply-templates mode="actions" />
</xsl:template>

<xsl:template match="node()" mode="actions" >
   <xsl:variable name="id" ><xsl:call-template name="id" /></xsl:variable>
   new Xevent('<xsl:value-of select="$id" />',<xsl:call-template name="val_attributes" />,Xactions.<xsl:value-of select="substring-after(name(),'xforms:')" />Handler);
</xsl:template>

<xsl:template match="xforms:action/node()" mode="actions">
   Xactions.<xsl:value-of select="substring-after(name(),'xforms:')" />(<xsl:call-template name="val_attributes" />);
</xsl:template>

<xsl:template match="xforms:action" mode="actions">
   <xsl:variable name="id" ><xsl:call-template name="id" /></xsl:variable>
   new Xevent('<xsl:value-of select="$id" />',<xsl:call-template name="val_attributes" />,function (evt) {  
   <xsl:apply-templates mode="actions" />
   alert('click <xsl:value-of select="generate-id(.)" />=<xsl:value-of select="../xforms:label/text()" />');
      }
      );
</xsl:template>

<xsl:template match="xforms:label" mode="actions" />

<!--
<xsl:template name="rebuild"></xsl:template>
<xsl:template name="recalculate"></xsl:template>
<xsl:template name="revalidate"></xsl:template>
<xsl:template name="refresh"></xsl:template>
<xsl:template name="setfocus"></xsl:template>
<xsl:template name="load"></xsl:template>
<xsl:template name="send"></xsl:template>
<xsl:template name="message"></xsl:template>
<xsl:template name="reset"></xsl:template>
-->

<!-- Core model init  -->
<xsl:template name="xforms" >
   xforms = new Xforms();
   <xsl:apply-templates select="//xforms:trigger" mode="actions" />
   <xsl:apply-templates select="//xforms:range" mode="model" />   
   <xsl:apply-templates select="//xforms:hint" mode="model" />
   <xsl:for-each select="//xforms:model">
   <xsl:variable name="pos" select="position()-1"/>
   //### model ###
   xmodel = new Xmodel(<xsl:call-template name="val_attributes"/>);
   //--- instance ---
   <xsl:choose>
   <xsl:when test="xforms:instance/@src=true()">
   var httpRequest = new XMLHttpRequest(); 
   httpRequest.onreadystatechange = function() { 
   if (httpRequest.readyState == 4) {
   switch (httpRequest.status) {
   case 200:
   xmodel.appendInstance(httpRequest.responseXML);
   //--- bind ---
   <xsl:apply-templates select="//xforms:bind" mode="model" /> 
   //--- events ---
   <!-- xsl:apply-templates select="xforms:action" mode="actions" -->   
   //--- submission<xsl:apply-templates select="//xforms:submission" mode="model" />
   //### end of model ###
   xforms.appendModel(xmodel);         
   break;
   default:
   document.dispatchEvent(Xevents['xforms-link-exception']);
   }
   }
   }
   
   httpRequest.open('GET','<xsl:value-of select="xforms:instance/@src" />', true);
   httpRequest.send(null);    
   </xsl:when>
   <xsl:otherwise>
   var xmlDoc = XmlDocument.create();
   xmlDoc.loadXML('<xsl:apply-templates select="xforms:instance/*" mode="escape-xml" />');
   xmodel.appendInstance(xmlDoc);
   //--- bind ---
   <xsl:apply-templates select="//xforms:bind" mode="model" /> 
   //--- events ---
   <!-- xsl:apply-templates select="xforms:action" mode="actions" -->   
   //--- submission<xsl:apply-templates select="//xforms:submission" mode="model" />
   //### end of model ###
   xforms.appendModel(xmodel);         
   </xsl:otherwise>
   </xsl:choose>
   
   </xsl:for-each>

</xsl:template>

<!-- Submission elements init  -->
<xsl:template match="xforms:submission" mode="model" >
   xmodel.appendSubmission(new Xsubmission(<xsl:call-template name="val_attributes" />));</xsl:template>

<!-- Bind init-->
<xsl:template match="xforms:bind" mode="model">
   xmodel.appendBind(new Xbind(<xsl:call-template name="val_attributes" />));</xsl:template>

<!-- Hint elements init -->
<xsl:template match="xforms:hint" mode="model" >
   new Hint(document.getElementById('<xsl:value-of select="generate-id(..)" />.label'),'<xsl:value-of select="text()" />');</xsl:template>

<!-- Range controls init -->
<xsl:template match="xforms:range" mode="model">
   new Slider('<xsl:value-of select="generate-id(.)" />',<xsl:call-template name="val_attributes" />);
</xsl:template>


<!-- XForms repeat module -->
<xsl:template name="repeat" ></xsl:template>
<xsl:template name="insert" ></xsl:template>
<xsl:template name="delete" ></xsl:template>
<xsl:template name="setindex" ></xsl:template>
<xsl:template name="copy" ></xsl:template>
<xsl:template name="itemset" ></xsl:template>

<xsl:template match="xforms:repeat">
<div id="{@id}" name="{@id}" class="repeat">
Repeat
</div>
</xsl:template>
</xsl:stylesheet>