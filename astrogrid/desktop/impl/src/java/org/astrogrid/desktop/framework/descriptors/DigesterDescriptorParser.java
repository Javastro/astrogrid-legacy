/*$Id: DigesterDescriptorParser.java,v 1.2 2005/09/02 14:03:34 nw Exp $
 * Created on 15-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.framework.descriptors;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.digester.Digester;
import org.apache.commons.digester.NodeCreateRule;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;

/** parses an xml document into a set of descriptor objects using commons digester.
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Mar-2005
 * 
 */
public class DigesterDescriptorParser implements DescriptorParser {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(DigesterDescriptorParser.class);

    private static final String PARAM = "module/component/method/parameter";
    private static final String RETURN = "module/component/method/return";
    private static final String METHOD = "module/component/method";
    private static final String COMPONENT = "module/component";
    private static final String MODULE = "module";
    /** Construct a new ModuleDescriptorBuilder
     * @throws ParserConfigurationException
     * 
     */
    public DigesterDescriptorParser() throws ParserConfigurationException {
        super();

            this.digester = new Digester(){{ // initializer.
                // set up.
                //URL url = this.getClass().getResource("module-descriptor.dtd");
                //register("-//Astrogrid//DTD Desktop Module 1.0//EN", url.toString());        
                //setValidating(true);
                //setSchemaLanguage("DTD");
                //setPublicId("-//Astrogrid//DTD Desktop Module 1.0//EN");
                
                // module.
                addObjectCreate(MODULE,ModuleDescriptor.class);
                addSetProperties(MODULE);
                
                
                
                // component
                addObjectCreate(COMPONENT,ComponentDescriptor.class);
                addSetProperties(COMPONENT);
                addSetNext(COMPONENT,"addComponent");
                
                // method
                addObjectCreate(METHOD,MethodDescriptor.class);
                addSetProperties(METHOD);
                addSetNext(METHOD,"addMethod");
                
                // return values.
                addObjectCreate(RETURN,ValueDescriptor.class);
                addSetProperties(RETURN);
                addSetNext(RETURN,"setReturnValue");
                
                // parameters.
                addObjectCreate(PARAM,ValueDescriptor.class);
                addSetProperties(PARAM);
                addSetNext(PARAM,"addParameter");            
                                        
                // generics.
                addBeanPropertySetter("*/description");

                addCallMethod("*/property","setProperty",2,new Class[]{String.class,String.class});
                addCallParam("*/property/key",0); // attribute called 'key'                
                addRule("*/property/value", new NodeCreateRule(){ // nasty hack - couldn't get it working any other way.
                    public void body(String s1,String s2,String s3) throws Exception{
                        super.body(s1,s2,s3);
                        Element e = (Element)getDigester().peek();
                        String s = XMLUtils.getInnerXMLString(e);
                       ((Object[])params.peek())[1] = s;
                    }
                });
            }          
            };

    }
    protected final Digester digester;  
    
    
    /** parse a descriptor into objects 
     * @throws SAXException
     * @throws IOException*/
    public ModuleDescriptor parse(InputStream is) throws IOException, SAXException {
        logger.info("Parsing module descriptor");
        ModuleDescriptor md =  (ModuleDescriptor)digester.parse(is);
        logger.info("Parsing completed");
        logger.debug(md);
        return md;
    }

}


/* 
$Log: DigesterDescriptorParser.java,v $
Revision 1.2  2005/09/02 14:03:34  nw
javadocs for impl

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.2  2005/04/13 12:59:13  nw
checkin from branch desktop-nww-998

Revision 1.1.2.1  2005/03/18 12:09:32  nw
got framework, builtin and system levels working.
 
*/