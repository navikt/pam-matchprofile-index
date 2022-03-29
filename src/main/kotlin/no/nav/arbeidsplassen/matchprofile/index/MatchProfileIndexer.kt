package no.nav.arbeidsplassen.matchprofile.index


import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.inject.Singleton
import org.opensearch.action.bulk.BulkRequest
import org.opensearch.action.bulk.BulkResponse
import org.opensearch.action.index.IndexRequest
import org.opensearch.client.RequestOptions
import org.opensearch.client.RestHighLevelClient
import org.opensearch.client.indices.CreateIndexRequest
import org.opensearch.client.indices.GetIndexRequest
import org.opensearch.common.xcontent.XContentType
import org.slf4j.LoggerFactory

@Singleton
class MatchProfileIndexer(private val client: RestHighLevelClient, private val objectMapper: ObjectMapper) {

    companion object {
        private val LOG = LoggerFactory.getLogger(MatchProfileIndexer::class.java)
        private val SETTINGS = MatchProfileIndexer::class.java
            .getResource("/opensearch/matchprofile-settings.json").readText()
        private val MAPPING = MatchProfileIndexer::class.java
            .getResource("/opensearch/matchprofile-mapping.json").readText()
        val defaultIndex = "matchprofile"
    }

    init {
        try {
            initIndex(defaultIndex)
        } catch (e: Exception) {
            LOG.error("OpenSearch might not be ready ${e.message}, will wait 20s and retry")
            Thread.sleep(20000)
            initIndex("matchprofile")
        }
    }

    private fun initIndex(indexName: String) {
        val indexRequest= GetIndexRequest(indexName)
        if (!client.indices().exists(indexRequest, RequestOptions.DEFAULT) && createIndex(indexName)) {
            LOG.info("$indexName has been created")
        }
        else {
            LOG.error("Failed to create $indexName")
        }
    }

    fun createIndex(indexName: String): Boolean {
        val createIndexRequest = CreateIndexRequest(indexName)
            .source(SETTINGS, XContentType.JSON)
            .mapping(MAPPING, XContentType.JSON)
        return client.indices().create(createIndexRequest, RequestOptions.DEFAULT).isAcknowledged
    }

    fun index(matchProfiles: List<MatchProfileDTO>): BulkResponse {
        return index(matchProfiles, defaultIndex)
    }

    fun index(matchProfile: MatchProfileDTO, indexName: String): BulkResponse {
        return index(listOf(matchProfile), indexName)
    }

    fun index(matchProfiles: List<MatchProfileDTO>, indexName: String): BulkResponse {
        val bulkRequest = BulkRequest()
        matchProfiles.forEach {
            bulkRequest.add(IndexRequest(indexName)
                .id(it.id)
                .source(objectMapper.writeValueAsString(it.toDoc()), XContentType.JSON)
            )
        }
        return client.bulk(bulkRequest, RequestOptions.DEFAULT)
    }

}
