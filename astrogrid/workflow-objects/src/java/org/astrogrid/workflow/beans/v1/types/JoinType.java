/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: JoinType.java,v 1.2 2004/03/03 21:48:00 nw Exp $
 */

package org.astrogrid.workflow.beans.v1.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class JoinType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/03 21:48:00 $
 */
public class JoinType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The true type
     */
    public static final int TRUE_TYPE = 0;

    /**
     * The instance of the true type
     */
    public static final JoinType TRUE = new JoinType(TRUE_TYPE, "true");

    /**
     * The false type
     */
    public static final int FALSE_TYPE = 1;

    /**
     * The instance of the false type
     */
    public static final JoinType FALSE = new JoinType(FALSE_TYPE, "false");

    /**
     * The any type
     */
    public static final int ANY_TYPE = 2;

    /**
     * The instance of the any type
     */
    public static final JoinType ANY = new JoinType(ANY_TYPE, "any");

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

    private JoinType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.workflow.beans.v1.types.JoinType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of JoinType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this JoinType
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
        members.put("true", TRUE);
        members.put("false", FALSE);
        members.put("any", ANY);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * JoinType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new JoinType based on the given
     * String value.
     * 
     * @param string
     */
    public static org.astrogrid.workflow.beans.v1.types.JoinType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid JoinType";
            throw new IllegalArgumentException(err);
        }
        return (JoinType) obj;
    } //-- org.astrogrid.workflow.beans.v1.types.JoinType valueOf(java.lang.String) 

}
