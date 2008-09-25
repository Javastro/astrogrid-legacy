/**
 * 
 */
package org.astrogrid.acr.astrogrid;


import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Application;


/** Registry description of a CEA application.
 * 
 * A CEA application describes a number of parameters ({@code p1},{@code p2},{@code p3} in the figure below). 
 * Each parameter is modelled by a {@link ParameterBean} which describes the 
 * name, type, and use of a parameter. The CEA application also describes a number of interfaces
 * ({@code i1},{@code i2} in the figure}, which represent a set of parameters
 * that the application can be invoked with. Each interface is modelled by a {@link InterfaceBean}, which contains
 * a list of {@link ParameterReferenceBean} which refer back to the parameters.
 * <br />
 * {@textdiagram ceaParams

 /----------------\    /--\
 |                +----+i1+---+
 | CeaApplication |    \--/   |
 |                +--+        |
 \-+----+----+----/  | /--\   |
   |    |    |       +-+i2+-+ |
   |    |    |         \--/ | |
 /-+\ /-+\ /-+\         .   | |
 |p1| |p2| |p3| ...     .   | :
 \--/ \--/ \--/         .   : |
  ^     ^   ^^              | |
  |     |   ||              | |
  :     :   ::              | |
  |     +----*--------------+ |
  |         :                 |
  +---------*-----------------+


 * }
 * @bean
 * @author Noel Winstanley
 * @see Applications Executing remote applications
 * @see Registry Querying for registry resources
 */
public interface CeaApplication extends Application {
    /** The interfaces (parameter sets) provided by this application.
     * @return an array of interface descriptions
     * @xmlrpc key will be {@code interfaces}, type will be array.
     */	
    public InterfaceBean[] getInterfaces();
    /** The Parameters used in this application.
     * @return an array of parameter descriptions
     * @xmlrpc key will be {@code parameters}, type will be array.
   */    
    public ParameterBean[] getParameters();
    
    /**
     *             The type of the underlying application - purely descriptive.
     */
    public String getApplicationKind() ;
}
