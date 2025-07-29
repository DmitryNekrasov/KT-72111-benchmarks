@file:Suppress("unused")

package benchmark.duration

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Param
import kotlinx.benchmark.Scope
import kotlinx.benchmark.State
import kotlin.time.Duration

@State(Scope.Benchmark)
open class DurationParseISOBenchmark {
    @Param(
        // Successful cases
        "len3",
        "len5",
        "len8",
        "len13",
        "len21",
        "len34",
        "len55",

        // Special cases
        "iso_signs",
        "iso_oversize_seconds",

        // Failing cases
        "invalid_empty",
        "iso_wrong_order",
        "iso_bad_fraction"
    )
    lateinit var caseId: String

    val input: String
        get() = when (caseId) {
            // Successful cases
            "len3" -> "P1D"
            "len5" -> "PT24H"
            "len8" -> "P1DT140M"
            "len13" -> "P123DT10H123S"
            "len21" -> "P15DT1234H128M52.874S"
            "len34" -> "P1234DT2587H85471M1234567.5258741S"
            "len55" -> "P1000DT123456708H875412386098M125487514523.25807451235S"

            // Special cases
            "iso_signs" -> "P+5DT-1H+15M-0.123S"
            "iso_oversize_seconds" -> "PT-12345678901234567890S"

            // Failing cases
            "invalid_empty" -> ""
            "iso_wrong_order" -> "PT1M2H"
            "iso_bad_fraction" -> "PT0.25.25S"

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