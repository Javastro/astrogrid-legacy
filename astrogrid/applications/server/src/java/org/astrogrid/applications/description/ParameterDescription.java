/*$Id: ParameterDescription.java,v 1.5 2007/03/08 14:35:25 clq2 Exp $
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
 
import org.astrogrid.applications.beans.v1.parameters.OptionList;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;

/** Description of a parameter to an application
 * @see org.astrogrid.applications.description.ApplicationDescription
 * @author Noel Winstanley nw@jb.man.ac.uk 25-May-2004
 * @TODO - would be sensible for the schema to match these names.....
 *
 */
public interface ParameterDescription {
    /** name of the parameter */
    public abstract String getName();
    /** name to use in UI */
    public abstract String getDisplayName();
   /** description to use in UI */
    public abstract String getDisplayDescription();
     /** UCD of parameter */
    public abstract String getUcd();
    /** units of parameter */
    public abstract String getUnits();
    /** type of parameter */
    public abstract  ParameterTypes getType();
    /** subtype / contraint on possible value of parameter */
    public abstract String getSubType();
    /** data encodings accepted for this parameter */
    public abstract String getAcceptEncodings();
    /** default value for this parameter */
    public abstract String getDefaultValue();
    /** Allowed values for this parameter */
    public abstract OptionList getOptionList();
    


}
/* 
$Log: ParameterDescription.java,v $
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