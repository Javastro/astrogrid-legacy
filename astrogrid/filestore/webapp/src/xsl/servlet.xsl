<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filestore/webapp/src/xsl/servlet.xsl,v $</cvs:source>
    | <cvs:author>$Author: dave $</cvs:author>
    | <cvs:date>$Date: 2004/09/17 16:46:14 $</cvs:date>
    | <cvs:version>$Revision: 1.4 $</cvs:version>
    | <cvs:log>
    |   $Log: servlet.xsl,v $
    |   Revision 1.4  2004/09/17 16:46:14  dave
    |   Fixed servlet deployment in FileStore ...
    |   Changed tabs to spaces in source code ...
    |
    |   Revision 1.3.4.1  2004/09/17 15:10:32  dave
    |   Uncommented the call to servlet deploy in the build script.
    |   Replaced tabs with spaces in source code.
    |
    |   Revision 1.3  2004/09/17 06:57:10  dave
    |   Added commons logging to FileStore.
    |   Updated logging properties in Community.
    |   Fixed bug in AGINAB deployment.
    |   Removed MySpace tests with hard coded grendel address.
    |
    |   Revision 1.2.34.1  2004/09/17 02:52:31  dave
    |   Added DTD and validation to web.xml and servlet.xsl ....
    |
    |   Revision 1.2  2004/08/18 19:00:01  dave
    |   Myspace manager modified to use remote filestore.
    |   Tested before checkin - integration tests at 91%.
    |
    |   Revision 1.1.2.2  2004/08/04 15:39:08  dave
    |   Added servlet mapping
    |
    |   Revision 1.1.2.1  2004/08/04 06:35:02  dave
    |   Added initial stubs for servlet ....
    |
    | </cvs:log>
    |
    +-->
<xsl:stylesheet
    version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    >
    <xsl:output
        doctype-public="-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN"
        doctype-system="http://java.sun.com/j2ee/dtds/web-app_2_2.dtd"
        />

    <!--+
        | Params from the Ant build.
        +-->
    <xsl:param name="servlet.class"/>
    <xsl:param name="servlet.name"/>
    <xsl:param name="servlet.desc"/>
    <xsl:param name="servlet.url"/>

    <!--+
        | Process the last servlet element in a web-app element that does not contain our servlet entry.
    <xsl:template match="//servlet[last()][not(normalize-space(parent::web-app/servlet/servlet-class/text()) = $servlet.class)]">
        +-->
    <xsl:template match="//servlet[last()][not(parent::web-app/servlet/servlet-class/text() = $servlet.class)]">
        <!--+
            | Copy this element.
            +-->
        <xsl:call-template name="default"/>
        <!--+
             | Add our servlet entry.
             +-->
        <xsl:call-template name="servlet"/>
    </xsl:template>

    <!--+
        | Add our servlet element.
    <xsl:template name="servlet" match="servlet[normalize-space(servlet-class/text()) = $servlet.class]">
        +-->
    <xsl:template name="servlet" match="servlet[servlet-class/text() = $servlet.class]">
        <xsl:element name="servlet">
            <xsl:element name="servlet-name">
                <xsl:value-of select="$servlet.name"/>
            </xsl:element>
            <xsl:element name="display-name">
                <xsl:value-of select="$servlet.name"/>
            </xsl:element>
            <xsl:element name="description">
                <xsl:value-of select="$servlet.desc"/>
            </xsl:element>
            <xsl:element name="servlet-class">
                <xsl:value-of select="$servlet.class"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>


    <!--+
        | Process the last servlet-mapping element in a web-app element that does not contain our servlet entry.
    <xsl:template match="//servlet-mapping[last()][not(normalize-space(parent::web-app/servlet-mapping/servlet-name/text()) = $servlet.name)]">
        +-->
    <xsl:template match="//servlet-mapping[last()][not(parent::web-app/servlet-mapping/servlet-name/text() = $servlet.name)]">
        <!--+
            | Copy this element.
            +-->
        <xsl:call-template name="default"/>
        <!--+
             | Add our servlet entry.
             +-->
        <xsl:call-template name="servlet-mapping"/>
    </xsl:template>

    <!--+
        | Add our servlet-mapping element.
    <xsl:template name="servlet-mapping" match="servlet-mapping[normalize-space(servlet-name/text()) = $servlet.name]">
        +-->
    <xsl:template name="servlet-mapping" match="servlet-mapping[servlet-name/text() = $servlet.name]">
        <xsl:element name="servlet-mapping">
            <xsl:element name="servlet-name">
                <xsl:value-of select="$servlet.name"/>
            </xsl:element>
            <xsl:element name="url-pattern">
                <xsl:value-of select="$servlet.url"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <!--+
        | Default template, copy all and apply templates.
        +-->
    <xsl:template name="default" match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
