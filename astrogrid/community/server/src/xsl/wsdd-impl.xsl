<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/xsl/Attic/wsdd-impl.xsl,v $</cvs:source>
    | <cvs:author>$Author: dave $</cvs:author>
    | <cvs:date>$Date: 2004/01/07 10:45:45 $</cvs:date>
    | <cvs:version>$Revision: 1.2 $</cvs:version>
    | <cvs:log>
    |   $Log: wsdd-impl.xsl,v $
    |   Revision 1.2  2004/01/07 10:45:45  dave
    |   Merged development branch, dave-dev-20031224, back into HEAD
    |
    |   Revision 1.1.2.1  2003/12/24 05:54:48  dave
    |   Initial Maven friendly structure (only part of the service implemented)
    |
    | </cvs:log>
    | 
    +-->
<xsl:stylesheet
    version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:wsdd="http://xml.apache.org/axis/wsdd/"
    >
    <!-- Implementation class from the Ant build -->
    <xsl:param name="classname">target class name</xsl:param>
    <xsl:param name="service">target service name</xsl:param>

    <!-- Match the service classname parameter -->
    <xsl:template match="wsdd:deployment/wsdd:service[@name=$service]/wsdd:parameter[@name='className']">
        <xsl:copy>
            <xsl:attribute name="name">
                <xsl:text>className</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="value">
                <xsl:value-of select="$classname"/>
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
