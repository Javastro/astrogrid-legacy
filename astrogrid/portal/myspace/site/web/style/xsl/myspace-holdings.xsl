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
      <agComponentTitle>MySpace Explorer</agComponentTitle>
      
      <ag-onload function="nofooter();"/>

      <ag-link rel="stylesheet" type="text/css" href="/astrogrid-portal/mount/myspace/xmlTree.css"/>
      <ag-link rel="stylesheet" type="text/css" href="/astrogrid-portal/mount/myspace/myspace.css"/>
      
      <ag-script type="text/javascript" src="/astrogrid-portal/mount/myspace/myspace.js"/>

      <ag-script type="text/javascript" src="/astrogrid-portal/mount/myspace/xmlTree.js"/>
      
      	<ag-menu name="myspace-menu"/>
	
        <form
            id="myspace-explorer-form"
            action="/astrogrid-portal/main/mount/myspace/myspace-explorer"
            method="POST"
            enctype="multipart/form-data">
          <input id="myspace-action" name="myspace-action" type="hidden"/>
          <input id="myspace-clipboard" name="myspace-clipboard" type="hidden"/>
          <input id="myspace-clipboard-url" name="myspace-clipboard-url" type="hidden"/>
          <input id="myspace-clipboard-copy" name="myspace-clipboard-copy" type="hidden"/>
          <input name="myspace-src" id="myspace-src" type="hidden"/>
          <input name="myspace-dest" id="myspace-dest" type="hidden"/>
          
          <div>
            <table id="myspace-tree-header">
              <tr>
                <td colspan="3"><hr class="agbar"/></td>
              </tr>

              <tr>
                <td>Upload File:</td>
                <td style="width:100%"><input name="myspace-file" id="myspace-file" type="file" style="width:100%"/></td>
                <td><input type="button" value="Upload" onclick="javascript:void(myspace_browse_fill('myspace-src', 'myspace-dest', 'myspace_generic_return', 'myspace-upload'));"/></td>
              </tr>
              <tr>
                <td>Upload URL:</td>
                <td style="width:100%"><input name="myspace-url" id="myspace-url" type="text" style="width:100%"/></td>
                <td><input type="button" value="Upload" onclick="javascript:void(myspace_browse_fill('myspace-src', 'myspace-dest', 'myspace_generic_return', 'myspace-upload-url'));"/></td>
              </tr>
              <tr>
                <td colspan="3"><hr class="agbar"/></td>
              </tr>
            </table>
  
            <xsl:apply-templates/>
          </div>
        </form>
    </ag-div>
  </xsl:template>

  <xsl:template match="myspace-tree">
    <xsl:apply-templates/>
  </xsl:template>

  <!-- Selects the top folder of the user.
       The idea is to ensure this one is always displayed as open.
       Priority 3 is to ensure this one is chosen first
   -->  
  <xsl:template match="myspace-tree/myspace-item[@type='folder']" priority="3">
    <xsl:call-template name="folder">
       <xsl:with-param name="display">block</xsl:with-param>
       <xsl:with-param name="icon-path">/astrogrid-portal/icons/Open.png</xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <!-- Selects all other folders of the user.
       The idea is to default display as not open.
       Priority 2 is to ensure this template is not chosen over the top folder selection
   -->  
  <xsl:template match="myspace-item[@type='folder']" priority="2">
    <xsl:call-template name="folder">
       <xsl:with-param name="display">none</xsl:with-param>
       <xsl:with-param name="icon-path">/astrogrid-portal/icons/Folder.png</xsl:with-param>
    </xsl:call-template>
  </xsl:template>

  <!-- Selects all other items of the user, which means files.
       Priority 1 is to ensure this template is not chosen over any folder selection
   -->  
  <xsl:template match="myspace-item" priority="1">

    <span style="cursor:pointer;cursor:hand;">
      <xsl:attribute name="onclick">setNewMySpaceName('<xsl:value-of select="@full-name"/>');</xsl:attribute>
      <img src="/astrogrid-portal/icons/Document.png" alt="doc"/>
    </span>

    &#160;
    <span class="document">
      <xsl:attribute name="id"><xsl:value-of select="@safe-name"/></xsl:attribute>
      <xsl:attribute name="onclick">setOldMySpaceNameUrl('<xsl:value-of select="@full-name"/>', '<xsl:value-of select="@url"/>');  setHighlight('<xsl:value-of select="@safe-name"/>', 'document-highlight');</xsl:attribute>
      <xsl:value-of select="@item-name"/>
    </span>

    <br/>
  </xsl:template>

  <!-- Named template "folder"
       The idea is to supply common processing for folders, varying only on the default
       display characteristics. As used above, the top folder is always shown as open by
       default, others are shown closed by default. The user has the option to toggle.
   -->  
  <xsl:template name="folder">
    <xsl:param name="display"/>
    <xsl:param name="icon-path"/>
    <span class="trigger">
      <xsl:attribute name="onclick">showBranch('<xsl:value-of select="@safe-name"/>');</xsl:attribute>

      <img alt="toggle">
        <xsl:attribute name="src"><xsl:value-of select="$icon-path"/></xsl:attribute>
        <xsl:attribute name="id">I<xsl:value-of select="@safe-name"/></xsl:attribute>
      </img>
    </span>

    &#160;
    <span class="folder" style="cursor:pointer;cursor:hand;">
      <xsl:attribute name="onclick">
           setNewMySpaceName('<xsl:value-of select="@full-name"/>');
           setOldMySpaceName('<xsl:value-of select="@full-name"/>');
      </xsl:attribute>
      <xsl:value-of select="@item-name"/><br/>
    </span>

    <span class="branch" >
      <xsl:attribute name="style">display:<xsl:value-of select="$display"/></xsl:attribute>
      <xsl:attribute name="id"><xsl:value-of select="@safe-name"/></xsl:attribute>
      <xsl:apply-templates/>
      <notag/>
    </span>

  </xsl:template>
   
   <!-- avoid output of text node 
        with default template -->
  <xsl:template match="@*|node()"/>

</xsl:stylesheet>
