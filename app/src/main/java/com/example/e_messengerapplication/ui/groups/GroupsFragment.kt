package com.example.e_messengerapplication.ui.groups

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_messengerapplication.R
import com.example.e_messengerapplication.databinding.FragmentGroupsBinding
import com.example.e_messengerapplication.ui.MainActivity

class GroupsFragment : Fragment() {
    private var _binding: FragmentGroupsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentGroupsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).setSelectedBottomMenuItem(R.id.groupsFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}