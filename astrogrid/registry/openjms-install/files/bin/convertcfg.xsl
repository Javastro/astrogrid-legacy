<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

  <xsl:output method="xml" indent="yes"/>

  <xsl:template match="/">
    <xsl:variable name="version">
      <xsl:call-template name="get-version" />
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="($version='0.6.0') or ($version='0.7.0')">
        <xsl:apply-templates select="JmsConfiguration" />
      </xsl:when>
      <xsl:when test="$version='0.7.2'">
        <xsl:message>
          Configuration is already in the correct format
        </xsl:message>
      </xsl:when>
      <xsl:otherwise>
        <xsl:message>
          <xsl:value-of select="concat('Cannot convert configuration, version',
                              '=', $version)" />
        </xsl:message>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="JmsConfiguration">

    <xsl:element name="Configuration">
      <xsl:apply-templates select="DatabaseConfiguration" />
      <xsl:apply-templates select="AdminConfiguration" />
      <xsl:apply-templates select="ServerConfiguration" />
      <xsl:apply-templates select="ConnectionFactories" />
      <xsl:call-template name="configure-tcp" />
      <xsl:apply-templates select="RmiRegistryConfiguration" />

      <xsl:variable name="embedded">
        <xsl:call-template name="is-embedded-JNDI" />
      </xsl:variable>

      <xsl:if test="not($embedded)">
        <xsl:apply-templates select="JndiClientConfiguration" />
      </xsl:if>
      
      <xsl:apply-templates select="LeaseManagerConfiguration" />
      <xsl:apply-templates select="MessageManagerConfiguration" />
      <xsl:apply-templates select="SchedulerConfiguration" />
      <xsl:apply-templates select="GarbageCollectionConfiguration" />
      <xsl:apply-templates select="LoggerConfiguration" />
      <xsl:apply-templates select="AdministeredDestinations" />
    </xsl:element>
  </xsl:template>

  <xsl:template match="DatabaseConfiguration">
    <xsl:element name="DatabaseConfiguration">

      <xsl:if test="@garbageCollectionInterval">
        <xsl:attribute name="garbageCollectionInterval">
          <xsl:value-of select="@garbageCollectionInterval" />
        </xsl:attribute>
      </xsl:if>

      <xsl:if test="@garbageCollectionBlockSize">
        <xsl:attribute name="garbageCollectionBlockSize">
          <xsl:value-of select="@garbageCollectionBlockSize" />
        </xsl:attribute>
      </xsl:if>

      <xsl:if test="@garbageCollectionThreadPriority">
        <xsl:attribute name="garbageCollectionThreadPriority">
          <xsl:value-of select="@garbageCollectionThreadPriority" />
        </xsl:attribute>
      </xsl:if>

      <xsl:copy-of select="JdbmDatabaseConfiguration" />
      <xsl:apply-templates select="RdbmsDatabaseConfiguration" />
    </xsl:element>
  </xsl:template>

  <xsl:template match="RdbmsDatabaseConfiguration">
   <RdbmsDatabaseConfiguration driver="{@driver}" url="{@url}" 
                               user="{@userName}" password="{@password}" />
  </xsl:template>

  <xsl:template match="AdminConfiguration">
    <AdminConfiguration script="{@jmsServer}" config="{@jmsConfig}" />
  </xsl:template>

  <xsl:template match="RmiRegistryConfiguration">
    <xsl:element name="RmiConfiguration">
      <xsl:attribute name="embeddedRegistry">
        <xsl:value-of select="@embeddedRegistry" />
      </xsl:attribute>
     
      <xsl:if test="@rmiRegistryHost">
        <xsl:attribute name="registryHost">
          <xsl:value-of select="@rmiRegistryHost" />
        </xsl:attribute>
      </xsl:if>

      <xsl:if test="@rmiRegistryPort">
        <xsl:attribute name="registryPort">
          <xsl:value-of select="@rmiRegistryPort" />
        </xsl:attribute>
      </xsl:if>
    </xsl:element>
  </xsl:template>

  <xsl:template match="ServerConfiguration">
    <xsl:element name="ServerConfiguration">
      <xsl:variable name="host">
        <xsl:call-template name="get-host" />
      </xsl:variable>

      <xsl:if test="$host">
        <xsl:attribute name="host">
          <xsl:value-of select="$host" />
        </xsl:attribute>
      </xsl:if>

      <xsl:attribute name="embeddedJNDI">
        <xsl:call-template name="is-embedded-JNDI" />
      </xsl:attribute>
    </xsl:element>
  </xsl:template>

  <xsl:template match="ConnectionFactories">
    <Connectors>
      <xsl:variable name="type">
        <xsl:call-template name="get-connector-type" />
      </xsl:variable>
      <Connector scheme="{$type}">
        <ConnectionFactories>
          <xsl:apply-templates select="ConnectionFactory" />
        </ConnectionFactories>
      </Connector>
    </Connectors>
  </xsl:template>

  <xsl:template match="ConnectionFactory">
    <xsl:choose>
      <xsl:when test="contains(@jndiName, 'JmsQueueConnectionFactory')">
        <QueueConnectionFactory name="{@jndiName}" />
      </xsl:when>
      <xsl:when test="contains(@jndiName, 'JmsTopicConnectionFactory')">
        <TopicConnectionFactory name="{@jndiName}" />
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="JndiClientConfiguration">
    <JndiConfiguration>
      <property name="{@contextFactory}" value="{@value}" />
      <xsl:apply-templates select="JndiContextFactoryProperty" />
    </JndiConfiguration>
  </xsl:template>

  <xsl:template match="JndiContextFactoryProperty">
    <property name="{@property}" value="{@value}" />
  </xsl:template>

  <xsl:template match="LeaseManagerConfiguration | 
                       MessageManagerConfiguration |
                       SchedulerConfiguration |
                       GarbageCollectionConfiguration">
    <xsl:copy-of select="current()" />  <!-- copy it as is -->
  </xsl:template>

  <xsl:template match="LoggerConfiguration">
    <xsl:variable name="version">
      <xsl:call-template name="get-version" />
    </xsl:variable>
    <xsl:choose>
      <xsl:when test="$version='0.6.0'">
        <xsl:element name="LoggerConfiguration">
          <xsl:attribute name="file">
            <xsl:value-of select="'${openjms.home}/config/log4j.xml'" />
          </xsl:attribute>
        </xsl:element>
      </xsl:when>
      <xsl:otherwise>
        <LoggerConfiguration file="{@file}" />
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="AdministeredDestinations">
    <xsl:element name="AdministeredDestinations">
      <xsl:apply-templates select="AdministeredTopic" />
      <xsl:apply-templates select="AdministeredQueue" />
    </xsl:element>
  </xsl:template>

  <xsl:template match="AdministeredTopic">
    <AdministeredTopic name="{@topicName}">
      <xsl:apply-templates select="Subscriber" />
    </AdministeredTopic>
  </xsl:template>

  <xsl:template match="Subscriber">
    <Subscriber name="{@subscriberName}" />
  </xsl:template>

  <xsl:template match="AdministeredQueue">
    <AdministeredQueue name="{@queueName}" />
  </xsl:template>

  <!-- Determines the configuration file version -->
  <xsl:template name="get-version">
    <xsl:choose>
      <xsl:when test="//Configuration/AdminConfiguration@script">
        <xsl:value-of select="'0.7.2'" />
      </xsl:when>
      <xsl:when test="//JmsConfiguration/AdminConfiguration@jmsServer">
        <xsl:choose>
          <xsl:when test="//JmsConfiguration/LoggerConfiguration@file">
            <xsl:value-of select="'0.7.0'" />
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="'0.6.0'" />
          </xsl:otherwise>
        </xsl:choose>        
      </xsl:when>
      <xsl:otherwise>unknown</xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- Determines the type of connector, returning one of 'tcp', 'tcps', -->
  <!-- 'rmi', or 'intravm'                                               -->
  <xsl:template name="get-connector-type">
    <xsl:variable name="class" select="//ServerConfiguration@serverClass" />
    <xsl:choose>
      <xsl:when test="contains($class, '.IpcJmsServer')">tcp</xsl:when>
      <xsl:when test="contains($class, '.SslIpcJmsServer')">tcps</xsl:when>
      <xsl:when test="contains($class, 'RmiJmsServer')">rmi</xsl:when>
      <xsl:when test="contains($class, 'EmbeddedJmsServer')">embedded
      </xsl:when>
    </xsl:choose>
  </xsl:template>

  <xsl:template name="configure-tcp">
    <xsl:variable name="type">
      <xsl:call-template name="get-connector-type" />
    </xsl:variable>

    <xsl:if test="starts-with($type,'tcp')">
      <xsl:variable name="name">
        <xsl:choose>
          <xsl:when test="$type='tcp'">TcpConfiguration</xsl:when>
          <xsl:when test="$type='tcps'">TcpsConfiguration</xsl:when>
        </xsl:choose>
      </xsl:variable>

      <xsl:element name="{$name}">
        <xsl:variable name="host">
          <xsl:call-template name="get-internal-host" />
        </xsl:variable>
        <xsl:if test="$host">
          <xsl:attribute name="internalHost">
            <xsl:value-of select="$host" />
          </xsl:attribute>
        </xsl:if>

        <xsl:variable name="port">
          <xsl:call-template name="get-tcp-port" />
        </xsl:variable>
        <xsl:if test="$port">
          <xsl:attribute name="port">
            <xsl:value-of select="$port" />
          </xsl:attribute>
        </xsl:if>

        <xsl:variable name="jndi-port">
          <xsl:call-template name="get-jndi-tcp-port" />
        </xsl:variable>
        <xsl:if test="jndi-port">
          <xsl:attribute name="jndiPort">
            <xsl:value-of select="$jndi-port" />
          </xsl:attribute>
        </xsl:if>
      </xsl:element>
    </xsl:if>
    
  </xsl:template>
 
  <xsl:template name="is-embedded-JNDI">
    <xsl:value-of select="starts-with(//JndiClientConfiguration@contextFactory,
                                      'org.exolab.jms.jndi')" />
  </xsl:template>

  <xsl:template name="get-host">
    <xsl:value-of select="//ServerConfiguration@serverAddress" />
  </xsl:template>

  <xsl:template name="get-internal-host">
    <xsl:value-of select="//ServerConfiguration@internalServerAddress" />
  </xsl:template>

  <xsl:template name="get-tcp-port">
    <xsl:value-of select="//ServerConfiguration@serverPort" />
  </xsl:template>

  <xsl:template name="get-jndi-tcp-port">
    <xsl:value-of select="//JndiServerConfiguration@jndiServerPort" />
  </xsl:template>

</xsl:stylesheet>
