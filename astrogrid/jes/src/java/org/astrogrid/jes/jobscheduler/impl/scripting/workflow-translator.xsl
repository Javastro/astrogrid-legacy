<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
<!-- this stylesheet takes the compile-up version of a workflow (i.e. a document of rules)
and translates this into the corresponding python code.
-->
<xsl:output method="text" />
<xsl:strip-space elements="*" />

<xsl:template match="rules">
from workflows import Interpreter, Rule
interp = Interpreter()
	<xsl:apply-templates select="rule" />
</xsl:template>

<!-- translates from a rule element to a call to the Rule constructor. some care needed 
to escape striing args that may themselves contain ' or ", and handling of optional parameters
-->
<xsl:template match="rule">
interp.addRule(Rule("<xsl:value-of select="@name"/>","""<xsl:value-of select="normalize-space(@trigger)"/>""",[
  <xsl:for-each select="action">
  <xsl:if test="position() != 1">,</xsl:if>"""<xsl:value-of select="." />"""
  </xsl:for-each>
  ]
  <xsl:if test="@env">
  , '<xsl:value-of select="normalize-space(@env)"/>'
  </xsl:if>
  ))
  
</xsl:template>

</xsl:stylesheet>
