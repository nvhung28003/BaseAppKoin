package com.example.baseappkoin.base.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.example.baseappkoin.R
import com.example.baseappkoin.extension.toast
import com.example.baseappkoin.extension.unit
import com.example.baseappkoin.ui.MainActivity
import com.example.baseappkoin.utils.errorBody
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException

abstract class BaseFragment<B : ViewBinding, A : Any?> : Fragment() {
    private var _binding: B? = null
    protected val binding: B get() = _binding!!
    protected open var handler: A? = null //It's base activity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        @Suppress("UNCHECKED_CAST")
        this.handler = this.activity as? A
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            val observer = Observer<LifecycleOwner?> {
                it ?: return@Observer

                it.lifecycle.addObserver(object : DefaultLifecycleObserver {

                    override fun onCreate(owner: LifecycleOwner) {
                        super.onCreate(owner)
                        Timber.tag("[VIEW_DEBUG]").d("${this@BaseFragment}::onCreate")
                    }

                    override fun onStart(owner: LifecycleOwner) {
                        super.onStart(owner)
                        Timber.tag("[VIEW_DEBUG]").d("${this@BaseFragment}::onStart")
                    }

                    override fun onResume(owner: LifecycleOwner) {
                        super.onResume(owner)
                        Timber.tag("[VIEW_DEBUG]").d("${this@BaseFragment}::onResume")
                    }

                    override fun onPause(owner: LifecycleOwner) {
                        super.onPause(owner)
                        Timber.tag("[VIEW_DEBUG]").d("${this@BaseFragment}::onPause")
                    }

                    override fun onStop(owner: LifecycleOwner) {
                        super.onStop(owner)
                        Timber.tag("[VIEW_DEBUG]").d("${this@BaseFragment}::onStop")
                    }

                    override fun onDestroy(owner: LifecycleOwner) {
                        super.onDestroy(owner)
                        Timber.tag("[VIEW_DEBUG]").d("${this@BaseFragment}::onDestroy")
                    }
                })
            }

            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                Timber.tag("[DEBUG]").d("${this@BaseFragment}::onCreate")
                viewLifecycleOwnerLiveData.observeForever(observer)
            }

            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                Timber.tag("[DEBUG]").d("${this@BaseFragment}::onStart")
            }

            override fun onResume(owner: LifecycleOwner) {
                super.onResume(owner)
                Timber.tag("[DEBUG]").d("${this@BaseFragment}::onResume")
            }

            override fun onPause(owner: LifecycleOwner) {
                super.onPause(owner)
                Timber.tag("[DEBUG]").d("${this@BaseFragment}::onPause")
            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                Timber.tag("[DEBUG]").d("${this@BaseFragment}::onStop")
            }

            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                Timber.tag("[DEBUG]").d("${this@BaseFragment}::onDestroy")
                viewLifecycleOwnerLiveData.removeObserver(observer)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = onInflateView(inflater, container)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupData()
    }

    protected abstract fun onInflateView(inflater: LayoutInflater, container: ViewGroup?): B
    protected abstract fun setupView()
    protected abstract fun setupData()

    fun showToolbar(isShow: Boolean) {
        if (activity is MainActivity) {
            (activity as MainActivity).showToolbar(isShow)
        }
    }

    fun showBottomNav(isShow: Boolean) {
        if (activity is MainActivity) {
            (activity as MainActivity).showBottomNav(isShow)
        }
    }

    fun showLoading(isShow: Boolean) {
        (activity as? BaseActivity<*>)?.showLoading(isShow)
    }

    fun handleException(throwable: Throwable) {
        when (throwable) {
            is IOException -> showAlertError(getString(R.string.network_error))
            is HttpException -> {
                val code = throwable.code()
                val errorResponse = throwable.errorBody
                handleErrorCode(code)
            }
            else -> showAlertError(getString(R.string.network_error))
        }
    }

    fun handleErrorCode(code: Int) {
        when (code) {
            NetworkCode.BAD_REQUEST_ERROR.code -> {

            }
            NetworkCode.FORBIDDEN_ERROR.code -> {

            }
            NetworkCode.NOT_FOUND_ERROR.code -> {

            }
            NetworkCode.SERVER_ERROR.code -> {

            }
            NetworkCode.UNAUTHORIZED_ERROR.code -> {
                toast("UnAuthorized")
            }
            else -> unit
        }
    }

    fun showAlertError(message: String) {
        BaseAlertError.show(childFragmentManager, message)
    }



    fun navigateTo(
        id: Int,
        bundle: Bundle? = null,
        popUpToId: Int? = null,
        isInclusive: Boolean? = null
    ) {
        val options = NavOptions.Builder().setEnterAnim(R.anim.slide_in_right)
            .setExitAnim(R.anim.slide_out_left)
            .setPopEnterAnim(R.anim.slide_in_left)
            .setPopExitAnim(R.anim.slide_out_right)
        if (popUpToId != null && isInclusive != null) {
            options.setPopUpTo(popUpToId, isInclusive)
        }
        findNavController().navigate(id, bundle, options.build())
    }

    fun navigateToNoAnimation(
        id: Int,
        bundle: Bundle? = null,
        popUpToId: Int? = null,
        isInclusive: Boolean? = null
    ) {
        val options = NavOptions.Builder()
        if (popUpToId != null && isInclusive != null) {
            options.setPopUpTo(popUpToId, isInclusive)
        }
        findNavController().navigate(id, bundle, options.build())
    }

    fun popBackStack(id: Int? = null, isInclusive: Boolean? = null) {
        if (id == null || isInclusive == null) {
            findNavController().popBackStack()
            return
        }
        findNavController().popBackStack(id, isInclusive)
    }

    /**
     * Leak memory cause constraint root .
     */
    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        (activity as? BaseActivity<*>)?.hideLoading()
    }
}