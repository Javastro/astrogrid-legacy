/*$Id: ReferenceCommandLineParameterAdapter.java,v 1.3 2004/07/26 12:03:33 nw Exp $
 * Created on 18-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.commandline;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.ParameterDirection;
import org.astrogrid.applications.description.exception.ParameterNotInInterfaceException;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.ParameterAdapterException;
import org.astrogrid.applications.parameter.ParameterWriteBackException;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.io.Piper;

import java.io.*;

/** adapter for a commandline parameter that is added to the argument list by 'reference' - i.e. the filename containing this parameter is added, rather than the value itself.
 * this kind of parameter can be used for both input and output parameters.
 * @author Noel Winstanley nw@jb.man.ac.uk 18-Jun-2004
 *
 */
public class ReferenceCommandLineParameterAdapter implements ParameterAdapter {
    /** Construct a new ReferenceCommandLineParameterAdapter
     * @param arg0
     * @param arg1
     * @param arg2
     */
    public ReferenceCommandLineParameterAdapter(ApplicationInterface appInterface, ParameterValue pval,  CommandLineParameterDescription desc, ExternalValue indirect, CommandLineApplicationEnvironment env) {
        this.pval = pval;
        this.desc = desc;
        this.indirect =indirect;
        this.env = env;
        this.referenceFile = env.getTempFile();
        try {
            this.isOutputOnly = appInterface.getParameterDirection(pval.getName()).equals(ParameterDirection.OUTPUT);
        } catch (ParameterNotInInterfaceException e) {
            // very unlikely.
            this.isOutputOnly = false;
        }
      }
    protected boolean isOutputOnly;
    protected final ParameterValue pval;
    protected final CommandLineParameterDescription desc;
    protected final ExternalValue indirect;
    protected File referenceFile;
    protected final CommandLineApplicationEnvironment env;
    public File getReferenceFile() {
        return referenceFile;
    }
    /** copies the parameter value (either direct, or indirect) into a file in the commandline environment
     * @return the name of the file the value has been written to.
     * @see org.astrogrid.applications.parameter.ParameterAdapter#process()
     */
    public Object process() throws CeaException {
        // don't want to fetch output-only files.
        try {
        if (!isOutputOnly) {
            if (indirect == null) {            
                String value = pval.getValue();
                PrintWriter pw = new PrintWriter(new FileWriter(referenceFile));
                pw.println(value);
                pw.close();                        
            } else {
                InputStream is = indirect.read();
                OutputStream os = new FileOutputStream(referenceFile);
                Piper.bufferedPipe(is,os);
                is.close();
                os.close();
            }
        }
        return   desc.addCmdlineAdornment(referenceFile.getAbsolutePath());
        } catch (IOException e) {
            throw new ParameterAdapterException("Could not process parameter " + pval.getName(),e);
        }
    }

    /**ignore the value passed , copy contents of reference file back to the original parameter (direct or indirect)
     * @see org.astrogrid.applications.parameter.ParameterAdapter#writeBack(java.lang.Object)
     */
    public void writeBack(Object arg0) throws CeaException {

        try {
            if (indirect == null) {
                StringWriter sw = new StringWriter();
                Reader r = new FileReader(referenceFile);
                Piper.pipe(r,sw);
                r.close();
                sw.close();
                pval.setValue(sw.toString());
            } else {
                InputStream is = new FileInputStream(referenceFile);
                OutputStream os = indirect.write();
                Piper.pipe(is,os);
                is.close();
                os.close();
            }
        } catch (IOException e) {
             throw new ParameterWriteBackException("Could not write back parameter" + pval.getName(),e);
        }
    }
    /**
     * @see org.astrogrid.applications.parameter.ParameterAdapter#getWrappedParameter()
     */
    public ParameterValue getWrappedParameter() {
        return pval;
    }

}


/* 
$Log: ReferenceCommandLineParameterAdapter.java,v $
Revision 1.3  2004/07/26 12:03:33  nw
updated to match name changes in cea server library

Revision 1.2  2004/07/01 11:07:59  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/07/01 01:43:39  nw
final version, before merge
 
*/