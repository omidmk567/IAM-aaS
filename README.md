# IAM-aaS
## Overview
This repository contains the codebase for IAM-aaS, an Identity and Access Management as a Service solution which uses [Keycloak](https://keycloak.org) as identity provider.
## Getting Started
### Prerequisites
- Docker and Docker Compose installed on your system
- Java 21 or higher
- Maven

### Setup

1. **Clone the IAM-aaS repository**:
```bash
git clone https://github.com/omidmk567/IAM-aaS.git && cd IAM-aaS/iam-api
```

2. **Set required environment variables placed in `iam-api/src/main/resources/application.properties` and `keycloak/iam-aas-realm.json`**, which are:

- `spring.mail.password`: Your mail smtp password to send mails.
- `vault.smtp-password`: Your mail smtp password to send mails via keycloak.
- `vault.google-client-secret`: Your Google client secret for oauth2. You may want to configure your client id and client secret.
- `vault.github-client-secret`: Your GitHub client secret for oauth2. You may want to configure your client id and client secret.

3. **Optionally configure other properties placed in `application.properties` file.**

4. **Build and start the IAM-aaS containers.**
```bash
mvn spring-boot:run
```
There are two profiles: `dev` and `prod`. You can specify the active profile with option `-Dspring-boot.run.profiles=prod`. the default active profile is `dev`.
Webserver is accessible on `localhost:8000`.

## Contributing
We welcome contributions to IAM-aaS! Feel free to submit bug reports, feature requests, or pull requests through our GitHub repository.


