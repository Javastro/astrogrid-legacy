package org.astrogrid.adql ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.StringReader;
import java.util.HashSet ;
import java.util.HashMap ;
import java.util.Iterator ;
import java.util.ArrayList ;
import java.util.ListIterator ;
import org.apache.xmlbeans.XmlOptions ;
import org.astrogrid.adql.beans.*; 
import org.w3c.dom.Node ;
import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlCursor;

//import org.astrogrid.acr.astrogrid.TableBean;//import org.astrogrid.acr.astrogrid.DatabaseBean;//import org.astrogrid.acr.ivoa.resource.Catalog;//import org.astrogrid.acr.ivoa.resource.DataCollection;

public class AdqlCompiler {
	
	private static Log log = LogFactory.getLog( AdqlCompiler.class ) ;
	   
    private static final boolean DETAILED_DEBUG_PRINT_ENABLED = false ;
    
	private StringBuffer logIndent = null ;
	
	public static final String DUPLICATE_TABLE_ALIAS =
	     "Query contains duplicated table alias: " ;
	public static final String TABLE_ALIAS_CLASH =
	     "Query contains alias with same name as a table: " ;
	public static final String NONEXISTENT_ALIAS =
	     "Query contains a column reference with unknown table or table alias: " ;
	public static final String DUPLICATE_EXPRESSION_ALIAS =
	     "Query contains duplicated expression alias: " ;
	public static final String UNRECOGNIZED_FRAGMENT =
	     "Unrecognized fragment of ADQL: " ;
	public static final String UNEXPECTED_REMAINDER =     
	     "Unexpected remaining characters found: " ;
	     
	public static final String SELECT_ELEMENT = "Select" ;
	public static final String SELECTION_ELEMENT = "selection" ;
	public static final String ALLOW_ELEMENT = "Allow" ;
	public static final String RESTRICT_ELEMENT = "Restrict" ;
	public static final String SELECTION_LIST_ELEMENT = "SelectionList" ;
	public static final String FROM_ELEMENT = "From" ;
	public static final String WHERE_ELEMENT = "Where" ;
	public static final String GROUPBY_ELEMENT = "GroupBy" ;
	public static final String HAVING_ELEMENT = "Having" ;
	public static final String ORDERBY_ELEMENT = "OrderBy" ;
	public static final String COLUMN_ELEMENT = "Column" ;
	public static final String ITEM_ELEMENT = "Item" ;
	public static final String SET_ELEMENT = "Set" ;
	public static final String CONDITION_ELEMENT = "Condition" ;
	public static final String TABLE_ELEMENT = "Table" ;
	public static final String ARG_ELEMENT = "Arg" ;
	public static final String PATTERN_ELEMENT = "Pattern" ;
	public static final String EXPRESSION_ELEMENT = "Expression" ;
	public static final String ORDER_ELEMENT = "Order" ;
	public static final String FROM_TABLE_TYPE_ELEMENT = "fromTableType" ;
	public static final String TABLES_ELEMENT = "Tables" ;
	public static final String LITERAL_ELEMENT = "Literal" ;
    public static final String JOINSPECIFICATION_ELEMENT = "JoinSpecification" ;
		
    public static final String AGGREGATE_FUNCTION_TYPE = AggregateFunctionType.type.getShortJavaName() ;
	public static final String INCLUSIVE_SEARCH_TYPE = InclusiveSearchType.type.getShortJavaName() ;	
	public static final String EXCLUSIVE_SEARCH_TYPE = ExclusiveSearchType.type.getShortJavaName() ;
	public static final String ORDER_TYPE = OrderType.type.getShortJavaName() ;
	public static final String ORDER_OPTION_TYPE = OrderOptionType.type.getShortJavaName() ;	
	public static final String SUB_QUERY_SET_TYPE = SubQuerySet.type.getShortJavaName() ;
	public static final String TABLE_TYPE = TableType.type.getShortJavaName() ;
	public static final String JOIN_TABLE_TYPE = JoinTableType.type.getShortJavaName() ;
	public static final String ARRAY_OF_FROM_TABLE_TYPE = ArrayOfFromTableType.type.getShortJavaName() ;
	public static final String ALIAS_SELECTION_ITEM_TYPE = AliasSelectionItemType.type.getShortJavaName() ;
    
    private int prettyPrintIndent = 2 ;
    
    private int numberForUniqueID = 0 ;
    
//    public static HashMap SchemaPrefixes = new HashMap() ;
//    static {
//        SchemaPrefixes.put( "urn:astrogrid:schema:ADQL:v2.0", "adql" ) ;
//    }
//    public static HashMap ImplicitNamespaces = new HashMap() ;
//    static {
//        ImplicitNamespaces.put( "adql", "urn:astrogrid:schema:ADQL:v2.0" ) ;
////        ImplicitNamespaces.put( "urn", "urn:astrogrid:schema:ADQL:v2.0" ) ;
//    }
//    
//    public static HashMap SubstituteNamespaces = new HashMap() ;
//    static {
//        SubstituteNamespaces.put( "urn:astrogrid:schema:ADQL:v2.0", "http://www.org.astrogrid/schema/adql" ) ;
//    }                              
		      
    public static final int[] SELECTION_LIST_SKIP_TO = { 
    	AdqlStoXConstants.FROM,
    	AdqlStoXConstants.WHERE,
    	AdqlStoXConstants.GROUPBY,
    	AdqlStoXConstants.HAVING,
    	AdqlStoXConstants.ORDERBY 
    } ;
		      
    public static final int[] WHERE_SKIP_TO = { 
    	AdqlStoXConstants.GROUPBY,
    	AdqlStoXConstants.HAVING,
    	AdqlStoXConstants.ORDERBY 
    } ;
    
    public static final int[] FROM_SKIP_TO = { 
    	AdqlStoXConstants.WHERE,
    	AdqlStoXConstants.GROUPBY,
    	AdqlStoXConstants.HAVING,
    	AdqlStoXConstants.ORDERBY 
    } ;
    
