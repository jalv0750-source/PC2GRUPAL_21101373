package com.example.a21101373_juanalvarado_pc2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.a21101373_juanalvarado_pc2.data.local.AsteroidEntity
import com.example.a21101373_juanalvarado_pc2.data.repository.AsteroidRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AsteroidUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val asteroids: List<AsteroidEntity> = emptyList(),
    val favorites: List<AsteroidEntity> = emptyList(),
    val filterOnlyHazardous: Boolean = false
)

class AsteroidViewModel(
    private val repository: AsteroidRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AsteroidUiState())
    val uiState: StateFlow<AsteroidUiState> = _uiState.asStateFlow()

    init {
        observeData()
        refreshAsteroids()
    }

    private fun observeData() {
        viewModelScope.launch {
            repository.allAsteroids.collect { list ->
                _uiState.update { currentState ->
                    currentState.copy(asteroids = list)
                }
            }
        }

        viewModelScope.launch {
            repository.favoriteAsteroids.collect { favList ->
                _uiState.update { currentState ->
                    currentState.copy(favorites = favList)
                }
            }
        }
    }

    fun refreshAsteroids() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = repository.fetchAsteroidsFromApi()
            result.onFailure { error ->
                val isOffline = error is java.net.UnknownHostException ||
                        error is java.net.ConnectException ||
                        error is java.net.SocketTimeoutException ||
                        error.cause is java.net.UnknownHostException ||
                        error.cause is java.net.ConnectException

                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        errorMessage = if (isOffline) "Sin conexión a internet. Mostrando contenido almacenado localmente." else null
                    )
                }
            }.onSuccess {
                _uiState.update { currentState ->
                    currentState.copy(isLoading = false, errorMessage = null)
                }
            }
        }
    }

    fun setFilterOnlyHazardous(onlyHazardous: Boolean) {
        _uiState.update { it.copy(filterOnlyHazardous = onlyHazardous) }
    }

    fun toggleFavorite(id: String) {
        viewModelScope.launch {
            val currentAsteroid = _uiState.value.asteroids.find { it.id == id }
                ?: _uiState.value.favorites.find { it.id == id }
            val currentFavoriteStatus = currentAsteroid?.isFavorite ?: false
            repository.updateFavoriteStatus(id, !currentFavoriteStatus)
        }
    }
}

class AsteroidViewModelFactory(
    private val repository: AsteroidRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AsteroidViewModel::class.java)) {
            return AsteroidViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
