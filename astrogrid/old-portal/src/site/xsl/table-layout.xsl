<?xml version="1.0"?>
<!--
<cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/old-portal/src/site/xsl/Attic/table-layout.xsl,v $</cvs:source>
<cvs:date>$Author: dave $</cvs:date>
<cvs:author>$Date: 2003/06/03 13:16:47 $</cvs:author>
<cvs:version>$Revision: 1.1 $</cvs:version>
<cvs:log>
	$Log: table-layout.xsl,v $
	Revision 1.1  2003/06/03 13:16:47  dave
	Added initial iter 02 code

	Revision 1.2  2003/05/25 11:29:04  Dumbledore
	Fixed bug in the cvs log header.
	
</cvs:log>
 -->
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
							<img width="100" height="100" src="images/site-logo.gif"/>
						</td>
						<td width="600" height="100" align="center" valign="center">
							Page header
						</td>
						<td width="100" height="100" align="center" valign="center">
							Login
						</td>
					</tr>
					<tr>
						<td width="100" height="350" align="center" valign="top">
							<xsl:apply-templates select="navigation"/>
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
	<xsl:template match="/page/navigation">
		<xsl:apply-templates/>
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
