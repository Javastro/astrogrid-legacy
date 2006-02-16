
package org.astrogrid.desktop.modules.adqlEditor ;

import java.math.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.SchemaProperty; 
import org.apache.xmlbeans.SchemaType; 
import org.apache.xmlbeans.SimpleValue; 
import org.apache.xmlbeans.XmlString;

import javax.xml.namespace.QName;
import javax.swing.tree.DefaultMutableTreeNode ;
import javax.swing.tree.TreeNode;

import org.astrogrid.desktop.modules.system.transformers.AdqlTransformer ;

/**
 * Represents the data for a single node in the XmlTree. This class (known as a
 * "user object" from the JTree perspective) provides a way to get information
 * about the node by essentially wrapping the XmlObject instance that the node
 * represents. The {@link XmlModel}class represents the XML data model to the
 * tree by calling methods of this object.
 */
public final class AdqlEntry extends DefaultMutableTreeNode {
    
    public static final Hashtable HIDE_CHILDREN ; 
    static {
        HIDE_CHILDREN = new Hashtable() ;
        HIDE_CHILDREN.put( "Atom", "Atom" ) ;
    }
    
    public static final String MISSING_VALUE = "{ missing }" ;
    
    public static final Hashtable VALUE_GETTERS ;
    static {
        VALUE_GETTERS = new Hashtable() ;
        VALUE_GETTERS.put( "REAL", "REAL" ) ;
        VALUE_GETTERS.put( "INTEGER", "INTEGER" ) ;
        VALUE_GETTERS.put( "STRING", "STRING" ) ; // Not sure about this one.
    }
    
    private static AdqlTransformer transformer = new AdqlTransformer() ;
    
    private XmlObject[] hiddenChildren = null ;
    
    private static XmlObject root = null ;
    
    public static AdqlEntry newInstance( XmlObject rootObject ) {
        root = rootObject ;
        AdqlEntry rootEntry = newInstance( null, rootObject ) ;
        return rootEntry ;
    }
     
    public static AdqlEntry newInstance( AdqlEntry parent, XmlObject xmlObject ) {
        AdqlEntry entry = buildInstance( xmlObject ) ;    
        if( parent != null ) {
            // Time to check where the position within the xml tree so we
            // can keep the display and the tree in line...
            int index = findRequiredChildIndex( parent.getXmlObject(), xmlObject ) ;
            if( index == -1 )
                parent.add( entry ) ; 
            else
                parent.insert( entry, index ) ;
        }
        return entry ;
    }
    
    public static boolean removeInstance( AdqlEntry parent, AdqlEntry child ) {
        boolean retValue = false ;
        //
        // Simple bit first. Remove the child entry from the mutable tree...
        parent.remove( child ) ;
        //
        // XmlCursor is plain wonderful! 
        // I use it to remove the object from the parent.
        XmlObject co = child.getXmlObject() ;  // child object
        XmlObject po = parent.getXmlObject() ; // parent object
        XmlCursor cursor = po.newCursor() ;
        cursor.toFirstChild() ; // There has to be a first child!
        do {
            if( co == cursor.getObject() ) {
                retValue = cursor.removeXml() ;
//                System.out.println( "cursor.removeXml(): " + retValue ) ;
                break ;
            }
        } while( cursor.toNextSibling() ) ;
        cursor.dispose() ;       
        return retValue ;
    }
    
    
    public static boolean isChildHidingRequired( XmlObject xmlbean ) {
        boolean retValue = false ;
        if(  ( xmlbean.schemaType().isBuiltinType() == false )
             &&
             ( HIDE_CHILDREN.containsKey( AdqlUtils.extractDisplayName( xmlbean ) ) )  ) {
            retValue = true ;
        }
        return retValue ;
    }
      
    private static int findRequiredChildIndex( XmlObject parent, XmlObject child ) {
        int index = 0 ;
        XmlCursor cursor = parent.newCursor() ;
        cursor.toFirstChild() ; // There has to be a first child!
        do {
            if( child == cursor.getObject() ) 
                break ;
            index++ ;
        } while( cursor.toNextSibling() ) ;
        if( !cursor.toNextSibling() ) {
            index = -1 ;
        }
        cursor.dispose() ;
        return index ;
    }
    
