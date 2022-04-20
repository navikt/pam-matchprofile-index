package no.nav.arbeidsplassen.matchprofile.index


import com.fasterxml.jackson.databind.ObjectMapper
import io.micronaut.context.annotation.Value
import jakarta.inject.Singleton
import org.opensearch.action.admin.indices.alias.IndicesAliasesRequest
import org.opensearch.action.admin.indices.alias.get.GetAliasesRequest
import org.opensearch.action.bulk.BulkRequest
import org.opensearch.action.bulk.BulkResponse
import org.opensearch.action.index.IndexRequest
import org.opensearch.client.RequestOptions
import org.opensearch.client.RestHighLevelClient
import org.opensearch.client.indices.CreateIndexRequest
import org.opensearch.client.indices.GetIndexRequest
import org.opensearch.common.xcontent.XContentType
import org.opensearch.rest.RestStatus
import org.slf4j.LoggerFactory

@Singleton
class MatchProfileIndexer(private val client: RestHighLevelClient,
                          @Value("\${matchprofile.index.name:matchprofile-2022}") private val indexName: String,
                          private val objectMapper: ObjectMapper) {

    companion object {
        private val LOG = LoggerFactory.getLogger(MatchProfileIndexer::class.java)
        private val SETTINGS = MatchProfileIndexer::class.java
            .getResource("/opensearch/matchprofile-settings.json").readText()
        private val MAPPING = MatchProfileIndexer::class.java
            .getResource("/opensearch/matchprofile-mapping.json").readText()
    }

    init {
        try {
            initIndex(indexName)
        } catch (e: Exception) {
            LOG.error("OpenSearch might not be ready ${e.message}, will wait 20s and retry")
            Thread.sleep(20000)
            initIndex(indexName)
        }
    }

    private fun initIndex(indexName: String) {
        val indexRequest= GetIndexRequest(indexName)
        if (!client.indices().exists(indexRequest, RequestOptions.DEFAULT)) {
            if (createIndex(indexName))
                LOG.info("$indexName has been created")
            else
                LOG.error("Failed to create $indexName")
        }
        val aliasIndexRequest = GetAliasesRequest(MATCHPROFILE)
        val response = client.indices().getAlias(aliasIndexRequest, RequestOptions.DEFAULT)
        if (response.status() == RestStatus.NOT_FOUND) {
            LOG.warn("Alias $MATCHPROFILE is not pointing to any index, updating alias")
            updateAlias(indexName)
        }

    }

    fun updateAlias(indexName: String, removePreviousAliases: Boolean = false): Boolean {
        val remove = IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.REMOVE)
            .index("$MATCHPROFILE*")
            .alias(MATCHPROFILE)
        val add = IndicesAliasesRequest.AliasActions(IndicesAliasesRequest.AliasActions.Type.ADD)
            .index(indexName)
            .alias(MATCHPROFILE)
        val request = IndicesAliasesRequest().apply {
            if (removePreviousAliases) addAliasAction(remove)
            addAliasAction(add)
        }
        LOG.info("updateAlias for alias $MATCHPROFILE and pointing to $indexName ")
        return client.indices().updateAliases(request, RequestOptions.DEFAULT).isAcknowledged
    }

    fun createIndex(indexName: String): Boolean {
        val createIndexRequest = CreateIndexRequest(indexName)
            .source(SETTINGS, XContentType.JSON)
            .mapping(MAPPING, XContentType.JSON)
        return client.indices().create(createIndexRequest, RequestOptions.DEFAULT).isAcknowledged
    }

    fun index(matchProfiles: List<MatchProfileDTO>): BulkResponse {
        return index(matchProfiles, indexName)
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
const val MATCHPROFILE = "matchprofile"
