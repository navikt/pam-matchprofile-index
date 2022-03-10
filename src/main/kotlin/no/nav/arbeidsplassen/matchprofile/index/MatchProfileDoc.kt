package no.nav.arbeidsplassen.matchprofile.match.indexer

import no.nav.arbeidsplassen.matchprofile.index.MatchProfileDTO
import java.time.Instant

data class MatchProfileDoc(val id: String, val orgnr: String?, val sourceId: String, val type: String, val status: String,
                           val title: String?, val description: String?, val concepts: List<String> = listOf(),
                           val createdBy: String, val updatedBy: String, val expires: Instant, val created: Instant, val updated: Instant)

fun MatchProfileDTO.toDoc() = MatchProfileDoc(id = id, orgnr = orgnr, sourceId = sourceId, type = type, status = status, title = title, description = description,
    concepts = profile.concepts.map { it.label }, createdBy = createdBy, updatedBy = updatedBy, expires = expires, created = created, updated = updated)

