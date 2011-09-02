/*$Id: ParameterDescription.java,v 1.2 2011/09/02 21:55:52 pah Exp $
 * Created on 25-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.description;

import java.util.List;

import org.astrogrid.applications.description.base.ParameterTypes;

/** Description of a parameter to an application.
 * @see org.astrogrid.applications.description.ApplicationDefinition
 * @author Noel Winstanley nw@jb.man.ac.uk 25-May-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 11 Mar 2008
 */
public interface ParameterDescription {
    /** name of the parameter */
    public abstract String getId();
    /** name to use in UI */
    public abstract String getName();
   /** description to use in UI */
    public abstract String getDescription();
     /** UCD of parameter */
    public abstract String getUcd();
    /** units of parameter */
    public abstract String getUnit();
    /** type of parameter */
    public abstract  ParameterTypes getType();
    /** mime type of the parameter*/
    public String getMimeType();
    
//    /** subtype / contraint on possible value of parameter */
//    public abstract String getSubType();
//    /** data encodings accepted for this parameter */
//    public abstract String getAcceptEncodings();
    /** default value for this parameter */
    public abstract List<String> getDefaultValue();
    /** Allowed values for this parameter */
    public abstract org.astrogrid.applications.description.base.OptionList getOptionList();
    


}
/* 
$Log: ParameterDescription.java,v $
Revision 1.2  2011/09/02 21:55:52  pah
result of merging the 2931 branch

Revision 1.1.2.1  2009/07/16 19:45:21  pah
NEW - bug 2950: rework parameterAdapter
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2950

Revision 1.1  2009/02/26 12:25:48  pah
separate more out into cea-common for both client and server

Revision 1.6  2008/09/03 14:18:43  pah
result of merge of pah_cea_1611 branch

Revision 1.5.12.2  2008/04/23 14:14:30  pah
ASIGNED - bug 2749: make sure all CECs use the  ThreadPoolExecutionController
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2749

Revision 1.5.12.1  2008/03/19 23:10:53  pah
First stage of refactoring done - code compiles again - not all unit tests passed

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

Revision 1.5  2007/03/08 14:35:25  clq2
gtr_apps_2056

Revision 1.4.220.1  2007/03/01 17:34:56  gtr
I added getOptionList().

Revision 1.4  2004/08/28 07:17:34  pah
commandline parameter passing - unit tests ok

Revision 1.3  2004/07/26 00:57:46  nw
javadoc

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.3  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.2  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.7.8.1  2004/05/28 10:23:10  nw
checked in early, broken version - but it builds and tests (fail)
 
*/