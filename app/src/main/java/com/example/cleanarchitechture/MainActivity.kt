package com.example.cleanarchitechture

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cleanarchitechture.data.cloud.CloudSource
import com.example.cleanarchitechture.presentation.ui.MainFragment
import org.koin.dsl.context.ModuleDefinition

class MainActivity : AppCompatActivity() {

    private lateinit var cloudSource2: CloudSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()
        }

        cloudSource2 = get()
    }
}
