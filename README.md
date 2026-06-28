<br />
<div align="center">
<h3 align="center">PRAGMA POWER-UP</h3>
  <p align="center">
    In this challenge you are going to design the backend of a system that centralizes the services and orders of a restaurant chain that has different branches in the city.
  </p>
</div>

## License

This project is licensed under the **Apache License 2.0** - see the [LICENSE](LICENSE) file for details.

### Built With

* ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=java&logoColor=white)
* ![Spring](https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
* ![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
* ![MySQL](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)


<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these steps.

### Prerequisites

* JDK 21 LTS [https://jdk.java.net/21](https://jdk.java.net/21)
* Gradle [https://gradle.org/install/](https://gradle.org/install/)
* MySQL [https://dev.mysql.com/downloads/installer/](https://dev.mysql.com/downloads/installer/)

### Recommended Tools
* IntelliJ Community [https://www.jetbrains.com/idea/download/](https://www.jetbrains.com/idea/download/)
* Postman [https://www.postman.com/downloads/](https://www.postman.com/downloads/)

### Installation

1. Clone the repo
2. Change directory
   ```sh
   cd power-up-arquetipo
   ```
3. Create a new database in MySQL called powerup
4. Configure the required environment variables. The application does **not** ship
   with default secrets, so it will not start unless these are set:

   | Variable | Required | Description |
   |----------|----------|-------------|
   | `JWT_SECRET` | **Yes** | Signing key for JWT. Minimum 32 characters (HMAC-SHA256). |
   | `DB_PASSWORD` | **Yes** | MySQL password. |
   | `DB_USERNAME` | No (default `root`) | MySQL user. |
   | `DB_URL` | No | Overrides the JDBC URL (default `jdbc:mysql://localhost:3306/powerup`). |
   | `JWT_EXPIRATION_MS` | No (default `3600000`) | Token lifetime in milliseconds. |
   | `TWILIO_ENABLED` | No (default `false`) | `true` to send real SMS via Twilio; otherwise SMS are only logged. |
   | `TWILIO_ACCOUNT_SID` | Only if Twilio enabled | Twilio Account SID. |
   | `TWILIO_AUTH_TOKEN` | Only if Twilio enabled | Twilio Auth Token. |
   | `TWILIO_FROM_NUMBER` | Only if Twilio enabled | Twilio sender number in E.164 format (e.g. `+1XXXXXXXXXX`). |

   Example (bash):
   ```sh
   export JWT_SECRET="a-very-long-random-secret-at-least-32-chars"
   export DB_PASSWORD="your-mysql-password"
   # Optional: enable real SMS notifications
   export TWILIO_ENABLED=true
   export TWILIO_ACCOUNT_SID="ACxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
   export TWILIO_AUTH_TOKEN="your-auth-token"
   export TWILIO_FROM_NUMBER="+1XXXXXXXXXX"
   ```

   > **SMS notifications (HU14):** when an order is marked *ready*, the client receives
   > an SMS with the pickup PIN. With a Twilio **trial** account you can only send to
   > numbers you have verified in the Twilio console, and messages are prefixed with a
   > trial notice. The recipient is the client's `celular`, stored at registration in
   > E.164 format (e.g. `+57XXXXXXXXXX`).

<!-- USAGE -->
## Usage

1. Right-click the class PowerUpApplication and choose Run
2. Open [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html) in your web browser

<!-- ROADMAP -->
## Tests

- Right-click the test folder and choose Run tests with coverage

## Technology Stack

- **Backend Framework**: Spring Boot 3.3.x
- **Language**: Java 21 LTS
- **Build Tool**: Gradle
- **Database**: MySQL 8.0+
- **Architecture**: Hexagonal Architecture
- **Authentication**: JWT (JSON Web Tokens)
- **Documentation**: OpenAPI 3.0 / Swagger UI

## Recent Updates

This project has been updated to **Spring Boot 3.3** with **Java 21 LTS**. See the following documentation for details:

- [Migration Guide](MIGRATION_GUIDE.md) - Detailed changes and technical updates
- [Benefits & Improvements](BENEFITS.md) - Performance improvements and benefits


