package org.astrogrid.security;

import java.util.Iterator;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPMessage;

/**
 * Trivial implementation of javax.xml.rpc.handler.MessageContext,
 * and javax.xml.rpc.handler.soap.SOAPMessageContext.  This class
 * avoids the need to use Axis' MessageContext directly when
 * testing.
 *
 * The accessors for the SOAP message work. The other methods do
 * nothing useful. In particular, they do not store and retrieve
 * properties of the context.
 *
 * @author Guy Rixon
 */
public class DummyMessageContext implements SOAPMessageContext {

  /**
   * The SOAP message in this context.
   */
  private SOAPMessage message;


  /**
   * Gets the message from the SOAP context.
   */
  public SOAPMessage getMessage () {
    return this.message;
  }


  /**
   * Sets the message in this context.
   */
  public void setMessage (SOAPMessage m) {
    this.message = m;
  }


  /**
   * Gets the SOAP actor roles associated with an execution of the
   * HandlerChain and its contained Handler instances. This
   * implementation always returns an empty array of roles.
   */
  public String[] getRoles () {
    return new String[] {};
  }


  /**
   * Returns true if the MessageContext contains a property with
   * the specified name. This implementation always returns false.
   */
  public boolean containsProperty (java.lang.String name) {
    return false;
  }


  /**
   * Gets the value of a specific property from the MessageContext.
   * This implementation always returns null.
   */
  public Object getProperty (String name) {
    return null;
  }


  /**
   * Returns an Iterator view of the names of the properties
   * in this MessageContext. This version always returns null;
   */
  public Iterator getPropertyNames () {
    return null;
  }


  /**
   * Removes a property (name-value pair) from the MessageContext.
   * This implementation does nothing.
   */
  public void removeProperty(String name) {}


  /**
   * Sets the name and value of a property associated
   * with the MessageContext. This implementation does nothing.
   */
  public void setProperty (String name, Object value) {}

}
