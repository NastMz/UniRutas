@echo off
cd ..

set "projects=flexcore unirutas-models unirutas-repository unirutas-commands unirutas-services unirutas-controllers unirutas-auth unirutas"

for %%i in (%projects%) do (
  if exist "%%i\pom.xml" (
    echo Construyendo proyecto: %%i
    cd "%%i"
    mvn clean install
    cd ..

    echo Moviendo jar generado al directorio lib
    if not exist "lib" mkdir "lib"
    copy /y "%%i\target\*.jar" "lib\"
  )
)

echo Construcción completa.
exit /b 0