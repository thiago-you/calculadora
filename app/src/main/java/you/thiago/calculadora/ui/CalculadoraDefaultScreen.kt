package you.thiago.calculadora.ui

import android.content.Context
import android.os.SystemClock
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import you.thiago.calculadora.R
import you.thiago.calculadora.components.Calculadora
import you.thiago.calculadora.components.CalculadoraBasic
import you.thiago.calculadora.enums.ThemeState

class CalculadoraDefaultScreen @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val calculadora: Calculadora by lazy { CalculadoraBasic(context) }
    private val textViewScreen: TextView? by lazy { findViewById(R.id.tv_screen) }

    companion object {
        private var lastTimeClicked: Long = 0
        private var defaultInterval: Long = 300
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.screen_default, this)
    }

    fun setTitle(@StringRes title: Int) {
        findViewById<TextView>(R.id.tv_screen_title)?.setText(title)
    }

    fun setupDashboardSwitch(callback: () -> Unit) {
        findViewById<ImageView>(R.id.swicth_dashboard)?.setOnClickListener {
            callback()
        }

        findViewById<TextView>(R.id.tv_screen_title)?.setOnClickListener {
            callback()
        }
    }

    fun setupThemeSwitch(selectedTheme: ThemeState): LiveData<ThemeState?> {
        val data = MutableLiveData(selectedTheme)

        val imageView = findViewById<ImageView>(R.id.theme_switch)

        if (data.value == ThemeState.LIGHT) {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_wb_sunny_24))
        } else {
            imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.baseline_mode_night_24))
        }

        imageView?.onSafeClickListener {
            data.postValue(data.value?.switchTheme())
        }

        return data
    }

    fun setButtonValue(value: String) {
        textViewScreen?.text = calculadora.operateValue(textViewScreen?.text.toString(), value)
    }

    private fun View.onSafeClickListener(onClickListener: () -> Unit) {
        this.setOnClickListener {
            if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
                return@setOnClickListener
            }

            lastTimeClicked = SystemClock.elapsedRealtime()

            onClickListener()
        }
    }
}
