package org.astrogrid.registry.messaging.processor;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlBlaster.client.key.PublishKey;
import org.xmlBlaster.client.qos.PublishQos;
import org.xmlBlaster.util.Global;
import org.xmlBlaster.util.MsgUnit;
import org.xmlBlaster.util.enum.PriorityEnum;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class XmlBlasterTestUtil {
  public static Element createXmlElement(String filename) throws Exception {
    DocumentBuilder builder = getDocumentBuilder();
    Document document = builder.parse(new File(filename));
    
    return document.getDocumentElement();
  }

  public static MsgUnit createMsgUnit(String clientTagXml, String contentFile) throws Exception {
    PublishKey publishKey = createPublishKey(clientTagXml);
    String content = readFile(contentFile);
    PublishQos publishQos = createPublishQos();
    
    MsgUnit result = new MsgUnit(publishKey, content, publishQos);
    
    return result;
  }
  
  public static String readFile(String filename) throws Exception {
    StringBuffer buffer = new StringBuffer();
    
    BufferedReader in = new BufferedReader(new FileReader(filename));
    while(in.ready()) {
      if(buffer.length() > 0) {
        buffer.append("\n");
      }
      
      buffer.append(in.readLine());
    }
    
    return buffer.toString();
  }

  private static DocumentBuilder getDocumentBuilder() throws Exception {
    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    builderFactory.setNamespaceAware(true);
    
    return builderFactory.newDocumentBuilder();
  }

  private static PublishKey createPublishKey(String clientTagXml) throws Exception {
    PublishKey result = new PublishKey(Global.instance());
    result.setClientTags(clientTagXml);
    
    return result;
  }
  
  private static PublishQos createPublishQos() throws Exception {
    PublishQos result = new PublishQos(Global.instance());
    result.setPriority(PriorityEnum.NORM_PRIORITY);
    result.setLifeTime(-1L);
    result.setPersistent(true);
    result.setForceUpdate(true);
    result.setReadonly(true);
    
    return result;
  }
}
