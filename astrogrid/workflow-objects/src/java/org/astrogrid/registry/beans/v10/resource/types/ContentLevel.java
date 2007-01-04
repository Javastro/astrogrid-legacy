/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ContentLevel.java,v 1.2 2007/01/04 16:26:31 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class ContentLevel.
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:31 $
 */
public class ContentLevel implements java.io.Serializable {


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
    public static final ContentLevel VALUE_0 = new ContentLevel(VALUE_0_TYPE, "General");

    /**
     * The Elementary Education type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the Elementary Education type
     */
    public static final ContentLevel VALUE_1 = new ContentLevel(VALUE_1_TYPE, "Elementary Education");

    /**
     * The Middle School Education type
     */
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the Middle School Education type
     */
    public static final ContentLevel VALUE_2 = new ContentLevel(VALUE_2_TYPE, "Middle School Education");

    /**
     * The Secondary Education type
     */
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the Secondary Education type
     */
    public static final ContentLevel VALUE_3 = new ContentLevel(VALUE_3_TYPE, "Secondary Education");

    /**
     * The Community College type
     */
    public static final int VALUE_4_TYPE = 4;

    /**
     * The instance of the Community College type
     */
    public static final ContentLevel VALUE_4 = new ContentLevel(VALUE_4_TYPE, "Community College");

    /**
     * The University type
     */
    public static final int VALUE_5_TYPE = 5;

    /**
     * The instance of the University type
     */
    public static final ContentLevel VALUE_5 = new ContentLevel(VALUE_5_TYPE, "University");

    /**
     * The Research type
     */
    public static final int VALUE_6_TYPE = 6;

    /**
     * The instance of the Research type
     */
    public static final ContentLevel VALUE_6 = new ContentLevel(VALUE_6_TYPE, "Research");

    /**
     * The Amateur type
     */
    public static final int VALUE_7_TYPE = 7;

    /**
     * The instance of the Amateur type
     */
    public static final ContentLevel VALUE_7 = new ContentLevel(VALUE_7_TYPE, "Amateur");

    /**
     * The Informal Education type
     */
    public static final int VALUE_8_TYPE = 8;

    /**
     * The instance of the Informal Education type
     */
    public static final ContentLevel VALUE_8 = new ContentLevel(VALUE_8_TYPE, "Informal Education");

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

    private ContentLevel(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.v10.resource.types.ContentLevel(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of ContentLevel
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this ContentLevel
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
     * ContentLevel
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new ContentLevel based on the given
     * String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.v10.resource.types.ContentLevel valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid ContentLevel";
            throw new IllegalArgumentException(err);
        }
        return (ContentLevel) obj;
    } //-- org.astrogrid.registry.beans.v10.resource.types.ContentLevel valueOf(java.lang.String) 

}
