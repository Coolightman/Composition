package com.coolightman.composition.presentation.fragment

import android.content.res.ColorStateList
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import com.coolightman.composition.R
import com.coolightman.composition.databinding.FragmentGameBinding
import com.coolightman.composition.domain.entity.GameResult
import com.coolightman.composition.domain.entity.Level
import com.coolightman.composition.presentation.viewmodel.GameViewModel

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(
            this,
            AndroidViewModelFactory.getInstance(requireActivity().application)
        )[GameViewModel::class.java]
    }
    private lateinit var level: Level
    private lateinit var options: List<Int>
    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            with(binding) {
                add(tvGameOption1)
                add(tvGameOption2)
                add(tvGameOption3)
                add(tvGameOption4)
                add(tvGameOption5)
                add(tvGameOption6)
            }
        }
    }

    companion object {
        const val NAME = "GameFragment"
        private const val ARG_LEVEL = "level"

        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_LEVEL, level)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observers()
        optionsListener()
        viewModel.startGame(level)
    }

    private fun observers() {
        observeTimer()
        observeQuestion()
        observeProgress()
        observeToast()
        observeGameResult()
    }

    private fun observeGameResult() {
        viewModel.gameResult.observe(viewLifecycleOwner) {
            launchResultFragment(it)
        }
    }

    private fun observeProgress() {
        viewModel.progressAnswers.observe(viewLifecycleOwner) {
            binding.tvProgressBar.text = it
        }

        viewModel.minPercent.observe(viewLifecycleOwner) {
            binding.progressBar.secondaryProgress = it
        }

        viewModel.percentOfRightAnswers.observe(viewLifecycleOwner) {
            binding.progressBar.setProgress(it, true)
        }

        viewModel.enoughCount.observe(viewLifecycleOwner) {
            binding.tvProgressBar.setTextColor(chooseColor(it))
        }

        viewModel.enoughPercent.observe(viewLifecycleOwner) {
            binding.progressBar.progressTintList = ColorStateList.valueOf(chooseColor(it))
        }
    }

    private fun chooseColor(goodState: Boolean): Int {
        val colorId = if (goodState) R.color.green
        else R.color.red
        return ContextCompat.getColor(requireContext(), colorId)
    }

    private fun observeToast() {
        viewModel.toast.observe(viewLifecycleOwner) {
            val toast = Toast.makeText(requireActivity(), it, Toast.LENGTH_SHORT)
            toast.show()
            Handler(Looper.getMainLooper()).postDelayed({ toast.cancel() }, 500)
        }
    }

    private fun observeQuestion() {
        viewModel.question.observe(viewLifecycleOwner) {
            with(binding) {
                tvGameSum.text = it.sum.toString()
                tvGameVisibleNumber.text = it.visibleNumber.toString()
                options = it.options
                for (i in 0 until tvOptions.size) {
                    tvOptions[i].text = options[i].toString()
                }
            }
        }
    }

    private fun observeTimer() {
        viewModel.formattedTime.observe(viewLifecycleOwner) {
            binding.tvGameTimer.text = it
        }
    }

    private fun optionsListener() {
        for (i in 0 until tvOptions.size) {
            tvOptions[i].setOnClickListener {
                viewModel.chooseAnswer(options[i])
            }
        }
    }

    private fun launchResultFragment(result: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, ResultFragment.newInstance(result))
            .addToBackStack(ResultFragment.NAME)
            .commit()
    }

    private fun parseArgs() {
        requireArguments().getParcelable<Level>(ARG_LEVEL)?.let {
            level = it
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}