@file:Suppress("unused")

package benchmark.duration

import kotlinx.benchmark.*
import kotlin.time.Duration

@State(Scope.Benchmark)
class DurationParseBenchmarkMultiInput {
    private val multiInputs = listOf(
        "PT24H",                    // iso_basic
        "PT1H0.123S",               // iso_fraction
        "PT12345678901234567890S",  // iso_oversize_seconds
        "1h 30m",                   // def_multi
        "Infinity",                 // def_infinite
        "",                         // invalid_empty
        "PT1M2H",                   // iso_wrong_order
        "PT0.25.25S",               // iso_bad_fraction
        "1m 1h",                    // def_wrong_order
        "12.5m 11.5s"               // def_bad_fraction
    )
    private var multiInputIndex = 0
    private val durationWrapper = DurationWrapper(Duration.ZERO)

    private fun getNextMultiInput(): String {
        val result = multiInputs[multiInputIndex]
        multiInputIndex = (multiInputIndex + 1) % multiInputs.size
        return result
    }

    @Benchmark
    fun parseMultiString(bh: Blackhole) {
        try {
            durationWrapper.duration = Duration.parse(getNextMultiInput())
            bh.consume(durationWrapper)
        } catch (_: IllegalArgumentException) {
        }
    }

    @Benchmark
    fun parseOrNullMultiString(bh: Blackhole) {
        durationWrapper.duration = Duration.parseOrNull(getNextMultiInput()) ?: Duration.ZERO
        bh.consume(durationWrapper)
    }

    @Benchmark
    fun parseIsoStringMultiString(bh: Blackhole) {
        try {
            durationWrapper.duration = Duration.parseIsoString(getNextMultiInput())
            bh.consume(durationWrapper)
        } catch (_: IllegalArgumentException) {
        }
    }

    @Benchmark
    fun parseIsoStringOrNullMultiString(bh: Blackhole) {
        durationWrapper.duration = Duration.parseIsoStringOrNull(getNextMultiInput()) ?: Duration.ZERO
        bh.consume(durationWrapper)
    }
}