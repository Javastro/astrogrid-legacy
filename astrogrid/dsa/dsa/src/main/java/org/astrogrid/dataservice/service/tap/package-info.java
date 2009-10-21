/**
 * Servlets and support classes to implement the IVOA TAP protocol.
 * <p>
 * {@link org.astrogrid.dataservice.service.tap.TapSyncServlet} receives
 * requests to the /sync resource; this resource conflates several
 * logically-separate things. The current servlet works out which real
 * resource is required and hands off to a servlet that provides that resource.
 * <p>
 * {@link org.astrogrid.dataservice.service.tap.TapSyncQueryServlet} handles
 * synchronous data and metadata queries.
 * <p>
 * {@link org.astrogrid.dataservice.service.tap.UwsServlet} filters all
 * requests to the UWS machinery. It determines from the request path which
 * UWS object is intended and forwards to an appropriate object. This servlet
 * adds the job ID to the forwarded request as an attribute.
 * <p>
 * There is a set of servlets to handle requests to UWS resources:
 * </p>
 * <ul>
 * <li>{@link org.astrogrid.dataservice.service.tap.UwsJobListServlet}</li>
 * <li>{@link org.astrogrid.dataservice.service.tap.UwsJobServlet}</li>
 * <li>{@link org.astrogrid.dataservice.service.tap.UwsJobPhaseServlet}</li>
 * <li>{@link org.astrogrid.dataservice.service.tap.UwsResultServlet}</li>
 * </ul>
 * <p>
 * All these servlets are designed to receive forwarded requests from
 * {@code UwsServlet}, and not to be mapped directly to URIs. They need to
 * see the attribute {@code uws.job} in their requests. This set of servlets
 * are extensions of {@link org.astrogrid.dataservice.service.tap.UwsResourceServlet}
 * which contains some common machinery for handling queries and UWS jobs.
 * <p>
 * Several of the servlets, for both synchronous and UWS calls, inherit from
 * {@link org.astrogrid.dataservice.service.tap.AbstractTapServlet} in order
 * to share utility methods.
 *
 */
package org.astrogrid.dataservice.service.tap;