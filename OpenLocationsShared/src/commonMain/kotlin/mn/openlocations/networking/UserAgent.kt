package mn.openlocations.networking

sealed interface UserAgent {
    val agent: String

    object Browser : UserAgent {
        override val agent: String
            get() = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/18.4 Safari/605.1.15"
    }

    object Mobile : UserAgentMobile(), UserAgent
}
