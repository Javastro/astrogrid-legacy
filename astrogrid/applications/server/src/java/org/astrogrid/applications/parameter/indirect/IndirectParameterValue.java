/*$Id: IndirectParameterValue.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 16-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.parameter.indirect;

import java.io.InputStream;
import java.io.OutputStream;

/** Interface for working with a parameter value that is 'indirect' - and so may be elsewhere
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public interface IndirectParameterValue {
    /** access a stream to read the contents of the indirect parameter from */
    InputStream read() throws InaccessibleIndirectParameterException;
    /** access a stream to write the contents of the indirect parameter to */
    OutputStream write() throws InaccessibleIndirectParameterException;
}


/* 
$Log: IndirectParameterValue.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/