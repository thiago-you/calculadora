package you.thiago.calculadora.ui

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import you.thiago.calculadora.R

class CalculadoraButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    var value: String = ""

    companion object {
        private const val DEFAULT_DIMENSION = 22
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.button, this)

        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CalculadoraButton,
            defStyleAttr,
            defStyleRes,
        )

        val textView = findViewById<TextView>(R.id.calculadora_button)

        typedArray.getString(R.styleable.CalculadoraButton_value)?.also {
            textView?.text = it
        }

        typedArray.getInt(R.styleable.CalculadoraButton_btnSize, DEFAULT_DIMENSION).also {
            textView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, it.toFloat())
        }

        typedArray.getColor(R.styleable.CalculadoraButton_btnColor, context.getColor(R.color.white)).also {
            textView?.setTextColor(it)
        }

        typedArray.getDrawable(R.styleable.CalculadoraButton_btnBackground)?.also {
            textView?.background = it
        }

        typedArray.recycle()

        convertStringIntoValue()
    }

    private fun convertStringIntoValue() {
        val textValue = findViewById<TextView>(R.id.calculadora_button)?.text.toString().lowercase()

        if (textValue.isBlank() || textValue == "null") {
            return
        }

        value = when (textValue) {
            "x" -> "*"
            "+/-" -> "signal"
            else -> textValue
        }
    }
}
