package com.example.baseappkoin.base.ui

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.example.baseappkoin.R

class LoadingDialog(context: Context) : Dialog(context) {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.dialog_base_loading)
    setCancelable(false)
    setCanceledOnTouchOutside(false)
    window?.setBackgroundDrawableResource(R.color.transparent)
  }
}
