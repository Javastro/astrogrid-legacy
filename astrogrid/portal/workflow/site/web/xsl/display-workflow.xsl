<?xml version="1.0"?>
<!--+
    | Initial revision:
    | Displays workflow as per 
    | GUI mockup
    +-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:template match="/*">
        <html>            
            <body>
                <table width="100%" align="center" border="0">
                    <tr>
                        <td>Name:
                            <b><xsl:value-of select="name()"/></b>
                        </td>
                    </tr>
                </table>
                <table border="0" cellpadding="0" cellspacing="0">  
                    <tr>
                        <xsl:apply-templates select="*"/>
                    </tr>
                </table>  
            </body>
        </html>
    </xsl:template>
    
    <xsl:template match="*">   
        <xsl:choose>
            <xsl:when test="*">
                <tr>
                    <xsl:call-template name="format-cells">
                        <xsl:with-param name="count" select="count(ancestor::*)"/>
                    </xsl:call-template>                    
                    <td>
                        <xsl:choose>
                            <xsl:when test="name() = 'sequence'"><img src="/astrogrid-portal/mount/test/sequence_top.gif" width="70" height="25" /></xsl:when>
                            <xsl:when test="name() = 'flow'"><img src="/astrogrid-portal/mount/test/flow_top.gif" width="70" height="25" /></xsl:when>
                            <xsl:when test="name() = 'step'">
                                <img src="/astrogrid-portal/mount/test/step.gif" width="70" height="25" />                           
                                <xsl:element name="input">
                                    <xsl:attribute name="value"><xsl:value-of select="@stepNumber"/></xsl:attribute>
                                    <xsl:attribute name="type">hidden</xsl:attribute>
                                    <xsl:attribute name="name">stepNumber</xsl:attribute>
                                </xsl:element>                                                        
                            </xsl:when>                            
                        </xsl:choose>
                    </td>
                </tr>
                <tr>
                    <xsl:apply-templates select="*"/>
                </tr>
            </xsl:when>
            <xsl:otherwise>
                <div>
                        <xsl:value-of select="name()"/>
                </div>
                <div>
                    <xsl:value-of select="."/>
                </div>
            </xsl:otherwise>
        </xsl:choose>        
    </xsl:template>




  <!--+
        | Blank cells (or containing images) for table.
       +-->
    <xsl:template name="format-cells">
        <xsl:param name="count"/>                         <!-- No. of ancestors for this node = no. columns required prior to displaying it-->
        <xsl:param name="counter" select="1"/>      <!-- Loop counter (needs to increment so that table can be formatted correctly -->
            <xsl:if test="$counter != $count">             <!-- Test to see if column should display details -->
                <td>                                
                    <xsl:for-each select="ancestor::*">    <!-- Display horizontal sequence image in relevant column -->
                        <xsl:if test="name() = 'sequence'">
                            <xsl:if test="count(ancestor::*) = $counter -1"><img src="/astrogrid-portal/mount/test/sequence.gif" width="70" height="25" /></xsl:if>
                        </xsl:if>
                    </xsl:for-each>                                                              
                </td>            
             <xsl:call-template name="format-cells">
                 <xsl:with-param name="counter" select="$counter +1"/>
                 <xsl:with-param name="count" select="$count"/>
             </xsl:call-template>
        </xsl:if>
        <xsl:if test="$counter = $count">
            <xsl:choose>                        
                <xsl:when test="count(following-sibling::*) != 0">  
                    <td align="center"><img src="/astrogrid-portal/mount/test/arrow.gif" width="70" height="25" /></td>
                </xsl:when>
                <xsl:when test="count(following-sibling::*) = 0"> <!-- if there are no following siblings then display bottom arrow image -->
                    <td align="center"><img src="/astrogrid-portal/mount/test/arrow_bottom.gif" width="70" height="25" /></td>
                </xsl:when>
            </xsl:choose>            
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
            <td>Description:
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
          | Match the parameter element.
          +-->
    <xsl:template match="parameter"/>
    <!--+
          | Match the documentation element.
          +-->
    <xsl:template match="documentation"/>
    <!--+
          | Match the input element.
          +-->
    <xsl:template match="input"/>
    <!--+
          | Match the output element.
          +-->
    <xsl:template match="output"/>
    <!--+
          | Match the tool element.
          +-->
    <xsl:template match="tool"/>
</xsl:stylesheet>
