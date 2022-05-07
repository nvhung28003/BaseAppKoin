package com.example.baseappkoin.ui.introduce.splash

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.example.baseappkoin.R
import com.example.baseappkoin.databinding.FragmentSplashBinding
import com.example.baseappkoin.base.ui.BaseFragment
import com.example.baseappkoin.ui.MainActivity
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment<FragmentSplashBinding, MainActivity>() {
    private val splashVM: SplashViewModel by viewModel()


    override fun onInflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSplashBinding = FragmentSplashBinding.inflate(inflater)

    override fun setupView() {
        with(splashVM) {

            stateFlow.onEach {
                when (it) {
                    AuthState.Available -> {
                        navigateTo(
                            R.id.action_splashFragment_to_noticeFragment,
                            null,
                            R.id.nav_home,
                            true
                        )
                        handler?.switchBottomNavTab(R.id.nav_photo)

                    }
                }
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    override fun setupData() {

    }

    override fun onResume() {
        super.onResume()
        showBottomNav(false)
    }
}