<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/portalB/src/cocoon/common/xsl/Attic/template.xsl,v $</cvs:source>
    | <cvs:date>$Author: dave $</cvs:date>
    | <cvs:author>$Date: 2003/06/29 02:45:22 $</cvs:author>
    | <cvs:version>$Revision: 1.6 $</cvs:version>
    | <cvs:log>
    | $Log: template.xsl,v $
    | Revision 1.6  2003/06/29 02:45:22  dave
    | Fixed display styles in explorer and add VOTable transform
    |
    | Revision 1.5  2003/06/27 03:17:38  dave
    | Simplified page path in sitemap
    |
    | Revision 1.4  2003/06/27 02:55:11  dave
    | Added images to tree nodes
    |
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
					table.menu { background:#ffffff; color:black; font-size:10pt; font-style:normal; font-weight:normal; font-family:arial, serif}
					tr.menu    { background:#ffffff; color:black; font-size:10pt; font-style:normal; font-weight:normal; font-family:arial, serif}
					td.menu    { background:#ffffff; color:black; font-size:10pt; font-style:normal; font-weight:normal; font-family:arial, serif}
				</style>
			</head>
			<body bgcolor="#FFFFFF">
				<table border="1" width="800" height="100%">
					<tr>
						<td width="150" height="100" align="left" valign="center">
							logo
						</td>
						<td width="500" height="100" align="center" valign="center">
							Header
						</td>
						<td width="150" height="100" align="center" valign="center">
							Login
						</td>
					</tr>
					<tr>
						<td width="150" height="350" align="center" valign="top">
							<xsl:apply-templates select="menu"/>
						</td>
						<td width="650" height="350" align="left"   valign="top" colspan="2">
							<xsl:apply-templates select="content"/>
						</td>
					</tr>
					<tr>
						<td width="150" height="100" align="center" valign="center">
							Blank
						</td>
						<td width="650" height="100" align="center" valign="center" colspan="2">
							<!-- Show the page debug links -->
							<xsl:apply-templates select="debug"/>
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
		<table border="0" class="menu" cellpadding="0" cellspacing="0">
			<xsl:apply-templates select="link"/>
		</table>
	</xsl:template>

	<!-- Match a menu link -->
	<xsl:template match="link[@type = 'menu']">
		<!-- Add a table row for this item -->
		<tr class="menu">
			<td class="menu">
				<xsl:for-each select="ancestor::link">
					<xsl:text>-- </xsl:text>
				</xsl:for-each>
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
							<span style="font-weight:bold">
								<xsl:text>[</xsl:text>
								<xsl:value-of select="display/text()"/>
								<xsl:text>]</xsl:text>
							</span>
						</xsl:when>
						<!-- If this link is NOT selected -->
						<xsl:otherwise>
							<xsl:text>[</xsl:text>
							<xsl:value-of select="display/text()"/>
							<xsl:text>]</xsl:text>
						</xsl:otherwise>
					</xsl:choose>
				</a>
			</td>
		</tr>
		<!-- Process any child items -->
		<xsl:apply-templates select="link"/>
	</xsl:template>

	<!-- Generate a tree node link -->
	<xsl:template match="link[@type = 'node']">
		<a class="tree">
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
			<img valign="center" border="0">
				<xsl:attribute name="src">
					<xsl:value-of select="image/@src"/>
				</xsl:attribute>
			</img>
			<!-- If we have some text -->
			<xsl:if test="string-length(normalize-space(text)) > 0">
				<xsl:choose>
					<!-- If this link is selected -->
					<xsl:when test="@selected = 'true'">
						<span style="font-weight:bold">
							<xsl:value-of select="text"/>
						</span>
					</xsl:when>
					<!-- If this link is NOT selected -->
					<xsl:otherwise>
						<xsl:value-of select="text"/>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:if>
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
					<span style="font-weight:bold">
						<xsl:text>[</xsl:text>
						<xsl:value-of select="display/text()"/>
						<xsl:text>]</xsl:text>
					</span>
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
