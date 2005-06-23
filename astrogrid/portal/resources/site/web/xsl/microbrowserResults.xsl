<?xml version="1.0"?>

  <xsl:stylesheet
    version="1.0"
    xmlns="http://www.astrogrid.org/portal"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
        xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.9"
        xmlns:vs="http://www.ivoa.net/xml/VODataService/v0.4"
    xmlns:cea="http://www.ivoa.net/xml/CEAService/v0.1"
    xmlns:ceapd="http://www.astrogrid.org/schema/AGParameterDefinition/v1"
    xmlns:ceab="http://www.astrogrid.org/schema/CommonExecutionArchitectureBase/v1"     >

    <xsl:output
      method="xml"
      version="1.0"
      indent="yes"/>
      
    <xsl:param name="parent_authId" />
    <xsl:param name="mainelement" />
      

      <xsl:template match="/results">
        <ag-div>                 
                                    
   <style type="text/css">
    div.summary{
        background-color:lightblue;
        border-top-width:2px;
        border-top-color:blue;
        border-top-style:solid;
        border-bottom-width:2px;
        border-bottom-color:blue;
        border-bottom-style:solid;
        border-left-width:2px;
        border-left-color:blue;
        border-left-style:solid;
        border-right-width:2px;
        border-right-color:blue;
        border-right-style:solid;                        
        margin-top:5px;
        padding:5px;
        margin-left:10px;
        margin-right:10px;                
    }
    div.detail{
        border-top-width:1px;
        border-top-color:blue;
        border-top-style:solid;
        margin-top:10px;
        padding:5px;
        display:none;
    }
    div.options{
        margin-top:2px;
    }    

    span.HEADING{color:blue; align:center;}
    span.KEY{color:blue; font-family:monospace; padding-left:5px;padding-right:5px;}
    span.VALUE{font-family:monospace; padding-left:5px;padding-right:5px;}

   </style>            
   
   <script language="javascript">  
    /*
     * toggle()
     * Toggle parameter div on or off - used to show parameter table
     *
     * @param id       element id           
     */ 
    function toggle(id) 
    {            
      var element = document.getElementById(id).style;
      {
        if ( element.display != "block" )
        {
          element.display = "block"
        } 
        else
        {
          element.display = "none"
        }
      }
    }
    
    /*
     * populateAndCloseTask()
     * Populate relevant text field in parent window
     *
     * @param id       element id
     * @param key      value to insert           
     */ 
    function populateAndCloseTask(id, key)
    {
      parentDoc = parent.opener.document;
      parentId = parentDoc.getElementById(id);
      parentId.value = key;
      parent.window.close();
    } 
    
    /*
     * populateAndCloseCatalog()
     * Populate relevant text field in parent window
     * Includes correction for crossbrowser amplisand issue
     *
     * @param identifier      value to insert           
     */ 
    function populateAndCloseCatalog(identifier)
    {
      var url = "/astrogrid-portal/main/mount/datacenter/variablesFromMB.html?action=getTable&amp;uniqueID=";
      url = url + identifier;
      var safe_url = url.substring(0,url.indexOf("amp;"));
      safe_url = safe_url + url.substring(url.indexOf("amp;")+4, url.length);
        var safer_URL = safe_url.replace(/\+/g, "%2B");

          parent.window.close();
/*        parent.opener.parent.location.href = safe_url; */
          parent.opener.parent.location.href = safer_URL;
          parent.opener.parent.focus();     
        }    
    
            
   </script>       
        
        <xsl:apply-templates/>
        
        <script type="text/javascript" src="/astrogrid-portal/extras.js"/>                                        
        
        </ag-div>
      </xsl:template>        
 

      <!-- RESULT DETAILS -->
      <xsl:template match="resourceDetails">

        <xsl:if test="@resource_result_count != ''">
          <span>
            Your search returned <b><xsl:value-of select="@resource_result_count"/></b> items from the registry:
          </span>
          <br />
        </xsl:if>

          <xsl:if test="@resource_error_message != ''">
            <span style="color:red">
              An error occured whilst processing your query:
              <br />
              <xsl:value-of select="@resource_error_message"/>
            </span>
            <br />
          </xsl:if>


        <xsl:if test="@resource_info_message != ''">
          <span style="color:green">
            <xsl:value-of select="@reource_info_message"/>
          </span>
          <br />
        </xsl:if>        
        
      </xsl:template>    


      <!-- RESOURCE -->
      <xsl:template match="vr:Resource">
        <div class="summary" >          
          <nobr>
            <span class="KEY">Title:</span> 
            <span class="VALUE"><xsl:value-of select="vr:Title"/>&#10;</span>
            <span class="KEY">Short name:</span> 
            <span class="VALUE"><xsl:value-of select="vr:ShortName"/>&#10;</span>
          </nobr>
          <br />
          <span class="KEY">Description:</span>
          <span class="VALUE"><xsl:value-of select="vr:Summary/vr:Description"/>&#10;</span>
          <br />
          <nobr>
            <span class="KEY">AuthorityID:</span> 
            <span class="VALUE"><xsl:value-of select="vr:Identifier/vr:AuthorityID"/>&#10;</span>
            <span class="KEY">ResourceKey:</span> 
            <span class="VALUE"><xsl:value-of select="vr:Identifier/vr:ResourceKey"/>&#10;</span>
            <span class="KEY">Type:</span> 
            <span class="VALUE"><xsl:value-of select="vr:Type"/>&#10;</span>            
          </nobr>
          <br />
          <xsl:if test="count(vr:ContentLevel) &gt; 0">
            <span class="KEY">Subject:</span><xsl:apply-templates select="vr:Subject" /><br />            
          </xsl:if>
          <xsl:if test="count(vr:ContentLevel) &gt; 0">          
            <span class="KEY">Content level:</span><xsl:apply-templates select="vr:ContentLevel" /><br />
          </xsl:if>
          
          <!-- TASK SELECT BUTTON -->
          <xsl:if test="$mainelement='Task'">  
            <xsl:element name="SPAN">                                
              <xsl:attribute name="title">&#10;Select this task&#10;</xsl:attribute> 
              <xsl:attribute name="class">agActionButton</xsl:attribute> 
              <xsl:attribute name="onclick">populateAndCloseTask('<xsl:value-of select="$parent_authId"/>','<xsl:value-of select="vr:Identifier/vr:AuthorityID"/>/<xsl:value-of select="vr:Identifier/vr:ResourceKey"/>'); </xsl:attribute>
              Select                         
            </xsl:element>
          </xsl:if>

          <!-- DETAIL LINKS -->
          <xsl:if test="count(vr:Curation) &gt; 0">
            <xsl:element name="SPAN">                                
              <xsl:attribute name="title">&#10;curation&#10;</xsl:attribute> 
              <xsl:attribute name="class">agActionButton</xsl:attribute> 
              <xsl:attribute name="onclick">toggle('curtation:<xsl:value-of select="vr:Identifier/vr:AuthorityID"/>/<xsl:value-of select="vr:Identifier/vr:ResourceKey"/>');</xsl:attribute>
              curation                         
            </xsl:element>
          </xsl:if>
          <xsl:if test="count(vr:RelatedResource) &gt; 0">          
            <xsl:element name="SPAN">                                
              <xsl:attribute name="title">&#10;related&#10;</xsl:attribute> 
              <xsl:attribute name="class">agActionButton</xsl:attribute> 
              <xsl:attribute name="onclick">toggle('related:<xsl:value-of select="vr:Identifier/vr:AuthorityID"/>/<xsl:value-of select="vr:Identifier/vr:ResourceKey"/>');</xsl:attribute>
              related resource
            </xsl:element>
          </xsl:if>
          <xsl:if test="count(vr:Coverage) &gt; 0">              
            <xsl:element name="SPAN">                                
              <xsl:attribute name="title">&#10;coverage&#10;</xsl:attribute> 
              <xsl:attribute name="class">agActionButton</xsl:attribute> 
              <xsl:attribute name="onclick">toggle('coverage:<xsl:value-of select="vr:Identifier/vr:AuthorityID"/>/<xsl:value-of select="vr:Identifier/vr:ResourceKey"/>');</xsl:attribute>
              coverage
            </xsl:element>
          </xsl:if>
          <xsl:if test="count(cea:ApplicationDefinition) &gt; 0">
            <xsl:element name="SPAN">                                
              <xsl:attribute name="title">&#10;parameters/interfaces&#10;</xsl:attribute> 
              <xsl:attribute name="class">agActionButton</xsl:attribute> 
              <xsl:attribute name="onclick">toggle('appdef:<xsl:value-of select="vr:Identifier/vr:AuthorityID"/>/<xsl:value-of select="vr:Identifier/vr:ResourceKey"/>');</xsl:attribute>
              application definition
            </xsl:element>                    
          </xsl:if>          
          <xsl:element name="SPAN">                                
            <xsl:attribute name="title">&#10;xml&#10;</xsl:attribute> 
            <xsl:attribute name="class">agActionButton</xsl:attribute> 
            <xsl:attribute name="onclick">toggle('xml:<xsl:value-of select="vr:Identifier/vr:AuthorityID"/>/<xsl:value-of select="vr:Identifier/vr:ResourceKey"/>');</xsl:attribute>
            xml
          </xsl:element>
          
          <xsl:apply-templates select="vr:Curation" />
          <xsl:apply-templates select="vr:RelatedResource" />
          <xsl:apply-templates select="vr:Coverage" />
          <xsl:apply-templates select="vs:Table" />
          <xsl:apply-templates select="cea:ApplicationDefinition" />
          <xsl:call-template name="xml" select="."/>         
        </div>
      </xsl:template>
            
      <xsl:template match="vr:Subject">
        <span class="VALUE"><xsl:value-of select="."/>, </span> 
      </xsl:template>  
      
      <xsl:template match="vr:ContentLevel">
        <span class="VALUE"><xsl:value-of select="."/>, </span> 
      </xsl:template>          




      <!-- CURATION -->
      <xsl:template match="vr:Curation">
        <div class="detail">
          <xsl:attribute name="id">curtation:<xsl:value-of select="../vr:Identifier/vr:AuthorityID"/>/<xsl:value-of select="../vr:Identifier/vr:ResourceKey"/></xsl:attribute>
          <span class="KEY">Title:</span> 
          <span class="VALUE"><xsl:value-of select="vr:Publisher/vr:Title"/></span><br />
          <xsl:if test="count(../vr:Identifier/vr:AuthorityID) &gt; 0">              
            <nobr>
              <span class="KEY">Publisher: AuthorityID:</span>
              <span class="VALUE"><xsl:value-of select="../vr:Identifier/vr:AuthorityID"/>
              <span class="KEY">ResourceKey:</span>
              <xsl:value-of select="../vr:Identifier/vr:ResourceKey"/></span>
            </nobr><br />             
          </xsl:if>
          <nobr>
            <span class="KEY">Contact: Name:</span> 
            <span class="VALUE"><xsl:value-of select="vr:Contact/vr:Name"/></span>
            <span class="KEY">Email:</span> 
            <span class="VALUE"><xsl:value-of select="vr:Contact/vr:Email"/></span>
          </nobr><br />
          <nobr>
            <xsl:if test="count(vr:Date) &gt; 0">
              <span class="KEY">Date:</span> 
            </xsl:if>
            <span class="VALUE"><xsl:value-of select="vr:Date"/></span>
            <xsl:if test="count(vr:Creator/vr:Name) &gt; 0">
              <span class="KEY">Creator:</span> 
              <span class="VALUE"><xsl:value-of select="vr:Creator/vr:Name"/></span>
              <span class="VALUE"><xsl:value-of select="vr:Creator/vr:Email"/></span>
            </xsl:if>
            <xsl:if test="count(vr:Contributor/vr:Name) &gt; 0">
              <span class="KEY">Contributor:</span>
              <xsl:apply-templates select="vr:Contributor" />
            </xsl:if>
          </nobr><br />          
        </div>
      </xsl:template>
      
      <xsl:template match="vr:Contributor"> 
        <span class="VALUE"><xsl:value-of select="vr:Name"/></span>
        <span class="VALUE"><xsl:value-of select="vr:Email"/></span>                                   
      </xsl:template>      
      
      


      <!-- COVERAGE -->
      <xsl:template match="vr:Coverage">
        <div class="detail">
          <xsl:attribute name="id">coverage:<xsl:value-of select="../vr:Identifier/vr:AuthorityID"/>/<xsl:value-of select="../vr:Identifier/vr:ResourceKey"/></xsl:attribute>
          <span class="KEY">Spatial:</span>
          <span class="KEY">all sky:</span>           
          <xsl:apply-templates select="vr:Spatial/vs:AllSky" />
          <span class="KEY">region of regard:</span>
          <xsl:apply-templates select="vr:Spatial/vs:RegionOfRegard" />
          <span class="KEY">Spectral:</span>           
          <xsl:apply-templates select="vs:Spectral/vs:Waveband" />
        </div>
      </xsl:template>      

      <xsl:template match="vs:Spectral/vs:Waveband">
        <span class="VALUE"><xsl:value-of select="."/>, </span> 
      </xsl:template>
      
      <xsl:template match="vr:Spatial/vs:AllSky">
        <span class="KEY">All sky:</span>
        <span class="VALUE"><xsl:value-of select="."/></span><br /> 
      </xsl:template>      

      <xsl:template match="vr:Spatial/vs:RegionOfRegard">
        <span class="KEY">Region of regard:</span>
        <span class="VALUE"><xsl:value-of select="."/></span> <br />
      </xsl:template>




      <!-- RELATED RESOURCE -->
      <xsl:template match="vr:RelatedResource">
        <div class="detail">
          <xsl:attribute name="id">related:<xsl:value-of select="../vr:Identifier/vr:AuthorityID"/>/<xsl:value-of select="../vr:Identifier/vr:ResourceKey"/></xsl:attribute>            
          <span class="KEY">Related to:</span>
          <span class="VALUE"><xsl:value-of select="vr:RelatedTo/vr:Title"/></span><br /> 
          <nobr>
            <span class="KEY">AuthorityID:</span>
            <span class="VALUE"><xsl:value-of select="vr:RelatedTo/vr:Identifier/vr:AuthorityID"/></span>
            <span class="KEY">ResourceKey:</span>
            <span class="VALUE"><xsl:value-of select="vr:RelatedTo/vr:Identifier/vr:ResourceKey"/></span>
          </nobr>
          <br />
          <span class="KEY">Relationship:</span> 
          <span class="VALUE"><xsl:value-of select="vr:Relationship"/></span><br />
        </div>
      </xsl:template>
      
      
      
      <!-- TABLE -->
      <xsl:template match="vs:Table">
        <div class="options">
          <!-- CATALOG SELECT BUTTON -->
          <xsl:if test="$mainelement='Catalog'">  
            <xsl:element name="SPAN">                                
              <xsl:attribute name="title">&#10;Select this table&#10;</xsl:attribute> 
              <xsl:attribute name="class">agActionButton</xsl:attribute> 
              <xsl:attribute name="onclick">populateAndCloseCatalog('<xsl:value-of select="../vr:Identifier/vr:AuthorityID"/>!<xsl:value-of select="../vr:Identifier/vr:ResourceKey"/>!<xsl:value-of select="vr:Name"/>'); </xsl:attribute>
              Select                         
            </xsl:element>
          </xsl:if>
          <span class="KEY">Table name:</span>
          <span class="VALUE"><xsl:value-of select="vr:Name"/></span>          
          <!-- COLUMN DETAILS -->
            <xsl:element name="SPAN">                                
              <xsl:attribute name="title">&#10;View column details&#10;</xsl:attribute> 
              <xsl:attribute name="class">agActionButton</xsl:attribute> 
              <xsl:attribute name="onclick">toggle('column:<xsl:value-of select="vr:Name"/>');</xsl:attribute>
              column
            </xsl:element>                              
          <div class="detail">
            <xsl:attribute name="id">column:<xsl:value-of select="vr:Name"/></xsl:attribute>                                
            <xsl:apply-templates select="vs:Column" />
          </div>
        </div>
      </xsl:template>
      
      <xsl:template match="vs:Column">
        <nobr>
          <span class="KEY">Column:</span>
          <span class="VALUE"><xsl:value-of select="vr:Name"/></span>
          <span class="KEY">Desc:</span>
          <span class="VALUE"><xsl:value-of select="vr:Description"/></span>
          <span class="KEY">Type:</span>
          <span class="VALUE"><xsl:value-of select="vs:DataType"/></span>
          <span class="KEY">Units:</span>
          <span class="VALUE"><xsl:value-of select="vs:Unit"/></span>
          <span class="KEY">UCD:</span>
          <span class="VALUE"><xsl:value-of select="vs:UCD"/></span>                                                           
        </nobr><br />
      </xsl:template>
            


      <!-- APPLICATION DEFINITION -->
      <xsl:template match="cea:ApplicationDefinition">          
        <div class="detail">
          <xsl:attribute name="id">appdef:<xsl:value-of select="../vr:Identifier/vr:AuthorityID"/>/<xsl:value-of select="../vr:Identifier/vr:ResourceKey"/></xsl:attribute>            
          <span class="KEY">Parameters:</span>
          <br />
          <xsl:apply-templates select="cea:Parameters/cea:ParameterDefinition" />
          <span class="KEY">Interfaces:</span>
          <xsl:apply-templates select="cea:Interfaces/ceab:Interface" />          
        </div>
      </xsl:template>                  

      <xsl:template match="cea:Parameters/cea:ParameterDefinition">
        <nobr>
          <span class="KEY">Default value:</span>
          <span class="VALUE"><xsl:value-of select="ceapd:UI_Name"/></span>
          <span class="KEY">Description:</span>
          <span class="VALUE"><xsl:value-of select="ceapd:UI_Description"/></span>
        </nobr>
        <br />
      </xsl:template>
      
      <xsl:template match="cea:Interfaces/ceab:Interface">
        <span class="VALUE"><xsl:value-of select="@name"/>,</span>
      </xsl:template>      
      
             

      <!-- XML -->
      <xsl:template name="xml">
        <div class="detail">
          <xsl:attribute name="id">xml:<xsl:value-of select="vr:Identifier/vr:AuthorityID"/>/<xsl:value-of select="vr:Identifier/vr:ResourceKey"/></xsl:attribute>
            <xsl:call-template name="xml-html" select="." mode="showxml" />
        </div>
      </xsl:template>
      
      
      
    <!-- XML TO HTML -->
        <!-- Default node handler, convert to HTML and apply templates -->
        <xsl:template match="*" name="xml-html" mode="showxml">
                <br/> 
                <!--Indent by number of parent nodes -->
                <xsl:for-each select="ancestor::*">
                        <xsl:text>....</xsl:text>
                </xsl:for-each>
                <!-- Open the element -->
                <xsl:text>&lt;</xsl:text>
                <!-- Add the element name -->
                <xsl:value-of select="name()"/>
                <!-- Copy all of the node attributes -->
                <xsl:for-each select="attribute::*">
                        <xsl:text>.</xsl:text>
                        <xsl:value-of select="name()"/>
                        <xsl:text>=</xsl:text>
                        <xsl:text>&quot;</xsl:text>
                        <xsl:value-of select="."/>
                        <xsl:text>&quot;</xsl:text>
                </xsl:for-each>
                <!-- If this node does not have any child nodes -->
                <xsl:if test="count(child::node()) + count(child::text()) + count(child::comment()) = 0">
                        <!-- Close the element -->
                        <xsl:text>/&gt;</xsl:text>                              
                </xsl:if>
                <!-- If this node does have some child nodes -->
                <xsl:if test="count(child::node()) + count(child::text()) + count(child::comment()) > 0">
                        <!-- Close the element -->
                        <xsl:text>&gt;</xsl:text>                       
                        <!-- Add the element content -->
                        <xsl:apply-templates mode="showxml"/>
                        <br/> 
                        <!--Indent by number of parent nodes -->
                        <xsl:for-each select="ancestor::*">
                                <xsl:text>....</xsl:text>
                        </xsl:for-each>
                        <!-- Open the element -->
                        <xsl:text>&lt;</xsl:text>
                        <xsl:text>/</xsl:text>
                        <!-- Add the element name -->
                        <xsl:value-of select="name()"/>
                        <!-- Close the element -->
                        <xsl:text>&gt;</xsl:text>
                </xsl:if>
        </xsl:template>    
          
          
    
    
    <!-- Default, copy all and apply templates -->
    <xsl:template match="@*|node()" priority="-2">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
    <xsl:template match="text()" priority="-1">
        <xsl:value-of select="."/>
    </xsl:template>
    
</xsl:stylesheet>