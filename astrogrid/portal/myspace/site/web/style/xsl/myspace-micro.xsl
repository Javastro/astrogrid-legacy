<?xml version="1.0"?>

<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:output
      method="xml"
      omit-xml-declaration="yes"/>
      
  <xsl:param name="ivorn" select="myspace-ivorn"/>
  <xsl:param name="agsl" select="myspace-agsl"/>

  <xsl:param name="form_name" select="form_name"/>
  <xsl:param name="form_action" select="form_action"/>

  <xsl:template match="/">
    <html>
      <head>
        <link rel="stylesheet" type="text/css" href="/astrogrid-portal/mount/myspace/xmlTree.css"/>
        <link rel="stylesheet" type="text/css" href="/astrogrid-portal/mount/myspace/myspace.css"/>
        <script type="text/javascript" src="/astrogrid-portal/mount/myspace/xmlTree.js">
          null;
        </script>
        <script type="text/javascript" src="/astrogrid-portal/mount/myspace/myspace.js">
          null;
        </script>
      </head>
      
      <body>
<!--
        <p>
          ivorn: <xsl:value-of select="$ivorn"/><br/>
          agsl: <xsl:value-of select="$agsl"/>
        </p>
-->
        <script type="text/javascript">
          window.document.title = "MySpace MicroBrowser";
        </script>
        
        <form id="myspace-explorer-form" action=".">
          <table id="myspace-tree-header">
            <tr>
              <td>IVORN:</td>
              <td style="width:100%"><input name="myspace-ivorn" id="myspace-ivorn" type="text" readonly="true" style="width:100%;border-style:none;"/></td>
            </tr>
            <tr>
              <td>AGSL:</td>
              <td style="width:100%"><input name="myspace-agsl" id="myspace-agsl" type="text" readonly="true" style="width:100%;border-style:none;"/></td>
            </tr>
          </table>
          
          <input type="button" value="OK">
            <xsl:attribute name="onclick">
              setParentIVORNAgsl('<xsl:value-of select="$ivorn"/>', '<xsl:value-of select="$agsl"/>');
              submitParentForm('<xsl:value-of select="$form_name"/>', '<xsl:value-of select="$form_action"/>');
            </xsl:attribute>
          </input>
          
          <input type="button" value="Cancel" onclick="window.close();"/>
        </form>
        
        <xsl:apply-templates/>
      </body>
    </html>
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
      <xsl:attribute name="onclick">setIVORNAgsl('<xsl:value-of select="@ivorn"/>', '<xsl:value-of select="@full-name"/>');</xsl:attribute>
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
      <xsl:attribute name="onclick">setIVORNAgsl('<xsl:value-of select="@ivorn"/>', '<xsl:value-of select="@full-name"/>');</xsl:attribute>
      <xsl:value-of select="@item-name"/>
    </span>
    <br/>
  </xsl:template>
   
   <!-- avoid output of text node 
        with default template -->
  <xsl:template match="@*|node()"/>

</xsl:stylesheet>
