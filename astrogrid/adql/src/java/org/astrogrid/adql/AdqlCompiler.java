package org.astrogrid.adql ;

import org.apache.commons.logging.Log ;
import org.apache.commons.logging.LogFactory ;
import java.util.HashSet ;
import java.util.HashMap;
import java.util.Iterator ;
import java.util.ArrayList ;
import java.util.ListIterator ;
import org.apache.xmlbeans.XmlOptions ;

import org.astrogrid.adql.metadata.MetadataQuery;
import org.astrogrid.adql.v1_0.beans.* ;
import org.w3c.dom.Node ;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlCursor;
import org.apache.xmlbeans.SchemaType;


public class AdqlCompiler {
	
	private static Log log = LogFactory.getLog( AdqlCompiler.class ) ;
	   
    private static final boolean DETAILED_DEBUG_PRINT_ENABLED = false ;
    private static final boolean DETAILED_ERROR_REPORT_ENABLED = false ;
    
	private StringBuffer logIndent = null ;
	
	public static final String DUPLICATE_TABLE_ALIAS =
	     "Duplicated table alias: " ;
	public static final String TABLE_ALIAS_CLASH =
	     "Alias with same name as a table: " ;
	public static final String NONEXISTENT_ALIAS =
	     "Column reference with unknown table or table alias: " ;
	public static final String DUPLICATE_EXPRESSION_ALIAS =
	     "Duplicated expression alias: " ;
    public static final String EXPRESSION_ALIAS_CLASH_WITH_TABLE =
         "Expression alias with same name or alias as a table: " ;
	public static final String UNRECOGNIZED_FRAGMENT =
	     "Unrecognized fragment of ADQL: " ;
	public static final String UNEXPECTED_REMAINDER =     
	     "Unexpected remaining characters found: " ;
    public static final String UNKNOWN_TABLE =
         "Unknown table: " ;
    public static final String COLUMN_NOT_KNOWN_IN_THIS_TABLE =
        "Column not known in this table: " ;
    public static final String PREMATURE_EOF_WHILST_IN_SELECTION_LIST =
        "premature end of query whilst searching selection list." ;
    
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
     
