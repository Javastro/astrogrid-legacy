<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/xsl/webapp/Attic/global-property.xsl,v $</cvs:source>
    | <cvs:author>$Author: dave $</cvs:author>
    | <cvs:date>$Date: 2004/03/30 01:40:03 $</cvs:date>
    | <cvs:version>$Revision: 1.2 $</cvs:version>
    | <cvs:log>
    |   $Log: global-property.xsl,v $
    |   Revision 1.2  2004/03/30 01:40:03  dave
    |   Merged development branch, dave-dev-200403242058, into HEAD
    |
    |   Revision 1.1.2.1  2004/03/28 02:00:55  dave
    |   Added database management tasks.
    |
    |   Revision 1.2  2004/02/20 21:11:05  dave
    |   Merged development branch, dave-dev-200402120832, into HEAD
    |
    |   Revision 1.1.2.2  2004/02/16 15:20:54  dave
    |   Changed tabs to spaces
    |
    |   Revision 1.1.2.1  2004/02/16 14:13:20  dave
    |   Fixed JNDI install scripts to use different methods
    |
    | </cvs:log>
    |
    | XST transform to add a global Environment property to the Tomcat config.
    |
    | Params :
    |   property.name  - the JNDI name of the property.
    |   property.type  - the Java type of the property.
    |   property.value - the value of the property.
    |
    +-->
<xsl:stylesheet
    version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    >
    <!--+
        | Params from the Ant build.
        +-->
    <xsl:param name="property.name"/>
    <xsl:param name="property.type"/>
    <xsl:param name="property.value"/>

    <!--+
        | Process a matching 'GlobalNamingResources' element that already contains our property.
        +-->
    <xsl:template match="/Server/GlobalNamingResources[Environment/@name = $property.name]">
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
        | Process a matching 'GlobalNamingResources' element that does NOT contain our property.
        +-->
    <xsl:template match="/Server/GlobalNamingResources[not(Environment/@name = $property.name)]">
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
        | Process our property element.
        | Match a global Environment element with the right name.
        +-->
    <xsl:template name="property" match="GlobalNamingResources/Environment[@name = $property.name]">
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
