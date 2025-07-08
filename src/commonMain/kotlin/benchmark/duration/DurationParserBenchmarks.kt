@file:Suppress("unused")

package benchmark.duration

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Param
import kotlinx.benchmark.Scope
import kotlinx.benchmark.State
import kotlin.time.Duration

@State(Scope.Benchmark)
open class DurationParseBenchmark {

    @Param(
        "iso_basic", "iso_fraction", "iso_negative_component", "iso_oversize_seconds",
        "iso_wrong_order", "iso_unsupported_months", "iso_bad_fraction",
        "def_basic", "def_multi", "def_negative_paren", "def_infinite",
        "def_infinite_uc", "def_neg_infinite", "def_wrong_order",
        "def_bad_fraction", "iso_fails_default_ok",
        "invalid_empty", "invalid_junk",
        "new_invalid_whitespace", "new_plus_no_body", "new_minus_no_body",
        "new_iso_PT_only", "new_iso_week", "new_iso_dup_sec",
        "new_iso_unknown_unit", "new_def_unknown_unit",
        "new_def_fraction_not_last", "new_def_dup_unit",
        "new_bad_paren", "new_inf_suffix", "new_inf_short"
    )
    lateinit var caseId: String

    val input: String
        get() = when (caseId) {
            // Valid ISO-8601 composite
            "iso_basic" -> "PT24H"
            "iso_fraction" -> "PT1H0.123S"
            "iso_negative_component" -> "PT-23H-45M"
            "iso_oversize_seconds" -> "PT12345678901234567890S"

            // Wrong order -> exception
            "iso_wrong_order" -> "PT1M2H"
            "iso_unsupported_months" -> "P1M"
            "iso_bad_fraction" -> "PT0.25.25S"

            // Valid default format
            "def_basic" -> "10s"
            "def_multi" -> "1h 30m"
            "def_negative_paren" -> "-(1h 30m)"
            "def_infinite" -> "Infinity"
            "def_infinite_uc" -> "INFINITY"
            "def_neg_infinite" -> "-Infinity"

            // Wrong order / malformed fraction -> exception
            "def_wrong_order" -> "1m 1h"
            "def_bad_fraction" -> "12.5m 11.5s"

            // Default succeeds, strict ISO must fail
            "iso_fails_default_ok" -> "Infinity"  // accepted only when strictIso = false

            // Totally invalid, early failures
            "invalid_empty" -> ""
            "invalid_junk" -> "something"

            // Exclusively-failing values
            "new_invalid_whitespace" -> " "
            "new_plus_no_body" -> "+"
            "new_minus_no_body" -> "-"
            "new_iso_PT_only" -> "PT"
            "new_iso_week" -> "P3W"
            "new_iso_dup_sec" -> "PT1S2S"
            "new_iso_unknown_unit" -> "PT1X"
            "new_def_unknown_unit" -> "10x"
            "new_def_fraction_not_last" -> "1.5s 1ms"
            "new_def_dup_unit" -> "1h 1h"
            "new_bad_paren" -> "-(12m 30s"
            "new_inf_suffix" -> "Infinity value"
            "new_inf_short" -> "Inf"

            else -> error("Unhandled case-id: $caseId")
        }

    @Benchmark
    fun parse(bh: Blackhole) {
        try {
            bh.consume(Duration.parse(input))
        } catch (_: IllegalArgumentException) {
        }
    }

    @Benchmark
    fun parseOrNull(bh: Blackhole) {
        bh.consume(Duration.parseOrNull(input))
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