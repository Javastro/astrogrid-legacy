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
                <td colspan="2" align="center">Name:</td><td align="center">Value:</td><td></td><td></td>
              </tr>                               
              <xsl:for-each select="./tool/inputParam">
                <xsl:call-template name="parameter">
                  <xsl:with-param name="direction">input</xsl:with-param>
                </xsl:call-template>
              </xsl:for-each>
              <xsl:if test="./tool/outputParam" >              
                <tr>
                  <td colspan="5" style="color: blue; background-color: lightblue; text-align: center;">Output:</td>
                </tr>
                <tr>
                  <td colspan="2" align="center">Name:</td><td align="center">Value:</td><td></td><td></td>
                </tr>                               
                <xsl:for-each select="./tool/outputParam">  <!-- Email tool has no output params, so don't display -->
                  <xsl:call-template name="parameter">
                    <xsl:with-param name="direction">output</xsl:with-param>
                  </xsl:call-template>
                </xsl:for-each>
              </xsl:if>            
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
            <form name="parameter_form" id="parameter_form" action="/astrogrid-portal/main/mount/workflow/agjobmanager.html">
                <tr>
                    <td><xsl:value-of select="@param-name"/></td>
                    <td style="color: blue;">
                        <font size="-1">
                            <xsl:element name="a">                                    
                                <xsl:attribute name="href">javascript:void(0);</xsl:attribute>
                                <xsl:attribute name="onMouseOver">this.T_TITLE='Parameter: <xsl:value-of select="@param-UI-name" /> '; this.T_WIDTH=250; this.T_DELAY=500; return escape('' +
                                                    ' &lt;b&gt;Description: &lt;/b&gt; <xsl:value-of select="@param-UI-description" /> ' +
                                                    ' &lt;br/&gt; &lt;b&gt;Type:&lt;/b&gt; <xsl:value-of select="@param-type" /> ' +
                                                    ' &lt;br/&gt; &lt;b&gt;Subtype:&lt;/b&gt; <xsl:value-of select="@param-subtype"/> ' +
                                                    ' &lt;br/&gt; &lt;b&gt;Units:&lt;/b&gt; <xsl:value-of select="@param-units"/> ' +
                                                    ' &lt;br/&gt; &lt;b&gt;UCD:&lt;/b&gt; <xsl:value-of select="@param-ucd"/> ' +
                                                    ' &lt;br/&gt; &lt;b&gt;Default:&lt;/b&gt; <xsl:value-of select="@param-defaultValue"/> ' +
                                                    ' &lt;br/&gt; &lt;b&gt;Indirect?:&lt;/b&gt; <xsl:value-of select="@param-indirect"/> ' +
                                                    ' &lt;br/&gt; Cardinality max: <xsl:value-of select="@param-cardinality-max"/> ' +
                                                    ' &lt;br/&gt; Cardinality min: <xsl:value-of select="@param-cardinality-min"/> ');
                                </xsl:attribute>
                            </xsl:element>
                            <small><b>(more)</b></small>
                        </font>
                        <xsl:element name="/a"></xsl:element>                                                       
                        </td>
                            <xsl:variable name="ivorn-id"><xsl:value-of select="count(preceding-sibling::*)"/>ivorn</xsl:variable>
                            <xsl:variable name="agsl-id"><xsl:value-of select="count(preceding-sibling::*)"/>agsl</xsl:variable>
                            <xsl:variable name="indirect-id"><xsl:value-of select="count(preceding-sibling::*)"/>param</xsl:variable>
                            <td>
                                <xsl:element name="input">
                                    <xsl:attribute name="id"><xsl:value-of select="$agsl-id"/></xsl:attribute>
                                    <xsl:attribute name="type">text</xsl:attribute>
                                    <xsl:attribute name="name">param-value</xsl:attribute>
                                    <xsl:attribute name="size">50</xsl:attribute>
                                    <xsl:attribute name="value"><xsl:value-of select="@param-value" /></xsl:attribute>
                                </xsl:element>                                
                                                                                                    
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
                                
                                <xsl:element name="input">
                                  <xsl:attribute name="id"><xsl:value-of select="$indirect-id"/></xsl:attribute>
                                  <xsl:attribute name="type">hidden</xsl:attribute>
                                  <xsl:attribute name="name">param_indirect</xsl:attribute>
                                </xsl:element>                                                                                                
                                                                                                
                            </td>                            
                            <td>
                                <input class="agActionButton" name="myspace-name" type="button" value="Browse...">
                                    <xsl:attribute name="onClick">javascript:document.getElementById('<xsl:value-of select="$indirect-id"/>').value = 'true'; void(window.open('/astrogrid-portal/lean/mount/myspace/myspace-micro?ivorn=<xsl:value-of select="$ivorn-id"/>&amp;agsl=<xsl:value-of select="$agsl-id"/>', 'mySpaceMicro', 'toolbar=no, directories=no, location=no, status=no, menubar=no, resizable=yes, scrollbars=yes, width=300, height=200'));</xsl:attribute>
                                </input>
                            </td>
                            <td><input class="agActionButton" type="submit" value="submit" /></td>

                </tr>
                <input type="hidden" name="action" value="insert-parameter-value" />
                <input type="hidden" name="param-name"><xsl:attribute name="value"><xsl:value-of select="@param-name"/></xsl:attribute></input>
                <input type="hidden" name="activity_key"><xsl:attribute name="value"><xsl:value-of select="../../@key"/></xsl:attribute></input>            
                <input type="hidden" name="direction"><xsl:attribute name="value"><xsl:value-of select="$direction"/></xsl:attribute></input>
                <input type="hidden" name="display_parameter_values"><xsl:attribute name="value">true</xsl:attribute></input>                                
                <input type="hidden" name="param_indirect" id="param_indirect" value="false" />                               
            </form>
    </xsl:template>


</xsl:stylesheet>
