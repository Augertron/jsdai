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

import jsdai.dictionary.*;
 

/** The <code>Binary</code> class represents the EXPRESS data type BINARY.
 * Instances of <code>Binary</code> are constant: their values cannot be
 * changed after they are created.
 */
public final class Binary {
	byte [] value;
	int unused;

	private static final int CONCATENATION_OPERAND_LENGTH = 32;

	private static final int CONCATENATION_RESULT_LENGTH = 64;

	private static final int CONCATENATION_BIT_RESULT_LENGTH = 256;


	Binary() throws SdaiException {
	}
/**
 * Initializes a newly created <code>Binary</code> object so that it
 * represents the sequence of bits described by the <code>String</code> argument.
 * A correspondence between a value of binary data type and its
 * <code>String</code> representation is defined as follows.
 * For a sequence of p bits, the string consists of k+1 hexadecimal
 * digits, where k is the smallest integer larger or equal to p/4.
 * The first digit is the value of 4k-p. Other digits are obtained
 * by first left filling the binary with 4k-p zero bits and then
 * dividing the sequence into groups of four bits. Each of these groups
 * gives a separate digit in the string representation.
 * <p> If the <code>String</code> submitted cannot be mapped to a
 * bit sequence according to the above rules, then SdaiException VA_NVLD is thrown.
 * @param str a <code>String</code> representing the binary value.
 * @throws SdaiException VA_NVLD, value invalid.
 * @see #Binary(byte [] str, int size)
 */
	public Binary(String str) throws SdaiException {
//		synchronized (SdaiCommon.syncObject) {
		int byte_index = 0;
		int byte_number = 0;
		int count_of_bytes;
		boolean first_byte_used;
		int numb;
		int ln = str.length();
		if (ln % 2 > 0) {
			count_of_bytes = (ln - 1) / 2;
			unused = 0;
			first_byte_used = false;
		} else {
			count_of_bytes = ln / 2;
			unused = 4;
			first_byte_used = true;
		}
		value = new byte[count_of_bytes];
		char sym = str.charAt(0);
		numb = (int)sym - 48;
		if (numb < 0 || numb > 3) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		unused += numb;
		for (int i = 1; i < ln; i++) {
			sym = str.charAt(i);
			numb = (int)sym;
			if (numb <= 57) {
				numb -= 48;
			} else {
				numb -= 55;
			}
			if (numb < 0 || numb > 15) {
				throw new SdaiException(SdaiException.VA_NVLD);
			}
			if (first_byte_used) {
				byte_number += numb;
				value[byte_index] = (byte)(byte_number - 128);
				byte_index++;
				first_byte_used = false;
			} else {
				byte_number = 16 * numb;
				first_byte_used = true;
			}
		}
//		} // syncObject
	}


/**
 * Initializes a newly created <code>Binary</code> object so that it
 * represents the sequence of bits described by the argument of type 
 * <code>byte</code> array.
 * A correspondence between a value of binary data type and its
 * <code>byte</code> array representation is defined as follows.
 * For a sequence of p bits, the <code>byte</code> array consists
 * of k+1 hexadecimal digits, where k is the smallest integer larger
 * or equal to p/4. The first digit is the value of 4k-p. Other digits
 * are obtained by first left filling the binary with 4k-p zero bits
 * and then dividing the sequence into groups of four bits. Each of
 * these groups gives a separate digit stored as a member of byte array.
 * <p> If the <code>byte</code> array submitted cannot be mapped to a
 * bit sequence according to the above rules, then SdaiException
 * VA_NVLD is thrown.
 * @param str a <code>byte</code> array representing the binary value.
 * @param size the number of bytes representing the binary value.
 * @throws SdaiException VA_NVLD, value invalid.
 * @see #Binary(String str)
 */
	public Binary(byte [] str, int size) throws SdaiException {
//		synchronized (SdaiCommon.syncObject) {
		int byte_index = 0;
		int byte_number = 0;
		int count_of_bytes;
		boolean first_byte_used;
		int numb;
		if (size % 2 > 0) {
			count_of_bytes = (size - 1) / 2;
			unused = 0;
			first_byte_used = false;
		} else {
			count_of_bytes = size / 2;
			unused = 4;
			first_byte_used = true;
		}
		value = new byte[count_of_bytes];
		numb = (int)str[0] - 48;
		if (numb < 0 || numb > 3) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		unused += numb;
		for (int i = 1; i < size; i++) {
			numb = (int)str[i];
			if (numb <= 57) {
				numb -= 48;
			} else {
				numb -= 55;
			}
			if (numb < 0 || numb > 15) {
				throw new SdaiException(SdaiException.VA_NVLD);
			}
			if (first_byte_used) {
				byte_number += numb;
				value[byte_index] = (byte)(byte_number - 128);
				byte_index++;
				first_byte_used = false;
			} else {
				byte_number = 16 * numb;
				first_byte_used = true;
			}
		}
//		} // syncObject
	}


