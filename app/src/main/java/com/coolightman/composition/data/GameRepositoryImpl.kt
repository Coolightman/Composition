package com.coolightman.composition.data

import android.util.Log
import com.coolightman.composition.domain.entity.GameSettings
import com.coolightman.composition.domain.entity.Level
import com.coolightman.composition.domain.entity.Question
import com.coolightman.composition.domain.repository.GameRepository
import kotlin.random.Random

object GameRepositoryImpl : GameRepository {

    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER_VALUE = 1
    private const val OPTIONS_DISPERSION = 10

    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        val sum = Random.nextInt(MIN_SUM_VALUE, maxSumValue + 1)
        val visibleNumber = Random.nextInt(MIN_ANSWER_VALUE, sum)
        val rightAnswer = sum - visibleNumber
        val optionsList = getOptions(rightAnswer, countOfOptions)

        val question = Question(sum, visibleNumber, optionsList)
        Log.i("question", question.toString())

        return question
    }

    private fun getOptions(rightAnswer: Int, countOfOptions: Int): List<Int> {
        val options = HashSet<Int>()
        options.add(rightAnswer)
        val from = rightAnswer - OPTIONS_DISPERSION
        val to = rightAnswer + OPTIONS_DISPERSION
        while (options.size < countOfOptions) {
            val option = Random.nextInt(from, to + 1)
            options.add(option)
        }
        return options.toList()
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when (level) {
            Level.TEST -> GameSettings(10, 3, 50, 8)
            Level.EASY -> GameSettings(10, 10, 70, 60)
            Level.NORMAL -> GameSettings(20, 20, 80, 50)
            Level.HARD -> GameSettings(30, 30, 90, 40)
        }
    }

}