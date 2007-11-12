/**
 * 
 */
package org.astrogrid.desktop.hivemind;

import net.sourceforge.hiveutils.service.ObjectBuilder;

/** Object builder that allows the object to be built to be specified by the 
 * implementation class.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 7, 20071:26:29 PM
 */
public interface ClassKeyObjectBuilder extends ObjectBuilder {
    /** instantiate an object of class clazz */
    public Object create(Class clazz);

}
