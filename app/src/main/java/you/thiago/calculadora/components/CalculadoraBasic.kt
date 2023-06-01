package you.thiago.calculadora.components

import android.content.Context
import you.thiago.calculadora.R
import java.lang.ref.WeakReference

open class CalculadoraBasic(context: Context) : Calculadora {

    private val weakContext = WeakReference(context)

    override fun operateValue(currentValue: String, value: String): String {
        val isAddingDirectValue = currentValue == "0" && value.isNumeric()
        val isAddingNegativeSignal = currentValue == "0" && value == "-"

        return if (isAddingDirectValue || isAddingNegativeSignal) {
            value
        } else {
            return when (value) {
                "c" -> clearValue()
                "⌫" -> deleteValue(currentValue)
                "=" -> doCalculation(currentValue)
                "signal" -> invertSignal(currentValue)
                else -> addValue(currentValue, value)
            }
        }
    }

    private fun clearValue(): String {
        return weakContext.get()?.getString(R.string.screenStartValue) ?: String()
    }

    private fun deleteValue(currentValue: String): String {
        if (currentValue != "0" && currentValue.isNotBlank()) {
            val isNegativeValue = currentValue.first() == '-' && currentValue.length == 2
            val isUniqueValue = currentValue.length == 1

            val value = if (isUniqueValue || isNegativeValue) {
                clearValue()
            } else {
                currentValue.dropLast(2.takeIf { currentValue.last() == ' ' } ?: 1)
            }

            if (value.isEmpty()) {
                return clearValue()
            }

            return value
        }

        return clearValue()
    }

    private fun addValue(currentValue: String, value: String): String {
        if (value == ",") {
            if (currentValue.isBlank()) {
                return clearValue()
            } else if (currentValue.last() == ',') {
                return currentValue
            }
        }

        return if (value.isNumeric()) {
            currentValue.plus(value)
        } else {
            currentValue.trimValue().let { _currentValue ->
                if (!_currentValue.last().isNumeric()) {
                    _currentValue.dropLast(1).plus(" $value ")
                } else if (value != "," && _currentValue.hasOperator()) {
                    if (value == "%") {
                        calculateValue(_currentValue, true)
                    } else {
                        calculateValue(_currentValue).plus(" $value ")
                    }
                } else {
                    _currentValue.plus(" $value ")
                }
            }
        }
    }

    private fun doCalculation(currentValue: String): String {
        if (currentValue.trimValue().isBlank()) {
            return String()
        }

        val lastChar = currentValue.trimValue().last()

        if (!lastChar.isNumeric() && lastChar != '%') {
            return currentValue
        }

        return calculateValue(currentValue.trimValue())
    }

    private fun calculateValue(currentValue: String, isPercentage: Boolean = false): String {
        if (currentValue.length == 1) {
            return currentValue
        }

        val value = currentValue.getValidValue()

        val operator = value.getOperator()

        if (operator.isNullOrBlank()) {
            return currentValue
        }

        val sequences = value.getSequences(operator)

        if (sequences.size < 2) {
            return currentValue
        }

        val firstNumber = sequences[0].convertDouble()
        var secondNumber = sequences[1].convertDouble()

        if (isPercentage) {
            secondNumber = (secondNumber / 100) * firstNumber
        }

        return doOperation(firstNumber, secondNumber, operator)
            .addSeparatorToOperation()
            .capDecimals()
    }

    private fun invertSignal(currentValue: String): String {
        if (currentValue.isBlank() || currentValue == "0") {
            return String()
        }

        return if (currentValue.first() == '-') {
            currentValue.drop(1)
        } else {
            "-".plus(currentValue)
        }
    }

    private fun doOperation(firstNumber: Double, secondNumber: Double, charOperator: String): String {
        runCatching {
            val operation = when (charOperator) {
                "+" -> firstNumber + secondNumber
                "-" -> firstNumber - secondNumber
                "/" -> firstNumber / secondNumber
                "*" -> firstNumber * secondNumber
                "%" -> firstNumber percentOf secondNumber
                else -> throw IllegalArgumentException("Not supported")
            }

            val intValue = operation.toInt()

            if (intValue.toDouble() == operation) {
                return operation.toInt().toString()
            }

            return@runCatching operation.toString()
        }.onSuccess {
            return it
        }

        return ""
    }

    private fun String.isNumeric(): Boolean {
        kotlin.runCatching { Integer.parseInt(this) }.onSuccess {
            return true
        }

        return false
    }

    private fun Char.isNumeric(): Boolean {
        return this.toString().isNumeric()
    }

    private fun String?.convertDouble(): Double {
        if (!this.isNullOrBlank()) {
            runCatching { this.toDouble() }.onSuccess {
                return it
            }
        }

        return 0.toDouble()
    }

    private fun String?.getOperator(): String? {
        return this?.removeSeparator()?.map { return@map it.toString() }?.lastOrNull {
            !it.isNumeric()
        }
    }

    private fun String.removeSeparator(): String {
        return this.replace(".", "").replace(",", "")
    }

    private fun String.removeSignal(): String {
        return if (this.first() == '-') {
            this.drop(1)
        } else {
            this
        }
    }

    private fun String.changeSeparatorToOperation(): String {
        return this.replace(".", "").replace(",", ".")
    }

    private fun String.addSeparatorToOperation(): String {
        return this.replace(".", ",")
    }

    private fun String.hasOperator(): Boolean {
        val value = this.removeSeparator()
            .removeSignal()
            .map { return@map it.toString() }
            .lastOrNull { !it.isNumeric() }

        return !value.isNullOrBlank()
    }

    private fun String.getSequences(operator: String): List<String> {
        val hasNegativeSequence = this.first() == '-'

        val sequences = if (hasNegativeSequence) {
            this.drop(1).split(operator)
        } else {
            this.split(operator)
        }

        if (sequences.size < 2) {
            return listOf()
        }

        if (hasNegativeSequence) {
            return listOf("-".plus(sequences[0]), sequences[1])
        }

        return sequences
    }

    private infix fun Double.percentOf(divideTo: Double): Double {
        if (divideTo == 0.0) {
            return 0.0
        }

        return (divideTo / 100) * this
    }

    private fun String.capDecimals(): String {
        val containsSeparator = this.contains(",")

        if (containsSeparator) {
            val value = this.substringBefore(",")
            val decimals = this.substringAfter(",")

            if (decimals.length > 4) {
                return "$value," + decimals.substring(0, 3)
            }
        }

        return this
    }

    private fun String?.trimValue(): String {
        return (this ?: String()).replace("\\s".toRegex(), "")
    }

    private fun String?.getValidValue(): String {
        if (this.isNullOrBlank()) {
            return String()
        }

        val value = if (this.last() == ',') {
            this.plus("0")
        } else {
            this
        }

        return value.changeSeparatorToOperation().trimValue()
    }
}
