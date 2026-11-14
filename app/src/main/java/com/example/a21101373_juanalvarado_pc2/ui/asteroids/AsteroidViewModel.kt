package com.example.a21101373_juanalvarado_pc2.ui.asteroids

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.a21101373_juanalvarado_pc2.data.local.AppDatabase
import com.example.a21101373_juanalvarado_pc2.data.local.AsteroidEntity
import com.example.a21101373_juanalvarado_pc2.data.remote.RetrofitClient
import com.example.a21101373_juanalvarado_pc2.data.repository.AsteroidRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AsteroidListUiState(
    val asteroids: List<AsteroidEntity> = emptyList(),
    val onlyHazardous: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
class AsteroidViewModel(private val repository: AsteroidRepository) : ViewModel() {

    private val onlyHazardous = MutableStateFlow(false)
    private val isLoading = MutableStateFlow(false)
    private val errorMessage = MutableStateFlow<String?>(null)

    private val asteroidsFlow: Flow<List<AsteroidEntity>> =
        onlyHazardous.flatMapLatest { repository.observeAsteroids(it) }

    val uiState: StateFlow<AsteroidListUiState> = combine(
        asteroidsFlow, onlyHazardous, isLoading, errorMessage
    ) { list, hazardous, loading, error ->
        AsteroidListUiState(list, hazardous, loading, error)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AsteroidListUiState(isLoading = true))

    val favorites: StateFlow<List<AsteroidEntity>> = repository.observeFavorites()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    init {
        refresh()
    }

    fun refresh() {
        if (isLoading.value) return
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = null
            repository.refresh().onFailure {
                errorMessage.value = "No se pudo actualizar. Revisa tu conexión y tu API key."
            }
            isLoading.value = false
        }
    }

    fun setOnlyHazardous(value: Boolean) = onlyHazardous.update { value }

    fun toggleFavorite(asteroid: AsteroidEntity) {
        viewModelScope.launch { repository.setFavorite(asteroid.id, !asteroid.isFavorite) }
    }

    fun observeAsteroid(id: String): Flow<AsteroidEntity?> = repository.observeAsteroid(id)
}

class AsteroidViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val database = AppDatabase.getDatabase(context)
        val repo = AsteroidRepository(
            api = RetrofitClient.nasaApiService,
            dao = database.asteroidDao()
        )
        return AsteroidViewModel(repo) as T
    }
}
