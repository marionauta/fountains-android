package mn.openlocations.networking

class KnownUris {
    companion object {
        const val website = "https://aguapp.nachbaur.dev"
        const val developer = "https://mario.nachbaur.dev"
        const val discovery = website

        fun help(slug: String): String = "${website}/help/${slug}"
    }
}