/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ImageServiceTypeType.java,v 1.3 2004/03/05 09:52:02 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.sia.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ImageServiceTypeType.
 * 
 * @version $Revision: 1.3 $ $Date: 2004/03/05 09:52:02 $
 */
public class ImageServiceTypeType implements java.io.Serializable {


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
    public static final ImageServiceTypeType CUTOUT = new ImageServiceTypeType(CUTOUT_TYPE, "Cutout");

    /**
     * The Mosaic type
     */
    public static final int MOSAIC_TYPE = 1;

    /**
     * The instance of the Mosaic type
     */
    public static final ImageServiceTypeType MOSAIC = new ImageServiceTypeType(MOSAIC_TYPE, "Mosaic");

    /**
     * The Atlas type
     */
    public static final int ATLAS_TYPE = 2;

    /**
     * The instance of the Atlas type
     */
    public static final ImageServiceTypeType ATLAS = new ImageServiceTypeType(ATLAS_TYPE, "Atlas");

    /**
     * The Pointed type
     */
    public static final int POINTED_TYPE = 3;

    /**
     * The instance of the Pointed type
     */
    public static final ImageServiceTypeType POINTED = new ImageServiceTypeType(POINTED_TYPE, "Pointed");

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

    private ImageServiceTypeType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.resource.sia.types.ImageServiceTypeType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of ImageServiceTypeType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this ImageServiceTypeType
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
     * ImageServiceTypeType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new ImageServiceTypeType based on
     * the given String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.resource.sia.types.ImageServiceTypeType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid ImageServiceTypeType";
            throw new IllegalArgumentException(err);
        }
        return (ImageServiceTypeType) obj;
    } //-- org.astrogrid.registry.beans.resource.sia.types.ImageServiceTypeType valueOf(java.lang.String) 

}
