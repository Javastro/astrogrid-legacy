<?xml version="1.0"?>

<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!--
    xmlns:agp-myspace="http://astrogrid.org/xsp/myspace/1.0">
-->

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
        <form id="myspace-explorer-form" action="." method="POST" enctype="multipart/form-data">
          <div id="myspace-tools">
            <div id="myspace-tools-header">
              MySpace Tools
            </div>
            
            <input name="myspace-action" type="submit" value="myspace-change"><br/></input>
            <input name="myspace-action" type="submit" value="myspace-copy"><br/></input>
            <input name="myspace-action" type="submit" value="myspace-delete"><br/></input>
            <input name="myspace-action" type="submit" value="myspace-find-url"><br/></input>
            <input name="myspace-action" type="submit" value="myspace-move"><br/></input>
            <input name="myspace-action" type="submit" value="myspace-new-container"><br/></input>
            <input name="myspace-action" type="submit" value="myspace-upload"><br/></input>
            <input name="myspace-action" type="submit" value="myspace-upload-url"><br/></input>
            <hr/>
            <input name="myspace-action" type="submit" value="TopCat" onclick="myspace_votable_topcat();"><br/></input>
            <input name="myspace-action" type="submit" value="Aladin" onclick="myspace_votable_aladin();"><br/></input>
          </div>

          <div>
            <table id="myspace-tree-header">
              <tr>
                <td>Source:</td>
                <td style="width:100%"><input name="myspace-src" id="myspace-src" type="text" readonly="true" style="width:100%;border-style:none;"/></td>
              </tr>
              <tr>
                <td>Destination:</td>
                <td style="width:100%"><input name="myspace-dest" id="myspace-dest" type="text" style="width:100%"/></td>
              </tr>
              <tr>
                <td>Upload File:</td>
                <td style="width:100%"><input name="myspace-file" id="myspace-file" type="file" style="width:100%"/></td>
              </tr>
              <tr>
                <td>Upload URL:</td>
                <td style="width:100%"><input name="myspace-url" id="myspace-url" type="text" style="width:100%"/></td>
              </tr>
            </table>
  
            <xsl:apply-templates/>
          </div>
        </form>
      </div>
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
    
    <span style="cursor:pointer;cursor:hand;">
      <xsl:attribute name="onclick">setNewMySpaceName('<xsl:value-of select="@full-name"/>');</xsl:attribute>
      <img src="/astrogrid-portal/icons/FolderIn.png" alt="[dest]"/>
    </span>

    &#160;
    <span style="cursor:pointer;cursor:hand;">
      <xsl:attribute name="onclick">setOldMySpaceName('<xsl:value-of select="@full-name"/>');</xsl:attribute>
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

    <span style="cursor:pointer;cursor:hand;">
      <xsl:attribute name="onclick">setNewMySpaceName('<xsl:value-of select="@full-name"/>');</xsl:attribute>
      <img src="/astrogrid-portal/icons/DocumentIn.png" alt="[dest]"/>
    </span>

    &#160;
    <span class="document">
      <xsl:attribute name="onclick">setOldMySpaceName('<xsl:value-of select="@full-name"/>')</xsl:attribute>
      <xsl:value-of select="@item-name"/>
    </span>
    <br/>
  </xsl:template>
   
   <!-- avoid output of text node 
        with default template -->
  <xsl:template match="@*|node()"/>

</xsl:stylesheet>
