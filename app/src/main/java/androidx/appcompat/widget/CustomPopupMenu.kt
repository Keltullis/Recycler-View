package androidx.appcompat.widget

import android.annotation.SuppressLint
import android.content.Context
import android.view.View

class CustomPopupMenu @SuppressLint("RestrictedApi") constructor(context: Context, anchor:View):PopupMenu(context, anchor) {

    init {
        mPopup.setForceShowIcon(true)
    }
}