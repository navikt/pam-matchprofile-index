package no.nav.arbeidsplassen.matchprofile.index

import io.micronaut.context.annotation.Factory
import jakarta.inject.Singleton
import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.client.CredentialsProvider
import org.apache.http.impl.client.BasicCredentialsProvider
import org.opensearch.client.RestClient
import org.opensearch.client.RestHighLevelClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

@Factory
class OpenSearchConfig {

    @Singleton
    fun buildOpenSearchClient(): RestHighLevelClient {
        val credentialsProvider: CredentialsProvider = BasicCredentialsProvider()

        credentialsProvider.setCredentials(
            AuthScope.ANY,
            UsernamePasswordCredentials("admin", "admin")
        )
        val builder = RestClient.builder(HttpHost("localhost", 9200, "https"))
            .setHttpClientConfigCallback { httpClientBuilder ->
                httpClientBuilder.setDefaultCredentialsProvider(
                    credentialsProvider
                )
                // TODO fix when go prod
                httpClientBuilder.setSSLHostnameVerifier { hostname, session -> true }
                val context = SSLContext.getInstance("SSL")
                context.init(null, arrayOf(object: X509TrustManager {
                    override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                    }

                    override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate>? {
                        return null
                    }

                }),SecureRandom())
                httpClientBuilder.setSSLContext(context)
            }
        return RestHighLevelClient(builder)
    }
}