    private static AdqlEntry buildInstance( XmlObject xmlObject ) {
        AdqlEntry entry = null ;
        XmlObject[] childArray = xmlObject.selectPath("./*") ;
        if( childArray != null && childArray.length > 0 ) {
            if( isChildHidingRequired( xmlObject ) ) {
                entry = new AdqlEntry( xmlObject, false ) ;
                entry.hiddenChildren = childArray ;
            }
            else {
                entry = new AdqlEntry( xmlObject ) ;
                for( int i=0; i<childArray.length; i++ ) {
                    if( childArray[i].schemaType().isAttributeType() == false )
                        entry.add( buildInstance( childArray[i] ) ) ;
                }
            }                    
        }
        else {
            entry = new AdqlEntry( xmlObject, !isChildHidingRequired( xmlObject ) ) ;
        }
        return entry ;
    }
    
    private boolean expanded = false ;
    private int useableWidth = 0 ;
    
    private AdqlEntry() {}
    
    private AdqlEntry( XmlObject o ) {
        super( o ) ;
    }
    
    private AdqlEntry( XmlObject o, boolean allowsChildren ) {
        super( o, allowsChildren ) ;
    }

    public boolean isChildHidingRequired() {
        return isChildHidingRequired( this.getXmlObject() ) ;
    }
    
    // This is a hodge podge.
    public boolean isBottomLeafEditable() {
        boolean result = false ;
        if( this.getXmlObject().schemaType().isAnonymousType() )
            return false ;
        String name = this.getXmlObject().schemaType().getName().getLocalPart() ;
        // Child hiding may give me problems here.
        // But I dont think it is yet been activated. (Yet!).
        if( this.getXmlObject().schemaType().isSimpleType() 
            ||
            this.isChildHidingRequired() ) {
            result = true ;
        }
        if( AdqlData.EDITABLE.containsKey( name ) )
            result = true ;
        return result ;
    }
    
    public String getDisplayName() {
        //return AdqlUtils.extractDisplayName( this.getXmlObject(), ADQLUtils.DISPLAY_NAME_FILTER )
        return AdqlUtils.extractDisplayName( this.getXmlObject() ) ;
    }
    
    
    public boolean isTableLinked() {
        return (this.getDisplayName().toUpperCase().indexOf( "TABLE" ) != -1) ;
    }
    
    public boolean isColumnLinked() {
        return (this.getDisplayName().toUpperCase().indexOf( "COLUMN" ) != -1) ;
    }
    
    
    /**
     * Gets the number of children of the XML this entry represents.
     * 
     * @return The number of children.
     */
    public int getChildCount() {
        int result = 0 ;
        if( this.children != null ) 
            result = this.children.size() ; ;
        return result ;
    }

    /**
     * Gets the child at <em>index</em> from among the children of the XML
     * this entry represents.
     * 
     * @param index The index number for the child to get.
     * @return An entry representing the child.
     */
    public AdqlEntry getChild(int index)
    {
        AdqlEntry childEntry = (AdqlEntry)this.children.elementAt(index);
        return childEntry;
    }

    /**
     * Gets the children of the XML this entry represents.
     * 
     * @return An entry array representing the children.
     */
    public AdqlEntry[] getChildren() {
        int childCount = getChildCount() ;
        AdqlEntry[] entryChildren = new AdqlEntry[childCount];
        if( childCount > 0 ) {
            Enumeration e = this.children.elements() ;
            for( int i = 0; e.hasMoreElements(); i++ ) {
                entryChildren[i] =  (AdqlEntry)e.nextElement() ;
            }
        }    
        return entryChildren;
    }

