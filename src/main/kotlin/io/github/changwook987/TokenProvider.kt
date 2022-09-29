package io.github.changwook987

import com.github.hanshsieh.pixivj.model.Credential
import com.github.hanshsieh.pixivj.oauth.PixivOAuthClient
import com.github.hanshsieh.pixivj.token.TokenProvider

class TokenProvider(refresher: String = "54exUZV6LiYq6IXQZ4Kh1mSko44vtOwiObKgSYb9j4k") :
    TokenProvider {
    private val accToken: String
    private val refToken: String

    init {
        val authClient = PixivOAuthClient.Builder().build()

        val cred = Credential()

        cred.apply {
            clientId = "MOBrBDS8blbauoSck0ZfDbtuzpyT"
            clientSecret = "lsACyCD94FhDUtGTXi3QzcFE2uU1hqtDaKeqrdwj"
            refreshToken = refresher
        }

        val res = authClient.authenticate(cred)

        accToken = res.accessToken
        refToken = res.refreshToken
    }

    override fun close() {
        return
    }

    override fun getAccessToken(): String {
        return accToken
    }

    fun getRefresherToken() = refToken
}