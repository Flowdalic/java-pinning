GRADLE ?= ./gradlew

.PHONY: all
all: check codecov eclipse javadocAll show-dependency-updates

.PHONY: codecov
codecov:
	$(GRADLE) java-pinning-java11:testCodeCoverageReport
	echo "Code coverage report available at file://$(PWD)/java-pinnging-java11/build/reports/jacoco/testCodeCoverageReport/html/index.html"

.PHONY: check
check:
	$(GRADLE) $@

.PHONY: eclipse
eclipse:
	$(GRADLE) $@

.PHONY: javadocAll
javadocAll:
	$(GRADLE) $@
	echo "javadoc available at file://$(PWD)/build/javadoc/index.html"

.PHONY: show-dependency-updates
show-dependency-updates:
	$(GRADLE) dependencyUpdates
