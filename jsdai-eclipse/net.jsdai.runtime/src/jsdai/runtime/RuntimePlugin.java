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

package jsdai.runtime;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.Bundle;

/**
 * The main plugin class to be used in the desktop.
 */
public class RuntimePlugin extends Plugin {
	public static final String PLUGIN_ID = "net.jsdai.runtime";

	private static final String LIBRARY_VERSIONS_COMMENT = "library-versions"; //$NON-NLS-1$
	private static final String LIBRARY_VERSIONS_TAG = "#" + LIBRARY_VERSIONS_COMMENT; //$NON-NLS-1$

	// private static final String EMPTY_REPOSITORY_FILE_NAME =
	// "ExpressCompilerRepo.sdai";

	// The shared instance.
	private static RuntimePlugin plugin = null;

	/**
	 * The constructor.
	 */
	public RuntimePlugin() {
		super();
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static RuntimePlugin getDefault() {
		return plugin;
	}

	public static String getResourcePath(String resourceName) {
		RuntimePlugin plugin = getDefault();
		Bundle bundle = plugin.getBundle();
		URL url = bundle.getEntry(resourceName);
		try {
			url = FileLocator.toFileURL(url);
			return new File(new URI(url.toString())).getAbsolutePath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getFragmentResourcePath(Bundle bundle, String resourceName) {
		URL url = bundle.getEntry(resourceName);
		try {
			url = FileLocator.toFileURL(url);
			return new File(new URI(url.toString())).getAbsolutePath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e);
		}
	}

	public String getLibraryVersionsAsString() throws IOException {
		IExtension[] libExtensions =
			Platform.getExtensionRegistry().getExtensionPoint(
					RuntimePlugin.PLUGIN_ID, "libraries").getExtensions();
		Properties libVerProps = new Properties();
		for (int i = 0; i < libExtensions.length; i++) {
			IExtension libExtension = libExtensions[i];
			if(libExtension.isValid()) {
				String libId = libExtension.getContributor().getName();
				Bundle libBundle = Platform.getBundle(libId);
				String libVersion = (String) libBundle.getHeaders().get("Bundle-Version");
				libVerProps.setProperty(libId, libVersion);
			}
		}
		ByteArrayOutputStream outBytes = new ByteArrayOutputStream();
		libVerProps.store(outBytes, LIBRARY_VERSIONS_COMMENT);
		return outBytes.toString("iso-8859-1"); //$NON-NLS-1$
	}

	public static Properties libraryVersionsToProperties(String versionString) throws IOException {
		Properties libVerProps = new Properties();
		if(versionString.startsWith(LIBRARY_VERSIONS_TAG)) {
			libVerProps.load(new ByteArrayInputStream(
					versionString.getBytes("iso-8859-1"))); //$NON-NLS-1$
		}
		return libVerProps;
	}
}
