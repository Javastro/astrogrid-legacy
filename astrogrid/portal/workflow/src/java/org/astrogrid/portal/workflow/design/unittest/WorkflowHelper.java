/*
 * @(#)WorkflowHelper.java   1.0
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.3, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.portal.workflow.design.unittest ;


import org.astrogrid.portal.workflow.design.*;
import java.util.Vector ;
import java.text.MessageFormat ;
import org.xml.sax.* ;
import java.io.StringReader ;

import org.apache.log4j.Logger ;
import org.apache.axis.utils.XMLUtils ;


/**
 * The <code>WorkflowHelper</code> class represents... 
 * <p>
 *
 * <p>
 * The class... 
 * 
 *
 * @author  Jeff Lusted
 * @version 1.0 29-Sep-2003
 * @see     
 * @see     
 * @since   AstroGrid 1.3
 */
public class WorkflowHelper {
    
    /** Compile-time switch used to turn tracing on/off. 
      * Set this to false to eliminate all trace statements within the byte code.*/         
    private static final boolean 
        TRACE_ENABLED = true ;
        
    private static Logger 
        logger = Logger.getLogger( WorkflowHelper.class ) ;
         
    private static final String jobOne = 
        "<?xml version=\"1.0\" encoding=\"UTF8\"?>" +
        "<workflow name = \"{0}\" templateName=\"OneStepJob\">" +
        "<community>{1}</community>" +
        "<description>This is a one step job</description>" +
        "<!-- " +        "    | The top level structure within a workflow is " +        "    | always a sequence... =============================================== -->" +
        "<sequence>" +
        " <step name=\"StepOne\" stepNumber=\"1\" sequenceNumber=\"1\">" +
        
                "  <tool name=\"someQueryTool\">" +
                "   <input>" +        "   <parameter name=\"query\" type=\"adql\" >" +
              //        "<!-- SQL equivalent" +
//        "SELECT t.a, g.d FROM Tab a, Tab d WHERE a.d < d.e AND a.f < d.f ORDER BY t.f, g.b" +//        " -->" +
        "<Select xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " + 
        "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
        "<SelectionList>" +
          "<ColumnExpr>" +
            "<SingleColumnReference>" +
              "<TableName>t</TableName>" +
              "<Name>a</Name>" +
            "</SingleColumnReference>" +
          "</ColumnExpr>" +
          "<ColumnExpr>" +
            "<SingleColumnReference>" +
              "<TableName>g</TableName>" +
              "<Name>d</Name>" +
            "</SingleColumnReference>" +
          "</ColumnExpr>" +
        "</SelectionList>" +
        "<TableClause>" +
          "<FromClause>" +
            "<TableReference>" +
              "<Table>" +
                "<Name>Tab</Name>" +
                "<AliasName>a</AliasName>" +
              "</Table>" +
              "<Table>" +
                "<Name>Tab</Name>" +
                "<AliasName>d</AliasName>" +
              "</Table>" +
            "</TableReference>" +
          "</FromClause>" +
          "<WhereClause>" +
            "<IntersectionSearch>" +
              "<FirstCondition xsi:type=\"PredicateSearch\">" +
                "<ComparisonPred>" +
                  "<FirstExpr xsi:type=\"ColumnExpr\">" +
                    "<SingleColumnReference>" +
                      "<TableName>a</TableName>" +
                      "<Name>d</Name>" +
                    "</SingleColumnReference>" +
                  "</FirstExpr>" +
                  "<Compare>&lt;</Compare>" +
                  "<SecondExpr xsi:type=\"ColumnExpr\">" +
                    "<SingleColumnReference>" +
                      "<TableName>d</TableName>" +
                      "<Name>e</Name>" +
                    "</SingleColumnReference>" +
                  "</SecondExpr>" +
                "</ComparisonPred>" +
              "</FirstCondition>" +
              "<SecondCondition xsi:type=\"PredicateSearch\">" +
                "<ComparisonPred>" +
                  "<FirstExpr xsi:type=\"ColumnExpr\">" +
                    "<SingleColumnReference>" +
                      "<TableName>a</TableName>" +
                      "<Name>f</Name>" +
                    "</SingleColumnReference>" +
                  "</FirstExpr>" +
                  "<Compare>&lt;</Compare>" +
                  "<SecondExpr xsi:type=\"ColumnExpr\">" +
                    "<SingleColumnReference>" +
                      "<TableName>d</TableName>" +
                      "<Name>f</Name>" +
                    "</SingleColumnReference>" +
                  "</SecondExpr>" +
                "</ComparisonPred>" +
              "</SecondCondition>" +
            "</IntersectionSearch>" +
          "</WhereClause>" +
        "</TableClause>" +
        "<OrderBy>" +
          "<OrderList>" +
            "<Order>" +
              "<Expr xsi:type=\"ColumnExpr\">" +
                "<SingleColumnReference>" +
                  "<TableName>t</TableName>" +
                  "<Name>f</Name>" +
                "</SingleColumnReference>" +
              "</Expr>" +
            "</Order>" +
            "<Order>" +
              "<Expr xsi:type=\"ColumnExpr\">" +
                "<SingleColumnReference>" +
                  "<TableName>g</TableName>" +
                  "<Name>b</Name>" +
                "</SingleColumnReference>" +
              "</Expr>" +
            "</Order>" +
          "</OrderList>" +
        "</OrderBy>" +
      "</Select>" +
            "  </parameter>" +
      
