/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ImageServiceType.java,v 1.2 2007/01/04 16:26:36 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource.sia.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * The class of image service: Cutout, Mosaic, Atlas, Pointed
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:36 $
 */
public class ImageServiceType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The Cutout type
     */
    public static final int CUTOUT_TYPE = 0;

    /**
     * The instance of the Cutout type
     */
    public static final ImageServiceType CUTOUT = new ImageServiceType(CUTOUT_TYPE, "Cutout");

    /**
     * The Mosaic type
     */
    public static final int MOSAIC_TYPE = 1;

    /**
     * The instance of the Mosaic type
     */
    public static final ImageServiceType MOSAIC = new ImageServiceType(MOSAIC_TYPE, "Mosaic");

    /**
     * The Atlas type
     */
    public static final int ATLAS_TYPE = 2;

    /**
     * The instance of the Atlas type
     */
    public static final ImageServiceType ATLAS = new ImageServiceType(ATLAS_TYPE, "Atlas");

    /**
     * The Pointed type
     */
    public static final int POINTED_TYPE = 3;

    /**
     * The instance of the Pointed type
     */
    public static final ImageServiceType POINTED = new ImageServiceType(POINTED_TYPE, "Pointed");

    /**
     * Field _memberTable
     */
    private static java.util.Hashtable _memberTable = init();

    /**
     * Field type
     */
    private int type = -1;

    /**
     * Field stringValue
     */
    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private ImageServiceType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.v10.resource.sia.types.ImageServiceType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of ImageServiceType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this ImageServiceType
     */
    public int getType()
    {
        return this.type;
    } //-- int getType() 

    /**
     * Method init
     */
    private static java.util.Hashtable init()
    {
        Hashtable members = new Hashtable();
        members.put("Cutout", CUTOUT);
        members.put("Mosaic", MOSAIC);
        members.put("Atlas", ATLAS);
        members.put("Pointed", POINTED);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * ImageServiceType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new ImageServiceType based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.v10.resource.sia.types.ImageServiceType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid ImageServiceType";
            throw new IllegalArgumentException(err);
        }
        return (ImageServiceType) obj;
    } //-- org.astrogrid.registry.beans.v10.resource.sia.types.ImageServiceType valueOf(java.lang.String) 

}
