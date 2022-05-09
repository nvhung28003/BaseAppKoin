package com.example.baseappkoin.ui.notice

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.baseappkoin.databinding.FragmentNoticeListBinding
import com.example.baseappkoin.ui.MainActivity

import com.example.baseappkoin.base.ui.BaseFragment
import org.koin.androidx.viewmodel.ext.android.sharedStateViewModel

/**
 * UN000
 */
class NoticeListFragment : BaseFragment<FragmentNoticeListBinding, MainActivity>() {

    private val viewModel: NoticeViewModel by sharedStateViewModel()

    override fun onInflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNoticeListBinding = FragmentNoticeListBinding.inflate(inflater)

    override fun setupView() {
        showBottomNav(true)
    }

    override fun setupData() {

    }


}
