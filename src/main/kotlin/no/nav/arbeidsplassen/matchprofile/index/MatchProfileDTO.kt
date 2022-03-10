package no.nav.arbeidsplassen.matchprofile.index

import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

@Introspected
data class MatchProfileDTO (
    var id: String,
    val orgnr: String?,
    val sourceId: String,
    val type: String,
    val status: String,
    val title: String? = null,
    val description: String? = null,
    val profile: ProfileDTO,
    val createdBy: String = "matchprofile-api",
    val updatedBy: String = "matchprofile-api",
    val expires: Instant = Instant.now().plus(30, ChronoUnit.DAYS),
    val created: Instant = Instant.now(),
    val updated: Instant = Instant.now()
)

@Introspected
data class ProfileDTO(val concepts: Set<ConceptDTO> = hashSetOf())

@Introspected
data class ConceptDTO(val label: String, val cid: Long? = null, val branch: String?, val expandedConcept: String? = null,
                      val lang: String = "no")
