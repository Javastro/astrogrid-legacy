<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:strip-space elements="*"/>
<xsl:output method="xml" indent="yes"/>

<xsl:template match="/" >
    <xsl:apply-templates />
</xsl:template>


<!-- copy all input to output.
when we encounter an element called 'typeMapping' whose 'type' attribute contains
the adql package, mangle serializer and deserializer attributes 
-->
<xsl:template match="node()">
    <xsl:choose>
    <!-- change className setting to point to our implementation class -->
    <xsl:when test="name() = 'parameter' and @name = 'className'">
    	<xsl:copy>
		<xsl:copy-of select="@*[not(name()='value')]" />
		<xsl:attribute name="value">org.astrogrid.datacenter.axisdataserver.MockAxisDataServerSoapBindingSkeleton</xsl:attribute>
		<xsl:apply-templates />
	</xsl:copy>
    </xsl:when>
    <xsl:otherwise>
        <xsl:copy>
            <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
    </xsl:otherwise>
    </xsl:choose>
</xsl:template>

<!--
bugfix for axis tool - sometimes drops spare &gt; around the place
 - filter these out.
-->
<xsl:template match="@*">
	<xsl:attribute name="{name(.)}"><xsl:value-of select="translate(.,'&gt;','')" /></xsl:attribute>
</xsl:template>



</xsl:stylesheet>
