<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/release/webapp/config/Attic/config.xsl,v $</cvs:source>
    | <cvs:author>$Author: dave $</cvs:author>
    | <cvs:date>$Date: 2003/12/16 15:08:06 $</cvs:date>
    | <cvs:version>$Revision: 1.2 $</cvs:version>
    | <cvs:log>
    |   $Log: config.xsl,v $
    |   Revision 1.2  2003/12/16 15:08:06  dave
    |   Updated the release jars
    |
    |   Revision 1.4  2003/11/06 15:35:26  dave
    |   Replaced tabs with spaces
    |
    |   Revision 1.3  2003/09/22 13:48:53  dave
    |   Updates to the community/portal build
    |
    |   Revision 1.2  2003/09/17 19:47:21  dave
    |   1) Fixed classnotfound problems in the build.
    |   2) Added the JUnit task to add the initial accounts and groups.
    |   3) Got the build to work together with the portal.
    |   4) Fixed some bugs in the Account handling.
    |
    |   Revision 1.1  2003/09/13 02:18:52  dave
    |   Extended the jConfig configuration code.
    |
    | </cvs:log>
    |
    | XSL transform to update a named config property.
    |
    +-->
<xsl:stylesheet
    version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:wsdd="http://xml.apache.org/axis/wsdd/"
    >
    <!-- Config path from the Ant build -->
    <xsl:param name="category">category</xsl:param>
    <xsl:param name="property.name"></xsl:param>
    <xsl:param name="property.value"></xsl:param>

    <!-- Match the named property -->
    <xsl:template match="/properties/category[@name=$category]/property[@name=$property.name]">
        <xsl:copy>
            <xsl:attribute name="name">
                <xsl:value-of select="$property.name"/>
            </xsl:attribute>
            <xsl:attribute name="value">
                <xsl:value-of select="$property.value"/>
            </xsl:attribute>
        </xsl:copy>
    </xsl:template>

    <!-- Default, copy all and apply templates -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
