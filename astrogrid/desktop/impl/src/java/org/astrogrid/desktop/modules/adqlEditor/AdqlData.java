/* AdqlData.java
 * Created on 02-Nov-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.adqlEditor ;

import java.util.Hashtable;

/**
 * @author jl99
 *
 *
 */
public class AdqlData {
    
    public static String NAMESPACE_0_74 = "http://www.ivoa.net/xml/ADQL/v0.7.4" ;
    public static String NAMESPACE_1_0 = "http://www.ivoa.net/xml/ADQL/v1.0" ;
    
    public static final String DUMMY_ENTRY = "".intern() ;
    public static final String COLUMN_REFERENCE_TYPE = "columnReferenceType" ;
    public static final String SELECT_TYPE = "selectType" ;
    public static final String FROM_TYPE = "fromType" ;
    public static final String JOIN_TABLE_TYPE = "joinTableType" ;
    public static final String ARRAY_OF_FROM_TABLE_TYPE = "ArrayOfFromTableType" ;
    public static final String TABLE_TYPE = "tableType" ;
    public static final String ARCHIVE_TABLE_TYPE = "archiveTableType" ;
    public static final String BINARY_EXPRESSION_TYPE = "binaryExprType" ;
    public static final String UNARY_EXPRESSION_TYPE = "unaryExprType" ;
    public static final String TRIG_FUNCTION_TYPE = "trigonometricFunctionType" ;
    public static final String MATH_FUNCTION_TYPE = "mathFunctionType" ;
    public static final String AGGREGATE_FUNCTION_TYPE = "aggregateFunctionType" ;
    public static final String COMPARISON_TYPE = "comparisonType" ;
    public static final String COMPARISON_PRED_TYPE = "comparisonPredType" ;
    
    public static final String TYPE_ENTRY = "TYPE".intern() ;
    public static final String ELEMENT_ENTRY = "ELEMENT".intern() ;
 
