<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<!-- very simple stylesheet to transform the web.xml deployment descriptor
 - want to adjust key value for which component manager to use. -->

<xsl:output method="xml" indent="yes" />

<xsl:template priority="2" match="env-entry[env-entry-name='cea.component.manager.class']">
        <env-entry>
                <description><xsl:value-of select="description" /></description>
                <env-entry-name><xsl:value-of select="env-entry-name" /></env-entry-name>
                <env-entry-type><xsl:value-of select="env-entry-type" /></env-entry-type>
                <env-entry-value> org.astrogrid.applications.commandline.CommandLineCEAComponentManager</env-entry-value>
        </env-entry>
</xsl:template>

<xsl:template priority="2" match="web-app/display-name">
        <display-name>Astrogrid CEA Server (cmdline)</display-name>
</xsl:template>	

<!-- copy-all template - matches everything else -->
<xsl:template match="node()|@*" priority="1">
        <xsl:copy> <!-- was:  name="{name()}" -->
                <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
</xsl:template>

</xsl:stylesheet>
