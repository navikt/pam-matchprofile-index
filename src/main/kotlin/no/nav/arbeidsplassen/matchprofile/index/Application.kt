package no.nav.arbeidsplassen.matchprofile.index

import io.micronaut.runtime.Micronaut

object Application {

    @JvmStatic
    fun main(args: Array<String>) {
        Micronaut.build()
            .packages("no.nav.arbeidsplassen.matchprofile.index")
            .mainClass(Application.javaClass)
            .start()
    }

}
