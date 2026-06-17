@echo off
setlocal

set APP_HOME=%~dp0
set WRAPPER_JAR=%APP_HOME%gradle\wrapper\gradle-wrapper.jar

if not exist "%WRAPPER_JAR%" (
  echo Gradle wrapper jar is missing: %WRAPPER_JAR% 1>&2
  echo Generate it with: gradle wrapper --gradle-version 8.10.2 1>&2
  exit /b 1
)

java -classpath "%WRAPPER_JAR%" org.gradle.wrapper.GradleWrapperMain %*
