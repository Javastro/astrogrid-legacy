<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/cocoon/common/xsl/Attic/template.xsl,v $</cvs:source>
    | <cvs:date>$Author: dave $</cvs:date>
    | <cvs:author>$Date: 2003/06/26 14:15:10 $</cvs:author>
    | <cvs:version>$Revision: 1.1 $</cvs:version>
    | <cvs:log>
    | $Log: template.xsl,v $
    | Revision 1.1  2003/06/26 14:15:10  dave
    | Added explorer pages and actions to Cocoon
    |
    | </cvs:log>
    | 
    +-->
<xsl:stylesheet
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	>

	<xsl:template match="/page">
		<html>
			<head>
				<xsl:apply-templates select="header"/>
			</head>
			<body>
				<table border="1" width="800" height="100%">
					<tr>
						<td width="100" height="100" align="left" valign="center">
							logo
						</td>
						<td width="600" height="100" align="center" valign="center">
							Header
						</td>
						<td width="100" height="100" align="center" valign="center">
							Login
						</td>
					</tr>
					<tr>
						<td width="100" height="350" align="center" valign="top">
							<xsl:apply-templates select="menu"/>
						</td>
						<td width="700" height="350" align="left"   valign="top" colspan="2">
							<xsl:apply-templates select="content"/>
						</td>
					</tr>
					<tr>
						<td width="100" height="100" align="center" valign="center">
							Blank
						</td>
						<td width="700" height="100" align="center" valign="center" colspan="2">
							Footer
						</td>
					</tr>
				</table>
			</body>
		</html>
	</xsl:template>

	<!-- Match the page header -->
	<xsl:template match="/page/header">
		<xsl:apply-templates/>
	</xsl:template>

	<!-- Match the page navigation -->
	<xsl:template match="/page/menu">
		<table border="1">
			<xsl:apply-templates select="link"/>
		</table>
	</xsl:template>

	<!-- Match a menu link -->
	<xsl:template name="menu" match="/page/menu//link">
		<tr>
			<xsl:call-template name="link"/>
		</tr>
		<xsl:for-each select="link">
			<xsl:call-template name="menu"/>
		</xsl:for-each>
	</xsl:template>

	<!-- Generate a href link -->
	<xsl:template name="link" match="link">
		<a>
			<xsl:attribute name="href">
				<xsl:value-of select="href/base"/>
				<xsl:for-each select="href/param">
					<xsl:choose>
						<xsl:when test="position() = 1">
							<xsl:text>?</xsl:text>
						</xsl:when>
						<xsl:otherwise>
							<xsl:text>&amp;</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
					<xsl:value-of select="@name"/>
					<xsl:text>=</xsl:text>
					<xsl:value-of select="text()"/>
				</xsl:for-each>
			</xsl:attribute>
			<xsl:choose>
				<!-- If this link is selected -->
				<xsl:when test="@selected = 'true'">
					<b>
						<xsl:text>[</xsl:text>
						<xsl:value-of select="display/text()"/>
						<xsl:text>]</xsl:text>
					</b>
				</xsl:when>
				<!-- If this link is NOT selected -->
				<xsl:otherwise>
					<xsl:text>[</xsl:text>
					<xsl:value-of select="display/text()"/>
					<xsl:text>]</xsl:text>
				</xsl:otherwise>
			</xsl:choose>
		</a>
	</xsl:template>

	<!-- Match the page content -->
	<xsl:template match="/page/content">
		<!-- Process the page content -->
		<xsl:apply-templates/>
	</xsl:template>

	<!-- Default, copy all and apply templates -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>
