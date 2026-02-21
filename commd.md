compile: javac -d out src/ludo/*.java
run: java -cp out ludo.LudoGame

jar make: jar cfe LudoGame.jar ludo.LudoGame -C out .
jar run : java -jar LudoGame.jar