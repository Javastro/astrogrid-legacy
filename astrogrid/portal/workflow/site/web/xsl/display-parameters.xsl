<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

 <!--+
          | Display tool details
          +-->
    <xsl:template name="parameter-details">
        <table border="1" cellpadding="0" cellspacing="0">
            <tr>
                <td align="center" colspan="4">
                    <div class="agWorkflow_table_header">
                        Parameters for <xsl:value-of select="@name"/>
                    </div>
                </td>
            </tr>
            <tr>
                <td align="center" colspan="4">
                    Input:
                </td>
            </tr>
            <tr>
                <td>Name:</td>
                <td>Type:</td>
                <td>Value:</td>
                <td></td>
            </tr>                               
                <xsl:apply-templates select="tool/input/parameter">
                    <xsl:with-param name="direction">input</xsl:with-param>
                </xsl:apply-templates>
            <tr>
                <td align="center" colspan="4">
                    Output:
                </td>
            </tr>
            <tr>
                <td>Name:</td>
                <td>Type:</td>
                <td>Value:</td>
                <td></td>
            </tr>
            <xsl:apply-templates select="tool/output/parameter">
                    <xsl:with-param name="direction">output</xsl:with-param>            
            </xsl:apply-templates>
        </table>
    </xsl:template>


    <!--+
          | Match the parameter element.
          +-->
    <xsl:template match="parameter">
        <xsl:param name="direction"/>
        <form name="parameter_form" >
            <tr>
                <td><xsl:value-of select="@name"/></td>
                <td><xsl:value-of select="@type"/></td>
                <td><xsl:value-of select="./"/></td>
<td><input name="Input" type="button" value="... " onClick="popUpWindow('myspace_blank.html',200,200,200,200);"/></td>                
            </tr>
            <input type="hidden" name="action" value="insert-value" />
            <input type="hidden" name="param-name"><xsl:attribute name="value"></xsl:attribute></input>
            <input type="hidden" name="activity-key"><xsl:attribute name="value"></xsl:attribute></input>            
            <input type="hidden" name="direction"><xsl:attribute name="value">@direction</xsl:attribute></input>
        </form>
    </xsl:template>


</xsl:stylesheet>
