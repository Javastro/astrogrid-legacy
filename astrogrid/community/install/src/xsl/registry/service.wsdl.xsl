<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/xsl/registry/service.wsdl.xsl,v $</cvs:source>
    | <cvs:author>$Author: jdt $</cvs:author>
    | <cvs:date>$Date: 2005/01/07 14:14:24 $</cvs:date>
    | <cvs:version>$Revision: 1.4 $</cvs:version>
    | <cvs:log>
    |   $Log: service.wsdl.xsl,v $
    |   Revision 1.4  2005/01/07 14:14:24  jdt
    |   merged from Reg_KMB_787
    |
    |   Revision 1.3.116.1  2005/01/05 10:47:46  KevinBenson
    |   added xdoc descriptions for the community
    |
    |   Revision 1.3  2004/06/18 13:45:20  dave
    |   Merged development branch, dave-dev-200406081614, into HEAD
    |
    |   Revision 1.2.32.1  2004/06/17 13:38:59  dave
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
    xmlns:wsdl="http://schemas.xmlsoap.org/wsdl/"
    xmlns:wsdlsoap="http://schemas.xmlsoap.org/wsdl/soap/"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
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

