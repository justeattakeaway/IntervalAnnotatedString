object BuildConfig {

    private val env = System.getenv()

    val isSnapshot: Boolean
        get() {
            return env.getOrElse("SNAPSHOT") { "true" }!!.toBoolean()
        }
}
