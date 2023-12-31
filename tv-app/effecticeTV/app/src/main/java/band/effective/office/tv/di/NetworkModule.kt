package band.effective.office.tv.di

import android.content.Context
import band.effective.office.tv.BuildConfig
import band.effective.office.tv.core.network.*
import band.effective.office.tv.core.network.EitherSynologyAdapterFactory
import band.effective.office.tv.network.LeaderIdRetrofitClient
import band.effective.office.tv.network.SynologyRetrofitClient
import band.effective.office.tv.core.network.UnsafeOkHttpClient
import band.effective.office.tv.network.*
import band.effective.office.tv.network.duolingo.DuolingoApi
import band.effective.office.tv.network.leader.LeaderApi
import band.effective.office.tv.network.mattermost.MattermostApi
import band.effective.office.tv.network.synology.SynologyApi
import band.effective.office.tv.network.uselessFact.UselessFactApi
import band.effective.office.tv.utils.GregorianCalendarMoshiAdapter
import band.effective.office.tv.utils.RStringGetter
import com.squareup.moshi.Moshi
import com.squareup.moshi.addAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import notion.api.v1.NotionClient
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.CallAdapter.Factory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @OptIn(ExperimentalStdlibApi::class)
    @Provides
    fun provideMoshi(): Moshi = Moshi.Builder().addAdapter(GregorianCalendarMoshiAdapter()).build()

    @Provides
    fun provideOkHttpClient() =
        OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()


    @Singleton
    @Provides
    @band.effective.office.tv.network.UnsafeOkHttpClient
    fun provideUnsafeOkHttpClient() =
        UnsafeOkHttpClient.getUnsafeOkHttpClient().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

    @Singleton
    @Provides
    @MattermostClient
    fun provideAuthInterceptor() : AuthInterceptor = AuthInterceptor(BuildConfig.mattermostBotToken)

    @Singleton
    @Provides
    @MattermostClient
    fun provideMattermostOkHttpClient(@MattermostClient authInterceptor: AuthInterceptor) = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(BuildConfig.mattermostBotToken))
        .addInterceptor(
            HttpLoggingInterceptor()
                .apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
        .build()

    @Singleton
    @Provides
    @LeaderIdRetrofitClient
    fun provideEitherAdapterFactory(): EitherLeaderIdAdapterFactory = EitherLeaderIdAdapterFactory()

    @Singleton
    @Provides
    @LeaderIdRetrofitClient
    fun provideLeaderIdRetrofit(
        moshiConverterFactory: MoshiConverterFactory,
        client: OkHttpClient,
        eitherLeaderIdAdapterFactory: EitherLeaderIdAdapterFactory
    ): Retrofit =
        Retrofit.Builder().addConverterFactory(moshiConverterFactory)
            .addCallAdapterFactory(eitherLeaderIdAdapterFactory).client(client)
            .baseUrl(BuildConfig.apiLeaderUrl).build()

    @Singleton
    @Provides
    @UselessFactClient
    fun provideUselessFactRetrofit(
        moshiConverterFactory: MoshiConverterFactory,
        client: OkHttpClient,
        eitherLeaderIdAdapterFactory: EitherLeaderIdAdapterFactory
    ): Retrofit =
        Retrofit.Builder().addConverterFactory(moshiConverterFactory)
            .addCallAdapterFactory(eitherLeaderIdAdapterFactory).client(client)
            .baseUrl(BuildConfig.uselessFactsApi).build()

    @Singleton
    @Provides
    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory =
        MoshiConverterFactory.create(moshi).asLenient()

    @Singleton
    @Provides
    @SynologyRetrofitClient
    fun provideEitherSynologyAdapterFactory(): CallAdapter.Factory =
        EitherSynologyAdapterFactory()

    @Singleton
    @Provides
    @DualingoRetrofitClient
    fun provideEitherDuolingoAdapterFactory(): CallAdapter.Factory =
        EitherDuolingoAdapterFactory()
    @Singleton
    @Provides
    @MattermostClient
    fun provideEitherMattermostAdapterFactory(): CallAdapter.Factory =
        EitherMattermostAdapterFactory()

    @Singleton
    @Provides
    @SynologyRetrofitClient
    fun provideSynologyRetrofit(
        moshiConverterFactory: MoshiConverterFactory,
        @band.effective.office.tv.network.UnsafeOkHttpClient client: OkHttpClient,
        @DualingoRetrofitClient callAdapter: CallAdapter.Factory
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(moshiConverterFactory)
            .addCallAdapterFactory(callAdapter)
            .client(client)
            .baseUrl(BuildConfig.apiSynologyUrl)
            .build()

    @Singleton
    @Provides
    @MattermostClient
    fun provideMattermostRetrofit(
        moshiConverterFactory: MoshiConverterFactory,
        @MattermostClient client: OkHttpClient,
        @MattermostClient callAdapter: Factory
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(moshiConverterFactory)
            .addCallAdapterFactory(callAdapter)
            .client(client)
            .baseUrl("https://${BuildConfig.apiMattermostUrl}")
            .build()

    @Singleton
    @Provides
    @DualingoRetrofitClient
    fun provideDuolingoRetrofit(
        moshiConverterFactory: MoshiConverterFactory,
        client: OkHttpClient,
        @DualingoRetrofitClient callAdapter: CallAdapter.Factory
    ): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(moshiConverterFactory)
            .addCallAdapterFactory(callAdapter)
            .client(client)
            .baseUrl(BuildConfig.duolingoUrl)
            .build()

    @Singleton
    @Provides
    fun provideLeaderApi(@LeaderIdRetrofitClient retrofit: Retrofit): LeaderApi =
        retrofit.create()

    @Singleton
    @Provides
    fun provideApiSynology(@SynologyRetrofitClient retrofit: Retrofit) =
        retrofit.create(SynologyApi::class.java)

    @Singleton
    @Provides
    fun provideApiMattermost(@MattermostClient retrofit: Retrofit): MattermostApi =
        retrofit.create()

    @Singleton
    @Provides
    fun provideUselessFactApi(@UselessFactClient retrofit: Retrofit): UselessFactApi =
        retrofit.create()

    @Singleton
    @Provides
    fun provideApiDuolingo(@DualingoRetrofitClient retrofit: Retrofit) =
        retrofit.create(DuolingoApi::class.java)

    @Singleton
    @Provides
    fun providesCoroutineScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }
    @Singleton
    @Provides
    fun provideNotionClient(): NotionClient {
        return NotionClient(BuildConfig.notionToken)
    }

    @Singleton
    @Provides
    fun provideRStringGetter(@ApplicationContext appContext: Context): RStringGetter = RStringGetter(context = appContext)
}