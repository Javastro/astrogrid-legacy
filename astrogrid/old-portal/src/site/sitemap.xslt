<?xml version="1.0"?>
<!--
<cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/old-portal/src/site/Attic/sitemap.xslt,v $</cvs:source>
<cvs:date>$Author: dave $</cvs:date>
<cvs:author>$Date: 2003/06/03 13:16:47 $</cvs:author>
<cvs:version>$Revision: 1.1 $</cvs:version>
<cvs:log>
	$Log: sitemap.xslt,v $
	Revision 1.1  2003/06/03 13:16:47  dave
	Added initial iter 02 code

	Revision 1.3  2003/05/25 12:16:19  Dumbledore
	Changed the sitemap xslt param from a path to the full path/file.
	Fixed problem of missing '/' at the end of the path.
	
	Revision 1.2  2003/05/25 11:29:04  Dumbledore
	Fixed bug in the cvs log header.
	
</cvs:log>
 -->
<xsl:stylesheet
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:map="http://apache.org/cocoon/sitemap/1.0"
	>
	<!-- Sitemap path from the Ant build -->
	<xsl:param name="sitemap">astrogrid</xsl:param>

	<!-- Match the element before last matcher in the last pipeline -->
	<xsl:template match="//map:pipelines/map:pipeline[position()=last()]/map:match[position()=(last()-1)]">
		<!-- If the matcher is not ours -->
		<xsl:if test="@pattern != 'astrogrid/**'">
			<!-- Copy the existing matcher -->
			<xsl:copy>
				<xsl:apply-templates select="@*|node()" />
			</xsl:copy>
		</xsl:if>
		<!-- Add our Astrogrid site match -->
		<map:match pattern="astrogrid/**">
			<map:mount check-reload="yes" uri-prefix="astrogrid">
				<xsl:attribute name="src">
					<xsl:value-of select="$sitemap"/>
				</xsl:attribute>
			</map:mount>
		</map:match>

	</xsl:template>

	<!-- Default, copy all and apply templates -->
	<!-- FIXME : Not sure if I need the with-param on this -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()">
				<xsl:with-param name="sitemap">
					<xsl:value-of select="$sitemap"/>
				</xsl:with-param>
			</xsl:apply-templates>
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>
