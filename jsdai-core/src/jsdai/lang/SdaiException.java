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

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.io.Serializable;

/**
 * The SdaiException class describes JSDAI errors appearing as a result
 * of a JSDAI operation that has failed to execute successfully.
 * This class contains a list of possible error indicators and methods to
 * access the private fields. For the specific error represented by an
 * instance of this class, the following information can be obtained: error
 * indicator, underlying <code>SdaiException</code> (if any),
 * the instance related to or causing the error, called error base (if any),
 * and error description.
 * @see "ISO 10303-22::11 SDAI errors"
 */
public class SdaiException extends Exception implements Serializable {

/**
 * Error indicator with the description: Session open.
 * <p> An attempt was made to open a session while a session was still open.
 */
	public static final int SS_OPN = 10;

/**
 * Error indicator with the description: Session not available.
 * <p> Access to an SDAI object is possible only when the session is open.
 */
	public static final int SS_NAVL = 20;

/**
 * Error indicator with the description: Session is not open.
 * <p> Access to an SDAI object is possible only when the session is open.
 */
	public static final int SS_NOPN = 30;

/**
 * Error indicator with the description: Repository does not exist.
 */
	public static final int RP_NEXS = 40;

/**
 * Error indicator with the description: Repository not available.
 */
	public static final int RP_NAVL = 50;

/**
 * Error indicator with the description: Repository open.
 */
	public static final int RP_OPN = 60;

/**
 * Error indicator with the description: Repository is not open.
 */
	public static final int RP_NOPN = 70;

/**
 * Error indicator with the description: Repository duplicate.
 */
	public static final int RP_DUP  = 75;

/**
 * Error indicator with the description: Transaction ended abnormally.
 */
	public static final int TR_EAB = 80;

/**
 * Error indicator with the description: Transaction exists.
 */
	public static final int TR_EXS = 90;

/**
 * Error indicator with the description: Transaction currently not available.
 */
	public static final int TR_NAVL = 100;

/**
 * Error indicator with the description: Transaction read-write.
 */
	public static final int TR_RW = 110;

/**
 * Error indicator with the description: Transaction not read-write.
 */
	public static final int TR_NRW = 120;

/**
 * Error indicator with the description: Transaction does not exist.
 */
	public static final int TR_NEXS = 130;

/**
 * Error indicator with the description: SDAI-model not domain equivalent.
 */
	public static final int MO_NDEQ = 140;

/**
 * Error indicator with the description: SDAI-model does not exist.
 */
	public static final int MO_NEXS = 150;

/**
 * Error indicator with the description: SDAI-model invalid.
 */
	public static final int MO_NVLD = 160;

/**
 * Error indicator with the description: SDAI-model duplicate.
 */
	public static final int MO_DUP = 170;

/**
 * Error indicator with the description: SDAI-model access not read-write.
 */
	public static final int MX_NRW = 180;

/**
 * Error indicator with the description: SDAI-model access not defined.
 */
	public static final int MX_NDEF = 190;

/**
 * Error indicator with the description: SDAI-model access read-write.
 */
	public static final int MX_RW = 200;

/**
 * Error indicator with the description: SDAI-model access read-only.
 */
	public static final int MX_RO = 210;

/**
 * Error indicator with the description: Schema definition not defined.
 */
	public static final int SD_NDEF = 220;

/**
 * Error indicator with the description: Entity definition not defined.
 */
	public static final int ED_NDEF = 230;

/**
 * Error indicator with the description: Entity definition not domain equivalent.
 */
	public static final int ED_NDEQ = 240;

/**
 * Error indicator with the description: Entity definition invalid.
 */
	public static final int ED_NVLD = 250;

/**
 * Error indicator with the description: Rule not defined.
 */
	public static final int RU_NDEF = 260;

/**
 * Error indicator with the description: Expression evaluation not supported.
 */
	public static final int EX_NSUP = 270;

/**
 * Error indicator with the description: Attribute invalid.
 */
	public static final int AT_NVLD = 280;

/**
 * Error indicator with the description: Attribute not defined.
 */
	public static final int AT_NDEF = 290;

/**
 * Error indicator with the description: Schema instance duplicate.
 */
	public static final int SI_DUP = 300;

/**
 * Error indicator with the description: Schema instance does not exist.
 */
	public static final int SI_NEXS = 310;

/**
 * Error indicator with the description: Entity instance does not exist.
 */
	public static final int EI_NEXS = 320;

/**
 * Error indicator with the description: Entity instance not available.
 */
	public static final int EI_NAVL = 330;

/**
 * Error indicator with the description: Entity instance invalid.
 */
	public static final int EI_NVLD = 340;

/**
 * Error indicator with the description: Entity instance not exported.
 */
	public static final int EI_NEXP = 350;

/**
 * Error indicator with the description: Aggregate instance does not exist.
 */
	public static final int AI_NEXS = 380;

/**
 * Error indicator with the description: Aggregate instance invalid.
 */
	public static final int AI_NVLD = 390;

/**
 * Error indicator with the description: Aggregate instance is empty.
 */
	public static final int AI_NSET = 400;

/**
 * Error indicator with the description: Value invalid.
 */
	public static final int VA_NVLD = 410;

/**
 * Error indicator with the description: Value does not exist.
 */
	public static final int VA_NEXS = 420;

/**
 * Error indicator with the description: Value not set.
 */
	public static final int VA_NSET = 430;

/**
 * Error indicator with the description: Value type invalid.
 */
	public static final int VT_NVLD = 440;

/**
 * Error indicator with the description: Iterator does not exist.
 */
	public static final int IR_NEXS = 450;

/**
 * Error indicator with the description: Current member is not defined.
 */
	public static final int IR_NSET = 460;

/**
 * Error indicator with the description: Index invalid.
 */
	public static final int IX_NVLD = 470;

/**
 * Error indicator with the description: Event recording not set.
 * <p> This SDAI error indicator is not used by JSDAI.
 */
	public static final int ER_NSET = 480;

/**
 * Error indicator with the description: Operator invalid.
 */
	public static final int OP_NVLD = 490;

/**
 * Error indicator with the description: Function not available.
 */
	public static final int FN_NAVL = 500;

/**
 * Error indicator with the description: Location invalid.
 */
	public static final int LC_NVLD = 600;

/**
 * Error indicator with the description: Read-only access to repository.
 */
	public static final int RP_RO = 610;

/**
 * Error indicator with the description: Schema instance locked by another user.
 */
	public static final int SI_LOCK = 630;

/**
 * Error indicator with the description: Model locked by another user.
 */
	public static final int MO_LOCK = 640;

/**
 * Error indicator with the description: Repository locked by another user.
 */
	public static final int RP_LOCK = 650;
	
/**
 * Error indicator with the description: Read-only access to remote session.
 */
	public static final int SS_RO = 660;

/**
 * Error indicator with the description: Underlying system error.
 */
	public static final int SY_ERR = 1000;

/**
 * Error indicator with the description: Security violation.
 * @since 4.0.0
 */
	public static final int SY_SEC = 1001;

/**
 * Error indicator with the description: Could not obtain lock.
 * @since 4.1.0
 */
	public static final int SY_LOC = 1002;

/**
 * Creates a new instance of <code>SdaiException</code> with error indicator SY_ERR.
 * @see #SdaiException(int id)
 * @see #SdaiException(int id, Object base)
 * @see #SdaiException(int id, Object base, String text)
 * @see #SdaiException(int id, Exception exception)
 * @see #SdaiException(int id, Object base, Exception exception)
 */
	public SdaiException() {
		super((String) error_descriptions.get(new Integer(SY_ERR)));
	}

/**
 * Creates a new instance of <code>SdaiException</code> with specified error indicator.
 * @param id the error indicator.
 * @see #SdaiException()
 * @see #SdaiException(int id, Object base)
 * @see #SdaiException(int id, Object base, String text)
 * @see #SdaiException(int id, Exception exception)
 * @see #SdaiException(int id, Object base, Exception exception)
 */
	public SdaiException(int id) {
		super((String) error_descriptions.get(new Integer(id)));
		error_id = id;
	}

/**
 * Creates a new instance of <code>SdaiException</code> with specified error indicator
 * and specified instance related to or causing the error.
 * @param id the error indicator.
 * @param base an instance related to or causing the error.
 * @see #SdaiException()
 * @see #SdaiException(int id)
 * @see #SdaiException(int id, Object base, String text)
 * @see #SdaiException(int id, Exception exception)
 * @see #SdaiException(int id, Object base, Exception exception)
 */
	public SdaiException(int id, Object base) {
		super((String) error_descriptions.get(new Integer(id)) + ". " + base);
		error_id = id;
		error_base = base;
	}

/**
 * Creates a new instance of <code>SdaiException</code> with specified error indicator
 * and specified instance related to or causing the error. 
 * Also, some text extending the standard error description can be submitted.
 * @param id the error indicator.
 * @param base an instance related to or causing the error.
 * @param text some text to be added to the standard error description.
 * @see #SdaiException()
 * @see #SdaiException(int id)
 * @see #SdaiException(int id, Object base)
 * @see #SdaiException(int id, Exception exception)
 * @see #SdaiException(int id, Object base, Exception exception)
 */
	public SdaiException(int id, Object base, String text) {
		super((String) error_descriptions.get(new Integer(id)) + ". " + text);
		error_id = id;
		error_base = base;
	}

/**
 * Creates a new instance of <code>SdaiException</code> with specified error indicator
 * and specified underlying <code>Exception</code>.
 * @param id the error indicator.
 * @param exception underlying <code>Exception</code>.
 * @see #SdaiException()
 * @see #SdaiException(int id)
 * @see #SdaiException(int id, Object base)
 * @see #SdaiException(int id, Object base, String text)
 * @see #SdaiException(int id, Object base, Exception exception)
 */
	public SdaiException(int id, Exception exception) {
		super((String) error_descriptions.get(new Integer(id)) + ". UnderlyingException:\n" + exception);
		error_id = id;
		underlyingException = exception;
	}

/**
 * Creates a new instance of <code>SdaiException</code> with specified error indicator,
 * the instance related to or causing the error,
 * and underlying <code>Exception</code>.
 * @param id the error indicator.
 * @param base an instance related to or causing the error.
 * @param exception underlying <code>Exception</code>.
 * @see #SdaiException()
 * @see #SdaiException(int id)
 * @see #SdaiException(int id, Object base)
 * @see #SdaiException(int id, Object base, String text)
 * @see #SdaiException(int id, Exception exception)
 */
	public SdaiException(int id, Object base, Exception exception) {
		super((String) error_descriptions.get(new Integer(id)) + ". UnderlyingException:\n" + exception);
		error_id = id;
		error_base = base;
		underlyingException = exception;
	}

