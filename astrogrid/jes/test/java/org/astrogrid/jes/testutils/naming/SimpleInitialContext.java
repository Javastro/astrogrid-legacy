/* $Id: SimpleInitialContext.java,v 1.2 2003/11/10 18:51:31 jdt Exp $
 * Created on 30-Oct-2003 by John Taylor jdt@roe.ac.uk .
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file. 
 */
package org.astrogrid.jes.testutils.naming;

import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

/**
 * A very simple naming service.  The only methods that have been implemented
 * are the bind and lookup .  Others will chuck <code>UnsupportedOperationException</code>s.
 * Fill in the blanks if you want.
 * @author jdt
 */
public final class SimpleInitialContext implements Context {
  /**
   * Stores the names to their objects
   */
  private Map map = new Hashtable();
  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#close()
   */
  public void close()  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#getNameInNamespace()
   */
  public String getNameInNamespace()  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @param name see superclass
   */
  public void destroySubcontext(final String name)  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @param name name to unbind
   */
  public void unbind(final String name)  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @return see superclass
   */
  public Hashtable getEnvironment()  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @param name see superclass
   */
  public void destroySubcontext(final Name name)  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#unbind(javax.naming.Name)
   */
  public void unbind(final Name name)  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#lookup(java.lang.String)
   */
  public Object lookup(final String name) throws NamingException  {
    Object result = map.get(name);
    if (result==null) {
      throw new NamingException("Couldn't find " + name);
    }
    return result;
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#lookupLink(java.lang.String)
   */
  public Object lookupLink(final String name)  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#removeFromEnvironment(java.lang.String)
   */
  public Object removeFromEnvironment(final String propName)  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#bind(java.lang.String, java.lang.Object)
   */
  public void bind(final String name, final Object obj)  {
    map.put(name, obj);

  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#rebind(java.lang.String, java.lang.Object)
   */
  public void rebind(final String name, final Object obj)  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#lookup(javax.naming.Name)
   */
  public Object lookup(final Name name)  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#lookupLink(javax.naming.Name)
   */
  public Object lookupLink(final Name name)  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#bind(javax.naming.Name, java.lang.Object)
   */
  public void bind(final Name name, final Object obj)  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#rebind(javax.naming.Name, java.lang.Object)
   */
  public void rebind(final Name name, final Object obj)  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#rename(java.lang.String, java.lang.String)
   */
  public void rename(final String oldName, final String newName)  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#createSubcontext(java.lang.String)
   */
  public Context createSubcontext(final String name)  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#createSubcontext(javax.naming.Name)
   */
  public Context createSubcontext(final Name name)  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#rename(javax.naming.Name, javax.naming.Name)
   */
  public void rename(final Name oldName, final Name newName)  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#getNameParser(java.lang.String)
   */
  public NameParser getNameParser(final String name)  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#getNameParser(javax.naming.Name)
   */
  public NameParser getNameParser(final Name name)  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#list(java.lang.String)
   */
  public NamingEnumeration list(final String name)  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#listBindings(java.lang.String)
   */
  public NamingEnumeration listBindings(final String name)  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#list(javax.naming.Name)
   */
  public NamingEnumeration list(final Name name)  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#listBindings(javax.naming.Name)
   */
  public NamingEnumeration listBindings(final Name name)  {
    throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#addToEnvironment(java.lang.String, java.lang.Object)
   */
  public Object addToEnvironment(final String propName, final Object propVal)
     {
      throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#composeName(java.lang.String, java.lang.String)
   */
  public String composeName(final String name, final String prefix)
     {
      throw new UnsupportedOperationException();
  }

  /**
   * Nothing but chuck an  <code>UnsupportedOperationException</code>
   * @see javax.naming.Context#composeName(javax.naming.Name, javax.naming.Name)
   */
  public Name composeName(final Name name, final Name prefix)  {
    throw new UnsupportedOperationException();
  }

  /**
   * Usual singleton creation pattern
   * @return The one and only SimpleInitialContext
   */
  public static Context getInstance() {
    return simpleInitialContext;
  }
  /**
  * The one and only SimpleInitialContext
  */
  private static Context simpleInitialContext = new SimpleInitialContext();
  /**
    *  Keep the ctor away from other classes.
   */
  public SimpleInitialContext() {} ;

}

/*
*$Log: SimpleInitialContext.java,v $
*Revision 1.2  2003/11/10 18:51:31  jdt
*Minor bits and pieces to satisfy the coding standards
*
*Revision 1.1  2003/10/31 17:21:44  jdt
*simple naming service to allow testing of database bits
*
*/