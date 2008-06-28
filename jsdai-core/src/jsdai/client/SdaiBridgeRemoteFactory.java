/*
 * $Id$
 *
 * JSDAI(TM), a way to implement STEP, ISO 10303
 * Copyright (C) 1997-2008, LKSoftWare GmbH, Germany
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License
 * version 3 as published by the Free Software Foundation (AGPL v3).
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * JSDAI is a registered trademark of LKSoftWare GmbH, Germany
 * This software is also available under commercial licenses.
 * See also http://www.jsdai.net/
 */

package jsdai.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import jsdai.lang.SdaiException;


/**
 * <p>Factory for creating {@link SdaiBridgeRemote} instances, with discovery and
 * configuration features similar to that employed by standard Java APIs
 * such as JAXP.</p>
 *
 * <p>Created: Fri Dec 17 10:28:49 2004</p>
 *
 * <p><strong>IMPLEMENTATION NOTE</strong> - This implementation is heavily
 * based on the SAXParserFactory and DocumentBuilderFactory implementations
 * (corresponding to the JAXP pluggability APIs) found in Apache Xerces.</p>
 *
 * @author <a href="mailto:vaidas.nargelas@lksoft.lt">Vaidas Nargelas</a>
 * @version $Revision$
 */

public abstract class SdaiBridgeRemoteFactory {

    protected static final String FACTORY_PROPERTY =
		"jsdai.client.SdaiBridgeRemoteFactory";

    protected static final String FACTORY_SERVICE_ID =
		"META-INF/services/jsdai.client.SdaiBridgeRemoteFactory";

	protected SdaiBridgeRemoteFactory() { }

	public abstract SdaiBridgeRemote newSdaiBridgeRemote() throws SdaiException;

	/**
	 * Usage:</br>
	 * <code>SdaiBridgeRemoteFactory sdaiBridgeRemoteFactory = </code></br>
	 * <code>    SdaiBridgeRemoteFactory.newInstance();</code></br>
	 * <code>SdaiBridgeRemote sdaiBridgeRemote = </code></br>
	 * <code>    sdaiBridgeRemoteFactory.newSdaiBridgeRemote();</code></br>
	 *
	 */
	public static SdaiBridgeRemoteFactory newInstance() throws SdaiException {
		try {
			SdaiBridgeRemoteFactory factory = null;
			// Identify the class loader we will be using
			ClassLoader contextClassLoader = 
				(ClassLoader)AccessController.doPrivileged(new PrivilegedExceptionAction() {
						public Object run() throws SdaiException {
							return getContextClassLoader();
						}
					});

			// First, try the system property
			try {
				String factoryClassName = System.getProperty(FACTORY_PROPERTY);
				if(factoryClassName != null) {
					factory = createFactory(factoryClassName, contextClassLoader);
				}
			} catch (SecurityException e) {
				;  // ignore
			}

			// Second, try to find a service by using the JDK1.3 jar
			// discovery mechanism. This will allow users to plug a logger
			// by just placing it in the lib/ directory of the webapp ( or in
			// CLASSPATH or equivalent ). This is similar to the second
			// step, except that it uses the (standard?) jdk1.3 location in the jar.
			if (factory == null) {
				try {
					String factoryClassName = getService(contextClassLoader, FACTORY_SERVICE_ID);
					if(factoryClassName != null && ! "".equals(factoryClassName)) {
						factory = createFactory(factoryClassName, contextClassLoader );
					}
				} catch( Exception ex ) {
					;
				}
			}
			if(factory == null) {
				throw new SdaiException(SdaiException.SY_ERR,
										"Could not create SdaiBridgeRemoteFactory instance " +
										"possibly because no suitable implementation found");
			}
			return factory;

		} catch (PrivilegedActionException e) {
			// e.getException() should be an instance of SdaiException,
			// as only "checked" exceptions will be "wrapped" in a
			// PrivilegedActionException.
			throw (SdaiException)e.getException();
		}
	}

    /**
     * Return the thread context class loader if available.
     * Otherwise return null.
     *
     * The thread context class loader is available for JDK 1.2
     * or later, if certain security conditions are met.
     *
     * @exception LogConfigurationException if a suitable class loader
     * cannot be identified.
     */
    protected static ClassLoader getContextClassLoader() throws SdaiException {
        ClassLoader classLoader = null;

        try {
            // Are we running on a JDK 1.2 or later system?
            Method method = Thread.class.getMethod("getContextClassLoader", null);
			
            // Get the thread context class loader (if there is one)
            try {
                classLoader = (ClassLoader)method.invoke(Thread.currentThread(), null);
            } catch (IllegalAccessException e) {
                throw (SdaiException)new SdaiException(SdaiException.SY_ERR,
													   "Unexpected IllegalAccessException").initCause(e);
            } catch (InvocationTargetException e) {
                /**
                 * InvocationTargetException is thrown by 'invoke' when
                 * the method being invoked (getContextClassLoader) throws
                 * an exception.
                 *
                 * getContextClassLoader() throws SecurityException when
                 * the context class loader isn't an ancestor of the
                 * calling class's class loader, or if security
                 * permissions are restricted.
                 *
                 * In the first case (not related), we want to ignore and
                 * keep going.  We cannot help but also ignore the second
                 * with the logic below, but other calls elsewhere (to
                 * obtain a class loader) will trigger this exception where
                 * we can make a distinction.
                 */
                if (e.getTargetException() instanceof SecurityException) {
                    ;  // ignore
                } else {
                    // Capture 'e.getTargetException()' exception for details
                    // alternate: log 'e.getTargetException()', and pass back 'e'.
                    throw (SdaiException)new SdaiException(SdaiException.SY_ERR,
														   "Unexpected InvocationTargetException")
						.initCause(e.getTargetException());
                }
            }
        } catch (NoSuchMethodException e) {
            // Assume we are running on JDK 1.1
            classLoader = SdaiBridgeRemoteFactory.class.getClassLoader();
        }

        // Return the selected class loader
        return classLoader;
    }

