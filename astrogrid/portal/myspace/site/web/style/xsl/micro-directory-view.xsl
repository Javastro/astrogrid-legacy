<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!--
    xmlns:agp-myspace="http://astrogrid.org/xsp/myspace/1.0">
-->

  <xsl:output method="xml" omit-xml-declaration="yes"/>

  <xsl:template match="/">
  
    <ag-div>
<!--
      <agComponentTitle>Directory View</agComponentTitle>
-->
      
      <ag-onload function="nofooter();"/>

      <ag-link rel="stylesheet" type="text/css" href="/astrogrid-portal/mount/myspace/xmlTree.css"/>
      <ag-link rel="stylesheet" type="text/css" href="/astrogrid-portal/mount/myspace/myspace.css"/>
      
      <ag-script type="text/javascript" src="/astrogrid-portal/mount/myspace/myspace.js"/>
      <ag-script type="text/javascript" src="/astrogrid-portal/mount/myspace/xmlTree.js"/>  
  
<script language="javascript">

<![CDATA[
    
function test() {
	alert( "test" ) ;
}

function highlight(diese) {
	diese.style.background = "#dcdcff";
}

function highlightDir(diese){
	diese.style.background = "#dcdcff";
}

function dehighlight(diese) {
	diese.style.background = "#ffffff";
}

function setSelected( item ) {   
    getFrameDocument("bottomControls").getElementById( 'fileName' ).value = item ;
    // this should work but doesnt...
    // top.bottomControls.document.getElementById('fileName').value = item ;
}

function setCurrent( directory ) {
    //alert( "setCurrent: " + directory ) ;
    //top.topControls.document.getElementById('currentLocation').value = directory ;
    getFrameDocument("topControls").getElementById('currentLocation').value = directory ;
}

function refreshDirectoryView( path, branch ) {
    //alert( "refreshDirectoryView.\n " + "path: " + path + "\nbranch: " + branch ) ;
    var directoryElement = document.getElementById( branch ) ;
    //alert( "directoryElement: " + directoryElement );
    var status = directoryElement.getAttribute("status") ;
    //alert( "status: " + status ) ;
    var form ;
    var mode ;
    var url ;
    if( status == "NOT_LOADED" ) {
       form = document.getElementById( 'myspace-action-form' ); 
       if( form ) { 
          if( path ) {
             document.getElementById( 'myspace-action' ).value = 'myspace-load-branch' ;
             document.getElementById( 'myspace-tree-view-path' ).value = path ; 
             document.getElementById( 'myspace-directory-view-path' ).value = path ;              
             //alert( "form.submit()" ) ;   
             form.submit();
          }
       }                   
    }
    else {
       mode = document.getElementById( 'requested-mode' ).value ;
       url = "/astrogrid-portal/bare/mount/myspace/micro-directory-view.xml?requested-mode=" + mode + "\x26myspace-directory-view-path=" + path ;
       //alert( "hello url: " + url );
       window.open( url, "directory" ) ;      
    }
}


function getFrameDocument( frameName ) {
    for( var i=0; i!= top.frames.length; i++ ) {
        if( top.frames[i].name == frameName ) {
           return top.frames[i].document ;
        }
    }
}

function matchItem( itemName ) {
    //alert( "matchItem itemName: " + itemName ) ;
    var element = document.getElementById('folderSafeName') ;
    //alert( "element: " + element) ;
    //alert( "element.name " + element.name) ;
    //alert( "element.value " + element.value) ;
    var parentFolderName = element.value ;
    var safeName = parentFolderName + itemName ;
    //alert( "safeName: " + safeName ) ;
    //alert( "top.directory.document.getElementById(safeName) " + top.directory.document.getElementById(safeName) ) ;
    // Safe name is different between files and folders...
    if(  document.getElementById(safeName) != null 
         ||
         document.getElementById(safeName + "_") != null  )
       return true ;
    else
       return false ;
}

function matchedItemType( itemName ) {

    var element = document.getElementById('folderSafeName') ;
    var parentFolderName = element.value ;
    var safeName = parentFolderName + itemName ;
 
    // Safe name is different between files and folders...
    if(  document.getElementById(safeName) != null )
       return "File" ;
        
    if(  document.getElementById(safeName + "_") != null  )
       return "Folder" ;

       return  ;
}

]]>

