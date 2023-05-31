package you.thiago.calculadora.ui.dashboard

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import you.thiago.calculadora.R

class CalculadoraAdvancedDashboard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : CalculadoraDashboard(context, attrs, defStyleAttr, defStyleRes) {

    init {
        LayoutInflater.from(context).inflate(R.layout.advanced_dashboard, this)
        setupButtons()
    }
}
