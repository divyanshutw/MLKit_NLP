package com.practice.mlkitnlp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.practice.mlkitnlp.databinding.FragmentChooserBinding

class ChooserFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding= DataBindingUtil.inflate<FragmentChooserBinding>(layoutInflater,R.layout.fragment_chooser,container,false)

        val list=ArrayList<String>();
        list.add("Language Identifier")
        list.add("Language Translation")
        list.add("Smart Reply")

        val arrayAdapter=
            ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item,list)
        binding.listView.adapter=arrayAdapter

        binding.listView.setOnItemClickListener { parent, view, position, id ->
            when(position) {
                0 -> view?.findNavController()
                    ?.navigate(R.id.action_chooserFragment_to_languageIdentifierFragment)
                1 -> view?.findNavController()
                        ?.navigate(R.id.action_chooserFragment_to_translationFragment)
                2-> view?.findNavController()
                    ?.navigate(R.id.action_chooserFragment_to_smartReplyFragment)
            }

        }

        return binding.root
    }
}