    public static final Hashtable T2D_NAMES ;
    static {
        T2D_NAMES = new Hashtable() ;
        T2D_NAMES.put( "closedExprType", "Bracket" ) ;
        T2D_NAMES.put( "binaryExprType", "Binary Expression" ) ;
        T2D_NAMES.put( "binaryOperatorType", "+ - * /" ) ;
        T2D_NAMES.put( "unaryExprType", "Unary expression" ) ;
        T2D_NAMES.put( "columnReferenceType", "Column" ) ;
//        T2D_NAMES.put( "atomType", "Literal" ) ;
        T2D_NAMES.put( "realType", "Real" ) ;
        T2D_NAMES.put( "integerType", "Integer" ) ;
        T2D_NAMES.put( "stringType", "String" ) ;
        T2D_NAMES.put( "selectionOptionType", "Allow" ) ;
        T2D_NAMES.put( "trigonometricFunctionType", "Trig Function" ) ;
        T2D_NAMES.put( "mathFunctionType", "Maths Function" ) ;
        T2D_NAMES.put( "aggregateFunctionType", "Aggregate Function" ) ;
        T2D_NAMES.put( "userDefinedFunctionType", "User-defined Function" ) ;
        T2D_NAMES.put( "aliasSelectionItemType", "Aliased Expression" ) ;
        T2D_NAMES.put( "allSelectionItemType", "All Columns" ) ;
        T2D_NAMES.put( "comparisonType", "Comparison" ) ;
        T2D_NAMES.put( "archiveTableType", "Archive table" ) ;
        T2D_NAMES.put( "tableType", "Table" ) ;
        T2D_NAMES.put( "joinTableType", "Join Table" ) ;
        T2D_NAMES.put( "includeTableType", "Include Table" ) ;
        T2D_NAMES.put( "dropTableType", "Exclude table" ) ;
        T2D_NAMES.put( "intersectionSearchType", "And" ) ;
        T2D_NAMES.put( "unionSearchType", "Or" ) ;
        T2D_NAMES.put( "xMatchType", "xMatch" ) ;
        T2D_NAMES.put( "likePredType", "Like" ) ;
        T2D_NAMES.put( "notLikePredType", "Not Like" ) ;
        T2D_NAMES.put( "closedSearchType", "Closed" ) ;
        T2D_NAMES.put( "comparisonPredType", "Comparison" ) ;
        T2D_NAMES.put( "betweenPredType", "Between" ) ;
        T2D_NAMES.put( "notBetweenPredType", "Not Between" ) ;
        T2D_NAMES.put( "inverseSearchType", "Not" ) ;
        T2D_NAMES.put( "regionSearchType", "Region" ) ;
        T2D_NAMES.put( "havingType", "Having" ) ;
        T2D_NAMES.put( "groupByType", "Group By" ) ;
        T2D_NAMES.put( "whereType", "Where" ) ;
        T2D_NAMES.put( "fromType", "From" ) ;
        T2D_NAMES.put( "selectionListType", "Items" ) ;
        T2D_NAMES.put( "selectionLimitType", "Top" ) ;
        T2D_NAMES.put( "orderDirectionType", "Ascending / Descending" ) ;
        T2D_NAMES.put( "orderType", "Item" ) ;
        T2D_NAMES.put( "orderOptionType", "Order" ) ;
        T2D_NAMES.put( "orderExpressionType", "Order By" ) ;
        T2D_NAMES.put( "selectType", "Select" ) ;
        T2D_NAMES.put( "intoType", "Into" ) ;  
        T2D_NAMES.put( "inclusiveSearchType", "In" ) ; 
        T2D_NAMES.put( "exclusiveSearchType", "Not In" ) ;
        T2D_NAMES.put( "ArrayOfFromTableType", "Tables" ) ;
        T2D_NAMES.put( "jointTableQualifierType", "Join Qualifier" ) ;
             
        T2D_NAMES.put( "Select", "Select" ) ;
        T2D_NAMES.put( "From", "From" ) ;
        T2D_NAMES.put( "Where", "Where" ) ; 
        T2D_NAMES.put( "Having", "Having" ) ;
        T2D_NAMES.put( "Allow", "All / Distinct" ) ;
        T2D_NAMES.put( "GroupBy", "Group By" ) ;
        T2D_NAMES.put( "OrderBy", "Order By" ) ;
        T2D_NAMES.put( "SelectionList", "Items" ) ;
        T2D_NAMES.put( "InTo", "Into" ) ;
        T2D_NAMES.put( "StartComment", "Start Comment" ) ;
        T2D_NAMES.put( "EndComment", "End Comment" ) ;
        T2D_NAMES.put( "Restrict", "Restrict" ) ;
   
        T2D_NAMES.put( "circleType", "Circle" ) ;
        T2D_NAMES.put( "ellipseType", "Ellipse" ) ;
        T2D_NAMES.put( "smallCircleType", "Small Circle" ) ;
        T2D_NAMES.put( "vertexType", "Vertex" ) ;
        T2D_NAMES.put( "polygonType", "Polygon" ) ;
        T2D_NAMES.put( "sectorType", "Sector" ) ;
        T2D_NAMES.put( "constraintType", "Constraint" ) ;
        T2D_NAMES.put( "convexType", "Convex" ) ;
        T2D_NAMES.put( "convexHullType", "Convex Hull" ) ;
        T2D_NAMES.put( "unionType", "Union" ) ;                 // Careful here
        T2D_NAMES.put( "intersectionType", "Intersection" ) ;   // Careful here
        T2D_NAMES.put( "negationType", "Negation" ) ;
        
        T2D_NAMES.put( "Radius", "Radius" ) ;
        T2D_NAMES.put( "Center", "Center" ) ;
        T2D_NAMES.put( "TableName", "Table Name" ) ;
        T2D_NAMES.put( "Point", "Point" ) ;
        T2D_NAMES.put( "MinorRadius", "Minor Radius" ) ;
        T2D_NAMES.put( "Position", "Vertex Position" ) ;
        T2D_NAMES.put( "PosAngle", "Position Angle" ) ;
        T2D_NAMES.put( "PosAngle1", "Area CCW Included" ) ;        
        T2D_NAMES.put( "PosAngle2", "Area CW Included" ) ; 
        T2D_NAMES.put( "Vector", "Vector" ) ;
        T2D_NAMES.put( "Offset", "Offset" ) ;
        T2D_NAMES.put( "Unit", "Unit" ) ;
        T2D_NAMES.put( "Item", "Literal" ) ;
        T2D_NAMES.put( "Arg", "Literal" ) ;      
        T2D_NAMES.put( "Pattern", "Pattern" ) ; 
        T2D_NAMES.put( "Expression", "Literal" ) ;            
        T2D_NAMES.put( "subQuerySet", "Sub-query" ) ;            
        T2D_NAMES.put( "ConstantListSet", "List" ) ;  
        T2D_NAMES.put( "Params", "Literal" ) ; 
        T2D_NAMES.put( "Name", "Name" ) ; 
        T2D_NAMES.put( "Pole", "Pole" ) ; 
        
    }
    
    public static final Hashtable D2T_NAMES ;
    static {
        D2T_NAMES = new Hashtable() ;
        D2T_NAMES.put( "", "" ) ;
        D2T_NAMES.put( "", "" ) ;
    }
    
