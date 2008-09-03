/*
 * $Id: CommandLineParameterAdapter.java,v 1.2 2008/09/03 14:18:53 pah Exp $
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

import java.util.List;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.parameter.ParameterAdapter;

/**
 * Extra Functions for adapters needed in the commandline case.
 * @author Paul Harrison (pah@jb.man.ac.uk) 23-Sep-2004
 * @version $Name:  $
 * @since iteration6
 */
public interface CommandLineParameterAdapter extends ParameterAdapter {

    /**
     * return the full command line representation of this parameter. 
     * @return This is expected to be a list of strings that make up the separate parts of the command line representation of the parameter. Each string will be considered a separated argument by the operating system command line parser. Can return null to indicate that nothing should be added to the command line.
     * @throws CeaException
     */
    List<String> addSwitches() throws CeaException;
}
