<?xml version="1.0"?>
<!--+
    |
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/maven/src/xsl/wsdd-scope.xsl,v $</cvs:source>
    | <cvs:author>$Author: jdt $</cvs:author>
    | <cvs:date>$Date: 2004/11/25 00:19:27 $</cvs:date>
    | <cvs:version>$Revision: 1.2 $</cvs:version>
    | <cvs:log>
    |   $Log: wsdd-scope.xsl,v $
    |   Revision 1.2  2004/11/25 00:19:27  jdt
    |   Merge from dave-dev-200410061224-200411221626
    |
    |   Revision 1.1.2.1  2004/10/15 03:53:04  dave
    |   Changed WSDD deployment to 'Application' to allow multiple mock instances.
    |
    | </cvs:log>
    | 
    +-->
<xsl:stylesheet
    version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:wsdd="http://xml.apache.org/axis/wsdd/"
    xmlns="http://xml.apache.org/axis/wsdd/"
    >
    <!--+
    	| Params from the Ant build.
    	+-->
    <xsl:param name="service">target service name</xsl:param>
    <xsl:param name="scope">deployment scope</xsl:param>

    <!--+
    	| Match the service deployment.
    	+-->
    <xsl:template match="wsdd:deployment/wsdd:service[@name=$service]">
        <xsl:copy>
		    <!--+
		    	| Copy the attributes.
		    	+-->
            <xsl:apply-templates select="@*"/>
		    <!--+
		    	| Add the scope.
		    	+-->
			<xsl:element name="wsdd:parameter">
	            <xsl:attribute name="name">
	                <xsl:text>scope</xsl:text>
	            </xsl:attribute>
	            <xsl:attribute name="value">
	                <xsl:value-of select="$scope"/>
	            </xsl:attribute>
			</xsl:element>
		    <!--+
		    	| Copy the rest of the elements.
		    	+-->
            <xsl:apply-templates select="node()"/>
        </xsl:copy>
    </xsl:template>

    <!-- Default, copy all and apply templates -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
