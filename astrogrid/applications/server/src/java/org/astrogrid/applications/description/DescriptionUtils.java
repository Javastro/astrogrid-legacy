/*$Id: DescriptionUtils.java,v 1.8 2007/03/08 14:35:25 clq2 Exp $
 * Created on 08-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.description;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.beans.v1.ApplicationBase;
import org.astrogrid.applications.beans.v1.Input;
import org.astrogrid.applications.beans.v1.Interface;
import org.astrogrid.applications.beans.v1.InterfacesType;
import org.astrogrid.applications.beans.v1.Output;
import org.astrogrid.applications.beans.v1.ParameterRef;
import org.astrogrid.applications.beans.v1.parameters.BaseParameterDefinition;
import org.astrogrid.applications.beans.v1.parameters.OptionList;
import org.astrogrid.applications.beans.v1.parameters.XhtmlDocumentation;
import org.astrogrid.applications.description.exception.ParameterNotInInterfaceException;

/** Class of static helper methods for working wih descriptions.
 * @author Noel Winstanley nw@jb.man.ac.uk 08-Jun-2004
 * 
 *
 */
public class DescriptionUtils {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(DescriptionUtils.class);

    /** Construct a new DescriptionUtils
     * 
     */
    private DescriptionUtils() {
        super();
    }
    /** maps between internal type ({@link ApplicationDescription}) and castor object type used in registry schema ({@link ApplicationBase})
     * <p/>
     * These types are structurally very similar - just differ on type names.
   * @param descr
   * @return
   */
  public static ApplicationBase applicationDescription2ApplicationBase(ApplicationDescription descr) {
      ApplicationBase base = new ApplicationBase();
      base.setName(descr.getName());
      // interface.
      ApplicationInterface[] ifaces = descr.getInterfaces();
      InterfacesType interfacesType = new InterfacesType();
      base.setInterfaces(interfacesType);
      for (int i = 0; i < ifaces.length; i++) {
          Interface iface = new Interface();
          ApplicationInterface appIface = ifaces[i];
          iface.setName(appIface.getName());
          Input input = new Input();
          Output output = new Output();
          iface.setInput(input);
          iface.setOutput(output);
          String[] inputRefs = appIface.getArrayofInputs();
          for (int j = 0; j < inputRefs.length; j++) {
              ParameterRef pref = new ParameterRef();
              pref.setRef(inputRefs[j]);
              try {
                  Cardinality c = appIface.getParameterCardinality(inputRefs[j]);
                  pref.setMaxoccurs(c.getMaxOccurs());
                  pref.setMinoccurs(c.getMinOccurs());
              } catch (ParameterNotInInterfaceException e) {
                  // minor problem. not worth halting for.
                  logger.warn("Problem with representation of " + inputRefs[j],e);
              }
              input.addPref(pref);
          }
          String[] outputRefs = appIface.getArrayofOutputs();
          for (int j = 0; j < outputRefs.length; j++) {
              ParameterRef pref = new ParameterRef();
              pref.setRef(outputRefs[j]);
              try {
                  Cardinality c = appIface.getParameterCardinality(outputRefs[j]);
                  pref.setMaxoccurs(c.getMaxOccurs());
                  pref.setMinoccurs(c.getMinOccurs());
              } catch (ParameterNotInInterfaceException e) {
                  // minor problem. not worth halting for.
                  logger.warn("Problem with representation of " + outputRefs[j],e);
              }
              output.addPref(pref);
          }        
          interfacesType.add_interface(iface);
      }
      // now parameters.
      ParameterDescription[] params = descr.getParameterDescriptions();
      org.astrogrid.applications.beans.v1.Parameters parameters = new org.astrogrid.applications.beans.v1.Parameters();
      base.setParameters(parameters);
      for (int i = 0; i < params.length; i++) {
          BaseParameterDefinition paramDef = new BaseParameterDefinition();
          ParameterDescription paramDesc = params[i];
          XhtmlDocumentation xhtmldoc = new XhtmlDocumentation();
          xhtmldoc.setContent(paramDesc.getDisplayDescription());
          paramDef.setUI_Description( xhtmldoc); 
          paramDef.setUI_Name(  paramDesc.getDisplayName());
          paramDef.setName(  paramDesc.getName());
          paramDef.setUCD( paramDesc.getUcd());
          paramDef.setType(paramDesc.getType());
          paramDef.setSubType(paramDesc.getSubType());
          paramDef.setDefaultValue(paramDesc.getDefaultValue());
          paramDef.setUnits(paramDesc.getUnits());
          paramDef.setAcceptEncodings(paramDesc.getAcceptEncodings());
          paramDef.setOptionList(paramDesc.getOptionList());
         parameters.addParameter(paramDef);
      }
    
      // blerght. glad that's over
      return base;
    
  }    
}


/* 
$Log: DescriptionUtils.java,v $
Revision 1.8  2007/03/08 14:35:25  clq2
gtr_apps_2056

Revision 1.7.192.1  2007/03/01 17:35:16  gtr
The OptionList is now transcribed with the other properties.

Revision 1.7  2004/09/22 10:52:50  pah
getting rid of some unused imports

Revision 1.6  2004/09/09 13:21:09  pah
schema change

Revision 1.5  2004/08/17 15:08:26  nw
fixed but I introduced previously.

Revision 1.4  2004/08/16 11:03:07  nw
added classes to model cardinality of prefs.

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
 
*/