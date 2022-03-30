package no.nav.arbeidsplassen.matchprofile.index

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import org.slf4j.LoggerFactory

@Controller("/internal")
class StatusController() {

    companion object {
        private val LOG = LoggerFactory.getLogger(StatusController::class.java)
    }

    @Get("/isReady")
    fun isReady(): HttpResponse<String> {
        return HttpResponse.ok("OK")
    }

    @Get("/isAlive")
    fun isAlive(): HttpResponse<String> {
        return HttpResponse.ok("OK")
    }

    @Get("/check")
    fun check(): HttpResponse<String> {
        return HttpResponse.ok("OK")
    }

}
