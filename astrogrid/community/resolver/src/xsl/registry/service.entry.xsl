<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/xsl/registry/service.entry.xsl,v $</cvs:source>
    | <cvs:author>$Author: dave $</cvs:author>
    | <cvs:date>$Date: 2004/07/27 13:41:17 $</cvs:date>
    | <cvs:version>$Revision: 1.5 $</cvs:version>
    | <cvs:log>
    |   $Log: service.entry.xsl,v $
    |   Revision 1.5  2004/07/27 13:41:17  dave
    |   Merged development branch, dave-dev-200407261230, into HEAD
    |
    |   Revision 1.4.32.1  2004/07/27 13:27:07  dave
    |   Added registry entry and resolver to integtation tests
    |
    |   Revision 1.4  2004/06/18 13:45:20  dave
    |   Merged development branch, dave-dev-200406081614, into HEAD
    |
    |   Revision 1.3.32.1  2004/06/17 13:38:59  dave
    |   Tidied up old CVS log entries
    |
    | </cvs:log>
    |
    | XSL transform to update a service entry for the registry cache.
    |
    +-->
<xsl:stylesheet
    version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:vr="http://www.ivoa.net/xml/VOResource/v0.9"
    >
    <!--+
        | Params from the Ant build
        +-->
    <xsl:param name="service.authority"/>
    <xsl:param name="service.resource"/>
    <xsl:param name="service.endpoint"/>

    <!--+
        | Process the Identifier element.
    <xsl:template match="/vr:VODescription/vr:Resource">
        <xsl:element name="FROG"/>
    </xsl:template>
        +-->

    <!--+
        | Process the Identifier element.
        +-->
    <xsl:template match="/vr:VODescription/vr:Resource/vr:Identifier">
        <xsl:element name="vr:Identifier">
            <xsl:element name="vr:AuthorityID">
                <xsl:value-of select="$service.authority"/>
            </xsl:element>
            <xsl:element name="vr:ResourceKey">
                <xsl:value-of select="$service.resource"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <!--+
        | Process the Interface element.
        +-->
    <xsl:template match="/vr:VODescription/vr:Resource/vr:Interface">
        <xsl:element name="vr:Interface">
            <xsl:element name="vr:Invocation">
                <xsl:text>WebService</xsl:text>
            </xsl:element>
            <xsl:element name="vr:AccessURL">
                <xsl:attribute name="use">
                    <xsl:text>base</xsl:text>
                </xsl:attribute>
                <xsl:value-of select="$service.endpoint"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <!-- Default, copy all and apply templates -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>

