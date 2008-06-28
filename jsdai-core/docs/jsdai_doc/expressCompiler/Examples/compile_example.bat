copy example_1.exp+example_2.exp examples.exp /B
java -Djava.ext.dirs=..\..\..\lib jsdai.expressCompiler.Main -is -express examples.exp -java -binaries -complex examples.ce -short_names  -p21 exmaples.p21 -compilation_sn 1000 -index_file
md jar
javac -d jar -classpath .;..\..\..\lib\jsdai_runtime.jar jsdai\SExample_1\*.java
javac -d jar -classpath .;..\..\..\lib\jsdai_runtime.jar jsdai\SExample_2\*.java
move jsdai\repository.properties jar\jsdai
cd jar
jar cvf example.jar jsdai\SExample_1\*.class jsdai\SExample_2\*.class jsdai\repository.properties
cd ..
