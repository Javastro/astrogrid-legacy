<?xml version="1.0"?>
<!--
<cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/old-portal/src/site/home/styles/Attic/table-navigation.xsl,v $</cvs:source>
<cvs:date>$Author: dave $</cvs:date>
<cvs:author>$Date: 2003/06/04 11:55:03 $</cvs:author>
<cvs:version>$Revision: 1.1 $</cvs:version>
<cvs:log>
	$Log: table-navigation.xsl,v $
	Revision 1.1  2003/06/04 11:55:03  dave
	Updating directory structure

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

	<!-- Match the page navigation -->
	<xsl:template match="/page/navigation">
		<table border="1" width="100" height="100%">
			<xsl:for-each select="item">
				<tr>
					<td width="100" height="10">
						<a>
							<xsl:attribute name="href">
								<xsl:value-of select="@url"/>
							</xsl:attribute>
							<xsl:value-of select="@display"/>
						</a>
					</td>
				</tr>

				<xsl:for-each select="item">
					<tr>
						<td width="100" height="10">
							<xsl:text> ## </xsl:text>
							<xsl:value-of select="@display"/>

							<xsl:for-each select="item">
								<br/>
								<xsl:text> ## ## </xsl:text>
								<xsl:value-of select="@display"/>
							</xsl:for-each>

						</td>
					</tr>
				</xsl:for-each>

			</xsl:for-each>
			<tr>
				<td>dummy</td>
			</tr>
		</table>
	</xsl:template>

	<!-- Default, copy all and apply templates -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>
