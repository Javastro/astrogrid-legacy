/*$Id: ServiceStoreBuilder.java,v 1.1 2003/11/18 11:48:15 nw Exp $
 * Created on 30-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2java;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.NodeCreateRule;
import org.apache.commons.digester.SetPropertyRule;
import org.astrogrid.datacenter.http2java.builder.ResultBuilderType;
import org.astrogrid.datacenter.http2java.request.Parameter;
import org.astrogrid.datacenter.http2java.request.RequestMapperType;
import org.astrogrid.datacenter.http2java.response.ResponseConvertorType;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**Build a store of configured legacy-web-service delegates, defined in xml config file.
 * <p>
 * implemented using common-digester - high-level rules based framework for processing an xml doc.
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public class ServiceStoreBuilder {
    
    private static final String RESULT_ELEMENT = "legacy-service/method/result";
    private static final String RESPONSE_ELEMENT = "*/response"; /* can be placed in a response-chain container element */
    private static final String PARAMETER_ELEMENT = "legacy-service/method/request/parameter";
    private static final String REQUEST_ELEMENT = "legacy-service/method/request";
    private static final String METHOD_ELEMENT = "legacy-service/method";

    private static final String TYPE_ATTR = "type";
    private static final String CLASS_ATTR = "class-name";
    
    public ServiceStoreBuilder() throws ParserConfigurationException {
        digester = new Digester();
        configureDigester();
    }
    
    private Digester digester;
    /** build a store of configured LegacyWebMethods
     * 
     * @param is input stream containing the xml configuration
     * @return store of configured web methods.
     * @throws SAXException ig xml cannot be parsed
     * @throws IOException if stream cannot be read.
     * @see DTD for input stream
     */
    public LegacyWebMethod.Store buildStore(InputStream is) throws SAXException, IOException {
        LegacyWebMethod.Store store  = new LegacyWebMethod.Store();
        digester.clear();
        digester.push(store);
        digester.parse(is);
        return store;
    } 
    
    /** set up rules for the digester */
    protected void configureDigester() throws ParserConfigurationException{
        digester.addObjectCreate(METHOD_ELEMENT,LegacyWebMethod.class);
        digester.addSetProperties(METHOD_ELEMENT);        
        digester.addSetNext(METHOD_ELEMENT,"addService");
        
        digester.addFactoryCreate(REQUEST_ELEMENT, new RequestObjectCreationFactory());
        digester.addSetProperties(REQUEST_ELEMENT);
        digester.addSetNext(REQUEST_ELEMENT,"setRequester");
        
        digester.addObjectCreate(PARAMETER_ELEMENT, Parameter.class);
        digester.addSetProperties(PARAMETER_ELEMENT);
        digester.addSetNext(PARAMETER_ELEMENT,"addParameter");
        
        digester.addFactoryCreate(RESPONSE_ELEMENT,new ResponseObjectCreationFactory()); 
        digester.addSetNext(RESPONSE_ELEMENT,"addConvertor");
        digester.addSetProperties(RESPONSE_ELEMENT);
        
        
        digester.addFactoryCreate(RESULT_ELEMENT,new ResultObjectCreationFactory());
        digester.addSetNext(RESULT_ELEMENT,"setBuilder");
        digester.addSetProperties(RESULT_ELEMENT);

         digester.addRule("*/set-property",new MySetPropertyRule("name","value"));
         digester.addRule("*/set-property/xml-document", new NodeCreateRule());
    }
 
    /** a rule that lets us set arbitrary properties - allows for extension
     * element forms
     * <set-property name="" value="" />
     * or
     * <set-property name="">value</set-property>
     * or
     * <set-property name=""><xml><embedded/><xml/><tags/></xml></set-property>
     */   
    private static class MySetPropertyRule extends SetPropertyRule {

        protected boolean useBody;
        protected String bodyText;
        protected String propertyName;
        public MySetPropertyRule(String name, String value) {
            super(name,value);
            useBody = false;
        }

        public void begin(Attributes attr) throws Exception {
            propertyName = attr.getValue(this.name);
            if (attr.getIndex(this.value) != -1) {
                super.begin(attr);
            } else {
                useBody = true;
            }
        }

        public void body(String arg0) throws Exception {
            if (useBody) {
                this.bodyText =  arg0;
            }            
        }

        public void end() throws Exception {
            // now need to work out whether to use bodyText or xml on stack..
            if (!useBody) {
                return;
            }
            Object o = getDigester().peek();
            if (o instanceof Node) {
                Document doc = XMLUtils.newDocument();
                Node n = doc.importNode( (Node) o,true); 
                NodeList nList = n.getChildNodes();
                for (int i = 0; i < nList.getLength();i++) {
                    doc.appendChild(nList.item(i));
                }                                
                getDigester().pop(); // remove Node from the Digester stack.
                PropertyUtils.setProperty(getDigester().peek(),propertyName,doc);
            } else {
                PropertyUtils.setProperty(o,propertyName,bodyText);
            }
            super.end();
        }

        public void finish() throws Exception {
            super.finish();
            useBody = false;
            propertyName = null;
            bodyText = null;
        }

    }
    
    private class RequestObjectCreationFactory extends AbstractObjectCreationFactory {

        public Object createObject(Attributes attr) throws Exception {
            if (attr.getValue(CLASS_ATTR) != null) {
                return Class.forName(attr.getValue(CLASS_ATTR)).newInstance();
            } else {
                return RequestMapperType.createRequestMapper(attr.getValue(TYPE_ATTR));
            }
        }

    } //end inner class
    
    private class ResponseObjectCreationFactory extends AbstractObjectCreationFactory {
        public Object createObject(Attributes attr) throws Exception {
            if (attr.getValue(CLASS_ATTR) != null) {
                  return Class.forName(attr.getValue(CLASS_ATTR)).newInstance();
             } else {
                 return ResponseConvertorType.createConvertor(attr.getValue(TYPE_ATTR));
            }
        }
    }// end inner class
    
    private class ResultObjectCreationFactory extends AbstractObjectCreationFactory {
        public Object createObject(Attributes attr) throws Exception {
            if (attr.getValue(CLASS_ATTR) != null) {
                 return Class.forName(attr.getValue(TYPE_ATTR)).newInstance();
             } else {
                  return ResultBuilderType.createResultBuilder(attr.getValue(TYPE_ATTR));
            }
        }
    } // end inner class
    
}


/* 
$Log: ServiceStoreBuilder.java,v $
Revision 1.1  2003/11/18 11:48:15  nw
mavenized http2java

Revision 1.2  2003/11/11 14:43:33  nw
added unit tests.
basic working version

Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/