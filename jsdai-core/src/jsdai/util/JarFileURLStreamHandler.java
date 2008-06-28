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

package jsdai.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Date;
import java.util.jar.JarFile;

/**
 * @author vaidas
 * @version $Revision$
 *
 */
public class JarFileURLStreamHandler extends URLStreamHandler {
	private JarFile jarFile;

	/**
	 * 
	 */
	public JarFileURLStreamHandler(JarFile jarFile) {
		this.jarFile = jarFile;
	}

	public JarFile getJarFile() {
		return jarFile;
	}

	/* (non-Javadoc)
	 * @see java.net.URLStreamHandler#openConnection(java.net.URL)
	 */
	protected URLConnection openConnection(URL url) throws IOException {
		return new JarFileURLConnection(this, url);
	}

	static private class JarFileURLConnection extends JarURLConnection {

		private JarFileURLStreamHandler handler;

		/**
		 * @param url
		 * @throws MalformedURLException
		 */
		protected JarFileURLConnection(JarFileURLStreamHandler handler, URL url)
				throws MalformedURLException {
			super(url);
			this.handler = handler;
		}

		/* (non-Javadoc)
		 * @see java.net.JarURLConnection#getJarFile()
		 */
		public JarFile getJarFile() throws IOException {
			return handler.jarFile;
		}

		public InputStream getInputStream() throws IOException {
			return handler.jarFile.getInputStream(getJarEntry());
		}

		/* (non-Javadoc)
		 * @see java.net.URLConnection#connect()
		 */
		public void connect() throws IOException {
			connected = true;
		}

		public int getContentLength() {
			try {
				return (int) getJarEntry().getSize();
			} catch (IOException e) {
				throw (RuntimeException) new RuntimeException(e.getMessage())
						.initCause(e);
			}
		}

		public String getContentType() {
			try {
				String contentType = guessContentTypeFromName(getEntryName());
				if (contentType == null) {
					InputStream is = new BufferedInputStream(getInputStream());
					try {
						contentType = guessContentTypeFromStream(is);
					} finally {
						is.close();
					}
					if (contentType == null) {
						contentType = "content/unknown";
					}
				}
				return contentType;
			} catch (IOException e) {
				throw (RuntimeException) new RuntimeException(e.getMessage())
						.initCause(e);
			}
		}

		public long getLastModified() {
			try {
				return getJarEntry().getTime();
			} catch (IOException e) {
				throw (RuntimeException) new RuntimeException(e.getMessage())
						.initCause(e);
			}
		}

		public String getHeaderField(String name) {
			String lowerName = name.toLowerCase();
			if (lowerName.equals("content-length")) {
				return Integer.toString(getContentLength());
			} else if (lowerName.equals("content-type")) {
				return getContentType();
			} else if (lowerName.equals("last-modified")) {
				return new Date(getLastModified()).toString();
			} else {
				return null;
			}
		}
	}

}
