/*
   ContextFactory.java

   Date      Author      Changes
   $date$    M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.common.wrapper;

import org.astrogrid.common.ucd.UcdDictionary;
import org.astrogrid.common.units.UnitDictionary;

/**
 * A Wrapper's ContextFactory acts a bit like a global (tsk tsk) set of
 * variables for a particular application.  It returns instances of the
 * validating dictionaries to
 * be used, acts as a factory for parameterbundles, etc.
 * Highly dodgy in that it acts as a global set of variables for access by
 * the system, including possibly configuration information such as where to
 * locate temporary files, etc.
 *
 * @version %I%
 * @author M Hill
 */

public interface ContextFactory
{
   public ParameterBundle newParameterBundle(String id, String units);

   public UcdDictionary getUcdDictionary();

   public UnitDictionary getUnitDictionary();

   public LocalTermDictionary getLocalTermDictionary();

}


