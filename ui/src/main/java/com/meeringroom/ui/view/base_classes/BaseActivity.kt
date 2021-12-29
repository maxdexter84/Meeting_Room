package com.meeringroom.ui.view.base_classes

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.example.core_module.deeplink_manager.DeeplinkNavigatorHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder

abstract class BaseActivity<B : ViewBinding>(
    val bindingFactory: (LayoutInflater) -> B,
    private val navHost: Int
) :
    AppCompatActivity() {
    lateinit var binding: B
    lateinit var navController: NavController
    lateinit var deeplinkNavigatorHelper: DeeplinkNavigatorHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingFactory(layoutInflater)
        setContentView(binding.root)

        navController =
            (supportFragmentManager.findFragmentById(navHost) as NavHostFragment).navController
        deeplinkNavigatorHelper = DeeplinkNavigatorHelper(navController)
    }

    abstract fun getViewBinding(): B
    /**
    By default, only declaring items are displayed in the dialog box.
    You need to pass positiveBtn and negativeBtn lambdas to implement your logic for clicking these buttons */
    fun showDialog(
        title: Int? = null,
        message: Int? = null,
        isCancelable: Boolean = true,
        positiveBtnText: Int? = null,
        negativeBtnText: Int? = null,
        positiveBtn: (() -> Unit)? = null,
        negativeBtn: (() -> Unit)? = null
    ) {
        MaterialAlertDialogBuilder(this).apply {
            title?.let { setTitle(it) }
            message?.let { setMessage(it) }
            setCancelable(isCancelable)
            positiveBtnText?.let {
                setPositiveButton(it) { dialog: DialogInterface, _: Int ->
                    positiveBtn?.invoke()
                    dialog.cancel()
                }
            }
            negativeBtnText?.let {
                setNegativeButton(it) { dialog: DialogInterface, _: Int ->
                    negativeBtn?.invoke()
                    dialog.cancel()
                }
            }
        }.show()
    }
}