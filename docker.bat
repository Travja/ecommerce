CALL mvn package -f parent/build-all -DskipTests

CALL docker-compose up --build
CALL docker-compose down
CALL docker image prune -f

pause