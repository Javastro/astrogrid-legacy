/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Status.java,v 1.1 2004/02/20 18:36:39 nw Exp $
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
 * Class Status.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/02/20 18:36:39 $
 */
public class Status implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The INITIALIZED type
     */
    public static final int INITIALIZED_TYPE = 0;

    /**
     * The instance of the INITIALIZED type
     */
    public static final Status INITIALIZED = new Status(INITIALIZED_TYPE, "INITIALIZED");

    /**
     * The RUNNING type
     */
    public static final int RUNNING_TYPE = 1;

    /**
     * The instance of the RUNNING type
     */
    public static final Status RUNNING = new Status(RUNNING_TYPE, "RUNNING");

    /**
     * The COMPLETED type
     */
    public static final int COMPLETED_TYPE = 2;

    /**
     * The instance of the COMPLETED type
     */
    public static final Status COMPLETED = new Status(COMPLETED_TYPE, "COMPLETED");

    /**
     * The ERROR type
     */
    public static final int ERROR_TYPE = 3;

    /**
     * The instance of the ERROR type
     */
    public static final Status ERROR = new Status(ERROR_TYPE, "ERROR");

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

    private Status(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.workflow.beans.v1.types.Status(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of Status
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this Status
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
        members.put("INITIALIZED", INITIALIZED);
        members.put("RUNNING", RUNNING);
        members.put("COMPLETED", COMPLETED);
        members.put("ERROR", ERROR);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * Status
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new Status based on the given String
     * value.
     * 
     * @param string
     */
    public static org.astrogrid.workflow.beans.v1.types.Status valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid Status";
            throw new IllegalArgumentException(err);
        }
        return (Status) obj;
    } //-- org.astrogrid.workflow.beans.v1.types.Status valueOf(java.lang.String) 

}