    "   </input>" +
    
    "   <output>" +    "   <parameter name=\"result\" type=\"MySpace_VOTableReference\" >" +    "       myspace://jl99@star.le.ac.uk/serv1/votables/votable2" +    "   </parameter>" +
    "   </output>" +
        "</tool>" +
        
        
        "</step>" +
        "</sequence>" +
        "</workflow>" ;
       
    private static final String jobTwo = 
        "<?xml version=\"1.0\" encoding=\"UTF8\"?>" +
        "<workflow name = \"{0}\" templateName=\"OneStepJob\">" +
        "<community>{1}</community>" +
        "<description>This is a one step job</description>" +
        "<!-- " +
        "    | The top level structure within a workflow is " +
        "    | always a sequence... =============================================== -->" +
        "<sequence>" +
        " <step name=\"StepOne\" stepNumber=\"1\" sequenceNumber=\"1\">" +    
        "  <tool name=\"someQueryTool\">" +
        "   <input>" +
        "      <parameter name=\"query\" type=\"MySpace_FileReference\" >" +
        "           myspace://jl99@star.le.ac.uk/serv1/query/query01.xml" +
        "      </parameter>" +        
        "   </input>" +
        "   <output>" +
        "      <parameter name=\"result\" type=\"MySpace_VOTableReference\" >" +
        "           myspace://jl99@star.le.ac.uk/serv1/votables/votable2" +
        "      </parameter>" +
        "   </output>" +
        "  </tool>" +  
        " </step>" +
        "</sequence>" +
        "</workflow>" ;
        
    private static String[] workflowTable =
        { 
            "JobOne", "JobTwo" 
        };



    private static String queryOne =
    "<?xml version=\"1.0\" encoding=\"UTF8\"?>" +
            
     "<!-- SQL equivalent" +
     "SELECT t.a, g.d FROM Tab a, Tab d WHERE a.d < d.e AND a.f < d.f ORDER BY t.f, g.b" +
     " -->" +
     "<Select xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " + 
     "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
     "<SelectionList>" +
       "<ColumnExpr>" +
         "<SingleColumnReference>" +
           "<TableName>t</TableName>" +
           "<Name>a</Name>" +
         "</SingleColumnReference>" +
       "</ColumnExpr>" +
       "<ColumnExpr>" +
         "<SingleColumnReference>" +
           "<TableName>g</TableName>" +
           "<Name>d</Name>" +
         "</SingleColumnReference>" +
       "</ColumnExpr>" +
     "</SelectionList>" +
     "<TableClause>" +
       "<FromClause>" +
         "<TableReference>" +
           "<Table>" +
             "<Name>Tab</Name>" +
             "<AliasName>a</AliasName>" +
           "</Table>" +
           "<Table>" +
             "<Name>Tab</Name>" +
             "<AliasName>d</AliasName>" +
           "</Table>" +
         "</TableReference>" +
       "</FromClause>" +
       "<WhereClause>" +
         "<IntersectionSearch>" +
           "<FirstCondition xsi:type=\"PredicateSearch\">" +
             "<ComparisonPred>" +
               "<FirstExpr xsi:type=\"ColumnExpr\">" +
                 "<SingleColumnReference>" +
                   "<TableName>a</TableName>" +
                   "<Name>d</Name>" +
                 "</SingleColumnReference>" +
               "</FirstExpr>" +
               "<Compare>&lt;</Compare>" +
               "<SecondExpr xsi:type=\"ColumnExpr\">" +
                 "<SingleColumnReference>" +
                   "<TableName>d</TableName>" +
                   "<Name>e</Name>" +
                 "</SingleColumnReference>" +
               "</SecondExpr>" +
             "</ComparisonPred>" +
           "</FirstCondition>" +
           "<SecondCondition xsi:type=\"PredicateSearch\">" +
             "<ComparisonPred>" +
               "<FirstExpr xsi:type=\"ColumnExpr\">" +
                 "<SingleColumnReference>" +
                   "<TableName>a</TableName>" +
                   "<Name>f</Name>" +
                 "</SingleColumnReference>" +
               "</FirstExpr>" +
               "<Compare>&lt;</Compare>" +
               "<SecondExpr xsi:type=\"ColumnExpr\">" +
                 "<SingleColumnReference>" +
                   "<TableName>d</TableName>" +
                   "<Name>f</Name>" +
                 "</SingleColumnReference>" +
               "</SecondExpr>" +
             "</ComparisonPred>" +
           "</SecondCondition>" +
         "</IntersectionSearch>" +
       "</WhereClause>" +
     "</TableClause>" +
     "<OrderBy>" +
       "<OrderList>" +
         "<Order>" +
           "<Expr xsi:type=\"ColumnExpr\">" +
             "<SingleColumnReference>" +
               "<TableName>t</TableName>" +
               "<Name>f</Name>" +
             "</SingleColumnReference>" +
           "</Expr>" +
         "</Order>" +
         "<Order>" +
           "<Expr xsi:type=\"ColumnExpr\">" +
             "<SingleColumnReference>" +
               "<TableName>g</TableName>" +
               "<Name>b</Name>" +
             "</SingleColumnReference>" +
           "</Expr>" +
         "</Order>" +
       "</OrderList>" +
     "</OrderBy>" +
   "</Select>"  ;

