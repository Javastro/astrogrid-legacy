<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/registryB/src/xsl/v0_9/Attic/tables.xsl,v $</cvs:source>
    | <cvs:author>$Author: KevinBenson $</cvs:author>
    | <cvs:date>$Date: 2003/10/28 12:02:09 $</cvs:date>
    | <cvs:version>$Revision: 1.1 $</cvs:version>
    | <cvs:log>
    |   $Log: tables.xsl,v $
    |   Revision 1.1  2003/10/28 12:02:09  KevinBenson
    |   Straiten up the build.xml where it will compile and started on a MockDelegate
    |
    |   Revision 1.1  2003/10/28 10:11:52  KevinBenson
    |   New Registry structure started with RegistryB so we can sort of leave the other one alone for now.
    |
    | </cvs:log>
    |
    +-->
<xsl:stylesheet
    version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    >
    <!--+
        | Import our tools.
        +-->
    <xsl:import href="tools.xsl"/>

    <!--+
        | XSL params from the Ant script.
    <xsl:param name="bean.xslt"/>
        +-->

    <!--+
        | Match the top level schema.
        +-->
    <xsl:template match="/schema">
        <xsl:apply-templates select="object"/>
    </xsl:template>

    <!--+
        | Match the 'bean' base class.
        +-->
    <xsl:template match="object[@type = 'bean']">
        <!--+
            | Do nothing, this type does not have a database table.
            +-->
    </xsl:template>

    <!--+
        | Match an object element, apart from the 'bean' base class.
        +-->
    <xsl:template match="object[@type != 'bean']">
        <!--+
            | Open the table declaration.
            +-->
        <xsl:call-template name="table.open">
            <xsl:with-param name="name" select="@type"/>
        </xsl:call-template>
        <!--+
            | Process the object properties.
            +-->
        <xsl:apply-templates select="property"/>
        <!--+
            | Add the ident field from the 'bean' object type.
            +-->
        <xsl:apply-templates select="/schema/object[@type = 'bean']/property[@name = 'ident']"/>
        <!--+
            | Add the primary key.
            | This should probably come from the schema by locating it in the bean base class.
            +-->
        <xsl:call-template name="table.primary.key">
            <xsl:with-param name="name">ident</xsl:with-param>
        </xsl:call-template>
        <!--+
            | Close the table declaration.
            +-->
        <xsl:call-template name="table.close"/>
    </xsl:template>

    <!--+
        | Match an unknown property.
        +-->
    <xsl:template match="property">
        <xsl:text><![CDATA[
            -- WARNING Unknown property type.
            -- Name : ]]></xsl:text><xsl:value-of select="@name"/><xsl:text><![CDATA[
            -- Type : ]]></xsl:text><xsl:value-of select="@type"/><xsl:text><![CDATA[
            -- Desc : ]]></xsl:text><xsl:call-template name="first.upper"><xsl:with-param name="text" select="description/text()"/></xsl:call-template><xsl:text><![CDATA[.]]></xsl:text>
    </xsl:template>

    <!--+
        | Match a string property.
        +-->
    <xsl:template match="property[@type = 'string']">
        <xsl:call-template name="table.field">
            <xsl:with-param name="name" select="@name"/>
            <xsl:with-param name="type">VARCHAR</xsl:with-param>
            <xsl:with-param name="null" select="@null = 'yes'"/>
        </xsl:call-template>
    </xsl:template>

    <!--+
        | Match an integer property.
        +-->
    <xsl:template match="property[@type = 'integer']">
        <xsl:call-template name="table.field">
            <xsl:with-param name="name" select="@name"/>
            <xsl:with-param name="type">INTEGER</xsl:with-param>
            <xsl:with-param name="null" select="@null = 'yes'"/>
        </xsl:call-template>
    </xsl:template>

    <!--+
        | Generate the start of a table definition.
        +-->
    <xsl:template name="table.open">
        <xsl:param name="name"/>
        <xsl:text><![CDATA[
        CREATE TABLE ]]></xsl:text>
            <!-- Add the table name -->
            <xsl:call-template name="first.lower">
                 <xsl:with-param name="text" select="$name"/>
            </xsl:call-template>
            <xsl:text><![CDATA[
            (]]></xsl:text>
    </xsl:template>

    <!--+
        | Generate a table field.
        +-->
    <xsl:template name="table.field">
        <xsl:param name="name"/>
        <xsl:param name="type"/>
        <xsl:param name="null"/>
        <xsl:text><![CDATA[
            ]]></xsl:text>
        <!-- Add the field name -->
        <xsl:call-template name="first.lower">
             <xsl:with-param name="text" select="$name"/>
        </xsl:call-template>
        <!-- Add the field type -->
        <xsl:text><![CDATA[ ]]></xsl:text>
        <xsl:value-of select="$type"/>
        <xsl:text><![CDATA[ ]]></xsl:text>
        <!-- Check if nulls are allowed -->
        <xsl:choose>
            <xsl:when test="$null">
                <xsl:text><![CDATA[NULL]]></xsl:text>
            </xsl:when>
            <xsl:otherwise>
                <xsl:text><![CDATA[NOT NULL]]></xsl:text>
            </xsl:otherwise>
        </xsl:choose>
        <!-- Always add a comma -->
        <xsl:text><![CDATA[,]]></xsl:text>
    </xsl:template>

    <!--+
        | Generate the primary key definition.
        +-->
    <xsl:template name="table.primary.key">
        <xsl:param name="name"/>
        <xsl:text><![CDATA[
            PRIMARY KEY (]]></xsl:text><xsl:value-of select="$name"/><xsl:text><![CDATA[)]]></xsl:text>
    </xsl:template>

    <!--+
        | Generate the end of a table definition.
        +-->
    <xsl:template name="table.close">
        <xsl:text><![CDATA[
            ) ;
        ]]></xsl:text>
    </xsl:template>

    <!--+
        | Default, copy all and apply templates.
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>
        +-->

</xsl:stylesheet>
