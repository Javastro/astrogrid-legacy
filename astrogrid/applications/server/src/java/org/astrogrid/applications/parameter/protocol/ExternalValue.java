/*$Id: ExternalValue.java,v 1.2 2004/08/24 11:45:00 pah Exp $
 * Created on 16-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.parameter.protocol;

import java.io.InputStream;
import java.io.OutputStream;

/** Interface for working with a value that is 'external' - ie probably not in this JVM. May be in local storage, may be remote.
 * <p>
 * Because of this vagueness, the interface provides the bare minimum for working with the external value.
 * @TODO rename the methods in this interface so that the imply opening a stream rather than reading and writing...
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public interface ExternalValue {
    /** access a stream to read the contents of the external value from 
     * @return an input stream containing the content of the external value
     * @throws InaccessibleExternalValueException*/
    InputStream read() throws InaccessibleExternalValueException;
    /** access a stream to write the contents of the external value to 
     * @return an output stream.
     * @throws InaccessibleExternalValueException*/
    OutputStream write() throws InaccessibleExternalValueException;
}


/* 
$Log: ExternalValue.java,v $
Revision 1.2  2004/08/24 11:45:00  pah
added todo comment

Revision 1.1  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/