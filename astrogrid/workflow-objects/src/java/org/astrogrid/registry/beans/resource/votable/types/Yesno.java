/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Yesno.java,v 1.7 2004/08/28 07:29:31 pah Exp $
 */

package org.astrogrid.registry.beans.resource.votable.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class Yesno.
 * 
 * @version $Revision: 1.7 $ $Date: 2004/08/28 07:29:31 $
 */
public class Yesno implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The yes type
     */
    public static final int YES_TYPE = 0;

    /**
     * The instance of the yes type
     */
    public static final Yesno YES = new Yesno(YES_TYPE, "yes");

    /**
     * The no type
     */
    public static final int NO_TYPE = 1;

    /**
     * The instance of the no type
     */
    public static final Yesno NO = new Yesno(NO_TYPE, "no");

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

    private Yesno(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.resource.votable.types.Yesno(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of Yesno
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this Yesno
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
        members.put("yes", YES);
        members.put("no", NO);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this Yesn
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new Yesno based on the given String
     * value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.resource.votable.types.Yesno valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid Yesno";
            throw new IllegalArgumentException(err);
        }
        return (Yesno) obj;
    } //-- org.astrogrid.registry.beans.resource.votable.types.Yesno valueOf(java.lang.String) 

}
