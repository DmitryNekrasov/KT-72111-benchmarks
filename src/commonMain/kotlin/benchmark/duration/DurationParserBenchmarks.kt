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
        "new_bad_paren", "new_inf_suffix", "new_inf_short",
        "iso_fail_empty", "iso_fail_space", "iso_fail_P", "iso_fail_PT", "iso_fail_P1DT",
        "iso_fail_P1", "iso_fail_PT1", "iso_fail_0", "iso_fail_plusP", "iso_fail_plus",
        "iso_fail_minus", "iso_fail_h", "iso_fail_H", "iso_fail_something",
        "iso_fail_1m", "iso_fail_1d", "iso_fail_2d11s", "iso_fail_infinity",
        "iso_fail_minusInfinity", "iso_fail_Pplus12plus34D", "iso_fail_P12minus34D",
        "iso_fail_bigNegSecs", "iso_fail_spaceP1D", "iso_fail_PT1S_space", "iso_fail_P3W",
        "iso_fail_P1Y", "iso_fail_P1M", "iso_fail_P1S", "iso_fail_PT1D", "iso_fail_PT1Y",
        "iso_fail_PT1S2S", "iso_fail_PT1S2H", "iso_fail_bigRange",
        "iso_fail_PT1_5H", "iso_fail_PT0_5D", "iso_fail_PT_dot5S", "iso_fail_PT0_25_25S",
        "iso_fail_PT_plusminus2H", "iso_fail_PT_minusplus2H", "iso_fail_PT_plusminusLongS"
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

            // Mappings for iso_fail
            "iso_fail_empty" -> ""
            "iso_fail_space" -> " "
            "iso_fail_P" -> "P"
            "iso_fail_PT" -> "PT"
            "iso_fail_P1DT" -> "P1DT"
            "iso_fail_P1" -> "P1"
            "iso_fail_PT1" -> "PT1"
            "iso_fail_0" -> "0"
            "iso_fail_plusP" -> "+P"
            "iso_fail_plus" -> "+"
            "iso_fail_minus" -> "-"
            "iso_fail_h" -> "h"
            "iso_fail_H" -> "H"
            "iso_fail_something" -> "something"
            "iso_fail_1m" -> "1m"
            "iso_fail_1d" -> "1d"
            "iso_fail_2d11s" -> "2d 11s"
            "iso_fail_infinity" -> "Infinity"
            "iso_fail_minusInfinity" -> "-Infinity"
            "iso_fail_Pplus12plus34D" -> "P+12+34D"
            "iso_fail_P12minus34D" -> "P12-34D"
            "iso_fail_bigNegSecs" -> "PT1234567890-1234567890S"
            "iso_fail_spaceP1D" -> " P1D"
            "iso_fail_PT1S_space" -> "PT1S "
            "iso_fail_P3W" -> "P3W"
            "iso_fail_P1Y" -> "P1Y"
            "iso_fail_P1M" -> "P1M"
            "iso_fail_P1S" -> "P1S"
            "iso_fail_PT1D" -> "PT1D"
            "iso_fail_PT1Y" -> "PT1Y"
            "iso_fail_PT1S2S" -> "PT1S2S"
            "iso_fail_PT1S2H" -> "PT1S2H"
            "iso_fail_bigRange" -> "P9999999999999DT-9999999999999H"
            "iso_fail_PT1_5H" -> "PT1.5H"
            "iso_fail_PT0_5D" -> "PT0.5D"
            "iso_fail_PT_dot5S" -> "PT.5S"
            "iso_fail_PT0_25_25S" -> "PT0.25.25S"
            "iso_fail_PT_plusminus2H" -> "PT+-2H"
            "iso_fail_PT_minusplus2H" -> "PT-+2H"
            "iso_fail_PT_plusminusLongS" -> "PT+-01234567890123456S"

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