	/**
	 * Return a new instance of the specified <code>SdaiBridgeRemoteFactory</code>
	 * implementation class, loaded by the specified class loader.
	 * If that fails, try the class loader used to load this
	 * (abstract) SdaiBridgeRemoteFactory.
	 *
	 */
	protected static SdaiBridgeRemoteFactory createFactory(final String factoryClassName,
														   final ClassLoader classLoader
														   ) throws SdaiException,
																	PrivilegedActionException {
        Object result = AccessController.doPrivileged(
            new PrivilegedExceptionAction() {
                public Object run() throws SdaiException {
                    // This will be used to diagnose bad configurations
                    // and allow a useful message to be sent to the user
                    Class factoryClass = null;
                    try {
                        if(classLoader != null) {
                            try {
                                // First the given class loader param (thread class loader)

                                // Warning: must typecast here & allow exception
                                // to be generated/caught & recast properly.
                                factoryClass = classLoader.loadClass(factoryClassName);
                                return (SdaiBridgeRemoteFactory)factoryClass.newInstance();

                            } catch (ClassNotFoundException e) {
                                if (classLoader == SdaiBridgeRemoteFactory.class.getClassLoader()) {
                                    // Nothing more to try, onwards.
                                    throw e;
                                }
                                // ignore exception, continue
                            } catch (NoClassDefFoundError e) {
                                if (classLoader == SdaiBridgeRemoteFactory.class.getClassLoader()) {
                                    // Nothing more to try, onwards.
                                    throw e;
                                }

                            } catch(ClassCastException e){

                              if (classLoader == SdaiBridgeRemoteFactory.class.getClassLoader()) {
                                    // Nothing more to try, onwards (bug in loader implementation).
                                    throw e;
                               }
                            }
                            // Ignore exception, continue
                        }

                        /* At this point, either classLoader == null, OR
                         * classLoader was unable to load factoryClass.
                         * Try the class loader that loaded this class:
                         * SdaiBridgeRemoteFactory.getClassLoader().
                         *
                         * Notes:
                         * a) SdaiBridgeRemoteFactory.class.getClassLoader() may return 'null'
                         *    if SdaiBridgeRemoteFactory is loaded by the bootstrap classloader.
                         * b) The Java endorsed library mechanism is instead
                         *    Class.forName(factoryClassName);
                         */
                        // Warning: must typecast here & allow exception
                        // to be generated/caught & recast properly.
                        factoryClass = Class.forName(factoryClassName);
                        return (SdaiBridgeRemoteFactory)factoryClass.newInstance();
                    } catch (Exception e) {
                        // Check to see if we've got a bad configuration
                        if (factoryClass != null
                            && !SdaiBridgeRemoteFactory.class.isAssignableFrom(factoryClass)) {
                            return (SdaiException)
								new SdaiException(SdaiException.SY_ERR,
												  "The chosen SdaiBridgeRemoteFactory " +
												  "implementation does not extend " +
												  "SdaiBridgeRemoteFactory. " +
												  "Please check your configuration.").initCause(e);
                        }
                        return (SdaiException)new SdaiException(SdaiException.SY_ERR,
																e.getMessage()).initCause(e);
                    }
                }
            });

        if(result instanceof SdaiException) {
            throw (SdaiException)result;
		} else {
			return (SdaiBridgeRemoteFactory)result;
		}
    }

	protected static String getService(final ClassLoader contextClassLoader, final String serviceId
									   ) throws IOException {
        InputStream is = (InputStream)AccessController.doPrivileged(new PrivilegedAction() {
                public Object run() {
                    if (contextClassLoader != null) {
                        return contextClassLoader.getResourceAsStream(serviceId);
                    } else {
                        return ClassLoader.getSystemResourceAsStream(serviceId);
                    }
                }
            });

		if(is != null) {
			// This code is needed by EBCDIC and other strange systems.
			// It's a fix for bugs reported in xerces
			BufferedReader rd;
			try {
				rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				rd = new BufferedReader(new InputStreamReader(is));
			}

			String service = rd.readLine();
			rd.close();
			return service;
		}
		return null;
	}

} // SdaiBridgeRemoteFactory
