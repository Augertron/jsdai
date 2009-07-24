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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jsdai.common.CommonPlugin;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;

/**
 * @author vaidas
 * @version $Revision$
 *
 */
public class IsolatedRunnableThread extends Thread {

	/**
	 * @param className
	 * @param methodName
	 * @param parameterTypes
	 * @param parameters
	 * @param classPathJars a list of jar files that make up class path for the classloader.
	 *        This list is passed to <code>{@link JarFileClassLoader#JarFileClassLoader}</code>.
	 * @param exceptions an array of exception resource strings passed to
	 *        <code>{@link JarFileClassLoader#JarFileClassLoader}</code>. The resources
	 *        whose names start with any of exception string in this array do not get loaded
	 *        by the loader and are delegated to the parent loader.</br>
	 *        <b>IMPORTANT</b>: Special care has to be taken of exceptions, the classes there
	 *        have to have no dependencies to other resources loadable by this loader
	 *        but the ones that are also included in the set.
	 * @return
	 * @throws CoreException
	 * @see JarFileClassLoader
	 */
	public static IsolatedRunnableThread newInstance(final String className, final String methodName,
			final Class[] parameterTypes, final Object[] parameters, final File[] classPathJars, String[] exceptions)
	throws CoreException {
		try {
			JarFileClassLoader classLoader =
				new JarFileClassLoader(classPathJars, IsolatedRunnableThread.class.getClassLoader(), exceptions);
			Class runnableCreatorClass = Class.forName(className, true, classLoader);
//			System.out.println("trying to get method - methodName: " + methodName + ", parameterTypes: " + parameterTypes);
			for (int xixigigi = 0; xixigigi < parameterTypes.length; xixigigi++) {
//				System.out.println("index: " + xixigigi + ", class: " + parameterTypes[xixigigi]);
			}
//			System.out.println("from class: " + runnableCreatorClass);


			Method runnableCreatorMethod = runnableCreatorClass.getMethod(methodName, parameterTypes);
//			System.out.println("selected method: " + runnableCreatorMethod);

			for (int xixigigi3 = 0; xixigigi3 < parameters.length; xixigigi3++) {
//				System.out.println("index: " + xixigigi3 + ", parameter: " + parameters[xixigigi3]);
			}

			Runnable runnable = (Runnable)runnableCreatorMethod.invoke(null, parameters);
			return new IsolatedRunnableThread(runnable, classLoader);
		} catch (IOException e) {
			throw new CoreException(
						new Status(Status.ERROR, CommonPlugin.PLUGIN_ID, Status.OK,
								e.getMessage(), e));
		} catch (ClassNotFoundException e) {
			throw new CoreException(
					new Status(Status.ERROR, CommonPlugin.PLUGIN_ID, Status.OK,
							e.getMessage(), e));
		} catch (SecurityException e) {
			throw new CoreException(
					new Status(Status.ERROR, CommonPlugin.PLUGIN_ID, Status.OK,
							e.getMessage(), e));
		} catch (NoSuchMethodException e) {
			throw new CoreException(
					new Status(Status.ERROR, CommonPlugin.PLUGIN_ID, Status.OK,
							e.getMessage(), e));
		} catch (IllegalArgumentException e) {
			throw new CoreException(
					new Status(Status.ERROR, CommonPlugin.PLUGIN_ID, Status.OK,
							e.getMessage(), e));
		} catch (IllegalAccessException e) {
			throw new CoreException(
					new Status(Status.ERROR, CommonPlugin.PLUGIN_ID, Status.OK,
							e.getMessage(), e));
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			CommonPlugin.log(realException);
			throw new CoreException(
					new Status(Status.ERROR, CommonPlugin.PLUGIN_ID, Status.OK,
							realException.getMessage(), realException));
		}
	}

	private JarFileClassLoader classLoader;

	public void close() throws IOException {
		classLoader.close();
	}

	private IsolatedRunnableThread(final Runnable runnable, final JarFileClassLoader classLoader) {
		super(runnable, "IsolatedRunnableThread");
		this.classLoader = classLoader;
		setContextClassLoader(classLoader);
	}

}