     private int prettyPrintIndent = 2 ;
     private ArrayList tableReferences = new ArrayList() ;
     private ArrayList columnReferences = new ArrayList() ;
     private ArrayList aliasSelections = new ArrayList() ;
         
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
         initCollections();
         initParserFields() ;
     }
     
     public void ReInit( java.io.Reader stream ) {
         this.resetIndent() ;
         this.parser.ReInit( stream ) ;
         initCollections();
         initParserFields() ;
     }
     
     public void ReInit(AdqlStoXTokenManager tm) {
         this.resetIndent() ;
         this.parser.ReInit( tm ) ;
         initCollections();
         initParserFields() ;
     }
     
     private void initCollections() {
         if( this.aliasSelections != null ) {
             this.aliasSelections.clear() ;
         }
         if( this.columnReferences != null ) {
             this.columnReferences.clear() ;
         }
         if( this.tableReferences != null ) {
             this.tableReferences.clear() ;
         }
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
     }
    
    private AdqlStoX parser ;
	private StringBuffer uBuffer = new StringBuffer() ;
    private boolean semanticProcessing = false ;
    private MetadataQuery metadataQuery = null ;
    
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
                XmlOptions opts = getSaveOptions( true ) ; ;
                              
                // Create an error listener.
                ArrayList errorList = new ArrayList() ;
                opts.setErrorListener( errorList ) ;
                
                // Validate the XML.
                boolean isValid = selectDoc.validate(opts);
                
                log.debug( "selectDoc validity is " + isValid 
                         + "\nCompilation before cross validation produced...\n" 
                         + selectDoc.xmlText(opts) ) ;
            }
            if( semanticProcessing == true ) {
                HashMap tables = checkAliases() ;
                checkColumnReferences( tables) ;
                if( metadataQuery != null ) {
                     HashMap tables2 = checkTablesForExistence() ;
                     checkColumnsForExistence( tables2 ) ;
                }
            }          
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
    
    public XmlOptions getSaveOptions( boolean prettyPrint ) {
        XmlOptions opts = new XmlOptions();
        opts.setSaveOuter() ;
//        opts.setSaveImplicitNamespaces( ImplicitNamespaces ) ;
        opts.setSaveNamespacesFirst() ;
        opts.setSaveAggressiveNamespaces() ;
        if( prettyPrint ) {     
           opts.setSavePrettyPrint() ;
           opts.setSavePrettyPrintIndent( prettyPrintIndent ) ; 
        }
        return opts ;
    }
	
	private void logReportOnErrors( XmlObject xmlObject ) {
        if( !DETAILED_ERROR_REPORT_ENABLED )
            return ;
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
                opts.setSavePrettyPrintIndent(4) ;
                log.debug( "Compilation produced: " 
                         + xmlObject.xmlText(opts) ) ;
         }
	}

    public SelectDocument compileToXmlBeans() throws AdqlException { 
        return exec() ;
    }
    
    public String compileToXmlText( boolean prettyPrint ) throws AdqlException {
        XmlOptions opts = getSaveOptions( prettyPrint ) ;
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
        XmlOptions opts = getSaveOptions( prettyPrint ) ;
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
                if( comments == null ) {
                    return ;
                }
                if( comments.length != 2) {
                    return ;
                }
                cursor = parent.newCursor() ;
                cursor.toFirstChild() ;
                do {
                    
                    if( cursor.isStart() ) {
                        xo = cursor.getObject() ;
                        if( xo == child ) {
                            if( comments[0] != null ) {
                                if( comments[0].length() > 0 ) {
                                    cursor.insertComment( SimpleNode.prepareComment( comments[0] ) ) ; 
                                }
                            }                            
                            cursor.toEndToken() ;
                            cursor.toNextToken() ;
                            if( cursor.isStart() || cursor.isEnddoc() ) {
                                if( comments[1] != null ) {
                                    if( comments[1].length() > 0 ) {
                                        cursor.insertComment( SimpleNode.prepareComment( comments[1] ) ) ;
                                    }
                                }  
                                
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
    
    public void _writeHeaderAndTrailerComments( XmlObject parent, XmlObject child, String[] comments ) {
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
                cursor.dispose();
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
                      fInValueListExpression() ;
				  }
			   }
			   else if( childName.equalsIgnoreCase( CONDITION_ELEMENT ) ) {
			 	  parentName = cp.getParent().getName() ;
				  // Join table comparison ...
				  // Unit test done.
				  if( parentName.equalsIgnoreCase( TABLE_ELEMENT ) ) {
                      fJoinComparison() ; 
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
                      fSetFunctionSpecifictationPartFragment() ;
				  }
				  // Unit test done.
				  else {
                      fValueExpression() ;
			  	  }
			   }
			   // Unit test done.
			   else if( childName.equalsIgnoreCase( PATTERN_ELEMENT ) ) {
                   fPattern() ;
			   }
			   // Unit test done.
			   else if( childName.equalsIgnoreCase( TABLE_ELEMENT ) ) {
                   fTableReference() ;
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
                     fValueExpression() ;
				  }
				  else {
				     throw new ParseException( UNRECOGNIZED_FRAGMENT + contextPath ) ;	
				  }
			   }
			   else if( childName.equalsIgnoreCase( ORDER_ELEMENT ) ) { 
			   	 // Unit test done.
				 if( child.getType().equalsIgnoreCase( ORDER_OPTION_TYPE ) ) {	
                     fOrderingSpecification() ;
				 }
			     else {
			     	 parent = cp.getParent() ;
			         ContextPath.Element grandParent = cp.getElement( cp.size() - 3 ) ;
			         // Unit test done.
			         if( parent.getName().equalsIgnoreCase( ITEM_ELEMENT )
			             ||
			             grandParent.getName().equalsIgnoreCase( ORDERBY_ELEMENT ) ) {
                         fOrderingSpecification() ; 
			         }
			         else {
				        throw new ParseException( UNRECOGNIZED_FRAGMENT + contextPath ) ;	
				     }
			     }
			  }
			  else if( childName.equalsIgnoreCase( FROM_TABLE_TYPE_ELEMENT ) ) {
			  	 // Unit test done.
			 	 if( child.getType().equalsIgnoreCase( TABLE_TYPE ) ) {
                    fTableReferenceWithinJoinFromArray() ;
				 }
				 else {
				    throw new ParseException( UNRECOGNIZED_FRAGMENT + contextPath ) ;	
				 }
			  }
			  else if( childName.equalsIgnoreCase( TABLES_ELEMENT ) ) {
			  	 // Unit test done.
				 if( child.getType().equalsIgnoreCase( ARRAY_OF_FROM_TABLE_TYPE ) ) {
                     fJoinFromArray() ;
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
       
    private void fQuerySpecification() throws ParseException {
        parser.query_specification_A() ;
        checkForRemainingSource() ;
        SelectDocument selectDoc = SelectDocument.Factory.newInstance() ;
        AST_Select selectNode = (AST_Select)parser.jjtree.rootNode() ;
        selectNode.buildXmlTree( selectDoc.addNewSelect() ) ;
        selectNode.getGeneratedObject() ;     
        if( semanticProcessing == true ) {
            HashMap tables = checkAliases() ;
            checkColumnReferences( tables) ;
            if( metadataQuery != null ) {
                 HashMap tables2 = checkTablesForExistence() ;
                 checkColumnsForExistence( tables2 ) ;
            }
        }     
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
    
    private void fJoinComparison() throws ParseException {
        parser.comparison_predicate_A() ;
        JoinTableType joinTable = JoinTableType.Factory.newInstance() ;
        AST_ComparisonPredicate comparisonPredicateNode = (AST_ComparisonPredicate)parser.jjtree.rootNode() ;
        comparisonPredicateNode.buildXmlTree( joinTable.addNewCondition() ) ; 
    }
    
    private void fJoinFromArray() throws ParseException {
        parser.table_array_fragment() ;
        JoinTableType joinTable = JoinTableType.Factory.newInstance() ;
        AST_TableArrayFragment tableArrayNode = (AST_TableArrayFragment)parser.jjtree.rootNode() ;
        tableArrayNode.buildXmlTree( joinTable.addNewTables() ) ; 
     }
    
    private void fTableReferenceWithinJoinFromArray() throws ParseException {
        parser.table_reference_A() ;     
        ArrayOfFromTableType tableArray = ArrayOfFromTableType.Factory.newInstance() ;
        AST_Table tableNode = (AST_Table)parser.jjtree.rootNode() ;
        tableNode.buildXmlTree(tableArray.addNewFromTableType() ) ;
    }
    
    private void fOrderingSpecification() throws ParseException {
        parser.ordering_specification_S() ;
        OrderType orderType = OrderType.Factory.newInstance() ;
        AST_OrderingSpecification orderSpecNode = (AST_OrderingSpecification)parser.jjtree.rootNode() ;
        orderSpecNode.buildXmlTree( orderType.addNewOrder() ) ;
    }
    
    private void fTableReference() throws ParseException {
        parser.table_reference_A() ;
        FromType from = FromType.Factory.newInstance() ;
        AST_Table tableNode = (AST_Table)parser.jjtree.rootNode() ;
        tableNode.buildXmlTree( from.addNewTable() ) ;
    }
     
     private void fPattern () throws ParseException {
         parser.pattern_A() ;
         LikePredType lpt = LikePredType.Factory.newInstance() ;
         AST_CharacterStringLiteral patternNode = (AST_CharacterStringLiteral)parser.jjtree.rootNode() ;
         patternNode.buildXmlTree( lpt.addNewPattern() ) ;
     }
     
     private void fValueExpression() throws ParseException {
         parser.value_expression_A() ;
         ComparisonPredType compPred = ComparisonPredType.Factory.newInstance() ;
         AST_ValueExpression valueExpressionNode = (AST_ValueExpression)parser.jjtree.rootNode() ;
         valueExpressionNode.buildXmlTree( compPred.addNewArg() ) ;
     }
     
     private void fSetFunctionSpecifictationPartFragment() throws ParseException {
         parser.set_function_specification_part_fragment() ;
         ComparisonPredType compPred = ComparisonPredType.Factory.newInstance() ;
         AST_SetFunctionSpecificationPartFragment sfsNode = (AST_SetFunctionSpecificationPartFragment)parser.jjtree.rootNode() ;
         sfsNode.buildXmlTree( compPred.addNewArg() ) ;
     }
     
     private void fSearchCondition() throws ParseException {
         parser.search_condition_S() ;  
         WhereType where = WhereType.Factory.newInstance() ;
         AST_SearchCondition searchConditionNode = (AST_SearchCondition)parser.jjtree.rootNode() ;
         searchConditionNode.buildXmlTree( where.addNewCondition() ) ;
     }
     
     private void fInValueListExpression() throws ParseException {
         parser.in_value_list_constant_A() ;
         ConstantListSet constantListSet = ConstantListSet.Factory.newInstance() ;
         AST_InValueListConstant inValueListConstantNode = (AST_InValueListConstant)parser.jjtree.rootNode() ;
         inValueListConstantNode.buildXmlTree( constantListSet.addNewItem() ) ;  
     }
         
     private void fSortSpecification() throws ParseException {
         parser.sort_specification_A() ;    
         OrderExpressionType orderExpression = OrderExpressionType.Factory.newInstance() ;
         AST_SortSpecification sortSpecNode = (AST_SortSpecification)parser.jjtree.rootNode() ;
         sortSpecNode.buildXmlTree( orderExpression.addNewItem() ) ;
     }
    
    /**
     * Crude check of table aliases and aliased expressions
     */   
    private HashMap checkAliases() throws ParseException {
        if( log.isTraceEnabled() ) enterTrace( "checkTableAliases" ) ;
        HashMap tables = new HashMap() ;
        
        ListIterator it = tableReferences.listIterator() ; 
        while( it.hasNext() ) {
            Object obj = it.next() ;
            TableType tt = null ;
            if( obj instanceof AST_Table ) {
                tt = ((AST_Table)obj).getTable() ;
            }
            else if( obj instanceof AST_TableReferenceBarJoinedTable ) {
                tt = ((AST_TableReferenceBarJoinedTable)obj).getTable() ;
            }
            //
            // Tables may be duplicated...
            tables.put( tt.getName(), tt ) ;
            //
            // And we allow for archive being used,
            // which may also be duplicated...
            if( tt.isSetArchive() ) {
                tables.put( tt.getArchive() + '.' + tt.getName(), tt ) ;
            } 
            //
            // But table aliases must not be duplicated in any sense...
            if( tt.isSetAlias() ) {
                if( tables.containsKey( tt.getAlias() ) ) {
                    String message = DUPLICATE_TABLE_ALIAS + tt.getAlias() ;
                    ParseException pex = new ParseException( message ) ;
                    parser.tracker.setError( pex ) ;
                }
                else {
                    tables.put( tt.getAlias(), tt ) ;
                } 
            }   
        }
        
        HashSet aliasedExpressionSet = new HashSet() ;
        it = aliasSelections.listIterator() ;
        while( it.hasNext() ) {
            AliasSelectionItemType asit = ((AST_DerivedColumn)it.next()).getAliasSelection() ;            
            //
            // The alias in aliased expressions also cannot be duplicated...
            if( aliasedExpressionSet.add( asit.getAs() ) == false ) {
                String message = DUPLICATE_EXPRESSION_ALIAS + asit.getAs() ;
                ParseException pex = new ParseException( message ) ;
                parser.tracker.setError( pex ) ;
            }
            
            //
            // The alias cannot be a table name or table alias...
            if( tables.containsKey( asit.getAs() ) ) {
                String message = EXPRESSION_ALIAS_CLASH_WITH_TABLE + asit.getAs() ;
                ParseException pex = new ParseException( message ) ;
                parser.tracker.setError( pex ) ;
            }
        }
        
        if( log.isTraceEnabled() ) exitTrace( "checkTableAliases" ) ;
        return tables ;
    }
    
    private void checkColumnReferences( HashMap tables ) {
        if( log.isTraceEnabled() ) enterTrace( "checkColumnReferences" ) ;
        
        ListIterator iterator = columnReferences.listIterator() ;
        while( iterator.hasNext() ) {
            ColumnReferenceType crt = ((AST_ColumnReference)iterator.next()).getColumnReference() ;
            if( !tables.containsKey( crt.getTable() ) ) {
                String message = NONEXISTENT_ALIAS + crt.getTable() + '.' + crt.getName() ;
                ParseException pex = new ParseException( message ) ;
                parser.tracker.setError( pex ) ;
            }
        }
        
        
        if( log.isTraceEnabled() ) exitTrace( "checkColumnReferences" ) ;
    }
    
    private HashMap checkTablesForExistence() {
        if( log.isTraceEnabled() ) enterTrace( "checkTablesForExistence" ) ;
        //
        // Check that tables are indeed known tables...
        
        // Hash map to hold valid tables...
        HashMap validTables = new HashMap() ;
        
        ListIterator it = tableReferences.listIterator() ;
        while( it.hasNext() ) {
            Object obj = it.next() ;
            TableType tt = null ;
            if( obj instanceof AST_Table ) {
                tt = ((AST_Table)obj).getTable() ;
            }
            else if( obj instanceof AST_TableReferenceBarJoinedTable ) {
                tt = ((AST_TableReferenceBarJoinedTable)obj).getTable() ;
            }
            
            if( !metadataQuery.isTable( tt.getName() ) ) {
                //
                // Table invalid...
                parser.tracker.setError( new ParseException( UNKNOWN_TABLE + tt.getName() ) ) ;
            }
            else {
                //
                // Table valid:
                // Add the table to the valid tables collection.
                // This could be twice!
                // Once for table name.
                // Once for the table alias if it exists.
                validTables.put( tt.getName(), tt ) ;
                if( tt.isSetAlias() ) {
                    validTables.put( tt.getAlias(), tt ) ;
                }
            }
        }
  
        if( log.isTraceEnabled() ) exitTrace( "checkTablesForExistence" ) ;
        return validTables ;        
    }

    private void checkColumnsForExistence( HashMap tables ) {
        if( log.isTraceEnabled() ) enterTrace( "checkColumnsForExistence" ) ;
        ListIterator it = columnReferences.listIterator() ;
        while( it.hasNext() ) {
            ColumnReferenceType crt = ((AST_ColumnReference)it.next()).getColumnReference() ;
           
            //
            // First check that the table the column refers to exists...
            String columnName = crt.getName().trim() ;
            String tableName = crt.getTable().trim() ;                 
            if( !tables.containsKey( tableName ) ) {
                //
                // Table invalid...
                // We don't bother to continue. The table would already have been flagged,
                // and it is pointless to festoon the diagnostics with extra messages.
                continue ;
            }
            //
            // Ensure we get the real table name and not some alias...
            TableType tt = (TableType)tables.get( tableName ) ;
            tableName = tt.getName() ;
            
            //
            // Now check that the column really exists in this table...
            if( !metadataQuery.isColumn( tableName, columnName ) ){  
                String message ;
                //
                // Column invalid...
                if( tt.isSetAlias() ) {
                    message = COLUMN_NOT_KNOWN_IN_THIS_TABLE + tt.getAlias() + "." + columnName + " for table " + tableName ;
                }
                else {
                    message = COLUMN_NOT_KNOWN_IN_THIS_TABLE + tableName + "." + columnName ;
                }
                ParseException pex2 = new ParseException( message ) ;
                parser.tracker.setError( pex2 ) ;                
            }  
            
        } // end while
  
        if( log.isTraceEnabled() ) exitTrace( "checkColumnsForExistence" ) ;
       
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
//	            String comment = commentLinkedToken.specialToken.image ;          
//	            cursor.insertComment( SimpleNode.prepareComment( comment ) ) ;     
                
                Token tmpToken = commentLinkedToken.specialToken ;
                while( tmpToken.specialToken != null ) tmpToken = tmpToken.specialToken;
                  // The above line walks back the special token chain until it
                  // reaches the first special token after the previous regular
                  // token.
                while (tmpToken != null) {
                    cursor.insertComment( SimpleNode.prepareComment( tmpToken.image ) ) ;
                  tmpToken = tmpToken.next;
                }

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
                    buffer.append( ((Token)iterator.next()).image ).append( ' ' ) ;                   
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
            buffer.append( ' ' ) ;
         }
         Token t = parser.getToken(1) ;
         log.debug( "syntax error on: " + t.image ) ;    
           do {  
                 if( t.kind == AdqlStoXConstants.EOF ) {
                     buffer.append( PREMATURE_EOF_WHILST_IN_SELECTION_LIST ) ;
                 }
                 else {
                     buffer.append( t.image ) ;
                 }                 
                 if( !t.image.equals( " " ) )
                    buffer.append( ' ' ) ;      
                 if( first && t.kind == AdqlStoXConstants.COMMA ) {
                    t = parser.getNextToken() ;
                    if( t.kind != AdqlStoXConstants.COMMA ) {
                        break ;
                    }
                 }
                 if( t.kind == AdqlStoXConstants.FROM ) {  
                     break ;
//                    throw new ParseException( buffer.toString() ) ;
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
    
    protected void addTableReference( Object t ) {
        this.tableReferences.add( t ) ;
    }
    
    protected void addColumnReference( AST_ColumnReference c ) {
        this.columnReferences.add( c ) ;
    }
    
    protected void addAliasSelection( AST_DerivedColumn d ) {
        this.aliasSelections.add( d ) ;
    }

    /**
     * @return the metadataQuery
     */
    public MetadataQuery getMetadataQuery() {
        return metadataQuery;
    }

    /**
     * @param metadataQuery the metadataQuery to set
     */
    public void setMetadataQuery(MetadataQuery metadataQuery) {
        this.metadataQuery = metadataQuery;
        this.semanticProcessing = true ;
    }
    
    public boolean isMetadataQueryEnabled() {
        return metadataQuery != null ;
    }
    
    public void unsetMetadataQueryEnabled() {
        metadataQuery = null ;
    }

    /**
     * @return the semanticProcessing
     */
    public boolean isSemanticProcessing() {
        return semanticProcessing;
    }

    /**
     * @param semanticProcessing the semanticProcessing to set
     */
    public void setSemanticProcessing(boolean semanticProcessing) {
        this.semanticProcessing = semanticProcessing;
    }
    
    public static void RemoveProcessingInstruction( SelectDocument doc, String name ) {
        XmlCursor cursor = doc.newCursor() ;
        try {
            while( !cursor.toNextToken().isNone() ) {
                if( cursor.isProcinst() ) {
                    if( cursor.getName().getLocalPart().equals( name ) )  {
                        cursor.removeXml() ;
                        break ;
                    }
                }
                else if( cursor.isStart()
                         &&
                         cursor.getObject().schemaType() == SelectionListType.type ) {
                    //
                    // None found by the time SelectionList encountered...
                    break ;               
                }
            } // end while
        }
        finally {
            cursor.dispose();
        }      
    }
    
    public static void WriteProcessingInstruction( SelectDocument doc, String name, String content ) {
        if( log.isTraceEnabled() ) log.trace( "enter: WriteProcessingInstruction()" ) ;
        
        String mangledContent = content.replaceAll( ">", "&gt_;" ).replaceAll( "<", "&lt_;" ) ;
        XmlCursor cursor = doc.newCursor() ;
        try {
            while( !cursor.toNextToken().isNone() ) {
                if( cursor.isProcinst() ) {
                    if( cursor.getName().getLocalPart().equals( name ) )  {
                       // OK. There's already one here. We'll use it...
                        cursor.setTextValue( content ) ;
                        return ;
                    }
                }
                else if( cursor.isStart() ) {
                    
                    SchemaType type = cursor.getObject().schemaType() ;
                    //
                    // Preference is to write before the SELECT
                    if( type == SelectType.type ) {
                        //
                        // Remember where the SELECT is located...
                        cursor.push() ;
                    }
                    //
                    // But we search for existing ones down to the SelectionList...
                    else if( type == SelectionListType.type ) {                       
                        break ;  
                    }
                }          
            } // end while
            cursor.pop() ;
            cursor.insertProcInst( name, mangledContent ) ;
        }
        finally {
            cursor.dispose();
            if( log.isTraceEnabled() ) log.trace( "exit: WriteProcessingInstruction()" ) ;
        }      
    } // end of writeProcessingInstruction
    
    public static String ReadProcessingInstruction( SelectDocument doc, String name ) {
        String content = null ;
        XmlCursor cursor = doc.newCursor() ;
        try {
            while( !cursor.toNextToken().isNone() ) {
                if( cursor.isProcinst() ) {
                    if( cursor.getName().getLocalPart().equals( name ) )  {
                       // Found it...
                        content = cursor.getTextValue().replaceAll( "&gt_;", ">" ).replaceAll( "&lt_;", "<" ) ;
                        break ;
                    }
                }
                else if( cursor.isStart()
                         &&
                         cursor.getObject().schemaType() == SelectionListType.type ) {
                    break ;               
                }
            } // end while
            return content ;
        }
        finally {
            cursor.dispose();
        }      
    } // end of readProcessingInstruction
}
