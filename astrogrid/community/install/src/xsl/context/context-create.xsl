<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/xsl/context/context-create.xsl,v $</cvs:source>
    | <cvs:author>$Author: dave $</cvs:author>
    | <cvs:date>$Date: 2004/04/15 02:27:46 $</cvs:date>
    | <cvs:version>$Revision: 1.2 $</cvs:version>
    | <cvs:log>
    |   $Log: context-create.xsl,v $
    |   Revision 1.2  2004/04/15 02:27:46  dave
    |   Merged development branch, dave-dev-200404071355, into HEAD
    |
    |   Revision 1.1.2.1  2004/04/09 00:32:11  dave
    |   Refactored the JNDI context tasks.
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
