include .env

build:
	mvn compile

unit-test:
	mvn test -P unit-test

integration-test:
	mvn test -P integration-test -D MP_NOTIFICATION_URL="${MP_NOTIFICATION_URL}" -D MP_ACCESS_TOKEN="${MP_ACCESS_TOKEN}"

system-test:
	mvn test -P bdd-test

production:
	mvn clean install -DskipTests -Pprd -q