     public static final int[] TABLE_REF_SKIP_TO = { 
     	AdqlStoXConstants.COMMA,
    	AdqlStoXConstants.WHERE,
    	AdqlStoXConstants.GROUPBY,
    	AdqlStoXConstants.HAVING,
    	AdqlStoXConstants.ORDERBY 
    } ;
     
     
     /**
      * Enables the compiler to be run from the command line, though a script
      * will probably make it easier. In this mode, you can...
      * <p>
      * (1) use a pipe to pass a complete query, or <br/>
      * (2) pass a file path to a query as the first argument, or <br/>
      * (3) pass a complete query as the first argument. <br/>
      * <p>
      * The input must be an ADQL/s query passed in one of the above ways.
      * The output is either a query compiled in xml (ADQL/x) in pretty print
      * to stdout or error messages to stderr.
      * <p>
      * A suitable script, which obeys the following pseudo code,
      * should come with the distribution.
      * <blockquote><pre>
      * Establish CLASSPATH to resolve dependencies<br/>
      * 
      * If number of args > 0
      *    java org.astrogrid.adql.AdqlCompiler args
      * Else
      *    java org.astrogrid.adql.AdqlCompiler
      * </pre></blockquote>   
      * This mode is not intended as an interactive conversation. If this is 
      * desired, see org.astrogrid.adql.Interactive
      * 
      */
     public static void main( String args[] ) {
                            
         if( args.length > 1 ) {
             System.err.println( "Too many input arguments." ) ;
         }
         else {
             String query ;
             AdqlCompiler compiler = null ;
             if( args.length == 0 ) {
                 System.err.println( "No input argument. Trying stdin." ) ;
                 query = getQueryFromStdin() ;
                 StringReader reader = new StringReader( query ) ;
                 compiler = new AdqlCompiler( reader ) ;
             } 
             else {
                 query = args[0].trim() ; 
                 compiler = getFileBasedCompiler( query ) ; 
                 if( compiler == null && query.substring(0,6).equalsIgnoreCase( "SELECT" ) ) {
                     compiler = new AdqlCompiler( new StringReader( query ) ) ;
                 }                
             }
             
             if( compiler == null ) {
                 System.err.println( "Cannot resolve input argument either to a file or a query." ) ;
             }
             else {
                 try {
                     System.out.println( compiler.compileToXmlText() + '\n' ) ;
                 }
                 catch( AdqlException aex ) {
                     System.err.println( "Following errors reported: " ) ;
                     String[] messages = aex.getMessages() ;
                     for( int i=0; i<messages.length; i++ ) {
                         System.err.println( messages[i] ) ;
                     }
                 }
                 catch( Exception ex ) {
                     System.err.println( "Possible internal compiler error: " ) ;
                     ex.printStackTrace( System.err ) ;
                 }
             }
                      
         }
         
     } // end main( String[] )
     
     
     private static AdqlCompiler getFileBasedCompiler( String path ) {
         AdqlCompiler compiler = null ;
         try {
             compiler = new AdqlCompiler( new FileReader( new File( path ) ) ) ;
         }
         catch( FileNotFoundException fnfex ) {
             ; 
         }
         return compiler ;
     }
     
     private static String getQueryFromStdin() {
         StringBuffer buffer = new StringBuffer( 1024 ) ;
         try {
             int ch = System.in.read() ;
             while( ch != -1 ) {
                 buffer.append( (char)ch ) ;
                 if( ch == ';' )
                     break ;
                 ch = System.in.read() ;
             }
             if( buffer.charAt(  buffer.length()-1 ) != ';' ) {
                 buffer.append( ';' ) ;
             }
         }
         catch( Exception iox ) {
             System.err.println( "Error reading stdin: \n" + iox.getLocalizedMessage() ) ;
         }        
         return buffer.toString() ;
     }
     
          
     public AdqlCompiler( java.io.InputStream stream ) {
         this.parser = new AdqlStoX( stream ) ;
         initParserFields() ;
     }
     
     public AdqlCompiler( java.io.InputStream stream, String encoding ) {
         this.parser = new AdqlStoX( stream, encoding ) ;
         initParserFields() ;
     }
     
     public AdqlCompiler( java.io.Reader stream ) {
         this.parser = new AdqlStoX( stream ) ;
         initParserFields() ;
     }
     
     public AdqlCompiler( AdqlStoXTokenManager tm ) {
         this.parser = new AdqlStoX( tm ) ;
         initParserFields() ;
     }
     
     public void ReInit( java.io.InputStream stream ) {
         ReInit( stream, null ) ;
      }
         
     public void ReInit( java.io.InputStream stream, String encoding ) {
         this.resetIndent() ;
         this.parser.ReInit( stream, encoding ) ;  
         initParserFields() ;
     }
     
     public void ReInit( java.io.Reader stream ) {
         this.resetIndent() ;
         this.parser.ReInit( stream ) ;
         initParserFields() ;
     }
     
     public void ReInit(AdqlStoXTokenManager tm) {
         this.resetIndent() ;
         this.parser.ReInit( tm ) ;
         initParserFields() ;
     }
     
     private void initParserFields() {
         if( parser.compiler == null ) {
             parser.compiler = this ;
         }
         if( parser.tracker == null ) {
            parser.tracker = new Tracker() ;
        }
        else {
            parser.tracker.Reinit() ;
        }   
        if( parser.comments == null ) {
            parser.comments = new HashSet() ;
        }
        else {
            parser.comments.clear() ;
        }
        parser.currentLinkedElementList = null ;
        this.numberForUniqueID = 0 ;
     }
    
    private AdqlStoX parser ;
	private StringBuffer uBuffer = new StringBuffer() ;    
    
	public SelectDocument exec() throws AdqlException {
        if( log.isTraceEnabled() ) enterTrace( "exec()" ) ; 
        SelectDocument selectDoc = SelectDocument.Factory.newInstance() ; 
		try {           
			parser.query_specification_A() ;
			checkForRemainingSource() ;	
            AST_Select selectNode = (AST_Select)parser.jjtree.rootNode() ;
            selectNode.buildXmlTree( selectDoc.addNewSelect() ) ;
            checkForTrailingComment( selectDoc ) ;     
            if( selectNode.isCommentPresent() ) {
                selectNode.writeComment( selectDoc ) ;
            }
			if( DETAILED_DEBUG_PRINT_ENABLED ) {
				XmlOptions opts = new XmlOptions();
		        opts.setSaveOuter() ;
		        opts.setSaveAggressiveNamespaces() ;
		        opts.setSavePrettyPrint() ;
		        opts.setSavePrettyPrintIndent( prettyPrintIndent ) ;
                              
                // Create an error listener.
                ArrayList errorList = new ArrayList() ;
                opts.setErrorListener( errorList ) ;
                
                // Validate the XML.
                boolean isValid = selectDoc.validate(opts);
                
                log.debug( "selectDoc validity is " + isValid 
                         + "\nCompilation before cross validation produced...\n" 
				         + selectDoc.xmlText(opts) ) ;
			}
			checkTableAliases( selectDoc ) ;
			checkExpressionAliases( selectDoc ) ; 
			if( parser.tracker.numberOfErrors() > 0 ) {
	    		throw new AdqlException( parser.tracker ) ;
	    	}
			return selectDoc ;
		} 
		catch ( ParseException pex ) {
			parser.tracker.setError( pex ) ;
	    	throw new AdqlException( parser.tracker ) ;
		} 
		finally {
			if( log.isDebugEnabled() ) {
			     logReportOnErrors( selectDoc ) ;
			}
            if( log.isTraceEnabled() ) exitTrace( "exec()" ) ;
		}
	} 
	
	private void logReportOnErrors( XmlObject xmlObject ) {
		 ArrayList list = parser.tracker.getErrors() ;
	     if( list.size() > 0 ) {
	     	 StringBuffer buffer = new StringBuffer() ;
	     	 buffer.append( "Errors encountered..." ) ;
             Iterator it = list.listIterator() ;	                 
             int i = 1 ;
             while( it.hasNext() ) {
                  Tracker.Error e = (Tracker.Error)it.next() ;
                  buffer
                     .append( "\nError No " )
                     .append( i )
                     .append( ":\n   " ) 
                     .append( e.getShortMessage() ) 
                     .append( "\nat position:\n   " ) 
                     .append( e.toPosition() ) ;
                  i++ ;
             }
             log.debug( buffer.toString() ) ;
	     }
         if( xmlObject != null ) {
             XmlOptions opts = new XmlOptions();
                opts.setSaveOuter() ;
                opts.setSaveAggressiveNamespaces() ;
                opts.setSavePrettyPrint() ;
                opts.setSavePrettyPrintIndent( prettyPrintIndent ) ;
                log.debug( "Compilation produced: " 
                         + xmlObject.xmlText(opts) ) ;
         }
	}

	
	public SelectDocument compileToXmlBeans() throws AdqlException { 
		return exec() ;
	}
	
