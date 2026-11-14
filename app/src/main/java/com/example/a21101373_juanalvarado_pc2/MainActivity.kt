package com.example.a21101373_juanalvarado_pc2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.a21101373_juanalvarado_pc2.data.local.AppDatabase
import com.example.a21101373_juanalvarado_pc2.data.remote.RetrofitClient
import com.example.a21101373_juanalvarado_pc2.data.repository.ApodRepository
import com.example.a21101373_juanalvarado_pc2.ui.main.MainScreen
import com.example.a21101373_juanalvarado_pc2.ui.theme._21101373_JuanAlvarado_PC2Theme
import com.example.a21101373_juanalvarado_pc2.ui.viewmodel.ApodViewModel
import com.example.a21101373_juanalvarado_pc2.ui.viewmodel.ApodViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = AppDatabase.getDatabase(applicationContext)
        val repository = ApodRepository(
            apodDao = database.apodDao(),
            nasaApiService = RetrofitClient.nasaApiService
        )

        setContent {
            _21101373_JuanAlvarado_PC2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val apodViewModel: ApodViewModel = viewModel(
                        factory = ApodViewModelFactory(repository)
                    )
                    MainScreen(
                        apodViewModel = apodViewModel
                    )
                }
            }
        }
    }
}
