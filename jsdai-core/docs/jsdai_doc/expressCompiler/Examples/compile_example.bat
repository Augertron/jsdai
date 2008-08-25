copy example_1.exp+example_2.exp examples.exp /B
set LIB_DIR=..\..\..\lib
java -cp %LIB_DIR%\commons-collections.jar;%LIB_DIR%\commons-lang-1.0.1.jar;%LIB_DIR%\jsdai_express.jar;%LIB_DIR%\jsdai_runtime.jar;%LIB_DIR%\SExtended_dictionary_schema.zip -Djsdai.properties=%LIB_DIR% jsdai.expressCompiler.Main -is -express examples.exp -java -binaries -complex examples.ce -short_names  -p21 examples.p21 -compilation_sn 1000 -index_file
md jar
javac -d jar -classpath .;%LIB_DIR%\jsdai_runtime.jar jsdai\SExample_1\*.java
javac -d jar -classpath .;%LIB_DIR%\jsdai_runtime.jar jsdai\SExample_2\*.java
move jsdai\repository.properties jar\jsdai
cd jar
jar cvf example.jar jsdai\SExample_1\*.class jsdai\SExample_2\*.class jsdai\repository.properties
cd ..