	public String compileToXmlText( boolean prettyPrint ) throws AdqlException {
		XmlOptions opts = new XmlOptions();
		opts.setSaveOuter() ;
//        opts.setSaveSuggestedPrefixes( SchemaPrefixes ) ;
        opts.setSaveNamespacesFirst() ;
        opts.setSaveAggressiveNamespaces() ;
		if( prettyPrint ) {		
		   opts.setSavePrettyPrint() ;
		   opts.setSavePrettyPrintIndent( prettyPrintIndent ) ; 
		}
		return exec().xmlText(opts) ; 
	}
	
	public String compileToXmlText() throws AdqlException {
		return compileToXmlText( true ) ;
	}
	
	public Node compileToXmlDom() throws AdqlException {
		return exec().getDomNode() ;
	}
	
	public String compileFragmentToXmlText( String contextPath
	                                      , boolean prettyPrint ) throws AdqlException {
		XmlOptions opts = new XmlOptions();
	    opts.setSaveOuter() ;
        opts.setSaveAggressiveNamespaces() ;
//        opts.setSaveSuggestedPrefixes( SchemaPrefixes ) ;
		if( prettyPrint ) {
		   opts.setSavePrettyPrint() ;
		   opts.setSavePrettyPrintIndent( prettyPrintIndent ) ;
		}
		return execFragment( contextPath ).xmlText(opts) ; 
	}
	
	public String compileFragmentToXmlText( String contextPath ) throws AdqlException {
		return compileFragmentToXmlText( contextPath, true ) ; 
	}
	
	public XmlObject compileFragmentToXmlBean( String contextPath ) throws AdqlException {
		return execFragment( contextPath ) ;
	}
    
    public XmlObject execFragment( String contextPath, String[] headerAndTrailerComments ) throws AdqlException {
        if( log.isTraceEnabled() ) enterTrace( "execFragment(String, String[] )" ) ;
        XmlObject xo = execFragment( contextPath ) ;
        if( headerAndTrailerComments != null ) {
            if( headerAndTrailerComments.length >= 2 ) {
                if( ((SimpleNode)parser.jjtree.rootNode()).isCommentPresent() ) {
                    headerAndTrailerComments[0] = ((SimpleNode)parser.jjtree.rootNode()).firstToken.specialToken.image ;                   
                }
                String trailingComment = getTrailingComment() ;
                if( trailingComment != null ) 
                    headerAndTrailerComments[1] = trailingComment ;
            } 
        }
        if( log.isTraceEnabled() ) exitTrace( "execFragment(String, String[] )" ) ;
        return xo ;       
    }
    
    public void writeHeaderAndTrailerComments( XmlObject parent, XmlObject child, String[] comments ) {
        if( log.isTraceEnabled() ) enterTrace( "writeHeaderAndTrailerComments()" ) ;
            XmlCursor cursor = null ;
            XmlObject xo = null ;
            try {
                cursor = parent.newCursor() ;
                cursor.toFirstChild() ;
                do {
                    
                    if( cursor.isStart() ) {
                        xo = cursor.getObject() ;
                        if( xo == child ) {
                            cursor.insertComment( SimpleNode.prepareComment( comments[0] ) ) ; 
                            cursor.toEndToken() ;
                            break ;
                        }
                    }
                } while( cursor.toNextToken() != XmlCursor.TokenType.NONE ) ; 
                 
                cursor = parent.newCursor() ;
                cursor.toFirstChild() ;
                do {
                    
                    if( cursor.isStart() ) {
                        xo = cursor.getObject() ;
                        if( xo == child ) {
                            cursor.toEndToken() ;
                            cursor.toNextToken() ;
                            if( cursor.isStart() || cursor.isEnddoc() ) {
                                cursor.insertComment( SimpleNode.prepareComment( comments[1] ) ) ;
                            }
                            break ;
                        }
                    }
                } while( cursor.toNextToken() != XmlCursor.TokenType.NONE ) ; 
            
            }
            catch( Exception ex ) {
                log.debug( "Problem encountered whilst writing a comment.", ex ) ;
            }
            finally {
                if( cursor != null )
                    cursor.dispose();
                if( log.isTraceEnabled() ) exitTrace( "writeHeaderAndTrailerComments()" ) ;
            }

    }
	
