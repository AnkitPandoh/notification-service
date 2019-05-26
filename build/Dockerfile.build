FROM maven:3-alpine

LABEL maintainer="Ankit Pandoh"

ENV WORKDIR /home/ankit

WORKDIR ${WORKDIR}

RUN curl -s -L https://binaries.sonarsource.com/Distribution/sonar-scanner-cli/sonar-scanner-cli-3.3.0.1492-linux.zip -o sonarscanner.zip \
  && unzip -qq sonarscanner.zip \
  && rm -rf sonarscanner.zip \
  && mv sonar-scanner-3.3.0.1492-linux sonar-scanner
  
COPY sonar-scanner.properties sonar-scanner/conf/sonar-scanner.properties

ENV SONAR_RUNNER_HOME=${WORKDIR}/sonar-scanner
ENV PATH $PATH:${WORKDIR}/sonar-scanner/bin

RUN sed -i 's/use_embedded_jre=true/use_embedded_jre=false/g' ${WORKDIR}/sonar-scanner/bin/sonar-scanner
