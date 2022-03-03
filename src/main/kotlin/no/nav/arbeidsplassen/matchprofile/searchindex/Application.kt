package no.nav.arbeidsplassen.matchprofile.searchindex

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
            .packages("no.nav.arbeidsplassen.matchprofile")
            .mainClass(Application.javaClass)
            .start()
    }

}