    public String toHtml( boolean expanded, boolean leaf, AdqlTree tree ) {
        String displayName = null;
        try {
            displayName = AdqlUtils.extractDisplayName( getXmlObject() ) ;
//            System.out.println( "displayName: " + displayName ) ;
        }
        catch( Exception ex ) {
            ex.printStackTrace() ;
        }
        String displayInfo = null ;
        if( expanded ) {
            displayInfo = displayName ;
            if( displayName.equals( "Comparison") ) {
                displayInfo += (" " + ((SimpleValue)AdqlUtils.get( getXmlObject(), "Comparison" )).getStringValue() ) ;
//                displayInfo += " java etc" ; 
                String value = ((SimpleValue)AdqlUtils.get( getXmlObject(), "Comparison" )).getStringValue() ;
//                System.out.println( "Comparison value: " + value ) ;
            }
            else if( hiddenChildren != null ) {
                displayInfo = extractValue( displayName, getXmlObject() ) ;
            }
            else {
                displayInfo = extractValue( displayName, getXmlObject() ) ;
            }
        }
        else if( hiddenChildren == null ) {
            // Get the ADQL/s representation of this element ...
            displayInfo = transformer.transformDisplayValues( this, tree, expanded, leaf ) ; 
            int index = displayInfo.toUpperCase().indexOf( displayName.toUpperCase() ) ;
            // This is a temporary kludge....
            if( index == -1 ) {
                index = displayInfo.indexOf( '>', "<html>".length() ) ;
                StringBuffer buffer = new StringBuffer( displayInfo.length() + 32 ) ;
                buffer
                	.append( displayInfo.substring( 0, index+1 ) ) 
                	.append( displayName )
                	.append( ' ' ) 
                	.append( displayInfo.substring( index+1 ) ) ;
                displayInfo = buffer.toString() ;
            } 
        }
        else {
            System.out.println( "hiddenChildren! ..." ) ;
            String hcName = AdqlUtils.extractDisplayName( hiddenChildren[0] ) ;
            System.out.println( "hcName: " + hcName ) ;
            String hcValue = extractValue( hcName, getXmlObject() ) ;
            System.out.println( "hcValue: " + hcValue ) ;
            displayInfo = hcValue  ;
        }
        
        if( displayInfo == null || displayInfo.trim().length() == 0 ) {
            displayInfo = "NO NAME" ;
        } 
//        System.out.println( "displayInfo: " + displayInfo ) ;
        return displayInfo ;
    }
    
    public String toText( boolean expanded, AdqlTree tree ) {
        return toString( expanded, tree ) ;
    } 
    /**
     * Returns a name that can be used as a tree node label.
     * 
     * @return The name of the element or attribute this entry represents.
     */
    private String toString( boolean expansionState, AdqlTree tree ) {
        String displayName = null;
        try {
            displayName = AdqlUtils.extractDisplayName( getXmlObject() ) ;
        }
        catch( Exception ex ) {
            System.out.println( "\nException thrown in toString().\nException stack trace follows... " ) ;
            System.out.println( "===== pretty print after exception in toString()... =====" ) ;
            XmlOptions opts = new XmlOptions();
            opts.setSavePrettyPrint();
            opts.setSavePrettyPrintIndent(4);
            System.out.println( root );
            ex.printStackTrace() ;
        }
        String displayInfo = null ;
        if( expansionState ) {
            displayInfo = displayName ;
            if( displayName.equals( "Comparison") ) {
                displayInfo += (" " + ((SimpleValue)AdqlUtils.get( getXmlObject(), "Comparison" )).getStringValue() ) ;
            }
            else if( hiddenChildren != null ) {
                displayInfo = extractValue( displayName, getXmlObject() ) ;
            }
            else {
                displayInfo = extractValue( displayName, getXmlObject() ) ;
            }
        }
        else if( hiddenChildren == null ) {
            String xml = getXmlObject().newCursor().xmlText() ;
            // Get the ADQL/s representation of this element ...
//            displayInfo = transformer.transform( xml ) ;
            if( displayInfo.startsWith( "<html") ) {            
                int fromIndex = displayInfo.indexOf( "<font" ) ;
                fromIndex = displayInfo.indexOf( '>', fromIndex ) ;
                int index = displayInfo.indexOf( displayName, fromIndex ) ;
                if( index == -1 || index > displayName.length() + 9 + fromIndex ) {
                    displayInfo = displayName + ' ' + displayInfo ;
                }
            }
        }
        else {
            displayInfo = AdqlUtils.extractDisplayName( hiddenChildren[0] ) ;
            displayInfo = extractValue( displayInfo, getXmlObject() ) ;
        }
        return  displayInfo.trim() ;
    }
    
