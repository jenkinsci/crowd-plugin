import org.acegisecurity.providers.ProviderManager
import org.acegisecurity.providers.anonymous.AnonymousAuthenticationProvider
import org.acegisecurity.providers.rememberme.RememberMeAuthenticationProvider
import com.atlassian.crowd.integration.acegi.user.CrowdUserDetailsServiceImpl
import com.atlassian.crowd.integration.acegi.RemoteCrowdAuthenticationProvider
import hudson.model.Hudson

crowdUserDetailsService(CrowdUserDetailsServiceImpl) {
    authenticationManager = ref("crowdAuthenticationManager")
    groupMembershipManager = ref("crowdGroupMembershipManager")
    userManager = ref("crowdUserManager")
}

crowdAuthenticationProvider(RemoteCrowdAuthenticationProvider, ref("crowdAuthenticationManager"), ref("httpAuthenticator"), ref("crowdUserDetailsService")) {
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
