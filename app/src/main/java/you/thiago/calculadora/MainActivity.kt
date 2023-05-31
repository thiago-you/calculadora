package you.thiago.calculadora

import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import you.thiago.calculadora.databinding.ActivityMainBinding
import you.thiago.calculadora.enums.ThemeState

class MainActivity : AppCompatActivity() {

    companion object {
        private const val SELECTED_THEME = "selected_theme"
    }

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    private var selectedTheme = ThemeState.EMPTY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        savedInstanceState.restoreTheme()

        setupButtonList()
        setupTheme()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.storeTheme()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.storeTheme()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        savedInstanceState.restoreTheme()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedInstanceState.restoreTheme()
    }

    private fun setupTheme() {
        binding.screen.setupThemeSwitch(selectedTheme).observe(this) { theme ->
            if (theme != null && selectedTheme != theme) {
                selectedTheme = theme
                configTheme(theme)
            }
        }
    }

    private fun setupButtonList() {
        binding.dashboard.setupDashboardScreen(binding.screen)
    }

    private fun configTheme(theme: ThemeState) {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                if (theme == ThemeState.DARK) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
            Configuration.UI_MODE_NIGHT_YES -> {
                if (theme == ThemeState.LIGHT) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }

    private fun Bundle?.storeTheme() {
        val value = when (selectedTheme) {
            ThemeState.LIGHT -> 1
            else -> 0
        }

        this?.putInt(SELECTED_THEME, value)
    }

    private fun Bundle?.restoreTheme() {
        this?.getInt(SELECTED_THEME)?.also {
            selectedTheme = when (it) {
                1 -> ThemeState.LIGHT
                else -> ThemeState.DARK
            }
        }
    }
}