	protected SdaiException(int id, Object base, String text, boolean makeDescription) {
		super(makeDescription ? 
			  (String) error_descriptions.get(new Integer(id)) + ". " + text : text);
		error_id = id;
		error_base = base;
	}


/**
 * Returns <code>Exception</code> (if any) that is underlying to this <code>SdaiException</code>.
 * If this object of <code>SdaiException</code> is for error which type
 * does not assume the existence of such an <code>Exception</code>, then
 * <code>null</code> is returned.
 * @return either null or underlying <code>Exception</code>.
 */
	public final Exception getUnderlyingException() {
		return underlyingException;
	}

/**
 * Returns the error indicator for this object of <code>SdaiException</code>.
 * Most error indicators implemented in this class are taken from Table 2 of
 * ISO 10303-22::11 SDAI errors. Some additional indicators are also included.
 * @return an indicator for the error represented by this <code>SdaiException</code>
 * object.
 * @see "ISO 10303-22::11 SDAI errors"
 */
	public final int getErrorId() {
		return error_id;
	}


/**
 * Returns the instance related to or causing the error, if any.
 * If this object of <code>SdaiException</code> is for error which type
 * does not assume the existence of such an instance, then
 * <code>null</code> is returned.
 * @return either null or the instance related to or causing the error.
 * @see "ISO 10303-22::11 SDAI errors"
 */
	public final Object getErrorBase() {
		return error_base;
	}


/**
 * Returns the description of the error for the specified error indicator.
 * @param id an error indicator.
 * @return a <code>String</code> providing an information about the error.
 * @see "ISO 10303-22::11 SDAI errors"
 */
	public static String getErrorDescription(int id) {
		return (String) error_descriptions.get(new Integer(id));
	}

// private fields and methods
	private int error_id = 1000;
	private Object error_base = null;
	private Exception underlyingException = null;
	private static Hashtable error_descriptions = new Hashtable();

