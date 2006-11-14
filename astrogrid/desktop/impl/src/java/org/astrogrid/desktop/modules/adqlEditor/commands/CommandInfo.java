/* CommandInfo.java
 * Created on 16-Mar-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 **/
package org.astrogrid.desktop.modules.adqlEditor.commands;

import org.apache.xmlbeans.SchemaProperty;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.astrogrid.desktop.modules.adqlEditor.nodes.AdqlNode;
;

/**
 * @author jl99
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface CommandInfo {
    
    public String getParentDisplayName() ;
    public String getChildDisplayName() ;

    public SchemaProperty getChildElement();

    public AdqlNode getParentEntry();
    public AdqlNode getChildEntry() ;
    
    public XmlObject getParentObject() ;
    public XmlObject getChildObject() ;
    
    public String getChildElementName();
    public String getParentElementName() ;

    public int getChildMinOccurs();
    public int getChildMaxOccurs();
    
    public SchemaType getChildType() ;
    public int getChildIndex() ;
    
    public boolean isChildEnabled();

    public boolean isParentSuitablePasteTargetFor( XmlObject clipboardObject ) ;

    public boolean isChildOptionalSingleton();

    public boolean isChildSupportedType();

    public boolean isChildMandatorySingleton();

    public boolean isChildHeldInArray();

    public boolean isInsertableIntoArray( int noElementsToInsert ) ;

    public boolean isChildCascadeable();

    public boolean isChildTableLinked();

    public boolean isChildColumnLinked();

    public boolean isChildDrivenByEnumeratedAttribute();
    
    public boolean isChildDrivenByEnumeratedElement() ;
    
    public boolean isChildInPatternContext() ;

    public Integer exchangeInEditStore( AdqlNode in, AdqlNode out ) ;
    
    public Integer getChildToken() ;
    
    public Integer getParentToken() ;

    public void exchangeInEditStore( Integer token, AdqlNode in ) ;
}