<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:param name="view=source" />

	<xsl:template match="workflow">
		<html>
			<head>
				<title>
					Maintain Workflow <xsl:value-of select="workflow-name"/>
				</title>
			</head>
			<body bgcolor="white" alink="red" link="blue" vlink="blue">
				<xsl:apply-templates/>
			</body>
		</html>
	</xsl:template>
	
	<xsl:template match="workflow-names">
		<h2 style="color: navy; text-align: center">
			<xsl:value-of select="workflow-name"/>
			<xsl:apply-templates/>
		</h2>
	</xsl:template>
	
	<xsl:template match="workflow-description">
		<p align="left">
			<xsl:value-of select="workflow-description"/>
			<xsl:apply-templates/>
		</p>
		<a href="http://www.astrogrid.org">
			<img src="OneStepJob.gif" title="AstroGrid Home" alt="" style="border: 0px solid ; width: 200px; height: 250px;"/>
		</a>
	</xsl:template>
	
</xsl:stylesheet>
		
	