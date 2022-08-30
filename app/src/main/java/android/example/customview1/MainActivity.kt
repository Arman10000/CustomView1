package android.example.customview1

import android.example.customview1.databinding.ActivityMainBinding
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomButtons.setListener {
            when(it) {
                BottomButtonAction.POSITIVE -> {

                }
                BottomButtonAction.NEGATIVE -> {

                }
            }
        }
    }
}