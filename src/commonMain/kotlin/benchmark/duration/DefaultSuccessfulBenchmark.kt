@file:Suppress("unused")

package benchmark.duration

import kotlinx.benchmark.*
import kotlin.time.Duration

@State(Scope.Benchmark)
open class DefaultSuccessfulBenchmark {
    @Param(
        "def_len_03",
        "def_len_05_long",
        "def_len_05_fraction",
        "def_len_08",
        "def_len_13",
        "def_len_21_long",
        "def_len_21_fraction",
        "def_len_21_mixed",
        "def_len_34",
        "def_len_55",
        "def_len_89_long",
        "def_len_89_mixed",

        "def_double_overflow_days_small",
        "def_double_overflow_days_medium",
        "def_double_overflow_days_large",
        "def_double_overflow_days_extra_large",

        "def_double_overflow_seconds_small",
        "def_double_overflow_seconds_medium",
        "def_double_overflow_seconds_large",
        "def_double_overflow_seconds_extra_large",

        "def_leading_zeros_small",
        "def_leading_zeros_medium",
        "def_leading_zeros_large",
        "def_leading_zeros_extra_large"
    )
    lateinit var caseId: String

    private fun generateDoubleOverflowString(k: Int, unit: String): String = "0.${"0123456789".repeat(k)}$unit"
    private fun generateLeadingZeros(k: Int): String = "${"0000000000".repeat(k)}1h"

    private val doubleOverflowDaysSmall = generateDoubleOverflowString(2, "d")
    private val doubleOverflowDaysMedium = generateDoubleOverflowString(3, "d")
    private val doubleOverflowDaysLarge = generateDoubleOverflowString(8, "d")
    private val doubleOverflowDaysExtraLarge = generateDoubleOverflowString(21, "d")

    private val doubleOverflowSecondsSmall = generateDoubleOverflowString(2, "s")
    private val doubleOverflowSecondsMedium = generateDoubleOverflowString(3, "s")
    private val doubleOverflowSecondsLarge = generateDoubleOverflowString(8, "s")
    private val doubleOverflowSecondsExtraLarge = generateDoubleOverflowString(21, "s")

    private val leadingZerosSmall = generateLeadingZeros(2)
    private val leadingZerosMedium = generateLeadingZeros(3)
    private val leadingZerosLarge = generateLeadingZeros(8)
    private val leadingZerosExtraLarge = generateLeadingZeros(21)

    val input: String
        get() = when (caseId) {
            "def_len_03" -> "10d"
            "def_len_05_long" -> "1d12h"
            "def_len_05_fraction" -> "0.12s"
            "def_len_08" -> "-5d23h2m"
            "def_len_13" -> "8d 31h 28m 6s"
            "def_len_21_long" -> "1585749654254823172ns"
            "def_len_21_fraction" -> "0.523974152896345971s"
            "def_len_21_mixed" -> "15d 98m 451s 30.123ms"
            "def_len_34" -> "100d 57h 12m 45s 28ms 3210.12345us"
            "def_len_55" -> "8765d 151h 452m 1233s 9873ms 123451us 987653.12345678ns"
            "def_len_89_long" -> "108652d 508745h 5723201m 1052381s 50423698574557ms 50985412302587us 5039785452315812307ns"
            "def_len_89_mixed" -> "-(01257d  012395h 0087542m  000115874s 0871542ms  00951487us    000125845751.985487515ns)"

            "def_double_overflow_days_small" -> doubleOverflowDaysSmall
            "def_double_overflow_days_medium" -> doubleOverflowDaysMedium
            "def_double_overflow_days_large" -> doubleOverflowDaysLarge
            "def_double_overflow_days_extra_large" -> doubleOverflowDaysExtraLarge

            "def_double_overflow_seconds_small" -> doubleOverflowSecondsSmall
            "def_double_overflow_seconds_medium" -> doubleOverflowSecondsMedium
            "def_double_overflow_seconds_large" -> doubleOverflowSecondsLarge
            "def_double_overflow_seconds_extra_large" -> doubleOverflowSecondsExtraLarge

            "def_leading_zeros_small" -> leadingZerosSmall
            "def_leading_zeros_medium" -> leadingZerosMedium
            "def_leading_zeros_large" -> leadingZerosLarge
            "def_leading_zeros_extra_large" -> leadingZerosExtraLarge

            else -> error("Unhandled case-id: $caseId")
        }

    val durationWrapper = DurationWrapper(Duration.ZERO)

    @Benchmark
    fun parse(bh: Blackhole) {
        durationWrapper.duration = Duration.parse(input)
        bh.consume(durationWrapper)
    }
}
