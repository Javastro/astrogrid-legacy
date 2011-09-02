/*
 * $Id: MergingParameterAdapter.java,v 1.5 2011/09/02 21:55:52 pah Exp $
 * 
 * Created on 23-Sep-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.commandline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.impl.CommandLineParameterDefinition;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.parameter.MutableInternalValue;
import org.astrogrid.applications.parameter.ParameterDirection;

/**
 * Special parameter adapter that will gather all the inputs into a single list.
 * The container that the results are collected in is passed in as a @link MergingParameterAdaptor.Concentrator. The same Concentrator should be shared by a set of MergingParameterAdaptors that wish to merge their inputs. 
 * 
 *
 * @author Paul Harrison (pah@jb.man.ac.uk) 23-Sep-2004
 * @version $Name:  $
 * @since iteration6
 */
public class MergingParameterAdapter extends DefaultCommandLineParameterAdapter {

   /**
    * @param appInterface
    * @param pval
    * @param desc
    * @param indirect
    * @param env
    * @param concentrator
    *           A single concentrator object should be shared between instances
    *           of the
    * @link MergingParameterAdapter that wish to merge their output.
    */
   public MergingParameterAdapter(ApplicationInterface appInterface,
         ParameterValue pval, CommandLineParameterDefinition desc, ParameterDirection dir,
         ApplicationEnvironment env,
         Concentrator concentrator) {
      super(appInterface, pval, desc, dir, env);
      this.concentrator = concentrator;
   }

   /**
    * The concentrator shared object.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 16 Sep 2008
 * @version $Name:  $
 *
 */
public static class Concentrator {

      private List<MutableInternalValue> gatheredValues = new ArrayList<MutableInternalValue>();

      private boolean haveListedSwitches = false;

      private final String separator;

      /**
       *  
       */
      public Concentrator(String theSeparator) {

         separator = theSeparator;
      }

      public boolean haveListedSwitches() {
         return haveListedSwitches;
      }

      public String getSeparator() {
         return separator;
      }

      public void setHaveListedSwitches(boolean haveListedSwitches) {
         this.haveListedSwitches = haveListedSwitches;
      }

      /**
       * @return
       */
      public Iterator<MutableInternalValue> getIterator() {
         return gatheredValues.iterator();
      }

      /**
       * @param o
       */
      public void add(MutableInternalValue o) {
         gatheredValues.add(o);
      }
   }

   /**
    * A single concentrator object should be shared between instances of the
    * 
    * @link MergingParameterAdapter that wish to merge their output.
    */
   private Concentrator concentrator;

   /*
    * (non-Javadoc)
    * 
    * @see org.astrogrid.applications.parameter.CommandLineParameterAdapter#addSwitches()
    */
   @Override
public List addSwitches() throws CeaException {
      if (concentrator.haveListedSwitches()) {
         return null;
      }
      else {
         //append all the gathered strings
         StringBuffer output = new StringBuffer();
         for (Iterator<MutableInternalValue> iter = concentrator.getIterator(); iter.hasNext();) {
            String val = iter.next().asString();
            output.append(val);
            if (iter.hasNext()) {
               output.append(concentrator.getSeparator());

            }
         }
         concentrator.setHaveListedSwitches(true);
         return cmdParamDesc.addCmdlineAdornment(output.toString());
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.astrogrid.applications.parameter.ParameterAdapter#process()
    */
   @Override
public MutableInternalValue getInternalValue() throws CeaException {
       MutableInternalValue o = super.getInternalValue();
      concentrator.add(o);
      return o;
   }
}