/**
 * $Id$
 * Created: 2005.12.16 15.51.44
 *
 * Copyright (c) LKSoftWare GmbH, 2005. All Rights Reserved.
 */
package jsdai.express_doc.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jsdai.express_doc.ExpressDocPlugin;

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
			Method runnableCreatorMethod = runnableCreatorClass.getMethod(methodName, parameterTypes);
			Runnable runnable = (Runnable)runnableCreatorMethod.invoke(null, parameters);
			return new IsolatedRunnableThread(runnable, classLoader);
		} catch (IOException e) {
			throw new CoreException(
						new Status(Status.ERROR, ExpressDocPlugin.ID_EXPRESS_DOC, Status.OK,
								e.getMessage(), e));
		} catch (ClassNotFoundException e) {
			throw new CoreException(
					new Status(Status.ERROR, ExpressDocPlugin.ID_EXPRESS_DOC, Status.OK,
							e.getMessage(), e));
		} catch (SecurityException e) {
			throw new CoreException(
					new Status(Status.ERROR, ExpressDocPlugin.ID_EXPRESS_DOC, Status.OK,
							e.getMessage(), e));
		} catch (NoSuchMethodException e) {
			throw new CoreException(
					new Status(Status.ERROR, ExpressDocPlugin.ID_EXPRESS_DOC, Status.OK,
							e.getMessage(), e));
		} catch (IllegalArgumentException e) {
			throw new CoreException(
					new Status(Status.ERROR, ExpressDocPlugin.ID_EXPRESS_DOC, Status.OK,
							e.getMessage(), e));
		} catch (IllegalAccessException e) {
			throw new CoreException(
					new Status(Status.ERROR, ExpressDocPlugin.ID_EXPRESS_DOC, Status.OK,
							e.getMessage(), e));
		} catch (InvocationTargetException e) {
			Throwable realException = e.getTargetException();
			ExpressDocPlugin.log(realException);
			throw new CoreException(
					new Status(Status.ERROR, ExpressDocPlugin.ID_EXPRESS_DOC, Status.OK,
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
