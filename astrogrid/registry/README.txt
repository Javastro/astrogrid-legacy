README

Release Notes for Astrogrid Iteration 02 Registry
27 June 2003
Elizabeth Auden, MSSL

1. registry.war contains the following files:

	* org/astrogrid/registry/QueryParser3_0.class
	* org/astrogrid/registry/QueryParser3_0.java
	* org/astrogrid/registry/Registry3_0.class
	* org/astrogrid/registry/Registry3_0.java
	* org/astrogrid/registry/RegistryInterface3_0.class
	* org/astrogrid/registry/RegistryInterface3_0.java
	* org/astrogrid/registry/parameters_v1_0.xml
	* org/astrogrid/registry/qpParameters.xml
	* org/astrogrid/registry/registry_v1_0.xml

	* jar_files/activation.jar
	* jar_files/mail.jar
	* jar_files/soap.jar
	* jar_files/soap.war
	* jar_files/xerces.jar
	* jar_files/xml4j.jar
	* jar_files/xqlpdom_1_0_2.jar


2. INSTALL assumes that Tomcat and SOAP are installed on the server. Tomcat 4.1 is available for download from http://jakarta.apache.org/tomcat/tomcat-4.1-doc/index.html.  SOAP can be installed by placing soap.war in the Tomcat/webapps directory.  The Apache SOAP API version 2.2 can be downloaded from http://xml.apache.org/soap/index.html.  Place the file soap.war in the Tomcat/webapps directory, and make sure that activation.jar, mail.jar, and soap.jar are all placed in the Tomcat/lib directory and named in classpath.

3. Apache Axis can also be used, which is a more complete SOAP implementation. Download Axis version 1.1 from http://ws.apache.org/axis. Axis is installed in the same way as Apache SOAP, by placing the axis.war file in the Tomcat/webapps directory.

4.  The registry files use jar files for xml parsing and xql queries in addition to the SOAP related jar files.  Registry.war contains a directory called jar_files containg the following 6 jars and 1 war:

	* activation.jar
	* mail.jar
	* soap.jar
	* soap.war
	* xerces.jar
	* xml4j.jar
	* xqlpdom_1_0_2.zip

The jar files can be placed in Tomcat/common/lib directory, and they must be specified in classpath.  Place soap.war in the Tomcat/webapps directory.
