<?xml version="1.0"?>

<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output
      method="xml"
      omit-xml-declaration="yes"/>

  <xsl:template match="/">
    <ag-div>
      <ag-link rel="stylesheet" type="text/css" href="/astrogrid-portal/mount/myspace/xmlTree.css"/>
      <ag-link rel="stylesheet" type="text/css" href="/astrogrid-portal/mount/myspace/myspace.css"/>
      <ag-script type="text/javascript" src="/astrogrid-portal/mount/myspace/xmlTree.js"/>
      <ag-script type="text/javascript" src="/astrogrid-portal/mount/myspace/myspace.js"/>
      
      <div>
        <form id="myspace-explorer-form" action=".">
          <table id="myspace-tree-header">
            <tr>
              <td>IVORN:</td>
              <td style="width:100%"><input name="myspace-ivorn" id="myspace-ivorn" type="text" readonly="true" style="width:100%;border-style:none;"/></td>
            </tr>
          </table>
          
          <input type="button" value="OK" onclick="setParentIVORN();"/>
        </form>
      </div>
      
      <xsl:apply-templates/>
    </ag-div>
  </xsl:template>

  <xsl:template match="myspace-tree">
    <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="myspace-item[@type='folder']">
    <span class="trigger">
      <xsl:attribute name="onclick">showBranch('<xsl:value-of select="@safe-name"/>');</xsl:attribute>

      <img src="/astrogrid-portal/icons/Folder.png" alt="toggle">
        <xsl:attribute name="id">I<xsl:value-of select="@safe-name"/></xsl:attribute>
      </img>
    </span>
    
    &#160;
    <span style="cursor:pointer;cursor:hand;">
      <xsl:attribute name="onclick">setIVORN('<xsl:value-of select="@ivorn"/>');</xsl:attribute>
      <xsl:value-of select="@item-name"/><br/>
    </span>

    <span class="branch">
      <xsl:attribute name="id"><xsl:value-of select="@safe-name"/></xsl:attribute>
      <xsl:apply-templates/>
      <notag/>
    </span>
  </xsl:template>

  <xsl:template match="myspace-item">
    <img src="/astrogrid-portal/icons/Document.png" alt="doc"/>

    &#160;
    <span class="document">
      <xsl:attribute name="onclick">setIVORN('<xsl:value-of select="@ivorn"/>');</xsl:attribute>
      <xsl:value-of select="@item-name"/>
    </span>
    <br/>
  </xsl:template>
   
   <!-- avoid output of text node 
        with default template -->
  <xsl:template match="@*|node()"/>

</xsl:stylesheet>
