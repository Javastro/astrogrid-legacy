<?xml version="1.0"?>
<xsl:stylesheet
	version="1.0" 
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:ast-debug="http://www.astrogrid.org/xml/namespace/ast-debug"
	>

<!--+
    | A stylesheet to transform an XML document into a HTML page.
    | Converts the XML elements into HTML text inside a <pre> block.
    +-->
	<!-- Handle the top level element -->
	<xsl:template match="/">
		<html>
			<body>
				<!-- Page content goes here -->
				<pre>
					<xsl:apply-templates/>
				</pre>
			</body>
		</html>
	</xsl:template>


	<!-- Default node handler, convert to HTML and apply templates -->
	<xsl:template match="node()">
		<br/>
		<!--Indent by number of parent nodes -->
		<xsl:for-each select="ancestor::*">
			<xsl:text>    </xsl:text>
		</xsl:for-each>
		<!-- Open the element -->
		<xsl:text>&lt;</xsl:text>
		<!-- Add the element name -->
		<xsl:value-of select="name()"/>
		<!-- Copy all of the node attributes -->
		<xsl:for-each select="attribute::*">
			<xsl:text> </xsl:text>
			<xsl:value-of select="name()"/>
			<xsl:text>=</xsl:text>
			<xsl:text>&quot;</xsl:text>
			<xsl:value-of select="."/>
			<xsl:text>&quot;</xsl:text>
		</xsl:for-each>
		<!-- If this node does not have any child nodes -->
		<xsl:if test="count(child::node()) + count(child::text()) + count(child::comment()) = 0">
			<!-- Close the element -->
			<xsl:text>/&gt;</xsl:text>
		</xsl:if>
		<!-- If this node does have some child nodes -->
		<xsl:if test="count(child::node()) + count(child::text()) + count(child::comment()) > 0">
			<!-- Close the element -->
			<xsl:text>&gt;</xsl:text>
			<!-- Add the element content -->
			<xsl:apply-templates/>
			<br/>
			<!--Indent by number of parent nodes -->
			<xsl:for-each select="ancestor::*">
				<xsl:text>    </xsl:text>
			</xsl:for-each>
			<!-- Open the element -->
			<xsl:text>&lt;</xsl:text>
			<xsl:text>/</xsl:text>
			<!-- Add the element name -->
			<xsl:value-of select="name()"/>
			<!-- Close the element -->
			<xsl:text>&gt;</xsl:text>
		</xsl:if>
	</xsl:template>

	<!-- Default text handler, enclose in [] and apply templates -->
	<xsl:template match="text()">
		<xsl:if test="string-length(normalize-space(.)) > 0">
			<br/>
			<!--Indent by number of parent nodes -->
			<xsl:for-each select="ancestor::*">
				<xsl:text>    </xsl:text>
			</xsl:for-each>
			<xsl:text>[</xsl:text>
			<xsl:value-of select="normalize-space(.)"/>
			<xsl:text>]</xsl:text>
		</xsl:if>
		<!-- Process any other elements -->
		<xsl:apply-templates/>
	</xsl:template>

	<!-- Default comment handler, enclose in [] and apply templates -->
	<xsl:template match="comment()">
		<xsl:if test="string-length(normalize-space(.)) > 0">
			<br/>
			<!--Indent by number of parent nodes -->
			<xsl:for-each select="ancestor::*">
				<xsl:text>    </xsl:text>
			</xsl:for-each>
			<xsl:text>&lt;-- </xsl:text>
			<xsl:value-of select="normalize-space(.)"/>
			<xsl:text> --&gt;</xsl:text>
		</xsl:if>
		<!-- Process any other elements -->
		<xsl:apply-templates/>
	</xsl:template>

	<!-- Default, copy all and apply templates -->
	<!--
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()" />
		</xsl:copy>
	</xsl:template>
	-->
</xsl:stylesheet>
