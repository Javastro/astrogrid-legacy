<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/client/src/xsl/registry/service.wsdl.xsl,v $</cvs:source>
    | <cvs:author>$Author: jdt $</cvs:author>
    | <cvs:date>$Date: 2004/12/16 17:25:49 $</cvs:date>
    | <cvs:version>$Revision: 1.2 $</cvs:version>
    | <cvs:log>
    |   $Log: service.wsdl.xsl,v $
    |   Revision 1.2  2004/12/16 17:25:49  jdt
    |   merge from dave-dev-200410061224-200412161312
    |
    |   Revision 1.1.2.1  2004/11/24 16:15:08  dave
    |   Added node functions to client ...
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

