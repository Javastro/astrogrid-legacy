<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!--+ 
       | Author: Phil Nicolson "pjn3@star.le.ac.uk"
       | Date:   Sept 2004
       +-->
       
    <xsl:param name="image_path">/astrogrid-portal/mount/workflow/</xsl:param>  <!-- path to images -->
    <xsl:include href="display-parameters.xsl"/>
     
    <xsl:template match="workflow">
    <ag-div>   
        

    <script language="javascript">
        
    /*
    * Note: javascript functions included in xsl here as using innerFrame causes problems in IE when trying
    * to include seperate script files (although causes no problem with Mozilla???)
    */            
        
    /*
     * showTable()
     *
     * Changes display setting of div
     *
     * @param id        id of div
     * @param count     count of executionRecord an activity contains - if greater than 10 do not show footer
     */
    var table_selected = false;
	var previously_selected_table = null;
    function show_table(id, footer)    
    {     
        if (table_selected==false) 
        {
            table_selected=true;
            if (parent.document.getElementById(id))
            {
              parent.document.getElementById(id).style.display="";
		      previously_selected_table=id;
		    }
        }
        else
        {	
            if ( parent.document.getElementById(previously_selected_table) )
            {
		      parent.document.getElementById(previously_selected_table).style.display="none"; 
		    }
            parent.document.getElementById(id).style.display="";
		    previously_selected_table=id;
		    table_selected = true;		  
        }
        if (footer) 
        {
            if(parent.document.getElementById("imgfootbar")!= null) 
            {
                parent.document.getElementById("imgfootbar").style.display = "";
            }
        }
        else
        {
            if(parent.document.getElementById("imgfootbar")!= null) 
            {
                parent.document.getElementById("imgfootbar").style.display = "none";
            }
        }        
    }
        
    
    var image_selected=false;
	var previously_selected=null;
	var previously_selected_node = null;
    function change_image(id, node_type)
    {       
        if (image_selected==false) 
        {
            image_selected=true;
            document.getElementById(id).src="/astrogrid-portal/mount/workflow/"+node_type+"_selected.gif";
		    previously_selected=id;
		    previously_selected_node = node_type;
        }
        else
        {
            if ( document.getElementById(previously_selected) ) 
            { 
		        document.getElementById(previously_selected).src="/astrogrid-portal/mount/workflow/"+previously_selected_node+".gif";		       
		    }
            document.getElementById(id).src="/astrogrid-portal/mount/workflow/"+node_type+"_selected.gif";
		    previously_selected=id;		  
		    previously_selected_node = node_type;		  
        }
    }
    
    function toggle(id) 
    {      
        var element = document.getElementById(id);
        with (element.style) 
        {
            if ( display == "none" )
            {
                display = ""
            } 
            else
            {
                display = "none"
            }
        }
    }
    
            
    </script>                             
      <table border="0" cellpadding="0" cellspacing="0">  
        <xsl:apply-templates select="*" mode="job-status"/>
      </table>          
    </ag-div>
  </xsl:template>

    <xsl:template match="*" mode="job-status"> 
      <tr>          
        <xsl:call-template name="format-cells-status">
          <xsl:with-param name="count" select="count(ancestor::*)"/>
        </xsl:call-template>             
          
        <td valign="top" align="left">
          <xsl:if test="name() = 'step'">
            <xsl:attribute name="style">cursor: help</xsl:attribute>            
          </xsl:if>            
          <xsl:element name="a">
            <xsl:attribute name="name"><xsl:value-of select="@key"/></xsl:attribute>
          </xsl:element>
          <xsl:choose>                                      
                                                                                                          
            <xsl:when test="name() = 'step'"> <!-- STEP -->                                     
              <xsl:attribute name="background">  <!-- prevent gaps appearing in 'trunk' when parameters are viewed - not req'd with step -->
              </xsl:attribute>                    
                <img width="70" height="25" border="0" alt="step">
                  <xsl:attribute name="src"><xsl:value-of select="$image_path"/><xsl:value-of select="name()"/>.gif</xsl:attribute>
                  <xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute>
                  <xsl:attribute name="index"><xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>
                  <xsl:attribute name="onMouseOver">
                                 change_image('<xsl:value-of select="@key"/>','<xsl:value-of select="name()"/>');                                                                                  
                                 show_table('<xsl:value-of select="@key"/>','<xsl:if test="count(descendant::*[(name()='executionRecord')]) &lt; 2">true</xsl:if>');
                  </xsl:attribute>
                  <xsl:attribute name="onClick">toggle('parameters:<xsl:value-of select="@key"/>');</xsl:attribute>
                </img>
              <td colspan="30" valign="top" style="color: blue;">
                <font size="-1">Name: <b><xsl:value-of select="@step-name"/></b>, Status: <b><xsl:value-of select="@step-status"/></b></font>                
              </td>
              <td rowspan="30" valign="top">
                  <div style="display: none;" >   <!-- Holder for parameter table -->
                    <xsl:attribute name="id">parameters:<xsl:value-of select="@key"/></xsl:attribute>
                      <table border="0" cellpadding="0" cellspacing="0">
                        <tr>
                          <td valign="top">
                            <img width="20" height="20" alt="hide">
                              <xsl:attribute name="src"><xsl:value-of select="$image_path"/>left_arrow.png</xsl:attribute>
                              <xsl:attribute name="onClick">toggle('parameters:<xsl:value-of select="@key"/>');</xsl:attribute>                                                
                            </img>                                     
                          </td>
                          <td>  <!-- INPUT/OUTPUT PARAMETER DISPLAY -->
                            <xsl:call-template name="parameter-details-status" />
                          </td>                                        
                        </tr>
                      </table>
                    </div>
                  </td>              
            </xsl:when>
            
            <xsl:when test="name() = 'script'"> <!-- SCRIPT -->                                                         
              <img width="70" height="25" border="0" alt="script">
                <xsl:attribute name="src"><xsl:value-of select="$image_path"/><xsl:value-of select="name()"/>.gif</xsl:attribute>
                <xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute>
                <xsl:attribute name="index"><xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>                  
                <xsl:attribute name="onMouseOver">
                               change_image('<xsl:value-of select="@key"/>','<xsl:value-of select="name()"/>');                                                                                  
                               show_table('<xsl:value-of select="@key"/>','<xsl:if test="count(descendant::*[(name()='executionRecord')]) &lt; 2">true</xsl:if>');
                </xsl:attribute>
              </img>  
              <td colspan="30" valign="middle" style="color: blue;">
                <font size="-1">Status: <b><xsl:value-of select="@script-status"/></b></font>               
              </td>
            </xsl:when>            
                    
            <xsl:otherwise> <!--  All OTHER ACTIVIIES -->            
              <img width="70" height="25" border="0">
                <xsl:attribute name="src"><xsl:value-of select="$image_path"/><xsl:value-of select="name()"/>.gif</xsl:attribute>
                <xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute>
                <xsl:attribute name="index"><xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>                  
                <xsl:attribute name="alt"><xsl:value-of select="name()"/></xsl:attribute>
                <xsl:attribute name="onMouseOver">
                               change_image('<xsl:value-of select="@key"/>','<xsl:value-of select="name()"/>');                                                                                  
                               show_table('<xsl:value-of select="@key"/>','<xsl:if test="count(descendant::*[(name()='executionRecord')]) &lt; 2">true</xsl:if>');
                </xsl:attribute>                               
              </img>                                        
            </xsl:otherwise>                                                                                                                    
           
          </xsl:choose>
        </td>
      </tr>
      <xsl:apply-templates select="*" mode="job-status"/>                        
    </xsl:template>


  <!--+
        | Blank cells (or containing images) for table.
       +-->
    <xsl:template name="format-cells-status">
      <xsl:param name="count"/>                         <!-- No. of ancestors for this node = no. columns required prior to displaying it-->
      <xsl:param name="counter" select="1"/>            <!-- Loop counter (needs to increment so that table can be formatted correctly -->
        <xsl:if test="$counter != $count">             <!-- Test to see if column should display details -->
          <td valign="top">
            <xsl:for-each select="ancestor::*">    <!-- Display vertical sequence image in relevant column -->                                                            
              <xsl:if test="count(ancestor::*) = $counter ">
                <xsl:if test="count(following-sibling::*[not(name()='toolsAvailable')]) != 0">
                  <xsl:attribute name="background">  <!-- prevent gaps appearing in 'trunk' when parameters are viewed -->
                    <xsl:value-of select="$image_path"/>sequence_trunk.gif
                  </xsl:attribute>
                  <img width="70" height="25" border="0">
                    <xsl:attribute name="src"><xsl:value-of select="$image_path"/>sequence_trunk.gif</xsl:attribute>
                  </img>                                              
                </xsl:if>     
              </xsl:if>                                                                       
            </xsl:for-each>                                                              
          </td>                          
        <xsl:call-template name="format-cells-status">
          <xsl:with-param name="counter" select="$counter +1"/>
          <xsl:with-param name="count" select="$count"/>
        </xsl:call-template>             
      </xsl:if>
      <xsl:if test="$counter = $count">              
        <td valign="top" align="center">             
          <xsl:choose>
            <xsl:when test="name() = 'toolsAvailable'"/>                                                      
              <xsl:when test="count(following-sibling::*[not(name()='toolsAvailable')]) != 0">                                         
                <xsl:attribute name="background">  <!-- prevent gaps appearing in 'trunk' when parameters are viewed -->
                  <xsl:value-of select="$image_path"/>sequence_trunk.gif
                </xsl:attribute>
              <img width="70" height="25" border="0">
                <xsl:attribute name="src"><xsl:value-of select="$image_path"/>arrow.gif</xsl:attribute>
              </img>                                                          
            </xsl:when>
          <xsl:otherwise> <!-- if there are no following siblings then display bottom arrow image --> 
            <img width="70" height="25" border="0">
              <xsl:attribute name="src"><xsl:value-of select="$image_path"/>arrow_bottom.gif</xsl:attribute>
            </img>                   
          </xsl:otherwise>
        </xsl:choose>            
      </td>
    </xsl:if>
  </xsl:template>

  <xsl:template match="tool" mode="job-status"/>  
  <xsl:template match="inputParam" mode="job-status"/>  
  <xsl:template match="outputParam" mode="job-status"/>  
  <xsl:template match="executionRecord" mode="job-status"/>
  <xsl:template match="workflowExecutionRecord" mode="job-status"/>  
  <xsl:template match="message" mode="job-status"/>      

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
