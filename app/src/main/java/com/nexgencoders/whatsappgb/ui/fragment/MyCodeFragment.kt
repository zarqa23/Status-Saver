package com.nexgencoders.whatsappgb.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.databinding.FragmentMyCodeBinding
import com.nexgencoders.whatsappgb.mvvm.viewmodel.MainViewModel
import com.nexgencoders.whatsappgb.ui.adapter.ScannedQrAdapter
import com.nexgencoders.whatsappgb.utils.toast

class MyCodeFragment : Fragment() {

    private lateinit var binding: FragmentMyCodeBinding
    private val viewModel: MainViewModel by viewModels()
    private var qrAdapter: ScannedQrAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyCodeBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.myCodeRv.setHasFixedSize(true)
        binding.myCodeRv.setLayoutManager(LinearLayoutManager(requireContext()))
        viewModel.getAllQrCode().observe(viewLifecycleOwner) { data ->
           if (data.isEmpty()){
               binding.messageTextView.visibility = View.VISIBLE
               binding.messageTextView.setText(R.string.no_files_found)
           }else{
               binding.messageTextView.visibility = View.GONE
               qrAdapter = ScannedQrAdapter(data)
               {
                   val action = QrCodeFragmentDirections.actionQrCodeFragmentToQrResultFragment(it,false)
                   findNavController().navigate(action)
               }
               binding.myCodeRv.setAdapter(qrAdapter)
           }

        }
    }

}