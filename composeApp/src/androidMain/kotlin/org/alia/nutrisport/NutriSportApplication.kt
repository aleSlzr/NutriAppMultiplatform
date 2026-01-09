package org.alia.nutrisport

import android.app.Application
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.initialize
import org.alia.nutrisport.di.initializeKoin
import org.koin.android.ext.koin.androidContext

class NutriSportApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        initializeKoin(
            config = {
                androidContext(this@NutriSportApplication)
            }
        )
        Firebase.initialize(context = this)
    }
}