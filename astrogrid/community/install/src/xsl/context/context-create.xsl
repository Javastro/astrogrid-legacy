<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/xsl/context/context-create.xsl,v $</cvs:source>
    | <cvs:author>$Author: dave $</cvs:author>
    | <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
    | <cvs:version>$Revision: 1.4 $</cvs:version>
    | <cvs:log>
    |   $Log: context-create.xsl,v $
    |   Revision 1.4  2004/06/18 13:45:20  dave
    |   Merged development branch, dave-dev-200406081614, into HEAD
    |
    |   Revision 1.3.18.1  2004/06/17 13:38:59  dave
    |   Tidied up old CVS log entries
    |
    | </cvs:log>
    |
    +-->
<xsl:stylesheet
    version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    >
    <!--+
        | Params from the Ant build.
        +-->
    <xsl:param name="context.path"/>
    <xsl:param name="context.base"/>

    <!--+
        | Process a 'Context' element.
        +-->
    <xsl:template match="Context">
        <xsl:element name="Context">
            <xsl:attribute name="path">
                <xsl:value-of select="$context.path"/>
            </xsl:attribute>
            <xsl:attribute name="docBase">
                <xsl:value-of select="$context.base"/>
            </xsl:attribute>
        </xsl:element>
    </xsl:template>

    <!--+
        | Default template, copy all and apply templates.
        +-->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
