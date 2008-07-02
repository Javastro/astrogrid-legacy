package org.astrogrid.security;

import java.io.ByteArrayOutputStream;
import org.apache.axis.AxisFault;
import org.apache.axis.MessageContext;
import org.apache.axis.SOAPPart;
import org.apache.axis.handlers.BasicHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xml.security.c14n.Canonicalizer;
import org.apache.xml.security.Init;
import org.astrogrid.security.wsse.WsseSignature;
import org.w3c.dom.Document;

/**
 * An Axis message-handler to sign the SOAP body of a message.
 * It is adapted from the WSDoAllSender class in WSS4J, written by
 * Werner Dittmann.
 *
 * @author Guy Rixon
 */ 
public class AxisClientCredentialHandler extends BasicHandler {
  
  private static Log log = LogFactory.getLog(AxisClientCredentialHandler.class);
  
  public void invoke(MessageContext msgContext) throws AxisFault {
    
    // Sign only outgoing messages.
    if (msgContext.getPastPivot()) {
      return;
    }
    
    // Turn all exceptions into SOAP faults.
    try {
      
      // Do nothing unless authentication is turned on.
      Boolean authenticate = (Boolean)msgContext.getProperty("org.astrogrid.security.authenticate");
      if (authenticate == null || authenticate.booleanValue() == false) {
        return;
      }
      
      // When Axis finally serializes the message in the connection to the
      // service it likes to play around with the XML; this invalidates the
      // signature. Tell it not to meddle.
      msgContext.setProperty("disablePrettyXML", Boolean.TRUE);
      
      // Retrieve the user credentials set up for this handler.
      SecurityGuard guard = (SecurityGuard)msgContext.getProperty("org.astrogrid.security.guard");
      if (guard == null) {
        throw new Exception("No security information was given.");
      }
    
      // Get the SOAP envelop as a DOM.
      Document envelope =
          msgContext.getCurrentMessage().getSOAPEnvelope().getAsDocument();
      if (envelope == null) {
        throw new Exception("SOAP Envelope is null");
      }
      
      // Sign the message using WSS4J. By default, the WSSignEnvelope signs the
      // the SOAP body as a whole, which is correct for this use case.
      Init.init();
      WsseSignature signature = new WsseSignature(envelope, null);
      signature.sign(guard);
      
      // Copy the signed message into the Axis engine. The canonicalizer
      // doesn't add the XML prologue-line. That would be OK, but Axis then 
      // muddles the length of the message in HTTP transmissions. Therefore,
      // add the XML prologue explicitly here.
      Canonicalizer canonicalizer 
          = Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      os.write("<?xml version='1.0'?>".getBytes());
      os.write(canonicalizer.canonicalizeSubtree(envelope));
      SOAPPart sp =
          (org.apache.axis.SOAPPart)(msgContext.getCurrentMessage().getSOAPPart());
      sp.setCurrentMessage(os.toByteArray(), SOAPPart.FORM_BYTES);
    }
    catch (Throwable t) {
      t.printStackTrace();
      log.error(t);
      throw new AxisFault("Failed to sign a request message", t);
    }
  }
}