	    private static String queryTwo =
	    "<?xml version=\"1.0\" encoding=\"UTF8\"?>" +
	               
	     "<!-- SQL equivalent" +
	     "SELECT t.a, g.d FROM Tab a, Tab d WHERE a.d < d.e AND a.f < d.f ORDER BY t.f, g.b" +
	     " -->" +
	     "<Select xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" " + 
	     "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
	     "<SelectionList>" +
	       "<ColumnExpr>" +
	         "<SingleColumnReference>" +
	           "<TableName>t</TableName>" +
	           "<Name>a</Name>" +
	         "</SingleColumnReference>" +
	       "</ColumnExpr>" +
	       "<ColumnExpr>" +
	         "<SingleColumnReference>" +
	           "<TableName>g</TableName>" +
	           "<Name>d</Name>" +
	         "</SingleColumnReference>" +
	       "</ColumnExpr>" +
	     "</SelectionList>" +
	     "<TableClause>" +
	       "<FromClause>" +
	         "<TableReference>" +
	           "<Table>" +
	             "<Name>Tab</Name>" +
	             "<AliasName>a</AliasName>" +
	           "</Table>" +
	           "<Table>" +
	             "<Name>Tab</Name>" +
	             "<AliasName>d</AliasName>" +
	           "</Table>" +
	         "</TableReference>" +
	       "</FromClause>" +
	       "<WhereClause>" +
	         "<IntersectionSearch>" +
	           "<FirstCondition xsi:type=\"PredicateSearch\">" +
	             "<ComparisonPred>" +
	               "<FirstExpr xsi:type=\"ColumnExpr\">" +
	                 "<SingleColumnReference>" +
	                   "<TableName>a</TableName>" +
	                   "<Name>d</Name>" +
	                 "</SingleColumnReference>" +
	               "</FirstExpr>" +
	               "<Compare>&lt;</Compare>" +
	               "<SecondExpr xsi:type=\"ColumnExpr\">" +
	                 "<SingleColumnReference>" +
	                   "<TableName>d</TableName>" +
	                   "<Name>e</Name>" +
	                 "</SingleColumnReference>" +
	               "</SecondExpr>" +
	             "</ComparisonPred>" +
	           "</FirstCondition>" +
	           "<SecondCondition xsi:type=\"PredicateSearch\">" +
	             "<ComparisonPred>" +
	               "<FirstExpr xsi:type=\"ColumnExpr\">" +
	                 "<SingleColumnReference>" +
	                   "<TableName>a</TableName>" +
	                   "<Name>f</Name>" +
	                 "</SingleColumnReference>" +
	               "</FirstExpr>" +
	               "<Compare>&lt;</Compare>" +
	               "<SecondExpr xsi:type=\"ColumnExpr\">" +
	                 "<SingleColumnReference>" +
	                   "<TableName>d</TableName>" +
	                   "<Name>f</Name>" +
	                 "</SingleColumnReference>" +
	               "</SecondExpr>" +
	             "</ComparisonPred>" +
	           "</SecondCondition>" +
	         "</IntersectionSearch>" +
	       "</WhereClause>" +
	     "</TableClause>" +
	     "<OrderBy>" +
	       "<OrderList>" +
	         "<Order>" +
	           "<Expr xsi:type=\"ColumnExpr\">" +
	             "<SingleColumnReference>" +
	               "<TableName>t</TableName>" +
	               "<Name>f</Name>" +
	             "</SingleColumnReference>" +
	           "</Expr>" +
	         "</Order>" +
	         "<Order>" +
	           "<Expr xsi:type=\"ColumnExpr\">" +
	             "<SingleColumnReference>" +
	               "<TableName>g</TableName>" +
	               "<Name>b</Name>" +
	             "</SingleColumnReference>" +
	           "</Expr>" +
	         "</Order>" +
	       "</OrderList>" +
	     "</OrderBy>" +
	   "</Select>" ;






