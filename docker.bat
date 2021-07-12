CALL mvn package -f build-all -DskipTests

CALL docker-compose up --build
pause