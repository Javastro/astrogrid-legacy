<?xml version="1.0"?>
<!--+
    |  $Id: info.xsl,v 1.7 2005/06/06 14:20:45 clq2 Exp $
    |  Transforms pages which simply display a message
    |
    +-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:template match="page">
    <div id="mainLoginPage">
      <xsl:apply-templates/>
    </div>
  </xsl:template>

  <xsl:template match="content">
      <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="boxmessage">
     <div id="loginBox">
       <h1><xsl:value-of select="@title"/></h1>
     <div id="loginBoxContent">
	<p>
        <xsl:value-of select="@value"/>
	</p>
      </div>

     </div>
     <xsl:apply-templates/>
  </xsl:template>
  
  <xsl:template match="status">
    <div id="statusBoxContent">
      <xsl:choose>
        <xsl:when test="announceMessage/@value != ''">
          <xsl:value-of select="anounceMessage/@value"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="statusMessage/@value"/>
        </xsl:otherwise>
      </xsl:choose>
    </div>
    <xsl:apply-templates/>
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
    | $Log: info.xsl,v $
    | Revision 1.7  2005/06/06 14:20:45  clq2
    | portal_pjn_1218 and
    | portalWF_1193_pjn
    |
    | Revision 1.6.94.2  2005/06/03 09:48:51  pjn3
    | spelling error!
    |
    | Revision 1.6.94.1  2005/06/02 11:47:07  pjn3
    | Status message added
    |
    | Revision 1.6  2004/12/07 16:26:04  clq2
    | portal_kea_719
    |
    | Revision 1.5.150.1  2004/12/03 16:34:21  kea
    | Changes for new login pages layout
    |
    | Revision 1.5  2004/04/21 14:42:27  jdt
    | changed title ->agComponentTitle
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
