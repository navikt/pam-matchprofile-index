package no.nav.arbeidsplassen.matchprofile.index

import java.time.Instant

data class MatchProfileDoc(val id: String, val orgnr: String?, val sourceId: String, val type: String, val status: String,
                           val title: String?, val description: String?, val concepts: List<String> = listOf(),
                           val country: List<String?> = listOf(), val county: List<String?> = listOf(), val municipal: List<String?> = listOf(),
                           val city: List<String?> = listOf(), val createdBy: String, val updatedBy: String,
                           val expires: Instant, val created: Instant, val updated: Instant)

fun MatchProfileDTO.toDoc() = MatchProfileDoc(id = id, orgnr = orgnr, sourceId = sourceId, type = type, status = status, title = title, description = description,
    concepts = profile.concepts.map { it.label }, country = profile.locations.map { it.country },
    county = profile.locations.map { it.county }, municipal = profile.locations.map { it.municipal },
    createdBy = createdBy, updatedBy = updatedBy, expires = expires, created = created, updated = updated)


