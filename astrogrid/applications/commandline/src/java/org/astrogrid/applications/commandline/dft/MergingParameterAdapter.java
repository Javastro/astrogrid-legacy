/*
 * $Id: MergingParameterAdapter.java,v 1.1 2004/09/23 22:44:23 pah Exp $
 * 
 * Created on 23-Sep-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.commandline.dft;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.commandline.CommandLineApplicationEnvironment;
import org.astrogrid.applications.commandline.CommandLineParameterDescription;
import org.astrogrid.applications.commandline.DefaultCommandLineParameterAdapter;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.parameter.protocol.ExternalValue;

/**
 * Special parameter adapter that will gather all the inputs into a single list.
 * FIXME this is still a hack specialized towards Dft.
 * 
 * @TODO this should be generalised so that the container that it stores the
 *       gathered values within can be passed in.
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
     */
    public MergingParameterAdapter(ApplicationInterface appInterface,
            ParameterValue pval, CommandLineParameterDescription desc,
            ExternalValue indirect, CommandLineApplicationEnvironment env) {
        super(appInterface, pval, desc, indirect, env);
    }

    static private List gatheredValues = new ArrayList();

    static private boolean haveListedSwitches = false;

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.applications.parameter.CommandLineParameterAdapter#addSwitches()
     */
    public List addSwitches() throws CeaException {
        if (haveListedSwitches) {
            return null;
        }
        else {
            //append all the gathered strings
            StringBuffer output = new StringBuffer();
            for (Iterator iter = gatheredValues.iterator(); iter.hasNext();) {
                String val = (String)iter.next();
                output.append(val);
                if (iter.hasNext()) {
                    output.append(',');

                }
            }
            haveListedSwitches = true;
            return desc.addCmdlineAdornment(output.toString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.applications.parameter.ParameterAdapter#process()
     */
    public Object process() throws CeaException {
        Object o = super.process();
        gatheredValues.add(o);
        return o;
    }
}