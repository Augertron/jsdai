/*
 * $Id$
 *
 * JSDAI(TM), a way to implement STEP, ISO 10303
 * Copyright (C) 1997-2015, LKSoftWare GmbH, Germany
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

/**
 * The enumeration of the supported by JSDAI implementation levels/conformance classes for clear text encoding.
 * <p>
 *
 * @author Rolandas Randis &lt;rolandas@lksoft.lt&gt;
 * @see "ISO 10303-21::8.2.2 file_description"
 * @since 4.4.1
 * <p>
 */
public enum FileImplementationLevel {

  /**
   * If used by the export clear text encoding the implementation level will be determined automatically by the count of
   * models(schema) instances.
   */
  AUTO(0, 0) {
    /**
     * Gets the automatically determined the implementation level.
     * <p>
     * @param dataCount The count of data sections in the STEP file.
     * <p>
     * @return The {@link  #LEVEL_31} level if the data sections (models) count is more than one, otherwise
     * {@link #LEVEL_21}
     */
    @Override
    public FileImplementationLevel getImplementationLevel(int dataCount) {
      return dataCount <= 1 ? LEVEL_21 : LEVEL_31;
    }
  },

  /**
   * Implementation level 2, conformance class 1.
   * <p>
   * If the following additional restrictions on the encoding are met, the value "2;1" or the value "2;2" may be used to
   * indicate conformance to this version of this part of ISO 10303:
   * <ul>
   * <li>the exchange structure shall contain a single data section, and the "DATA" keyword shall not be followed by a
   * parenthesized PARAMETER_LIST; the exchange structure header section shall not contain FILE_POPULATION
   * entities;</li>
   * <li>the exchange structure header section shall not contain SECTION_LANGUAGE entities; </li>
   * <li>the exchange structure header section shall not contain SECTION_CONTEXT entities; </li>
   * <li>the enumerated values of an EXPRESS ENUMERATION shall not be encoded using short names.</li>
   * </ul>
   * If used, the value "2;1" shall designate exchange structures adhering to conformance class 1
   */
  LEVEL_21(2, 1),

  /**
   * Implementation level 3, conformance class 1.
   * <p>
   * If the following restrictions on the encoding are met, the value "3;1"   * or the value "3;2" may be used to indicate   * conformance to this version of this part of ISO 10303:
   * <ul>
   * <li>the exchange structure shall contain a header section;</li>
   * <li>the exchange structure shall not contain an anchor section;</li>
   * <li>the exchange structure shall not contain a reference section;</li>
   * <li>the exchange structure header section shall not contain a SCHEMA_POPULATION entity;</li>
   * <li>the exchange structure shall contain one or more data sections;</li>
   * <li>the string tokens within the exchange structure shall only encode the characters at U+0080 to U+10FFFF with the
   * "\X2\" and "\X4\" control directives defined in 6.4.3.3.</li>
   * </ul>
   * If used, the value "3;1" shall designate exchange structures adhering to conformance class 1
   */
  LEVEL_31(3, 1);

  /**
   * The implementation level.
   */
  private final int implementationLevel; //NOPMD

  /**
   * The conformance class of the exported file.
   */
  private final int conformanceClass; //NOPMD

  private FileImplementationLevel(int implementationLevel, int conformanceClass) { //NOPMD
    this.implementationLevel = implementationLevel;
    this.conformanceClass = conformanceClass;
  }

  /**
   * Gets the implementation level text to be added to exported file header.
   * <p>
   *
   * @return the implementation level text to be added to exported file header.
   */
  public String getHeaderText() {
    return String.format("%d;%d", implementationLevel, conformanceClass);
  }

  /**
   * Gets the implementation level for the exported clear text encoded file.
   * <p>
   *
   * @return the implementation level for the exported clear text encoded file.
   */
  public int getImplementationLevel() {
    return implementationLevel;
  }

  /**
   * Gets the value for exchange structures adhering to conformance class.
   * <p>
   *
   * @return the value for exchange structures adhering to conformance class.
   */
  public int getConformanceClass() {
    return conformanceClass;
  }

  /**
   * Gets the automatically determined the implementation level.
   * <p>
   *
   * @param dataCount The count of data sections in the STEP file.
   *                  <p>
   * @return By default it returns the implementation level set. However in the case of {@link #AUTO} this will be
   * determined automatically based on the count of the data sections in the clear text file.
   */
  public FileImplementationLevel getImplementationLevel(int dataCount) {
    return this;
  }
}
