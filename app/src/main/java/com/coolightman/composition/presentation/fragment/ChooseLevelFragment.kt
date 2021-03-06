package com.coolightman.composition.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.coolightman.composition.databinding.FragmentChooseLevelBinding
import com.coolightman.composition.domain.entity.Level


class ChooseLevelFragment : Fragment() {

    private var _binding: FragmentChooseLevelBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChooseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeners()
    }

    private fun listeners() {
        with(binding) {
            btLevelTest.setOnClickListener {
                val level = Level.TEST
                launchGameFragment(level)
            }

            btLevelEasy.setOnClickListener {
                val level = Level.EASY
                launchGameFragment(level)
            }

            btLevelNormal.setOnClickListener {
                val level = Level.NORMAL
                launchGameFragment(level)
            }

            btLevelHard.setOnClickListener {
                val level = Level.HARD
                launchGameFragment(level)
            }
        }
    }

    private fun launchGameFragment(level: Level) {
        findNavController().navigate(
            ChooseLevelFragmentDirections.actionChooseLevelFragmentToGameFragment(level)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}