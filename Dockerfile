FROM       openjdk:11
EXPOSE     80
EXPOSE     443
EXPOSE     8080
ADD        /target/apiEscuelasEscalada.jar apiEscuelasEscalada.jar
ENTRYPOINT [ "java", "-jar", "apiEscuelasEscalada.jar" ]