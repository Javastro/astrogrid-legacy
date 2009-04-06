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

import java.util.HashSet;
import java.util.Hashtable;
import java.util.regex.Pattern;

/**
 * @author jl99
 *
 *
 */
public class AdqlData {
    
    /**
     * This pattern is suitable for vetting so-called Regular Identifiers...
     * Names within a database system that must begin with an alphabetic character
     * and then continue with alphabetic characters, digits or the two special
     * characters _ and $. (ie: underscore and dollar sign).
     * 
     * This range may not be adequate in the medium to long term.
     * 
     */
    public static Pattern REGULAR_IDENTIFIER  = Pattern.compile( "\\p{Alpha}{1}[\\p{Alpha}\\p{Digit}_$]*" ) ;
    
    public static String NAMESPACE_0_74 = "http://www.ivoa.net/xml/ADQL/v0.7.4" ;
    public static String NAMESPACE_1_0 = "http://www.ivoa.net/xml/ADQL/v1.0" ;
    public static final String PI_QB_REGISTRY_RESOURCES = "qb-registry-resources" ;
    public static final String PI_ADQL_SCHEMA_VERSION_TAG = "ag-adql-schema-version" ;
    public static final String PI_ADQL_SCHEMA_VERSION_VALUE = "v1.0a2" ;
    
    
    public static final String ELEMENT_FROM_TABLE_TYPE = "fromTableType" ;
    public static final String DUMMY_ENTRY = "".intern() ;
    public static final String COLUMN_REFERENCE_TYPE = "columnReferenceType" ;
    public static final String SELECT_TYPE = "selectType" ;
    public static final String FROM_TYPE = "fromType" ;
    public static final String JOIN_TABLE_TYPE = "joinTableType" ;
    public static final String ARRAY_OF_FROM_TABLE_TYPE = "ArrayOfFromTableType" ;
    public static final String TABLE_TYPE = "tableType" ;
    public static final String ARCHIVE_TABLE_TYPE = "archiveTableType" ;
    public static final String BINARY_EXPRESSION_TYPE = "binaryExprType" ;
    public static final String BINARY_OPERATOR_TYPE = "binaryOperatorType" ;
    public static final String UNARY_EXPRESSION_TYPE = "unaryExprType" ;
    public static final String TRIG_FUNCTION_TYPE = "trigonometricFunctionType" ;
    public static final String MATH_FUNCTION_TYPE = "mathFunctionType" ;
    public static final String AGGREGATE_FUNCTION_TYPE = "aggregateFunctionType" ;
    public static final String COMPARISON_TYPE = "comparisonType" ;
    public static final String COMPARISON_PRED_TYPE = "comparisonPredType" ;
    public static final String REAL_TYPE = "realType" ;
    public static final String INTEGER_TYPE = "integerType" ;
    public static final String STRING_TYPE = "stringType" ;
    public static final String LITERAL_TYPE = "literalType" ;
    public static final String PATTERN_ELEMENT_NAME = "Pattern" ;
    public static final String ATOM_TYPE = "atomType" ;
    public static final String ALL_SELECTION_ITEM_TYPE = "allSelectionItemType" ;
    public static final String SELECTION_LIST_TYPE = "selectionListType" ;
    public static final String SELECTION_OPTION_TYPE = "selectionOptionType" ;
    public static final String INTERSECTION_SEARCH_TYPE = "intersectionSearchType" ;
    public static final String UNION_SEARCH_TYPE = "unionSearchType" ;
    public static final String ALIAS_SELECTION_ITEM_TYPE = "aliasSelectionItemType" ;
    
    
    // NB: The literal is almost certainly a typo in the schema and should read
    // "joinTableQualifierType". Make any future changes/corrections easier by 
    // using this definition.
    public static final String JOIN_TABLE_QUALIFIER_TYPE = "jointTableQualifierType" ;
    
    
    public static final String TYPE_ENTRY = "TYPE".intern() ;
    public static final String ELEMENT_ENTRY = "ELEMENT".intern() ;
 
