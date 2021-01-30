package com.practice.mlkitnlp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions
import com.google.mlkit.nl.languageid.LanguageIdentifier
import com.practice.mlkitnlp.databinding.FragmentLanguageIdentifierBinding


class LanguageIdentifierFragment : Fragment() {


    lateinit var binding:FragmentLanguageIdentifierBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= DataBindingUtil.inflate(layoutInflater,R.layout.fragment_language_identifier,container,false)

        val options=LanguageIdentificationOptions.Builder()
            .setConfidenceThreshold(0.3f)
            .build()
        val languageIdentifier=LanguageIdentification.getClient(options)

        binding.button.setOnClickListener {
            languageIdentifier.identifyPossibleLanguages(binding.editText.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        if(it.result!=null) {
                            var s=""
                            for (language in it.result!!)
                            {
                                s+=language.languageTag+"\t"+(language.confidence*100)+"%\n"
                            }
                            binding.textView.text=s
                        }
                        else
                            Log.d("div","result ${it.result}")
                    }
                    else
                        Log.d("div","Task failed ${it.exception}")
                }
        }
        return binding.root
    }


}