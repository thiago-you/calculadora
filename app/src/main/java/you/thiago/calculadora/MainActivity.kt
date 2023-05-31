package you.thiago.calculadora

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import you.thiago.calculadora.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        setupButtonList()
    }

    private fun setupButtonList() {
        binding.dashboard.setupDashboardScreen(binding.screen)
    }
}
