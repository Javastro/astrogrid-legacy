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
			<strong>Image Plot<br /></strong>
			<xsl:choose>
				<xsl:when test="//topcat/url">
					<xsl:element name="img">
						<xsl:attribute name="src">votable?url=<xsl:value-of select="//topcat/url/@val" />
						<xsl:if test="//topcat/xaxis">
							<xsl:if test="//topcat/yaxis">
								&amp;xaxis=<xsl:value-of select="//topcat/xaxis/@val" />
								&amp;yaxis=<xsl:value-of select="//topcat/yaxis/@val" />
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
		<form method="get" name="TopCatForm">
			<xsl:element name="input">
				<xsl:attribute name="type">hidden</xsl:attribute>
				<xsl:attribute name="name">url</xsl:attribute>
				<xsl:attribute name="value"><xsl:value-of select="//topcat/url/@val" /></xsl:attribute>				
			</xsl:element>
			<br />
			<strong>You can replot to various x and y axis by choosing the axis below and hit "Plot it"</strong>
			<br />
			<strong>xaxis:</strong>
			<select name="xaxis">
				<xsl:for-each select="//topcat/axislist/axisitem">
					<xsl:element name="option">
						<xsl:attribute name="value"><xsl:value-of select="@val"/></xsl:attribute>
						<xsl:if test="@defaultx">
							<xsl:attribute name="selected"><xsl:value-of select="true" /></xsl:attribute>
						</xsl:if>
						<xsl:value-of select="@val"/>
					</xsl:element>
				</xsl:for-each>
			</select>
			<br />
			<strong>yaxis:</strong>
			<select name="yaxis">
				<xsl:for-each select="//topcat/axislist/axisitem">
					<xsl:element name="option">
						<xsl:attribute name="value"><xsl:value-of select="@val"/></xsl:attribute>
						<xsl:if test="@defaulty">
							<xsl:attribute name="selected"><xsl:value-of select="true" /></xsl:attribute>
						</xsl:if>
						<xsl:value-of select="@val"/>
					</xsl:element>
				</xsl:for-each>
			</select>
			<br />
			<input type="submit" name="replot" value="Plot it" />
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