    public static final Hashtable<String,String> T2D_NAMES ;
    static {
        T2D_NAMES = new Hashtable<String,String>() ;
        T2D_NAMES.put( "closedExprType", "Bracket" ) ;
        T2D_NAMES.put( BINARY_EXPRESSION_TYPE, "Binary Expression" ) ;
        T2D_NAMES.put( BINARY_OPERATOR_TYPE, "+ - * /" ) ;
        T2D_NAMES.put( UNARY_EXPRESSION_TYPE, "Unary expression" ) ;
        T2D_NAMES.put( COLUMN_REFERENCE_TYPE, "Column" ) ;
        // Experiment
        T2D_NAMES.put( ATOM_TYPE, "Literal" ) ;
//        T2D_NAMES.put( REAL_TYPE, "Literal" ) ;
//        T2D_NAMES.put( INTEGER_TYPE, "Literal" ) ;
//        T2D_NAMES.put( STRING_TYPE, "Literal" ) ;
        T2D_NAMES.put( REAL_TYPE, "Real" ) ;
        T2D_NAMES.put( INTEGER_TYPE, "Integer" ) ;
        T2D_NAMES.put( STRING_TYPE, "String" ) ;
        // End of experiment
        
        T2D_NAMES.put( SELECTION_OPTION_TYPE, "Allow" ) ;
        T2D_NAMES.put( TRIG_FUNCTION_TYPE, "Trig Function" ) ;
        T2D_NAMES.put( MATH_FUNCTION_TYPE, "Maths Function" ) ;
        T2D_NAMES.put( AGGREGATE_FUNCTION_TYPE, "Aggregate Function" ) ;
        T2D_NAMES.put( "userDefinedFunctionType", "User-defined Function" ) ;
        T2D_NAMES.put( ALIAS_SELECTION_ITEM_TYPE, "Aliased Expression" ) ;
        T2D_NAMES.put( ALL_SELECTION_ITEM_TYPE, "All Columns" ) ;
        T2D_NAMES.put( "comparisonType", "Comparison" ) ;
        T2D_NAMES.put( "archiveTableType", "Archive table" ) ;
        T2D_NAMES.put( "tableType", "Table" ) ;
        T2D_NAMES.put( JOIN_TABLE_TYPE, "Join Table" ) ;
        T2D_NAMES.put( "includeTableType", "Include Table" ) ;
        T2D_NAMES.put( "dropTableType", "Exclude table" ) ;
        T2D_NAMES.put( INTERSECTION_SEARCH_TYPE, "And" ) ;
        T2D_NAMES.put( UNION_SEARCH_TYPE, "Or" ) ;
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
        T2D_NAMES.put( SELECT_TYPE, "Select" ) ;
        T2D_NAMES.put( "intoType", "Into" ) ;  
        T2D_NAMES.put( "inclusiveSearchType", "In" ) ; 
        T2D_NAMES.put( "exclusiveSearchType", "Not In" ) ;
        T2D_NAMES.put( "ArrayOfFromTableType", "Tables" ) ;
        T2D_NAMES.put( JOIN_TABLE_QUALIFIER_TYPE, "Join Qualifier" ) ;
             
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
    
    public static final Hashtable<String,String> D2T_NAMES ;
    static {
        D2T_NAMES = new Hashtable<String,String>() ;
        D2T_NAMES.put( "", "" ) ;
        D2T_NAMES.put( "", "" ) ;
    }
    
    public static final Hashtable<String,String> UNSUPPORTED_TYPES ;
    static {
        UNSUPPORTED_TYPES = new Hashtable<String,String>() ;
//        UNSUPPORTED_TYPES.put( JOIN_TABLE_TYPE, TYPE_ENTRY ) ;
        UNSUPPORTED_TYPES.put( "archiveTableType", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES.put( ALIAS_SELECTION_ITEM_TYPE, TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES.put( "exclusiveSearchType", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES.put( "inclusiveSearchType", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES.put( "subQuerySet", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES.put( "constantListSet", TYPE_ENTRY ) ;
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
//        UNSUPPORTED_TYPES_74.put( JOIN_TABLE_TYPE, TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES_74.put( "archiveTableType", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES_74.put( ALIAS_SELECTION_ITEM_TYPE, TYPE_ENTRY ) ;
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
//        UNSUPPORTED_TYPES_10.put( JOIN_TABLE_TYPE, TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES_10.put( "archiveTableType", TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES_10.put( ALIAS_SELECTION_ITEM_TYPE, TYPE_ENTRY ) ;
//        UNSUPPORTED_TYPES_10.put( "intoType", TYPE_ENTRY ) ; 
//    }
    
    
    public static final Hashtable<String,String> CASCADEABLE ;
    static {
        CASCADEABLE = new Hashtable<String,String>() ;
        CASCADEABLE.put( COLUMN_REFERENCE_TYPE, DUMMY_ENTRY ) ;
        CASCADEABLE.put( TRIG_FUNCTION_TYPE, DUMMY_ENTRY ) ;
        CASCADEABLE.put( "tableType", DUMMY_ENTRY ) ;    
        CASCADEABLE.put( MATH_FUNCTION_TYPE, DUMMY_ENTRY ) ;  
        CASCADEABLE.put( AGGREGATE_FUNCTION_TYPE, DUMMY_ENTRY ) ;  
        CASCADEABLE.put( "archiveTableType", DUMMY_ENTRY ) ;  
 //       CASCADEABLE.put( JOIN_TABLE_QUALIFIER_TYPE, DUMMY_ENTRY ) ; 
        CASCADEABLE.put( "joinTableType", DUMMY_ENTRY ) ;  
        CASCADEABLE.put( BINARY_EXPRESSION_TYPE, DUMMY_ENTRY ) ;  
        CASCADEABLE.put( UNARY_EXPRESSION_TYPE, DUMMY_ENTRY ) ;  
        CASCADEABLE.put( SELECTION_OPTION_TYPE, DUMMY_ENTRY ) ;  
        CASCADEABLE.put( "comparisonPredType", DUMMY_ENTRY ) ;  
        CASCADEABLE.put( "orderOptionType", DUMMY_ENTRY ) ;  
    }
    
    public static final Hashtable<String,String> ENUMERATED_ATTRIBUTES ;
    static {
        ENUMERATED_ATTRIBUTES = new Hashtable<String,String>() ;
        ENUMERATED_ATTRIBUTES.put( TRIG_FUNCTION_TYPE , "trigonometricFunctionNameType" ) ;
        ENUMERATED_ATTRIBUTES.put( MATH_FUNCTION_TYPE , "mathFunctionNameType" ) ;
        ENUMERATED_ATTRIBUTES.put( AGGREGATE_FUNCTION_TYPE , "aggregateFunctionNameType" ) ;
        ENUMERATED_ATTRIBUTES.put( BINARY_EXPRESSION_TYPE , BINARY_OPERATOR_TYPE ) ;
        ENUMERATED_ATTRIBUTES.put( UNARY_EXPRESSION_TYPE , "unaryOperatorType" ) ;
        ENUMERATED_ATTRIBUTES.put( SELECTION_OPTION_TYPE , "allOrDistinctType" ) ;
        ENUMERATED_ATTRIBUTES.put( "comparisonPredType" , "comparisonType" ) ;
        ENUMERATED_ATTRIBUTES.put( "orderOptionType" , "orderDirectionType" ) ;
    }
    
    
    public static final Hashtable<String, String> ENUMERATED_ELEMENTS ;
    static {
        ENUMERATED_ELEMENTS = new Hashtable<String, String>() ;
        ENUMERATED_ELEMENTS.put( JOIN_TABLE_TYPE , JOIN_TABLE_QUALIFIER_TYPE ) ;
    }
    
    // Enumerated values which for some obscure reason or reasons
    // we do not wish to be exposed. Better here and visible so they
    // are easier to track.  
    // (1) CROSS is for join tables. A cross join should not possess
    //     a predicate. But the schema says the predicate is mandatory.
    //     So, for the moment we need to suppress cross joins.
    // (2) FULL_OUTER is also for join tables. Unfortunately, full
    //     outer joins are not so straight forward in MySQL. Not impossible,
    //     but not straightforward. Might be difficult to manage with a 
    //     simple XSLT translation. Might need programmable input via XmlBeans
    //     to achieve.
    //
    public static final Hashtable<String,String> ENUM_FILTERED_VALUES ;
    static {
        ENUM_FILTERED_VALUES = new Hashtable<String,String>() ;
        ENUM_FILTERED_VALUES.put( "CROSS", DUMMY_ENTRY ) ;
        ENUM_FILTERED_VALUES.put( "FULL_OUTER", DUMMY_ENTRY ) ;
    }
    
    public static final Hashtable<String,String[]> ENUM_SYNONYMS ;
    static {
        ENUM_SYNONYMS = new Hashtable<String,String[]>() ;
        ENUM_SYNONYMS.put( "<>", new String[]{ "<>", "!="  } ) ;
        ENUM_SYNONYMS.put( "!=", new String[]{ "<>", "!="  } ) ;
    }
    
    // An experiment where we need to prevent an AdqlNode being formed.
    // At the moment these are not context sensitive.
    // Also, HidingNode (abstract class) does the same thing with a blunderbuss...
    // It prevents all its children from forming nodes.
    // The important one in the following is JoinTableQualifierType, which so
    // far is our only enumerated element.
    public static final Hashtable<String, String> NON_NODE_FORMING ;
    static {
        NON_NODE_FORMING = new Hashtable<String, String>() ;
        NON_NODE_FORMING.put( JOIN_TABLE_QUALIFIER_TYPE , DUMMY_ENTRY ) ;
    }
    
    
    public static final Hashtable<String,String> METADATA_LINK_TABLE ;
    static {
        METADATA_LINK_TABLE = new Hashtable<String,String>() ;
        METADATA_LINK_TABLE.put( "tableType", DUMMY_ENTRY ) ;
//        METADATA_LINK_TABLE.put( JOIN_TABLE_TYPE, DUMMY_ENTRY ) ;
        METADATA_LINK_TABLE.put( "archiveTableType", DUMMY_ENTRY ) ;
    }
    
    public static final Hashtable<String, String> METADATA_LINK_COLUMN ;
    static {
        METADATA_LINK_COLUMN = new Hashtable<String, String>() ;
        METADATA_LINK_COLUMN.put( COLUMN_REFERENCE_TYPE, DUMMY_ENTRY ) ;
    }
    
    
    public static final Hashtable<String,Object> EDITABLE_ATTRIBUTES ; // value is sometimes String[] sometimes not.
    static {
        EDITABLE_ATTRIBUTES = new Hashtable<String,Object>() ;
        EDITABLE_ATTRIBUTES.put( "selectionLimitType", new String[] { "Top" } ) ;
        EDITABLE_ATTRIBUTES.put( REAL_TYPE, new String[] { "Value" } ) ;
        EDITABLE_ATTRIBUTES.put( INTEGER_TYPE, new String[] { "Value" } ) ;
        EDITABLE_ATTRIBUTES.put( STRING_TYPE, new String[] { "Value" } ) ;
        EDITABLE_ATTRIBUTES.put( "comparisonPredType", new String[] { "Comparison" } ) ;    
        EDITABLE_ATTRIBUTES.put( AGGREGATE_FUNCTION_TYPE, new String[] { "Name" } ) ;
        EDITABLE_ATTRIBUTES.put( TRIG_FUNCTION_TYPE, new String[] { "Name" } ) ;
        EDITABLE_ATTRIBUTES.put( MATH_FUNCTION_TYPE, new String[] { "Name" } ) ;
        EDITABLE_ATTRIBUTES.put( BINARY_EXPRESSION_TYPE, new String[] { "Oper" } ) ;
        EDITABLE_ATTRIBUTES.put( UNARY_EXPRESSION_TYPE, new String[] { "Oper" } ) ;
        EDITABLE_ATTRIBUTES.put( SELECTION_OPTION_TYPE, new String[] { "Option" } ) ;
        EDITABLE_ATTRIBUTES.put( "orderOptionType", new String[] { "Direction" } ) ; 
        EDITABLE_ATTRIBUTES.put( COLUMN_REFERENCE_TYPE, new String[] { "Table", "Name"  } ) ;
        EDITABLE_ATTRIBUTES.put( "tableType", new String[] { "Name", "Alias"  } ) ;
//        EDITABLE.put( ATOM_TYPE, DUMMY_ENTRY ) ;  // experiment     
        EDITABLE_ATTRIBUTES.put( "string", DUMMY_ENTRY ) ; // this is the built in type
        EDITABLE_ATTRIBUTES.put( "double", DUMMY_ENTRY ) ; // this is the built in type
    }
    
    public static final Hashtable<String, String[]> EDITABLE_ELEMENTS ;
    static {
        EDITABLE_ELEMENTS = new Hashtable<String, String[]>() ;
        EDITABLE_ELEMENTS.put( JOIN_TABLE_TYPE, new String[] { "Qualifier" } ) ; 
    }
    
    public static final Hashtable<String, String[]> CROSS_VALIDATION ;
    static {
        CROSS_VALIDATION = new Hashtable<String,String[]>() ;
        CROSS_VALIDATION.put( "tableType" , new String[] { "Alias" } ) ;
    }
    
    
    
    public static final Hashtable<String,String> ATTRIBUTE_DEFAULTS ;
    static {
        ATTRIBUTE_DEFAULTS = new Hashtable<String,String>() ;
        ATTRIBUTE_DEFAULTS.put( "selectionLimitType", "100" ) ;
        ATTRIBUTE_DEFAULTS.put( REAL_TYPE, "0" ) ;
        ATTRIBUTE_DEFAULTS.put( INTEGER_TYPE, "0" ) ;
        ATTRIBUTE_DEFAULTS.put( STRING_TYPE, "" ) ;
    }
    
    public static final Hashtable<String, Integer[]> IMPOSED_CARDINIALITIES ;
    static {
        IMPOSED_CARDINIALITIES = new Hashtable<String, Integer[]>() ;
        IMPOSED_CARDINIALITIES.put( ELEMENT_FROM_TABLE_TYPE, new Integer[]{ new Integer(2), new Integer(2) } ) ;
    }
    
    public static final Hashtable<String,Integer[]> FUNCTION_CARDINALITIES ;
    static {
        FUNCTION_CARDINALITIES = new Hashtable<String,Integer[]>() ;
        FUNCTION_CARDINALITIES.put( "SIN", new Integer[]{ new Integer(1), new Integer(1) } ) ;
        FUNCTION_CARDINALITIES.put( "COS", new Integer[]{ new Integer(1), new Integer(1) } ) ;
        FUNCTION_CARDINALITIES.put( "TAN", new Integer[]{ new Integer(1), new Integer(1) } ) ;
        FUNCTION_CARDINALITIES.put( "COT", new Integer[]{ new Integer(1), new Integer(1) } ) ;
        FUNCTION_CARDINALITIES.put( "ASIN", new Integer[]{ new Integer(1), new Integer(1) } ) ;
        FUNCTION_CARDINALITIES.put( "ACOS", new Integer[]{ new Integer(1), new Integer(1) } ) ;
        FUNCTION_CARDINALITIES.put( "ATAN", new Integer[]{ new Integer(1), new Integer(1) } ) ;
        FUNCTION_CARDINALITIES.put( "ATAN2", new Integer[]{ new Integer(2), new Integer(2) } ) ;
        FUNCTION_CARDINALITIES.put( "ABS", new Integer[]{ new Integer(1), new Integer(1) } ) ;
        FUNCTION_CARDINALITIES.put( "CEILING", new Integer[]{ new Integer(1), new Integer(1) } ) ;
        FUNCTION_CARDINALITIES.put( "DEGREES", new Integer[]{ new Integer(1), new Integer(1) } ) ;
        FUNCTION_CARDINALITIES.put( "EXP", new Integer[]{ new Integer(1), new Integer(1) } ) ;
        FUNCTION_CARDINALITIES.put( "FLOOR", new Integer[]{ new Integer(1), new Integer(1) } ) ;
        FUNCTION_CARDINALITIES.put( "LOG", new Integer[]{ new Integer(1), new Integer(1) } ) ;
        FUNCTION_CARDINALITIES.put( "PI", new Integer[]{ new Integer(0), new Integer(0) } ) ;
        FUNCTION_CARDINALITIES.put( "POWER", new Integer[]{ new Integer(2), new Integer(2) } ) ;
        FUNCTION_CARDINALITIES.put( "RADIANS", new Integer[]{ new Integer(1), new Integer(1) } ) ;
        FUNCTION_CARDINALITIES.put( "SQRT", new Integer[]{ new Integer(1), new Integer(1) } ) ;
        FUNCTION_CARDINALITIES.put( "SQUARE", new Integer[]{ new Integer(1), new Integer(1) } ) ;
        FUNCTION_CARDINALITIES.put( "LOG10", new Integer[]{ new Integer(1), new Integer(1) } ) ;
        FUNCTION_CARDINALITIES.put( "RAND", new Integer[]{ new Integer(0), new Integer(1) } ) ;
        FUNCTION_CARDINALITIES.put( "ROUND", new Integer[]{ new Integer(1), new Integer(2) } ) ;
        FUNCTION_CARDINALITIES.put( "TRUNCATE", new Integer[]{ new Integer(1), new Integer(2) } ) ;
        FUNCTION_CARDINALITIES.put( "AVG", new Integer[]{ new Integer(1), new Integer(1) } ) ;
        FUNCTION_CARDINALITIES.put( "MIN", new Integer[]{ new Integer(1), new Integer(1) } ) ;
        FUNCTION_CARDINALITIES.put( "MAX", new Integer[]{ new Integer(1), new Integer(1) } ) ;
        FUNCTION_CARDINALITIES.put( "SUM", new Integer[]{ new Integer(1), new Integer(1) } ) ;
        FUNCTION_CARDINALITIES.put( "COUNT", new Integer[]{ new Integer(1), new Integer(1) } ) ;
    }
  
    
    public static final Hashtable<String,String> DERIVED_DEFAULTS ;
    static {
        DERIVED_DEFAULTS = new Hashtable<String,String>() ;
        DERIVED_DEFAULTS.put( "double2Type", "0 0" ) ;
        DERIVED_DEFAULTS.put( "double3Type", "0 0 0" ) ;
        DERIVED_DEFAULTS.put( "double4Type", "0 0 0 0" ) ;
        DERIVED_DEFAULTS.put( "double5Type", "0 0 0 0 0" ) ;
        DERIVED_DEFAULTS.put( "double6Type", "0 0 0 0 0 0" ) ;
        DERIVED_DEFAULTS.put( "double7Type", "0 0 0 0 0 0 0" ) ;
        DERIVED_DEFAULTS.put( "double8Type", "0 0 0 0 0 0 0 0" ) ;
        DERIVED_DEFAULTS.put( "double9Type", "0 0 0 0 0 0 0 0 0" ) ;
        
//        DERIVED_DEFAULTS.put( JOIN_TABLE_QUALIFIER_TYPE, "INNER" ) ;
    }
    
   
    // Note: DEC no longer present!
    private static String RESERVED_WORDS = 
           "ABSOLUTE | ACTION | ADD | ALL " +
         "| ALLOCATE | ALTER | AND | ANY | ARE | AS | ASC | ASSERTION | AT | AUTHORIZATION | AVG | BEGIN " +
         "| BETWEEN | BIT | BIT_LENGTH | BOTH | BY | CASCADE | CASCADED | CASE | CAST | CATALOG | CHAR " +
         "| CHARACTER | CHAR_LENGTH | CHARACTER_LENGTH | CHECK | CLOSE | COALESCE | COLLATE | COLLATION " +
         "| COLUMN | COMMIT | CONNECT | CONNECTION | CONSTRAINT | CONSTRAINTS | CONTINUE | CONVERT | CORRESPONDING " +
         "| COUNT | CREATE | CROSS | CURRENT | CURRENT_DATE | CURRENT_TIME | CURRENT_TIMESTAMP | CURRENT_USER " +
         "| CURSOR | DATE | DAY | DEALLOCATE | DECIMAL | DECLARE | DEFAULT | DEFERRABLE | DEFERRED | DELETE " +
         "| DESC | DESCRIBE | DESCRIPTOR | DIAGNOSTICS | DISCONNECT | DISTINCT | DOMAIN | DOUBLE | DROP | ELSE " +
         "| END | END-EXEC | ESCAPE | EXCEPT | EXCEPTION | EXEC | EXECUTE | EXISTS" +
         "| EXTERNAL | EXTRACT | FALSE | FETCH | FIRST | FLOAT | FOR | FOREIGN | FOUND | FROM | FULL | GET | GLOBAL " +
         "| GO | GOTO | GRANT | GROUP | HAVING | HOUR | IDENTITY | IMMEDIATE | IN | INDICATOR | INITIALLY | INNER " +
         "| INPUT | INSENSITIVE | INSERT | INT | INTEGER | INTERSECT | INTERVAL | INTO | IS | ISOLATION | JOIN | KEY " +
         "| LANGUAGE | LAST | LEADING | LEFT | LEVEL | LIKE | LOCAL | LOWER" +
         "| MATCH | MAX | MIN | MINUTE | MODULE | MONTH | NAMES | NATIONAL | NATURAL | NCHAR | NEXT | NO | NOT " +
         "| NULL | NULLIF | NUMERIC | OCTET_LENGTH | OF | ON | ONLY | OPEN | OPTION | OR | ORDER | OUTER | OUTPUT " +
         "| OVERLAPS | PAD | PARTIAL | POSITION | PRECISION | PREPARE | PRESERVE | PRIMARY | PRIOR | PRIVILEGES " +
         "| PROCEDURE | PUBLIC | READ | REAL | REFERENCES | RELATIVE | RESTRICT | REVOKE | RIGHT | ROLLBACK | ROWS " +
         "| SCHEMA | SCROLL | SECOND | SECTION | SELECT | SESSION | SESSION_USER | SET | SIZE | SMALLINT | SOME " +
         "| SPACE | SQL | SQLCODE | SQLERROR | SQLSTATE | SUBSTRING | SUM | SYSTEM_USER | TABLE | TEMPORARY | THEN " +
         "| TIME | TIMESTAMP | TIMEZONE_HOUR | TIMEZONE_MINUTE | TO | TRAILING | TRANSACTION | TRANSLATE " +
         "| TRANSLATION | TRIM | TRUE | UNION | UNIQUE | UNKNOWN | UPDATE | UPPER | USAGE | USER | USING | VALUE " +
         "| VALUES | VARCHAR | VARYING | VIEW | WHEN | WHENEVER | WHERE | WITH | WORK | WRITE | YEAR | ZONE" ;
  

    public static final HashSet<String> ADQL_RESERVED_WORDS ;
    static {
        String[] reservedWords = RESERVED_WORDS.split( " | " ) ;
        ADQL_RESERVED_WORDS = new HashSet<String>( reservedWords.length ) ;
        for( int i=0; i<reservedWords.length; i++ ) {
            ADQL_RESERVED_WORDS.add( reservedWords[i] ) ;
        }
        RESERVED_WORDS = null ;
        reservedWords = null ;
    }
      
//    public static final String NEW_QUERY =
//        "<Select xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
//        "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
//        "        xmlns=\"http://www.ivoa.net/xml/ADQL/v1.0\"" +
//        "        xsi:type=\"selectType\"> " +
//        "    <Allow xsi:type=\"selectionOptionType\" Option=\"DISTINCT\" /> " +
//        "    <Restrict xsi:type=\"selectionLimitType\" Top=\"100\" /> " +
//        "    <SelectionList xsi:type=\"selectionListType\"> " +
//        "        <Item xsi:type=\"allSelectionItemType\"/> " +
//        "    </SelectionList> " +
//        "    <From xsi:type=\"fromType\"> " +
//        "    </From> " +
//        "    <Where xsi:type=\"whereType\"> " +
//        "    </Where> " +
//        "</Select>" ;
    
    public static final String DUMMY_TABLE_NAME = "Replace_with_correct_table_name" ;
//    public static final String DUMMY_TABLE_NAME = "\"Enter a table name here\"" ;
    
    public static final String NEW_QUERY =
        "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
        "<?" + PI_ADQL_SCHEMA_VERSION_TAG + " " + PI_ADQL_SCHEMA_VERSION_VALUE + " ?>" +
        "<Select xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " +
        "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
        "        xmlns=\"http://www.ivoa.net/xml/ADQL/v1.0\"" +
        "        xsi:type=\"selectType\"> " +    
        "    <SelectionList xsi:type=\"selectionListType\"> " +
        "        <Item xsi:type=\"allSelectionItemType\"/> " +
        "    </SelectionList> " +
        "    <From xsi:type=\"fromType\"> " +
        "        <Table Name=\"" + DUMMY_TABLE_NAME + "\" xsi:type=\"tableType\"/> " +
        "    </From> " +
//        "    <Where xsi:type=\"whereType\"> " +
//        "    </Where> " +
        "</Select>" ;

}
