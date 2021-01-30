package com.practice.mlkitnlp

import android.os.Bundle
import android.provider.Telephony
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.textclassifier.ConversationActions
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.mlkit.nl.smartreply.*
import com.google.mlkit.nl.smartreply.component.SmartReplyComponentRegistrar
import com.practice.mlkitnlp.databinding.FragmentLanguageIdentifierBinding


class SmartReplyFragment : Fragment() {

    private lateinit var binding:FragmentLanguageIdentifierBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=DataBindingUtil.inflate(layoutInflater,R.layout.fragment_language_identifier,container,false)

        val conversation=ArrayList<TextMessage>()
        conversation.add(TextMessage.createForLocalUser("how are you",System.currentTimeMillis()))
        conversation.add(TextMessage.createForRemoteUser("fine",System.currentTimeMillis(),"recipient1"))
        //The local user is the user itself,
        // the remote user is the person who is on the other sie of the conversation,
        // the userId can be any string that uniquely identifies the person on other side
        //the userId can be firebase userId in case of firebase
        conversation.add(TextMessage.createForLocalUser("how are you",System.currentTimeMillis()))
        conversation.add(TextMessage.createForRemoteUser("fine",System.currentTimeMillis(),"recipient1"))
        conversation.add(TextMessage.createForLocalUser("how are you",System.currentTimeMillis()))
        conversation.add(TextMessage.createForRemoteUser("not fine",System.currentTimeMillis(),"recipient1"))
        conversation.add(TextMessage.createForLocalUser("how are you",System.currentTimeMillis()))
        conversation.add(TextMessage.createForRemoteUser("fine and you",System.currentTimeMillis(),"recipient1"))
        conversation.add(TextMessage.createForLocalUser("how are you",System.currentTimeMillis()))
        conversation.add(TextMessage.createForRemoteUser("sleepy",System.currentTimeMillis(),"recipient1"))
        conversation.add(TextMessage.createForLocalUser("how are you",System.currentTimeMillis()))
        conversation.add(TextMessage.createForRemoteUser("unwell",System.currentTimeMillis(),"recipient1"))
        conversation.add(TextMessage.createForLocalUser("how are you",System.currentTimeMillis()))
        conversation.add(TextMessage.createForRemoteUser("shut up",System.currentTimeMillis(),"recipient1"))

        val smartReplyGenerator=SmartReply.getClient()

        binding.button.setOnClickListener {
            smartReplyGenerator.suggestReplies(conversation)
                .addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        if(it.result!=null) {
                            if (it.result!!.status == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {
                                Toast.makeText(requireContext(),"Language not supported. Only English",Toast.LENGTH_LONG).show()
                            } else if (it.result!!.status == SmartReplySuggestionResult.STATUS_SUCCESS) {
                                var s=""
                                for(suggestion in it.result!!.suggestions)
                                    s="$s${suggestion.text} | "
                                binding.textView.text=s
                            }
                        }
                        else
                        {
                            Toast.makeText(requireContext(),"task result ${it.result}",Toast.LENGTH_LONG).show()
                            Log.d("div","task result ${it.result}")
                        }

                    }
                    else {
                        Toast.makeText(requireContext(),"Task failed ${it.exception}",Toast.LENGTH_LONG).show()
                        Log.d("div", "Task failed ${it.exception}")
                    }
                }
        }

        return binding.root
    }


}