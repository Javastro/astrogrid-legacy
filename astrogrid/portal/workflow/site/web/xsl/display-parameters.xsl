<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

 <!--+
          | Display tool details
          |
          | Display table containing details of input and output parameters.
          | Allows inserting of parameter values.
          | Pop-up for micro-browser also available
          |
          +-->
    <xsl:template name="parameter-details">
        <table border="1" cellpadding="0" cellspacing="0">
            <tr>
                <td align="center" colspan="5">
                    <div class="agWorkflow_table_header">
                        Parameters for step: <xsl:value-of select="@step-name"/>; tool: <xsl:value-of select="./tool/@tool-name"/>
                    </div>
                </td>
            </tr>
          <xsl:choose>
            <xsl:when test="./tool/@tool-name != 'null'">            
              <tr>
                <td align="center" colspan="5"> Input:</td>
              </tr>
              <tr>
                <td>Name:</td><td>Type:</td><td>Value:</td><td></td><td></td>
              </tr>                               
              <xsl:for-each select="./tool/inputParam">
                <xsl:call-template name="parameter">
                  <xsl:with-param name="direction">input</xsl:with-param>
                </xsl:call-template>
              </xsl:for-each>
              <tr>
                <td align="center" colspan="5"> Output:</td>
              </tr>
              <tr>
                <td>Name:</td><td>Type:</td><td>Value:</td><td></td><td></td>
              </tr>                               
              <xsl:for-each select="./tool/outputParam">
                <xsl:call-template name="parameter">
                  <xsl:with-param name="direction">output</xsl:with-param>
                </xsl:call-template>
              </xsl:for-each>
            </xsl:when>
          <xsl:otherwise>
            <tr>
                <td colspan="5">There is currently no tool associated with this step</td>
            </tr>    
          </xsl:otherwise>
        </xsl:choose>                        
      </table>
    </xsl:template>


    <!--+
          | Match the parameter element.
          +-->
    <xsl:template name="parameter">
        <xsl:param name="direction"/>
            <form name="parameter_form" action="/astrogrid-portal/main/mount/workflow/agjobmanager.html">
                <tr>
                    <td>name<xsl:value-of select="@param-name"/></td>
                    <td>type<xsl:value-of select="@param-type"/></td>
<!--
                    <xsl:choose>
                        <xsl:when test="@param-value = '' ">
-->
                            <td>
                                <xsl:element name="input">
                                  <xsl:attribute name="id"><xsl:value-of select="../../@key"/><xsl:value-of select="count(preceding-sibling::*)"/>myspace-agsl</xsl:attribute>
                                  <xsl:attribute name="type">text</xsl:attribute>
                                  <xsl:attribute name="name">param-value</xsl:attribute>
                                  <xsl:attribute name="size">30</xsl:attribute>
                                  <xsl:attribute name="value"><xsl:value-of select="@param-value" /></xsl:attribute>
                                </xsl:element>
                                <xsl:element name="input">
                                  <xsl:attribute name="type">hidden</xsl:attribute>
                                  <xsl:attribute name="name">original-param-value</xsl:attribute>
                                  <xsl:attribute name="value"><xsl:value-of select="@param-value" /></xsl:attribute>
                                </xsl:element>                                
                                <xsl:element name="input">
                                  <xsl:attribute name="id"><xsl:value-of select="../../@key"/><xsl:value-of select="@param-value"/>myspace-ivorn</xsl:attribute>
                                  <xsl:attribute name="type">hidden</xsl:attribute>
                                  <xsl:attribute name="name">ivorn-value</xsl:attribute>
                                </xsl:element>
                            </td>
                            <td><input type="submit" value="submit" /></td>
                            <td><input name="myspace-name" type="button" value="Browse..." onclick="javascript:void(window.open('/astrogrid-portal/mount/myspace/myspace-micro', 'mySpaceMicro', 'toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, width=300, height=200'))"/></td>    
<!--                        </xsl:when>
                        <xsl:otherwise>
                            <td><xsl:value-of select="@param-value" /></td>
                            <td></td>
                            <td></td>
                        </xsl:otherwise>
                    </xsl:choose>                                       
-->
                </tr>
                <input type="hidden" name="action" value="insert-parameter-value" />
                <input type="hidden" name="param-name"><xsl:attribute name="value"><xsl:value-of select="@param-name"/></xsl:attribute></input>
                <input type="hidden" name="activity_key"><xsl:attribute name="value"><xsl:value-of select="../../@key"/></xsl:attribute></input>            
                <input type="hidden" name="direction"><xsl:attribute name="value"><xsl:value-of select="$direction"/></xsl:attribute></input>
                <input type="hidden" name="display_parameter_values"><xsl:attribute name="value">true</xsl:attribute></input>
            </form>
    </xsl:template>


</xsl:stylesheet>
