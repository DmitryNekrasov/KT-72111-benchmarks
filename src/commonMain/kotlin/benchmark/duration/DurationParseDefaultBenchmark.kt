@file:Suppress("unused")

package benchmark.duration

import kotlinx.benchmark.*
import kotlin.time.Duration

@State(Scope.Benchmark)
open class DurationParseDefaultBenchmark {
    @Param(
        // Successful cases
        "len3",
        "len5",
        "len8",
        "len13",
        "len21",
        "len34",
        "len55",
        "len89"
    )
    lateinit var caseId: String

    val input: String
        get() = when (caseId) {
            // Successful cases
            "len3" -> "10d"
            "len5" -> "1d12h"
            "len8" -> "-5d23h2m"
            "len13" -> "8d 31h 28m 6s"
            "len21" -> "15d 98m 451s 30.123ms"
            "len34" -> "100d 57h 12m 45s 28ms 3210.12345us"
            "len55" -> "8765d 151h 452m 1233s 9873ms 123451us 987653.12345678ns"
            "len89" -> "-(01257d  012395h 0087542m  000115874s 0871542ms  00951487us    000125845751.985487515ns)"

            else -> error("Unhandled case-id: $caseId")
        }

    val durationWrapper = DurationWrapper(Duration.ZERO)

    @Benchmark
    fun parse(bh: Blackhole) {
        try {
            durationWrapper.duration = Duration.parse(input)
            bh.consume(durationWrapper)
        } catch (_: IllegalArgumentException) {
        }
    }
}