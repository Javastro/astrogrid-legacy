<?xml version="1.0"?>
<xsl:stylesheet
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	>

	<!--+
	    | Match the root element.
		+-->
	<xsl:template match="/">
		<xsl:apply-templates/>
	</xsl:template>

	<!--+
	    | Match the explorer element.
		+-->
	<xsl:template match="topcat">
		<html>
			<!-- Add our page content -->
			<body>
				<xsl:call-template name="topcat_form"/>
			</body>
			</html>
	</xsl:template>

	<!--+
	    | Generate the query form.
	    +-->
	<xsl:template name="topcat_form">
		<form method="get" name="TopCatForm">
			<strong>Image Plot<br /></strong>
			<xsl:choose>
				<xsl:when test="//topcat/url">
					<xsl:element name="img">
						<xsl:attribute name="src">votable?<xsl:value-of select="//topcat/url/@val" />
						<xsl:if test="//topcat/xaxis">
								<xsl:if test="//topcat/yaxis">
									&amp;<xsl:value-of select="//topcat/xaxis/@val" />
									&amp;<xsl:value-of select="//topcat/yaxis/@val" />
								</xsl:if>
						</xsl:if>						
						</xsl:attribute>							
						</xsl:element>
				</xsl:when>
				<xsl:otherwise>
					<font color="red">
						The VOTable url was never passed in.  Cannot plot the data.
					</font>
				</xsl:otherwise>
			</xsl:choose>
		</form>
	</xsl:template>

	<!--+
	    | Default template, copy all and apply templates.
	    +-->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>

</xsl:stylesheet>
