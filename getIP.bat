@echo off

REM Check if Java 8 or above is installed
java -version 2>&1 | findstr /I "version \"1[.]8"
if errorlevel 1 (
  echo Error: Java 8 or above is required.
  exit /b 1
)

REM Check if the required arguments are provided
if "%*"=="" (
  echo Error: At least one argument is required.
  exit /b 1
)

REM Run the Java JAR file with all arguments
java -jar your_jar_file.jar %*