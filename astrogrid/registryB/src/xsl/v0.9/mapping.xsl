<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/registryB/src/xsl/v0.9/Attic/mapping.xsl,v $</cvs:source>
    | <cvs:author>$Author: KevinBenson $</cvs:author>
    | <cvs:date>$Date: 2003/10/28 10:11:52 $</cvs:date>
    | <cvs:version>$Revision: 1.1 $</cvs:version>
    | <cvs:log>
    |   $Log: mapping.xsl,v $
    |   Revision 1.1  2003/10/28 10:11:52  KevinBenson
    |   New Registry structure started with RegistryB so we can sort of leave the other one alone for now.
    |
    | </cvs:log>
    |
    | TODO
    | required=true
    | description
    | Change tools bean.name to java.name
    | Change tools bean.name param to object
    | Add tools class.name (java.package + java.name)
    | Change $bean.package to $java.package
    | Make base class type, bean, configurable
    | Make base class ident, ident, configurable
    | Experiment with java.lang.String and java.lang.Integer as the java types.
    |
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
        +-->
    <xsl:param name="bean.package"/>

    <!--+
        | Match the top level schema.
        +-->
    <xsl:template match="/schema">
        <xsl:element name="mapping">
            <xsl:apply-templates select="object"/>
        </xsl:element>
    </xsl:template>

    <!--+
        | Match the 'bean' base class.
        +-->
    <xsl:template match="object[@type = 'bean']">
        <!--+
            | Do nothing, this type does not have a database mapping.
            +-->
    </xsl:template>

    <!--+
        | Match an object element, apart from the 'bean' base class.
        +-->
    <xsl:template match="object[@type != 'bean']">
        <xsl:variable name="super.type" select="@extends"/>

        <xsl:element name="class">
            <!--+
                | Add the JavaBean class.
                +-->
            <xsl:attribute name="name">
                <xsl:value-of select="$bean.package"/>
                <xsl:text>.</xsl:text>
                <xsl:call-template name="bean.name">
                    <xsl:with-param name="bean" select="."/>
                </xsl:call-template>
            </xsl:attribute>

            <!--+
                | Add the super class (if it is not the bean base class).
                +-->
            <xsl:if test="$super.type != 'bean'">
                <xsl:attribute name="extends">
                    <xsl:value-of select="$bean.package"/>
                    <xsl:text>.</xsl:text>
                    <xsl:call-template name="bean.name">
                        <xsl:with-param name="bean" select="/schema/object[@type = $super.type]"/>
                    </xsl:call-template>
                </xsl:attribute>
            </xsl:if>

            <!--+
                | Add the identity field.
                +-->
            <xsl:attribute name="identity">
                <xsl:text>ident</xsl:text>
            </xsl:attribute>

            <!--+
                | Add the database table.
                +-->
            <xsl:element name="map-to">
                <xsl:attribute name="table">
                    <xsl:call-template name="first.lower">
                         <xsl:with-param name="text" select="@type"/>
                    </xsl:call-template>
                </xsl:attribute>
            </xsl:element>

            <!--+
                | Process the object properties.
                +-->
            <xsl:apply-templates select="property"/>

            <!--+
                | Add the ident field from the 'bean' object type.
                +-->
            <xsl:apply-templates select="/schema/object[@type = 'bean']/property[@name = 'ident']"/>

        </xsl:element>
    </xsl:template>

    <!--+
        | Match an unknown property.
        +-->
    <xsl:template match="property">
        <xsl:comment>
            <xsl:text><![CDATA[+
            | WARNING Unknown property type.
            | Name : ]]></xsl:text><xsl:value-of select="@name"/><xsl:text><![CDATA[
            | Type : ]]></xsl:text><xsl:value-of select="@type"/><xsl:text><![CDATA[
            | Desc : ]]></xsl:text><xsl:call-template name="first.upper"><xsl:with-param name="text" select="description/text()"/></xsl:call-template><xsl:text><![CDATA[.
            +]]></xsl:text>
        </xsl:comment>
    </xsl:template>

    <!--+
        | Match a string property.
        +-->
    <xsl:template match="property[@type = 'string']">
        <xsl:call-template name="map.field">
            <xsl:with-param name="java.name" select="@name"/>
            <xsl:with-param name="java.type">java.lang.String</xsl:with-param>
            <xsl:with-param name="sql.name" select="@name"/>
            <xsl:with-param name="sql.type">char</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <!--+
        | Match an integer property.
        +-->
    <xsl:template match="property[@type = 'integer']">
        <xsl:call-template name="map.field">
            <xsl:with-param name="java.name" select="@name"/>
            <xsl:with-param name="java.type">integer</xsl:with-param>
            <xsl:with-param name="sql.name" select="@name"/>
            <xsl:with-param name="sql.type">integer</xsl:with-param>
        </xsl:call-template>
    </xsl:template>

    <!--+
        | Generate a field element.
        +-->
    <xsl:template name="map.field">
        <xsl:param name="java.name"/>
        <xsl:param name="java.type"/>
        <xsl:param name="sql.name"/>
        <xsl:param name="sql.type"/>
        <xsl:element name="field">
            <xsl:attribute name="name">
                <xsl:call-template name="first.lower">
                    <xsl:with-param name="text" select="$java.name"/>
                </xsl:call-template>
            </xsl:attribute>
            <xsl:attribute name="type">
                <xsl:value-of select="$java.type"/>
            </xsl:attribute>
            <xsl:element name="sql">
                <xsl:attribute name="name">
                    <xsl:call-template name="first.lower">
                        <xsl:with-param name="text" select="$sql.name"/>
                    </xsl:call-template>
                </xsl:attribute>
                <xsl:attribute name="type">
                    <xsl:value-of select="$sql.type"/>
                </xsl:attribute>
            </xsl:element>
        </xsl:element>
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
