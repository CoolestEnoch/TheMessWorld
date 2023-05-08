package foss.coolest.fucker.ui

import android.content.ComponentName
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import foss.coolest.fucker.R
import foss.coolest.fucker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when (isActive()) {
            true -> {
                binding.activeStatus.text = "模块已激活"
                binding.titleTip.text = "资本or自由？  自由!\uD83C\uDFC1"
            }
            false -> {
                binding.titleCard.setBackgroundColor(resources.getColor(R.color.lightRed,resources.newTheme()))
                binding.activeStatus.text = "模块没激活"
                binding.titleTip.text = "资本or自由？  资本!\uD83D\uDC94"
            }
        }

        binding.openPangGuaiShengHuo.setOnClickListener {
            startActivity(Intent().apply {
                action = "com.qiekj.user"
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
                component =
                    ComponentName("com.qiekj.user", "com.qiekj.user.MainActivity")
            })
        }


    }

    private fun isActive() = false
}