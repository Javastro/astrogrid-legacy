package org.astrogrid.store.delegate.myspaceItn05;

/**
 * The <code>EntryCodes</code> class defines the values for the various
 * codes specifying the type of each entry (ie. file) held in a MySpace
 * Service.  The alternatives are things like: VOTable, query, workflow
 * etc.
 *
 * @author A C Davenhall (Edinburgh)
 * @since Iteration 5.
 * @version Iteration 5.
 */

public class EntryCodes
{  

//
//Public constants defining the permitted codes for the entry type.

/**
 * The entry is of an unknown type.
 */
   public static final int UNKNOWN = 0;  // Unknown.

/**
 * The entry is a container.
 */
   public static final int CON = 1;      // Container.

/**
 * The entry is a VOTable.
 */
   public static final int VOT = 2;      // VOTable (XML).

/**
 * The entry is a query (XML).
 */
   public static final int QUERY = 3;    // Query (XML).

/**
 * The entry is a workflow (XML).
 */
   public static final int WORKFLOW = 4; // Workflow (XML).

/**
 * The entry is generic XML of unrecognised type.
 */
   public static final int XML = 5;      // Generic XML.

}
