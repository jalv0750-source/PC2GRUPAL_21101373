package com.example.a21101373_juanalvarado_pc2.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.a21101373_juanalvarado_pc2.data.local.ApodEntity
import com.example.a21101373_juanalvarado_pc2.data.repository.ApodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ApodUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val apods: List<ApodEntity> = emptyList(),
    val favorites: List<ApodEntity> = emptyList()
)

class ApodViewModel(
    private val repository: ApodRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ApodUiState())
    val uiState: StateFlow<ApodUiState> = _uiState.asStateFlow()

    init {
        observeData()
        refreshApods()
    }

    private fun observeData() {
        viewModelScope.launch {
            repository.allApods.collect { apodList ->
                _uiState.update { currentState ->
                    currentState.copy(apods = apodList)
                }
            }
        }

        viewModelScope.launch {
            repository.favoriteApods.collect { favList ->
                _uiState.update { currentState ->
                    currentState.copy(favorites = favList)
                }
            }
        }
    }

    fun refreshApods() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = repository.fetchApodsFromApi()
            result.onFailure { error ->
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        errorMessage = "Error de conexión/red. Mostrando datos guardados localmente."
                    )
                }
            }.onSuccess {
                _uiState.update { currentState ->
                    currentState.copy(isLoading = false, errorMessage = null)
                }
            }
        }
    }

    fun toggleFavorite(date: String) {
        viewModelScope.launch {
            val currentApod = _uiState.value.apods.find { it.date == date }
                ?: _uiState.value.favorites.find { it.date == date }
            val currentFavoriteStatus = currentApod?.isFavorite ?: false
            repository.updateFavoriteStatus(date, !currentFavoriteStatus)
        }
    }
}

class ApodViewModelFactory(
    private val repository: ApodRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ApodViewModel::class.java)) {
            return ApodViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