</script>
    

	
        <form
            id="myspace-action-form"
            name="myspace-action-form"
            action="/astrogrid-portal/bare/mount/myspace/myspace-micro-action"
            method="POST"
            enctype="multipart/form-data"> 
            <xsl:apply-templates select="directory/preset-form-values" />    
            <div>                 
               <table cellspacing="0" cellpadding="2" width="100%">
                  <thead>
                     <tr>
                        <th></th>
                        <th align="left">Name</th>
                        <th align="center">Size</th>
                        <th align="left">Type</th>
                        <th align="left">Date modified</th>
                        <th align="left">Date created</th>
                     </tr>
                  </thead>
                  <tbody align="left" valign="bottom">        
                     <xsl:apply-templates select="directory/directory-item" />
                  </tbody>
               </table >
            </div>                          
         </form>
         
    </ag-div>
    
  </xsl:template>
  
  
  <xsl:template match="preset-form-values">
       <input id="myspace-action" name="myspace-action" type="hidden"/>
       <input name="myspace-directory-view-path" id="myspace-directory-view-path" type="hidden"/>
       <input name="myspace-source-path" id="myspace-source-path" type="hidden"/>
       <input name="myspace-target-name" id="myspace-target-name" type="hidden"/>  
       <input name="myspace-target-path" id="myspace-target-path" type="hidden"/>      
       <input name="myspace-tree-view-path" id="myspace-tree-view-path" type="hidden"/> 
       <input name="requested-mode" id="requested-mode" type="hidden"><xsl:attribute name="value"><xsl:value-of select="mode"/></xsl:attribute></input>     
  </xsl:template>
  

  <xsl:template match="directory-item">
  
      <input name="folderSafeName" id="folderSafeName" type="hidden">
          <xsl:attribute name="value"><xsl:value-of select="@safe-name"/></xsl:attribute>      
      </input> 
                
      <xsl:apply-templates select="myspace-item[@type='folder']">                
          <xsl:sort select="@item-name"/>         
      </xsl:apply-templates>
      <xsl:apply-templates select="myspace-item[@type='file']">                
          <xsl:sort select="@item-name"/>         
      </xsl:apply-templates> 
  </xsl:template>

  <!-- Selects all folders of the user.
       The idea is to default display as not open.
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
    
    <tr onmouseover="highlight(this)" 
        onmouseout="dehighlight(this)" >
        
        <td> 
          <img src="/astrogrid-portal/icons/Document.png" alt="doc" style="cursor:pointer;cursor:hand;"/>
        </td> 
        
        <td style="cursor:pointer;cursor:hand;" >
          <span class="document" style="cursor:pointer;cursor:hand;">
           <xsl:attribute name="id"><xsl:value-of select="@safe-name"/></xsl:attribute>
           <xsl:attribute name="onclick">
               setHighlight('<xsl:value-of select="@safe-name"/>', 'document-highlight');
               setSelected( '<xsl:value-of select="@item-name"/>' );
           </xsl:attribute>
           <xsl:value-of select="@item-name"/>
          </span>
        </td> 
        <td align="right">
           <xsl:value-of select="@size"/>&#160;bytes&#160;
        </td>
        <td>
           <xsl:value-of select="@mime-type"/>
        </td>
        <td  align="left">
           <xsl:value-of select="@modified"/>
        </td> 
        <td  align="left">
           <xsl:value-of select="@created"/>
        </td> 
     </tr>
    
  </xsl:template>

  <!-- Named template "folder"
       The idea is to supply common processing for folders, varying only on the default
       display characteristics. As used above, the top folder is always shown as open by
       default, others are shown closed by default. The user has the option to toggle.
   -->  
  <xsl:template name="folder">
    <xsl:param name="display"/>
    <xsl:param name="icon-path"/>
    
    <tr onmouseover="highlightDir(this)" 
        onmouseout="dehighlight(this)" >
        
        <td> 
          <img style="cursor:pointer;cursor:hand;">
             <xsl:attribute name="title">Show folder details</xsl:attribute>
             <xsl:attribute name="src"><xsl:value-of select="$icon-path"/></xsl:attribute>
             <xsl:attribute name="id">I<xsl:value-of select="@safe-name"/></xsl:attribute> 
             <xsl:attribute name="onclick">
                  setCurrent( '<xsl:value-of select="@full-name"/>' ) ;
                  refreshDirectoryView( '<xsl:value-of select="@full-name"/>', '<xsl:value-of select="@safe-name"/>' ) ;
             </xsl:attribute>                       
          </img>
        </td> 
        
        <td style="cursor:pointer;cursor:hand;">
          <span class="folder" style="cursor:pointer;cursor:hand;">
           <xsl:attribute name="id"><xsl:value-of select="@safe-name"/></xsl:attribute>
           <xsl:attribute name="onclick">
               setHighlight('<xsl:value-of select="@safe-name"/>', 'folder-highlight');              
           </xsl:attribute>
           
           
      <xsl:choose>
         <xsl:when test="@filled = 'false'" >
            <xsl:attribute name="status">NOT_LOADED</xsl:attribute>
         </xsl:when>
         <xsl:otherwise>
             <xsl:attribute name="status">LOADED</xsl:attribute>
         </xsl:otherwise>
      </xsl:choose>           
               
           
           
           <xsl:value-of select="@item-name"/>
          </span>
        </td> 
        <td align="right">
           <xsl:value-of select="@size"/>
        </td>
        <td>
           <xsl:value-of select="@mime-type"/>
        </td>
        <td  align="left">
           <xsl:value-of select="@modified"/>
        </td> 
        <td  align="left">
           <xsl:value-of select="@created"/>
        </td> 
     </tr>

  </xsl:template>
   
   <!-- avoid output of text node 
        with default template -->
  <xsl:template match="@*|node()"/>

</xsl:stylesheet>
