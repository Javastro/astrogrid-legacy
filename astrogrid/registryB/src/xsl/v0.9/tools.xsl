<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/registryB/src/xsl/v0.9/Attic/tools.xsl,v $</cvs:source>
    | <cvs:author>$Author: KevinBenson $</cvs:author>
    | <cvs:date>$Date: 2003/10/28 10:11:52 $</cvs:date>
    | <cvs:version>$Revision: 1.1 $</cvs:version>
    | <cvs:log>
    |   $Log: tools.xsl,v $
    |   Revision 1.1  2003/10/28 10:11:52  KevinBenson
    |   New Registry structure started with RegistryB so we can sort of leave the other one alone for now.
    |
    | </cvs:log>
    |
    +-->
<xsl:stylesheet
    version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    >

    <!--+
        | Generate the name for a JavaBean.
        +-->
    <xsl:template name="bean.name">
        <xsl:param name="bean"/>
        <!-- Add this type name -->
        <xsl:call-template name="first.upper">
            <xsl:with-param name="text" select="$bean/@type"/>
        </xsl:call-template>
        <!-- Add our parent type name(s) -->
        <xsl:if test="$bean/@extends">
            <xsl:call-template name="bean.name">
                <xsl:with-param name="bean" select="/schema/object[@type = $bean/@extends]"/>
            </xsl:call-template>
        </xsl:if>
    </xsl:template>

    <!--+
        | Uppercase the first letter of a string.
        +-->
    <xsl:template name="first.upper">
        <xsl:param name="text"/>
        <xsl:value-of select="concat(translate(substring($text, 1, 1), 'abcdefghijklmnopqrstuvwxyz', 'ABCDEFGHIJKLMNOPQRSTUVWXYZ'), substring($text, 2))"/>
    </xsl:template>

    <!--+
        | Lowercase the first letter of a string.
        +-->
    <xsl:template name="first.lower">
        <xsl:param name="text"/>
        <xsl:value-of select="concat(translate(substring($text, 1, 1), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), substring($text, 2))"/>
    </xsl:template>

</xsl:stylesheet>
