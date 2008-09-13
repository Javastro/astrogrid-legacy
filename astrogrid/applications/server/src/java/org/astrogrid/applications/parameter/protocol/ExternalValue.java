/*$Id: ExternalValue.java,v 1.5 2008/09/13 09:51:02 pah Exp $
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
 * @TODO add method to copyto local file - to take advantage of any any secialization that the protocol might have to do this...then the paramter adapters should be rewritten to use this. (also exportFrom & getURL)
 * @TODO would be nice to have a string returning the "location" value also
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 * @author Paul Harrison (pah@jb.man.ac.uk)
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
    
    /**
     * Copy the contents of the external value to a file. This method can take advantage of any specializations that the protocol might have.
    * @param f The file to which the contents of the externalValue should be copied.
    * @return file that it has copied to - this might not be the file requested for efficiencies' sake 
    * @throws InaccessibleExternalValueException
   
   File importTo(File f) throws InaccessibleExternalValueException;
    */
}


/* 
$Log: ExternalValue.java,v $
Revision 1.5  2008/09/13 09:51:02  pah
code cleanup

Revision 1.4  2004/11/27 13:20:02  pah
result of merge of pah_cea_bz561 branch

Revision 1.3.6.2  2004/11/25 15:36:20  pah
another idea in a comment

Revision 1.3.6.1  2004/11/04 16:56:13  pah
new comment

Revision 1.3  2004/09/30 15:12:25  pah
comment on future enhancement

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