/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AstronTimeTypeReferenceUnit.java,v 1.2 2004/03/26 16:03:34 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class AstronTimeTypeReferenceUnit.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:34 $
 */
public class AstronTimeTypeReferenceUnit implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The s type
     */
    public static final int S_TYPE = 0;

    /**
     * The instance of the s type
     */
    public static final AstronTimeTypeReferenceUnit S = new AstronTimeTypeReferenceUnit(S_TYPE, "s");

    /**
     * The d type
     */
    public static final int D_TYPE = 1;

    /**
     * The instance of the d type
     */
    public static final AstronTimeTypeReferenceUnit D = new AstronTimeTypeReferenceUnit(D_TYPE, "d");

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

    private AstronTimeTypeReferenceUnit(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeReferenceUnit(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of AstronTimeTypeReferenceUnit
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this
     * AstronTimeTypeReferenceUnit
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
        members.put("s", S);
        members.put("d", D);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * AstronTimeTypeReferenceUnit
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new AstronTimeTypeReferenceUnit
     * based on the given String value.
     * 
     * @param string
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeReferenceUnit valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid AstronTimeTypeReferenceUnit";
            throw new IllegalArgumentException(err);
        }
        return (AstronTimeTypeReferenceUnit) obj;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeReferenceUnit valueOf(java.lang.String) 

}
