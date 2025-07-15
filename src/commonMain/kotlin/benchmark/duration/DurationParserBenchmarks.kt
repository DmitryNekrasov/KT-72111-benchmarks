@file:Suppress("unused")

package benchmark.duration

import kotlinx.benchmark.*
import kotlin.time.Duration

@State(Scope.Benchmark)
open class DurationParseBenchmark {

    @Param(
        // Successful cases
        "iso_basic",
        "iso_fraction",
        "iso_oversize_seconds",
        "def_multi",
        "def_infinite",

        // Failing cases
        "invalid_empty",
        "iso_wrong_order",
        "iso_bad_fraction",
        "def_wrong_order",
        "def_bad_fraction"
    )
    lateinit var caseId: String

    val input: String
        get() = when (caseId) {
            //  Success
            "iso_basic" -> "PT24H"
            "iso_fraction" -> "PT1H0.123S"
            "iso_oversize_seconds" -> "PT12345678901234567890S"
            "def_multi" -> "1h 30m"
            "def_infinite" -> "Infinity"

            // Failure
            "invalid_empty" -> ""
            "iso_wrong_order" -> "PT1M2H"
            "iso_bad_fraction" -> "PT0.25.25S"
            "def_wrong_order" -> "1m 1h"
            "def_bad_fraction" -> "12.5m 11.5s"

            else -> error("Unhandled case-id: $caseId")
        }

    class DurationWrapper(var duration: Duration)

    val durationWrapper = DurationWrapper(Duration.ZERO)

    @Benchmark
    fun parseAndConsumeBoxedDuration(bh: Blackhole) {
        try {
            bh.consume(Duration.parse(input))
        } catch (_: IllegalArgumentException) {
        }
    }

    @Benchmark
    fun parse(bh: Blackhole) {
        try {
            durationWrapper.duration = Duration.parse(input)
            bh.consume(durationWrapper)
        } catch (_: IllegalArgumentException) {
        }
    }

    @Benchmark
    fun parseOrNull(bh: Blackhole) {
        durationWrapper.duration = Duration.parseOrNull(input) ?: Duration.ZERO
        bh.consume(durationWrapper)
    }

    @Benchmark
    fun parseIsoString(bh: Blackhole) {
        try {
            durationWrapper.duration = Duration.parseIsoString(input)
            bh.consume(durationWrapper)
        } catch (_: IllegalArgumentException) {
        }
    }

    @Benchmark
    fun parseIsoStringOrNull(bh: Blackhole) {
        durationWrapper.duration = Duration.parseIsoStringOrNull(input) ?: Duration.ZERO
        bh.consume(durationWrapper)
    }
}