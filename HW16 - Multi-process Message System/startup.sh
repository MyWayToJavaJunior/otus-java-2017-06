mvn package -f ../HW10\ -\ Hibernate/pom.xml
mvn install:install-file -Dfile=../HW10\ -\ Hibernate/target/HW10.jar -DgroupId=otus -DartifactId=hw10 -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true
mvn package -f ../HW11\ -\ Cache/pom.xml
mvn install:install-file -Dfile=../HW11\ -\ Cache/target/HW11.jar -DgroupId=otus -DartifactId=hw11 -Dversion=1.0 -Dpackaging=jar -DgeneratePom=true