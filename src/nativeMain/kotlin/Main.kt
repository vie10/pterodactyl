import api.KtorPterodactylApi
import api.PterodactylApi
import org.koin.core.context.startKoin
import org.koin.dsl.bind
import org.koin.dsl.module

fun main(args: Array<out String>) {
    startKoin {
        modules(module {
            single { KtorPterodactylApi() } bind PterodactylApi::class
        })
    }
    PterodactylCommand().main(args)
}