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

        if (currentValue == "0") {
            textViewScreen?.text = value
        } else {
            textViewScreen?.text = currentValue.plus(value)
        }
    }
}