	// NB: It looks as if attributes also need to be compiled at this level.
	// For example, in the expression "sin( a.ra ) as x", x is compiled as
	// an attribute within the type aliasSelectionItemType.
	// Maybe OK. I think the smallest amount to compile is an element.
	public XmlObject execFragment( String contextPath ) throws AdqlException {
        if( log.isTraceEnabled() ) enterTrace( "execFragment(String contextPath)" ) ;
		try {		
		    ContextPath cp = new ContextPath( contextPath ) ;
		    ContextPath.Element child = cp.getChild() ;
		    String childName = child.getName() ;
			ContextPath.Element parent = null ;
			String parentName = null ;
			
			if( log.isDebugEnabled() ) {
			   log.debug( "cp.getChild().getName(): " + cp.getChild().getName() ) ;
			   log.debug( "cp.getChild().getType(): " + cp.getChild().getType() ) ;
			   log.debug( "cp.getParent().getName(): " + cp.getParent().getName() ) ;
			   log.debug( "cp.getParent().getType(): " + cp.getParent().getType() ) ;
			}
		
//. <xs:element name="Select" type="tns:selectType">		
		
//.	<xs:element name="Allow" type="tns:selectionOptionType" minOccurs="0"/>//.	<xs:element name="Restrict" type="tns:selectionLimitType" minOccurs="0"/>//.	<xs:element name="SelectionList" type="tns:selectionListType"/>//	<xs:element name="InTo" type="tns:intoType" minOccurs="0"/>//.	<xs:element name="From" type="tns:fromType" minOccurs="0"/>//.	<xs:element name="Where" type="tns:whereType" minOccurs="0"/>//.	<xs:element name="GroupBy" type="tns:groupByType" minOccurs="0"/>//.	<xs:element name="Having" type="tns:havingType" minOccurs="0"/>//.	<xs:element name="OrderBy" type="tns:orderExpressionType" minOccurs="0"/>//	<xs:element name="StartComment" type="xs:string" minOccurs="0"/>//	<xs:element name="EndComment" type="xs:string" minOccurs="0"/>

//. <xs:element name="Condition" type="tns:searchType" minOccurs="2" maxOccurs="2"/>
//. <xs:element name="Column" type="tns:columnReferenceType" maxOccurs="unbounded"/>

//.	<xs:element name="Item" type="tns:orderType" maxOccurs="unbounded"/>
//. <xs:element name="Item" type="tns:literalType" maxOccurs="unbounded"/>
//. <xs:element name="Item" type="tns:selectionItemType" maxOccurs="unbounded"/>

//. <xs:element name="Arg" type="tns:scalarExpressionType"/>
//. <xs:element name="Arg" type="tns:selectionItemType" minOccurs="0" maxOccurs="unbounded"/>

//.?  <xs:element name="Literal" type="tns:literalType"/>
// Do we support units in the current version?...
//?	<xs:element name="Unit" type="xs:string" minOccurs="0"/>

//. <xs:element name="Expression" type="tns:scalarExpressionType"/>
				
// These 3 from xMatch:
// <xs:element name="Table" type="tns:xMatchTableAliasType" minOccurs="2" maxOccurs="unbounded"/>
// <xs:element name="Nature" type="tns:comparisonType"/>
// <xs:element name="Sigma" type="tns:numberType"/>		

//. <xs:element name="Pattern" type="tns:atomType"/>

// aliasSelectionItemType. Check. Probably not required.
//. exclusiveSearchType: NOT IN
//. orderType
//. inclusiveSearchType: IN
//. <xs:element name="Expression" type="tns:scalarExpressionType"/>

// These two probably need experimentation to distinguish between them...
// <xs:element name="Set" type="tns:inclusionSetType"/>
//. <xs:element name="selection" type="tns:selectType"/>

// <xs:element name="Region" type="reg:regionType"/>

//. <xs:element name="Table" type="tns:fromTableType" maxOccurs="unbounded"/>

// This from InTo:
// <xs:element name="TableName" type="xs:string"/>

// This from OrderBy:
//. <xs:element name="Expression" type="tns:scalarExpressionType"/>
//. <xs:element name="Order" type="tns:orderOptionType" minOccurs="0"/>

// This from user defined function:
// <xs:element name="Name" type="xs:string"/>
// <xs:element name="Params" type="tns:scalarExpressionType" minOccurs="0" maxOccurs="unbounded"/>

// This from join table:
// <xs:element name="Qualifier" type="tns:jointTableQualifierType"/>
//. <xs:element name="Tables" type="tns:ArrayOfFromTableType"/>		
//. <xs:element name="Condition" type="tns:comparisonPredType"/>
// <xs:element name="fromTableType" type="tns:fromTableType" nillable="true" maxOccurs="unbounded"/>
			// Unit test done.		
			if( childName.equalsIgnoreCase( SELECT_ELEMENT ) ) {
			    fQuerySpecification() ;
			} 
			// This check is probably being pedantic...
			// Unit test done.
			else if( childName.equalsIgnoreCase( SELECTION_ELEMENT ) ) {
			    parent = cp.getParent() ;
			    if( parent.getType().equalsIgnoreCase( SUB_QUERY_SET_TYPE ) ) {
			        fSubQuerySpecification() ;
			    }
			    else {
			        throw new ParseException( UNRECOGNIZED_FRAGMENT + contextPath ) ;	
			    }
			}
			else {
			    // Unit test done.
			    if( childName.equalsIgnoreCase( ALLOW_ELEMENT ) ) {
			        fSetQuantifier() ;
			    }
			    // Unit test done.
			    else if( childName.equalsIgnoreCase( RESTRICT_ELEMENT ) ) {
			        fSetLimit() ;
			    }
			    // Unit test done.
			    else if( childName.equalsIgnoreCase( SELECTION_LIST_ELEMENT ) ) {
			        fSelectionList() ;
			    }
			    // Unit test done.
			    else if( childName.equalsIgnoreCase( FROM_ELEMENT ) ) {
			        fFromClause() ;
			    }
			    // Unit test done.
			    else if( childName.equalsIgnoreCase( WHERE_ELEMENT ) ) {
			        fWhereClause() ;
			    }
			    // Unit test done.
			    else if( childName.equalsIgnoreCase( GROUPBY_ELEMENT ) ) {
			        fGroupBy() ;
			    }
			    // Unit test done.
			    else if( childName.equalsIgnoreCase( HAVING_ELEMENT ) ) {
			        fHaving() ;
			    }
			    // Unit test done.
			    else if( childName.equalsIgnoreCase( ORDERBY_ELEMENT ) ) {
			        fOrderBy() ;
			    }
			    // Unit test done.
			    else if( childName.equalsIgnoreCase( COLUMN_ELEMENT ) ) {
			        fColumnReference() ;
			    }
			    else if( childName.equalsIgnoreCase( ITEM_ELEMENT ) ) {
			        parentName = cp.getParent().getName() ;
			        // Unit test done.
			        if( parentName.equalsIgnoreCase( SELECTION_LIST_ELEMENT ) ) {
			            fDerivedColumn() ;
			        }
			        // Unit test done.
			        else if( parentName.equalsIgnoreCase( ORDERBY_ELEMENT ) ) {
			            fSortSpecification() ;
			        }
			        // Unit test done.
			        else if( parentName.equalsIgnoreCase( SET_ELEMENT ) ) {
			            fInValueListConstant() ;
			        }
			    }
			    else if( childName.equalsIgnoreCase( JOINSPECIFICATION_ELEMENT ) ) {
			        parentName = cp.getParent().getName() ;
			        // Join table comparison ...
			        // Unit test done.
			        if( parentName.equalsIgnoreCase( TABLE_ELEMENT ) ) {
			            fJoinSpecification() ;
			        }
			        // Unit test done.
			        else {
			            fSearchCondition() ;
			        }
			    }
			    else if( childName.equalsIgnoreCase( ARG_ELEMENT ) ) {
			        parent = cp.getParent() ;
			        // Unit test done.
			        if( parent.getType().equalsIgnoreCase( AGGREGATE_FUNCTION_TYPE ) ) {
			            parser.set_function_specification_part_fragment() ;
			            ComparisonPredType compPred = ComparisonPredType.Factory.newInstance() ;
			            AST_SetFunctionSpecificationPartFragment sfsNode = (AST_SetFunctionSpecificationPartFragment)parser.jjtree.rootNode() ;
			            sfsNode.buildXmlTree( compPred.addNewArg() ) ;
			        }
			        // Unit test done.
			        else {
			            parser.value_expression_A() ;
			            ComparisonPredType compPred = ComparisonPredType.Factory.newInstance() ;
			            AST_ValueExpression valueExpressionNode = (AST_ValueExpression)parser.jjtree.rootNode() ;
			            valueExpressionNode.buildXmlTree( compPred.addNewArg() ) ;
			        }
			    }
			    // Unit test done.
			    else if( childName.equalsIgnoreCase( PATTERN_ELEMENT ) ) {
			        parser.pattern_A() ;
			        LikePredType lpt = LikePredType.Factory.newInstance() ;
			        AST_CharacterStringLiteral patternNode = (AST_CharacterStringLiteral)parser.jjtree.rootNode() ;
			        patternNode.buildXmlTree( lpt.addNewPattern() ) ;
			    }
			    // Unit test done.
			    else if( childName.equalsIgnoreCase( TABLE_ELEMENT ) ) {
			        parser.table_reference_A() ;
			        FromType from = FromType.Factory.newInstance() ;
			        AST_Table tableNode = (AST_Table)parser.jjtree.rootNode() ;
			        tableNode.buildXmlTree( from.addNewTable() ) ;
			    }
			    // Adjustment made for:
			    // /Select[@type='selectType']/SelectionList[@type='selectionListType']/Item[@type='aliasSelectionItemType']/Expression[@type='mathFunctionType']
			    else if( childName.equalsIgnoreCase( EXPRESSION_ELEMENT ) ) {
			        parent = cp.getParent() ;
			        // Unit test done 
			        if( parent.getType().equalsIgnoreCase( INCLUSIVE_SEARCH_TYPE )
			                ||
			                // Unit test done 
			                parent.getType().equalsIgnoreCase( EXCLUSIVE_SEARCH_TYPE ) 
			                ||
			                // Unit test done.
			                parent.getType().equalsIgnoreCase( ORDER_TYPE ) 
			                ||
			                // Unit test done.
			                parent.getType().equalsIgnoreCase( ALIAS_SELECTION_ITEM_TYPE ) ) {
			            parser.value_expression_A() ;
			            ComparisonPredType compPred = ComparisonPredType.Factory.newInstance() ;
			            AST_ValueExpression valueExpressionNode = (AST_ValueExpression)parser.jjtree.rootNode() ;
			            valueExpressionNode.buildXmlTree( compPred.addNewArg() ) ;
			        }
			        else {
			            throw new ParseException( UNRECOGNIZED_FRAGMENT + contextPath ) ;	
			        }
			    }
			    else if( childName.equalsIgnoreCase( ORDER_ELEMENT ) ) { 
			        // Unit test done.
			        if( child.getType().equalsIgnoreCase( ORDER_OPTION_TYPE ) ) {		  
			            parser.ordering_specification_S() ;
			            OrderType orderType = OrderType.Factory.newInstance() ;
			            AST_OrderingSpecification orderSpecNode = (AST_OrderingSpecification)parser.jjtree.rootNode() ;
			            orderSpecNode.buildXmlTree( orderType.addNewOrder() ) ;
			        }
			        else {
			            parent = cp.getParent() ;
			            ContextPath.Element grandParent = cp.getElement( cp.size() - 3 ) ;
			            // Unit test done.
			            if( parent.getName().equalsIgnoreCase( ITEM_ELEMENT )
			                    ||
			                    grandParent.getName().equalsIgnoreCase( ORDERBY_ELEMENT ) ) {
			                parser.ordering_specification_S() ;
			                OrderType orderType = OrderType.Factory.newInstance() ;
			                AST_OrderingSpecification orderSpecNode = (AST_OrderingSpecification)parser.jjtree.rootNode() ;
			                orderSpecNode.buildXmlTree( orderType.addNewOrder() ) ; 
			            }
			            else {
			                throw new ParseException( UNRECOGNIZED_FRAGMENT + contextPath ) ;	
			            }
			        }
			    }
			    else if( childName.equalsIgnoreCase( FROM_TABLE_TYPE_ELEMENT ) ) {
			        // Unit test done.
			        if( child.getType().equalsIgnoreCase( TABLE_TYPE ) ) {
			            parser.table_reference_A() ;     
			            ArrayOfFromTableType tableArray = ArrayOfFromTableType.Factory.newInstance() ;
			            AST_Table tableNode = (AST_Table)parser.jjtree.rootNode() ;
			            tableNode.buildXmlTree(tableArray.addNewFromTableType() ) ;
			        }
			        else {
			            throw new ParseException( UNRECOGNIZED_FRAGMENT + contextPath ) ;	
			        }
			    }
			    else if( childName.equalsIgnoreCase( TABLES_ELEMENT ) ) {
			        // Unit test done.
			        if( child.getType().equalsIgnoreCase( ARRAY_OF_FROM_TABLE_TYPE ) ) {
			            parser.table_array_fragment() ;
			            JoinTableType joinTable = JoinTableType.Factory.newInstance() ;
			            AST_TableArrayFragment tableArrayNode = (AST_TableArrayFragment)parser.jjtree.rootNode() ;
			            tableArrayNode.buildXmlTree( joinTable.addNewTables() ) ; 
			        }
			        else {
			            throw new ParseException( UNRECOGNIZED_FRAGMENT + contextPath ) ;	
			        }
			    }
			    // Unit test done.
			    else {
			        throw new ParseException( UNRECOGNIZED_FRAGMENT + contextPath ) ;
			    }
			    checkForRemainingSource() ;
			}
			if( parser.tracker.numberOfErrors() > 0 ) {
			    throw new AdqlException( parser.tracker ) ;
			}
			return (XmlObject)parser.jjtree.rootNode().getGeneratedObject() ;

		}
		catch( ParseException pex ) {
		    parser.tracker.setError( pex ) ;
		    throw new AdqlException( parser.tracker ) ;
		}
		finally {
		    if( log.isDebugEnabled() ) {
		        try {
		            XmlObject xo = (XmlObject)parser.jjtree.rootNode().getGeneratedObject() ;
		            logReportOnErrors( xo ) ;
		        }
		        catch( Throwable th ) {
		            ; // Ignore
		        }
		    }
		    if( log.isTraceEnabled() ) exitTrace( "execFragment(String contextPath)" ) ;
		}

	}
    
