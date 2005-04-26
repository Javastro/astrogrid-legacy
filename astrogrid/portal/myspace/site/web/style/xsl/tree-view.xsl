<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!--
    xmlns:agp-myspace="http://astrogrid.org/xsp/myspace/1.0">
-->

  <xsl:output method="xml" omit-xml-declaration="yes"/>

<xsl:template match="/">
    <ag-div>
<!--
      <agComponentTitle>Tree View</agComponentTitle>
-->
      
      <ag-onload function="nofooter();"/>

      <ag-link rel="stylesheet" type="text/css" href="/astrogrid-portal/mount/myspace/xmlTree.css"/>
      <ag-link rel="stylesheet" type="text/css" href="/astrogrid-portal/mount/myspace/myspace.css"/>
      
      <ag-script type="text/javascript" src="/astrogrid-portal/mount/myspace/myspace.js"/>
      <ag-script type="text/javascript" src="/astrogrid-portal/mount/myspace/xmlTree.js"/>
	
	
	
<script language="javascript">

function setCurrent( directory ){
    top.controls.document.getElementById('currentLocation').value = directory ;
}

function refreshDirectoryView( path ) {
    // alert( top.directory.name ) ;
    top.controls.document.getElementById('selected').value = "*** none ***" ;
    window.open( "/astrogrid-portal/bare/mount/myspace/directory-view.xml?myspace-directory-view-path=" + path, "directory" ) ;
}

// Create a branches collection for this document scope...
var branchCollection = new Branches() ;

function refreshTreeView( path ) {
  // alert( "open branches were: " + document.getElementById( 'myspace-tree-open-branches' ).value ) ;
  // alert( "open branches now: \n" + branchCollection.getContents() ) ;
  var form = document.getElementById( 'myspace-tree-form' ); 

  if( form ) { 
    if( path ) {
       document.getElementById( 'myspace-action' ).value = 'myspace-load-branch' ;
       document.getElementById( 'myspace-tree-view-path' ).value = path ; 
       document.getElementById( 'myspace-tree-open-branches' ).value = branchCollection.getContents() ;    
       form.submit();
    }
  }  
  window.close();
}

</script>     	
	
         <form
            id="myspace-tree-form"
            action="/astrogrid-portal/bare/mount/myspace/tree-view-action"
            method="POST"
            enctype="multipart/form-data">
              <xsl:apply-templates select="tree/preset-form-values" />
          <div>
              <xsl:apply-templates select="tree/myspace-tree" />
          </div>
        </form>
   
  </ag-div>

</xsl:template>	


<xsl:template match="preset-form-values">
          <input id="myspace-action" name="myspace-action" type="hidden"/>
          <input id="myspace-clipboard" name="myspace-clipboard" type="hidden"/>
          <input id="myspace-clipboard-url" name="myspace-clipboard-url" type="hidden"/>
          <input id="myspace-clipboard-copy" name="myspace-clipboard-copy" type="hidden"/>
          <input name="myspace-src" id="myspace-src" type="hidden"/>
          <input name="myspace-dest" id="myspace-dest" type="hidden"/>
          <input name="myspace-tree-view-path" id="myspace-tree-view-path" type="hidden"/>
          <input name="myspace-tree-open-branches" id="myspace-tree-open-branches" type="hidden" ><xsl:attribute name="value"><xsl:value-of select="open-branches"/></xsl:attribute></input>
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

  <!-- Selects all other open folders of the user.
       Priority 2 is to ensure this template is not chosen over the top folder selection
   -->  
  <xsl:template match="myspace-item[@type='folder' and @open='true']" priority="2">
    <xsl:call-template name="folder">
       <xsl:with-param name="display">block</xsl:with-param>
       <xsl:with-param name="icon-path">/astrogrid-portal/icons/Open.png</xsl:with-param>
    </xsl:call-template>
  </xsl:template>
  
  <!-- Selects all other closed folders of the user.
       Priority 2 is to ensure this template is not chosen over the top folder selection
   -->  
  <xsl:template match="myspace-item[@type='folder' and @open='false']" priority="2">
    <xsl:call-template name="folder">
       <xsl:with-param name="display">none</xsl:with-param>
       <xsl:with-param name="icon-path">/astrogrid-portal/icons/Folder.png</xsl:with-param>
    </xsl:call-template>
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
      <xsl:attribute name="onclick">
           showBranch('<xsl:value-of select="@safe-name"/>', '<xsl:value-of select="@full-name"/>' );
           this.title="Close branch";
      </xsl:attribute>
      <xsl:attribute name="title">Open branch</xsl:attribute>
      <img alt="toggle">
        <xsl:attribute name="src"><xsl:value-of select="$icon-path"/></xsl:attribute>
        <xsl:attribute name="id">I<xsl:value-of select="@safe-name"/></xsl:attribute>
      </img>
    </span>

    &#160;
    <span class="folder" style="cursor:pointer;cursor:hand;">
      <xsl:attribute name="id">S<xsl:value-of select="@safe-name"/></xsl:attribute>
      <xsl:attribute name="title">Show folder details</xsl:attribute>
      <xsl:attribute name="onclick">
           setHighlight('S<xsl:value-of select="@safe-name"/>', 'folder-highlight');
           setCurrent( '<xsl:value-of select="@full-name"/>' ) ;
           refreshDirectoryView( '<xsl:value-of select="@full-name"/>' ) ;
      </xsl:attribute> 
      <xsl:value-of select="@item-name"/><br/>
    </span>

    <span class="branch" >
      <xsl:attribute name="style">display:<xsl:value-of select="$display"/></xsl:attribute>
      <xsl:attribute name="id"><xsl:value-of select="@safe-name"/></xsl:attribute>
      <xsl:choose>
         <xsl:when test="@filled = 'false'" >
             <xsl:attribute name="status">NOT_LOADED</xsl:attribute>
             <i>&#160;loading...</i>
         </xsl:when>
         <xsl:when test="@empty = 'false' and myspace-item/@type = 'folder'">
            <xsl:attribute name="status">LOADED</xsl:attribute>
            <xsl:apply-templates select="myspace-item[@type='folder']">                
               <xsl:sort select="@item-name"/>         
            </xsl:apply-templates>
         </xsl:when>
         <xsl:when test="@empty = 'true'">
             <xsl:attribute name="status">LOADED</xsl:attribute>
             <i>&#160;empty</i>
         </xsl:when>
         <xsl:otherwise>
             <xsl:attribute name="status">LOADED</xsl:attribute>
             <i>&#160;empty of folders</i>
         </xsl:otherwise>
      </xsl:choose>

      <notag/>
    </span>

  </xsl:template>
   
   <!-- avoid output of text node 
        with default template -->
  <xsl:template match="@*|node()"/>

</xsl:stylesheet>
