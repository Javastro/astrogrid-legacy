<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/cocoon/common/xsl/Attic/template.xsl,v $</cvs:source>
    | <cvs:date>$Author: dave $</cvs:date>
    | <cvs:author>$Date: 2003/06/27 02:43:18 $</cvs:author>
    | <cvs:version>$Revision: 1.2 $</cvs:version>
    | <cvs:log>
    | $Log: template.xsl,v $
    | Revision 1.2  2003/06/27 02:43:18  dave
    | Added images to tree nodes
    |
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
				<style>
					<xsl:apply-templates select="style"/>
				</style>
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

	<!-- Match a page style element -->
	<xsl:template match="/page/style">
		<xsl:apply-templates/>
	</xsl:template>

	<!-- Match the page navigation -->
	<xsl:template match="/page/menu">
		<table border="1">
			<xsl:apply-templates select="link"/>
		</table>
	</xsl:template>

	<!-- Match a menu link -->
	<xsl:template match="link[@type = 'menu']">
		<!-- Add a table row for this item -->
		<tr>
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
		</tr>
		<!-- Process any child items -->
		<xsl:apply-templates select="link"/>
	</xsl:template>

	<!-- Generate a tree node link -->
	<xsl:template match="link[@type = 'node']">
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
			<img border="0">
				<xsl:attribute name="src">
					<xsl:value-of select="image/@src"/>
				</xsl:attribute>
			</img>
			<xsl:choose>
				<!-- If this link is selected -->
				<xsl:when test="@selected = 'true'">
					<strong>
						<xsl:value-of select="text"/>
					</strong>
				</xsl:when>
				<!-- If this link is NOT selected -->
				<xsl:otherwise>
					<xsl:value-of select="text"/>
				</xsl:otherwise>
			</xsl:choose>
		</a>
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
