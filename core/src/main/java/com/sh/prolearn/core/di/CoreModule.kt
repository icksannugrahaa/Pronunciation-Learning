package com.sh.prolearn.core.di

import com.sh.prolearn.core.data.repository.AccountRepository
import com.sh.prolearn.core.data.repository.ModuleRepository
import com.sh.prolearn.core.data.source.remote.RemoteDataSource
import com.sh.prolearn.core.data.source.remote.network.ApiService
import com.sh.prolearn.core.domain.repository.IAccountRepository
import com.sh.prolearn.core.domain.repository.IModuleRepository
import com.sh.prolearn.core.utils.Consts.BASE_API_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object CoreModule {
//    val databaseModule = module {
//        factory { get<AppDatabase>().newsDao() }
//        factory { get<AppDatabase>().wifihistoryDao() }
//        single {
//            Room.databaseBuilder(
//                androidContext(),
//                AppDatabase::class.java, "MYUNLA.db"
//            ).fallbackToDestructiveMigration().build()
//        }
//    }

    val networkModule = module {
        single {
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .build()
        }
        single {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(get())
                .build()
            retrofit.create(ApiService::class.java)
        }
    }

    val repositoryModule = module {
//        single { LocalDataSource(get(), get()) }
        single { RemoteDataSource(get()) }
        single<IAccountRepository> {
            AccountRepository(
                get()
            )
        }
        single<IModuleRepository> {
            ModuleRepository(
                get()
            )
        }
    }
}