<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/xsl/database-config.xsl,v $</cvs:source>
    | <cvs:author>$Author: dave $</cvs:author>
    | <cvs:date>$Date: 2004/02/12 06:56:46 $</cvs:date>
    | <cvs:version>$Revision: 1.2 $</cvs:version>
    | <cvs:log>
    |   $Log: database-config.xsl,v $
    |   Revision 1.2  2004/02/12 06:56:46  dave
    |   Merged development branch, dave-dev-200401131047, into HEAD
    |
    |   Revision 1.1.2.1  2004/01/26 13:18:08  dave
    |   Added new DatabaseManager to enable local JUnit testing
    |
    | </cvs:log>
    | 
    +-->
<xsl:stylesheet
    version="1.0" 
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:wsdd="http://xml.apache.org/axis/wsdd/"
    >
    <!-- Configuration params from the Maven build -->
    <xsl:param name="url"/>
    <xsl:param name="name"/>
    <xsl:param name="engine"/>
    <xsl:param name="mapping"/>
    <xsl:param name="driver"/>
    <xsl:param name="user"/>
    <xsl:param name="pass"/>


    <!-- Match the database element -->
    <xsl:template match="/database">
        <xsl:copy>
            <xsl:attribute name="name">
                <xsl:value-of select="$name"/>
            </xsl:attribute>
            <xsl:attribute name="engine">
                <xsl:value-of select="$engine"/>
            </xsl:attribute>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!-- Match the database driver element -->
    <xsl:template match="/database/driver">
        <xsl:copy>
            <xsl:attribute name="class-name">
                <xsl:value-of select="$driver"/>
            </xsl:attribute>
            <xsl:attribute name="url">
                <xsl:value-of select="$url"/>
            </xsl:attribute>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!-- Match the database user name param -->
    <xsl:template match="/database/driver/param[@name = 'user']">
        <xsl:copy>
            <xsl:attribute name="name">
                <xsl:text>user</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="value">
                <xsl:value-of select="$user"/>
            </xsl:attribute>
        </xsl:copy>
    </xsl:template>

    <!-- Match the database password param -->
    <xsl:template match="/database/driver/param[@name = 'password']">
        <xsl:copy>
            <xsl:attribute name="name">
                <xsl:text>password</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="value">
                <xsl:value-of select="$pass"/>
            </xsl:attribute>
        </xsl:copy>
    </xsl:template>

    <!-- Match the database mapping element -->
    <xsl:template match="/database/mapping">
        <xsl:copy>
            <xsl:attribute name="href">
                <xsl:value-of select="$mapping"/>
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
