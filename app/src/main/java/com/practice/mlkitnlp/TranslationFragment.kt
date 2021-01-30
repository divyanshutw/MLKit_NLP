package com.practice.mlkitnlp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import com.practice.mlkitnlp.databinding.FragmentTranslationBinding


class TranslationFragment : Fragment() {

    private lateinit var binding:FragmentTranslationBinding
    private var toLang=TranslateLanguage.ENGLISH

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(layoutInflater,R.layout.fragment_translation,container,false)

        var list=ArrayList<String>()
        list.add("English")
        list.add("Hindi")
        list.add("German")
        list.add("French")
        list.add("Japanese")
        val adapter=ArrayAdapter<String>(requireContext(),R.layout.support_simple_spinner_dropdown_item,list)
        binding.spinner.adapter=adapter
        binding.spinner.onItemSelectedListener=object: OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> toLang=TranslateLanguage.ENGLISH
                    1 -> toLang=TranslateLanguage.HINDI
                    2 -> toLang=TranslateLanguage.GERMAN
                    3 -> toLang=TranslateLanguage.FRENCH
                    4 -> toLang=TranslateLanguage.JAPANESE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                toLang=TranslateLanguage.ENGLISH
            }

        }

        val languageIdentifier=LanguageIdentification.getClient()
        binding.button.setOnClickListener {
            languageIdentifier.identifyLanguage(binding.editText.text.toString())
                .addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        if(it.result!=null)
                        {
                            if(it.result!="und" && TranslateLanguage.fromLanguageTag(it.result!!)!=null)
                            {
                                Log.d("div","Translate L35 ${it.result} ${TranslateLanguage.fromLanguageTag(it.result!!)}")
                                binding.textViewCurrentLanguage.text=it.result
                                translate(binding.editText.text.toString(), TranslateLanguage.fromLanguageTag(it.result!!)!!)
                            }
                            else
                                binding.textViewCurrentLanguage.text="Undefined Language"
                        }
                        else
                            binding.textViewCurrentLanguage.text="null"
                    }
                    else
                    {
                        binding.textViewCurrentLanguage.text="Error ${it.exception}"
                        Log.d("div","Identification Task Failed ${it.exception}")
                    }
                }
        }

        return binding.root
    }

    private fun translate(text: String, fromLang: String) {
        val options=TranslatorOptions.Builder()
            .setSourceLanguage(fromLang)
            .setTargetLanguage(toLang)
            .build()
        val translator=Translation.getClient(options)

        val conditions=DownloadConditions.Builder()
            .build()                                        //You can provide conditions like wifi and charging here
        translator.downloadModelIfNeeded(conditions)
            .addOnCompleteListener { it1 ->
                if(it1.isSuccessful)
                {
                    Toast.makeText(requireContext(),"Translation model downloaded successfully",Toast.LENGTH_LONG).show()
                    translator.translate(text)
                        .addOnCompleteListener {
                            if(it.isSuccessful)
                            {
                                binding.textView.text=it.result
                                Log.d("div","Translation successful ${it.result}")
                            }
                            else
                            {
                                Toast.makeText(requireContext(),"Translation failed",Toast.LENGTH_LONG).show()
                                Log.d("div","Translation Task failed ${it.exception}")
                            }
                        }
                }
                else
                {
                    Toast.makeText(requireContext(),"Translation model couldn't be downloaded",Toast.LENGTH_LONG).show()
                    Log.d("div","Translation Download Task failed ${it1.exception}")
                }

            }




    }


}