<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/xsl/registry/authority.entry.xsl,v $</cvs:source>
    | <cvs:author>$Author: jdt $</cvs:author>
    | <cvs:date>$Date: 2005/01/07 14:14:24 $</cvs:date>
    | <cvs:version>$Revision: 1.4 $</cvs:version>
    | <cvs:log>
    |   $Log: authority.entry.xsl,v $
    |   Revision 1.4  2005/01/07 14:14:24  jdt
    |   merged from Reg_KMB_787
    |
    |   Revision 1.3.116.1  2005/01/05 10:47:46  KevinBenson
    |   added xdoc descriptions for the community
    |
    |   Revision 1.3  2004/06/18 13:45:20  dave
    |   Merged development branch, dave-dev-200406081614, into HEAD
    |
    |   Revision 1.2.26.1  2004/06/17 13:38:59  dave
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
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
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

