# $Id$
#
# JSDAI(TM), a way to implement STEP, ISO 10303
# Copyright (C) 1997-2008, LKSoftWare GmbH, Germany
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License
# version 3 as published by the Free Software Foundation (AGPL v3).
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
# See the GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#
# JSDAI is a registered trademark of LKSoftWare GmbH, Germany
# This software is also available under commercial licenses.
# See also http://www.jsdai.net/

ifndef CFG
CFG=com_lksoft_ia_CommonAppdata-win32-release
endif
GCCPREFIX=
CC=$(GCCPREFIX)gcc
CFLAGS=-I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/win32
CXX=$(GCCPREFIX)g++
CXXFLAGS=$(CFLAGS)
RC=$(GCCPREFIX)windres -O COFF
ifeq "$(CFG)"  "com_lksoft_ia_CommonAppdata-win32-release"
CFLAGS+=-Wall -O2 -DWIN32 -D_JNI_IMPLEMENTATION_ -DNDEBUG -D_WINDOWS -D_MBCS -D_USRDLL -DMACRETRIEVEBRIDGE_EXPORTS
LD=$(GCCPREFIX)gcc
LDFLAGS=-Wl,--kill-at -shared
TARGET=../src/com_lksoft_ia_CommonAppdata.dll
LDFLAGS+=
LIBS+=-lkernel32 -luser32 -lgdi32 -lwinspool -lcomdlg32 -ladvapi32 -lshell32 -lole32 -loleaut32 -luuid -lodbc32 -lodbccp32 -liphlpapi
else
ifeq "$(CFG)"  "com_lksoft_ia_CommonAppdata-win32-debug"
CFLAGS+=-Wall -g -O0 -DWIN32 -D_JNI_IMPLEMENTATION_ -D_DEBUG -D_WINDOWS -D_MBCS -D_USRDLL -DMACRETRIEVEBRIDGE_EXPORTS
LD=$(CC)
LDFLAGS=-Wl,--kill-at -shared
TARGET=com_lksoft_ia_CommonAppdata.dll
LDFLAGS+=
LIBS+=-lkernel32 -luser32 -lgdi32 -lwinspool -lcomdlg32 -ladvapi32 -lshell32 -lole32 -loleaut32 -luuid -lodbc32 -lodbccp32 -liphlpapi
endif
endif

ifndef TARGET
TARGET=com_lksoft_ia_CommonAppdata.dll
endif

.PHONY: all
all: $(TARGET)

%.o: %.c
	$(CC) $(CFLAGS) $(CPPFLAGS) -o $@ -c $<

%.o: %.cc
	$(CXX) $(CXXFLAGS) $(CPPFLAGS) -o $@ -c $<

%.o: %.cpp
	$(CXX) $(CXXFLAGS) $(CPPFLAGS) -o $@ -c $<

%.o: %.cxx
	$(CXX) $(CXXFLAGS) $(CPPFLAGS) -o $@ -c $<

%.res: %.rc
	$(RC) $(CPPFLAGS) -o $@ -i $<

CFLAGS+=
SOURCE_FILES= \
	com_lksoft_ia_CommonAppdata.c

HEADER_FILES= \
	com_lksoft_ia_CommonAppdata.h

RESOURCE_FILES=

SRCS=$(SOURCE_FILES) $(HEADER_FILES) $(RESOURCE_FILES)

OBJS=$(patsubst %.rc,%.res,$(patsubst %.cxx,%.o,$(patsubst %.cpp,%.o,$(patsubst %.cc,%.o,$(patsubst %.c,%.o,$(filter %.c %.cc %.cpp %.cxx %.rc,$(SRCS)))))))

$(TARGET): $(OBJS)
	$(LD) $(LDFLAGS) -o $@ $(OBJS) $(LIBS)

.PHONY: clean
clean:
	-rm -f $(OBJS) $(TARGET) com_lksoft_ia_CommonAppdata

.PHONY: depends
depends:
	-$(CXX) $(CXXFLAGS) $(CPPFLAGS) -MM $(filter %.c %.cc %.cpp %.cxx,$(SRCS)) > com_lksoft_ia_CommonAppdata.dep

-include com_lksoft_ia_CommonAppdata.dep
