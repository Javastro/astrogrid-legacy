<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/registryB/src/xsl/v0_9/Attic/beans.xsl,v $</cvs:source>
    | <cvs:author>$Author: KevinBenson $</cvs:author>
    | <cvs:date>$Date: 2003/10/28 12:02:09 $</cvs:date>
    | <cvs:version>$Revision: 1.1 $</cvs:version>
    | <cvs:log>
    |   $Log: beans.xsl,v $
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
        +-->
    <xsl:param name="bean.xslt"/>
    <xsl:param name="bean.schema"/>
    <xsl:param name="bean.path"/>
    <xsl:param name="bean.package"/>

    <!--+
        | Match the top level schema.
        +-->
    <xsl:template match="/schema">

        <xsl:comment>Ant build script to generate JavaBeans from our schema.</xsl:comment>
        <xsl:element name="project">
            <xsl:attribute name="default">
                <xsl:text>bean.xslt</xsl:text>
            </xsl:attribute>

            <xsl:comment>Ant target to generate JavaBeans from our schema.</xsl:comment>
            <target name="bean.xslt" depends="">
                <echo message=""/>
                <echo message="Creating JavaBeans from schema"/>
                <xsl:apply-templates select="object"/>
            </target>

        </xsl:element>
    </xsl:template>

    <!--+
        | Process a top level object definition.
        | Create an Ant XSLT task to generate the object class from the schema.
        +-->
    <xsl:template match="/schema/object">

        <xsl:variable name="name">
            <xsl:call-template name="bean.name">
                <xsl:with-param name="bean" select="."/>
            </xsl:call-template>
        </xsl:variable>

        <xsl:comment>
            <xsl:text> </xsl:text>
            <xsl:text>Generate Java code for</xsl:text>
            <xsl:text> </xsl:text>
            <xsl:value-of select="$name"/>
            <xsl:text> </xsl:text>
        </xsl:comment>
        <xsl:element name="echo">
            <xsl:attribute name="message">
                <xsl:text></xsl:text>
            </xsl:attribute>
        </xsl:element>
        <xsl:element name="echo">
            <xsl:attribute name="message">
                <xsl:text>Generating Java code for </xsl:text>
                <xsl:value-of select="$name"/>
            </xsl:attribute>
        </xsl:element>

        <xsl:element name="xslt">
            <xsl:attribute name="style">
                <xsl:value-of select="$bean.xslt"/>
            </xsl:attribute>

            <xsl:attribute name="in">
                <xsl:value-of select="$bean.schema"/>
            </xsl:attribute>

            <xsl:attribute name="out">
                <xsl:value-of select="$bean.path"/>
                <xsl:text>/</xsl:text>
                <xsl:value-of select="translate($bean.package,'.','/')"/>
                <xsl:text>/</xsl:text>
                <xsl:value-of select="$name"/>
                <xsl:text>.java</xsl:text>
            </xsl:attribute>

            <xsl:element name="param">
                <xsl:attribute name="name">
                    <xsl:text>bean.name</xsl:text>
                </xsl:attribute>
                <xsl:attribute name="expression">
                    <xsl:value-of select="$name"/>
                </xsl:attribute>
            </xsl:element>

            <xsl:element name="param">
                <xsl:attribute name="name">
                    <xsl:text>bean.type</xsl:text>
                </xsl:attribute>
                <xsl:attribute name="expression">
                    <xsl:value-of select="@type"/>
                </xsl:attribute>
            </xsl:element>

            <xsl:element name="param">
                <xsl:attribute name="name">
                    <xsl:text>bean.package</xsl:text>
                </xsl:attribute>
                <xsl:attribute name="expression">
                    <xsl:value-of select="$bean.package"/>
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
