<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <xsl:output method="html"></xsl:output>

  <xsl:template match="/">
    <ag-div>
      <ag-link rel="stylesheet" type="text/css" href="/astrogrid-portal/mount/myspace/xmlTree.css"/>
      <ag-script type="text/javascript" src="/astrogrid-portal/mount/myspace/xmlTree.js"> </ag-script>
      
<!--
      <form action="/astrogrid-portal/main/mount/myspace/myspace-tree.xml">
        MySpace End Point:&#160;<input name="myspace-end-point" type="text" value="http://grendel12.roe.ac.uk:8080/astrogrid-mySpace/services/MySpaceManager"></input>
        <input type="submit" value="Browse"></input>
      </form>
-->
      
      <div>
        <xsl:apply-templates/>
      </div>
    </ag-div>
  </xsl:template>

  <xsl:template match="myspace-tree">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="myspace-item[@type=1]">
    <span class="trigger">
      <xsl:attribute name="onclick">showBranch('<xsl:value-of select="@safe-name"/>');</xsl:attribute>

      <img src="/astrogrid-portal/mount/myspace/closed.gif" alt="toggle">
        <xsl:attribute name="id">I<xsl:value-of select="@safe-name"/></xsl:attribute>
      </img>

      <xsl:value-of select="@item-name"/>
      <br/>
    </span>

    <span class="branch">
      <xsl:attribute name="id"><xsl:value-of select="@safe-name"/></xsl:attribute>
      <xsl:apply-templates/>
      <notag/>
    </span>
  </xsl:template>

  <xsl:template match="myspace-item">
    <img src="/astrogrid-portal/mount/myspace/doc.gif" alt="doc"/>
    <span><xsl:value-of select="@item-name"/></span>
    <br/>
  </xsl:template>
   
   <!-- avoid output of text node 
        with default template -->
  <xsl:template match="@*|node()"/>

</xsl:stylesheet>
