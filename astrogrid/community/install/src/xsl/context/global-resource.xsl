<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/xsl/context/global-resource.xsl,v $</cvs:source>
    | <cvs:author>$Author: dave $</cvs:author>
    | <cvs:date>$Date: 2004/06/18 13:45:20 $</cvs:date>
    | <cvs:version>$Revision: 1.3 $</cvs:version>
    | <cvs:log>
    |   $Log: global-resource.xsl,v $
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
    <xsl:param name="resource.name"/>
    <xsl:param name="resource.auth"/>
    <xsl:param name="resource.type"/>

    <!--+
        | Process a 'GlobalNamingResources' element that already contains our resource.
        +-->
    <xsl:template match="/Server/GlobalNamingResources[Resource/@name = $resource.name]">
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
        | Process a 'GlobalNamingResources' element that does NOT contain our resource.
        +-->
    <xsl:template match="/Server/GlobalNamingResources[not(Resource/@name = $resource.name)]">
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
        | Process a global 'Resource' element with the right name.
        +-->
    <xsl:template name="resource" match="GlobalNamingResources/Resource[@name = $resource.name]">
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