    private void fSearchCondition() throws ParseException {
        parser.search_condition_S() ;  
        WhereType where = WhereType.Factory.newInstance() ;
        AST_SearchCondition searchConditionNode = (AST_SearchCondition)parser.jjtree.rootNode() ;
        searchConditionNode.buildXmlTree( where.addNewCondition() ) ;
    }
    
    private void fJoinSpecification() throws ParseException {
        parser.join_specification_S() ;
        JoinTableType joinTable = JoinTableType.Factory.newInstance() ;
        AST_JoinSpecification jsNode = (AST_JoinSpecification)parser.jjtree.rootNode() ;
        jsNode.buildXmlTree( joinTable.addNewJoinSpecification() ) ; 
    }
    
    private void fInValueListConstant() throws ParseException {
        parser.in_value_list_constant_A() ;
        ConstantListSet constantListSet = ConstantListSet.Factory.newInstance() ;
        AST_InValueListConstant inValueListConstantNode = (AST_InValueListConstant)parser.jjtree.rootNode() ;
        inValueListConstantNode.buildXmlTree( constantListSet.addNewItem() ) ;  
    }
    
    private void fQuerySpecification() throws ParseException {
        parser.query_specification_A() ;
        checkForRemainingSource() ;
        SelectDocument selectDoc = SelectDocument.Factory.newInstance() ;
        AST_Select selectNode = (AST_Select)parser.jjtree.rootNode() ;
        selectNode.buildXmlTree( selectDoc.addNewSelect() ) ;
        XmlObject xmlObject = (XmlObject)selectNode.getGeneratedObject() ;               
        checkTableAliases( xmlObject ) ;
        checkExpressionAliases( xmlObject ) ;
    }
    
    private void fSubQuerySpecification() throws ParseException {
        parser.query_specification_A() ;
        checkForRemainingSource() ;
        SubQuerySet subQuerySet = SubQuerySet.Factory.newInstance() ;
        AST_Select selectNode = (AST_Select)parser.jjtree.rootNode() ;
        selectNode.buildXmlTree( subQuerySet.addNewSelection() ) ;    
    }
    
    private void fSetQuantifier() throws ParseException {
        parser.set_quantifier_S() ;
        SelectType select = SelectType.Factory.newInstance() ;
        AST_Allow allowNode = (AST_Allow)parser.jjtree.rootNode() ;
        allowNode.buildXmlTree( select.addNewAllow() ) ;     
    }
    
