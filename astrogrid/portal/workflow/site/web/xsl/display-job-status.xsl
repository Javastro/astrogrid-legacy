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
              <input type="text" size="40" name="workflow-name" readonly="true">                        
                <xsl:attribute name="value"><xsl:value-of select="@workflow-name"/></xsl:attribute>
              </input> 
            </td>
            <td nowrap="true">Workflow submitted: </td>
            <td>
              <input type="text" size="40" name="workflow-start-time" readonly="true">
                <xsl:attribute name="value"><xsl:value-of select="@workflow-start-time"/></xsl:attribute>
              </input>
            </td>
            <td rowspan="2" valign="bottom">
              <form action="/astrogrid-portal/main/mount/workflow/agjobmanager-printer-friendly.html" name="printer_form" id="printer_form">
                <input class="agActionButton" type="submit" name="action" value="Workflow transcript"/>
                <input type="hidden" name="jobURN" id="jobURN"/>
              </form>
            </td>
          </tr>            
          <tr>
            <td>Description:</td>
            <td>
              <input type="text" size="40" name="workflow-description" readonly="true">
                <xsl:attribute name="value"><xsl:value-of select="@workflow-description"/></xsl:attribute>
              </input>
            </td>
            <td nowrap="true">Overall status: </td>
            <td>
              <input type="text" size="40" name="workflow-status" readonly="true">                  
                <xsl:attribute name="value"><xsl:value-of select="@workflow-status"/></xsl:attribute>
              </input>
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
            <xsl:call-template name="format-cells">
                <xsl:with-param name="count" select="count(ancestor::*)"/>
            </xsl:call-template>             
          
            <td valign="top" align="left">
              <xsl:element name="a">
                <xsl:attribute name="name"><xsl:value-of select="@key"/></xsl:attribute>
              </xsl:element>
                <xsl:choose>                                      
                                                                                                          
                    <xsl:when test="name() = 'step'">  <!-- STEP -->                   
                        <xsl:attribute name="background">  <!-- prevent gaps appearing in 'trunk' when parameters are viewed - not req'd with step -->
                        </xsl:attribute>
                        <img width="70" height="25" alt="step">                                            
                            <xsl:attribute name="src"><xsl:value-of select="$image_path"/>step.gif</xsl:attribute>
                            <xsl:attribute name="index"><xsl:value-of select="count(preceding-sibling::*)"/></xsl:attribute>
                        </img>
                        <td colspan="30" valign="middle" style="color: blue;">
                            <font size="-1">
                                Name: <b><xsl:value-of select="@step-name"/></b>, Status: <b><xsl:value-of select="@step-status"/> </b>
                                <xsl:element name="a">
                                    <xsl:attribute name="style">padding-left: 10;</xsl:attribute>
                                    <xsl:attribute name="href">javascript:void(0);</xsl:attribute>
                                    <xsl:attribute name="onMouseOver">this.T_TITLE='' +
                                    'Step: <xsl:value-of select="normalize-space(@step-name)" /> &lt;br/&gt;'+
                                    ' Status: <xsl:value-of select="@step-status" />'; this.T_WIDTH=250; this.T_DELAY=500; this.T_STICKY=true; return escape('' +
                                    ' Desc: <xsl:value-of select="normalize-space(@step-description)"/> &lt;br/&gt; ' + 
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
                    
                    <xsl:otherwise>  <!--  All OTHER ACTIVIIES --> 
                        <img width="70" height="25">      
                            <xsl:attribute name="src"><xsl:value-of select="$image_path"/><xsl:value-of select="name()"/>.gif</xsl:attribute>
                            <xsl:attribute name="alt"><xsl:value-of select="name()"/></xsl:attribute>
                        </img>
                    </xsl:otherwise>
                                                                                                                    
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
                        <xsl:if test="count(ancestor::*) = $counter ">
                            <xsl:if test="count(following-sibling::*[not(name()='toolsAvailable')]) != 0">
                                <xsl:attribute name="background">  <!-- prevent gaps appearing in 'trunk' when parameters are viewed -->
                                    <xsl:value-of select="$image_path"/>sequence_trunk.gif
                                </xsl:attribute>
                                <img width="70" height="25">
                                    <xsl:attribute name="src"><xsl:value-of select="$image_path"/>sequence_trunk.gif</xsl:attribute>
                                </img>                                              
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
                        <img width="70" height="25">
                            <xsl:attribute name="src"><xsl:value-of select="$image_path"/>arrow.gif</xsl:attribute>
                        </img>                                                          
                    </xsl:when>
                    <xsl:otherwise> <!-- if there are no following siblings then display bottom arrow image --> 
                        <img width="70" height="25">
                            <xsl:attribute name="src"><xsl:value-of select="$image_path"/>arrow_bottom.gif</xsl:attribute>
                        </img>                   
                    </xsl:otherwise>
                </xsl:choose>            
            </td>
        </xsl:if>
    </xsl:template>

    <xsl:template match="tool"/>

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
