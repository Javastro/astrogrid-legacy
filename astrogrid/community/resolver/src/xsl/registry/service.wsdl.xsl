<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/resolver/src/xsl/registry/service.wsdl.xsl,v $</cvs:source>
    | <cvs:author>$Author: dave $</cvs:author>
    | <cvs:date>$Date: 2004/03/30 01:40:03 $</cvs:date>
    | <cvs:version>$Revision: 1.3 $</cvs:version>
    | <cvs:log>
    |   $Log: service.wsdl.xsl,v $
    |   Revision 1.3  2004/03/30 01:40:03  dave
    |   Merged development branch, dave-dev-200403242058, into HEAD
    |
    |   Revision 1.2.4.1  2004/03/28 09:11:43  dave
    |   Convert tabs to spaces
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
    xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
    xmlns:wsdlsoap="http://schemas.xmlsoap.org/wsdl/soap/"
    >
    <!--+
        | Params from the Ant build
        +-->
    <xsl:param name="service.name"/>
    <xsl:param name="service.href"/>

    <!--+
        | Process the address element.
        +-->
    <xsl:template match="/wsdl:definitions/wsdl:service/wsdl:port/wsdlsoap:address">
        <xsl:copy>
            <xsl:attribute name="location">
                <xsl:value-of select="$service.href"/>
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