    public String toString() {
        String retVal = null ;
        if( isBottomLeafEditable() ) {
            retVal = "Hello" ;
        }
        return retVal ;
    }
    
    private String extractValue( String displayName, XmlObject o ) {
        if( VALUE_GETTERS.containsKey( displayName.toUpperCase() ) ) {
            return displayName + ' ' + (new Atom( o )).formatDisplay() ;
        }
        return displayName ;
    }

   
    public XmlObject getXmlObject() {
//        System.out.println( "getXmlObject..." ) ;
        XmlObject xmlObject = null ;
        Object obj = getUserObject() ;
        if( obj == null ) {
//            System.out.println( "user object is null" ) ;
        }
        else if( obj instanceof XmlObject ) {
            xmlObject = (XmlObject)obj ;
//            System.out.println( "OK" ) ;
        }
        else if( obj instanceof String ) {
            xmlObject = XmlString.Factory.newInstance() ;
            ((XmlString)xmlObject).setStringValue( (String)obj ) ;
//            System.out.println( "Just created transient XmlString object" ) ;
        }
        else {
//            System.out.println( "Unknown: " + obj.getClass().getName() )  ;
            if( obj instanceof AdqlEntry ) {
//                System.out.println( "whoops" ) ;
            }
        }
        return xmlObject ;
    }
    
    
    public SchemaProperty[] getElementProperties() {
        return  getXmlObject().schemaType().getElementProperties() ;
    }
    
    public SchemaType getSchemaType() {
        return getXmlObject().schemaType() ;
    }
    
    
    public void setExpanded( boolean expanded ) {
        this.expanded = expanded ;
    }
    
    public class Atom {
        private XmlObject atomType ;
         
        public Atom( XmlObject atomType ) {
            this.atomType = atomType ;         
        }
        
        public String getUnits() {
            XmlObject retValObj = null ;
            String retVal = null ;
            boolean isSet = AdqlUtils.isSet( this.atomType, "Unit" ) ;
            if( isSet ) {
                retValObj = AdqlUtils.get( this.atomType, "Unit" ) ;
                if( retValObj != null )
                    retVal = ((SimpleValue)retValObj).getStringValue() ;
                if( retVal != null )
                    retVal = retVal.trim() ;
            }  
            return retVal ;
        }
        
        public String getValue()  {
            XmlObject retValObj = null ;
            String retVal = MISSING_VALUE ; // default to missing
            XmlObject o = AdqlUtils.get( this.atomType, "Literal" ) ;
            if( o != null ) {
                retValObj = AdqlUtils.get( o, "Value" ) ;
                if( retValObj != null )
                    retVal = ((SimpleValue)retValObj).getStringValue() ;
                if( retVal != null )
                    retVal = retVal.trim() ;
                if( retVal == null || retVal.length() == 0 )
                    retVal =  MISSING_VALUE ;
            } 
            return retVal ;
        }
        
        public String formatDisplay() {
            StringBuffer buffer = new StringBuffer() ;
            String 
            	units = this.getUnits(),
            	value = this.getValue() ;          
            if( units != null ) {
                buffer.append( "Units: " ).append( units ).append( ", " ) ;
            } 
            buffer.append( "Value: " ).append( value ) ;
            return buffer.toString() ;
        }
    }
  
    /**
     * @return Returns the expanded.
     */
    public boolean isExpanded() {
        return expanded;
    }
    public int getUseableWidth() {
        return useableWidth;
    }
    public void setUseableWidth(int useableWidth) {
        this.useableWidth = useableWidth;
    }
}