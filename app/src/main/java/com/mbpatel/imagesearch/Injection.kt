package com.mbpatel.imagesearch

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.mbpatel.imagesearch.api.ServiceGenerator
import com.mbpatel.imagesearch.data.ImageRepository
import com.mbpatel.imagesearch.db.AppDatabase
import com.mbpatel.imagesearch.db.CommentRepository
import com.mbpatel.imagesearch.ui.detail.ImageDetailViewModelFactory
import com.mbpatel.imagesearch.ui.list.ViewModelFactory

/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    /**
     * Creates an instance of [ImageRepository] based on the [ServiceGenerator] and a
     * [ImageLocalCache]
     */
    private fun provideSearchRepository(): ImageRepository {
        return ImageRepository(ServiceGenerator.create())
    }

    /**
     * Provides the [ViewModelProvider.Factory] that is then used to get a reference to
     * [ViewModel] objects.
     */
    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(provideSearchRepository())
    }

    private fun getCommentRepository(context: Context): CommentRepository {
        return CommentRepository.getInstance(
            AppDatabase.getInstance(context.applicationContext).commentDao()
        )
    }

    fun provideImageDetailViewModelFactory(
        context: Context, imageId: String
    ): ImageDetailViewModelFactory {
        return ImageDetailViewModelFactory(getCommentRepository(context), imageId)
    }
}
