# documentupload

# Installing oracle jdbc library in your local repository
mvn install:install-file -Dfile={Path/to/your/ojdbc.jar generally {ORACLE_HOME}\jdbc\lib\ojdbc6.jar} -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0 -Dpackaging=jar

Example:
mvn -e install:install-file -Dfile=D:/oraclexe/app/oracle/product/11.2.0/server/jdbc/lib/ojdbc6.jar -DgroupId=com.oracle -DartifactId=ojdbc6 -Dversion=11.2.0 -Dpackaging=jar


