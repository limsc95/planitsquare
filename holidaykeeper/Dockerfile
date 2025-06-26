FROM eclipse-temurin:21-jdk-jammy AS builder

WORKDIR /app
COPY . .

# gradlew 실행 권한 부여
RUN chmod +x ./gradlew

# 4. build
RUN ./gradlew clean build -x test

# --- 실행 이미지 ----
FROM eclipse-temurin:21-jre-jammy

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]