    public static final Hashtable UNSUPPORTED_TYPES ;
    static {
        UNSUPPORTED_TYPES = new Hashtable() ;
        UNSUPPORTED_TYPES.put( "joinTableType", TYPE_ENTRY ) ;
        UNSUPPORTED_TYPES.put( "archiveTableType", TYPE_ENTRY ) ;
        UNSUPPORTED_TYPES.put( "aliasSelectionItemType", TYPE_ENTRY ) ;
        UNSUPPORTED_TYPES.put( "exclusiveSearchType", TYPE_ENTRY ) ;
//???      UNSUPPORTED_TYPES.put( "inclusiveSetType", TYPE_ENTRY ) ;
        UNSUPPORTED_TYPES.put( "inclusiveSearchType", TYPE_ENTRY ) ;
        UNSUPPORTED_TYPES.put( "subQuerySet", TYPE_ENTRY ) ;
        UNSUPPORTED_TYPES.put( "constantListSet", TYPE_ENTRY ) ;
        UNSUPPORTED_TYPES.put( "intoType", TYPE_ENTRY ) ;
        
        UNSUPPORTED_TYPES.put( "xMatchType", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES.put( "likePredType", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES.put( "notLikePredType", TYPE_ENTRY ) ;
        UNSUPPORTED_TYPES.put( "regionSearchType", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES.put( "closedSearchType", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES.put( "betweenPredType", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES.put( "notBetweenPredType", TYPE_ENTRY ) ;
        UNSUPPORTED_TYPES.put( "userDefinedFunctionType", TYPE_ENTRY ) ;
        
        UNSUPPORTED_TYPES.put( "StartComment", ELEMENT_ENTRY ) ;
        UNSUPPORTED_TYPES.put( "EndComment", ELEMENT_ENTRY ) ;
        UNSUPPORTED_TYPES.put( "Unit", ELEMENT_ENTRY ) ;    
    }
    
//    public static final Hashtable UNSUPPORTED_TYPES_74 ;
//    static {
//        UNSUPPORTED_TYPES_74 = new Hashtable() ;
//        UNSUPPORTED_TYPES_74.put( "joinTableType", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES_74.put( "archiveTableType", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES_74.put( "aliasSelectionItemType", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES_74.put( "exclusiveSearchType", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES_74.put( "inclusiveSetType", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES_74.put( "inclusiveSearchType", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES_74.put( "subQuerySet", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES_74.put( "constantListSet", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES_74.put( "intoType", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES_74.put( "StartComment", ELEMENT_ENTRY ) ;
//        UNSUPPORTED_TYPES_74.put( "EndComment", ELEMENT_ENTRY ) ;
//        UNSUPPORTED_TYPES_74.put( "Unit", ELEMENT_ENTRY ) ;    
//    }
//    
//    public static final Hashtable UNSUPPORTED_TYPES_10 ;
//    static {
//        UNSUPPORTED_TYPES_10 = new Hashtable() ;
//        UNSUPPORTED_TYPES_10.put( "joinTableType", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES_10.put( "archiveTableType", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES_10.put( "aliasSelectionItemType", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES_10.put( "intoType", TYPE_ENTRY ) ; 
//    }
    
    
    
    
    public static final Hashtable CASCADEABLE ;
    static {
        CASCADEABLE = new Hashtable() ;
        CASCADEABLE.put( "columnReferenceType", DUMMY_ENTRY ) ;
        CASCADEABLE.put( "trigonometricFunctionType", DUMMY_ENTRY ) ;
        CASCADEABLE.put( "tableType", DUMMY_ENTRY ) ;    
        CASCADEABLE.put( "mathFunctionType", DUMMY_ENTRY ) ;  
        CASCADEABLE.put( "aggregateFunctionType", DUMMY_ENTRY ) ;  
        CASCADEABLE.put( "archiveTableType", DUMMY_ENTRY ) ;  
        CASCADEABLE.put( "jointTableQualifierType", DUMMY_ENTRY ) ;   
        CASCADEABLE.put( "binaryExprType", DUMMY_ENTRY ) ;  
        CASCADEABLE.put( "unaryExprType", DUMMY_ENTRY ) ;  
        CASCADEABLE.put( "selectionOptionType", DUMMY_ENTRY ) ;  
        CASCADEABLE.put( "comparisonPredType", DUMMY_ENTRY ) ;  
        CASCADEABLE.put( "orderOptionType", DUMMY_ENTRY ) ;  
    }
    
    public static final Hashtable ENUMERATED_ATTRIBUTES ;
    static {
        ENUMERATED_ATTRIBUTES = new Hashtable() ;
        ENUMERATED_ATTRIBUTES.put( "trigonometricFunctionType" , "trigonometricFunctionNameType" ) ;
        ENUMERATED_ATTRIBUTES.put( "mathFunctionType" , "mathFunctionNameType" ) ;
        ENUMERATED_ATTRIBUTES.put( "aggregateFunctionType" , "aggregateFunctionNameType" ) ;
        ENUMERATED_ATTRIBUTES.put( "binaryExprType" , "binaryOperatorType" ) ;
        ENUMERATED_ATTRIBUTES.put( "unaryExprType" , "unaryOperatorType" ) ;
        ENUMERATED_ATTRIBUTES.put( "selectionOptionType" , "allOrDistinctType" ) ;
        ENUMERATED_ATTRIBUTES.put( "comparisonPredType" , "comparisonType" ) ;
        ENUMERATED_ATTRIBUTES.put( "orderOptionType" , "orderDirectionType" ) ;
//        ENUMERATED_ATTRIBUTES.put( "joinTableType" , "jointTableQualifierType" ) ;  // this is incorrect
    }
    
    
    public static final Hashtable ENUMERATED_ELEMENTS ;
    static {
        ENUMERATED_ELEMENTS = new Hashtable() ;
        ENUMERATED_ELEMENTS.put( "jointTableQualifierType" , "jointTableQualifierType" ) ;
    }
    
    
    public static final Hashtable METADATA_LINK_TABLE ;
    static {
        METADATA_LINK_TABLE = new Hashtable() ;
        METADATA_LINK_TABLE.put( "tableType", DUMMY_ENTRY ) ;
        METADATA_LINK_TABLE.put( "joinTableType", DUMMY_ENTRY ) ;
        METADATA_LINK_TABLE.put( "archiveTableType", DUMMY_ENTRY ) ;
    }
    
    public static final Hashtable METADATA_LINK_COLUMN ;
    static {
        METADATA_LINK_COLUMN = new Hashtable() ;
        METADATA_LINK_COLUMN.put( "columnReferenceType", DUMMY_ENTRY ) ;
    }
    
    
    public static final Hashtable EDITABLE ;
    static {
        EDITABLE = new Hashtable() ;
        EDITABLE.put( "selectionLimitType", "Top" ) ;
        EDITABLE.put( "realType", "Value" ) ;
        EDITABLE.put( "integerType", "Value" ) ;
        EDITABLE.put( "stringType", "Value" ) ;
        EDITABLE.put( "string", DUMMY_ENTRY ) ; // this is the built in type
        EDITABLE.put( "double", DUMMY_ENTRY ) ; // this is the built in type
    }
    
    public static final Hashtable ATTRIBUTE_DEFAULTS ;
    static {
        ATTRIBUTE_DEFAULTS = new Hashtable() ;
        ATTRIBUTE_DEFAULTS.put( "selectionLimitType", "100" ) ;
        ATTRIBUTE_DEFAULTS.put( "realType", "0" ) ;
        ATTRIBUTE_DEFAULTS.put( "integerType", "0" ) ;
        ATTRIBUTE_DEFAULTS.put( "stringType", "" ) ;
    }
    
    public static final Hashtable DERIVED_DEFAULTS ;
    static {
        DERIVED_DEFAULTS = new Hashtable() ;
        DERIVED_DEFAULTS.put( "double2Type", "0 0" ) ;
        DERIVED_DEFAULTS.put( "double3Type", "0 0 0" ) ;
        DERIVED_DEFAULTS.put( "double4Type", "0 0 0 0" ) ;
        DERIVED_DEFAULTS.put( "double5Type", "0 0 0 0 0" ) ;
        DERIVED_DEFAULTS.put( "double6Type", "0 0 0 0 0 0" ) ;
        DERIVED_DEFAULTS.put( "double7Type", "0 0 0 0 0 0 0" ) ;
        DERIVED_DEFAULTS.put( "double8Type", "0 0 0 0 0 0 0 0" ) ;
        DERIVED_DEFAULTS.put( "double9Type", "0 0 0 0 0 0 0 0 0" ) ;
    }
    public static final String NEW_QUERY =
        "<Select xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
        "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
        "        xmlns=\"http://www.ivoa.net/xml/ADQL/v1.0\"" +
        "        xsi:type=\"selectType\"> " +
        "    <SelectionList xsi:type=\"selectionListType\"> " +
        "        <Item xsi:type=\"allSelectionItemType\"/> " +
        "    </SelectionList> " +
        "    <From xsi:type=\"fromType\"> " +
        "    </From> " +
        "    <Where xsi:type=\"whereType\"> " +
        "    </Where> " +
        "</Select>" ;

}
