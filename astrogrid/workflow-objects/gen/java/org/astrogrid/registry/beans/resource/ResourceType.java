/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ResourceType.java,v 1.2 2005/07/05 08:26:56 clq2 Exp $
 */

package org.astrogrid.registry.beans.resource;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import org.astrogrid.registry.beans.resource.types.CategoryType;
import org.astrogrid.registry.beans.resource.types.ContentLevelType;
import org.astrogrid.registry.beans.resource.types.ResourceTypeStatusType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class ResourceType.
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:56 $
 */
public class ResourceType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The date this resource metadata description was created.
     *  
     */
    private java.util.Date _created;

    /**
     * The date this resource metadata description was last
     * updated.
     *  
     */
    private java.util.Date _updated;

    /**
     * a tag indicating whether this resource is believed to be
     * still
     *  actively maintained.
     *  
     */
    private org.astrogrid.registry.beans.resource.types.ResourceTypeStatusType _status = org.astrogrid.registry.beans.resource.types.ResourceTypeStatusType.valueOf("active");

    /**
     * Unambiguous reference to the resource conforming to the IVOA
     *  standard for identifiers
     *  
     */
    private org.astrogrid.registry.beans.resource.IdentifierType _identifier;

    /**
     * the full name of a resource
     */
    private java.lang.String _title;

    /**
     * a short name or abbreviation for this resource that
     * applications 
     *  may use to refer to this resource in a compact display. 
     *  
     */
    private java.lang.String _shortName;

    /**
     * An informational, human-readable overview of a resource.
     *  
     */
    private org.astrogrid.registry.beans.resource.SummaryType _summary;

    /**
     * Nature or genre of the content of the resource
     *  
     */
    private java.util.ArrayList _typeList;

    /**
     * an description of a relationship to another resource. 
     *  
     */
    private java.util.ArrayList _relatedResourceList;

    /**
     * This is typically not provided unless this resource is 
     *  claiming to be a mirror of another. Multiple 
     *  LogicalIdentifiers can be present, usually with different
     *  role attributes. For each role, there should be a 
     *  corresponding relationship described under a
     * RelatedResource,
     *  when applicable.
     *  
     */
    private java.util.ArrayList _logicalIdentifierList;

    /**
     * Information regarding the general curation of the resource
     *  
     */
    private org.astrogrid.registry.beans.resource.CurationType _curation;

    /**
     * List of topics, object types, or other descriptive keywords 
     *  about the resource. 
     *  
     */
    private java.util.ArrayList _subjectList;

    /**
     * Description of the content level or intended audience
     *  
     */
    private java.util.ArrayList _contentLevelList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ResourceType() {
        super();
        setStatus(org.astrogrid.registry.beans.resource.types.ResourceTypeStatusType.valueOf("active"));
        _typeList = new ArrayList();
        _relatedResourceList = new ArrayList();
        _logicalIdentifierList = new ArrayList();
        _subjectList = new ArrayList();
        _contentLevelList = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.ResourceType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addContentLevel
     * 
     * @param vContentLevel
     */
    public void addContentLevel(org.astrogrid.registry.beans.resource.types.ContentLevelType vContentLevel)
        throws java.lang.IndexOutOfBoundsException
    {
        _contentLevelList.add(vContentLevel);
    } //-- void addContentLevel(org.astrogrid.registry.beans.resource.types.ContentLevelType) 

    /**
     * Method addContentLevel
     * 
     * @param index
     * @param vContentLevel
     */
    public void addContentLevel(int index, org.astrogrid.registry.beans.resource.types.ContentLevelType vContentLevel)
        throws java.lang.IndexOutOfBoundsException
    {
        _contentLevelList.add(index, vContentLevel);
    } //-- void addContentLevel(int, org.astrogrid.registry.beans.resource.types.ContentLevelType) 

    /**
     * Method addLogicalIdentifier
     * 
     * @param vLogicalIdentifier
     */
    public void addLogicalIdentifier(org.astrogrid.registry.beans.resource.LogicalIdentifierType vLogicalIdentifier)
        throws java.lang.IndexOutOfBoundsException
    {
        _logicalIdentifierList.add(vLogicalIdentifier);
    } //-- void addLogicalIdentifier(org.astrogrid.registry.beans.resource.LogicalIdentifierType) 

    /**
     * Method addLogicalIdentifier
     * 
     * @param index
     * @param vLogicalIdentifier
     */
    public void addLogicalIdentifier(int index, org.astrogrid.registry.beans.resource.LogicalIdentifierType vLogicalIdentifier)
        throws java.lang.IndexOutOfBoundsException
    {
        _logicalIdentifierList.add(index, vLogicalIdentifier);
    } //-- void addLogicalIdentifier(int, org.astrogrid.registry.beans.resource.LogicalIdentifierType) 

    /**
     * Method addRelatedResource
     * 
     * @param vRelatedResource
     */
    public void addRelatedResource(org.astrogrid.registry.beans.resource.RelatedResourceType vRelatedResource)
        throws java.lang.IndexOutOfBoundsException
    {
        _relatedResourceList.add(vRelatedResource);
    } //-- void addRelatedResource(org.astrogrid.registry.beans.resource.RelatedResourceType) 

    /**
     * Method addRelatedResource
     * 
     * @param index
     * @param vRelatedResource
     */
    public void addRelatedResource(int index, org.astrogrid.registry.beans.resource.RelatedResourceType vRelatedResource)
        throws java.lang.IndexOutOfBoundsException
    {
        _relatedResourceList.add(index, vRelatedResource);
    } //-- void addRelatedResource(int, org.astrogrid.registry.beans.resource.RelatedResourceType) 

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
    public void addType(org.astrogrid.registry.beans.resource.types.CategoryType vType)
        throws java.lang.IndexOutOfBoundsException
    {
        _typeList.add(vType);
    } //-- void addType(org.astrogrid.registry.beans.resource.types.CategoryType) 

    /**
     * Method addType
     * 
     * @param index
     * @param vType
     */
    public void addType(int index, org.astrogrid.registry.beans.resource.types.CategoryType vType)
        throws java.lang.IndexOutOfBoundsException
    {
        _typeList.add(index, vType);
    } //-- void addType(int, org.astrogrid.registry.beans.resource.types.CategoryType) 

    /**
     * Method clearContentLevel
     */
    public void clearContentLevel()
    {
        _contentLevelList.clear();
    } //-- void clearContentLevel() 

    /**
     * Method clearLogicalIdentifier
     */
    public void clearLogicalIdentifier()
    {
        _logicalIdentifierList.clear();
    } //-- void clearLogicalIdentifier() 

    /**
     * Method clearRelatedResource
     */
    public void clearRelatedResource()
    {
        _relatedResourceList.clear();
    } //-- void clearRelatedResource() 

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
     * Method enumerateLogicalIdentifier
     */
    public java.util.Enumeration enumerateLogicalIdentifier()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_logicalIdentifierList.iterator());
    } //-- java.util.Enumeration enumerateLogicalIdentifier() 

    /**
     * Method enumerateRelatedResource
     */
    public java.util.Enumeration enumerateRelatedResource()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_relatedResourceList.iterator());
    } //-- java.util.Enumeration enumerateRelatedResource() 

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
        
        if (obj instanceof ResourceType) {
        
            ResourceType temp = (ResourceType)obj;
            if (this._created != null) {
                if (temp._created == null) return false;
                else if (!(this._created.equals(temp._created))) 
                    return false;
            }
            else if (temp._created != null)
                return false;
            if (this._updated != null) {
                if (temp._updated == null) return false;
                else if (!(this._updated.equals(temp._updated))) 
                    return false;
            }
            else if (temp._updated != null)
                return false;
            if (this._status != null) {
                if (temp._status == null) return false;
                else if (!(this._status.equals(temp._status))) 
                    return false;
            }
            else if (temp._status != null)
                return false;
            if (this._identifier != null) {
                if (temp._identifier == null) return false;
                else if (!(this._identifier.equals(temp._identifier))) 
                    return false;
            }
            else if (temp._identifier != null)
                return false;
            if (this._title != null) {
                if (temp._title == null) return false;
                else if (!(this._title.equals(temp._title))) 
                    return false;
            }
            else if (temp._title != null)
                return false;
            if (this._shortName != null) {
                if (temp._shortName == null) return false;
                else if (!(this._shortName.equals(temp._shortName))) 
                    return false;
            }
            else if (temp._shortName != null)
                return false;
            if (this._summary != null) {
                if (temp._summary == null) return false;
                else if (!(this._summary.equals(temp._summary))) 
                    return false;
            }
            else if (temp._summary != null)
                return false;
            if (this._typeList != null) {
                if (temp._typeList == null) return false;
                else if (!(this._typeList.equals(temp._typeList))) 
                    return false;
            }
            else if (temp._typeList != null)
                return false;
            if (this._relatedResourceList != null) {
                if (temp._relatedResourceList == null) return false;
                else if (!(this._relatedResourceList.equals(temp._relatedResourceList))) 
                    return false;
            }
            else if (temp._relatedResourceList != null)
                return false;
            if (this._logicalIdentifierList != null) {
                if (temp._logicalIdentifierList == null) return false;
                else if (!(this._logicalIdentifierList.equals(temp._logicalIdentifierList))) 
                    return false;
            }
            else if (temp._logicalIdentifierList != null)
                return false;
            if (this._curation != null) {
                if (temp._curation == null) return false;
                else if (!(this._curation.equals(temp._curation))) 
                    return false;
            }
            else if (temp._curation != null)
                return false;
            if (this._subjectList != null) {
                if (temp._subjectList == null) return false;
                else if (!(this._subjectList.equals(temp._subjectList))) 
                    return false;
            }
            else if (temp._subjectList != null)
                return false;
            if (this._contentLevelList != null) {
                if (temp._contentLevelList == null) return false;
                else if (!(this._contentLevelList.equals(temp._contentLevelList))) 
                    return false;
            }
            else if (temp._contentLevelList != null)
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
    public org.astrogrid.registry.beans.resource.types.ContentLevelType getContentLevel(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _contentLevelList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.types.ContentLevelType) _contentLevelList.get(index);
    } //-- org.astrogrid.registry.beans.resource.types.ContentLevelType getContentLevel(int) 

    /**
     * Method getContentLevel
     */
    public org.astrogrid.registry.beans.resource.types.ContentLevelType[] getContentLevel()
    {
        int size = _contentLevelList.size();
        org.astrogrid.registry.beans.resource.types.ContentLevelType[] mArray = new org.astrogrid.registry.beans.resource.types.ContentLevelType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.types.ContentLevelType) _contentLevelList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.types.ContentLevelType[] getContentLevel() 

    /**
     * Method getContentLevelCount
     */
    public int getContentLevelCount()
    {
        return _contentLevelList.size();
    } //-- int getContentLevelCount() 

    /**
     * Returns the value of field 'created'. The field 'created'
     * has the following description: The date this resource
     * metadata description was created.
     *  
     * 
     * @return the value of field 'created'.
     */
    public java.util.Date getCreated()
    {
        return this._created;
    } //-- java.util.Date getCreated() 

    /**
     * Returns the value of field 'curation'. The field 'curation'
     * has the following description: Information regarding the
     * general curation of the resource
     *  
     * 
     * @return the value of field 'curation'.
     */
    public org.astrogrid.registry.beans.resource.CurationType getCuration()
    {
        return this._curation;
    } //-- org.astrogrid.registry.beans.resource.CurationType getCuration() 

    /**
     * Returns the value of field 'identifier'. The field
     * 'identifier' has the following description: Unambiguous
     * reference to the resource conforming to the IVOA
     *  standard for identifiers
     *  
     * 
     * @return the value of field 'identifier'.
     */
    public org.astrogrid.registry.beans.resource.IdentifierType getIdentifier()
    {
        return this._identifier;
    } //-- org.astrogrid.registry.beans.resource.IdentifierType getIdentifier() 

    /**
     * Method getLogicalIdentifier
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.LogicalIdentifierType getLogicalIdentifier(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _logicalIdentifierList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.LogicalIdentifierType) _logicalIdentifierList.get(index);
    } //-- org.astrogrid.registry.beans.resource.LogicalIdentifierType getLogicalIdentifier(int) 

    /**
     * Method getLogicalIdentifier
     */
    public org.astrogrid.registry.beans.resource.LogicalIdentifierType[] getLogicalIdentifier()
    {
        int size = _logicalIdentifierList.size();
        org.astrogrid.registry.beans.resource.LogicalIdentifierType[] mArray = new org.astrogrid.registry.beans.resource.LogicalIdentifierType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.LogicalIdentifierType) _logicalIdentifierList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.LogicalIdentifierType[] getLogicalIdentifier() 

    /**
     * Method getLogicalIdentifierCount
     */
    public int getLogicalIdentifierCount()
    {
        return _logicalIdentifierList.size();
    } //-- int getLogicalIdentifierCount() 

    /**
     * Method getRelatedResource
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.RelatedResourceType getRelatedResource(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _relatedResourceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.RelatedResourceType) _relatedResourceList.get(index);
    } //-- org.astrogrid.registry.beans.resource.RelatedResourceType getRelatedResource(int) 

    /**
     * Method getRelatedResource
     */
    public org.astrogrid.registry.beans.resource.RelatedResourceType[] getRelatedResource()
    {
        int size = _relatedResourceList.size();
        org.astrogrid.registry.beans.resource.RelatedResourceType[] mArray = new org.astrogrid.registry.beans.resource.RelatedResourceType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.RelatedResourceType) _relatedResourceList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.RelatedResourceType[] getRelatedResource() 

    /**
     * Method getRelatedResourceCount
     */
    public int getRelatedResourceCount()
    {
        return _relatedResourceList.size();
    } //-- int getRelatedResourceCount() 

    /**
     * Returns the value of field 'shortName'. The field
     * 'shortName' has the following description: a short name or
     * abbreviation for this resource that applications 
     *  may use to refer to this resource in a compact display. 
     *  
     * 
     * @return the value of field 'shortName'.
     */
    public java.lang.String getShortName()
    {
        return this._shortName;
    } //-- java.lang.String getShortName() 

    /**
     * Returns the value of field 'status'. The field 'status' has
     * the following description: a tag indicating whether this
     * resource is believed to be still
     *  actively maintained.
     *  
     * 
     * @return the value of field 'status'.
     */
    public org.astrogrid.registry.beans.resource.types.ResourceTypeStatusType getStatus()
    {
        return this._status;
    } //-- org.astrogrid.registry.beans.resource.types.ResourceTypeStatusType getStatus() 

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
     * Returns the value of field 'summary'. The field 'summary'
     * has the following description: An informational,
     * human-readable overview of a resource.
     *  
     * 
     * @return the value of field 'summary'.
     */
    public org.astrogrid.registry.beans.resource.SummaryType getSummary()
    {
        return this._summary;
    } //-- org.astrogrid.registry.beans.resource.SummaryType getSummary() 

    /**
     * Returns the value of field 'title'. The field 'title' has
     * the following description: the full name of a resource
     * 
     * @return the value of field 'title'.
     */
    public java.lang.String getTitle()
    {
        return this._title;
    } //-- java.lang.String getTitle() 

    /**
     * Method getType
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.types.CategoryType getType(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _typeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.types.CategoryType) _typeList.get(index);
    } //-- org.astrogrid.registry.beans.resource.types.CategoryType getType(int) 

    /**
     * Method getType
     */
    public org.astrogrid.registry.beans.resource.types.CategoryType[] getType()
    {
        int size = _typeList.size();
        org.astrogrid.registry.beans.resource.types.CategoryType[] mArray = new org.astrogrid.registry.beans.resource.types.CategoryType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.types.CategoryType) _typeList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.types.CategoryType[] getType() 

    /**
     * Method getTypeCount
     */
    public int getTypeCount()
    {
        return _typeList.size();
    } //-- int getTypeCount() 

    /**
     * Returns the value of field 'updated'. The field 'updated'
     * has the following description: The date this resource
     * metadata description was last updated.
     *  
     * 
     * @return the value of field 'updated'.
     */
    public java.util.Date getUpdated()
    {
        return this._updated;
    } //-- java.util.Date getUpdated() 

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
    public boolean removeContentLevel(org.astrogrid.registry.beans.resource.types.ContentLevelType vContentLevel)
    {
        boolean removed = _contentLevelList.remove(vContentLevel);
        return removed;
    } //-- boolean removeContentLevel(org.astrogrid.registry.beans.resource.types.ContentLevelType) 

    /**
     * Method removeLogicalIdentifier
     * 
     * @param vLogicalIdentifier
     */
    public boolean removeLogicalIdentifier(org.astrogrid.registry.beans.resource.LogicalIdentifierType vLogicalIdentifier)
    {
        boolean removed = _logicalIdentifierList.remove(vLogicalIdentifier);
        return removed;
    } //-- boolean removeLogicalIdentifier(org.astrogrid.registry.beans.resource.LogicalIdentifierType) 

    /**
     * Method removeRelatedResource
     * 
     * @param vRelatedResource
     */
    public boolean removeRelatedResource(org.astrogrid.registry.beans.resource.RelatedResourceType vRelatedResource)
    {
        boolean removed = _relatedResourceList.remove(vRelatedResource);
        return removed;
    } //-- boolean removeRelatedResource(org.astrogrid.registry.beans.resource.RelatedResourceType) 

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
    public boolean removeType(org.astrogrid.registry.beans.resource.types.CategoryType vType)
    {
        boolean removed = _typeList.remove(vType);
        return removed;
    } //-- boolean removeType(org.astrogrid.registry.beans.resource.types.CategoryType) 

    /**
     * Method setContentLevel
     * 
     * @param index
     * @param vContentLevel
     */
    public void setContentLevel(int index, org.astrogrid.registry.beans.resource.types.ContentLevelType vContentLevel)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _contentLevelList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _contentLevelList.set(index, vContentLevel);
    } //-- void setContentLevel(int, org.astrogrid.registry.beans.resource.types.ContentLevelType) 

    /**
     * Method setContentLevel
     * 
     * @param contentLevelArray
     */
    public void setContentLevel(org.astrogrid.registry.beans.resource.types.ContentLevelType[] contentLevelArray)
    {
        //-- copy array
        _contentLevelList.clear();
        for (int i = 0; i < contentLevelArray.length; i++) {
            _contentLevelList.add(contentLevelArray[i]);
        }
    } //-- void setContentLevel(org.astrogrid.registry.beans.resource.types.ContentLevelType) 

    /**
     * Sets the value of field 'created'. The field 'created' has
     * the following description: The date this resource metadata
     * description was created.
     *  
     * 
     * @param created the value of field 'created'.
     */
    public void setCreated(java.util.Date created)
    {
        this._created = created;
    } //-- void setCreated(java.util.Date) 

    /**
     * Sets the value of field 'curation'. The field 'curation' has
     * the following description: Information regarding the general
     * curation of the resource
     *  
     * 
     * @param curation the value of field 'curation'.
     */
    public void setCuration(org.astrogrid.registry.beans.resource.CurationType curation)
    {
        this._curation = curation;
    } //-- void setCuration(org.astrogrid.registry.beans.resource.CurationType) 

    /**
     * Sets the value of field 'identifier'. The field 'identifier'
     * has the following description: Unambiguous reference to the
     * resource conforming to the IVOA
     *  standard for identifiers
     *  
     * 
     * @param identifier the value of field 'identifier'.
     */
    public void setIdentifier(org.astrogrid.registry.beans.resource.IdentifierType identifier)
    {
        this._identifier = identifier;
    } //-- void setIdentifier(org.astrogrid.registry.beans.resource.IdentifierType) 

    /**
     * Method setLogicalIdentifier
     * 
     * @param index
     * @param vLogicalIdentifier
     */
    public void setLogicalIdentifier(int index, org.astrogrid.registry.beans.resource.LogicalIdentifierType vLogicalIdentifier)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _logicalIdentifierList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _logicalIdentifierList.set(index, vLogicalIdentifier);
    } //-- void setLogicalIdentifier(int, org.astrogrid.registry.beans.resource.LogicalIdentifierType) 

    /**
     * Method setLogicalIdentifier
     * 
     * @param logicalIdentifierArray
     */
    public void setLogicalIdentifier(org.astrogrid.registry.beans.resource.LogicalIdentifierType[] logicalIdentifierArray)
    {
        //-- copy array
        _logicalIdentifierList.clear();
        for (int i = 0; i < logicalIdentifierArray.length; i++) {
            _logicalIdentifierList.add(logicalIdentifierArray[i]);
        }
    } //-- void setLogicalIdentifier(org.astrogrid.registry.beans.resource.LogicalIdentifierType) 

    /**
     * Method setRelatedResource
     * 
     * @param index
     * @param vRelatedResource
     */
    public void setRelatedResource(int index, org.astrogrid.registry.beans.resource.RelatedResourceType vRelatedResource)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _relatedResourceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _relatedResourceList.set(index, vRelatedResource);
    } //-- void setRelatedResource(int, org.astrogrid.registry.beans.resource.RelatedResourceType) 

    /**
     * Method setRelatedResource
     * 
     * @param relatedResourceArray
     */
    public void setRelatedResource(org.astrogrid.registry.beans.resource.RelatedResourceType[] relatedResourceArray)
    {
        //-- copy array
        _relatedResourceList.clear();
        for (int i = 0; i < relatedResourceArray.length; i++) {
            _relatedResourceList.add(relatedResourceArray[i]);
        }
    } //-- void setRelatedResource(org.astrogrid.registry.beans.resource.RelatedResourceType) 

    /**
     * Sets the value of field 'shortName'. The field 'shortName'
     * has the following description: a short name or abbreviation
     * for this resource that applications 
     *  may use to refer to this resource in a compact display. 
     *  
     * 
     * @param shortName the value of field 'shortName'.
     */
    public void setShortName(java.lang.String shortName)
    {
        this._shortName = shortName;
    } //-- void setShortName(java.lang.String) 

    /**
     * Sets the value of field 'status'. The field 'status' has the
     * following description: a tag indicating whether this
     * resource is believed to be still
     *  actively maintained.
     *  
     * 
     * @param status the value of field 'status'.
     */
    public void setStatus(org.astrogrid.registry.beans.resource.types.ResourceTypeStatusType status)
    {
        this._status = status;
    } //-- void setStatus(org.astrogrid.registry.beans.resource.types.ResourceTypeStatusType) 

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
     * Sets the value of field 'summary'. The field 'summary' has
     * the following description: An informational, human-readable
     * overview of a resource.
     *  
     * 
     * @param summary the value of field 'summary'.
     */
    public void setSummary(org.astrogrid.registry.beans.resource.SummaryType summary)
    {
        this._summary = summary;
    } //-- void setSummary(org.astrogrid.registry.beans.resource.SummaryType) 

    /**
     * Sets the value of field 'title'. The field 'title' has the
     * following description: the full name of a resource
     * 
     * @param title the value of field 'title'.
     */
    public void setTitle(java.lang.String title)
    {
        this._title = title;
    } //-- void setTitle(java.lang.String) 

    /**
     * Method setType
     * 
     * @param index
     * @param vType
     */
    public void setType(int index, org.astrogrid.registry.beans.resource.types.CategoryType vType)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _typeList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _typeList.set(index, vType);
    } //-- void setType(int, org.astrogrid.registry.beans.resource.types.CategoryType) 

    /**
     * Method setType
     * 
     * @param typeArray
     */
    public void setType(org.astrogrid.registry.beans.resource.types.CategoryType[] typeArray)
    {
        //-- copy array
        _typeList.clear();
        for (int i = 0; i < typeArray.length; i++) {
            _typeList.add(typeArray[i]);
        }
    } //-- void setType(org.astrogrid.registry.beans.resource.types.CategoryType) 

    /**
     * Sets the value of field 'updated'. The field 'updated' has
     * the following description: The date this resource metadata
     * description was last updated.
     *  
     * 
     * @param updated the value of field 'updated'.
     */
    public void setUpdated(java.util.Date updated)
    {
        this._updated = updated;
    } //-- void setUpdated(java.util.Date) 

    /**
     * Method unmarshalResourceType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.ResourceType unmarshalResourceType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.ResourceType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.ResourceType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.ResourceType unmarshalResourceType(java.io.Reader) 

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
