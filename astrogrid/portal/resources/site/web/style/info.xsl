<?xml version="1.0"?>
<!--+
    |  $Id: info.xsl,v 1.2 2004/11/26 15:03:25 clq2 Exp $
    |  Transforms pages which simply display a message
    |
    +-->

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
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
    | Revision 1.2  2004/11/26 15:03:25  clq2
    | POR_KEA_716/POR_KEA_716/portal_pfo_620_697_707_717_718_733_735
    |
    | Revision 1.1.2.1  2004/11/22 18:03:12  kea
    | Check in of branch with new top navbar in place.
    | Still needed: hide nav prior to login.
    | Still needed: remove existing toplevel menu when ready.
    | Still needed: proper Resources and Help toplevel pages.
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