    private void fSetLimit() throws ParseException {
        parser.set_limit_A() ;
        SelectType select = SelectType.Factory.newInstance() ;
        AST_Restrict restrictNode = (AST_Restrict)parser.jjtree.rootNode() ;
        restrictNode.buildXmlTree( select.addNewRestrict() ) ;  
    }
    
    private void fSelectionList() throws ParseException {
        parser.select_list_S() ;
        SelectType select = SelectType.Factory.newInstance() ;
        AST_SelectionList selectionListsNode = (AST_SelectionList)parser.jjtree.rootNode() ;
        selectionListsNode.buildXmlTree( select.addNewSelectionList() ) ; 
    }
    
    private void fFromClause() throws ParseException {
        parser.from_clause_S() ;
        SelectType select = SelectType.Factory.newInstance() ;
        AST_From fromNode = (AST_From)parser.jjtree.rootNode() ;
        fromNode.buildXmlTree( select.addNewFrom() ) ;  
    }
    
    private void fWhereClause() throws ParseException {
        parser.where_clause_S() ;
        SelectType select = SelectType.Factory.newInstance() ;
        AST_Where whereNode = (AST_Where)parser.jjtree.rootNode() ;
        whereNode.buildXmlTree( select.addNewWhere() ) ;
    }
    
    private void fGroupBy() throws ParseException {
        parser.group_by_clause_S() ;
        SelectType select = SelectType.Factory.newInstance() ;
        AST_GroupBy groupByNode = (AST_GroupBy)parser.jjtree.rootNode() ;
        groupByNode.buildXmlTree( select.addNewGroupBy() ) ; 
    }
    
    private void fHaving() throws ParseException {
        parser.having_clause_S() ;
        SelectType select = SelectType.Factory.newInstance() ;
        AST_Having havingNode = (AST_Having)parser.jjtree.rootNode() ;
        havingNode.buildXmlTree( select.addNewHaving() ) ;
    }
    
    private void fOrderBy() throws ParseException {
        parser.order_by_clause_S() ;
        SelectType select = SelectType.Factory.newInstance() ;
        AST_OrderByClause orderByNode = (AST_OrderByClause)parser.jjtree.rootNode() ;
        orderByNode.buildXmlTree( select.addNewOrderBy() ) ;      
    }
    
    private void fSortSpecification() throws ParseException {
        parser.sort_specification_A() ; 
        OrderExpressionType orderExpression = OrderExpressionType.Factory.newInstance() ;
        AST_SortSpecification sortSpecNode = (AST_SortSpecification)parser.jjtree.rootNode() ;
        sortSpecNode.buildXmlTree( orderExpression.addNewItem() ) ;
    }
    
    private void fColumnReference() throws ParseException {
        parser.column_reference_A() ;
        SelectionListType selectList = SelectionListType.Factory.newInstance() ;
        AST_ColumnReference colRefNode = (AST_ColumnReference)parser.jjtree.rootNode() ;
        colRefNode.buildXmlTree( selectList.addNewItem() ) ; 
    }
    
    private void fDerivedColumn() throws ParseException {
        parser.derived_column_S() ; 
        SelectionListType selectList = SelectionListType.Factory.newInstance() ;
        AST_DerivedColumn derivedColNode = (AST_DerivedColumn)parser.jjtree.rootNode() ;
        derivedColNode.buildXmlTree( selectList.addNewItem() ) ; 
    }
	
	
    /**
     * Crude check of table aliases and column references
     */
	private void checkTableAliases( XmlObject xmlObject ) throws ParseException {
        if( log.isTraceEnabled() ) enterTrace( "checkTableAliases" ) ;
		HashSet tRefs = getTableReferences( xmlObject ) ;
        XmlObject element = null ;
        XmlCursor cursor = xmlObject.newCursor() ;
        String sTabReference = null ;
        parser.tracker.resetPosition() ;
        try {
            cursor.toFirstChild() ; // There has to be a first child!
            do {
                if( cursor.isStart() ) {
                    parser.tracker.push( cursor.getName().getLocalPart()
                            , cursor.getObject().schemaType() ) ;
                    element = cursor.getObject() ;
                    if( element.schemaType() == ColumnReferenceType.type ) {
                        ColumnReferenceType col = (ColumnReferenceType)element ;
                        if( col.isSetArchive() ) {
                            sTabReference = col.getArchive() + '.' + col.getTable() ;
                        }
                        else {
                            sTabReference = col.getTable() ;
                        }
                        if( !tRefs.contains( sTabReference ) ) {
                            String message = NONEXISTENT_ALIAS + col.getTable() + '.' + col.getName() ;
                            ParseException pex = new ParseException( message ) ;
                            parser.tracker.setError( pex ) ;
                        }
                    }                       
                } 
                else if( cursor.isEnd() ) {
                    parser.tracker.pop() ;
                }
            } while( cursor.toNextToken() != XmlCursor.TokenType.NONE ) ;
        }
        finally {    	
            cursor.dispose() ;
            if( log.isTraceEnabled() ) exitTrace( "checkTableAliases" ) ;
        }
	}
	
	private HashSet getTableReferences( XmlObject selObject ) throws ParseException {
        if( log.isTraceEnabled() ) enterTrace( "getTableReferences" ) ;
		HashSet tables = new HashSet() ;
		HashSet aliases = new HashSet() ;
        TableType tType = null ;
//        ArchiveTableType aType = null ;
        String alias = null ;
        //
        // Loop through the whole of the query looking for table refs....
        XmlCursor cursor = selObject.newCursor() ;
        SchemaType schemaType = null ;
        XmlObject xmlObject = null ;
        try {
            while( !cursor.toNextToken().isNone() ) {
                if( cursor.isStart() ) { 
                    xmlObject = cursor.getObject() ;
                    schemaType = xmlObject.schemaType() ;
                    
                    parser.tracker.push( cursor.getName().getLocalPart()
                                       , schemaType ) ;
                    
                    if( schemaType == TableType.type ) {
                        tType = (TableType)xmlObject ;
                        if( tType.isSetArchive() ) {
                            tables.add( tType.getArchive() + '.' + tType.getName() ) ;
                        }
                        else {
                            tables.add( tType.getName() ) ;
                        }                       
                        alias = tType.getAlias() ;
                    }
//                    else if( schemaType == ArchiveTableType.type ) {
//                        aType = (ArchiveTableType)xmlObject ;
//                        tables.add( aType.getArchive() + '.'  + aType.getName() ) ;  
//                        alias = aType.getAlias() ;
//                    }
                    else {
                        continue ;
                    }
                    if( alias != null ) {
                        if( !aliases.add( alias ) ) {
                            String message = DUPLICATE_TABLE_ALIAS + alias ;
                            ParseException pex = new ParseException( message ) ;
                            parser.tracker.setError( pex ) ;
                        }
                    }
                }
                else if( cursor.isEnd() ) {
                    parser.tracker.pop() ;
                }
            } // end while
            
            //
            // Check for aliases with the same name as a table...
            Iterator it = aliases.iterator() ;
            while( it.hasNext() ) {
                String aName = (String)it.next() ;
                if( tables.contains( aName ) ) {
                    String message = TABLE_ALIAS_CLASH + aName  ;
                    throw new ParseException( message ) ;
                }
            }
            tables.addAll( aliases ) ;
            if( log.isDebugEnabled() ) {
                log.debug( "Tables contained in query: " + tables.toString() ) ;
            }
        }
        finally {
            cursor.dispose();
            if( log.isTraceEnabled() ) exitTrace( "getTableReferences" ) ;
        }       
        return tables ;
	}
	
