<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/xsl/webapp/Attic/webxml-property.xsl,v $</cvs:source>
    | <cvs:author>$Author: dave $</cvs:author>
    | <cvs:date>$Date: 2004/03/30 01:40:03 $</cvs:date>
    | <cvs:version>$Revision: 1.2 $</cvs:version>
    | <cvs:log>
    |   $Log: webxml-property.xsl,v $
    |   Revision 1.2  2004/03/30 01:40:03  dave
    |   Merged development branch, dave-dev-200403242058, into HEAD
    |
    |   Revision 1.1.2.1  2004/03/28 02:00:55  dave
    |   Added database management tasks.
    |
    |   Revision 1.2  2004/02/20 21:11:05  dave
    |   Merged development branch, dave-dev-200402120832, into HEAD
    |
    |   Revision 1.1.2.2  2004/02/16 15:20:54  dave
    |   Changed tabs to spaces
    |
    |   Revision 1.1.2.1  2004/02/15 00:28:22  dave
    |   Added tools for adding separate webapp context file to Tomcat
    |
    | </cvs:log>
    |
    | XSL template to add a JNDI env-entry to a WebApp web.xml file.
    | Params :
    |   property.name  - the JNDI name of the property.
    |   property.type  - the Java type of the property.
    |   property.value - the value of the property.
    |
    +-->
<xsl:stylesheet
    version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    >
    <!--+
        | Set the output document type.
        +-->
    <xsl:output
        doctype-system="http://java.sun.com/j2ee/dtds/web-app_2_2.dtd"
        doctype-public="-//Sun Microsystems, Inc.//DTD Web Application 2.2//EN"
        />

    <!--+
        | Params from the Ant build.
        +-->
    <xsl:param name="property.name"/>
    <xsl:param name="property.type"/>
    <xsl:param name="property.value"/>

    <!--+
        | Process a web-app element that already contains our property
        +-->
    <xsl:template match="/web-app[env-entry/env-entry-name/text() = $property.name]">
        <xsl:copy>
            <!--+
                | Process all of the attributes.
                +-->
            <xsl:apply-templates select="@*"/>
            <!--+
                | Process all of the elements, including our config-property.
                +-->
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!--+
        | Process a web-app element that does NOT contain our JNDI property.
        +-->
    <xsl:template match="/web-app[not(env-entry/env-entry-name/text() = $property.name)]">
        <xsl:copy>
            <!--+
                | Process all of the attributes.
                +-->
            <xsl:apply-templates select="@*"/>
            <!--+
                | Process the rest of the elements.
                +-->
            <xsl:apply-templates/>
            <!--+
                | Call our property template.
                +-->
            <xsl:call-template name="property"/>
        </xsl:copy>
    </xsl:template>

    <!--+
        | Process a matching JNDI property.
        +-->
    <xsl:template name="property" match="/web-app/env-entry[env-entry-name/text() = $property.name]">
        <xsl:element name="env-entry">
            <xsl:comment> JNDI property for community config </xsl:comment>
            <xsl:element name="env-entry-name">
                <xsl:value-of select="$property.name"/>
            </xsl:element>
            <xsl:element name="env-entry-type">
                <xsl:value-of select="$property.type"/>
            </xsl:element>
            <xsl:element name="env-entry-value">
                <xsl:value-of select="$property.value"/>
            </xsl:element>
        </xsl:element>
    </xsl:template>

    <!--+
        | Default template, copy all and apply templates.
        +-->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
