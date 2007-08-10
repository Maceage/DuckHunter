@echo off
echo Starting JVM...
java -version
java -Xms64m -Xmx256m -jar ./release/DuckHunter.jar -fps
cd ..
