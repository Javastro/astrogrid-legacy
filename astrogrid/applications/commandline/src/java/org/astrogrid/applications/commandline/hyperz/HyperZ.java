/*
 * $Id: HyperZ.java,v 1.6 2004/12/18 15:43:57 jdt Exp $
 * 
 * Created on 16-Jan-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.commandline.hyperz;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.commandline.CommandLineApplication;
import org.astrogrid.applications.commandline.CommandLineApplicationEnvironment;
import org.astrogrid.applications.commandline.CommandLineParameterDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.workflow.beans.v1.Tool;

import cds.savot.model.SavotVOTable;

/**
 * Specialization for HyperZ. This needs to take a VOTable and convert it to suitable form for native input into hyperZ, and then create a 
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 * @TODO there are dependencies on the AVO DEMO setup file paths that need to be removed
 */
public class HyperZ extends CommandLineApplication {
   
   /** Construct a new HyperZ
     * @param id
     * @param jobStepId
     * @param user
     * @param description
     */
    public HyperZ(String id, String jobStepId, Tool tool, ApplicationInterface description, CommandLineApplicationEnvironment env,ProtocolLibrary lib) {
        super(jobStepId, tool,description,env,lib);
    }

    private SavotVOTable inputVOTable;

   

 
/*
   protected void preWritebackHook() {
      // TODO convert the output file 
      HyperZVOTableWriter wrt = new HyperZVOTableWriter(inputVOTable, (FileReferenceCommandLineParameterAdapter)findParameterAdapter("output_catalog"), applicationEnvironment);
      wrt.write();
      
   }
   */

   /* (non-Javadoc)
    * @see org.astrogrid.applications.commandline.CmdLineApplication#postParamSetupHook()
    */
    /*
   protected void postParamSetupHook() {
      // create read the input file from the VOTable
      HyperZVOTableReader conv = new HyperZVOTableReader((FileReferenceCommandLineParameterAdapter) findParameterAdapter("input_catalog"),applicationEnvironment);
      inputVOTable = conv.read();
   }*/
   
   

   protected final VOTableSourceIndirector votableSource = new VOTableSourceIndirector(); // necessary, as we may see output param before input param!        
   
    /** specialized parameter adapter factory, that will build custom parameter adapters for the input and output files - 
     * more structured than the previous technique of hacking with pre and post- hooks.     
     * @author Noel Winstanley nw@jb.man.ac.uk 07-Jun-2004
     * @author Paul Harrison (pah@jb.man.ac.uk)
     *
     */
  
        protected ParameterAdapter instantiateAdapter( ParameterValue pval, ParameterDescription desr, ExternalValue indirectVal) {
            if (pval.getName().equals("input_catalog")) {
                String bands = findInputParameter("BAND_ORDER").getValue(); //TODO this is a bit hacky - will not allow indirect parameter setting - but need a quick fix...
                HyperZVOTableReader reader = new HyperZVOTableReader(getApplicationInterface(),pval, (CommandLineParameterDescription) desr,applicationEnvironment,indirectVal, bands);
                votableSource.setSource(reader);
                return reader;
            } else if (pval.getName().equals("output_catalog")) {
                return new HyperZVOTableWriter(getApplicationInterface(), pval,(CommandLineParameterDescription)desr,applicationEnvironment,indirectVal,votableSource);
            } else { // default behaviour                
                return super.instantiateAdapter(pval,desr,indirectVal);
            }            
          }        


    /* inderector - allows late binding between reader and writer */
    static class VOTableSourceIndirector implements HyperZVOTableWriter.VOTableSource {
        public SavotVOTable getVOTable() {
            return source.getVOTable();
        }
        private HyperZVOTableWriter.VOTableSource source;
        public void setSource(HyperZVOTableWriter.VOTableSource source) {
            this.source = source;
        }
    }



}
