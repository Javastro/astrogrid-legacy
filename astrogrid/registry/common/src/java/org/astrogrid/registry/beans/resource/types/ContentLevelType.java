/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ContentLevelType.java,v 1.6 2004/03/19 08:16:47 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ContentLevelType.
 * 
 * @version $Revision: 1.6 $ $Date: 2004/03/19 08:16:47 $
 */
public class ContentLevelType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The General type
     */
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the General type
     */
    public static final ContentLevelType VALUE_0 = new ContentLevelType(VALUE_0_TYPE, "General");

    /**
     * The Elementary Education type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the Elementary Education type
     */
    public static final ContentLevelType VALUE_1 = new ContentLevelType(VALUE_1_TYPE, "Elementary Education");

    /**
     * The Middle School Education type
     */
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the Middle School Education type
     */
    public static final ContentLevelType VALUE_2 = new ContentLevelType(VALUE_2_TYPE, "Middle School Education");

    /**
     * The Secondary Education type
     */
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the Secondary Education type
     */
    public static final ContentLevelType VALUE_3 = new ContentLevelType(VALUE_3_TYPE, "Secondary Education");

    /**
     * The Community College type
     */
    public static final int VALUE_4_TYPE = 4;

    /**
     * The instance of the Community College type
     */
    public static final ContentLevelType VALUE_4 = new ContentLevelType(VALUE_4_TYPE, "Community College");

    /**
     * The University type
     */
    public static final int VALUE_5_TYPE = 5;

    /**
     * The instance of the University type
     */
    public static final ContentLevelType VALUE_5 = new ContentLevelType(VALUE_5_TYPE, "University");

    /**
     * The Research type
     */
    public static final int VALUE_6_TYPE = 6;

    /**
     * The instance of the Research type
     */
    public static final ContentLevelType VALUE_6 = new ContentLevelType(VALUE_6_TYPE, "Research");

    /**
     * The Amateur type
     */
    public static final int VALUE_7_TYPE = 7;

    /**
     * The instance of the Amateur type
     */
    public static final ContentLevelType VALUE_7 = new ContentLevelType(VALUE_7_TYPE, "Amateur");

    /**
     * The Informal Education type
     */
    public static final int VALUE_8_TYPE = 8;

    /**
     * The instance of the Informal Education type
     */
    public static final ContentLevelType VALUE_8 = new ContentLevelType(VALUE_8_TYPE, "Informal Education");

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

    private ContentLevelType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.resource.types.ContentLevelType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of ContentLevelType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this ContentLevelType
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
        members.put("General", VALUE_0);
        members.put("Elementary Education", VALUE_1);
        members.put("Middle School Education", VALUE_2);
        members.put("Secondary Education", VALUE_3);
        members.put("Community College", VALUE_4);
        members.put("University", VALUE_5);
        members.put("Research", VALUE_6);
        members.put("Amateur", VALUE_7);
        members.put("Informal Education", VALUE_8);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * ContentLevelType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new ContentLevelType based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.resource.types.ContentLevelType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid ContentLevelType";
            throw new IllegalArgumentException(err);
        }
        return (ContentLevelType) obj;
    } //-- org.astrogrid.registry.beans.resource.types.ContentLevelType valueOf(java.lang.String) 

}
