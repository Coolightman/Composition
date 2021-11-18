package com.coolightman.composition.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.coolightman.composition.R
import com.coolightman.composition.databinding.FragmentGameBinding
import com.coolightman.composition.domain.entity.GameResult
import com.coolightman.composition.domain.entity.GameSettings
import com.coolightman.composition.domain.entity.Level

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private lateinit var level: Level

    companion object {
        const val NAME = "GameFragment"
        private const val ARG_LEVEL = "level"

        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_LEVEL, level)
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
        listeners()
    }

    private fun listeners() {
        with(binding) {
            tvGameOption1.setOnClickListener {
                showResult()
            }
            tvGameOption2.setOnClickListener {

            }
            tvGameOption3.setOnClickListener {

            }
            tvGameOption4.setOnClickListener {

            }
            tvGameOption5.setOnClickListener {

            }
            tvGameOption6.setOnClickListener {

            }
        }
    }

    private fun showResult() {
        val settings = GameSettings(10, 15, 80, 60)
        val result = GameResult(false, 20, 23, settings)
        launchResultFragment(result)
    }

    private fun launchResultFragment(result: GameResult) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, ResultFragment.newInstance(result))
            .addToBackStack(null)
            .commit()
    }

    private fun parseArgs() {
        level = requireArguments().getSerializable(ARG_LEVEL) as Level
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}