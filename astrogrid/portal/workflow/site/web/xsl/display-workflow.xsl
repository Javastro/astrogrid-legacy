<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:param name="image_path">/astrogrid-portal/mount/test/</xsl:param>  <!-- path to images -->
    
    <xsl:template match="/*">
      <link rel="stylesheet" type="text/css" href="/astrogrid-portal/mount/test/workflow.css"/>
        <html>
        
        <style type="text/css">
            div.agWorkflow_table_header
            {
              background-color:    blue;
              color:               lightblue;
              text-align:               center;
            }
         </style>
        
            <script language="javascript">
                var image_selected=false;
	     var previously_selected=null;
	     var previously_selected_node = null;
                function change_image(id, node_type)
                { 
                    if (image_selected==false) 
                    {
                        image_selected=true;
                        document.getElementById(id).src="/astrogrid-portal/mount/test/"+node_type+"_selected.gif";
		  previously_selected=id;
		  previously_selected_node = node_type;
                    }
                    else
                    {		
		  document.getElementById(previously_selected).src="/astrogrid-portal/mount/test/"+previously_selected_node+".gif";		       
                        document.getElementById(id).src="/astrogrid-portal/mount/test/"+node_type+"_selected.gif";
		  previously_selected=id;		  
		  previously_selected_node = node_type;		  
                   }
               }

               function populate_tool_details(step_name, join_condition, stepNumber, tool_name, tool_documentation)
               { 
                     if (step_name == '') 
                     {
                         show_select('step_name_button');
                     }
                     else if (step_name != '')
                     {
                         hide_select('step_name_button');                         
                     }
                     if (tool_name == '') 
                     {
                         document.getElementById('tool_select_dropdown').style.display="";
                     }
                     else if (tool_name != '')
                     {
                         document.getElementById('tool_select_dropdown').style.display="none";
                     }                     
                      document.properties_form.step_name.value = step_name;
                      document.properties_form.join_condition.value = join_condition; 
                      document.properties_form.activity_key.value = stepNumber;  
                      document.properties_form.tool_name.value = tool_name;
                      document.properties_form.tool_documentation.value = tool_documentation;                    

               }
               
               
               function show_select(object)
               {
                   if (document.getElementById)
                   {
                       if ( document.getElementById(object) != null)
                           node = document.getElementById(object).style.visibility='visible';
                   }
                   else if (document.layers)
                   {
                       if (document.layers[object] != null)
                           document.layers[object].visibility = 'visible';
                   }
                   else if (document.all)
                   {
                       document.all[object].style.visibility = 'visible';
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
                       } else
                       {
                           display = "none"
                       }
                   }
               }               
                     
               

               function hide_select(object) 
               {
                   if (document.getElementById)
                   {
                       if (document.getElementById(object) != null)
                           node = document.getElementById(object).style.visibility='hidden';
                   }     
                   else if (document.layers)
                   {
                       if (document.layers[object] != null)
                           document.layers[object].visibility = 'hidden';
                   }
                   else if (document.all)
                       document.all[object].style.visibility = 'hidden';
                   }
                   
                                   
                   var popUpWin=0;
                   function popUpWindow(URLStr, left, top, width, height)
                   {
                      if(popUpWin)
                      {
                          if(!popUpWin.closed) popUpWin.close();
                      }
                      popUpWin = open(URLStr, 'popUpWin', 'toolbar=no,location=no,directories=no,status=no,menub ar=no,scrollbar=yes,resizable=yes,copyhistory=yes,width='+width+',height='+height+',left='+left+', top='+top+',screenX='+left+',screenY='+top+'');
                  }                   
                   

            </script>        
        
        
            <body>
                <table width="100%" align="center" border="0">
                    <tr>
                        <td>Name:
                            <b><xsl:value-of select="@name"/></b>
                        </td>
                    </tr>
                </table>
                <table border="0" cellpadding="0" cellspacing="0">  
                    <tr>
                        <xsl:apply-templates select="*"/>
                    </tr>
                </table>
                <xsl:call-template name="tool-details"/>
            </body>
        </html>
    </xsl:template>


    <xsl:template match="*">   
        <tr>
            <xsl:call-template name="format-cells">
                <xsl:with-param name="count" select="count(ancestor::*)"/>
            </xsl:call-template>                    
            <td valign="top" align="left">
                <xsl:attribute name="background">  <!-- prevent gaps appearing in 'trunk' when parameters are viewed -->
                    <xsl:value-of select="$image_path"/>sequence_trunk.gif
                </xsl:attribute>            
                <xsl:choose>          
                            
                    <xsl:when test="name() = 'sequence'">  <!--  SEQUENCE -->
                        <xsl:element name="img">
                            <xsl:attribute name="src"><xsl:value-of select="$image_path"/>sequence.gif</xsl:attribute>
                            <xsl:attribute name="id"><xsl:value-of select="@stepNumber"/></xsl:attribute>
                            <xsl:attribute name="index"><xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>
                            <xsl:attribute name="width">70</xsl:attribute>
                            <xsl:attribute name="height">25</xsl:attribute>
                            <xsl:attribute name="alt">sequence</xsl:attribute>
                            <xsl:attribute name="onMouseOver">change_image('<xsl:value-of select="@stepNumber"/>','<xsl:value-of select="name()"/>');hide_select('step_tool_details');</xsl:attribute>
                        </xsl:element>                                                                                        
                    </xsl:when>
                            
                    <xsl:when test="name() = 'flow'">  <!--  FLOW -->                            
                        <xsl:element name="img">
                            <xsl:attribute name="src"><xsl:value-of select="$image_path"/>flow.gif</xsl:attribute>
                            <xsl:attribute name="id"><xsl:value-of select="@stepNumber"/></xsl:attribute>
                            <xsl:attribute name="index"><xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>
                            <xsl:attribute name="width">70</xsl:attribute>
                            <xsl:attribute name="height">25</xsl:attribute>
                            <xsl:attribute name="alt">flow</xsl:attribute>
                            <xsl:attribute name="onMouseOver">change_image('<xsl:value-of select="@stepNumber"/>','<xsl:value-of select="name()"/>');hide_select('step_tool_details');</xsl:attribute>
                        </xsl:element>                                                                                        
                    </xsl:when>
                            
                    <xsl:when test="name() = 'step'">  <!-- STEP -->
                        <xsl:attribute name="background">  <!-- prevent gaps appearing in 'trunk' when parameters are viewed - not req'd with step -->
                        </xsl:attribute>                    
                        <xsl:element name="img">
                            <xsl:attribute name="src"><xsl:value-of select="$image_path"/>step.gif</xsl:attribute>
                            <xsl:attribute name="id"><xsl:value-of select="@stepNumber"/></xsl:attribute>
                            <xsl:attribute name="index"><xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>
                            <xsl:attribute name="width">70</xsl:attribute>
                            <xsl:attribute name="height">25</xsl:attribute>
                            <xsl:attribute name="alt">step</xsl:attribute>
                            <xsl:attribute name="onMouseOver">change_image('<xsl:value-of select="@stepNumber"/>','<xsl:value-of select="name()"/>'); populate_tool_details('<xsl:value-of select="@name"/>','<xsl:value-of select="@joinCondition"/>','<xsl:value-of select="@stepNumber"/>', '<xsl:value-of select="./tool/@name"/>','<xsl:value-of select="./tool/documentation"/>'); show_select('step_tool_details');</xsl:attribute>             
                            <xsl:attribute name="onClick">toggle('parameters:<xsl:value-of select="@stepNumber"/>');</xsl:attribute>
                        </xsl:element>
                        <td>
                            <xsl:attribute name="colspan">20</xsl:attribute>
                        </td>
                        <td rowspan="30" valign="top">
                            <div style="display: none;" >   <!-- Holder for parameter table -->
                                <xsl:attribute name="id">parameters:<xsl:value-of select="@stepNumber"/></xsl:attribute>
                                <table border="0" cellpadding="0" cellspacing="0">
                                    <tr>
                                        <td valign="top">                                        
                                            <xsl:element name="img">
                                                <xsl:attribute name="src"><xsl:value-of select="$image_path"/>left_arrow.png</xsl:attribute>
                                                <xsl:attribute name="onClick">toggle('parameters:<xsl:value-of select="@stepNumber"/>');</xsl:attribute>
                                                <xsl:attribute name="width">20</xsl:attribute>
                                                <xsl:attribute name="height">20</xsl:attribute>                            
                                                <xsl:attribute name="alt">hide</xsl:attribute>
                                            </xsl:element>                                                                                                                                                              
                                        </td>
                                        <td>  <!-- INPUT/OUTPUT PARAMETER DISPLAY -->
                                            <xsl:call-template name="parameter-details"/>
                                        </td>                                        
                                    </tr>
                                </table>
                            </div>
                        </td>
                    </xsl:when>                                                                            
                </xsl:choose>
            </td>
        </tr>
        <tr>
            <xsl:apply-templates select="*"/>
        </tr>

        
    </xsl:template>


  <!--+
        | Blank cells (or containing images) for table.
       +-->
    <xsl:template name="format-cells">
        <xsl:param name="count"/>                         <!-- No. of ancestors for this node = no. columns required prior to displaying it-->
        <xsl:param name="counter" select="1"/>      <!-- Loop counter (needs to increment so that table can be formatted correctly -->
            <xsl:if test="$counter != $count">             <!-- Test to see if column should display details -->
                <td valign="top">               
                    <xsl:for-each select="ancestor::*">    <!-- Display horizontal sequence image in relevant column -->
                        <xsl:if test="name() = 'sequence'">
                            <xsl:if test="count(ancestor::*) = $counter -1">
                                <xsl:attribute name="background">  <!-- prevent gaps appearing in 'trunk' when parameters are viewed -->
                                    <xsl:value-of select="$image_path"/>sequence_trunk.gif
                                </xsl:attribute> 
                                <xsl:element name="img">
                                    <xsl:attribute name="src"><xsl:value-of select="$image_path"/>sequence_trunk.gif</xsl:attribute>
                                    <xsl:attribute name="width">70</xsl:attribute>
                                    <xsl:attribute name="height">25</xsl:attribute>
                                </xsl:element>                                
                            </xsl:if>
                        </xsl:if>
                    </xsl:for-each>                                                              
                </td>            
             <xsl:call-template name="format-cells">
                 <xsl:with-param name="counter" select="$counter +1"/>
                 <xsl:with-param name="count" select="$count"/>
             </xsl:call-template>
        </xsl:if>
        <xsl:if test="$counter = $count">
            <td valign="top" align="center">
                <xsl:choose>                        
                    <xsl:when test="count(following-sibling::*) != 0">                     
                        <xsl:attribute name="background">  <!-- prevent gaps appearing in 'trunk' when parameters are viewed -->
                            <xsl:value-of select="$image_path"/>sequence_trunk.gif
                        </xsl:attribute>                                              
                        <xsl:element name="img">
                            <xsl:attribute name="src"><xsl:value-of select="$image_path"/>arrow.gif</xsl:attribute>
                            <xsl:attribute name="width">70</xsl:attribute>
                            <xsl:attribute name="height">25</xsl:attribute>
                        </xsl:element>
                    </xsl:when>
                    <xsl:when test="count(following-sibling::*) = 0"> <!-- if there are no following siblings then display bottom arrow image -->
                        <xsl:element name="img">
                            <xsl:attribute name="src"><xsl:value-of select="$image_path"/>arrow_bottom.gif</xsl:attribute>
                            <xsl:attribute name="width">70</xsl:attribute>
                            <xsl:attribute name="height">25</xsl:attribute>
                        </xsl:element>
                    </xsl:when>
                </xsl:choose>            
            </td>
        </xsl:if>
    </xsl:template>


    <!--+
          | Match the community element.
          +-->
    <xsl:template match="community"/>


    <!--+
          | Match the description element.
          +-->
    <xsl:template match="description">
        <tr>
            <td colspan="10">Description:
                <b><xsl:value-of select="."/></b>
            </td>
        </tr>    
        <tr>
            <td>
                <p/>
            </td>
        </tr>        
    </xsl:template>
    
    
    <!--+
          | Match the documentation element.
          | TODO: include as hidden parameters in page          
          +-->
    <xsl:template match="documentation"/>
     
    
    <!--+
          | Match the output element.
          | TODO: include as hidden parameters in page          
          +-->
    <xsl:template match="output"/>
    
  <!--+
          | Match the tool element.
          | TODO: include as hidden parameters in page          
          +-->
    <xsl:template match="tool"></xsl:template>    
 
 
    <xsl:include href="display-tool.xsl"/>
    <xsl:include href="display-parameters.xsl"/>
    
</xsl:stylesheet>
