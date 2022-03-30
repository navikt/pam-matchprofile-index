package no.nav.arbeidsplassen.matchprofile.index

import io.micronaut.configuration.kafka.annotation.KafkaListener
import io.micronaut.configuration.kafka.annotation.OffsetReset
import io.micronaut.configuration.kafka.annotation.OffsetStrategy
import io.micronaut.configuration.kafka.annotation.Topic
import io.micronaut.context.annotation.Requires
import org.apache.kafka.clients.consumer.Consumer
import org.slf4j.LoggerFactory

@Requires(property = "matchprofile.kafka.enabled", value = "true")
@KafkaListener(groupId = "\${matchprofile.kafka.group-id:pam-matchprofile-index}", threads = 1, offsetReset = OffsetReset.EARLIEST,
    batch = true, offsetStrategy = OffsetStrategy.DISABLED)
class MatchProfileTopicListener(private val matchProfileIndexer: MatchProfileIndexer, private val kafkaStateRegistry: KafkaStateRegistry) {

    companion object {
        private val LOG = LoggerFactory.getLogger(MatchProfileTopicListener::class.java)
    }

    @Topic("\${matchprofile.kafka.topic:teampam.pam-matchprofile-intern-1}")
    fun receive(profiles: List<MatchProfileDTO>, offsets: List<Long>, partitions: List<Int>, topics: List<String>, kafkaconsumer: Consumer<*, *>) {
        LOG.info("Received batch with {} matchprofiles", profiles.size)
        if (kafkaStateRegistry.hasError()) {
            LOG.error("Kafka state is set to error, skipping this batch to avoid message loss. Consumer should be set to pause")
            return
        }
        if (profiles.isNotEmpty()) {
            val response = matchProfileIndexer.index(profiles)
            if (response.hasFailures()) {
                LOG.error("We got error while indexing to opensearch ${response.buildFailureMessage()}")
                LOG.error("failed at start batch offset ${offsets[0]} partition ${partitions[0]}")
                throw Throwable("Index failed!")
            }
            LOG.info("Index ${profiles.size} successfully , committing offset")
            kafkaconsumer.commitSync()
        }
    }
}
