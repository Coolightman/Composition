package com.coolightman.composition.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.coolightman.composition.R
import com.coolightman.composition.databinding.FragmentResultBinding
import com.coolightman.composition.domain.entity.GameResult

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var gameResult: GameResult

    private val args by navArgs<ResultFragmentArgs>()

    companion object {
        private const val PERCENTS = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.gameResult = args.gameResult
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewValues()
        listeners()
    }

    private fun listeners() {
        with(binding) {
            btAgain.setOnClickListener {
                retryGame()
            }
        }
    }

    private fun retryGame() {
        findNavController().popBackStack()
    }

    private fun setViewValues() {
        val settings = gameResult.gameSettings
        val winner = gameResult.winner
        val rightAnswers = gameResult.countOfRightAnswers
        val questions = gameResult.countOfQuestions
        val minRightAnswers = settings.minCountOfRightAnswers
        val minRightPercent = settings.minPercentOfRightAnswers
        val rightAnswersPercent = ((rightAnswers / questions.toDouble()) * PERCENTS).toInt()

        val countedAnswersText =
            String.format(getString(R.string.tv_result_counted_answers), rightAnswers)
        val countedPercentText =
            String.format(getString(R.string.tv_result_counted_percent), rightAnswersPercent)
        val neededAnswersText =
            String.format(getString(R.string.tv_result_needed_answers), minRightAnswers)
        val neededPercentText =
            String.format(getString(R.string.tv_result_needed_percent), minRightPercent)

        setEmoji(winner)
        with(binding) {
            tvResultCountedAnswers.text = countedAnswersText
            tvResultCountedPercent.text = countedPercentText
            tvResultNeededAnswers.text = neededAnswersText
            tvResultNeededPercent.text = neededPercentText

            if (rightAnswers >= minRightAnswers) {
                tvResultCountedAnswers.setTextColor(chooseColor(true))
            } else {
                tvResultCountedAnswers.setTextColor(chooseColor(false))
            }

            if (rightAnswersPercent >= minRightPercent) {
                tvResultCountedPercent.setTextColor(chooseColor(true))
            } else {
                tvResultCountedPercent.setTextColor(chooseColor(false))
            }
        }
    }

    private fun chooseColor(goodState: Boolean): Int {
        val colorId = if (goodState) R.color.green
        else R.color.red
        return ContextCompat.getColor(requireContext(), colorId)
    }

    private fun setEmoji(winner: Boolean) {
        if (winner) {
            binding.imgResult.setImageResource(R.drawable.nerd_emoji)
        } else {
            binding.imgResult.setImageResource(R.drawable.confused_emoji)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}