package you.thiago.calculadora.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import you.thiago.calculadora.R

class CalculadoraDefaultScreen @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private var textViewScreen: TextView? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.default_screen, this)

        textViewScreen = findViewById(R.id.tv_screen)
    }

    fun setButtonValue(value: String) {
        val currentValue = textViewScreen?.text.toString()

        if (currentValue == "0" && value.isNumeric() || (currentValue == "0" && value == "-")) {
            textViewScreen?.text = value
        } else {
            when (value) {
                "c" -> clearValue()
                "⌫" -> deleteValue(currentValue)
                "=" -> calculateValue(currentValue)
                "signal" -> invertSignal(currentValue)
                else -> addValue(currentValue, value)
            }
        }
    }

    private fun clearValue() {
        textViewScreen?.text = context.getString(R.string.screenStartValue)
    }

    private fun deleteValue(currentValue: String) {
        if (currentValue != "0") {
            val isNegativeValue = currentValue.first() == '-' && currentValue.length == 2
            val isUniqueValue = currentValue.length == 1

            if (isUniqueValue || isNegativeValue) {
                textViewScreen?.text = context.getString(R.string.screenStartValue)
            } else {
                textViewScreen?.text = currentValue.dropLast(1)
            }
        }
    }

    private fun addValue(currentValue: String, value: String) {
        if (value == "," && currentValue.last() == ',') {
            return
        }

        if (value.isNumeric()) {
            textViewScreen?.text = currentValue.plus(value)
        } else {
            if (!currentValue.last().isNumeric()) {
                textViewScreen?.text = textViewScreen?.text.toString().dropLast(1).plus(value)
            } else if (value != "," && currentValue.hasOperator()) {
                textViewScreen?.text = calculateValue(currentValue).plus(value)
            } else {
                textViewScreen?.text = textViewScreen?.text.toString().plus(value)
            }
        }
    }

    private fun calculateValue(currentValue: String): String {
        if (currentValue.length == 1) {
            return currentValue
        }

        val value = if (currentValue.last() == ',') {
            currentValue.plus("0").changeSeparatorToOperation()
        } else {
            currentValue.changeSeparatorToOperation()
        }

        val operator = value.getOperator()

        if (operator.isNullOrBlank()) {
            return currentValue
        }

        val sequences = value.getSequences(operator)

        if (sequences.size < 2) {
            return currentValue
        }

        val firstNumber = sequences[0].convertDouble()
        val secondNumber = sequences[1].convertDouble()

        val result = doOperation(firstNumber, secondNumber, operator).addSeparatorToOperation()

        textViewScreen?.text = result

        return result
    }

    private fun invertSignal(currentValue: String) {
        if (currentValue.isBlank() || currentValue == "0") {
            return
        }

        if (currentValue.first() == '-') {
            textViewScreen?.text = currentValue.drop(1)
        } else {
            textViewScreen?.text = "-".plus(currentValue)
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
}
