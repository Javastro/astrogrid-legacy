/*
 * Created on 25-Apr-2003
 */
package org.astrogrid.registry.astromessagelog;

import org.xmlBlaster.client.I_Callback;
import org.xmlBlaster.client.I_XmlBlasterAccess;
import org.xmlBlaster.client.key.EraseKey;
import org.xmlBlaster.client.key.GetKey;
import org.xmlBlaster.client.key.PublishKey;
import org.xmlBlaster.client.qos.ConnectQos;
import org.xmlBlaster.client.qos.ConnectReturnQos;
import org.xmlBlaster.client.qos.DisconnectQos;
import org.xmlBlaster.client.qos.EraseQos;
import org.xmlBlaster.client.qos.EraseReturnQos;
import org.xmlBlaster.client.qos.GetQos;
import org.xmlBlaster.client.qos.PublishQos;
import org.xmlBlaster.client.qos.PublishReturnQos;
import org.xmlBlaster.util.Global;
import org.xmlBlaster.util.MsgUnit;
import org.xmlBlaster.util.XmlBlasterException;
import org.xmlBlaster.util.enum.Constants;
import org.xmlBlaster.util.enum.PriorityEnum;
import org.xmlBlaster.util.key.QueryKeyData;
import org.xmlBlaster.util.qos.HistoryQos;
import org.xmlBlaster.util.qos.QueryQosData;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public class XmlBlasterUtil {

  // Common constants.
  protected static final String CLIENT_TAGS = "<name>fred</name>";

  // XMLBlaster objects.  
  private Global global;
  private I_XmlBlasterAccess xmlBlasterAccess;

  public XmlBlasterUtil() {
    global = Global.instance();
    xmlBlasterAccess = global.getXmlBlasterAccess();
  }
  
  public XmlBlasterUtil(String[] args) {
    try {
      global = new Global(args);
    }
    catch (Exception e) {
      // Ignore.
    }

    global = Global.instance();
  }
  
  // Utility methods.
  public ConnectReturnQos connect(ConnectQos connectQos, I_Callback callback) throws XmlBlasterException {
    return xmlBlasterAccess.connect(connectQos, callback);
  }
  
  public boolean disconnect(DisconnectQos disconnectQos) throws XmlBlasterException {
    return xmlBlasterAccess.disconnect(disconnectQos);
  }
  
  public PublishReturnQos publishString(String content) throws XmlBlasterException {
    return publishString(content, null);
  }
  
  public PublishReturnQos publishString(String content, String clientTags) throws XmlBlasterException {
    // Quality of service request.
    PublishQos publishQos = new PublishQos(global);
    publishQos.setPriority(PriorityEnum.NORM_PRIORITY);
    publishQos.setLifeTime(-1L);
    publishQos.setPersistent(true);
    publishQos.setForceUpdate(true);
    publishQos.setReadonly(true);
    
    // Client-specific key structure for message.
    PublishKey publishKey = new PublishKey(global);
    
    if(clientTags == null) {
      publishKey.setClientTags(XmlBlasterUtil.CLIENT_TAGS);
    }
    else { 
      publishKey.setClientTags(clientTags);
    }
    
    return publishString(publishKey, content, publishQos);
  }
  
  public MsgUnit[] getMessages(String key) throws XmlBlasterException {
    // Get message.
    GetKey getKey = new GetKey(global, key, Constants.XPATH);
    
    HistoryQos historyQos = new HistoryQos(global);
    historyQos.setNumEntries(1);
    
    QueryQosData queryQosData = new QueryQosData(global);
    queryQosData.setHistoryQos(historyQos);
    
    GetQos getQos = new GetQos(global, queryQosData);
    return getMsgUnits(getKey, getQos);
  }
  
  public EraseReturnQos[] eraseMessages(String key) throws XmlBlasterException {
    QueryKeyData queryKeyData = new QueryKeyData(global, key, Constants.XPATH);
    EraseKey eraseKey = new EraseKey(global, queryKeyData);
    EraseQos eraseQos = new EraseQos(global);
    
    return eraseMsg(eraseKey, eraseQos);
  }
  
  private PublishReturnQos publishString(PublishKey publishKey, String content, PublishQos publishQos) throws XmlBlasterException {
    MsgUnit msgUnit = new MsgUnit(publishKey, content, publishQos);
    return xmlBlasterAccess.publish(msgUnit);
  }

  private MsgUnit[] getMsgUnits(GetKey getKey, GetQos getQos) throws XmlBlasterException { 
    return xmlBlasterAccess.get(getKey, getQos);
  }

  private EraseReturnQos[] eraseMsg(EraseKey eraseKey, EraseQos eraseQos) throws XmlBlasterException {
    return xmlBlasterAccess.erase(eraseKey, eraseQos);
  }
}
