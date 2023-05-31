package you.thiago.calculadora.ui.components.dashboard

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import you.thiago.calculadora.R
import you.thiago.calculadora.ui.components.CalculadoraButton
import you.thiago.calculadora.ui.components.CalculadoraDefaultScreen

open class CalculadoraDashboard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val buttonList = mutableListOf<CalculadoraButton?>()

    protected fun setupButtons() {
        buttonList.add(findViewById(R.id.btn_0))
        buttonList.add(findViewById(R.id.btn_1))
        buttonList.add(findViewById(R.id.btn_2))
        buttonList.add(findViewById(R.id.btn_3))
        buttonList.add(findViewById(R.id.btn_4))
        buttonList.add(findViewById(R.id.btn_5))
        buttonList.add(findViewById(R.id.btn_6))
        buttonList.add(findViewById(R.id.btn_7))
        buttonList.add(findViewById(R.id.btn_8))
        buttonList.add(findViewById(R.id.btn_9))
        buttonList.add(findViewById(R.id.btn_minus))
        buttonList.add(findViewById(R.id.btn_adition))
        buttonList.add(findViewById(R.id.btn_multiply))
        buttonList.add(findViewById(R.id.btn_divide))
        buttonList.add(findViewById(R.id.btn_percent))
        buttonList.add(findViewById(R.id.btn_clear))
        buttonList.add(findViewById(R.id.btn_signal))
        buttonList.add(findViewById(R.id.btn_delete))
        buttonList.add(findViewById(R.id.btn_equals))
        buttonList.add(findViewById(R.id.btn_divide))
        buttonList.add(findViewById(R.id.btn_separator))
    }

    open fun setupDashboardScreen(screen: CalculadoraDefaultScreen) {
        buttonList.filterNotNull().forEach { button ->
            button.setOnClickListener {
                screen.setButtonValue(button.value)
            }
        }
    }
}
