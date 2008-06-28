This simple example illustrates how to use the express compiler,
including the following points: 
- how to compile short form schemas
- how to create complex entities
- how to add short names

The batch file included in this directory does all that.  

First, you see that a merged file examples.exp is built which contains both
express schemas from example_1.exp and example_2.exp files.
It is needed because these two schemas are in short form relationship:
example_2 schema has "USE FROM example_2;" interface specification.

Next, notice -D"jsdai.properties=." switch on the command line.
It tells to use the jsdai.properties file from the current directory.
And in the jsdai.properties file, the location of the ExpressCompilerRepo
repository, which is necessary for the express compiler to run, is specified. 

Now, look at the command line switches of the express compiler itself.
The order of the switches in not significant.

-is switch  switches off a strict implementation of a feature of ISO 10303-11
that does not have any effect with these simple schemas but that could really 
cause  problems for JSDAI with some short form schemas 
with interfaced SELECT data types.
One might say that -is switch just implements a more sensible and improved
ISO 10303-11. 

-express examples.exp  switch tell the express compiler to compile the express 
schemas inside examples.exp file.

-relax switch is a switch that hopefully again does nothing in this case.
If there are some non-syntax errors or warning during parsing, without this
switch the next passes and backend would not start.
So it is safer not to use this switch, unless some insignificant error message
cannot be avoided, then this switch allows to complete the job and you 
perhaps still may get correct output data.

-java switch tells to generate java packages for these two schemas.
So two subdirectories containing the java packages will appear in the current
directory:
jsdai\SExample_1\
and 
jsdai\SExample_2\

-binaries switch makes the binary files with the sdai models with the 
dictionary data of the two schemas to be added to their java packages.
So the files
EXAMPLE_1_DICTIONARY_DATA and EXAMPLE_2_DICTIONARY_DATA will appear in the
corresponding subdirectories.

-complex examples.ce  switch tells the express compiler to create complex 
entities that are defined in the examples.ce file.
In this case, one complex entity will be created, i.e, the java entity class
for it will be generate in SExample_2 package 
and also the instance of its entity definition will be  created in the
dictionary model of that schema.

-short_names switch tells the express compiler to add to the dictionary 
optional short names for the entities. The short names are read from 
example_1.SN and example_2.SN files in SHORT_NAMES subdirectory of the
current directory, as a different directory for short names is not specified
with -short_name_dir switch.           

-p21 examples.p21 switch tell the express compiler to export the repository
to ISO 10303-21  format file with the name examples.p21.
You can use this file to browse the dictionary if you do not feel like using
SdaiEdit which you should use for that purpose, really.                     


