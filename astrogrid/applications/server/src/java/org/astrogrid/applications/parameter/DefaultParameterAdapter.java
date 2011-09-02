/*$Id: DefaultParameterAdapter.java,v 1.18 2011/09/02 21:55:52 pah Exp $
 * Created on 04-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.applications.parameter;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.security.SecurityGuard;

/**
 * The default implementation of
 * {@link org.astrogrid.applications.parameter.ParameterAdapter}
 * <p/>
 * Handles both direct and indirect parameters, returing them as an in-memory
 * string.
 * 
 * @see org.astrogrid.applications.parameter.protocol.ProtocolLibrary#getExternalValue(ParameterValue,
 *      SecurityGuard)
 * @author Noel Winstanley (nw@jb.man.ac.uk)
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @todo should really do different things according to what the type of the
 *       parameter is.
 * @FIXME - binary encode in some fashion
 */
public class DefaultParameterAdapter extends AbstractParameterAdapter {
    /**
     * Constructor.
     * 
     * @param val
     * @param description
     * @param dir 
     * @param externalVal
     */
    public DefaultParameterAdapter(ParameterValue val,
            ParameterDescription description, ParameterDirection dir, ApplicationEnvironment env ){
        super(val, description, dir, env);
    }

    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(DefaultParameterAdapter.class);

    /**
     * retrieves the value for this parameter if the parameter is direct, just
     * return the value of the parameter value itself, if indirect, retrieve the
     * value from the {@link #externalVal}
     * 
     * @return always returns the string value of this parameter
     * */
    @Override
    public MutableInternalValue getInternalValue() throws CeaException {
        if (internalVal == null) {

            if (!val.isIndirect()) {
                internalVal = new DefaultInternalValue(val.getValue());

            } else {
                try {

                    FileBasedInternalValue ival = new FileBasedInternalValue(
                            env.getTempFile(val.getId()));
                    if(direction.equals(ParameterDirection.INPUT)){ // only read the value if it is an input
                    ExternalValue externalVal = getProtocolLib()
                            .getExternalValue(val, env.getSecGuard());
                    ival.setValue(externalVal.read());
                    }
                    internalVal = ival;
                } catch (Exception e) {
                    throw new CeaException("Could not process parameter "
                            + val.getId(), e);
                }
            }
        }
        return internalVal;
    }

    protected InternalValue createInternalValue() {
        return new DefaultInternalValue();
    }

    /**
     * Writes the value of an output parameter back to the parameter storage.
     * That storage may be an internal buffer (a "direct" parameter) or an
     * external location (an "indirect" parameter).  In a direct parameter, the value is forced into a
     * String object and will be encoded in the process.
     */
    @Override
    public void writeBack() throws CeaException {
        if(direction.equals(ParameterDirection.OUTPUT)){
        if (val.isIndirect()) {
            ExternalValue evalue = getProtocolLib().getExternalValue(val,
                    env.getSecGuard());
            OutputStream os = evalue.write();
            try {
                internalVal.writeToStream(os);
            } catch (IOException e) {
                throw new ParameterWriteBackException(
                        "cannot write back parameter =" + val.getId(), e);
            } finally {
                try {
                    os.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } else {
            val.setValue(internalVal.asString());
            val.setEncoding(internalVal.getStringEncoding());
        }
        }
        else {
            throw new IllegalParameterUse("cannot write to input parameter " + val.getId());
        }
    }

}

/*
 * $Log: DefaultParameterAdapter.java,v $
 * Revision 1.18  2011/09/02 21:55:52  pah
 * result of merging the 2931 branch
 *
 * Revision 1.17.6.2  2009/07/16 19:48:05  pah
 * ASSIGNED - bug 2950: rework parameterAdapter
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2950
 *
 * Revision 1.17.6.1  2009/07/15 09:49:36  pah
 * redesign of parameterAdapters
 * Revision 1.17 2008/09/13 09:51:05 pah
 * code cleanup
 * 
 * Revision 1.16 2008/09/04 19:10:53 pah ASSIGNED - bug 2825: support VOSpace
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2825 Added the basic
 * implementation to support VOSpace - however essentially untested on real
 * deployement
 * 
 * Revision 1.15 2008/09/03 14:18:57 pah result of merge of pah_cea_1611 branch
 * 
 * Revision 1.14.2.2 2008/06/10 20:01:38 pah moved ParameterValue and friends to
 * CEATypes.xsd
 * 
 * Revision 1.14.2.1 2008/04/17 16:08:33 pah removed all castor marshalling -
 * even in the web service layer - unit tests passing
 * 
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611 ASSIGNED - bug 2708:
 * Use Spring as the container
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708 ASSIGNED - bug 2739:
 * remove dependence on castor/workflow objects
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739
 * 
 * Revision 1.14 2008/02/12 12:10:56 pah build with 1.0 registry and filemanager
 * clients
 * 
 * Revision 1.13 2006/03/17 17:50:58 clq2 gtr_1489_cea correted version
 * 
 * Revision 1.11 2006/03/07 21:45:26 clq2 gtr_1489_cea
 * 
 * Revision 1.10.118.2 2006/02/07 18:45:06 gtr This class now support parameter
 * values written back via InputStreams. This allows some major optimizations.
 * 
 * Revision 1.10.118.1 2006/02/07 16:34:22 gtr Binary output parameters are now
 * delivered to the ParameterAdapter as byte[] and treated accordingly.
 * 
 * Revision 1.10 2004/11/27 13:20:02 pah result of merge of pah_cea_bz561 branch
 * 
 * Revision 1.9.10.1 2004/10/27 16:04:05 pah pulled up an
 * AbstractParameterAdapter
 * 
 * Revision 1.9 2004/09/22 10:52:51 pah getting rid of some unused imports
 * 
 * Revision 1.8 2004/09/17 11:39:07 nw made sure streams are closed
 * 
 * Revision 1.7 2004/08/28 07:17:34 pah commandline parameter passing - unit
 * tests ok
 * 
 * Revision 1.6 2004/07/30 14:54:47 jdt merges in from case3 branch
 * 
 * Revision 1.5.2.1 2004/07/30 13:11:59 jdt typo
 * 
 * Revision 1.5 2004/07/26 12:07:38 nw renamed indirect package to protocol,
 * renamed classes and methods within protocol package javadocs
 * 
 * Revision 1.4 2004/07/22 16:33:48 nw reads in values fully now.
 * 
 * Revision 1.3 2004/07/20 02:03:23 nw doc
 * 
 * Revision 1.2 2004/07/01 11:16:22 nw merged in branch
 * nww-itn06-componentization
 * 
 * Revision 1.1.2.2 2004/07/01 01:42:46 nw final version, before merge
 * 
 * Revision 1.1.2.1 2004/06/17 09:21:23 nw finished all major functionality
 * additions to core
 * 
 * Revision 1.1.2.1 2004/06/14 08:56:58 nw factored applications into
 * sub-projects, got packaging of wars to work again
 */