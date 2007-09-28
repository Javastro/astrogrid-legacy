package org.astrogrid.applications.commandline;

import java.util.List;

/**
 * A list of jobs in a CEA server. This interface is the same as java.util.List
 * with the additional assurance that each entry in the List can be cast to
 * org.astrogrid.applications.Application.
 *
 * @author Guy Rixon
 */
public interface JobList extends List {}