	public static Binary BitsToBinary(String str) throws SdaiException {
//		synchronized (SdaiCommon.syncObject) {
		int bit_count = str.length();
		int res_count = bit_count / 4 + 1;
		int rem = bit_count % 4;
		int fill_number = rem;
		if (rem > 0) {
			fill_number = 4 - fill_number;
		}
		if (rem != 0) {
			res_count++;
		}
		StaticFields staticFields = StaticFields.get();
		if (staticFields.result == null) {
			if (res_count > CONCATENATION_RESULT_LENGTH) {
				staticFields.result = new byte[res_count];
			} else {
				staticFields.result = new byte[CONCATENATION_RESULT_LENGTH];
			}
		} else if (res_count > staticFields.result.length) {
			int new_length = staticFields.result.length * 2;
			if (new_length < res_count) {
				new_length = res_count;
			}
			staticFields.result = new byte[new_length];
		}
		staticFields.result[0] = (byte)(fill_number + 48);
		int pos = 0;
		boolean first = true;
		for (int i = 1; i < res_count; i++) {
			int numb = 0;
			int added = 8;
			for (int j = 0; j < 4; j++) {
				if (!first || j >= fill_number) {
					if ((byte)str.charAt(pos) == 49) {
						numb += added;
					}
					pos++;
				}
				added /= 2;
			}
			if (numb <= 9) {
				staticFields.result[i] = (byte)(numb + 48);
			} else {
				staticFields.result[i] = (byte)(numb + 55);
			}
//			if (first && rem > 0) {
//				pos = rem;
//			} else {
//				pos += 4;
//			}
			first = false;
		}
		return new Binary(staticFields.result, res_count);
//		} // syncObject
	}



/**
 * Returns a <code>String</code> representing the value of this Binary.
 * For a sequence of p bits, this string consists of k+1 hexadecimal
 * digits, where k is the smallest integer larger or equal to p/4.
 * The first digit is the value of 4k-p. Other digits are obtained
 * by first left filling the binary with 4k-p zero bits and then
 * dividing the sequence into groups of four bits. Each of these groups
 * gives a separate digit in the string representation.
 * <P><B>Example:</B>
 * <P><TT><pre>    Binary bin = ...; // suppose that value is 0100101011
 *    System.out.println("Binary value: " + bin);</TT></pre>
 * &nbsp;&nbsp;&nbsp;&nbsp;The following string will be printed:
 * <pre>    Binary value: 212B</pre>
 * @return a string representation of the value of this Binary.
 * @see #toByteArray 
 * @see "ISO 10303-21::7.3.6 Binary"
 */
	public String toString() {
//		synchronized (SdaiCommon.syncObject) {
		StringBuffer str = new StringBuffer((value.length << 1) + (unused < 4 ? 1 : 0));
		int numb;
		int first_numb;
		if (unused > 3) {
			numb = unused - 4;
		} else {
			numb = unused;
		}
		str.append((char)(numb + 48));
		for (int i = 0; i < value.length; i++) {
			numb = value[i] + 128;
			if (unused > 3 && i == 0) {
				if (numb <= 9) {
					numb += 48;
				} else {
					numb += 55;
				}
				str.append((char)numb);
			} else {
				first_numb = numb / 16;
				numb -= first_numb * 16;
				if (first_numb <= 9) {
					first_numb += 48;
				} else {
					first_numb += 55;
				}
				str.append((char)first_numb);
				if (numb <= 9) {
					numb += 48;
				} else {
					numb += 55;
				}
				str.append((char)numb);
			}
		}
		return str.toString();
//		} // syncObject
	}


/**
 * Stores the value of this Binary into submitted <code>byte</code> array.
 * For a sequence of p bits, this array consists of k+1 hexadecimal
 * digits, where k is the smallest integer larger or equal to p/4.
 * The first digit is the value of 4k-p. Other digits are obtained
 * by first left filling the binary with 4k-p zero bits and then
 * dividing the sequence into groups of four bits. Each of these groups
 * gives a separate digit stored as a member of byte array.
 * <p> If the size of the array submitted as an argument to this method
 * is too small, then SdaiException VA_NVLD is thrown.
 * <P><B>Example:</B>
 * <P><TT><pre>    Binary bin = ...; // suppose that value is 0100101011
 *    byte [] bt_array = new byte [5];
 *    int count = bin.toByteArray(bt_array);
 *    for (int i = 0; i < count; i++) {
 *       System.out.println("  i = " + i + "  value = " + (char)bt_array[i]);
 *    }</TT></pre>
 * &nbsp;&nbsp;&nbsp;&nbsp;The following lines will be printed:
 * <pre>    i = 0  value = 2</pre>
 * <pre>    i = 1  value = 1</pre>
 * <pre>    i = 2  value = 2</pre>
 * <pre>    i = 3  value = B</pre>
 * @param byte_array a byte array submitted to store the value of this Binary.
 * @return the number of bytes used to store the value of this Binary.
 * @throws SdaiException VA_NVLD, value invalid.
 * @see #toString 
 * @see "ISO 10303-21::7.3.6 Binary"
 */
	public int toByteArray(byte [] byte_array) throws SdaiException {
//		synchronized (SdaiCommon.syncObject) {
		int numb;
		int first_numb;
		int index = 0;
		if (unused > 3) {
			numb = unused - 4;
		} else {
			numb = unused;
		}
		if (byte_array.length < 1) {
			throw new SdaiException(SdaiException.VA_NVLD);
		}
		byte_array[0] = (byte)(numb + 48);
		for (int i = 0; i < value.length; i++) {
			numb = value[i] + 128;
			if (unused > 3 && i == 0) {
				if (numb <= 9) {
					numb += 48;
				} else {
					numb += 55;
				}
				index++;
				if (byte_array.length < index + 1) {
					throw new SdaiException(SdaiException.VA_NVLD);
				}
				byte_array[index] = (byte)numb;
			} else {
				first_numb = numb / 16;
				numb -= first_numb * 16;
				if (first_numb <= 9) {
					first_numb += 48;
				} else {
					first_numb += 55;
				}
				index++;
				if (byte_array.length < index + 1) {
					throw new SdaiException(SdaiException.VA_NVLD);
				}
				byte_array[index] = (byte)first_numb;
				if (numb <= 9) {
					numb += 48;
				} else {
					numb += 55;
				}
				index++;
				if (byte_array.length < index + 1) {
					throw new SdaiException(SdaiException.VA_NVLD);
				}
				byte_array[index] = (byte)numb;
			}
		}
		return index + 1;
//		} // syncObject
	}


/** 
 * Returns the length of the value of this Binary.
 * The length is equal to the number of bits in the bit sequence
 * representing a value of the binary data type.
 * @return the length of the sequence of bits represented by this Binary object.
 */
	public int getSize() {
//		synchronized (SdaiCommon.syncObject) {
			return value.length * 8 - unused;
//		} // syncObject
	}


/**
 * Compares this <code>Binary</code> to the specified object of <code>Binary</code> type.
 * @param bin the object to compare this <code>Binary</code> against.
 * @return <code>true</code> if and only if the argument is not <code>null</code>
 * and represents the same sequence of bits as this <code>Binary</code> object;
 * <code>false</code> otherwise.
 */
	public boolean equals(Binary bin) {
//		synchronized (SdaiCommon.syncObject) {
		if (bin == null) {
			return false;
		}
		if (value.length != bin.value.length) {
			return false;
		}
		for (int i = 0; i < value.length; i++) {
			if (value[i] != bin.value[i]) {
				return false;
			}
		}
		return true;
//		} // syncObject
	}


