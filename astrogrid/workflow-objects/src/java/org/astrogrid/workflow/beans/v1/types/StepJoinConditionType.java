/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: StepJoinConditionType.java,v 1.2 2004/03/02 14:09:49 pah Exp $
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
 * Class StepJoinConditionType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/02 14:09:49 $
 */
public class StepJoinConditionType implements java.io.Serializable {


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
    public static final StepJoinConditionType TRUE = new StepJoinConditionType(TRUE_TYPE, "true");

    /**
     * The false type
     */
    public static final int FALSE_TYPE = 1;

    /**
     * The instance of the false type
     */
    public static final StepJoinConditionType FALSE = new StepJoinConditionType(FALSE_TYPE, "false");

    /**
     * The any type
     */
    public static final int ANY_TYPE = 2;

    /**
     * The instance of the any type
     */
    public static final StepJoinConditionType ANY = new StepJoinConditionType(ANY_TYPE, "any");

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

    private StepJoinConditionType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.workflow.beans.v1.types.StepJoinConditionType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of StepJoinConditionType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this StepJoinConditionType
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
     * StepJoinConditionType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new StepJoinConditionType based on
     * the given String value.
     * 
     * @param string
     */
    public static org.astrogrid.workflow.beans.v1.types.StepJoinConditionType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid StepJoinConditionType";
            throw new IllegalArgumentException(err);
        }
        return (StepJoinConditionType) obj;
    } //-- org.astrogrid.workflow.beans.v1.types.StepJoinConditionType valueOf(java.lang.String) 

}
