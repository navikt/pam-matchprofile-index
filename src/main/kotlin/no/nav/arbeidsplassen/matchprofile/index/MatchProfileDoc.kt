package no.nav.arbeidsplassen.matchprofile.match.indexer

import no.nav.arbeidsplassen.matchprofile.index.MatchProfileDTO
import java.time.Instant

data class MatchProfileDoc(val id: String, val sourceId: String, val status: String,  val title: String?, val description: String?, val type: String,
                           val concepts: List<String> = listOf(), val createdBy: String = "matchprofile-api",
                           val updatedBy: String = "matchprofile-api", val created: Instant = Instant.now(),
                           val updated: Instant = Instant.now())

fun MatchProfileDTO.toDoc() = MatchProfileDoc(id = id, sourceId = sourceId, type = type, status = status, title = title, description = description,
    concepts = profile.concepts.map { it.label }, createdBy = createdBy, updatedBy = updatedBy, created = created, updated = updated)

