package com.moronlu18.invoice.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar


//Esta función de extensión se podrá llamar desde cualquier Activity/o fragmento a través de su Activity
//fragmento a través de su Activity

/*fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}*/

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}


object Utils {


    //habría que modificarlo respecto al de arriba
    fun showToast(context: Context,message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    //Todo no es extensión
    //Esta función es accesible desde cualquier Activity/Fragment
    fun showSnackBar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
    }
}