<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

 <!--+
          | Display parameter details
          +-->
    <xsl:template name="parameter-details">
        <table border="1" cellpadding="0" cellspacing="0">
            <tr>
                <td align="center" colspan="4">
                    Parameters for <xsl:value-of select="@name"/>
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
                <xsl:apply-templates select="tool/input/parameter"/>
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
            <xsl:apply-templates select="tool/input/parameter"/>            
        </table>
    </xsl:template>


    <!--+
          | Match the parameter element.
          | TODO: include as hidden parameters in page
          +-->
    <xsl:template match="parameter">
        <form name="parameter_form" >
            <tr>
                <td><xsl:value-of select="@name"/></td>
                <td><xsl:value-of select="@type"/></td>
                <td><xsl:value-of select="@value"/></td>
                <td><input type="submit" value="Submit" /></td>
            </tr>
            <input type="hidden" name="action" value="insert-value" />
            <input type="hidden" name="param-name"><xsl:attribute name="value"></xsl:attribute></input>
            <input type="hidden" name="activity-key"><xsl:attribute name="value"></xsl:attribute></input>            
        </form>
    </xsl:template>


</xsl:stylesheet>