package com.coolightman.composition.domain.usecase

import com.coolightman.composition.domain.entity.GameSettings
import com.coolightman.composition.domain.entity.Level
import com.coolightman.composition.domain.repository.GameRepository

class GetGameSettingsUseCase(
    private val repository: GameRepository,
) {

    operator fun invoke(level: Level): GameSettings {
        return repository.getGameSettings(level)
    }
}