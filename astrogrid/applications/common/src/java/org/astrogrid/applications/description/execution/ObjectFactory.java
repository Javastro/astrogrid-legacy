//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.3-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.08.28 at 09:45:07 PM BST 
//


package org.astrogrid.applications.description.execution;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.astrogrid.applications.description.execution package. 
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

    private final static QName _JobId_QNAME = new QName("http://www.ivoa.net/xml/CEA/types/v1.1", "jobId");
    private final static QName _Tool_QNAME = new QName("http://www.ivoa.net/xml/CEA/types/v1.1", "tool");
    private final static QName _Message_QNAME = new QName("http://www.ivoa.net/xml/CEA/types/v1.1", "message");
    private final static QName _ExecutionSummary_QNAME = new QName("http://www.ivoa.net/xml/CEA/types/v1.1", "executionSummary");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.astrogrid.applications.description.execution
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ParameterValueArray }
     * 
     */
    public ParameterValueArray createParameterValueArray() {
        return new ParameterValueArray();
    }

    /**
     * Create an instance of {@link InitializeJob }
     * 
     */
    public InitializeJob createInitializeJob() {
        return new InitializeJob();
    }

    /**
     * Create an instance of {@link ParameterValue }
     * 
     */
    public ParameterValue createParameterValue() {
        return new ParameterValue();
    }

    /**
     * Create an instance of {@link MessageType }
     * 
     */
    public MessageType createMessageType() {
        return new MessageType();
    }

    /**
     * Create an instance of {@link ResultListType }
     * 
     */
    public ResultListType createResultListType() {
        return new ResultListType();
    }

    /**
     * Create an instance of {@link ExecutionSummaryType }
     * 
     */
    public ExecutionSummaryType createExecutionSummaryType() {
        return new ExecutionSummaryType();
    }

    /**
     * Create an instance of {@link ListOfParameterValues }
     * 
     */
    public ListOfParameterValues createListOfParameterValues() {
        return new ListOfParameterValues();
    }

    /**
     * Create an instance of {@link InputListType }
     * 
     */
    public InputListType createInputListType() {
        return new InputListType();
    }

    /**
     * Create an instance of {@link Tool }
     * 
     */
    public Tool createTool() {
        return new Tool();
    }

    /**
     * Create an instance of {@link ParameterValueGroup }
     * 
     */
    public ParameterValueGroup createParameterValueGroup() {
        return new ParameterValueGroup();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ivoa.net/xml/CEA/types/v1.1", name = "jobId")
    public JAXBElement<String> createJobId(String value) {
        return new JAXBElement<String>(_JobId_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Tool }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ivoa.net/xml/CEA/types/v1.1", name = "tool")
    public JAXBElement<Tool> createTool(Tool value) {
        return new JAXBElement<Tool>(_Tool_QNAME, Tool.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link MessageType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ivoa.net/xml/CEA/types/v1.1", name = "message")
    public JAXBElement<MessageType> createMessage(MessageType value) {
        return new JAXBElement<MessageType>(_Message_QNAME, MessageType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ExecutionSummaryType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ivoa.net/xml/CEA/types/v1.1", name = "executionSummary")
    public JAXBElement<ExecutionSummaryType> createExecutionSummary(ExecutionSummaryType value) {
        return new JAXBElement<ExecutionSummaryType>(_ExecutionSummary_QNAME, ExecutionSummaryType.class, null, value);
    }

}