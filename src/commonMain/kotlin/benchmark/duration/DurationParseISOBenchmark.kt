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
        "len89",

        // Overflow cases
        "iso_overflow_small",
        "iso_overflow_medium",
        "iso_overflow_large",
        "iso_overflow_extra_large",

        // Failing cases
        "invalid_empty",
        "iso_wrong_order",
        "iso_bad_fraction"
    )
    lateinit var caseId: String

    private fun generateOverflowIsoDurationString(k: Int): String = "PT${"1234567890".repeat(k)}S"

    private val isoOverflowSmall = generateOverflowIsoDurationString(2)
    private val isoOverflowMedium = generateOverflowIsoDurationString(3)
    private val isoOverflowLarge = generateOverflowIsoDurationString(8)
    private val isoOverflowExtraLarge = generateOverflowIsoDurationString(21)

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
            "len89" -> "P+00000001000DT-0000000123456708H+0000000875412386098M-0000000125487514523.2580745123500S"

            // Overflow cases
            "iso_overflow_small" -> isoOverflowSmall
            "iso_overflow_medium" -> isoOverflowMedium
            "iso_overflow_large" -> isoOverflowLarge
            "iso_overflow_extra_large" -> isoOverflowExtraLarge

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