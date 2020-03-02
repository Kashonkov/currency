package com.exmpale.ui

import com.exmpale.AppComponent
import com.exmpale.dagger.ActivityScope
import dagger.Component

/**
 * @author Kashonkov Nikita
 */
@ActivityScope
@Component(dependencies = [AppComponent::class])
interface MainComponent {
    fun inject(activity: MainActivity)
}