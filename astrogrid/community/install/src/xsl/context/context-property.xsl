<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/xsl/context/context-property.xsl,v $</cvs:source>
    | <cvs:author>$Author: dave $</cvs:author>
    | <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
    | <cvs:version>$Revision: 1.3 $</cvs:version>
    | <cvs:log>
    |   $Log: context-property.xsl,v $
    |   Revision 1.3  2004/06/18 13:45:20  dave
    |   Merged development branch, dave-dev-200406081614, into HEAD
    |
    |   Revision 1.2.18.1  2004/06/17 13:38:59  dave
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
    <xsl:param name="property.name"/>
    <xsl:param name="property.type"/>
    <xsl:param name="property.value"/>

    <!--+
        | Process a 'Context' element that already contains our property.
        +-->
    <xsl:template match="//Context[@path = $context.path][Environment/@name = $property.name]">
        <xsl:copy>
            <!--+
                | Process all of the attributes.
                +-->
            <xsl:apply-templates select="@*"/>
            <!--+
                | Process all of the elements, including our target property.
                +-->
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!--+
        | Process a 'Context' element that does NOT contain our property.
        +-->
    <xsl:template match="//Context[@path = $context.path][not(Environment/@name = $property.name)]">
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
                 | Call our template to add a new property.
                 +-->
            <xsl:call-template name="property"/>
        </xsl:copy>
    </xsl:template>

    <!--+
        | Process an 'Environment' element with the right name and a parent 'Context' with the right path.
        +-->
    <xsl:template name="property" match="Environment[@name = $property.name][parent::Context/@path = $context.path]">
        <xsl:element name="Environment">
            <xsl:attribute name="name">
                <xsl:value-of select="$property.name"/>
            </xsl:attribute>
            <xsl:attribute name="type">
                <xsl:value-of select="$property.type"/>
            </xsl:attribute>
            <xsl:attribute name="value">
                <xsl:value-of select="$property.value"/>
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
