package you.thiago.calculadora.ui.components

import android.content.Context
import android.util.AttributeSet
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

    init {
        LayoutInflater.from(context).inflate(R.layout.button, this)

        val typedArray = context.obtainStyledAttributes(
            attrs,
            R.styleable.CalculadoraButton,
            defStyleAttr,
            defStyleRes,
        )

        typedArray.getString(R.styleable.CalculadoraButton_value)?.also {
            findViewById<TextView>(R.id.calculadora_button)?.text = it
        }

        typedArray.recycle()
    }
}
