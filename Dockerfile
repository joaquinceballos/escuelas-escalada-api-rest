FROM       java:8
EXPOSE     8080
ADD        /target/apiEscuelasEscalada.jar apiEscuelasEscalada.jar
ENTRYPOINT [ "java", "-jar", "-Dspring.datasource.url=${DB_URL}", "-Dspring.jpa.hibernate.ddl-auto=${DB_DDL_AUTO}", "apiEscuelasEscalada.jar" ]