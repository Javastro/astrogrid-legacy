/**
 * Sesame.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis WSDL2Java emitter.
 */

package org.astrogrid.acr.cds;

import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.ServiceException;
/** AR Service: Sesame Object Name Resolver, from CDS.
 * Resolve object  names to position by querying  Simbad and/or NED and/or VizieR.
 * <p />
 * {@link #resolve} resolves an object name to a datastructure containing position, error, aliases, etc.
 * <p />
 * {@link #sesame} and {@link #sesameChooseService}
 * provide low-level access to the raw webservice.
 * @see <a href="http://cdsweb.u-strasbg.fr/cdsws/name_resolver.gml">Sesame Webservice Description</a>
 * @see #resolve Example of resolving object name to position.
 * @author CDS
 *@service cds.sesame
 */
public interface Sesame {

	/** Resolve an object name to a position using Sesame.
	 * 
	 * {@example "Python Example"
     *  # connect to the AR
     * from xmlrpc import Server
     * from os.path import expanduser
     * ar = Server(file(expanduser('~/.astrogrid-desktop')).next().strip() +'xmlrpc')
     *  #call this function
     * posBean = ar.cds.sesame.resolve('ngc123')
     *  #see what we get
     * from pprint import pprint
     * pprint(posBean)
     *  #access some variables
     * print "Position",posBean['ra'],posBean['dec']
     * } 
     * 
     * The output is 
     * <pre>
     * {'OName': '{NGC} 123',
     * 'aliases': [],
     * 'dec': -1.6000000000000001,
     * 'decErr': 0.0,
     * 'posStr': '00:27.8     -01:36     ',
     * 'ra': 6.9500000000000002,
     * 'raErr': 0.0,
     * 'service': 'VizieR',
     * 'target': 'ngc123'}
     * Position 6.95 -1.6
     * </pre>
	 * 

	 * @param name the object name to resolve 
	 * @return a datastructure of positional information about the object.
	 * @throws ServiceException if the sesame service could not be contacted
	 * @throws NotFoundException if the named object is not known to sesame.
	 */
	public SesamePositionBean resolve(String name) throws ServiceException, NotFoundException;
	
    /** resolve a name to position 
  * @param name the name to resolve
     * @param resultType
     * <dl>
     *           <dt>{@code u}</dt><dd>usual (corresponding to the deprecated Sesame(String name) output)</dd>
                <dt>{@code H}</dt><dd>html</dd> 
                <dt>{@code x}</dt><dd>XML</dd> 
       </dl>
                {@code p}(for plain (text/plain)) and {@code i} (for all identifiers) options can be added to {@code H} or {@code x}
     * @return format depending on the resultType parameter
     * @throws ServiceException
     * @see <a href="http://vizier.u-strasbg.fr/xml/sesame_1.dtd">Sesame DTD</a>
     * @see <a href="http://vizier.u-strasbg.fr/xml/sesame_1.xsd">Sesame Schema</a>
     */
    public java.lang.String sesame(java.lang.String name, java.lang.String resultType)throws ServiceException;
    /** resolve a name, selecting which services to use.
  * @param name the name to resolve
     * @param resultType
     * <dl>
     *           <dt>{@code u}</dt><dd>usual (corresponding to the deprecated Sesame(String name) output)</dd>
                <dt>{@code H}</dt><dd>html</dd> 
                <dt>{@code x}</dt><dd>XML</dd> 
       </dl>
                {@code p}(for plain (text/plain)) and {@code i} (for all identifiers) options can be added to {@code H} or {@code x}
     * @param all true if all identifiers wanted 
     * @param service
     * <dl>
     *   <dt>{@code S}</dt><dd>Simbad</dd>
        <dt>{@code N}</dt><dd>NED</dd> 
        <dt>{@code V}</dt><dd>VizieR</dd>
        <dt>{@code A}</dt><dd>all</dd>
        </dl>
        (examples : {@code S} to query Simbad, {@code NS} to query  Ned if not found in Simbad,
        A to query in Ned, Simbad and VizieR, ...)

     * @return format depending on the resultTtype parameter

     * @throws ServiceException
     * @see <a href="http://vizier.u-strasbg.fr/xml/sesame_1.dtd">Sesame DTD</a>
     * @see <a href="http://vizier.u-strasbg.fr/xml/sesame_1.xsd">Sesame Schema</a>      
     */
    public java.lang.String sesameChooseService(java.lang.String name, java.lang.String resultType, boolean all, java.lang.String service) throws ServiceException;
 
}
