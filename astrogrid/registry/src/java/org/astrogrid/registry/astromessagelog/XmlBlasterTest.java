/*
 * Created on 24-Apr-2003
 */
package org.astrogrid.registry.astromessagelog;

import junit.framework.TestCase;

import org.xmlBlaster.client.qos.ConnectReturnQos;
import org.xmlBlaster.util.Global;
import org.xmlBlaster.util.MsgUnit;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class XmlBlasterTest extends TestCase {
  // Test constants.
  private static final String KEY = "//key[name='fred']";
  private static final String CONTENT = "this is a test";

  // XMLBlaster wrapper.
  private XmlBlasterUtil blaster;
  
  public XmlBlasterTest(String name) {
    super(name);
  }
  
  public static void main(String[] args) {
    Global global = new Global(args);
    junit.textui.TestRunner.run(XmlBlasterTest.class);
  }
  
  public void testConnectDisconnect() throws Exception {
    blaster.connect(null, null);
    blaster.disconnect(null);
  }

  public void testPublishString() throws Exception {
    ConnectReturnQos connectReturnQos = blaster.connect(null, null);
    
    // Message content.
    blaster.publishString(CONTENT);
    
    blaster.disconnect(null);
  }

  public void testGetMsgUnits() throws Exception {
    ConnectReturnQos connectReturnQos = blaster.connect(null, null);
    blaster.publishString(CONTENT);

    MsgUnit[] msgUnits = blaster.getMessages(KEY);
    assertEquals(1, msgUnits.length);

    blaster.disconnect(null);
  }

  protected void setUp() throws Exception {
    blaster = new XmlBlasterUtil();
  }
  
  protected void tearDown() throws Exception {
    blaster.connect(null, null);
    blaster.eraseMessages(KEY);
    blaster.disconnect(null);
    
    blaster = null;
  }
}
