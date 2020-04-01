# medcom-vdx-organisation
Organisationservice


# Udviklerguide

Til udviklingsformål anvendes følgende værktøjer:
* Maven (2+)
* Java (JDK 11)
* Docker (version 16+)

medcom-vdx-organisation er bygge op af følgende moduler
* medcom-vdx-organisation-interface: Interfaceklasser til Organisations API'et
* medcom-vdx-organisation-service: Organisations API'et services, forretningslogik
* medcom-vdx-organisation-web: Organisations API'et passes som Spring Boot Web applikation i Docker
* medcom-vdx-organisation-documentation: OpenAPI 3.0 dokumentation for Organisations API'et og generering af testklientkode
* medcom-vdx-organisation-integrationtest: Opstart af Organisations API'et (i Docker) og afvikling af integrationstests baseret på klientkode fra medcom-vdx-organisation-documentation
* medcom-vdx-organisation-qa: Samlet Jacoco testrapport (herunder codecoverage)

## Udvikling af snitflader i Organisationservice

En udviklingsopgave på snitfladen starter med, at dokumentationen opdateres. Som editor kan med fordel vælges SwaggerEditor, der har autocompletion og preview.
Den kan startes i Docker:
´´´
docker run -d -p 8080:8080 swaggerapi/swagger-editor
´´´
Hvorefter editoren kan anvendes i en [browser](http://localhost:8080).

Når snitfladebeskrivelsen er opdateret kan der dannes nu testklientkode ved at bygge modulet medcom-vdx-organisation-documentation.

Derefter kan passende integrationstests udvikles i medcom-vdx-organisation-integrationtest.

Selve servicen implementeres i de to moduler medcom-vdx-organisation-interface hhv medcom-vdx-organisation-service

## Afvikling af integrationstest

Det er muligt at afvikle integrationstests på to måder. Den ene måde starter servicen op i en container med det docker image som servicen bygger. Den anden måde starter servicen op som en Spring Boot applikation. Fordelen ved den sidste måde er at det så er muligt at debugge servicen fra ens favorit IDE. 

**Service startes i docker**

For at afvikle integrationstest mod servicen kørende i en container aktiverer du blot maven profilen docker-test. Det er også sådan byg på Jenkins afvikles. 

``m̀vn clean install -Pdocker-test``

**Service startes som Spring Boot applikation**

Dette er standard måden når du bygger med maven og også sådan testen afvikles hvis du debugger en test i dit favorit IDE.  