    private static String[] queryTable =
        { 
            "QueryOne", "QueryTwo" 
        };
       
        
          
    /*
     * At present this returns just an Iterator of string Objects representing the names
     * of the files.
     */
    public static Vector readWorkflowList( String communitySnippet
                                         , String filter ) {
        if( TRACE_ENABLED ) trace( "WorkflowHelper.readWorkflowList() entry") ; 
        
        // JBL: For the moment we are ignoring filter.
        
        java.util.Vector
           vector = null ;
        
        try {
            
            vector = new Vector( 10 ) ;
                                              
            for( int i = 0; i < workflowTable.length; i++ ) {
                vector.add( i, workflowTable[i] ) ;
            }
                          
        }
        catch ( Exception ex ) {
            ex.printStackTrace() ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "WorkflowHelper.readWorkflowList() exit") ; 
        }
       
        return vector ;
            
    } // end of readWorkflowList()
    
    
    public static Workflow readWorkflow( String communitySnippet
                                       , String name ) {
        if( TRACE_ENABLED ) trace( "WorkflowHelper.readWorkflow() entry") ; 
        
        Workflow
            workflow = null;
        String
            xmlString= null ;
         
        try {
            Object []
                inserts = new Object[2] ;
            inserts[0] = name ;
            inserts[1] = communitySnippet ;
            
            xmlString = MessageFormat.format( jobOne, inserts ) ;
            
            InputSource
               source = new InputSource( new StringReader( xmlString ) );
                         
            workflow = new Workflow( communitySnippet, XMLUtils.newDocument(source) ) ;
      
        }
        catch ( Exception ex ) {
            ex.printStackTrace() ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "WorkflowHelper.readWorkflow() exit") ; 
        }
       
        return workflow ;
        
    } // end of readWorkflow() 
    
    

    public static boolean deleteWorkflow( String communitySnippet
                                        , String name  ) {
        if( TRACE_ENABLED ) trace( "WorkflowHelper.deleteWorkflow() entry") ; 
        
        boolean
            retValue = false ;
         
        try {           
            retValue = true ;
        }
        catch( Exception ex ) {
            ex.printStackTrace() ;
        }
        finally {
            if( TRACE_ENABLED ) trace( "WorkflowHelper.deleteWorkflow() exit") ; 
        }
        
        return retValue ;
        
    } // end of deleteWorkflow()
    
    

    public static boolean saveWorkflow( Workflow workflow ) {
        if( TRACE_ENABLED ) trace( "WorkflowHelper.saveWorkflow() entry") ; 
        
     boolean
         retValue = false ;    
         
     try {
         retValue = true ;

     }
     catch( Exception ex ) {
         ex.printStackTrace() ;
     }
     finally {
         if( TRACE_ENABLED ) trace( "WorkflowHelper.saveWorkflow() exit") ; 
     }
        
     return retValue ;

    } // end of saveWorkflow()
    
    
    public static Vector readQueryList( String communitySnippet
                                      , String filter ) {
        if( TRACE_ENABLED ) trace( "WorkflowHelper.readQueryList() entry") ; 
        
        // JBL: For the moment we are ignoring filter.
        
         java.util.Vector
            vector = null ;
        
         try {
            
             vector = new Vector( 10 ) ;
                                              
             for( int i = 0; i < queryTable.length; i++ ) {
                 vector.add( i, queryTable[i] ) ;
             }
                          
         }
         catch ( Exception ex ) {
             ex.printStackTrace() ;
         }
         finally {
             if( TRACE_ENABLED ) trace( "WorkflowHelper.readQueryList() exit") ; 
         }
       
         return vector ;

    } // end of readQueryList()
    
    
    
    public static String readQuery( String communitySnippet
                                  , String name ) {
        if( TRACE_ENABLED ) trace( "WorkflowHelper.readQuery() entry") ; 
         
        try {
            return queryOne ;
         }
         finally {
             if( TRACE_ENABLED ) trace( "WorkflowHelper.readQuery() exit") ; 
         }
        
    } // end of readQuery()
    
    


    
    
       
    private static void trace( String traceString ) {
        System.out.println( traceString ) ;
        // logger.debug( traceString ) ;
    }
    
    private static void debug( String logString ){
        System.out.println( logString ) ;
        // logger.debug( logString ) ;
    }


} // end of MySpaceHelper
