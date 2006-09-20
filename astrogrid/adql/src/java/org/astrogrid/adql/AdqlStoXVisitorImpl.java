/*
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.adql;

import org.apache.xmlbeans.*;
import org.astrogrid.adql.v1_0.beans.*;

/**
 * AdqlStoXVisitorImpl
 *
 *
 * @author Jeff Lusted jl99@star.le.ac.uk
 * Sep 18, 2006
 */
public class AdqlStoXVisitorImpl implements AdqlStoXVisitor {

	/* (non-Javadoc)
	 * @see org.astrogrid.adql.AdqlStoXVisitor#visit(org.astrogrid.adql.SimpleNode, java.lang.Object)
	 */
	public Object visit( SimpleNode node, Object data ) {
		return null ;
	}
	

	/* (non-Javadoc)
	 * @see org.astrogrid.adql.AdqlStoXVisitor#visit(org.astrogrid.adql.ASTFrom, java.lang.Object)
	 */
	public Object visit(ASTFrom node, Object data) {
		SelectType selectType = (SelectType)data ;
		FromType fromType = selectType.addNewFrom() ;
		node.childrenAccept( this, fromType ) ;
		FromTableType[] tableRefArray = new FromTableType[ node.tableRefList.size() ] ;
		fromType.setTableArray( (FromTableType[])node.tableRefList.toArray(tableRefArray) ) ;
		return fromType ;
	}

	/* (non-Javadoc)
	 * @see org.astrogrid.adql.AdqlStoXVisitor#visit(org.astrogrid.adql.ASTGroupBy, java.lang.Object)
	 */
	public Object visit(ASTGroupBy node, Object data) {
		SelectType selectType = (SelectType)data ;
		GroupByType groupByType = selectType.addNewGroupBy() ;
		node.childrenAccept( this, groupByType ) ;
		return groupByType ;
	}

	/* (non-Javadoc)
	 * @see org.astrogrid.adql.AdqlStoXVisitor#visit(org.astrogrid.adql.ASTHaving, java.lang.Object)
	 */
	public Object visit(ASTHaving node, Object data) {
		SelectType selectType = (SelectType)data ;
		HavingType havingType = selectType.addNewHaving() ;
		node.childrenAccept( this, havingType ) ;
		return havingType ;
	}

	/* (non-Javadoc)
	 * @see org.astrogrid.adql.AdqlStoXVisitor#visit(org.astrogrid.adql.ASTOrderBy, java.lang.Object)
	 */
	public Object visit(ASTOrderBy node, Object data) {
		SelectType selectType = (SelectType)data ;
		OrderExpressionType orderType = selectType.addNewOrderBy() ;
		node.childrenAccept( this, orderType ) ;
		return orderType ;
	}

	/* (non-Javadoc)
	 * @see org.astrogrid.adql.AdqlStoXVisitor#visit(org.astrogrid.adql.ASTSelect, java.lang.Object)
	 */
	public Object visit(ASTSelect node, Object data) {
		SelectDocument selectDoc = (SelectDocument)data ;
		SelectType selectType = selectDoc.addNewSelect() ;
		node.childrenAccept( this, selectType ) ;
		return selectType ;
	}

	/* (non-Javadoc)
	 * @see org.astrogrid.adql.AdqlStoXVisitor#visit(org.astrogrid.adql.ASTSelectionList, java.lang.Object)
	 */
	public Object visit(ASTSelectionList node, Object data) {
		SelectType selectType = (SelectType)data ;
		SelectionListType selectionList = selectType.addNewSelectionList() ;
		node.childrenAccept( this, selectionList ) ;
		SelectionItemType[] itemArray = new SelectionItemType[ node.itemList.size() ] ;	
		selectionList.setItemArray( (SelectionItemType[])node.itemList.toArray(itemArray) ) ;
		return selectionList ;
	}

	/* (non-Javadoc)
	 * @see org.astrogrid.adql.AdqlStoXVisitor#visit(org.astrogrid.adql.ASTRestrict, java.lang.Object)
	 */
	public Object visit(ASTRestrict node, Object data) {
		SelectType selectType = (SelectType)data ;
		SelectionLimitType selectionLimit = selectType.addNewRestrict() ;
		selectionLimit.setTop( node.getValue() ) ;
		return selectionLimit ;
	}

	/* (non-Javadoc)
	 * @see org.astrogrid.adql.AdqlStoXVisitor#visit(org.astrogrid.adql.ASTAllow, java.lang.Object)
	 */
	public Object visit(ASTAllow node, Object data) {
		SelectType selectType = (SelectType)data ;
		SelectionOptionType selectionOption = selectType.addNewAllow() ;
		selectionOption.setOption( node.getValue() ) ;
		return selectionOption ;
	}

	/* (non-Javadoc)
	 * @see org.astrogrid.adql.AdqlStoXVisitor#visit(org.astrogrid.adql.ASTWhere, java.lang.Object)
	 */
	public Object visit(ASTWhere node, Object data) {
		SelectType selectType = (SelectType)data ;
		WhereType whereType = selectType.addNewWhere() ;
		node.childrenAccept( this, whereType ) ;
		return whereType ;
	}

}
