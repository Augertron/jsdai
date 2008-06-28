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

package jsdai.lang;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.NoSuchElementException;

import jsdai.runtime.RuntimePlugin;

import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

/**
 * @author vaidas
 * @version $Revision$
 *
 */
class SdaiClassLoaderProvider {

	static SdaiClassLoaderProvider sdaiClassLoaderProvider = null;
	
	private final ClassLoader classLoader;

	private SdaiClassLoaderProvider() {
		ClassLoader parentClassLoader = SdaiSession.class.getClassLoader();
		if(parentClassLoader == null) {
			parentClassLoader = ClassLoader.getSystemClassLoader();
		}
		classLoader = new SdaiClassLoader(parentClassLoader);
	}
	
	static synchronized SdaiClassLoaderProvider getDefault() {
		if(sdaiClassLoaderProvider == null) {
			sdaiClassLoaderProvider = new SdaiClassLoaderProvider();
		}
		return sdaiClassLoaderProvider;
	}

	static synchronized void dispose() {
		sdaiClassLoaderProvider = null;
	}

	ClassLoader getClassLoader() {
		return classLoader;
	}

	/**
	 * @author vaidas
	 * @version $Revision$
	 *
	 */
	private class SdaiClassLoader extends ClassLoader {
		
		private final Bundle[] bundles;

		private SdaiClassLoader(ClassLoader parent) {
			super(parent);
			IExtension[] libExtensions =
				Platform.getExtensionRegistry().getExtensionPoint(
						RuntimePlugin.PLUGIN_ID, "libraries").getExtensions();
			Collection bundleCollection = new ArrayList();
			for (int i = 0; i < libExtensions.length; i++) {
				IExtension libExtension = libExtensions[i];
				if(libExtension.isValid()) {
					bundleCollection.add(Platform.getBundle(libExtension.getContributor().getName()));
				}
			}
			bundles =
				(Bundle[]) bundleCollection.toArray(new Bundle[bundleCollection.size()]); 
		}

		protected Class findClass(String name) throws ClassNotFoundException {
			ClassNotFoundException notFoundException = null;
			for (int i = 0; i < bundles.length; i++) {
				try {
					return bundles[i].loadClass(name);
				} catch (ClassNotFoundException e) {
					notFoundException = e;
				}
			}
			throw notFoundException != null ? notFoundException : new ClassNotFoundException(name);
		}
		
		protected URL findResource(String name) {
			for (int i = 0; i < bundles.length; i++) {
				URL resource = bundles[i].getResource(name);
				if(resource != null) {
					return resource;
				}
			}
			return null;
		}
		
		protected Enumeration findResources(String name) throws IOException {
	        final Enumeration[] enumArray = new Enumeration[bundles.length];
			for (int i = 0; i < bundles.length; i++) {
				enumArray[i] = bundles[i].getResources(name);
			}
	        return new Enumeration() {

	        	/** index in the enums array */
	        	private int index = 0;

	        	public boolean hasMoreElements() {
	        		while (index < enumArray.length) {
	        			if (enumArray[index] != null
	        					&& enumArray[index].hasMoreElements()) {
	        				return true;
	        			}
	        			index++;
	        		}
	        		return false;
	        	}

			    public Object nextElement() throws NoSuchElementException {
			    	if (hasMoreElements()) {
			    		return enumArray[index].nextElement();
			    	}
			    	throw new NoSuchElementException();
			    }
			};
		}
	}

}
