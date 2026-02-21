# 1. Start with a computer that has Java and Maven installed
FROM maven:3.9-eclipse-temurin-17

# 2. Create a folder inside this computer called /app
WORKDIR /app

# 3. Copy all your MindLog code into this folder
COPY . .

# 4. Download tools and package the app
RUN mvn clean package

# 5. The command to start the server!
CMD ["mvn", "exec:java", "-Dexec.mainClass=MindLogServer"]