/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AggregateFunction.java,v 1.5 2003/11/19 18:44:51 nw Exp $
 */

package org.astrogrid.datacenter.adql.generated.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class AggregateFunction.
 * 
 * @version $Revision: 1.5 $ $Date: 2003/11/19 18:44:51 $
 */
public class AggregateFunction implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The AVG type
     */
    public static final int AVG_TYPE = 0;

    /**
     * The instance of the AVG type
     */
    public static final AggregateFunction AVG = new AggregateFunction(AVG_TYPE, "AVG");

    /**
     * The MIN type
     */
    public static final int MIN_TYPE = 1;

    /**
     * The instance of the MIN type
     */
    public static final AggregateFunction MIN = new AggregateFunction(MIN_TYPE, "MIN");

    /**
     * The MAX type
     */
    public static final int MAX_TYPE = 2;

    /**
     * The instance of the MAX type
     */
    public static final AggregateFunction MAX = new AggregateFunction(MAX_TYPE, "MAX");

    /**
     * The SUM type
     */
    public static final int SUM_TYPE = 3;

    /**
     * The instance of the SUM type
     */
    public static final AggregateFunction SUM = new AggregateFunction(SUM_TYPE, "SUM");

    /**
     * The COUNT type
     */
    public static final int COUNT_TYPE = 4;

    /**
     * The instance of the COUNT type
     */
    public static final AggregateFunction COUNT = new AggregateFunction(COUNT_TYPE, "COUNT");

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

    private AggregateFunction(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.datacenter.adql.generated.types.AggregateFunction(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of AggregateFunction
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this AggregateFunction
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
        members.put("AVG", AVG);
        members.put("MIN", MIN);
        members.put("MAX", MAX);
        members.put("SUM", SUM);
        members.put("COUNT", COUNT);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * AggregateFunction
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new AggregateFunction based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.datacenter.adql.generated.types.AggregateFunction valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid AggregateFunction";
            throw new IllegalArgumentException(err);
        }
        return (AggregateFunction) obj;
    } //-- org.astrogrid.datacenter.adql.generated.types.AggregateFunction valueOf(java.lang.String) 

}
