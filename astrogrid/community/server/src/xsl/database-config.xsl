<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/server/src/xsl/database-config.xsl,v $</cvs:source>
    | <cvs:author>$Author: clq2 $</cvs:author>
    | <cvs:date>$Date: 2005/09/09 11:39:00 $</cvs:date>
    | <cvs:version>$Revision: 1.4 $</cvs:version>
    | <cvs:log>
    |   $Log: database-config.xsl,v $
    |   Revision 1.4  2005/09/09 11:39:00  clq2
    |   Comm_KMB_1097
    |
    |   Revision 1.3.196.1  2005/08/09 14:23:46  KevinBenson
    |   cleared up docs when it comes to other databases.  and made a small fix to use the jndi
    |   instead of driver element in server/src/config/astrogrid-database-config.xml by changing the database-config.xsl
    |
    |   Revision 1.3  2004/06/18 13:45:20  dave
    |   Merged development branch, dave-dev-200406081614, into HEAD
    |
    |   Revision 1.2.60.1  2004/06/17 13:38:59  dave
    |   Tidied up old CVS log entries
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

    <!-- Match the database driver element 
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
	-->

    <xsl:template match="/database/jndi">
		<xsl:element name="driver">
            <xsl:attribute name="class-name">
                <xsl:value-of select="$driver"/>
            </xsl:attribute>
            <xsl:attribute name="url">
                <xsl:value-of select="$url"/>
            </xsl:attribute>

			<xsl:element name="param">
            <xsl:attribute name="name">
                <xsl:text>user</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="value">
                <xsl:value-of select="$user"/>
            </xsl:attribute>
			</xsl:element>

			<xsl:element name="param">
            <xsl:attribute name="name">
                <xsl:text>password</xsl:text>
            </xsl:attribute>
            <xsl:attribute name="value">
                <xsl:value-of select="$pass"/>
            </xsl:attribute>
			</xsl:element>
		</xsl:element>
            <xsl:apply-templates/>
    </xsl:template>

    <!-- Match the database user name param 
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
	-->

    <!-- Match the database password param 
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
	-->

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
