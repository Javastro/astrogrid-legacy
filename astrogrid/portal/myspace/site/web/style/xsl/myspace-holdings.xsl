<?xml version="1.0"?>

<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:agp-myspace="http://astrogrid.org/xsp/myspace/1.0">
  <xsl:output method="html"/>

  <xsl:template match="/">
    <ag-div>
      <ag-link rel="stylesheet" type="text/css" href="/astrogrid-portal/mount/myspace/xmlTree.css"/>
      <ag-link rel="stylesheet" type="text/css" href="/astrogrid-portal/mount/myspace/myspace.css"/>
      <ag-script type="text/javascript" src="/astrogrid-portal/mount/myspace/xmlTree.js"/>
      <ag-script type="text/javascript" src="/astrogrid-portal/mount/myspace/myspace.js"/>
      
      <div>
        <form id="myspace-explorer-form" action=".">
          <div id="myspace-tools">
            <div id="myspace-tools-header">
              MySpace Tools
            </div>
            
            <input name="myspace-action" type="submit" value="Copy"><br/></input>
            <input name="myspace-action" type="submit" value="Delete"><br/></input>
            <input name="myspace-action" type="submit" value="Rename"><br/></input>
            <input name="myspace-action" type="button" value="ExtendLease" onclick="myspace_extension_days();"><br/></input><input class="myspace-hidden" id="myspace-extension-days" name="myspace-extension-days" type="text"><span/></input>
            <input name="myspace-action" type="submit" value="NewContainer"><br/></input>
            <input name="myspace-action" type="submit" value="ChangeOwner" onclick="myspace_chown();"><br/></input><input class="myspace-hidden" id="myspace-chown" name="myspace-chown" type="text"><span/></input>
            <input name="myspace-action" type="submit" value="UploadURL" onclick="myspace_upload_url();"><span/></input><input class="myspace-hidden" id="myspace-upload-url" name="myspace-upload-url" type="text"/><input class="myspace-hidden" id="myspace-upload-category" name="myspace-upload-category" type="text"><span/></input>
            <hr/>
            <input name="myspace-action" type="submit" value="TopCat" onclick="myspace_votable_topcat();"><br/></input>
            <input name="myspace-action" type="submit" value="Aladin" onclick="myspace_votable_aladin();"><br/></input>
            <input name="myspace-temp" type="button" value="temp" onclick="myspace_votable_temp();"><br/></input>
          </div>

          <div>
            <table id="myspace-tree-header">
              <tr>
                <td>Source:</td>
                <td style="width:100%"><input name="myspace-old-name" id="myspace-old-name" type="text" readonly="true" style="width:100%;border-style:none;"/></td>
              </tr>
              <tr>
                <td>Destination:</td>
                <td style="width:100%"><input name="myspace-new-name" id="myspace-new-name" type="text" style="width:100%"/></td>
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

  <xsl:template match="myspace-item[@type=1]">
    <span style="cursor:pointer;cursor:hand;">
      <xsl:attribute name="onclick">setNewMySpaceName('<xsl:value-of select="@full-name"/>');</xsl:attribute>
      [dest]
    </span>
    
    <span class="trigger">
      <xsl:attribute name="onclick">showBranch('<xsl:value-of select="@safe-name"/>');setOldMySpaceName('<xsl:value-of select="@full-name"/>');</xsl:attribute>

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
    <span style="cursor:pointer;cursor:hand;">
      <xsl:attribute name="onclick">setNewMySpaceName('<xsl:value-of select="@full-name"/>');</xsl:attribute>
      [dest]
    </span>

    <img src="/astrogrid-portal/mount/myspace/doc.gif" alt="doc"/>

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
