package com.moronlu18.invoice.ui

import android.content.Context
import com.danielstone.materialaboutlibrary.MaterialAboutFragment
import com.danielstone.materialaboutlibrary.items.MaterialAboutActionItem
import com.danielstone.materialaboutlibrary.model.MaterialAboutCard
import com.danielstone.materialaboutlibrary.model.MaterialAboutList
import com.moronlu18.invoice.R

class AboutMaterialFragment :MaterialAboutFragment() {

     override fun getMaterialAboutList(context: Context): MaterialAboutList {
        val authorCard: MaterialAboutCard.Builder = MaterialAboutCard.Builder()
        authorCard.title(getString(R.string.about_authors))
        authorCard.addItem(
            MaterialAboutActionItem.Builder()
                .text(getString(R.string.about_authors_alex))
                .subText(getString(R.string.about_authors_alexdesc))
                .icon(R.drawable.ic_action_onion)
                .build()
        )

         authorCard.addItem(
             MaterialAboutActionItem.Builder()
                 .text(getString(R.string.about_authors_jess))
                 .subText(getString(R.string.about_authors_jessdesc))
                 .icon(R.drawable.ic_action_woman)
                 .build()
         )

         authorCard.addItem(
             MaterialAboutActionItem.Builder()
                 .text(getString(R.string.about_authors_mateo))
                 .subText(getString(R.string.about_authors_mateodesc))
                 .icon(R.drawable.ic_action_leg)
                 .build()
         )

         authorCard.addItem(
             MaterialAboutActionItem.Builder()
                 .text(getString(R.string.about_authors_serg))
                 .subText(getString(R.string.about_authors_sergdesc))
                 .icon(R.drawable.ic_action_dolphinv2)
                 .build()
         )

       /* authorCard.addItem( MaterialAboutActionItem.Builder()
            .text(R.string.abut_bifurcar_en_github)
          //  .icon(R.drawable.ic_action_share)
            .setOnClickAction {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    //Uri.parse(getString(R.string.about_url_repos_GitHub))
                )
              //  startActivity(intent)
            }
            .build())*/

        val cardBuilder: MaterialAboutCard.Builder = MaterialAboutCard.Builder()
        cardBuilder.addItem(
            MaterialAboutActionItem.Builder()
                .text(R.string.about_version)
                .subText(getString(R.string.about_versionapp))
                .icon(R.drawable.ic_action_info)
                .build()
        )
        return MaterialAboutList.Builder()
            .addCard(authorCard.build())
            .addCard(cardBuilder.build())
            .build()
    }
}