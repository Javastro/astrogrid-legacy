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
        <table border="2" cellpadding="0" cellspacing="0">
            <tr>
                <td align="center" colspan="5">
                    <div style="color: blue; background-color: lightblue; text-align: center;">
                        Parameters for step: <b><xsl:value-of select="@step-name"/></b>; tool: <b><xsl:value-of select="./tool/@tool-name"/></b>.
                    </div>
                </td>
            </tr>
          <xsl:choose>
            <xsl:when test="./tool/@tool-name != 'null'">            
              <tr>
                <td colspan="5" style="color: blue; background-color: lightblue; text-align: center;">Input:</td>
              </tr>
              <tr>
                <td align="center">Name:</td><td align="center">Type:</td><td align="center">Value:</td><td></td><td></td>
              </tr>                               
              <xsl:for-each select="./tool/inputParam">
                <xsl:call-template name="parameter">
                  <xsl:with-param name="direction">input</xsl:with-param>
                </xsl:call-template>
              </xsl:for-each>
              <tr>
                <td colspan="5" style="color: blue; background-color: lightblue; text-align: center;">Output:</td>
              </tr>
              <tr>
                <td align="center">Name:</td><td align="center">Type:</td><td align="center">Value:</td><td></td><td></td>
              </tr>                               
              <xsl:for-each select="./tool/outputParam">
                <xsl:call-template name="parameter">
                  <xsl:with-param name="direction">output</xsl:with-param>
                </xsl:call-template>
              </xsl:for-each>
            </xsl:when>
          <xsl:otherwise>
            <tr>
                <td colspan="5" >There is currently no tool associated with this step</td>
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
                    <td><xsl:value-of select="@param-name"/></td>
                    <td><xsl:value-of select="@param-type"/></td>
<!--
                    <xsl:choose>
                        <xsl:when test="@param-value = '' ">
-->
                            <xsl:variable name="ivorn-id"><xsl:value-of select="../../@key"/><xsl:value-of select="@param-name" />ivorn</xsl:variable>
                            <xsl:variable name="agsl-id"><xsl:value-of select="../../@key"/><xsl:value-of select="@param-name" />agsl</xsl:variable>
                            <td>
                                <xsl:choose>
                                    <xsl:when test="@param-type = 'FileReference'">                                
                                        <xsl:element name="input">
                                            <xsl:attribute name="id"><xsl:value-of select="$agsl-id"/></xsl:attribute>
                                            <xsl:attribute name="type">text</xsl:attribute>
                                            <xsl:attribute name="name">param-value</xsl:attribute>
                                            <xsl:attribute name="size">30</xsl:attribute>
<!--                                            <xsl:attribute name="readonly">true</xsl:attribute>  -->
                                            <xsl:attribute name="value"><xsl:value-of select="@param-value" /></xsl:attribute>
                                        </xsl:element>
                                    </xsl:when>
                                    <xsl:when test="@param-type = 'MySpace_FileReference'">
                                        <xsl:element name="input">
                                            <xsl:attribute name="id"><xsl:value-of select="$agsl-id"/></xsl:attribute>
                                            <xsl:attribute name="type">text</xsl:attribute>
                                            <xsl:attribute name="name">param-value</xsl:attribute>
                                            <xsl:attribute name="size">30</xsl:attribute>
<!--                                            <xsl:attribute name="readonly">true</xsl:attribute>    -->
                                            <xsl:attribute name="value"><xsl:value-of select="@param-value" /></xsl:attribute>
                                        </xsl:element>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:element name="input">
                                            <xsl:attribute name="id"><xsl:value-of select="$agsl-id"/></xsl:attribute>
                                            <xsl:attribute name="type">text</xsl:attribute>
                                            <xsl:attribute name="name">param-value</xsl:attribute>
                                            <xsl:attribute name="size">30</xsl:attribute>
                                            <xsl:attribute name="value"><xsl:value-of select="@param-value" /></xsl:attribute>
                                        </xsl:element>
                                    </xsl:otherwise>
                                </xsl:choose>                                
                                                                                                    
                                <xsl:element name="input">
                                  <xsl:attribute name="type">hidden</xsl:attribute>
                                  <xsl:attribute name="name">original-param-value</xsl:attribute>
                                  <xsl:attribute name="value"><xsl:value-of select="@param-value" /></xsl:attribute>
                                </xsl:element>                                
                                <xsl:element name="input">
                                  <xsl:attribute name="id"><xsl:value-of select="$ivorn-id"/></xsl:attribute>
                                  <xsl:attribute name="type">hidden</xsl:attribute>
                                  <xsl:attribute name="name">ivorn-value</xsl:attribute>
                                </xsl:element>
                            </td>                            
                            <td>
                                <xsl:choose>
                                    <xsl:when test="@param-type = 'FileReference'">
                                        <input name="myspace-name" type="button" value="Browse...">
                                            <xsl:attribute name="onclick">javascript:void(window.open('/astrogrid-portal/mount/myspace/myspace-micro?ivorn=<xsl:value-of select="$ivorn-id"/>&amp;agsl=<xsl:value-of select="$agsl-id"/>', 'mySpaceMicro', 'toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, width=300, height=200'));</xsl:attribute>
                                        </input>
                                    </xsl:when>
                                    <xsl:when test="@param-type = 'MySpace_FileReference'">
                                        <input name="myspace-name" type="button" value="Browse...">
                                            <xsl:attribute name="onclick">javascript:void(window.open('/astrogrid-portal/mount/myspace/myspace-micro?ivorn=<xsl:value-of select="$ivorn-id"/>&amp;agsl=<xsl:value-of select="$agsl-id"/>', 'mySpaceMicro', 'toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, width=300, height=200'));</xsl:attribute> 
                                        </input>
                                    </xsl:when>
                                    <xsl:otherwise>
                                        <xsl:attribute name="align">center</xsl:attribute>---
                                    </xsl:otherwise>
                                </xsl:choose>
                            </td>
                            <td><input type="submit" value="submit" /></td>
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
