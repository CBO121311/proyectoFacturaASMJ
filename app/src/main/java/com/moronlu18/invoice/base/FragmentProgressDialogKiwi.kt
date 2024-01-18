package com.moronlu18.invoice.base

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.moronlu18.invoice.R


//import com.moronlu18.loginapplication.R

class FragmentProgressDialogKiwi : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Utilizar la instancia de LayoutInfater para inflar el diseño XML que contiene un
        // componente ProgressBar
        val inflater = LayoutInflater.from(requireContext())
        val view = inflater.inflate(R.layout.fragment_dialog_progress_kiwi, null)


        val imageViewLoading = view.findViewById<ImageView>(R.id.imageViewLoadingKiwi)
        val rotation = ObjectAnimator.ofFloat(imageViewLoading, "rotation", 0f, 360f)
        rotation.duration = 1000
        rotation.repeatCount = ObjectAnimator.INFINITE
        rotation.start()


        // Crea un cuadro de diálogo con el diseño inflado
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)
        builder.setTitle(getString(R.string.progress_dialog_title))

        builder.setCancelable(false)


        // Devuelve el cuadro de diálogo creado
        return builder.create().apply {
            setCanceledOnTouchOutside(false)
        }
    }
}
