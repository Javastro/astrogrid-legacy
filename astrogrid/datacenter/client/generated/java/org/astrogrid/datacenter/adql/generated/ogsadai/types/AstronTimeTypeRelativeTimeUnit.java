/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AstronTimeTypeRelativeTimeUnit.java,v 1.2 2004/03/26 16:03:34 eca Exp $
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
 * Class AstronTimeTypeRelativeTimeUnit.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:34 $
 */
public class AstronTimeTypeRelativeTimeUnit implements java.io.Serializable {


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
    public static final AstronTimeTypeRelativeTimeUnit S = new AstronTimeTypeRelativeTimeUnit(S_TYPE, "s");

    /**
     * The d type
     */
    public static final int D_TYPE = 1;

    /**
     * The instance of the d type
     */
    public static final AstronTimeTypeRelativeTimeUnit D = new AstronTimeTypeRelativeTimeUnit(D_TYPE, "d");

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

    private AstronTimeTypeRelativeTimeUnit(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeRelativeTimeUnit(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of AstronTimeTypeRelativeTimeUnit
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this
     * AstronTimeTypeRelativeTimeUnit
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
     * AstronTimeTypeRelativeTimeUnit
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new AstronTimeTypeRelativeTimeUnit
     * based on the given String value.
     * 
     * @param string
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeRelativeTimeUnit valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid AstronTimeTypeRelativeTimeUnit";
            throw new IllegalArgumentException(err);
        }
        return (AstronTimeTypeRelativeTimeUnit) obj;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeRelativeTimeUnit valueOf(java.lang.String) 

}
