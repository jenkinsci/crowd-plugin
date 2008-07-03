import org.acegisecurity.providers.ProviderManager
import org.acegisecurity.providers.anonymous.AnonymousAuthenticationProvider
import com.atlassian.crowd.integration.acegi.CrowdUserDetailsService
import com.atlassian.crowd.integration.acegi.CrowdAuthenticationProvider
import org.acegisecurity.providers.rememberme.RememberMeAuthenticationProvider
import hudson.model.Hudson

crowdUserDetailsService(CrowdUserDetailsService) {
	securityServerClient = ref("securityServerClient")
}

// global so that this bean can be retrieved as UserDetailsService
crowdAuthenticationProvider(CrowdAuthenticationProvider) {
	userDetailsService = crowdUserDetailsService
	httpAuthenticator = ref("httpAuthenticator")
	securityServerClient = ref("securityServerClient")
}

authenticationManager(ProviderManager) {
    providers = [
        crowdAuthenticationProvider,

    	// these providers apply everywhere
        bean(RememberMeAuthenticationProvider) {
            key = Hudson.getInstance().getSecretKey();
        },
        // this doesn't mean we allow anonymous access.
        // we just authenticate anonymous users as such,
        // so that later authorization can reject them if so configured
        bean(AnonymousAuthenticationProvider) {
            key = "anonymous"
        }
    ]
}
