<?xml version="1.0"?>
<!--+
    | <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/community/install/src/xsl/webapp/Attic/global-resource-params.xsl,v $</cvs:source>
    | <cvs:author>$Author: dave $</cvs:author>
    | <cvs:date>$Date: 2004/03/30 01:40:03 $</cvs:date>
    | <cvs:version>$Revision: 1.2 $</cvs:version>
    | <cvs:log>
    |   $Log: global-resource-params.xsl,v $
    |   Revision 1.2  2004/03/30 01:40:03  dave
    |   Merged development branch, dave-dev-200403242058, into HEAD
    |
    |   Revision 1.1.2.2  2004/03/28 09:11:43  dave
    |   Convert tabs to spaces
    |
    |   Revision 1.1.2.1  2004/03/28 02:00:55  dave
    |   Added database management tasks.
    |
    |   Revision 1.2  2004/02/20 21:11:05  dave
    |   Merged development branch, dave-dev-200402120832, into HEAD
    |
    |   Revision 1.1.2.1  2004/02/20 19:34:11  dave
    |   Added JNDI Resource for community database.
    |   Removed multiple calls to loadDatabaseConfiguration .
    |
    | </cvs:log>
    |
    | XST transform to add a global database Resource to the Tomcat config.
    |
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
        | Params from the Ant build.
        +-->
    <xsl:param name="resource.name"/>
    <xsl:param name="database.url"/>
    <xsl:param name="database.driver.name"/>
    <xsl:param name="database.driver.class"/>
    <xsl:param name="database.user"/>
    <xsl:param name="database.pass"/>

    <!--+
        | Process a matching 'GlobalNamingResources' element that already contains our resource params.
        +-->
    <xsl:template match="/Server/GlobalNamingResources[ResourceParams/@name = $resource.name]">
        <xsl:copy>
            <!--+
                | Process all of the attributes.
                +-->
            <xsl:apply-templates select="@*"/>
            <!--+
                | Process all of the elements, including our target property.
                +-->
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>

    <!--+
        | Process a matching 'GlobalNamingResources' element that does NOT contain our resource params.
        +-->
    <xsl:template match="/Server/GlobalNamingResources[not(ResourceParams/@name = $resource.name)]">
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
                 | Call our template to add a new resource.
                 +-->
            <xsl:call-template name="resource-params"/>
        </xsl:copy>
    </xsl:template>

    <!--+
        | Process our property element.
        | Match a global Resource element with the right name.
        +-->
    <xsl:template name="resource-params" match="GlobalNamingResources/ResourceParams[@name = $resource.name]">
        <xsl:element name="ResourceParams">
            <xsl:attribute name="name">
                <xsl:value-of select="$resource.name"/>
            </xsl:attribute>
            <!--+
                | Add the database URL.
                <parameter> 
                    <name>url</name> 
                    <value>jdbc:hsqldb:/var/astrogrid/community-jndi</value> 
                </parameter> 
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
                <parameter> 
                    <name>driverName</name>
                    <value>jdbc:hsqldb:/var/astrogrid/community-jndi</value> 
                </parameter> 
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
                <parameter> 
                    <name>driverClassName</name> 
                    <value>org.hsqldb.jdbcDriver</value> 
                </parameter> 
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
                <parameter> 
                    <name>username</name> 
                    <value>sa</value> 
                </parameter> 
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
                <parameter> 
                    <name>password</name> 
                    <value></value> 
                </parameter> 
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
