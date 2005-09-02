package org.astrogrid.security;

import org.apache.axis.MessageContext;
import org.apache.ws.axis.security.WSDoAllSender;
import org.apache.ws.security.components.crypto.Crypto;


public class AxisClientCredentialHandler extends WSDoAllSender {

  protected Crypto loadSignatureCrypto(RequestData reqData) {
    MessageContext mc = MessageContext.getCurrentContext();
    return (Crypto)mc.getProperty("org.astrogrid.security.signature.crypto");
  }

}
