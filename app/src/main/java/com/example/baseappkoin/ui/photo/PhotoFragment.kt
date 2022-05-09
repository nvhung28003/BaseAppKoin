package com.example.baseappkoin.ui.photo

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.baseappkoin.databinding.FragmentPhotoBinding
import com.example.baseappkoin.base.ui.BaseFragment
import com.example.baseappkoin.extension.*
import com.example.baseappkoin.ui.MainActivity

import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel

class PhotoFragment : BaseFragment<FragmentPhotoBinding, MainActivity>() {
    private val viewModel: PhotoViewModel by sharedStateViewModel()
    override fun onInflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPhotoBinding  = FragmentPhotoBinding.inflate(inflater)

    override fun setupView() {
        showBottomNav(true)
    }

    override fun setupData() {

    }


}