    /**
     * Crude check of expression aliases.
     * In the absence of metadata for columns, the best that can be accomplished
     * is to check for duplicates. 
     */
    private void checkExpressionAliases( XmlObject xmlObject ) throws ParseException {
        if( log.isTraceEnabled() ) enterTrace( "checkExpressionAliases" ) ;
        HashSet aliases = new HashSet() ;
        AliasSelectionItemType aType = null ;
        String alias = null ;
        parser.tracker.resetPosition() ;
        //
        // Loop through the whole of the query looking for AliasSelectionItemTypes....
        XmlCursor cursor = xmlObject.newCursor() ;
        try {
            while( !cursor.toNextToken().isNone() ) {
                if( cursor.isStart() ) { 
                    parser.tracker.push( cursor.getName().getLocalPart()
                                       , cursor.getObject().schemaType() ) ;
//                    if( log.isTraceEnabled() ) {
//                        log.trace( "cursor.getName().getLocalPart(): " + cursor.getName().getLocalPart() ) ;
//                        log.trace( "cursor.getObject().schemaType().getName().getLocalPart(): " + cursor.getObject().schemaType().getName().getLocalPart() ) ;
//                    }
                    if( cursor.getObject().schemaType() == AliasSelectionItemType.type ) {
                        aType = (AliasSelectionItemType)cursor.getObject() ;
                        alias = aType.getAs() ;
                        if( alias != null ) { 
                            if( !aliases.add( alias ) ) {
                                String message = DUPLICATE_EXPRESSION_ALIAS 
                                + alias ;
                                ParseException pex = new ParseException( message ) ;
                                parser.tracker.setError( pex ) ;
                            }
                        }
                    }
                }
                else if( cursor.isEnd() ) {
                    parser.tracker.pop() ;
                }
            } // end while
        }
        finally {
            cursor.dispose();   
            if( log.isTraceEnabled() ) exitTrace( "checkExpressionAliases" ) ;
            
        }
    }

    
    private boolean isEof() {
        if( parser.token.kind == AdqlStoXConstants.EOF )
            return true ;
        parser.getNextToken() ;
        return false ;
    }
	
	private void checkForTrailingComment( XmlObject parent ) {
	    if( log.isTraceEnabled() ) enterTrace( "checkForTrailingComment" ) ;
	    XmlCursor cursor = null ;
	    Token commentLinkedToken = null ;
	    try {

	        do  {
	            if( parser.token.specialToken != null ) {
	                if( parser.token.specialToken.kind == AdqlStoXConstants.COMMENT ) {
	                    commentLinkedToken = parser.token ;
	                }
	            }
	        } while( isEof() == false ) ;

	        if( commentLinkedToken != null ) {

	            if( log.isDebugEnabled() ) { 
	                log.debug( "commentLinkedToken.image: \"" + commentLinkedToken.image + "\" contains comment: " + commentLinkedToken.specialToken.image ) ;   
	            }

	            cursor = parent.newCursor() ;
	            cursor.toEndToken() ;
	            String comment = commentLinkedToken.specialToken.image ;          
	            cursor.insertComment( SimpleNode.prepareComment( comment ) ) ;      

	        }

	    }	    catch( Exception ex ) {	        log.debug( "Problem encountered whilst writing end comment.", ex ) ;	    }	    finally {	        if( cursor != null )	            cursor.dispose();
	        if( log.isTraceEnabled() ) exitTrace( "checkForTrailingComment" ) ;	    }	
	}
    
    private String getTrailingComment() {
        Token commentLinkedToken = null ;
    
            do  {
                if( parser.token.specialToken != null ) {
                    if( parser.token.specialToken.kind == AdqlStoXConstants.COMMENT ) {
                        commentLinkedToken = parser.token ;
                    }
                }
            } while( isEof() == false ) ;
            
        if( commentLinkedToken != null )
            return commentLinkedToken.specialToken.image ;
        return null ;
    }
    
    boolean lookForBoolean() {
       int i = 0 ;	
       Token t;
       boolean found = false ;
       do {
           t = parser.getToken(i++);
           if( t.kind == AdqlStoXConstants.OR
               ||
               t.kind == AdqlStoXConstants.AND 
               ||
               t.kind == AdqlStoXConstants.NOT ) {
               found = true ;
               break ;
           }
       } while (t.kind != AdqlStoXConstants.EOF );
       return found ;    	
    }
    
    
    void checkForRemainingSource() throws ParseException {
        if( log.isTraceEnabled() ) enterTrace( "checkForRemainingSource" ) ;
        try {
            if( parser.getToken(1).kind != AdqlStoXConstants.EOF ) {
                if( log.isDebugEnabled() ) {
                    log.debug( "parser.getToken(1).image: \"" + parser.getToken(1).image + "\"" ) ;
                }
                ArrayList rTokens = new ArrayList() ;
                int i = 1 ;
                Token t = parser.getToken(i) ;
                try {
                    while( t.kind != AdqlStoXConstants.EOF ) {
                        rTokens.add( t ) ;
                        t = parser.getToken(++i) ;
                    }
                }
                catch( Exception pex ) {
                    ; // do nothing
                }
                StringBuffer buffer = new StringBuffer( 32 + (rTokens.size() * 8) ) ;
                buffer.append( UNEXPECTED_REMAINDER ) ;
                ListIterator iterator = rTokens.listIterator() ;
                while( iterator.hasNext() ) {
                    buffer.append( ((Token)iterator.next()).image ) ;
                }
                throw new ParseException( buffer.toString() ) ;
            }
        }
        finally {
            if( log.isTraceEnabled() ) exitTrace( "checkForRemainingSource" ) ;
        }
    }
 
    protected boolean matches( int[] kindArray, int kind ) {
    	for( int i=0; i < kindArray.length; i++ ) {
    		if( kind == kindArray[i] ) {
    		   if( log.isDebugEnabled() ) {
    		   	  log.debug( "Match found on token: " + kind ) ;
    		   }	
    		   return true ;	
    		}
    	}
    	log.debug( "No match found" ) ;
    	return false ;
    }
    
    
    protected String arrayToString( int[] intArray ) {
    	StringBuffer buffer = new StringBuffer() ;
    	for( int i=0; i<intArray.length; i++ ) {
    		buffer.append( intArray[i] ).append( " " ) ;
    	} 
    	return buffer.toString() ; 	
    }
    
    
    protected StringBuffer initUtilityBuffer( String prefix ) {
    	if( uBuffer.length() > 0 ) {
    		uBuffer.delete( 0, uBuffer.length() ) ;
    	}
    	if( prefix != null ) {
    	   uBuffer.append( prefix ) ;
    	}
    	return uBuffer ;
    }

	public void enterTrace( String entry ) {
		log.trace( getIndent().toString() + "enter: " + entry ) ;
		indentPlus() ;
	}

    public void exitTrace( String entry ) {
    	indentMinus() ;
		log.trace( getIndent().toString() + "exit : " + entry ) ;
	}
	
    public void indentPlus() {
		getIndent().append( ' ' ) ;
	}
	
