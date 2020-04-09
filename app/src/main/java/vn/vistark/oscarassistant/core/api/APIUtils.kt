import vn.vistark.oscarassistant.core.api.RetrofitClient
import vn.vistark.oscarassistant.core.api.APIServices

class APIUtils {
    companion object {
        var mAPIServices: APIServices? = getAPIServices()
        private fun getAPIServices(): APIServices? {
            return RetrofitClient.getClient()?.create(APIServices::class.java)
        }

        fun replaceAPIServices() {
            mAPIServices = getAPIServices()
        }
    }
}