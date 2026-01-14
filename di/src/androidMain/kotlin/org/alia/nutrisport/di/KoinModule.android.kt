package org.alia.nutrisport.di

import org.alia.nutrisport.manage_product.util.PhotoPicker
import org.koin.dsl.module

actual val targetModule = module {
    single<PhotoPicker> { PhotoPicker() }
}