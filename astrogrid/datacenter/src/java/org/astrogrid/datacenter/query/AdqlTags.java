/*
 * $Id: AdqlTags.java,v 1.1 2003/08/26 16:52:05 mch Exp $
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */
package org.astrogrid.datacenter.query;

/**
 * Defines the tags used in the ADQL query when in XML form.
 * <p>
 * NB at the moment (26th Aug 2003) this is not real ADQL as it's not defined
 * yet...
 */
public interface AdqlTags
{
   public static final String
       QUERY_ELEMENT = "query",
       FROM_ELEMENT = "from",
       ORDER_ELEMENT = "order",
       GROUP_ELEMENT = "group",
       SUBQUERY_ELEMENT = "subquery";

   public static final String
       CATALOG_ELEMENT = "catalog",
       CATALOG_NAME_ATTR = "name" ;

   public static final String
       SERVICE_ELEMENT = "service",
       SERVICE_NAME_ATTR = "name",
       SERVICE_URL_ATTR = "url";

   public static final String
       TABLE_ELEMENT = "table",
       TABLE_NAME_ATTR = "name" ;

   public static final String
       RETURN_ELEMENT = "return";

   public static final String
       CRITERIA_ELEMENT = "criteria";

   public static final String
       FIELD_ELEMENT = "field",
       FIELD_NAME_ATTR = "name",
       FIELD_TYPE_ATTR = "type",
       FIELD_TYPE_UCD = "UCD",
       FIELD_TYPE_COLUMN = "COLUMN",
       FIELD_TYPE_PASSTHROUGH = "PASSTHROUGH" ;

   public static final String
       OP_ELEMENT = "operation",
       OP_NAME_ATTR = "name",
       OP_NAME_ORDER = "order",
       OP_NAME_AND = "AND",
       OP_NAME_OR = "OR",
       OP_NAME_NOT = "NOT",
       OP_NAME_LT = "LESS_THAN",
       OP_NAME_GT = "GREATER_THAN",
       OP_NAME_DIFFERENCE = "DIFFERENCE",
       OP_NAME_AVERAGE = "AVERAGE",
       OP_NAME_CONE = "CONE",
       OP_NAME_EQUALS ="EQUALS",
       OP_NAME_GTE = "GREATER_THAN_OR_EQUALS",
       OP_NAME_LTE = "LESS_THAN_OR_EQUALS",
       OP_NAME_IN = "IN",
       OP_NAME_ANY = "ANY",
       OP_NAME_LIKE = "LIKE",
       OP_NAME_ALL = "ALL" ;

}
