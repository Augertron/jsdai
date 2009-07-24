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
package jsdai.common.utils;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import jsdai.util.JarFileURLStreamHandler;

/**
 * @author vaidas
 *
 */
public class JarFileClassLoader extends ClassLoader {
	private static final Enumeration emptyEnumeration = new Enumeration() {
		public boolean hasMoreElements() {
			return false;
		}

		public Object nextElement() {
			return null;
		}
	};

	private File[] files;
	private String[] fileUrlStrings;
	private JarFileURLStreamHandler[] jarUrlStreamHandlers;
	private String[] exceptions;
	private boolean open;

	/**
	 * @param files
	 * @param parent
	 * @param exceptions an array of exception resource strings. The resources whose names start with
	 *        any of exception string in this array do not get loaded by the loader and are delegated
	 *        to the parent loader.</br>
	 *        <b>IMPORTANT</b>: Special care has to be taken of exceptions, the classes there 
	 *        have to have no dependencies to other resources loadable by this loader
	 *        but the ones that are also included in the set.
	 * @throws IOException
	 */
	public JarFileClassLoader(File[] files, ClassLoader parent, String[] exceptions) throws IOException {
		super(parent);
		this.files = files;
		this.exceptions = exceptions;
		fileUrlStrings = new String[files.length];
		for(int i = 0; i < files.length; i++) {
			fileUrlStrings[i] = files[i].toURL().toString(); 
		}
		jarUrlStreamHandlers = new JarFileURLStreamHandler[files.length];
		for(int i = 0; i < files.length; i++) {
			jarUrlStreamHandlers[i] = new JarFileURLStreamHandler(new JarFile(files[i])); 
		}
		open = true;
	}

	public void close() throws IOException {
		for(int i = 0; i < files.length; i++) {
			jarUrlStreamHandlers[i].getJarFile().close(); 
		}
		open = false;
	}

	/* This implementation breaks java convention by first trying to load class
	 * file using this loader and then delegates it to the parent loader
	 * (to superclass implementation actually). However this should ensure correct behavior
	 * for JSDAI class loading  
	 * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
	 */
	protected synchronized Class loadClass(String name, boolean resolve) throws ClassNotFoundException {
		Class c = findLoadedClass(name);
		if(c == null) {
			c = findClassOrNull(name);
			if(c == null) {
				return super.loadClass(name, resolve);
			}
		}
		if (resolve) {
			resolveClass(c);
		}
		return c;
	}

	/* (non-Javadoc)
	 * @see java.lang.ClassLoader#findClass(java.lang.String)
	 */
	protected Class findClass(String name) throws ClassNotFoundException {
		throw new ClassNotFoundException(name);
	}

	protected Class findClassOrNull(String name) throws ClassNotFoundException {

//System.out.println("<findClassOrNull> name: " + name + " >>>>>>>>>>>>>>");

		if(open) {
			String jarName = name.replace('.', '/') + ".class";
//System.out.println("<findClassOrNull> - jarName: " + jarName);
			if(exceptions != null) {
//System.out.println("<findClassOrNull> - exceptions NOT null: " + exceptions);
				for (int i = 0; i < exceptions.length; i++) {
//System.out.println("<findClassOrNull> - exception: " + exceptions[i]);
					if(jarName.startsWith(exceptions[i])) {
						return null;
					}
				}
			}
			for(int i = 0; i < files.length; i++) {
//System.out.println("<findClassOrNull> finding class - i: " + i);
				Class c = findClass(i, name, jarName);
				if(c != null) {
					return c;
				}
			}
			return null;
		} else {
			throw new ClassNotFoundException(name);
		}
	}

	private Class findClass(int i, String name, String jarName) throws ClassNotFoundException {
		JarFile jarFile = jarUrlStreamHandlers[i].getJarFile();
		JarEntry jarEntry = jarFile.getJarEntry(jarName);
		if(jarEntry == null) {
			return null;
		}
		DataInputStream classStream = null;
		byte[] classBytes = new byte[(int)jarEntry.getSize()];
		try {
			classStream = new DataInputStream(jarFile.getInputStream(jarEntry));
			classStream.readFully(classBytes);
		} catch (IOException e) {
			throw (ClassNotFoundException)new ClassNotFoundException(name).initCause(e);
		} finally {
			if(classStream != null) {
				try {
					classStream.close();
				} catch (IOException e) {
					throw (ClassNotFoundException)new ClassNotFoundException(name).initCause(e);
				}
			}
		}
		return defineClass(name, classBytes, 0, classBytes.length);
	}

	/* (non-Javadoc)
	 * @see java.lang.ClassLoader#findResource(java.lang.String)
	 */
	protected URL findResource(String name) {
		if(open) {
			if(exceptions != null) {
				for (int i = 0; i < exceptions.length; i++) {
					if(name.startsWith(exceptions[i])) {
						return null;
					}
				}
			}
			for(int i = 0; i < files.length; i++) {
				URL url = findResource(i, name);
				if(url != null) {
					return url;
				}
			}
			return null;
		} else {
			return null;
		}
	}

	private URL findResource(int i, String name) {
		JarEntry jarEntry = jarUrlStreamHandlers[i].getJarFile().getJarEntry(name);
		if(jarEntry == null) {
			return null;
		}
		try {
			URL url = new URL("jar", "", -1, fileUrlStrings[i] + "!/" + jarEntry.getName(),
					jarUrlStreamHandlers[i]);
			return url;
		} catch (MalformedURLException e) {
			throw (RuntimeException)new RuntimeException(e.getMessage()).initCause(e);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.ClassLoader#findResources(java.lang.String)
	 */
	protected Enumeration findResources(String name) throws IOException {
		if(open) {
			final URL resourceUrls[] = new URL[files.length];
			int j = 0;
			for(int i = 0; i < files.length; i++) {
				URL url = findResource(i, name);
				if(url != null) {
					resourceUrls[j++] = url;
				}
			}
			final int resourceUrlsCount = j;
			return new Enumeration() {
				private int idx = 0;
				
				public boolean hasMoreElements() {
					return idx < resourceUrlsCount;
				}
				
				public Object nextElement() {
					if(idx < resourceUrlsCount) {
						int current = idx;
						idx++;
						return resourceUrls[current];
					} else {
						throw new NoSuchElementException();
					}
				}
			};
		} else {
			return emptyEnumeration;
		}
	}


}
