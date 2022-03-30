package no.nav.arbeidsplassen.matchprofile.index

import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Test
import java.util.UUID

@MicronautTest
class MatchProfileIndexerTest(private val indexer: MatchProfileIndexer) {

    //@Test good for testing with OS
    fun indexerInit() {
        indexer.index(listOf(MatchProfileDTO(id = UUID.randomUUID().toString(), orgnr = "123",
            sourceId = UUID.randomUUID().toString(), type = "JOB",status = "ACTIVE", profile = ProfileDTO() )))
    }
}