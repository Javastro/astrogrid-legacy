<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/xsl/context/context-resource-params.xsl,v $</cvs:source>
    | <cvs:author>$Author: dave $</cvs:author>
    | <cvs:date>$Date: 2004/04/15 02:27:46 $</cvs:date>
    | <cvs:version>$Revision: 1.2 $</cvs:version>
    | <cvs:log>
    |   $Log: context-resource-params.xsl,v $
    |   Revision 1.2  2004/04/15 02:27:46  dave
    |   Merged development branch, dave-dev-200404071355, into HEAD
    |
    |   Revision 1.1.2.1  2004/04/09 00:32:11  dave
    |   Refactored the JNDI context tasks.
    |
    | </cvs:log>
    |
    +-->
<xsl:stylesheet
    version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    >
    <!--+
        | Params from the Ant build.
        +-->
    <xsl:param name="context.path"/>
    <xsl:param name="resource.name"/>
    <xsl:param name="database.url"/>
    <xsl:param name="database.driver.name"/>
    <xsl:param name="database.driver.class"/>
    <xsl:param name="database.user"/>
    <xsl:param name="database.pass"/>

    <!--+
        | Process a 'Context' element that already contains our resource params.
        +-->
    <xsl:template match="//Context[@path = $context.path][ResourceParams/@name = $resource.name]">
        <xsl:copy>
            <!--+
                | Process all of the attributes.
                +-->
            <xsl:apply-templates select="@*"/>
            <!--+
                | Process all of the elements, including our target resource.
                +-->
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!--+
        | Process a 'Context' element that does NOT contain our resource params.
        +-->
    <xsl:template match="//Context[@path = $context.path][not(ResourceParams/@name = $resource.name)]">
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
                 | Call our template to add the new resource params.
                 +-->
            <xsl:call-template name="resource"/>
        </xsl:copy>
    </xsl:template>

    <!--+
        | Process a 'ResourceParams' element with the right name and a parent 'Context' with the right path.
        +-->
    <xsl:template name="resource" match="ResourceParams[@name = $resource.name][parent::Context/@path = $context.path]">
        <xsl:element name="ResourceParams">
            <xsl:attribute name="name">
                <xsl:value-of select="$resource.name"/>
            </xsl:attribute>
            <!--+
                | Add the database URL.
                +-->
            <xsl:element name="parameter">
                <xsl:element name="name">
                    <xsl:text>url</xsl:text>
                </xsl:element>
                <xsl:element name="value">
                    <xsl:value-of select="$database.url"/>
                </xsl:element>
            </xsl:element>
            <!--+
                | Add the database driver name.
                +-->
            <xsl:element name="parameter">
                <xsl:element name="name">
                    <xsl:text>driverName</xsl:text>
                </xsl:element>
                <xsl:element name="value">
                    <xsl:value-of select="$database.driver.name"/>
                </xsl:element>
            </xsl:element>
            <!--+
                | Add the database driver class.
                +-->
            <xsl:element name="parameter">
                <xsl:element name="name">
                    <xsl:text>driverClassName</xsl:text>
                </xsl:element>
                <xsl:element name="value">
                    <xsl:value-of select="$database.driver.class"/>
                </xsl:element>
            </xsl:element>
            <!--+
                | Add the database user name.
                +-->
            <xsl:element name="parameter">
                <xsl:element name="name">
                    <xsl:text>username</xsl:text>
                </xsl:element>
                <xsl:element name="value">
                    <xsl:value-of select="$database.user"/>
                </xsl:element>
            </xsl:element>
            <!--+
                | Add the database password.
                +-->
            <xsl:element name="parameter">
                <xsl:element name="name">
                    <xsl:text>password</xsl:text>
                </xsl:element>
                <xsl:element name="value">
                    <xsl:value-of select="$database.pass"/>
                </xsl:element>
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
