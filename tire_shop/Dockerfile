# Używamy obrazu z JDK 17 (wersja slim jest lżejsza)
FROM openjdk:17-jdk-slim

# Ustawiamy katalog roboczy wewnątrz kontenera
WORKDIR /app

# Kopiujemy plik .jar z folderu target do kontenera
COPY target/myapp-0.0.1-SNAPSHOT.jar myapp.jar

# Ujawniamy port 8080 (nie zawsze wymagane, ale dobra praktyka)
EXPOSE 8080

# Definiujemy polecenie startowe
ENTRYPOINT ["java", "-jar", "myapp.jar"]
