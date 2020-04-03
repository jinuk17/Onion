package calulator

import java.lang.RuntimeException

class StringCalculator {

    fun add(text: String?): Int {

        if(text.isNullOrEmpty()) return 0

        return split(text).map { toPositive(it) }.sum()
    }

    private fun split(text: String): List<String> {

        val m = "//(.)\n(.*)".toRegex().matchEntire(text)

        if(m != null) {
            val customDelimiter = m.groups[1]?.value
            if (customDelimiter != null) {
                val value = m.groups[2]?.value
                if(value != null) {
                    return value.split(customDelimiter)
                }
            }
        }

        return text.split("[,:]".toRegex())
    }

    private fun toPositive(value: String): Int {
        val number = value.toInt()
        if(number < 0) throw RuntimeException()
        return number
    }

}
