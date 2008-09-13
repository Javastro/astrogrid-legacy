/*$Id: DefaultParameterAdapter.java,v 1.17 2008/09/13 09:51:05 pah Exp $
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.io.Piper;
import org.astrogrid.security.SecurityGuard;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;


/** The default implementation of {@link org.astrogrid.applications.parameter.ParameterAdapter}
 * <p/>
 * Handles both direct and indirect parameters, returing them as an in-memory string.
 * @see org.astrogrid.applications.parameter.protocol.ProtocolLibrary#getExternalValue(ParameterValue, SecurityGuard)
 * @author Noel Winstanley (nw@jb.man.ac.uk)
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @todo should really do different things according to what the type of the parameter is.
 * @fixme check whether it is really appropriate use Readers and Writers to access the indirect parameters -no...
 */
public class DefaultParameterAdapter extends AbstractParameterAdapter {
    /**
     * Constructor.
    * @param val
    * @param description
    * @param externalVal
    */
   public DefaultParameterAdapter(ParameterValue val, ParameterDescription description, ExternalValue externalVal) {
      super(val, description, externalVal);
      
   }

   /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(DefaultParameterAdapter.class);

     /**
     * retreives the value for this paramter
     * if the parameter is direct, just return the value of the parameter value itself, 
     * if indirect, retreive the value from the {@link #externalVal}
     * @return always returns the string value of this parameter
     *  */
    @Override
    public Object process() throws CeaException {
        if (externalVal == null) {
            return val.getValue();
        } else {
            Reader r = null;
            StringWriter sw = null;
            try {
                sw = new StringWriter();
                r = new InputStreamReader(externalVal.read());                
                Piper.pipe(r, sw);

                return sw.toString();                
            }
            catch (IOException e) {
                throw new CeaException("Could not process parameter " + val.getId());
            } finally {
                if (r != null) {
                    try {
                        r.close();
                    } catch (IOException e) {
                        logger.warn("could not close reader",e);
                    }
                }
                if (sw != null) {
                    try {
                        sw.close();
                    } catch (IOException e) {
                        logger.warn("could not close writer",e);
                    }
                }
            }

        }
    }

  /** 
   * Writes the value of an output parameter back to the parameter storage.
   * That storage may be an internal buffer (a "direct" parameter) or 
   * an external location (an "indirect" parameter). 
   * Various forms of the value are supported: null object reference;
   * byte array; InputStream; any other object. Binary values, such
   * as the contents of FITS files, should be given as byte arrays or
   * streams. Other values can be given as any object for which the
   * toString() method produces a valid copy of the value. Note that
   * the bit pattern of binary values is only sure to be preserved
   * for an indirect parameter. In a direct parameter, the value is
   * forced into a String object and may be corrupted in the process.
   */
  @Override
public void writeBack(Object o) throws CeaException {
    if (this.externalVal == null) {
      this.writeBackToParameterValue(o);
    }
    else {
      this.writeBackToExternalValue(o);
    }
  }
    
  /**
   * Writes a parameter value to a local ParameterValue object.  The value
   * is always sent as a String, since ParameterValue requires this.
   * Binary values (e.g. FITS files) are expected to arrive as byte arrays;
   * these don't have an effective toString() method and are treated specially.
   *
   * @param o The object to be written out.
   */
  private void writeBackToParameterValue(Object o) throws CeaException {
    try {
      if (o == null) {
        this.val.setValue("<null>");
      }
      else if (o instanceof byte[]) {
        this.val.setValue(new String((byte[])o));
      }
      else if (o instanceof InputStream) {
        InputStream is = (InputStream)o;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        this.pipe(is, os);
        this.val.setValue(new String(os.toByteArray()));
      }
      else {
        this.val.setValue(o.toString());
      }
    }
    catch (Exception e) {
      throw new CeaException("Failed to write back a value to a direct parameter.", e);
    }
  }
   
  /**
   * Writes a parameter value to an ExternalValue object. 
   *
   * @param o The object to be written out.
   */
  private void writeBackToExternalValue(Object o) throws CeaException {
    try {
      if (o == null) {
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(externalVal.write()));
        pw.println("<null>");
        pw.close();
      }
      else if (o instanceof byte[]) {
        OutputStream os = this.externalVal.write();
        os.write((byte[])o);
        os.close();
      }
      else if (o instanceof InputStream) {
        InputStream is = (InputStream)o;
        OutputStream os = this.externalVal.write();
        this.pipe(is, os);
        os.close();
      }
      else {
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(externalVal.write()));
        pw.print(o);
        pw.close();
      }
    }
    catch (Exception e) {
      throw new CeaException("Failed to write back a value to an indirect parameter.", e);
    }
    
  }
  
  /**
   * Copies an input stream onto an output stream.
   */
  private void pipe(InputStream is, OutputStream os) throws IOException {
    byte[] block = new byte[65536];
    int read = is.read(block);
    while (read > -1) {
      os.write(block, 0, read);
      read = is.read(block);
    }
  }
}

/* 
$Log: DefaultParameterAdapter.java,v $
Revision 1.17  2008/09/13 09:51:05  pah
code cleanup

Revision 1.16  2008/09/04 19:10:53  pah
ASSIGNED - bug 2825: support VOSpace
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2825
Added the basic implementation to support VOSpace  - however essentially untested on real deployement

Revision 1.15  2008/09/03 14:18:57  pah
result of merge of pah_cea_1611 branch

Revision 1.14.2.2  2008/06/10 20:01:38  pah
moved ParameterValue and friends to CEATypes.xsd

Revision 1.14.2.1  2008/04/17 16:08:33  pah
removed all castor marshalling - even in the web service layer - unit tests passing

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
ASSIGNED - bug 2708: Use Spring as the container
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
ASSIGNED - bug 2739: remove dependence on castor/workflow objects
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739

Revision 1.14  2008/02/12 12:10:56  pah
build with 1.0 registry and filemanager clients

Revision 1.13  2006/03/17 17:50:58  clq2
gtr_1489_cea correted version

Revision 1.11  2006/03/07 21:45:26  clq2
gtr_1489_cea

Revision 1.10.118.2  2006/02/07 18:45:06  gtr
This class now support parameter values written back via InputStreams. This allows some major optimizations.

Revision 1.10.118.1  2006/02/07 16:34:22  gtr
Binary output parameters are now delivered to the ParameterAdapter as byte[] and treated accordingly.

Revision 1.10  2004/11/27 13:20:02  pah
result of merge of pah_cea_bz561 branch

Revision 1.9.10.1  2004/10/27 16:04:05  pah
pulled up an AbstractParameterAdapter

Revision 1.9  2004/09/22 10:52:51  pah
getting rid of some unused imports

Revision 1.8  2004/09/17 11:39:07  nw
made sure streams are closed

Revision 1.7  2004/08/28 07:17:34  pah
commandline parameter passing - unit tests ok

Revision 1.6  2004/07/30 14:54:47  jdt
merges in from case3 branch

Revision 1.5.2.1  2004/07/30 13:11:59  jdt
typo

Revision 1.5  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.4  2004/07/22 16:33:48  nw
reads in values fully now.

Revision 1.3  2004/07/20 02:03:23  nw
doc

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/