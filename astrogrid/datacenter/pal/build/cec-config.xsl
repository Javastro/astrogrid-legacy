<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
xmlns="http://xml.apache.org/axis/wsdd/">
<!-- very simple stylesheet to transform the CEC deployment descriptor,
  just sets the impelemntation class to the correct class in the datacenter -->

<xsl:output method="xml" indent="yes" />



<xsl:template match="parameter[@name='className']" priority="2">
  <parameter name="className" value="org.astrogrid.datacenter.service.v05.CeaDataService"/>
</xsl:template>

<!-- copy-all template - matches everything else -->
<xsl:template match="node()|@*" priority="1">
        <xsl:copy> <!-- was:  name="{name()}" -->
                <xsl:apply-templates select="@* | node()"/>
        </xsl:copy>
</xsl:template>

</xsl:stylesheet>