	private static void initError_descriptions() {
   error_descriptions.put(new Integer(SS_OPN), 
    "SS_OPN - Session open");
   error_descriptions.put(new Integer(SS_NAVL), 
    "SS_NAVL - Session not available");
   error_descriptions.put(new Integer(SS_NOPN), 
    "SS_NOPN - Session is not open");
   error_descriptions.put(new Integer(RP_NEXS), 
    "RP_NEXS - Repository does not exist");
   error_descriptions.put(new Integer(RP_NAVL), 
    "RP_NAVL - Repository not available");
   error_descriptions.put(new Integer(RP_OPN), 
    "RP_OPN - Repository open");
   error_descriptions.put(new Integer(RP_NOPN), 
    "RP_NOPN - Repository is not open");
	 error_descriptions.put(new Integer(RP_DUP), 
    "RP_DUP - Repository duplicate");
   error_descriptions.put(new Integer(TR_EAB), 
    "TR_EAB - Transaction ended abnormally");
   error_descriptions.put(new Integer(TR_EXS), 
    "TR_EXS - Transaction exists");
   error_descriptions.put(new Integer(TR_NAVL), 
    "TR_NAVL - Transaction currently not available");
   error_descriptions.put(new Integer(TR_RW), 
    "TR_RW - Transaction read-write");
   error_descriptions.put(new Integer(TR_NRW), 
    "TR_NRW - Transaction not read-write");
   error_descriptions.put(new Integer(TR_NEXS), 
    "TR_NEXS - Transaction does not exist");
   error_descriptions.put(new Integer(MO_NDEQ), 
    "MO_NDEQ - SDAI-model not domain equivalent");
   error_descriptions.put(new Integer(MO_NEXS), 
    "MO_NEXS - SDAI-model does not exist");
   error_descriptions.put(new Integer(MO_NVLD), 
    "MO_NVLD - SDAI-model invalid");
   error_descriptions.put(new Integer(MO_DUP), 
    "MO_DUP - SDAI-model duplicate");
   error_descriptions.put(new Integer(MX_NRW), 
    "MX_NRW - SDAI-model access not read-write");
   error_descriptions.put(new Integer(MX_NDEF), 
    "MX_NDEF - SDAI-model access not defined");
   error_descriptions.put(new Integer(MX_RW), 
    "MX_RW - SDAI-model access read-write");
   error_descriptions.put(new Integer(MX_RO), 
    "MX_RO - SDAI-model access read-only");
   error_descriptions.put(new Integer(SD_NDEF), 
    "SD_NDEF - Schema definition not defined");
   error_descriptions.put(new Integer(ED_NDEF), 
    "ED_NDEF - Entity definition not defined");
   error_descriptions.put(new Integer(ED_NDEQ), 
    "ED_NDEQ - Entity definition not domain equivalent");
   error_descriptions.put(new Integer(ED_NVLD), 
    "ED_NVLD - Entity definition invalid");
   error_descriptions.put(new Integer(RU_NDEF), 
    "RU_NDEF - Rule not defined");
   error_descriptions.put(new Integer(EX_NSUP), 
    "EX_NSUP - Expression evaluation not supported");
   error_descriptions.put(new Integer(AT_NVLD), 
    "AT_NVLD - Attribute invalid");
   error_descriptions.put(new Integer(AT_NDEF), 
    "AT_NDEF - Attribute not defined");
   error_descriptions.put(new Integer(SI_DUP), 
    "SI_DUP - Schema instance duplicate");
   error_descriptions.put(new Integer(SI_NEXS), 
    "SI_NEXS - Schema instance does not exist");
   error_descriptions.put(new Integer(EI_NEXS), 
    "EI_NEXS - Entity instance does not exist");
   error_descriptions.put(new Integer(EI_NAVL), 
    "EI_NAVL - Entity instance not available");
   error_descriptions.put(new Integer(EI_NVLD), 
    "EI_NVLD - Entity instance invalid");
   error_descriptions.put(new Integer(EI_NEXP), 
    "EI_NEXP - Entity instance not exported");
   error_descriptions.put(new Integer(AI_NEXS), 
    "AI_NEXS - Aggregate instance does not exist");
   error_descriptions.put(new Integer(AI_NVLD), 
    "AI_NVLD - Aggregate instance invalid");
   error_descriptions.put(new Integer(AI_NSET), 
    "AI_NSET - Aggregate instance is empty");
   error_descriptions.put(new Integer(VA_NVLD), 
    "VA_NVLD - Value invalid");
   error_descriptions.put(new Integer(VA_NEXS), 
    "VA_NEXS - Value does not exist");
   error_descriptions.put(new Integer(VA_NSET), 
    "VA_NSET - Value not set");
   error_descriptions.put(new Integer(VT_NVLD), 
    "VT_NVLD - Value type invalid");
   error_descriptions.put(new Integer(IR_NEXS), 
    "IR_NEXS - Iterator does not exist");
   error_descriptions.put(new Integer(IR_NSET), 
    "IR_NSET - Current member is not defined");
   error_descriptions.put(new Integer(IX_NVLD), 
    "IX_NVLD - Index invalid");
   error_descriptions.put(new Integer(ER_NSET), 
    "ER_NSET - Event recording not set");
   error_descriptions.put(new Integer(OP_NVLD), 
    "OP_NVLD - Operator invalid");
   error_descriptions.put(new Integer(FN_NAVL), 
    "FN_NAVL - Function not available");
   error_descriptions.put(new Integer(LC_NVLD), 
    "LC_NVLD - Location invalid");
   error_descriptions.put(new Integer(RP_RO), 
    "RP_RO - read-only access to repository");

   error_descriptions.put(new Integer(SI_LOCK ), 
    "SI_LOCK  - Schema instance locked by another user");
   error_descriptions.put(new Integer(MO_LOCK), 
    "MO_LOCK - Model locked by another user");
   error_descriptions.put(new Integer(RP_LOCK), 
    "RP_LOCK - Repository locked by another user");
   error_descriptions.put(new Integer(SS_RO), 
    "SS_RO - read-only access to remote session");

   error_descriptions.put(new Integer(SY_ERR), 
    "SY_ERR - Underlying system error");
   error_descriptions.put(new Integer(SY_SEC), 
    "SY_SEC - Security violation");
   error_descriptions.put(new Integer(SY_LOC), 
    "SY_LOC - Could not obtain lock");
  }

    /**
     * Prints the stack trace of the exception that occurred to the
     * specified print stream.
     */
    public void printStackTrace(PrintStream ps) {
        synchronized (ps) {
			super.printStackTrace(ps);
            if (underlyingException != null) {
                ps.print("Caused by: ");
                underlyingException.printStackTrace(ps);
            }
        }
    }

    /**
     * Prints the stack trace of the exception that occurred to the
     * specified print writer.
     */
    public void printStackTrace(PrintWriter pw) {
        synchronized (pw) {
			super.printStackTrace(pw);
            if (underlyingException != null) {
                pw.print("Caused by: ");
                underlyingException.printStackTrace(pw);
            }
        }
    }

  static {
    initError_descriptions();
  }
  
}
