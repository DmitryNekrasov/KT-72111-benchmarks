@file:Suppress("unused")

package benchmark.duration

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Param
import kotlinx.benchmark.Scope
import kotlinx.benchmark.State
import kotlin.time.Duration

@State(Scope.Benchmark)
open class IsoSuccessfulBenchmark {
    @Param(
        "iso_len3",
        "iso_len_05",
        "iso_len_08",
        "iso_len_13",
        "iso_len_21",
        "iso_len_34",
        "iso_len_55",
        "iso_len_89",

        "iso_long_overflow_small",
        "iso_long_overflow_medium",
        "iso_long_overflow_large",
        "iso_long_overflow_extra_large",

        "iso_double_overflow_small",
        "iso_double_overflow_medium",
        "iso_double_overflow_large",
        "iso_double_overflow_extra_large",

        "iso_leading_zeros_small",
        "iso_leading_zeros_medium",
        "iso_leading_zeros_large",
        "iso_leading_zeros_extra_large",

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
            "iso_len_03" -> "P1D"
            "iso_len_05" -> "PT24H"
            "iso_len_08" -> "P1DT140M"
            "iso_len_13" -> "P123DT10H123S"
            "iso_len_21" -> "P15DT1234H128M52.874S"
            "iso_len_34" -> "P1234DT2587H85471M1234567.5258741S"
            "iso_len_55" -> "P1000DT123456708H875412386098M125487514523.25807451235S"
            "iso_len_89" -> "P+00000001000DT-0000000123456708H+0000000875412386098M-0000000125487514523.2580745123500S"

            "iso_long_overflow_small" -> longOverflowSmall
            "iso_long_overflow_medium" -> longOverflowMedium
            "iso_long_overflow_large" -> longOverflowLarge
            "iso_long_overflow_extra_large" -> longOverflowExtraLarge

            "iso_double_overflow_small" -> doubleOverflowSmall
            "iso_double_overflow_medium" -> doubleOverflowMedium
            "iso_double_overflow_large" -> doubleOverflowLarge
            "iso_double_overflow_extra_large" -> doubleOverflowExtraLarge

            "iso_leading_zeros_small" -> leadingZerosSmall
            "iso_leading_zeros_medium" -> leadingZerosMedium
            "iso_leading_zeros_large" -> leadingZerosLarge
            "iso_leading_zeros_extra_large" -> leadingZerosExtraLarge

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