package org.astrogrid.registry.messaging.log;

import org.xmlBlaster.client.I_Callback;
import org.xmlBlaster.client.qos.ConnectQos;
import org.xmlBlaster.client.qos.ConnectReturnQos;
import org.xmlBlaster.client.qos.DisconnectQos;
import org.xmlBlaster.client.qos.EraseReturnQos;
import org.xmlBlaster.client.qos.PublishReturnQos;
import org.xmlBlaster.util.MsgUnit;
import org.xmlBlaster.util.XmlBlasterException;

/**
 * @author peter.shillan <mailto:gps@roe.ac.uk />
 */
public interface XmlBlaster {
  // Utility methods.
  public abstract ConnectReturnQos connect(ConnectQos connectQos, I_Callback callback) throws XmlBlasterException;
  public abstract boolean disconnect(DisconnectQos disconnectQos) throws XmlBlasterException;
  public abstract PublishReturnQos publishString(String content) throws XmlBlasterException;
  public abstract PublishReturnQos publishString(String content, String clientTags) throws XmlBlasterException;
  public abstract MsgUnit[] getMessages(String key) throws XmlBlasterException;
  public abstract EraseReturnQos[] eraseMessages(String key) throws XmlBlasterException;
}