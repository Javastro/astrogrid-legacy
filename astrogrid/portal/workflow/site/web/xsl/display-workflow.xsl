<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:param name="image_path">/astrogrid-portal/mount/workflow/</xsl:param>  <!-- path to images -->
    <xsl:param name="activity_key" />
    <xsl:param name="display_parameter_values" />
    <xsl:param name="display_tool_values" />
    
    <xsl:template match="workflow">
    <ag-div>        
       <agComponentTitle>Workflow</agComponentTitle>        
        <ag-script type="text/javascript" src="/astrogrid-portal/mount/workflow/workflow-functions.js"/>
<!--        <ag-link rel="stylesheet" type="text/css" href="/astrogrid-portal/mount/workflow/workflow.css"/>   -->
        <xsl:if test="$activity_key != ''">
          <xsl:if test="$display_parameter_values = 'true'">
            <ag-onload>
              <xsl:attribute name="function">toggle('parameters:<xsl:value-of select="$activity_key"/>');</xsl:attribute>
            </ag-onload>
          </xsl:if>
        </xsl:if>
                
        <table border="1">
            <form action="/astrogrid-portal/main/mount/workflow/agjobmanager.html" name="workflow_form">
                <tr>
                    <td colspan="4">
                        <ag-menu name="workflow-menu"/>
                    </td>
                </tr>
                <tr>
                    <td width="30">Name:</td>
                    <td>                        
                        <xsl:element name="input">
                            <xsl:attribute name="type">text</xsl:attribute>
                            <xsl:attribute name="size">40</xsl:attribute>
                            <xsl:attribute name="value"><xsl:value-of select="@workflow-name"/></xsl:attribute>
                            <xsl:attribute name="name">workflow-name</xsl:attribute>
                        </xsl:element>           
                    </td>
                    <td rowspan="2">                        
                        <input type="submit" name="action" value="add-name-description" />
                    </td>
                    <td>
                        ivorn:<input type="text" name="open-workflow-ivorn" id="open-workflow-ivorn"/>
                        agsl:<input type="text" name="open-workflow-agsl" id="open-workflow-agsl"/>                        
                        <input type="submit" name="action" value="read-workflow" />                        
                    </td>                                
                </tr>
                <tr>
                    <td width="30">Description:</td>
                    <td>
                        <xsl:element name="input">
                            <xsl:attribute name="type">text</xsl:attribute>
                            <xsl:attribute name="size">40</xsl:attribute>
                            <xsl:attribute name="value"><xsl:value-of select="@workflow-description"/></xsl:attribute>
                            <xsl:attribute name="name">workflow-description</xsl:attribute>
                        </xsl:element>                  
                    </td>
                    <td>
                        ivorn:<input type="text" name="save-workflow-ivorn" id="save-workflow-ivorn"/>
                        agsl:<input type="text" name="save-workflow-agsl" id="save-workflow-agsl"/>
                        <input type="submit" name="action" value="save-workflow"/>                                                                       
                    </td>
                </tr>
            </form>                    
        </table>                     
        <table border="0" cellpadding="0" cellspacing="0">  
            <tr>
                <xsl:apply-templates select="*"/>
            </tr>
        </table>
    <xsl:call-template name="tool-details"/>
    </ag-div>
    </xsl:template>

    <xsl:template match="*"> 
        <tr>
          <xsl:if test="name() = 'sequence'">
            <xsl:call-template name="format-cells">
                <xsl:with-param name="count" select="count(ancestor::*)"/>
            </xsl:call-template>                    
          </xsl:if>
          <xsl:if test="name() = 'flow'">
            <xsl:call-template name="format-cells">
                <xsl:with-param name="count" select="count(ancestor::*)"/>
            </xsl:call-template>                    
          </xsl:if> 
          <xsl:if test="name() = 'step'">
            <xsl:call-template name="format-cells">
                <xsl:with-param name="count" select="count(ancestor::*)"/>
            </xsl:call-template>                    
          </xsl:if>            
            <td valign="top" align="left">
                <xsl:choose>                                      
                    <xsl:when test="name() = 'sequence'">  <!--  SEQUENCE -->                             
                        <xsl:element name="img">
                            <xsl:attribute name="src"><xsl:value-of select="$image_path"/>sequence.gif</xsl:attribute>
                            <xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute>
                            <xsl:attribute name="index"><xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>
                            <xsl:attribute name="width">70</xsl:attribute>
                            <xsl:attribute name="height">25</xsl:attribute>
                            <xsl:attribute name="alt">sequence</xsl:attribute>
                            <xsl:attribute name="onMouseOver">change_image('<xsl:value-of select="@key"/>','<xsl:value-of select="name()"/>');hide_select('step_tool_details');populate_activity_container_insert_form('<xsl:value-of select="@key"/>','<xsl:value-of select="count(preceding-sibling::*)"/>');</xsl:attribute>
                            <xsl:if test="count(child::*) = 0">
                                <ag-onload function="change_image('/sequence','sequence');populate_activity_container_insert_form('/sequence','0');"/>
                            </xsl:if>
                        </xsl:element>
                    </xsl:when>
                            
                    <xsl:when test="name() = 'flow'">  <!--  FLOW -->                            
                        <xsl:element name="img">
                            <xsl:attribute name="src"><xsl:value-of select="$image_path"/>flow.gif</xsl:attribute>
                            <xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute>
                            <xsl:attribute name="index"><xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>
                            <xsl:attribute name="width">70</xsl:attribute>
                            <xsl:attribute name="height">25</xsl:attribute>
                            <xsl:attribute name="alt">flow</xsl:attribute>
                            <xsl:attribute name="onMouseOver">change_image('<xsl:value-of select="@key"/>','<xsl:value-of select="name()"/>');hide_select('step_tool_details');populate_activity_container_insert_form('<xsl:value-of select="@key"/>','<xsl:value-of select="count(preceding-sibling::*)"/>');</xsl:attribute>
                        </xsl:element>                                                                                        
                    </xsl:when>
                            
                    <xsl:when test="name() = 'step'">  <!-- STEP -->                   
                        <xsl:attribute name="background">  <!-- prevent gaps appearing in 'trunk' when parameters are viewed - not req'd with step -->
                        </xsl:attribute>                    
                        <xsl:element name="img">
                            <xsl:attribute name="src"><xsl:value-of select="$image_path"/>step.gif</xsl:attribute>
                            <xsl:attribute name="id"><xsl:value-of select="@key"/></xsl:attribute>
                            <xsl:attribute name="index"><xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>
                            <xsl:attribute name="width">70</xsl:attribute>
                            <xsl:attribute name="height">25</xsl:attribute>
                            <xsl:attribute name="alt">step</xsl:attribute>
                            <xsl:attribute name="onMouseOver">change_image('<xsl:value-of select="@key"/>','<xsl:value-of select="name()"/>'); populate_tool_details('<xsl:value-of select="@step-name"/>','<xsl:value-of select="@joinCondition"/>','<xsl:value-of select="@step-description"/>','<xsl:value-of select="@key"/>', '<xsl:value-of select="./tool/@tool-name"/>','<xsl:value-of select="./tool/@tool-documentation"/>'); show_select('step_tool_details');populate_activity_container_insert_form('<xsl:value-of select="../@key"/>','<xsl:value-of select="count(preceding-sibling::*)"/>');footer();</xsl:attribute>             
                            <xsl:attribute name="onClick">toggle('parameters:<xsl:value-of select="@key"/>');footer();</xsl:attribute>
                        </xsl:element>                       
                        <td>
                            <xsl:attribute name="colspan">20</xsl:attribute>
                        </td>
                        <td rowspan="30" valign="top">
                            <div style="display: none;" >   <!-- Holder for parameter table -->
                                <xsl:attribute name="id">parameters:<xsl:value-of select="@key"/></xsl:attribute>
                                <table border="0" cellpadding="0" cellspacing="0">
                                    <tr>
                                        <td valign="top">                                        
                                            <xsl:element name="img">
                                                <xsl:attribute name="src"><xsl:value-of select="$image_path"/>left_arrow.png</xsl:attribute>
                                                <xsl:attribute name="onClick">toggle('parameters:<xsl:value-of select="@key"/>');</xsl:attribute>
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
        <xsl:param name="counter" select="1"/>            <!-- Loop counter (needs to increment so that table can be formatted correctly -->
            <xsl:if test="$counter != $count">             <!-- Test to see if column should display details -->
                <td valign="top">
                    <xsl:for-each select="ancestor::*">    <!-- Display vertical sequence image in relevant column -->                    
                        <xsl:if test="name() = 'sequence'">                           
                            <xsl:if test="count(ancestor::*) = $counter ">
                                <xsl:if test="count(following-sibling::*[not(name()='toolsAvailable')]) != 0">
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
                        </xsl:if>                                                                      
                        <xsl:if test="name() = 'flow'">                           
                            <xsl:if test="count(ancestor::*) = $counter ">
                                <xsl:if test="count(following-sibling::*[not(name()='toolsAvailable')]) != 0">
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
                    <xsl:when test="name() = 'toolsAvailable'"/>                                                      
                    <xsl:when test="count(following-sibling::*[not(name()='toolsAvailable')]) != 0">                                         
                        <xsl:attribute name="background">  <!-- prevent gaps appearing in 'trunk' when parameters are viewed -->
                            <xsl:value-of select="$image_path"/>sequence_trunk.gif
                        </xsl:attribute>                                              
                        <xsl:element name="img">
                            <xsl:attribute name="src"><xsl:value-of select="$image_path"/>arrow.gif</xsl:attribute>
                            <xsl:attribute name="width">70</xsl:attribute>
                            <xsl:attribute name="height">25</xsl:attribute>
                        </xsl:element>
                    </xsl:when>
                    <xsl:otherwise> <!-- if there are no following siblings then display bottom arrow image -->                    
                        <xsl:element name="img">
                            <xsl:attribute name="src"><xsl:value-of select="$image_path"/>arrow_bottom.gif</xsl:attribute>
                            <xsl:attribute name="width">70</xsl:attribute>
                            <xsl:attribute name="height">25</xsl:attribute>
                        </xsl:element>
                    </xsl:otherwise>
                </xsl:choose>            
            </td>
        </xsl:if>
    </xsl:template>

 
    <xsl:include href="display-tool.xsl"/>
    <xsl:include href="display-parameters.xsl"/>


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
