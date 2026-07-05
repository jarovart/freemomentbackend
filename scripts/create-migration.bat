@echo off
setlocal enabledelayedexpansion

if "%~1"=="" (
  echo Bitte Migration-Namen angeben.
  echo Beispiel: scripts\create-migration.bat add-user-bio
  exit /b 1
)

set MIGRATION_TITLE=%~1
set CHANGELOG_DIR=src\main\resources\db\changelog\changes
set MASTER_FILE=src\main\resources\db\changelog\db.changelog-master.yaml

set MAX=0

for %%f in (%CHANGELOG_DIR%\*.yaml) do (
  set FILE=%%~nxf
  set PREFIX=!FILE:~0,3!

  echo !PREFIX!| findstr /r "^[0-9][0-9][0-9]$" >nul
  if !errorlevel! == 0 (
    if !PREFIX! GTR !MAX! set MAX=!PREFIX!
  )
)

set /a NEXT_NUM=1%MAX% - 1000 + 1

if %NEXT_NUM% LSS 10 (
  set NEXT=00%NEXT_NUM%
) else if %NEXT_NUM% LSS 100 (
  set NEXT=0%NEXT_NUM%
) else (
  set NEXT=%NEXT_NUM%
)

set MIGRATION_NAME=%NEXT%-%MIGRATION_TITLE%
set MIGRATION_FILE=%CHANGELOG_DIR%\%MIGRATION_NAME%.yaml

echo Erzeuge Migration: %MIGRATION_FILE%

call mvn liquibase:diff -Dmigration.name=%MIGRATION_NAME%

if errorlevel 1 (
  echo Liquibase Diff fehlgeschlagen.
  exit /b 1
)

>>"%MASTER_FILE%" echo   - include:
>>"%MASTER_FILE%" echo       file: db/changelog/changes/%MIGRATION_NAME%.yaml

echo Fertig.
echo Migration erstellt: %MIGRATION_FILE%
echo In Master eingetragen: %MASTER_FILE%