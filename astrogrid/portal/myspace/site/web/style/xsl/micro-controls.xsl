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

  <xsl:param name="field_name" select="field_name"/>
  <xsl:param name="field_value" select="field_value"/>

  <xsl:param name="parent_func"/>

  <xsl:template match="/">      
	  <ag-div>
	    <!-- Add our page content -->
		<content>
		  <agPUBMessage>
            MySpace Microbrowser
          </agPUBMessage> 
          <ag-script type="text/javascript" src="/astrogrid-portal/mount/myspace/xmlTree.js"/>
          <ag-script type="text/javascript" src="/astrogrid-portal/mount/myspace/myspace.js"/>
          <ag-script type="text/javascript" src="/astrogrid-portal/mount/myspace/focus.js"/>	  
          
          <ag-link rel="stylesheet" type="text/css" href="/astrogrid-portal/mount/myspace/xmlTree.css"/>
          <ag-link rel="stylesheet" type="text/css" href="/astrogrid-portal/mount/myspace/myspace.css"/>

          <ag-onload function="fcsOnMe();"/>

        <form id="myspace-explorer-form" action=".">
        	<input name="myspace-endpoint" id="myspace-endpoint" type="hidden">
        		<xsl:attribute name="value"><xsl:value-of select="/myspace-tree/@endpoint"/></xsl:attribute>
        	</input>
          <table id="myspace-tree-header">
            <tr>
              <td>Location:</td>
              <td style="width:100%"><input name="myspace-agsl" id="myspace-agsl" type="text" readonly="true" style="width:100%;border-style:none;"/></td>
            </tr>
            <tr>
              <td>Name:</td>
              <td style="width:100%"><input name="myspace-item" id="myspace-item" type="text" onfocus="skipcycle=true;" onblur="skipcycle=false;"/></td>
            </tr>
          </table>
          <input type="hidden" name="myspace-ivorn" id="myspace-ivorn" />
 <!--         
          <p>
            Item Name: <input name="myspace-item" id="myspace-item" type="text" onfocus="skipcycle=true;" onblur="skipcycle=false;"/>
          </p>
          -->
          <input class="agActionButton" type="button" value="OK">
            <xsl:attribute name="onclick">
              processMicroBrowserOK('<xsl:value-of select="$ivorn"/>', '<xsl:value-of select="$agsl"/>',
                                    '<xsl:value-of select="$field_name"/>', '<xsl:value-of select="$field_value"/>',
                                    '<xsl:value-of select="$form_name"/>', '<xsl:value-of select="$form_action"/>',
                                    '<xsl:value-of select="$parent_func"/>');
            </xsl:attribute>
          </input>
<!-- JL: Caching temporarilly removed from the system ...
          <input class="agActionButton" type="button" value="Refresh">
            <xsl:attribute name="onclick">
              processMicroBrowserRefresh('<xsl:value-of select="$agsl"/>',
                                         '<xsl:value-of select="$parent_func"/>');
            </xsl:attribute>
          </input>
-->
          <input class="agActionButton" type="button" value="Cancel" onclick="window.close();"/>
        </form>

        <xsl:apply-templates/>
		</content>
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
  
  <!-- Named template "folder"
       The idea is to supply common processing for folders, varying only on the default
       display characteristiacs. As used above, the top folder is always shown as open by
       default, others are shown closed by default. The user has the option to toggle.
  -->  
  <xsl:template name="folder">
    <xsl:param name="display"/>
    <xsl:param name="icon-path"/>
    <span class="trigger">
      <xsl:attribute name="onclick">
        showBranch('<xsl:value-of select="@safe-name"/>');
      </xsl:attribute>

      <img alt="toggle">
        <xsl:attribute name="src"><xsl:value-of select="$icon-path"/></xsl:attribute>
        <xsl:attribute name="id">I<xsl:value-of select="@safe-name"/></xsl:attribute>
      </img>
    </span>

    &#160;
    <span style="cursor:pointer;cursor:hand;">
      <xsl:attribute name="onclick">
        setIVORNAgsl('', '<xsl:value-of select="@full-name"/>');
        newAgsl('<xsl:value-of select="@folder-path"/>', '');
      </xsl:attribute>
      <xsl:value-of select="@item-name"/><br/>
    </span>

    <span class="branch">
      <xsl:attribute name="style">display:<xsl:value-of select="$display"/></xsl:attribute>
      <xsl:attribute name="id"><xsl:value-of select="@safe-name"/></xsl:attribute>
      <xsl:apply-templates select="myspace-item[@type='folder']">                
          <xsl:sort select="@item-name"/>         
      </xsl:apply-templates>
      <xsl:apply-templates select="myspace-item[@type='file']">                
          <xsl:sort select="@item-name"/>         
      </xsl:apply-templates>      
      
      <notag/>
    </span>    
  </xsl:template>
  
  
  <!-- Selects all other items of the user, which means files.
       Priority 1 is to ensure this template is not chosen over any folder selection
  --> 
  <xsl:template match="myspace-item[@type='file']" priority="1">
      
    <img src="/astrogrid-portal/icons/Document.png" alt="doc"/>

    &#160;
    <span class="document">
      <xsl:attribute name="id"><xsl:value-of select="@safe-name"/></xsl:attribute>
      <xsl:attribute name="onclick">
        setIVORNAgsl('<xsl:value-of select="@ivorn"/>', '<xsl:value-of select="@full-name"/>');
        newAgsl('<xsl:value-of select="@folder-path"/>', '<xsl:value-of select="@item-name"/>');
        setHighlight('<xsl:value-of select="@safe-name"/>', 'document-highlight');
      </xsl:attribute>
      <xsl:value-of select="@item-name"/>
    </span>
    <br/>
  </xsl:template>

   <!-- avoid output of text node
        with default template -->
  <xsl:template match="@*|node()"/>

</xsl:stylesheet>
          
