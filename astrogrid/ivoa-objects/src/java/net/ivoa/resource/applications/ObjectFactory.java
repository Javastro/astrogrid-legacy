//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.3-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.06.10 at 03:44:35 PM BST 
//


package net.ivoa.resource.applications;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the net.ivoa.resource.applications package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: net.ivoa.resource.applications
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link DataFormat }
     * 
     */
    public DataFormat createDataFormat() {
        return new DataFormat();
    }

    /**
     * Create an instance of {@link SoftwareLibrary }
     * 
     */
    public SoftwareLibrary createSoftwareLibrary() {
        return new SoftwareLibrary();
    }

    /**
     * Create an instance of {@link DesktopApplication }
     * 
     */
    public DesktopApplication createDesktopApplication() {
        return new DesktopApplication();
    }

    /**
     * Create an instance of {@link ExecutionEnvironment }
     * 
     */
    public ExecutionEnvironment createExecutionEnvironment() {
        return new ExecutionEnvironment();
    }

    /**
     * Create an instance of {@link ApplicationCapability }
     * 
     */
    public ApplicationCapability createApplicationCapability() {
        return new ApplicationCapability();
    }

    /**
     * Create an instance of {@link Application }
     * 
     */
    public Application createApplication() {
        return new Application();
    }

}