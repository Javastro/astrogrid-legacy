/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: SwitchTypes.java,v 1.13 2004/03/10 13:58:29 pah Exp $
 */

package org.astrogrid.applications.beans.v1.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * normal is the -x value style and keyword is the x=value style
 * 
 * @version $Revision: 1.13 $ $Date: 2004/03/10 13:58:29 $
 */
public class SwitchTypes implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The normal type
     */
    public static final int NORMAL_TYPE = 0;

    /**
     * The instance of the normal type
     */
    public static final SwitchTypes NORMAL = new SwitchTypes(NORMAL_TYPE, "normal");

    /**
     * The keyword type
     */
    public static final int KEYWORD_TYPE = 1;

    /**
     * The instance of the keyword type
     */
    public static final SwitchTypes KEYWORD = new SwitchTypes(KEYWORD_TYPE, "keyword");

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

    private SwitchTypes(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.applications.beans.v1.types.SwitchTypes(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of SwitchTypes
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this SwitchTypes
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
        members.put("normal", NORMAL);
        members.put("keyword", KEYWORD);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * SwitchTypes
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new SwitchTypes based on the given
     * String value.
     * 
     * @param string
     */
    public static org.astrogrid.applications.beans.v1.types.SwitchTypes valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid SwitchTypes";
            throw new IllegalArgumentException(err);
        }
        return (SwitchTypes) obj;
    } //-- org.astrogrid.applications.beans.v1.types.SwitchTypes valueOf(java.lang.String) 

}
