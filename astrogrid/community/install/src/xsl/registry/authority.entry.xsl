<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/xsl/registry/authority.entry.xsl,v $</cvs:source>
    | <cvs:author>$Author: dave $</cvs:author>
    | <cvs:date>$Date: 2004/04/01 13:27:36 $</cvs:date>
    | <cvs:version>$Revision: 1.2 $</cvs:version>
    | <cvs:log>
    |   $Log: authority.entry.xsl,v $
    |   Revision 1.2  2004/04/01 13:27:36  dave
    |   Merged development branch, dave-dev-200404010813, into HEAD
    |
    |   Revision 1.1.2.1  2004/04/01 13:19:51  dave
    |   Updated install registration tasks.
    |
    |   Revision 1.2  2004/03/30 01:40:03  dave
    |   Merged development branch, dave-dev-200403242058, into HEAD
    |
    |   Revision 1.1.2.2  2004/03/28 09:11:43  dave
    |   Convert tabs to spaces
    |
    |   Revision 1.1.2.1  2004/03/28 02:00:55  dave
    |   Added database management tasks.
    |
    |   Revision 1.2  2004/03/23 16:34:08  dave
    |   Merged development branch, dave-dev-200403191458, into HEAD
    |
    |   Revision 1.1.2.1  2004/03/23 10:48:32  dave
    |   Registry configuration files and PolicyManagerResolver tests.
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
    <xsl:param name="service.date"/>

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
        | Process the Curation Date element.
        +-->
    <xsl:template match="/vr:VODescription/vr:Resource/vr:Curation/vr:Date">
        <xsl:element name="vr:Date">
            <xsl:value-of select="$service.date"/>
        </xsl:element>
    </xsl:template>

    <!--+
        | Default, copy all and apply templates.
        +-->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>

