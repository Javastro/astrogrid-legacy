<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <xsl:template name="workflow-map">                                                
    <table border="0" cellpadding="0" cellspacing="0">  
      <tr>
        <td>
          <xsl:apply-templates mode="mapping"/>
        </td>
      </tr>
    </table>
  </xsl:template>
    
        
  <xsl:template match="sequence" mode="mapping"> <!--  SEQUENCE -->
    <tr>       
      <xsl:call-template name="format-cells-map">
        <xsl:with-param name="count" select="count(ancestor::*)"/>
      </xsl:call-template>                                                  
      <td valign="top" align="left">                                                                     
        <xsl:element name="img">
          <xsl:attribute name="src"><xsl:value-of select="$image_path"/>red.gif</xsl:attribute>
          <xsl:attribute name="width">10</xsl:attribute>
          <xsl:attribute name="height">5</xsl:attribute>
        </xsl:element>                                                                             
      </td>
    </tr>
    <tr>
      <xsl:apply-templates mode="mapping"/>
    </tr>                        
  </xsl:template>


  <xsl:template match="step" mode="mapping"> <!-- STEP -->
    <tr>        
      <xsl:call-template name="format-cells-map">
        <xsl:with-param name="count" select="count(ancestor::*)"/>
      </xsl:call-template>                                
      <td valign="top" align="left">                    
        <xsl:attribute name="background">  <!-- prevent gaps appearing in 'trunk' when parameters are viewed - not req'd with step -->
        </xsl:attribute>                    
        <xsl:element name="img">
          <xsl:attribute name="src"><xsl:value-of select="$image_path"/>yellow.gif</xsl:attribute>
          <xsl:attribute name="width">10</xsl:attribute>
          <xsl:attribute name="height">5</xsl:attribute>
          <xsl:attribute name="alt">step</xsl:attribute>
        </xsl:element>                       
      </td>
    </tr>
    <tr>
      <xsl:apply-templates mode="mapping"/>
    </tr>                        
  </xsl:template>
    
    
  <xsl:template match="flow" mode="mapping"> <!-- FLOW -->
    <tr>       
      <xsl:call-template name="format-cells-map">
        <xsl:with-param name="count" select="count(ancestor::*)"/>
      </xsl:call-template>                                      
      <td valign="top" align="left">                              
        <xsl:element name="img">
          <xsl:attribute name="src"><xsl:value-of select="$image_path"/>green.gif</xsl:attribute>
          <xsl:attribute name="width">10</xsl:attribute>
          <xsl:attribute name="height">5</xsl:attribute>
          <xsl:attribute name="alt">flow</xsl:attribute>
        </xsl:element>                                                                                        
      </td>
    </tr>
    <tr>
      <xsl:apply-templates mode="mapping"/>
    </tr>                        
  </xsl:template> 
  
  <xsl:template match="script" mode="mapping"> <!-- SCRIPT -->
    <tr>       
      <xsl:call-template name="format-cells-map">
        <xsl:with-param name="count" select="count(ancestor::*)"/>
      </xsl:call-template>                                      
      <td valign="top" align="left">                              
        <xsl:element name="img">
          <xsl:attribute name="src"><xsl:value-of select="$image_path"/>green.gif</xsl:attribute>
          <xsl:attribute name="width">10</xsl:attribute>
          <xsl:attribute name="height">5</xsl:attribute>
          <xsl:attribute name="alt">flow</xsl:attribute>
        </xsl:element>                                                                                        
      </td>
    </tr>
    <tr>
      <xsl:apply-templates mode="mapping"/>
    </tr>                        
  </xsl:template>      


  <!--+
        | Blank cells (or containing images) for table.
       +-->
    <xsl:template name="format-cells-map" mode="mapping">
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
                                        <xsl:attribute name="width">10</xsl:attribute>
                                        <xsl:attribute name="height">5</xsl:attribute>
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
                                        <xsl:attribute name="width">10</xsl:attribute>
                                        <xsl:attribute name="height">5</xsl:attribute>
                                    </xsl:element>                                                                                                                                                        
                                </xsl:if>     
                            </xsl:if>
                        </xsl:if>                                                                       
                    </xsl:for-each>                                                              
                </td>                          
             <xsl:call-template name="format-cells-map" mode="mapping">
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
                            <xsl:attribute name="width">10</xsl:attribute>
                            <xsl:attribute name="height">5</xsl:attribute>
                        </xsl:element>
                    </xsl:when>
                    <xsl:otherwise> <!-- if there are no following siblings then display bottom arrow image -->                    
                        <xsl:element name="img">
                            <xsl:attribute name="src"><xsl:value-of select="$image_path"/>arrow_bottom.gif</xsl:attribute>
                            <xsl:attribute name="width">10</xsl:attribute>
                            <xsl:attribute name="height">5</xsl:attribute>
                        </xsl:element>
                    </xsl:otherwise>
                </xsl:choose>            
            </td>
        </xsl:if>
    </xsl:template>
    
    
</xsl:stylesheet>
