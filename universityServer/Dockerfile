FROM openjdk:21-jdk-slim
WORKDIR /app
COPY gradlew .
COPY gradle gradle
RUN chmod +x gradlew
COPY . .
RUN ./gradlew build -x test
EXPOSE 8081
CMD ["java", "-jar", "build/libs/theboringuniversity-0.0.1-SNAPSHOT.jar"]

