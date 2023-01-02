package com.luukitoo.customviewdemo

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.luukitoo.customviewdemo.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.loaderButton.setOnAsyncClickListener(lifecycleScope) {
            binding.loaderButton.inProgress = true
            delay(3000)
            Snackbar.make(binding.root, "Operation finished", Snackbar.LENGTH_SHORT).show()
            binding.loaderButton.inProgress = false
        }

    }
}