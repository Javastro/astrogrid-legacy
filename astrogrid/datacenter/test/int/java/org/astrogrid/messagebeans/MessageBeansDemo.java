/*$Id: MessageBeansDemo.java,v 1.1 2003/09/11 09:30:10 nw Exp $
 * Created on 11-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.messagebeans;

import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.axis.utils.XMLUtils;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
/** Demonstration of using java beans to represent inter-workgroup communication,
 * where conversion to-fropm XML is done via castor, with 
 * no preprocessing. (all based on java beans naming conventions)
 * @author Noel Winstanley nw@jb.man.ac.uk 11-Sep-2003
 *
 */
public class MessageBeansDemo extends TestCase {

    /**
     * Constructor for MessageBeansDemo.
     * @param arg0
     */
    public MessageBeansDemo(String arg0) {
        super(arg0);
    }

    public static Test suite() {
          // Reflection is used here to add all the testXXX() methods to the suite.
          return new TestSuite(MessageBeansDemo.class);
      }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(MessageBeansDemo.class);
    }

    /** set up a message bean */
    protected void setUp() {
        b = new CommunityBean();
        b.setUserId("noel");
        b.setFlag(true);
    }
    private CommunityBean b;

    /** round trip to a org.w3c.document and back */
    public void testMarshallToDoc()throws ParserConfigurationException, MarshalException, ValidationException {
        Document doc = XMLUtils.newDocument();
        Marshaller.marshal(b,doc);
        //document should now have content in it.  

       CommunityBean cb = (CommunityBean)Unmarshaller.unmarshal(CommunityBean.class,doc);
       assertNotNull(cb);
       assertEquals(b,cb);
       
       //can also get it back from the element within the document
       Element el = (Element)doc.getElementsByTagName("community-bean").item(0);
       assertNotNull(el);
       CommunityBean cb1 = (CommunityBean)Unmarshaller.unmarshal(CommunityBean.class,el);
       assertNotNull(cb1);
       assertEquals(b,cb1); 
              
    }
    /** round trip to a string and back */
    public void testMarshalToString() throws ParserConfigurationException, MarshalException, ValidationException {
       StringWriter buff = new StringWriter();
       Marshaller.marshal(b,buff);
       String xml = buff.toString();
       assertNotNull(xml);
       System.out.println(xml);
       StringReader in = new StringReader(xml);
       
       CommunityBean cb = (CommunityBean)Unmarshaller.unmarshal(CommunityBean.class,in);
       assertNotNull(cb);
       in.close();
       assertEquals(b,cb);
    }
    /** round-trip a complex type, mostly empty */
    public void testMarshallComplexToString() throws MarshalException, ValidationException {
        InterestingBean ib = new InterestingBean();
        ib.setADate(new Date());
        roundTrip(ib);
        
    }
    /** round-trip a more complex type with nested data */
    public void testMarshallPopulatedComplexToString() throws ParserConfigurationException, MarshalException, ValidationException, MalformedURLException {
        InterestingBean ib = new InterestingBean();
        ib.setADate(new Date());
        ib.setCommunityBean(b);
        ib.setList(0,new Integer(1));
        ib.setList(1,b); // add another community bean.
        ib.setList(2,"hi there");
        ib.setSomeInts(new int[]{1,2,3,4});
        
        roundTrip(ib);
    }

    private void roundTrip(InterestingBean ib)
        throws MarshalException, ValidationException {
        StringWriter buff = new StringWriter();
        Marshaller.marshal(ib,buff);
        String xml = buff.toString();
        assertNotNull(xml);
        System.out.println(xml);
        
        StringReader in = new StringReader(xml);
        InterestingBean ib1 = (InterestingBean)Unmarshaller.unmarshal(InterestingBean.class,in);
        assertNotNull(ib1);
        in.close();
        assertEquals(ib,ib1); 
    }

}


/* 
$Log: MessageBeansDemo.java,v $
Revision 1.1  2003/09/11 09:30:10  nw
demonstrator for using castor to map javabeans to xml
 
*/