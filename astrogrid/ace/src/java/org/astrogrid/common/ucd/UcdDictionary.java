/*
   UcdDictionary.java

   Date      Author      Changes
   1 Oct 02  M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.common.ucd;


/**
 * Implementations of UcdDictionary provide a way of validating strings that
 * are thought to be UCDs.  They might be used, for example, when parsing an
 * XML document of parameters for an application.
 *
 * @version %I%
 * @author M Hill
 */
   
public interface UcdDictionary
{
   /**
    * Returns true if the given string is a valid UCD
    */
   public boolean isUcdValid(String ucd);
   
   /**
    * An assertion based check to see if the given string is a valid UCD.
    * Throws IllegalUcdException.  Used when you want to propogate an error
    * up the calling stack, rather than explicitly check with isUcdValid()
    */
   public void assertUcdValid(String ucd) throws IllegalUcdException;
   
   /**
    * Returns the description of the UCD.  Implementations should assert the
    * UCD is valid.
    */
   public String getUcdDescription(String ucd) throws IllegalUcdException;
}

