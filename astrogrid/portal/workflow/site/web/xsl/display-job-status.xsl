<?xml version="1.0"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:param name="image_path">/astrogrid-portal/mount/workflow/</xsl:param>  <!-- path to images -->
    
    <xsl:template match="workflow">
    <ag-div>        
       <agComponentTitle>Workflow</agComponentTitle>        
                
        <table border="1" cellpadding="0" cellspacing="0">
            <tr>
                <td>Name:</td>
                <td>                        
                    <xsl:element name="input">
                        <xsl:attribute name="type">text</xsl:attribute>
                        <xsl:attribute name="size">40</xsl:attribute>
                        <xsl:attribute name="value"><xsl:value-of select="@workflow-name"/></xsl:attribute>
                        <xsl:attribute name="name">workflow-name</xsl:attribute>
                        <xsl:attribute name="READONLY">true</xsl:attribute>                            
                    </xsl:element>           
                </td>
                <td nowrap="true">Workflow submitted: </td>
                <td>
                    <xsl:element name="input">
                        <xsl:attribute name="type">text</xsl:attribute>
                        <xsl:attribute name="size">40</xsl:attribute>
                        <xsl:attribute name="value"><xsl:value-of select="@workflow-start-time"/></xsl:attribute>
                        <xsl:attribute name="name">workflow-start-time</xsl:attribute>
                        <xsl:attribute name="READONLY">true</xsl:attribute>
                    </xsl:element>                  
                </td>
            </tr>            
            <tr>
                <td>Description:</td>
                <td>
                    <xsl:element name="input">
                        <xsl:attribute name="type">text</xsl:attribute>
                        <xsl:attribute name="size">40</xsl:attribute>
                        <xsl:attribute name="value"><xsl:value-of select="@workflow-description"/></xsl:attribute>
                        <xsl:attribute name="name">workflow-description</xsl:attribute>
                        <xsl:attribute name="READONLY">true</xsl:attribute>
                    </xsl:element>                  
                </td>
                <td nowrap="true">Overall status: </td>
                <td>
                    <xsl:element name="input">
                        <xsl:attribute name="type">text</xsl:attribute>
                        <xsl:attribute name="size">40</xsl:attribute>
                        <xsl:attribute name="value"><xsl:value-of select="@workflow-status"/></xsl:attribute>
                        <xsl:attribute name="name">workflow-status</xsl:attribute>
                        <xsl:attribute name="READONLY">true</xsl:attribute>
                    </xsl:element>                  
                </td>
            </tr>                                                           
        </table>                     
        <table border="0" cellpadding="0" cellspacing="0">  
            <tr>
                <xsl:apply-templates select="*"/>
            </tr>
        </table>
    </ag-div>
    <script type="text/javascript" src="/astrogrid-portal/mount/workflow/wz_tooltip.js"/>
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
          <xsl:if test="name() = 'script'">
            <xsl:call-template name="format-cells">
                <xsl:with-param name="count" select="count(ancestor::*)"/>
            </xsl:call-template>                    
          </xsl:if>                      
            <td valign="top" align="left">
                <xsl:choose>                                      
                    <xsl:when test="name() = 'sequence'">  <!--  SEQUENCE -->                             
                        <xsl:element name="img">
                            <xsl:attribute name="src"><xsl:value-of select="$image_path"/>sequence.gif</xsl:attribute>
                            <xsl:attribute name="index"><xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>
                            <xsl:attribute name="width">70</xsl:attribute>
                            <xsl:attribute name="height">25</xsl:attribute>
                            <xsl:attribute name="alt">sequence</xsl:attribute>
                        </xsl:element>
                    </xsl:when>
                            
                    <xsl:when test="name() = 'flow'">  <!--  FLOW -->                            
                        <xsl:element name="img">
                            <xsl:attribute name="src"><xsl:value-of select="$image_path"/>flow.gif</xsl:attribute>
                            <xsl:attribute name="index"><xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>
                            <xsl:attribute name="width">70</xsl:attribute>
                            <xsl:attribute name="height">25</xsl:attribute>
                            <xsl:attribute name="alt">flow</xsl:attribute>
                        </xsl:element>                                                                                        
                    </xsl:when>
                            
                    <xsl:when test="name() = 'step'">  <!-- STEP -->                   
                        <xsl:attribute name="background">  <!-- prevent gaps appearing in 'trunk' when parameters are viewed - not req'd with step -->
                        </xsl:attribute>                    
                        <xsl:element name="img">
                            <xsl:attribute name="src"><xsl:value-of select="$image_path"/>step.gif</xsl:attribute>
                            <xsl:attribute name="index"><xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>
                            <xsl:attribute name="width">70</xsl:attribute>
                            <xsl:attribute name="height">25</xsl:attribute>
                            <xsl:attribute name="alt">step</xsl:attribute>
                        </xsl:element>                       
                        <td colspan="30" valign="middle" style="color: blue;">
                            <font size="-1">
                                Name: <b><xsl:value-of select="@step-name"/></b>, Status: <b><xsl:value-of select="@step-status"/> </b>
                                <xsl:element name="a">
                                    <xsl:attribute name="style">padding-left: 10;</xsl:attribute>
                                    <xsl:attribute name="href">javascript:void(0);</xsl:attribute>
                                    <xsl:attribute name="onMouseOver">this.T_TITLE='' +
                                    'Step: <xsl:value-of select="@step-name" /> &lt;br/&gt;'+
                                    ' Status: <xsl:value-of select="@step-status" />'; this.T_WIDTH=250; this.T_DELAY=500; return escape('' +
                                    ' Desc: <xsl:value-of select="@step-description"/> &lt;br/&gt; ' + 
                                    ' Start: <xsl:value-of select="@step-start-time"/> &lt;br/&gt; ' +
                                    ' Finish: <xsl:value-of select="@step-finish-time"/> &lt;br/&gt; ' +
                                    ' Join: <xsl:value-of select="@step-join-condition"/> &lt;br/&gt; ' +
                                    ' Message: <xsl:value-of select="@step-message"/> ');</xsl:attribute>
                                </xsl:element>
                                <small><b>(more)</b></small>
                                </font>
                            <xsl:element name="/a"></xsl:element>                                                       
                        </td>
                    </xsl:when>
                    
                    <xsl:when test="name() = 'script'">  <!--  SCRIPT -->                            
                        <xsl:element name="img">
                            <xsl:attribute name="src"><xsl:value-of select="$image_path"/>script.gif</xsl:attribute>
                            <xsl:attribute name="index"><xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>
                            <xsl:attribute name="width">70</xsl:attribute>
                            <xsl:attribute name="height">25</xsl:attribute>
                            <xsl:attribute name="alt">script</xsl:attribute>
                        </xsl:element>                                                                                        
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


<!-- 
    <xsl:include href="display-tool.xsl"/>
    <xsl:include href="display-parameters.xsl"/>
-->

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
