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
        "long_overflow_small",
        "long_overflow_medium",
        "long_overflow_large",
        "long_overflow_extra_large",

        "double_overflow_small",
        "double_overflow_medium",
        "double_overflow_large",
        "double_overflow_extra_large",

        "leading_zeros_small",
        "leading_zeros_medium",
        "leading_zeros_large",
        "leading_zeros_extra_large",

        // Failing cases
        "invalid_empty",
        "iso_wrong_order",
        "iso_bad_fraction"
    )
    lateinit var caseId: String

    private fun generateLongOverflowString(k: Int): String = "PT${"1234567890".repeat(k)}S"
    private fun generateDoubleOverflowString(k: Int): String = "PT0.${"0123456789".repeat(k)}S"
    private fun generateLeadingZeros(k: Int): String = "PT${"0000000000".repeat(k)}1H"

    private val longOverflowSmall = generateLongOverflowString(2)
    private val longOverflowMedium = generateLongOverflowString(3)
    private val longOverflowLarge = generateLongOverflowString(8)
    private val longOverflowExtraLarge = generateLongOverflowString(21)

    private val doubleOverflowSmall = generateDoubleOverflowString(2)
    private val doubleOverflowMedium = generateDoubleOverflowString(3)
    private val doubleOverflowLarge = generateDoubleOverflowString(8)
    private val doubleOverflowExtraLarge = generateDoubleOverflowString(21)

    private val leadingZerosSmall = generateLeadingZeros(2)
    private val leadingZerosMedium = generateLeadingZeros(3)
    private val leadingZerosLarge = generateLeadingZeros(8)
    private val leadingZerosExtraLarge = generateLeadingZeros(21)

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
            "long_overflow_small" -> longOverflowSmall
            "long_overflow_medium" -> longOverflowMedium
            "long_overflow_large" -> longOverflowLarge
            "long_overflow_extra_large" -> longOverflowExtraLarge

            "double_overflow_small" -> doubleOverflowSmall
            "double_overflow_medium" -> doubleOverflowMedium
            "double_overflow_large" -> doubleOverflowLarge
            "double_overflow_extra_large" -> doubleOverflowExtraLarge

            "leading_zeros_small" -> leadingZerosSmall
            "leading_zeros_medium" -> leadingZerosMedium
            "leading_zeros_large" -> leadingZerosLarge
            "leading_zeros_extra_large" -> leadingZerosExtraLarge

            // Failing cases
            "invalid_empty" -> ""
            "iso_wrong_order" -> "PT1M2H"
            "iso_bad_fraction" -> "PT0.25.25S"

            else -> error("Unhandled case-id: $caseId")
        }

    val durationWrapper = DurationWrapper(Duration.ZERO)

    @Benchmark
    fun parseIsoString(bh: Blackhole) {
        try {
            durationWrapper.duration = Duration.parseIsoString(input)
            bh.consume(durationWrapper)
        } catch (_: IllegalArgumentException) {
        }
    }
}