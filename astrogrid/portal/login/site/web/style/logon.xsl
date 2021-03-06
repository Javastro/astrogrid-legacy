<?xml version="1.0"?>
<!--+
    |  $Id: logon.xsl,v 1.11 2005/04/26 15:27:47 clq2 Exp $
    |  Transforms pages which simply display a message
    |
    +-->

<xsl:stylesheet version="1.0" 
      xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="page">
    <div id="mainLoginPage">
      <xsl:apply-templates/>
    </div>
  </xsl:template>

  <xsl:template match="error">
    <div id="loginErrorBox">
      <xsl:apply-templates/>
    </div>
  </xsl:template>

  <xsl:template match="content">
  	  <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="login-form">
    <div id="loginBox">
        <h1><xsl:value-of select="@title"/></h1>
    <div id="loginBoxContent">
    <xsl:element name="form">
      <xsl:attribute name = "method"><xsl:value-of select="@method"/></xsl:attribute> 
      <xsl:attribute name = "action"><xsl:value-of select="@action"/></xsl:attribute>
      <xsl:attribute name = "id">login-form</xsl:attribute> 
      <xsl:attribute name = "onSubmit">gatherScreenData()</xsl:attribute>                     
      <input type="hidden" name="visited" id="visited"/>
      <input type="hidden" name="available-height" id="available-height"/>
      <input type="hidden" name="available-left" id="available-left"/>
      <input type="hidden" name="available-top" id="available-top"/>
      <input type="hidden" name="available-width" id="available-width"/>
      <input type="hidden" name="color-depth" id="color-depth"/>
      <input type="hidden" name="screen-height" id="screen-height"/>
      <input type="hidden" name="pixel-depth" id="pixel-depth"/>       
      <input type="hidden" name="screen-width" id="screen-width"/>                                         
      <center>
      <table border="0" cellpadding="4" cellspacing="0">
        <xsl:apply-templates/>
      </table>
      </center>
    </xsl:element> 
    </div>
    </div>
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

  <xsl:template match="login-form/login-input-right">
    <tr>
      <td align="right"><xsl:value-of select="@caption"/></td>
      <td align="right">
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
    | Revision 1.11  2005/04/26 15:27:47  clq2
    | por_jl_1046
    |
    | Revision 1.10.14.1  2005/04/05 15:13:18  jl99
    | Added ability to retrieve details of the user's screen attributes and store them in the session object
    |
    | Revision 1.10  2005/03/01 13:23:26  clq2
    | upgrade to v10 registry
    |
    | Revision 1.9.16.1  2005/02/28 17:53:34  KevinBenson
    | fixing small bugs so that it will now query registry using sql/adql.  Also rolled back login page for now back to
    | a textbox for the community id because it was the one thing not switched over to 0.10
    |
    | Revision 1.8  2004/12/07 16:26:04  clq2
    | portal_kea_719
    |
    | Revision 1.7.150.1  2004/12/03 16:34:21  kea
    | Changes for new login pages layout
    |
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
