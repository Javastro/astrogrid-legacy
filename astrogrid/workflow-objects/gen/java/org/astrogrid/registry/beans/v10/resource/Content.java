/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Content.java,v 1.2 2005/07/05 08:26:56 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.astrogrid.registry.beans.v10.resource.types.ContentLevel;
import org.astrogrid.registry.beans.v10.resource.types.Type;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Information regarding the general content of a resource
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:56 $
 */
public class Content extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * List of topics, object types, or other descriptive keywords 
     *  about the resource. 
     *  
     */
    private java.util.ArrayList _subjectList;

    /**
     * An account of the nature of the resource
     *  
     */
    private java.lang.String _description;

    /**
     * a bibliographic reference from which the present resource is
     * 
     *  derived or extracted. 
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.Source _source;

    /**
     * URL pointing to a human-readable document describing this 
     *  resource. 
     *  
     */
    private java.lang.String _referenceURL;

    /**
     * Nature or genre of the content of the resource
     *  
     */
    private java.util.ArrayList _typeList;

    /**
     * Description of the content level or intended audience
     *  
     */
    private java.util.ArrayList _contentLevelList;

    /**
     * a description of a relationship to another resource. 
     *  
     */
    private java.util.ArrayList _relationshipList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Content() {
        super();
        _subjectList = new ArrayList();
        _typeList = new ArrayList();
        _contentLevelList = new ArrayList();
        _relationshipList = new ArrayList();
    } //-- org.astrogrid.registry.beans.v10.resource.Content()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addContentLevel
     * 
     * @param vContentLevel
     */
    public void addContentLevel(org.astrogrid.registry.beans.v10.resource.types.ContentLevel vContentLevel)
        throws java.lang.IndexOutOfBoundsException
    {
        _contentLevelList.add(vContentLevel);
    } //-- void addContentLevel(org.astrogrid.registry.beans.v10.resource.types.ContentLevel) 

    /**
     * Method addContentLevel
     * 
     * @param index
     * @param vContentLevel
     */
    public void addContentLevel(int index, org.astrogrid.registry.beans.v10.resource.types.ContentLevel vContentLevel)
        throws java.lang.IndexOutOfBoundsException
    {
        _contentLevelList.add(index, vContentLevel);
    } //-- void addContentLevel(int, org.astrogrid.registry.beans.v10.resource.types.ContentLevel) 

    /**
     * Method addRelationship
     * 
     * @param vRelationship
     */
    public void addRelationship(org.astrogrid.registry.beans.v10.resource.Relationship vRelationship)
        throws java.lang.IndexOutOfBoundsException
    {
        _relationshipList.add(vRelationship);
    } //-- void addRelationship(org.astrogrid.registry.beans.v10.resource.Relationship) 

    /**
     * Method addRelationship
     * 
     * @param index
     * @param vRelationship
     */
    public void addRelationship(int index, org.astrogrid.registry.beans.v10.resource.Relationship vRelationship)
        throws java.lang.IndexOutOfBoundsException
    {
        _relationshipList.add(index, vRelationship);
    } //-- void addRelationship(int, org.astrogrid.registry.beans.v10.resource.Relationship) 

    /**
     * Method addSubject
     * 
     * @param vSubject
     */
    public void addSubject(java.lang.String vSubject)
        throws java.lang.IndexOutOfBoundsException
    {
        _subjectList.add(vSubject);
    } //-- void addSubject(java.lang.String) 

    /**
     * Method addSubject
     * 
     * @param index
     * @param vSubject
     */
    public void addSubject(int index, java.lang.String vSubject)
        throws java.lang.IndexOutOfBoundsException
    {
        _subjectList.add(index, vSubject);
    } //-- void addSubject(int, java.lang.String) 

    /**
     * Method addType
     * 
     * @param vType
     */
    public void addType(org.astrogrid.registry.beans.v10.resource.types.Type vType)
        throws java.lang.IndexOutOfBoundsException
    {
        _typeList.add(vType);
    } //-- void addType(org.astrogrid.registry.beans.v10.resource.types.Type) 

    /**
     * Method addType
     * 
     * @param index
     * @param vType
     */
    public void addType(int index, org.astrogrid.registry.beans.v10.resource.types.Type vType)
        throws java.lang.IndexOutOfBoundsException
    {
        _typeList.add(index, vType);
    } //-- void addType(int, org.astrogrid.registry.beans.v10.resource.types.Type) 

    /**
     * Method clearContentLevel
     */
    public void clearContentLevel()
    {
        _contentLevelList.clear();
    } //-- void clearContentLevel() 

    /**
     * Method clearRelationship
     */
    public void clearRelationship()
    {
        _relationshipList.clear();
    } //-- void clearRelationship() 

    /**
     * Method clearSubject
     */
    public void clearSubject()
    {
        _subjectList.clear();
    } //-- void clearSubject() 

    /**
     * Method clearType
     */
    public void clearType()
    {
        _typeList.clear();
    } //-- void clearType() 

    /**
     * Method enumerateContentLevel
     */
    public java.util.Enumeration enumerateContentLevel()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_contentLevelList.iterator());
    } //-- java.util.Enumeration enumerateContentLevel() 

    /**
     * Method enumerateRelationship
     */
    public java.util.Enumeration enumerateRelationship()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_relationshipList.iterator());
    } //-- java.util.Enumeration enumerateRelationship() 

    /**
     * Method enumerateSubject
     */
    public java.util.Enumeration enumerateSubject()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_subjectList.iterator());
    } //-- java.util.Enumeration enumerateSubject() 

    /**
     * Method enumerateType
     */
    public java.util.Enumeration enumerateType()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_typeList.iterator());
    } //-- java.util.Enumeration enumerateType() 

    /**
     * Note: hashCode() has not been overriden
     * 
     * @param obj
     */
    public boolean equals(java.lang.Object obj)
    {
        if ( this == obj )
            return true;
        
        if (super.equals(obj)==false)
            return false;
        
        if (obj instanceof Content) {
        
            Content temp = (Content)obj;
            if (this._subjectList != null) {
                if (temp._subjectList == null) return false;
                else if (!(this._subjectList.equals(temp._subjectList))) 
                    return false;
            }
            else if (temp._subjectList != null)
                return false;
            if (this._description != null) {
                if (temp._description == null) return false;
                else if (!(this._description.equals(temp._description))) 
                    return false;
            }
            else if (temp._description != null)
                return false;
            if (this._source != null) {
                if (temp._source == null) return false;
                else if (!(this._source.equals(temp._source))) 
                    return false;
            }
            else if (temp._source != null)
                return false;
            if (this._referenceURL != null) {
                if (temp._referenceURL == null) return false;
                else if (!(this._referenceURL.equals(temp._referenceURL))) 
                    return false;
            }
            else if (temp._referenceURL != null)
                return false;
            if (this._typeList != null) {
                if (temp._typeList == null) return false;
                else if (!(this._typeList.equals(temp._typeList))) 
                    return false;
            }
            else if (temp._typeList != null)
                return false;
            if (this._contentLevelList != null) {
                if (temp._contentLevelList == null) return false;
                else if (!(this._contentLevelList.equals(temp._contentLevelList))) 
                    return false;
            }
            else if (temp._contentLevelList != null)
                return false;
            if (this._relationshipList != null) {
                if (temp._relationshipList == null) return false;
                else if (!(this._relationshipList.equals(temp._relationshipList))) 
                    return false;
            }
            else if (temp._relationshipList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Method getContentLevel
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.v10.resource.types.ContentLevel getContentLevel(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _contentLevelList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.v10.resource.types.ContentLevel) _contentLevelList.get(index);
    } //-- org.astrogrid.registry.beans.v10.resource.types.ContentLevel getContentLevel(int) 

    /**
     * Method getContentLevel
     */
    public org.astrogrid.registry.beans.v10.resource.types.ContentLevel[] getContentLevel()
    {
        int size = _contentLevelList.size();
        org.astrogrid.registry.beans.v10.resource.types.ContentLevel[] mArray = new org.astrogrid.registry.beans.v10.resource.types.ContentLevel[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.v10.resource.types.ContentLevel) _contentLevelList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.v10.resource.types.ContentLevel[] getContentLevel() 

    /**
     * Method getContentLevelCount
     */
    public int getContentLevelCount()
    {
        return _contentLevelList.size();
    } //-- int getContentLevelCount() 

    /**
     * Returns the value of field 'description'. The field
     * 'description' has the following description: An account of
     * the nature of the resource
     *  
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'referenceURL'. The field
     * 'referenceURL' has the following description: URL pointing
     * to a human-readable document describing this 
     *  resource. 
     *  
     * 
     * @return the value of field 'referenceURL'.
     */
    public java.lang.String getReferenceURL()
    {
        return this._referenceURL;
    } //-- java.lang.String getReferenceURL() 

    /**
     * Method getRelationship
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.v10.resource.Relationship getRelationship(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _relationshipList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.v10.resource.Relationship) _relationshipList.get(index);
    } //-- org.astrogrid.registry.beans.v10.resource.Relationship getRelationship(int) 

    /**
     * Method getRelationship
     */
    public org.astrogrid.registry.beans.v10.resource.Relationship[] getRelationship()
    {
        int size = _relationshipList.size();
        org.astrogrid.registry.beans.v10.resource.Relationship[] mArray = new org.astrogrid.registry.beans.v10.resource.Relationship[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.v10.resource.Relationship) _relationshipList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.v10.resource.Relationship[] getRelationship() 

    /**
     * Method getRelationshipCount
     */
    public int getRelationshipCount()
    {
        return _relationshipList.size();
    } //-- int getRelationshipCount() 

    /**
     * Returns the value of field 'source'. The field 'source' has
     * the following description: a bibliographic reference from
     * which the present resource is 
     *  derived or extracted. 
     *  
     * 
     * @return the value of field 'source'.
     */
    public org.astrogrid.registry.beans.v10.resource.Source getSource()
    {
        return this._source;
    } //-- org.astrogrid.registry.beans.v10.resource.Source getSource() 

    /**
     * Method getSubject
     * 
     * @param index
     */
    public java.lang.String getSubject(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _subjectList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_subjectList.get(index);
    } //-- java.lang.String getSubject(int) 

    /**
     * Method getSubject
     */
    public java.lang.String[] getSubject()
    {
        int size = _subjectList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_subjectList.get(index);
        }
        return mArray;
    } //-- java.lang.String[] getSubject() 

    /**
     * Method getSubjectCount
     */
    public int getSubjectCount()
    {
        return _subjectList.size();
    } //-- int getSubjectCount() 

    /**
     * Method getType
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.v10.resource.types.Type getType(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _typeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.v10.resource.types.Type) _typeList.get(index);
    } //-- org.astrogrid.registry.beans.v10.resource.types.Type getType(int) 

    /**
     * Method getType
     */
    public org.astrogrid.registry.beans.v10.resource.types.Type[] getType()
    {
        int size = _typeList.size();
        org.astrogrid.registry.beans.v10.resource.types.Type[] mArray = new org.astrogrid.registry.beans.v10.resource.types.Type[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.v10.resource.types.Type) _typeList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.v10.resource.types.Type[] getType() 

    /**
     * Method getTypeCount
     */
    public int getTypeCount()
    {
        return _typeList.size();
    } //-- int getTypeCount() 

    /**
     * Method isValid
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Method removeContentLevel
     * 
     * @param vContentLevel
     */
    public boolean removeContentLevel(org.astrogrid.registry.beans.v10.resource.types.ContentLevel vContentLevel)
    {
        boolean removed = _contentLevelList.remove(vContentLevel);
        return removed;
    } //-- boolean removeContentLevel(org.astrogrid.registry.beans.v10.resource.types.ContentLevel) 

    /**
     * Method removeRelationship
     * 
     * @param vRelationship
     */
    public boolean removeRelationship(org.astrogrid.registry.beans.v10.resource.Relationship vRelationship)
    {
        boolean removed = _relationshipList.remove(vRelationship);
        return removed;
    } //-- boolean removeRelationship(org.astrogrid.registry.beans.v10.resource.Relationship) 

    /**
     * Method removeSubject
     * 
     * @param vSubject
     */
    public boolean removeSubject(java.lang.String vSubject)
    {
        boolean removed = _subjectList.remove(vSubject);
        return removed;
    } //-- boolean removeSubject(java.lang.String) 

    /**
     * Method removeType
     * 
     * @param vType
     */
    public boolean removeType(org.astrogrid.registry.beans.v10.resource.types.Type vType)
    {
        boolean removed = _typeList.remove(vType);
        return removed;
    } //-- boolean removeType(org.astrogrid.registry.beans.v10.resource.types.Type) 

    /**
     * Method setContentLevel
     * 
     * @param index
     * @param vContentLevel
     */
    public void setContentLevel(int index, org.astrogrid.registry.beans.v10.resource.types.ContentLevel vContentLevel)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _contentLevelList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _contentLevelList.set(index, vContentLevel);
    } //-- void setContentLevel(int, org.astrogrid.registry.beans.v10.resource.types.ContentLevel) 

    /**
     * Method setContentLevel
     * 
     * @param contentLevelArray
     */
    public void setContentLevel(org.astrogrid.registry.beans.v10.resource.types.ContentLevel[] contentLevelArray)
    {
        //-- copy array
        _contentLevelList.clear();
        for (int i = 0; i < contentLevelArray.length; i++) {
            _contentLevelList.add(contentLevelArray[i]);
        }
    } //-- void setContentLevel(org.astrogrid.registry.beans.v10.resource.types.ContentLevel) 

    /**
     * Sets the value of field 'description'. The field
     * 'description' has the following description: An account of
     * the nature of the resource
     *  
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'referenceURL'. The field
     * 'referenceURL' has the following description: URL pointing
     * to a human-readable document describing this 
     *  resource. 
     *  
     * 
     * @param referenceURL the value of field 'referenceURL'.
     */
    public void setReferenceURL(java.lang.String referenceURL)
    {
        this._referenceURL = referenceURL;
    } //-- void setReferenceURL(java.lang.String) 

    /**
     * Method setRelationship
     * 
     * @param index
     * @param vRelationship
     */
    public void setRelationship(int index, org.astrogrid.registry.beans.v10.resource.Relationship vRelationship)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _relationshipList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _relationshipList.set(index, vRelationship);
    } //-- void setRelationship(int, org.astrogrid.registry.beans.v10.resource.Relationship) 

    /**
     * Method setRelationship
     * 
     * @param relationshipArray
     */
    public void setRelationship(org.astrogrid.registry.beans.v10.resource.Relationship[] relationshipArray)
    {
        //-- copy array
        _relationshipList.clear();
        for (int i = 0; i < relationshipArray.length; i++) {
            _relationshipList.add(relationshipArray[i]);
        }
    } //-- void setRelationship(org.astrogrid.registry.beans.v10.resource.Relationship) 

    /**
     * Sets the value of field 'source'. The field 'source' has the
     * following description: a bibliographic reference from which
     * the present resource is 
     *  derived or extracted. 
     *  
     * 
     * @param source the value of field 'source'.
     */
    public void setSource(org.astrogrid.registry.beans.v10.resource.Source source)
    {
        this._source = source;
    } //-- void setSource(org.astrogrid.registry.beans.v10.resource.Source) 

    /**
     * Method setSubject
     * 
     * @param index
     * @param vSubject
     */
    public void setSubject(int index, java.lang.String vSubject)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _subjectList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _subjectList.set(index, vSubject);
    } //-- void setSubject(int, java.lang.String) 

    /**
     * Method setSubject
     * 
     * @param subjectArray
     */
    public void setSubject(java.lang.String[] subjectArray)
    {
        //-- copy array
        _subjectList.clear();
        for (int i = 0; i < subjectArray.length; i++) {
            _subjectList.add(subjectArray[i]);
        }
    } //-- void setSubject(java.lang.String) 

    /**
     * Method setType
     * 
     * @param index
     * @param vType
     */
    public void setType(int index, org.astrogrid.registry.beans.v10.resource.types.Type vType)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _typeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _typeList.set(index, vType);
    } //-- void setType(int, org.astrogrid.registry.beans.v10.resource.types.Type) 

    /**
     * Method setType
     * 
     * @param typeArray
     */
    public void setType(org.astrogrid.registry.beans.v10.resource.types.Type[] typeArray)
    {
        //-- copy array
        _typeList.clear();
        for (int i = 0; i < typeArray.length; i++) {
            _typeList.add(typeArray[i]);
        }
    } //-- void setType(org.astrogrid.registry.beans.v10.resource.types.Type) 

    /**
     * Method unmarshalContent
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.Content unmarshalContent(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.Content) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.Content.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.Content unmarshalContent(java.io.Reader) 

    /**
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
