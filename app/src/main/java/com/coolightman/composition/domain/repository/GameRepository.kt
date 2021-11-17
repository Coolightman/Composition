package com.coolightman.composition.domain.repository

import com.coolightman.composition.domain.entity.GameSettings
import com.coolightman.composition.domain.entity.Level
import com.coolightman.composition.domain.entity.Question

interface GameRepository {

    fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question

    fun getGameSettings(level: Level): GameSettings
}