    public void indentMinus() {
        if( logIndent.length() > 0 ) {
            getIndent().deleteCharAt( logIndent.length()-1 ) ;
        }
	}
	
    public StringBuffer getIndent() {
	    if( logIndent == null ) {
	       logIndent = new StringBuffer() ;	
	    }
	    return logIndent ;	
	}
    
    public void resetIndent() {
        if( logIndent != null ) { 
            if( logIndent.length() > 0 ) {
               logIndent.delete( 0, logIndent.length() )  ;
            }
        }   
    }
	
	protected void errorSkipTo( int[] kindArray, ParseException pex ) {
	   if( log.isTraceEnabled() ) enterTrace ( "errorSkipTo()" ) ;
	   try {
		   log.debug( "Syntax error around token.image: " + parser.token.image
		            + "\nSkipping to one of " + arrayToString(kindArray) ) ;
	       String lastMessage = pex.getLocalizedMessage() ;
           parser.tracker.setError( lastMessage ) ;
		   if( matches( kindArray, parser.getToken(1).kind ) ) {
		      return ;
		   }
	       Token t;	       
	       do {
	           t = parser.getNextToken();
	           log.debug( "Token: " + t.image ) ;
	           if( t.kind == AdqlStoXConstants.EOF )
	               return ;
	       } while ( !matches( kindArray, parser.getToken(1).kind ) );
	   }
	   finally {
           if( log.isTraceEnabled() ) exitTrace ( "errorSkipTo()" ) ;
	   }
    }
    

	protected void errorSkipPast( int[] kindArray, ParseException pex ) 
	{
	    if( log.isTraceEnabled() ) enterTrace ( "errorSkipPast()" ) ;	
	    log.debug( "Syntax error around token.image: " + parser.token.image
	            + "\nSkipping to one of " + arrayToString( kindArray ) ) ;
	    try {
	        parser.tracker.setError( pex ) ;
	        Token t;
	        while( !matches( kindArray, parser.getToken(1).kind ) ) {
	            t = parser.getNextToken() ;
	            if( t.kind == AdqlStoXConstants.EOF ) {
	                return ;	
	            }   	
	        }
	    }
	    finally {
	        if( log.isTraceEnabled() ) exitTrace ( "errorSkipPast()" ) ;
	    }
	}
    protected boolean checkForMatchingBrace() {      if( log.isTraceEnabled() ) enterTrace ( "checkForMatchingBrace()" ) ;      boolean retCode = false ;      try {	          		      Token tok = parser.getToken(1) ;		      int nesting = 1;		      while ( tok.kind != AdqlStoXConstants.EOF ) {		        if( tok.kind == AdqlStoXConstants.LEFT_PAREN ) 		           nesting++;		        if( tok.kind == AdqlStoXConstants.RIGHT_PAREN ) {		           nesting--;		           if( nesting == 0 ) {		          	  retCode = true ;		          	  break ;		          }		        }		        tok = parser.getNextToken();		      }	           }      finally {      	 if( log.isDebugEnabled() ) {      	    log.debug( "retCode: " + retCode ) ;	      	 }      	 if( log.isTraceEnabled() ) exitTrace ( "checkForMatchingBrace()" ) ;      }      return retCode ;    }
    
    protected void selectSublistError( boolean first, ParseException pex ) throws ParseException {
      if( log.isTraceEnabled() ) enterTrace ( "selectSublistError()" ) ;
      try {
         StringBuffer buffer = null ;
         if( pex.getMessage() == null ) {
            buffer = initUtilityBuffer( "Encountered " ) ;
         }
         else {
            buffer = initUtilityBuffer( pex.getMessage() );
         }
         Token t = parser.getToken(1) ;
         log.debug( "syntax error on: " + t.image ) ;    
           do {    
                 buffer.append( t.image ) ;
                 if( !t.image.equals( " " ) )
                    buffer.append( ' ' ) ;      
                 if( first && t.kind == AdqlStoXConstants.COMMA ) {
                    t = parser.getNextToken() ;
                    if( t.kind != AdqlStoXConstants.COMMA ) {
                        break ;
                    }
                 }
                 if( t.kind == AdqlStoXConstants.FROM ) {  
                     parser.tracker.setError( buffer.toString() ) ;             
                    throw new ParseException( buffer.toString() ) ;
                 }
               t = parser.getNextToken();
               if( t.kind == AdqlStoXConstants.EOF )
                   break ;
               t = parser.getToken(1) ;
           } while (t.kind != AdqlStoXConstants.COMMA );
           parser.tracker.setError( buffer.toString() ) ;
      }
      finally {
         if( log.isTraceEnabled() ) exitTrace ( "selectSublistError()" ) ;
      }
    }

    

    protected void selectSublistEnsureNotComma() {
        if( log.isTraceEnabled() ) enterTrace ( "selectSublistEnsureNotComma()" ) ;
        try {
           Token t = parser.getToken(1) ;
           while (t.kind == AdqlStoXConstants.COMMA ) {   
               parser.tracker.setError( "Encountered " + t.image ) ;         
               t = parser.getNextToken();
               t = parser.getToken(1) ;
           } ;
        }
        finally {
           if( log.isTraceEnabled() ) exitTrace ( "selectSublistEnsureNotComma()" ) ;
        }   
    }

    
    protected void selectSublistEnsureCommaOrFrom() {
        if( log.isTraceEnabled() ) enterTrace ( "selectSublistEnsureCommaOrFrom()" ) ;
        boolean bError = false ;
        try {
           Token t = parser.getToken(1) ;
           StringBuffer buffer = parser.compiler.initUtilityBuffer( "Encountered " ) ;
           while (t.kind != AdqlStoXConstants.FROM
                  &&
                  t.kind != AdqlStoXConstants.EOF) {  
               // If the next token is not a comma, loop looking for a comma
               // (or EOF or FROM clause)       
               if( t.kind != AdqlStoXConstants.COMMA ) {              
                  buffer.append( t.image ); 
                  if( !t.image.equals( " " ) )
                     buffer.append( ' ' ) ;   
                  t = parser.getNextToken();
                  t = parser.getToken(1) ;
                  bError = true ;
                  continue ;
               }
               
               // If the next token is a comma, ensure it is not followed immediately
               // by another comma...
               if( parser.getToken(2).kind == AdqlStoXConstants.COMMA ) {    
                  buffer.append( t.image ) ;
                  if( !t.image.equals( " " ) )
                     buffer.append( ' ' ) ;  
                  t = parser.getNextToken();
                  t = parser.getToken(1) ;
                  bError = true ;
                  continue ;
               }
               
               break ;
           } ;
           if( bError == true ) {
               parser.tracker.setError( buffer.toString() ) ;
           }
        }
        finally {
           if( log.isTraceEnabled() ) exitTrace ( "selectSublistEnsureCommaOrFrom()" ) ;
        } 
       
    }


    /**
     * @return the prettyPrintIndent
     */
    public int getPrettyPrintIndent() {
        return this.prettyPrintIndent ;
    }


    /**
     * @param prettyPrintIndent 
     */
    public void setPrettyPrintIndent( int prettyPrintIndent ) {
        this.prettyPrintIndent = prettyPrintIndent ;
    }
     
    protected String formUniqueID() {
         this.numberForUniqueID++ ;
         return "ID_" + Integer.toString( this.numberForUniqueID ) ;       
    }
    
}
