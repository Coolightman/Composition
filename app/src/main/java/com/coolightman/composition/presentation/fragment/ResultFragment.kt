package com.coolightman.composition.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.coolightman.composition.R
import com.coolightman.composition.databinding.FragmentResultBinding
import com.coolightman.composition.domain.entity.GameResult

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var gameResult: GameResult

    companion object {
        const val NAME = "ResultFragment"
        private const val ARG_RESULT = "result"
        private const val PERCENTS = 100

        fun newInstance(result: GameResult): ResultFragment {
            return ResultFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_RESULT, result)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    private fun parseArgs() {
        requireArguments().getParcelable<GameResult>(ARG_RESULT)?.let {
            gameResult = it
        }
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
        setOnBackPressed()
    }

    private fun setOnBackPressed() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                retryGame()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun listeners() {
        with(binding) {
            btAgain.setOnClickListener {
                retryGame()
            }
        }
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(GameFragment.NAME, 1)
    }

    private fun setViewValues() {
        val settings = gameResult.gameSettings
        val winner = gameResult.winner
        val rightAnswers = gameResult.countOfRightAnswers
        val questions = gameResult.countOfQuestions
        val minRightAnswers = settings.minCountOfRightAnswers
        val minRightPercent = settings.minPercentOfRightAnswers
        val percent = ((rightAnswers / questions.toDouble()) * PERCENTS).toInt()

        val countedAnswersText =
            String.format(resources.getString(R.string.tv_result_counted_answers), rightAnswers)
        val countedPercentText =
            String.format(resources.getString(R.string.tv_result_counted_percent), percent)
        val neededAnswersText =
            String.format(resources.getString(R.string.tv_result_needed_answers), minRightAnswers)
        val neededPercentText =
            String.format(resources.getString(R.string.tv_result_needed_percent), minRightPercent)

        setEmoji(winner)
        with(binding) {
            tvResultCountedAnswers.text = countedAnswersText
            tvResultCountedPercent.text = countedPercentText
            tvResultNeededAnswers.text = neededAnswersText
            tvResultNeededPercent.text = neededPercentText
        }
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