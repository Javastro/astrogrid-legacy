/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: ContentLevelType.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
 */

package org.astrogrid.registry.generated.package.types;

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
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class ContentLevelType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The General type
     */
    public static final int GENERAL_TYPE = 0;

    /**
     * The instance of the General type
     */
    public static final ContentLevelType GENERAL = new ContentLevelType(GENERAL_TYPE, "General");

    /**
     * The Elementary Education type
     */
    public static final int ELEMENTARY_EDUCATION_TYPE = 1;

    /**
     * The instance of the Elementary Education type
     */
    public static final ContentLevelType ELEMENTARY_EDUCATION = new ContentLevelType(ELEMENTARY_EDUCATION_TYPE, "Elementary Education");

    /**
     * The Middle School Education type
     */
    public static final int MIDDLE_SCHOOL_EDUCATION_TYPE = 2;

    /**
     * The instance of the Middle School Education type
     */
    public static final ContentLevelType MIDDLE_SCHOOL_EDUCATION = new ContentLevelType(MIDDLE_SCHOOL_EDUCATION_TYPE, "Middle School Education");

    /**
     * The Secondary Education type
     */
    public static final int SECONDARY_EDUCATION_TYPE = 3;

    /**
     * The instance of the Secondary Education type
     */
    public static final ContentLevelType SECONDARY_EDUCATION = new ContentLevelType(SECONDARY_EDUCATION_TYPE, "Secondary Education");

    /**
     * The Community College type
     */
    public static final int COMMUNITY_COLLEGE_TYPE = 4;

    /**
     * The instance of the Community College type
     */
    public static final ContentLevelType COMMUNITY_COLLEGE = new ContentLevelType(COMMUNITY_COLLEGE_TYPE, "Community College");

    /**
     * The University type
     */
    public static final int UNIVERSITY_TYPE = 5;

    /**
     * The instance of the University type
     */
    public static final ContentLevelType UNIVERSITY = new ContentLevelType(UNIVERSITY_TYPE, "University");

    /**
     * The Research type
     */
    public static final int RESEARCH_TYPE = 6;

    /**
     * The instance of the Research type
     */
    public static final ContentLevelType RESEARCH = new ContentLevelType(RESEARCH_TYPE, "Research");

    /**
     * The Amateur type
     */
    public static final int AMATEUR_TYPE = 7;

    /**
     * The instance of the Amateur type
     */
    public static final ContentLevelType AMATEUR = new ContentLevelType(AMATEUR_TYPE, "Amateur");

    /**
     * The Informal Education type
     */
    public static final int INFORMAL_EDUCATION_TYPE = 8;

    /**
     * The instance of the Informal Education type
     */
    public static final ContentLevelType INFORMAL_EDUCATION = new ContentLevelType(INFORMAL_EDUCATION_TYPE, "Informal Education");

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
    } //-- org.astrogrid.registry.generated.package.types.ContentLevelType(int, java.lang.String)


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
        members.put("General", GENERAL);
        members.put("Elementary Education", ELEMENTARY_EDUCATION);
        members.put("Middle School Education", MIDDLE_SCHOOL_EDUCATION);
        members.put("Secondary Education", SECONDARY_EDUCATION);
        members.put("Community College", COMMUNITY_COLLEGE);
        members.put("University", UNIVERSITY);
        members.put("Research", RESEARCH);
        members.put("Amateur", AMATEUR);
        members.put("Informal Education", INFORMAL_EDUCATION);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method readResolve will be called during deserialization to
     * replace the deserialized object with the correct constant
     * instance. <br/>
     */
    private java.lang.Object readResolve()
    {
        return valueOf(this.stringValue);
    } //-- java.lang.Object readResolve() 

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
    public static org.astrogrid.registry.generated.package.types.ContentLevelType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid ContentLevelType";
            throw new IllegalArgumentException(err);
        }
        return (ContentLevelType) obj;
    } //-- org.astrogrid.registry.generated.package.types.ContentLevelType valueOf(java.lang.String) 

}