	boolean checkBinaryValue(EBinary_type bin_type) throws SdaiException {
		if (bin_type.testWidth(null)) {
			EBound bound = bin_type.getWidth(null);
			int bit_bound = bound.getBound_value(null);
			int bit_count = getSize();
			if (bin_type.getFixed_width(null)) {
				if (bit_count == bit_bound) {
					return true;
				} else {
					return false;
				}
			} else {
				if (bit_count <= bit_bound) {
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}


	int subbinary(StaticFields staticFields, int index1, int index2) throws SdaiException {
		int bit_count = getSize();
		if (index1 < 1 || index1 > bit_count || index2 < index1 || index2 > bit_count) {
			return -1;
		}
		int res_bit_count = (index2 - index1 + 1);
		int res_count = res_bit_count / 4 + 1;
		int rem = res_bit_count % 4;
		int fill_number = rem;
		if (rem > 0) {
			fill_number = 4 - fill_number;
		}
		if (rem != 0) {
			res_count++;
		}
		if (staticFields.bit_array == null) {
			if (bit_count > CONCATENATION_BIT_RESULT_LENGTH) {
				staticFields.bit_array = new byte[bit_count];
			} else {
				staticFields.bit_array = new byte[CONCATENATION_BIT_RESULT_LENGTH];
			}
		} else if (bit_count > staticFields.bit_array.length) {
			int new_length = staticFields.bit_array.length * 2;
			if (new_length < bit_count) {
				new_length = bit_count;
			}
			staticFields.bit_array = new byte[new_length];
		}
		int count = this.getBits(staticFields, staticFields.bit_array, 0);
		index1--;
		for (int i = 0; i < res_bit_count; i++) {
			staticFields.bit_array[i] = staticFields.bit_array[index1 + i];
		}
		putBits(staticFields, res_count, fill_number, rem);
		return res_count;
	}


	int concatenation(StaticFields staticFields, Binary bin) throws SdaiException {
		int res_bit_count = getSize() + bin.getSize();
		int res_count = res_bit_count / 4 + 1;
		int rem = res_bit_count % 4;
		int fill_number = rem;
		if (rem > 0) {
			fill_number = 4 - fill_number;
		}
		if (rem != 0) {
			res_count++;
		}
		if (staticFields.bit_array == null) {
			if (res_bit_count > CONCATENATION_BIT_RESULT_LENGTH) {
				staticFields.bit_array = new byte[res_bit_count];
			} else {
				staticFields.bit_array = new byte[CONCATENATION_BIT_RESULT_LENGTH];
			}
		} else if (res_bit_count > staticFields.bit_array.length) {
			int new_length = staticFields.bit_array.length * 2;
			if (new_length < res_bit_count) {
				new_length = res_bit_count;
			}
			staticFields.bit_array = new byte[new_length];
		}
		int count = this.getBits(staticFields, staticFields.bit_array, 0);
		count = bin.getBits(staticFields, staticFields.bit_array, count);
		putBits(staticFields, res_count, fill_number, rem);
		return res_count;
	}


	private void putBits(StaticFields staticFields, int res_count, int fill_number, int rem) throws SdaiException {
		if (staticFields.result == null) {
			if (res_count > CONCATENATION_RESULT_LENGTH) {
				staticFields.result = new byte[res_count];
			} else {
				staticFields.result = new byte[CONCATENATION_RESULT_LENGTH];
			}
		} else if (res_count > staticFields.result.length) {
			int new_length = staticFields.result.length * 2;
			if (new_length < res_count) {
				new_length = res_count;
			}
			staticFields.result = new byte[new_length];
		}
		staticFields.result[0] = (byte)(fill_number + 48);
		int pos = 0;
		boolean first = true;
		for (int i = 1; i < res_count; i++) {
			int numb = 0;
			int added = 8;
			for (int j = 0; j < 4; j++) {
				if (!first || j >= fill_number) {
					if (staticFields.bit_array[pos] == 49) {
						numb += added;
					}
					pos++;
				}
				added /= 2;
			}
			if (numb <= 9) {
				staticFields.result[i] = (byte)(numb + 48);
			} else {
				staticFields.result[i] = (byte)(numb + 55);
			}
//			if (first && rem > 0) {
//				pos = rem;
//			} else {
//				pos += 4;
//			}
			first = false;
		}
	}


	private int getBits(StaticFields staticFields, byte [] bits, int count) throws SdaiException {
		int op_bit_count = getSize();
		int op_count = op_bit_count / 4 + 1;
		int rem = op_bit_count % 4;
		int fill_number = rem;
		if (rem > 0) {
			fill_number = 4 - fill_number;
		}
		if (rem != 0) {
			op_count++;
		}
		if (staticFields.operand == null) {
			if (op_count > CONCATENATION_OPERAND_LENGTH) {
				staticFields.operand = new byte[op_count];
			} else {
				staticFields.operand = new byte[CONCATENATION_OPERAND_LENGTH];
			}
		} else if (op_count > staticFields.operand.length) {
			int new_length = staticFields.operand.length * 2;
			if (new_length < op_count) {
				new_length = op_count;
			}
			staticFields.operand = new byte[new_length];
		} 
		int bytes_count = toByteArray(staticFields.operand);
		boolean first = true;
		for (int i = 1; i < op_count; i++) {
			int numb = staticFields.operand[i];
			if (numb >= 65) {
				numb -= 55;
			} else {
				numb -= 48;
			}
			int comp_numb = 8;
			int dif = 4;
			for (int j = 0; j < 4; j++) {
				if (!first || j >= fill_number) {
					if (numb >= comp_numb) {
						bits[count++] = 49;
						numb -= comp_numb;
					} else {
						bits[count++] = 48;
					}
				}
				comp_numb -= dif;
				dif /= 2;
			}
			first = false;
		}
		return count;
	}


	private byte [] prepareBitsArray(byte [] bits) throws SdaiException {
		int bit_count = getSize();
		if (bits == null) {
			if (bit_count > CONCATENATION_BIT_RESULT_LENGTH) {
				bits = new byte[bit_count];
			} else {
				bits = new byte[CONCATENATION_BIT_RESULT_LENGTH];
			}
		} else if (bit_count > bits.length) {
			int new_length = bits.length * 2;
			if (new_length < bit_count) {
				new_length = bit_count;
			}
			bits = new byte[new_length];
		}
		return bits;
	}


	int compare(Binary bin) throws SdaiException {
		StaticFields staticFields = StaticFields.get();
		staticFields.bit_array = prepareBitsArray(staticFields.bit_array);
		int count1 = getBits(staticFields, staticFields.bit_array, 0);
		staticFields.bit_array2 = bin.prepareBitsArray(staticFields.bit_array2);
		int count2 = bin.getBits(staticFields, staticFields.bit_array2, 0);
		for (int i = 0; i < Math.min(count1, count2); i++) {
			if (staticFields.bit_array[i] < staticFields.bit_array2[i]) {
				return -1;
			} else if (staticFields.bit_array[i] > staticFields.bit_array2[i]) {
				return 1;
			}
		}
		if (count1 < count2) {
			return -1;
		} else if (count1 > count2) {
			return 1;
		} else {
			return 0;
		}
	}


}


