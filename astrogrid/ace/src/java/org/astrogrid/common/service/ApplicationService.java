/*
   ApplicationWrapper.java

   Date       Author      Changes
   1 Oct 2002 M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.common.service;

import org.w3c.dom.Element;
import java.io.IOException;

/**
 * ApplicationWrapper describes the methods required by wrapper classes
 * so that they can work in the Grid Service environment
 *
 * @version %I%
 * @author M Hill
 */
   
public interface ApplicationService
{
   /**
    * The basic http-like get-return method.  This blocks until the
    * application completes, and returns a DOM-element containing the
    * results
    */
   public Element runApplication(Element xmlConfigNode) throws IOException;
}

