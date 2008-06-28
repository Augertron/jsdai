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

package jsdai.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author vaidas
 * @version $Revision$
 *
 */
public class ResettableFileInputStream extends InputStream {

	private final String name;
	private RandomAccessFile actualFile;
	private long markPos;

	/**
	 * @param in
	 * @throws FileNotFoundException 
	 */
	public ResettableFileInputStream(File file) throws FileNotFoundException {
		this.name = file.getPath();
		actualFile = null;
		markPos = -1;
		if(!file.canRead()) {
			throw new FileNotFoundException(file.toString());
		}
	}

	/**
	 * @param name
	 * @throws FileNotFoundException
	 */
	public ResettableFileInputStream(String name) throws FileNotFoundException {
		this.name = name;
		actualFile = null;
		markPos = 0;
		if(!new File(name).canRead()) {
			throw new FileNotFoundException(name);
		}
		
	}

	/* (non-Javadoc)
	 * @see java.io.FilterInputStream#close()
	 */
	public void close() throws IOException {
		if(actualFile != null) {
			actualFile.close();
		}
	}

	/* (non-Javadoc)
	 * @see java.io.FilterInputStream#mark(int)
	 */
	public synchronized void mark(int readlimit) {
		try {
			markPos = getActualFile().getFilePointer();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see java.io.FilterInputStream#markSupported()
	 */
	public boolean markSupported() {
		return true;
	}

	/* (non-Javadoc)
	 * @see java.io.FilterInputStream#read()
	 */
	public int read() throws IOException {
		return getActualFile().read();
	}

	/* (non-Javadoc)
	 * @see java.io.FilterInputStream#read(byte[], int, int)
	 */
	public int read(byte[] b, int off, int len) throws IOException {
		return getActualFile().read(b, off, len);
	}

	/* (non-Javadoc)
	 * @see java.io.FilterInputStream#read(byte[])
	 */
	public int read(byte[] b) throws IOException {
		return getActualFile().read(b);
	}

	/* (non-Javadoc)
	 * @see java.io.FilterInputStream#reset()
	 */
	public synchronized void reset() throws IOException {
		getActualFile().seek(markPos);
	}

	/* (non-Javadoc)
	 * @see java.io.FilterInputStream#skip(long)
	 */
	public long skip(long n) throws IOException {
		if (n <= 0) {
		    return 0;
		}
		RandomAccessFile file = getActualFile();
		long pointer = file.getFilePointer();
		long length = file.length();
		if(pointer + n > length) {
			n = length - pointer;
		}
		file.seek(pointer + n);
		return n;
	}

	private RandomAccessFile getActualFile() throws IOException {
		if(actualFile == null) {
			actualFile = new RandomAccessFile(name, "r");
		}
		return actualFile;
	}
}
