@file:Suppress("unused")

package benchmark.duration

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Param
import kotlinx.benchmark.Scope
import kotlinx.benchmark.State
import kotlin.time.Duration

@State(Scope.Benchmark)
open class IsoFailingBenchmark {
    @Param(
        "iso_wrong_order",
        "iso_bad_fraction"
    )
    lateinit var caseId: String

    val input: String
        get() = when (caseId) {
            "iso_wrong_order" -> "PT1M2H"
            "iso_bad_fraction" -> "PT0.25.25S"

            else -> error("Unhandled case-id: $caseId")
        }

    @Benchmark
    fun parseIsoString(bh: Blackhole) {
        try {
            bh.consume(Duration.parseIsoString(input))
        } catch (_: IllegalArgumentException) {
        }
    }

    @Benchmark
    fun parseIsoStringOrNull(bh: Blackhole) {
        bh.consume(Duration.parseIsoStringOrNull(input))
    }
}