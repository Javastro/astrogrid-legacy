<?xml version="1.0"?>
<!--+
    |  $Id: logon.xsl,v 1.7 2004/04/21 14:42:27 jdt Exp $
    |  Transforms pages which simply display a message
    |
    +-->

<xsl:stylesheet version="1.0" 
      xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="page">
    <div>
      <xsl:apply-templates/>
    </div>
  </xsl:template>

  <xsl:template match="content">
    <div>
  	  <xsl:apply-templates/>
    </div>
  </xsl:template>
  <xsl:template match="error">
    <div style="color: red">
  	  <xsl:apply-templates/>
    </div>
  </xsl:template>
  
  <xsl:template match="login-form">
    <xsl:element name="form">
      <xsl:attribute name = "method"><xsl:value-of select="@method"/></xsl:attribute> 
      <xsl:attribute name = "action"><xsl:value-of select="@action"/></xsl:attribute>
    
      <input type="hidden" name="visited"/>
      <center>
      <table width="50%" border="0">
        <caption  style="text-align: left">
          <xsl:value-of select="@message"/>
        </caption>
        <xsl:apply-templates/>
      </table>
      </center>
    </xsl:element> 
  </xsl:template>
  <xsl:template match="login-form/login-input">
    <tr>
      <td align="right"><xsl:value-of select="@caption"/></td>
      <td align="left">
        <xsl:element name="input">
          <xsl:attribute name = "type"><xsl:value-of select="@type"/></xsl:attribute>
          <xsl:attribute name = "size"><xsl:value-of select="@size"/></xsl:attribute>
          <xsl:attribute name = "name"><xsl:value-of select="@name"/></xsl:attribute>
          <xsl:attribute name = "value"><xsl:value-of select="@value"/></xsl:attribute>
        </xsl:element>
      </td>
    </tr>
  </xsl:template>  
  
    <xsl:template match="@*|node()">
      <xsl:copy>
        <xsl:apply-templates select="@*|node()"/>
      </xsl:copy>
    </xsl:template>
    <xsl:template match="text()">
      <xsl:value-of select="."/>
    </xsl:template>
</xsl:stylesheet>		
		
<!--+
    | $Log: logon.xsl,v $
    | Revision 1.7  2004/04/21 14:42:27  jdt
    | changed title ->agComponentTitle
    |
    | Revision 1.6  2004/04/21 11:22:02  pfo
    | pfo: putting the labels closer to the input areas
    |
    | Revision 1.5  2004/04/20 12:40:32  pfo
    | PFO eliminated the border lines in the table and shrank the table width
    |
    | Revision 1.4  2004/03/30 13:46:05  jdt
    | Merge from PLGN_JDT_bz#275
    |
    | Revision 1.3.4.1  2004/03/30 13:33:45  jdt
    | Corrected xhtml - tags should be lowercase.
    |
    | Revision 1.3  2004/03/19 13:02:25  jdt
    | Pruned the log messages - they cause conflicts on merge, 
    | best just to reduce them to the merge message.
    |
    +-->
