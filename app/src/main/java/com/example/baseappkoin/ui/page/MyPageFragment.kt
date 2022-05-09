package com.example.baseappkoin.ui.page

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.baseappkoin.databinding.FragmentMyPageBinding
import com.example.baseappkoin.base.ui.BaseFragment
import com.example.baseappkoin.ui.MainActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

//UM000
class MyPageFragment : BaseFragment<FragmentMyPageBinding, MainActivity>() {

    private val viewModel: MyPageViewModel by viewModel()

    override fun onInflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMyPageBinding = FragmentMyPageBinding.inflate(inflater)

    override fun setupView() {
        showBottomNav(true)
    }

    override fun setupData() {

    }
}