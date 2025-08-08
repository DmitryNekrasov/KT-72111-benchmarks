@file:Suppress("unused")

package benchmark.duration

import kotlinx.benchmark.*
import kotlin.time.Duration

@State(Scope.Benchmark)
open class DefaultSuccessfulBenchmark {
    @Param(
        // Successful cases
        "def_len_03",
        "def_len_05",
        "def_len_08",
        "def_len_13",
        "def_len_21",
        "def_len_34",
        "def_len_55",
        "def_len_89",

        // Overflow cases
        "def_double_overflow_small",
        "def_double_overflow_medium",
        "def_double_overflow_large",
        "def_double_overflow_extra_large",

        "def_leading_zeros_small",
        "def_leading_zeros_medium",
        "def_leading_zeros_large",
        "def_leading_zeros_extra_large"
    )
    lateinit var caseId: String

    private fun generateDoubleOverflowString(k: Int): String = "0.${"0123456789".repeat(k)}s"
    private fun generateLeadingZeros(k: Int): String = "${"0000000000".repeat(k)}1h"

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
            "def_len_03" -> "10d"
            "def_len_05" -> "1d12h"
            "def_len_08" -> "-5d23h2m"
            "def_len_13" -> "8d 31h 28m 6s"
            "def_len_21" -> "15d 98m 451s 30.123ms"
            "def_len_34" -> "100d 57h 12m 45s 28ms 3210.12345us"
            "def_len_55" -> "8765d 151h 452m 1233s 9873ms 123451us 987653.12345678ns"
            "def_len_89" -> "-(01257d  012395h 0087542m  000115874s 0871542ms  00951487us    000125845751.985487515ns)"

            // Overflow cases
            "def_double_overflow_small" -> doubleOverflowSmall
            "def_double_overflow_medium" -> doubleOverflowMedium
            "def_double_overflow_large" -> doubleOverflowLarge
            "def_double_overflow_extra_large" -> doubleOverflowExtraLarge

            "def_leading_zeros_small" -> leadingZerosSmall
            "def_leading_zeros_medium" -> leadingZerosMedium
            "def_leading_zeros_large" -> leadingZerosLarge
            "def_leading_zeros_extra_large" -> leadingZerosExtraLarge

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