/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: DEFINITIONSItem.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
 */

package org.astrogrid.registry.generated.package;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class DEFINITIONSItem.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class DEFINITIONSItem implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _COOSYS
     */
    private org.astrogrid.registry.generated.package.COOSYS _COOSYS;

    /**
     * Field _PARAM
     */
    private org.astrogrid.registry.generated.package.PARAM _PARAM;


      //----------------/
     //- Constructors -/
    //----------------/

    public DEFINITIONSItem() {
        super();
    } //-- org.astrogrid.registry.generated.package.DEFINITIONSItem()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'COOSYS'.
     * 
     * @return the value of field 'COOSYS'.
     */
    public org.astrogrid.registry.generated.package.COOSYS getCOOSYS()
    {
        return this._COOSYS;
    } //-- org.astrogrid.registry.generated.package.COOSYS getCOOSYS() 

    /**
     * Returns the value of field 'PARAM'.
     * 
     * @return the value of field 'PARAM'.
     */
    public org.astrogrid.registry.generated.package.PARAM getPARAM()
    {
        return this._PARAM;
    } //-- org.astrogrid.registry.generated.package.PARAM getPARAM() 

    /**
     * Sets the value of field 'COOSYS'.
     * 
     * @param COOSYS the value of field 'COOSYS'.
     */
    public void setCOOSYS(org.astrogrid.registry.generated.package.COOSYS COOSYS)
    {
        this._COOSYS = COOSYS;
    } //-- void setCOOSYS(org.astrogrid.registry.generated.package.COOSYS) 

    /**
     * Sets the value of field 'PARAM'.
     * 
     * @param PARAM the value of field 'PARAM'.
     */
    public void setPARAM(org.astrogrid.registry.generated.package.PARAM PARAM)
    {
        this._PARAM = PARAM;
    } //-- void setPARAM(org.astrogrid.registry.generated.package.PARAM) 

}
