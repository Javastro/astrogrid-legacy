<?xml version="1.0"?>

<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:agp-myspace="http://astrogrid.org/xsp/myspace/1.0">
  <xsl:template match="/">
    <xsl:apply-templates select="/explorer/tree/*"/>
  </xsl:template>
  
  <xsl:template match="ag-div">
    <xsl:copy>
      <xsl:apply-templates select="@*"/>
        <ag-script type="text/javascript" src="/astrogrid-portal/mount/myspace/utils.js"/>
        <ag-script type="text/javascript" src="/astrogrid-portal/mount/myspace/explorer.js"/>
<!--
        <ag-onload function="reset_action('myspace-explorer-form', '/astrogrid-portal/main/mount/myspace/myspace-explorer')"/>
-->
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="table[@id = 'myspace-tree-header']">
    <table>
      <tr>
        <td>MySpace End Point:</td>
        <td><xsl:copy-of select="/explorer/select/select"/>
        <xsl:copy-of select="//input[@id = 'myspace-end-point']"/></td>
        <td><input name="myspace-change" type="button" value="Change" onclick="javascript:void(myspace_submit_form('myspace-explorer-form', 'myspace-action', 'myspace-change'));"/></td>
      </tr>
    </table>
    <xsl:copy-of select="."/>
  </xsl:template>
  
<!--
  <xsl:template match="form[@id = 'myspace-explorer-form']">
    <xsl:copy>
      <xsl:apply-templates select="@*[not(name() = 'action')]"/>
      <xsl:apply-templates/>
    </xsl:copy>
  </xsl:template>
-->
  
  <!-- Default, copy all and apply templates -->
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()" />
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
