/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: DEFINITIONSItem.java,v 1.3 2004/03/05 09:52:02 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.votable;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class DEFINITIONSItem.
 * 
 * @version $Revision: 1.3 $ $Date: 2004/03/05 09:52:02 $
 */
public class DEFINITIONSItem implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _COOSYS
     */
    private org.astrogrid.registry.beans.resource.votable.COOSYS _COOSYS;

    /**
     * Field _PARAM
     */
    private org.astrogrid.registry.beans.resource.votable.PARAM _PARAM;


      //----------------/
     //- Constructors -/
    //----------------/

    public DEFINITIONSItem() {
        super();
    } //-- org.astrogrid.registry.beans.resource.votable.DEFINITIONSItem()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'COOSYS'.
     * 
     * @return the value of field 'COOSYS'.
     */
    public org.astrogrid.registry.beans.resource.votable.COOSYS getCOOSYS()
    {
        return this._COOSYS;
    } //-- org.astrogrid.registry.beans.resource.votable.COOSYS getCOOSYS() 

    /**
     * Returns the value of field 'PARAM'.
     * 
     * @return the value of field 'PARAM'.
     */
    public org.astrogrid.registry.beans.resource.votable.PARAM getPARAM()
    {
        return this._PARAM;
    } //-- org.astrogrid.registry.beans.resource.votable.PARAM getPARAM() 

    /**
     * Sets the value of field 'COOSYS'.
     * 
     * @param COOSYS the value of field 'COOSYS'.
     */
    public void setCOOSYS(org.astrogrid.registry.beans.resource.votable.COOSYS COOSYS)
    {
        this._COOSYS = COOSYS;
    } //-- void setCOOSYS(org.astrogrid.registry.beans.resource.votable.COOSYS) 

    /**
     * Sets the value of field 'PARAM'.
     * 
     * @param PARAM the value of field 'PARAM'.
     */
    public void setPARAM(org.astrogrid.registry.beans.resource.votable.PARAM PARAM)
    {
        this._PARAM = PARAM;
    } //-- void setPARAM(org.astrogrid.registry.beans.resource.votable.PARAM) 

}
