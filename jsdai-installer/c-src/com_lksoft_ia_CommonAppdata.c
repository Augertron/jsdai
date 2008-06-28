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

#include "com_lksoft_ia_CommonAppdata.h"

#define WIN32_LEAN_AND_MEAN		// Exclude rarely-used stuff from Windows headers

#include <windows.h>
#include <shlobj.h>

BOOL APIENTRY DllMain( HANDLE hModule, 
                       DWORD  ul_reason_for_call, 
                       LPVOID lpReserved
					 ){
    return TRUE;
}

/*
 * Class:     com_lksoft_ia_CommonAppdata
 * Method:    getCommonAppdataDir
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_lksoft_ia_CommonAppdata_getCommonAppdataDir
  (JNIEnv *env, jclass c) {

	WCHAR path[MAX_PATH];
	if(SHGetFolderPathW(NULL, CSIDL_COMMON_APPDATA, NULL, 0, path) == S_OK) {
		return (*env)->NewString(env, (jchar *)path, wcslen(path));
	} else {
		return NULL;
	}
}
