<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/xsl/context/context-resource.xsl,v $</cvs:source>
    | <cvs:author>$Author: dave $</cvs:author>
    | <cvs:date>$Date: 2004/04/15 02:27:46 $</cvs:date>
    | <cvs:version>$Revision: 1.2 $</cvs:version>
    | <cvs:log>
    |   $Log: context-resource.xsl,v $
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
    <xsl:param name="resource.name"/>
    <xsl:param name="resource.auth"/>
    <xsl:param name="resource.type"/>

    <!--+
        | Process a 'Context' element that already contains our resource.
        +-->
    <xsl:template match="//Context[@path = $context.path][Resource/@name = $resource.name]">
        <xsl:copy>
            <!--+
                | Process all of the attributes.
                +-->
            <xsl:apply-templates select="@*"/>
            <!--+
                | Process all of the elements, including our target resource.
                +-->
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!--+
        | Process a 'Context' element that does NOT contain our resource.
        +-->
    <xsl:template match="//Context[@path = $context.path][not(Resource/@name = $resource.name)]">
        <xsl:copy>
            <!--+
                | Process all of the attributes.
                +-->
            <xsl:apply-templates select="@*"/>
            <!--+
                | Process the rest of the elements.
                +-->
            <xsl:apply-templates/>
            <!--+
                 | Call our template to add a new resource.
                 +-->
            <xsl:call-template name="resource"/>
        </xsl:copy>
    </xsl:template>

    <!--+
        | Process a 'Resource' element with the right name and a parent 'Context' with the right path.
        +-->
    <xsl:template name="resource" match="Resource[@name = $resource.name][parent::Context/@path = $context.path]">
        <xsl:element name="Resource">
            <xsl:attribute name="name">
                <xsl:value-of select="$resource.name"/>
            </xsl:attribute>
            <xsl:attribute name="type">
                <xsl:value-of select="$resource.type"/>
            </xsl:attribute>
            <xsl:attribute name="auth">
                <xsl:value-of select="$resource.auth"/>
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
