# Базовый образ для Java
FROM openjdk:17-jdk-slim

# Установим рабочую директорию
WORKDIR /app

# Копируем jar-файл
COPY target/demo-0.0.1-SNAPSHOT.jar app.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "app.jar"]
