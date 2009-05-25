/**
 * Classes for low-level control of the community database.
 * <p>
 * The database is accessed through a Castor-JDO wrapper. The
 * {@link DatabaseConfiguration} class makes that wrapper easier
 * to use by taking care of configuration via the software environment.
 * To access the JDO layer, first obtain a DatabaseConfiguration instance.
 * On that,  call the getDatabase() method to obtain a Database instance;
 * the latter exposes the JDO interface for queries and updates.
 * <p>
 * {@link DatabaseConfigurationTestData} is used for unit testing
 * of this package and also in the "health test" performed on a
 * database in an installed community.
 */
package org.astrogrid.community.server.database.configuration;