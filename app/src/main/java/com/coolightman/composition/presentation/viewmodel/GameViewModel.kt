package com.coolightman.composition.presentation.viewmodel

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.coolightman.composition.R
import com.coolightman.composition.data.GameRepositoryImpl
import com.coolightman.composition.domain.entity.GameResult
import com.coolightman.composition.domain.entity.GameSettings
import com.coolightman.composition.domain.entity.Level
import com.coolightman.composition.domain.entity.Question
import com.coolightman.composition.domain.usecase.GenerateQuestionUseCase
import com.coolightman.composition.domain.usecase.GetGameSettingsUseCase

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var gameSettings: GameSettings
    private lateinit var level: Level
    private lateinit var timer: CountDownTimer

    private val context = application
    private val repository = GameRepositoryImpl
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)

    private var countOfRightAnswers = 0
    private var countOfQuestions = 0

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTE = 60
        private const val PERCENTS = 100
    }

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _enoughCount = MutableLiveData<Boolean>()
    val enoughCount: LiveData<Boolean>
        get() = _enoughCount

    private val _enoughPercent = MutableLiveData<Boolean>()
    val enoughPercent: LiveData<Boolean>
        get() = _enoughPercent

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private val _toast = MutableLiveData<String>()
    val toast: LiveData<String>
        get() = _toast

    fun startGame(level: Level) {
        getGameSettings(level)
        startTimer()
        generateQuestion()
        updateProgress()
    }

    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        updateProgress()
        generateQuestion()
    }

    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()
        _percentOfRightAnswers.value = percent
        val baseProgressString = context.resources.getString(R.string.tv_progress_bar_game)

        _progressAnswers.value =
            String.format(
                baseProgressString,
                countOfRightAnswers,
                gameSettings.minCountOfRightAnswers
            )

        _enoughCount.value =
            countOfRightAnswers >= gameSettings.minCountOfRightAnswers

        _enoughPercent.value =
            percent >= gameSettings.minPercentOfRightAnswers

    }

    private fun calculatePercentOfRightAnswers(): Int {
        val result = countOfRightAnswers / countOfQuestions.toDouble()
        val percent = result * PERCENTS
        return percent.toInt()
    }

    private fun checkAnswer(number: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (number == rightAnswer) {
            countOfRightAnswers++
            _toast.value = context.getString(R.string.toast_right)
        } else {
            _toast.value = context.getString(R.string.toast_wrong)
        }
        countOfQuestions++
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        this.gameSettings = getGameSettingsUseCase(level)
        _minPercent.value = gameSettings.minPercentOfRightAnswers
    }

    private fun startTimer() {
        this.timer = object : CountDownTimer(
            gameSettings.gameTimeInSeconds * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.value = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer.start()
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSettings.maxSumValue)
    }

    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTE
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTE)
        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    private fun finishGame() {
        _gameResult.value = getGameResult()
    }

    private fun getGameResult(): GameResult {
        val winner = enoughCount.value == true &&
                enoughPercent.value == true
        return GameResult(winner, countOfRightAnswers, countOfQuestions, gameSettings)
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}