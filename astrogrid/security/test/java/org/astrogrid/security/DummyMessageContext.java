package org.astrogrid.security;

import java.util.Hashtable;
import java.util.Iterator;
import javax.xml.rpc.handler.MessageContext;
import javax.xml.rpc.handler.soap.SOAPMessageContext;
import javax.xml.soap.SOAPMessage;

/**
 * Implementation of javax.xml.rpc.handler.MessageContext,
 * and javax.xml.rpc.handler.soap.SOAPMessageContext.  This class
 * avoids the need to use Axis' MessageContext directly when
 * testing.
 *
 * All the methods work according to JAX-RPC semantics
 * except {@link getRoles}. The latter always returns an empty
 * array of role-names.
 *
 * @author Guy Rixon
 */
public class DummyMessageContext implements SOAPMessageContext {

  /**
   * The SOAP message in this context.
   */
  private SOAPMessage message;

  /**
   * Properties stored by other object.
   */
  private Hashtable properties;


  /**
   * Constructs a DummyMessageContext, initializing the list of properties.
   */
  public DummyMessageContext () {
    this.properties = new Hashtable();
  }


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
   * the specified name.
   */
  public boolean containsProperty (java.lang.String name) {
    return this.properties.containsKey(name);
  }


  /**
   * Gets the value of a specific property from the MessageContext.
   */
  public Object getProperty (String name) {
    return this.properties.get(name);
  }


  /**
   * Returns an Iterator view of the names of the properties
   * in this MessageContext. This version always returns null;
   */
  public Iterator getPropertyNames () {
    return this.properties.keySet().iterator();
  }


  /**
   * Removes a property (name-value pair) from the MessageContext.
   */
  public void removeProperty(String name) {
    this.properties.remove(name);
  }


  /**
   * Sets the name and value of a property associated
   * with the MessageContext.
   */
  public void setProperty (String name, Object value) {
    this.properties.put(name, value);
  }

}
