pam-matchprofile-index
================

`pam-matchprofile-index` henter match-profiler over Kafka og indekserer dem i OpenSearch.

# Komme i gang

Applikasjonen kan startes lokalt i terminal fra prosjektets rotmappe: 

```
./gradlew run
```

Eventuelt kan man lage en `Micronaut`-konfigurasjon med Main class `no.nav.arbeidsplassen.matchprofile.index.Application`
i IntelliJ.

API-et eksponeres på port `8080`.

## Lokalt utviklingsmiljø

Se README til [pam-matchprofile-api](https://github.com/navikt/pam-matchprofile-api) for instruksjoner om å 
starte komplett lokalt utviklingsmiljø.

# Henvendelser

Spørsmål knyttet til koden eller prosjektet kan stilles som issues her på GitHub.
