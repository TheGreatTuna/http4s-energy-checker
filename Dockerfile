FROM sbtscala/scala-sbt:eclipse-temurin-17.0.4_1.7.1_3.2.0

WORKDIR /app

COPY . .

RUN sbt assembly
CMD sbt run
#copy the build.sbt first, then download dependencies, then run assembly