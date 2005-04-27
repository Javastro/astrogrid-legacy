<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

  <!--+ 
       | Author: Phil Nicolson "pjn3@star.le.ac.uk"
       | Date:   Sept 2004
       +-->

  <xsl:template name="workflow-map">                                                
    <table border="0" cellpadding="0" cellspacing="0">  
      <tr>
        <td>
          <xsl:apply-templates mode="mapping"/>
        </td>
      </tr>
    </table>
  </xsl:template>

  <xsl:template match="*" mode="mapping"> 
    <tr>          
      <xsl:call-template name="format-cells">
        <xsl:with-param name="count" select="count(ancestor::*)"/>
      </xsl:call-template>       
   
      <td valign="top" align="left">
        <xsl:choose>                                                
          <xsl:when test="name() = 'step'">  <!-- STEP -->                   
            <img  width="10" height="5"><xsl:attribute name="src">/astrogrid-portal/mount/workflow/grey.gif</xsl:attribute></img>
          </xsl:when>
          <xsl:when test="name() = 'flow'">  <!-- FLOW -->                   
            <img  width="10" height="5"><xsl:attribute name="src">/astrogrid-portal/mount/workflow/black.gif</xsl:attribute></img>
          </xsl:when>          
          <xsl:when test="name() = 'sequence'">  <!-- SEQUENCE -->                   
            <img  width="10" height="5"><xsl:attribute name="src">/astrogrid-portal/mount/workflow/yellow.gif</xsl:attribute></img>
          </xsl:when>          
          <xsl:when test="name()='if' or name()='then' or name()='else' or name()='scope' or name()='script' or name()='set' or name()='unset' ">  <!-- LOGIC -->                   
            <img  width="10" height="5"><xsl:attribute name="src">/astrogrid-portal/mount/workflow/green.gif</xsl:attribute></img>
          </xsl:when>          
          <xsl:when test="name()='forObj' or name()='parForObj' or name()='whileObj'">  <!-- LOOP -->                   
            <img  width="10" height="5"><xsl:attribute name="src">/astrogrid-portal/mount/workflow/blue.gif</xsl:attribute></img>
          </xsl:when>                             
          <xsl:when test="name()='catchObj' or name()='tryObj'">  <!-- ERROR -->                   
            <img  width="10" height="5"><xsl:attribute name="src">/astrogrid-portal/mount/workflow/red.gif</xsl:attribute></img>
          </xsl:when>                             
          <xsl:otherwise>  <!--  All OTHER ACTIVIIES -->                            
            <img width="10" height="5"><xsl:attribute name="src">/astrogrid-portal/mount/workflow/red.gif</xsl:attribute></img>
          </xsl:otherwise>                                                       
        </xsl:choose>
      </td>
      </tr>
      <tr>
        <xsl:apply-templates select="*" mode="mapping"/>
      </tr>                        
    </xsl:template>


  <!--+
        | Blank cells (or containing images) for table.
       +-->
    <xsl:template name="format-cells" mode="mapping">
      <xsl:param name="count"/>                         <!-- No. of ancestors for this node = no. columns required prior to displaying it-->
      <xsl:param name="counter" select="1"/>            <!-- Loop counter (needs to increment so that table can be formatted correctly -->
        <xsl:if test="$counter != $count">             <!-- Test to see if column should display details -->
          <td valign="top">
            <xsl:for-each select="ancestor::*">    <!-- Display vertical sequence image in relevant column -->                                                            
              <xsl:if test="count(ancestor::*) = $counter ">
                <xsl:if test="count(following-sibling::*[not(name()='toolsAvailable')]) != 0">
                  <xsl:attribute name="background">  <!-- prevent gaps appearing in 'trunk' when parameters are viewed -->
                    /astrogrid-portal/mount/workflow/sequence_trunk.gif
                  </xsl:attribute>
                  <img width="10" height="5">
                    <xsl:attribute name="src">/astrogrid-portal/mount/workflow/sequence_trunk.gif</xsl:attribute>
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
                  /astrogrid-portal/mount/workflow/sequence_trunk.gif
                </xsl:attribute>                                              
                <img width="10" height="5">
                  <xsl:attribute name="src">/astrogrid-portal/mount/workflow/arrow.gif</xsl:attribute>
                </img>
              </xsl:when>
              <xsl:otherwise> <!-- if there are no following siblings then display bottom arrow image -->    
                <img width="10" height="5">
                  <xsl:attribute name="src">/astrogrid-portal/mount/workflow/arrow_bottom.gif</xsl:attribute>
                </img>                
              </xsl:otherwise>
            </xsl:choose>            
          </td>
        </xsl:if>
    </xsl:template>

    <xsl:template match="tool" mode="mapping"/>
    <xsl:template match="toolsAvailable" mode="mapping"/>
    <xsl:template match="executionRecord" mode="mapping"/>
    <xsl:template match="message" mode="mapping"/>
    <xsl:template match="inputParam" mode="mapping"/>  
    <xsl:template match="outputParam" mode="mapping"/>
    <xsl:template match="workflowExecutionRecord" mode="mapping"/>            

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
