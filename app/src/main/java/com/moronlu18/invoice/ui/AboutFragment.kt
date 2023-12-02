package com.moronlu18.invoice.ui

import android.content.Context
import android.content.Intent
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard
import com.danielstone.materialaboutlibrary.model.MaterialAboutList

import com.moronlu18.invoice.R


class AboutFragment {
     fun getMaterialAboutList(context: Context): MaterialAboutList {
        val authorCard: MaterialAboutCard.Builder = MaterialAboutCard.Builder()
        authorCard.title(R.string.about_autor)
        authorCard.addItem(
            MaterialAboutActionItem.Builder()
                .text(R.string.about_alejandro_l_pez)
                .subText(R.string.about_alumno_2_dam)
                //.icon(R.drawable.ic_action_person)
                .build()
        )
        authorCard.addItem( MaterialAboutActionItem.Builder()
            .text(R.string.abut_bifurcar_en_github)
          //  .icon(R.drawable.ic_action_share)
            .setOnClickAction {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    //Uri.parse(getString(R.string.about_url_repos_GitHub))
                )
              //  startActivity(intent)
            }
            .build())
        authorCard.addItem( MaterialAboutActionItem.Builder()
            .text(R.string.about_mi_linkedin)
           // .icon(R.drawable.ic_action_linkedin)
            .setOnClickAction {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    //Uri.parse(getString(R.string.about_url_linkedin))
                )
                //startActivity(intent)
            }
            .build())
        val cardBuilder: MaterialAboutCard.Builder = MaterialAboutCard.Builder()
        cardBuilder.addItem(
            MaterialAboutActionItem.Builder()
                .text(R.string.about_version)
                //.subText(R.string.about_num_version)
                //.icon(R.drawable.ic_action_info)
                .build()
        )
        return MaterialAboutList.Builder()
            .addCard(authorCard.build())
            .addCard(cardBuilder.build())
            .build()
    }


    /***
     * Método sobreescrito que muestra el título de la actividad
     * @return devuelve la cadena "Acerca de"
     */
    fun getActivityTitle(): CharSequence? {
        return R.string.mal_title_about.toString()
    }
}