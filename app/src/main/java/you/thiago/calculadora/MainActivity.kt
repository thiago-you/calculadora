package you.thiago.calculadora

import android.content.res.Configuration
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import you.thiago.calculadora.databinding.ActivityMainBinding
import you.thiago.calculadora.enums.ThemeState

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        setupButtonList()
        setupScreenEvents()
    }

    private fun setupScreenEvents() {
        binding.screen.setupDashboardSwitch {
            binding.dashboardContent.showNext()

            if (binding.dashboardBasic.isVisible) {
                binding.screen.setTitle(R.string.basic_dashboard)
            } else {
                binding.screen.setTitle(R.string.advanced_dashboard)
            }
        }

        binding.screen.setupThemeSwitch(getThemeState()).observe(this) { theme ->
            val uiTheme = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

            when {
                theme == ThemeState.LIGHT && uiTheme == Configuration.UI_MODE_NIGHT_NO -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                theme == ThemeState.DARK && uiTheme == Configuration.UI_MODE_NIGHT_YES -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }

    private fun setupButtonList() {
        binding.dashboardBasic.setupDashboardScreen(binding.screen)
        binding.dashboardAdvanced.setupDashboardScreen(binding.screen)
    }

    private fun getThemeState(): ThemeState {
        return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_NO -> {
                ThemeState.DARK
            }
            else -> {
                ThemeState.LIGHT
            }
        }
    }
}
