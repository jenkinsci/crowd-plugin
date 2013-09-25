package com.ds.tools.hudson.crowd;

import com.atlassian.crowd.integration.acegi.user.CrowdDataAccessException;
import com.atlassian.crowd.integration.exception.InvalidAuthorizationTokenException;
import com.atlassian.crowd.integration.exception.ObjectNotFoundException;
import com.atlassian.crowd.integration.service.GroupManager;
import com.atlassian.crowd.integration.service.soap.client.ClientProperties;
import com.atlassian.crowd.integration.soap.SOAPGroup;
import groovy.lang.Binding;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.security.GroupDetails;
import hudson.security.SecurityRealm;
import hudson.util.spring.BeanBuilder;
import org.acegisecurity.Authentication;
import org.acegisecurity.AuthenticationException;
import org.acegisecurity.AuthenticationManager;
import org.acegisecurity.GrantedAuthority;
import org.acegisecurity.userdetails.UserDetails;
import org.acegisecurity.userdetails.UserDetailsService;
import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.apache.log4j.Logger;
import org.kohsuke.stapler.DataBoundConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class CrowdSecurityRealm extends SecurityRealm {
    private static org.apache.log4j.Logger log = Logger.getLogger(CrowdSecurityRealm.class);

    public final String url;

    public final String applicationName;

    public final String applicationPassword;

    @Extension
    public static final class DescriptorImpl extends Descriptor<SecurityRealm> {
        public DescriptorImpl() {
            super(CrowdSecurityRealm.class);
        }

        @Override
        public String getDisplayName() {
            return "Crowd";
        }

    }

    @DataBoundConstructor
    public CrowdSecurityRealm(String url, String applicationName, String applicationPassword) {
        this.url = url.trim();
        this.applicationName = applicationName.trim();
        this.applicationPassword = applicationPassword.trim();
    }

    public SecurityComponents createSecurityComponents() {
        // load the base configuration from the crowd-integration-client jar
        XmlWebApplicationContext crowdConfigContext = new XmlWebApplicationContext();
        crowdConfigContext.setClassLoader(getClass().getClassLoader());
        /*
         * crowdConfigContext .setConfigLocations(new String[] {
         * "classpath:/applicationContext-HudsonCrowdClient.xml" });
         */
        crowdConfigContext
                .setConfigLocations(new String[]{"classpath:/applicationContext-CrowdClient.xml"});
        crowdConfigContext.refresh();

        // load the Hudson-Crowd configuration from Crowd.groovy
        BeanBuilder builder = new BeanBuilder(crowdConfigContext, getClass().getClassLoader());
        Binding binding = new Binding();
        builder.parse(getClass().getResourceAsStream("Crowd.groovy"), binding);
        WebApplicationContext context = builder.createApplicationContext();

        // configure the ClientProperties object
        if (applicationName != null || applicationPassword != null || url != null) {
            Properties props = new Properties();
            props.setProperty("application.name", applicationName);
            props.setProperty("application.password", applicationPassword);
            props.setProperty("crowd.server.url", url);
            props.setProperty("session.validationinterval", "5");
            ClientProperties clientProperties = (ClientProperties) crowdConfigContext
                    .getBean("clientProperties");
            clientProperties.updateProperties(props);
        } else {
            log.warn("Client properties are incomplete");
        }

        return new SecurityComponents(
                new CrowdAuthenticationManager(findBean(AuthenticationManager.class, context)),
                new CrowdDetailsServices(findBean(UserDetailsService.class, context),
                        (GroupManager) crowdConfigContext.getBean("crowdGroupManager", GroupManager.class)));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
        return super.loadUserByUsername(
                username);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public GroupDetails loadGroupByGroupname(String groupname)
            throws UsernameNotFoundException, DataAccessException {
        if (getSecurityComponents().userDetails instanceof CrowdDetailsServices) {
            return CrowdDetailsServices.class.cast(getSecurityComponents().userDetails).loadGroupByGroupname
                    (groupname);
        }
        return super.loadGroupByGroupname(groupname);
    }

    private static class CrowdAuthenticationManager implements AuthenticationManager {
        private final AuthenticationManager delegate;

        public CrowdAuthenticationManager(AuthenticationManager delegate) {
            this.delegate = delegate;
        }

        public Authentication authenticate(Authentication authentication) throws AuthenticationException {
            return new CrowdAuthentication(delegate.authenticate(authentication));
        }
    }

    private static class CrowdAuthentication implements Authentication {
        private final Authentication delegate;

        public CrowdAuthentication(Authentication delegate) {
            this.delegate = delegate;
        }

        public GrantedAuthority[] getAuthorities() {
            GrantedAuthority[] authorities = delegate.getAuthorities();
            List<GrantedAuthority> result = new ArrayList<GrantedAuthority>(
                    Arrays.asList(authorities == null ? new GrantedAuthority[0] : authorities));
            if (isAuthenticated()) {
                result.add(AUTHENTICATED_AUTHORITY);
            }
            return result.toArray(new GrantedAuthority[result.size()]);
        }

        public Object getCredentials() {
            return delegate.getCredentials();
        }

        public Object getDetails() {
            return delegate.getDetails();
        }

        public Object getPrincipal() {
            return delegate.getPrincipal();
        }

        public boolean isAuthenticated() {
            return delegate.isAuthenticated();
        }

        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
            delegate.setAuthenticated(isAuthenticated);
        }

        public String getName() {
            return delegate.getName();
        }

        @Override
        public int hashCode() {
            return delegate.hashCode();
        }

        @Override
        public String toString() {
            return delegate.toString();
        }

        @Override
        public boolean equals(Object another) {
            return delegate.equals(another);
        }
    }

    public static class CrowdDetailsServices implements UserDetailsService {

        private final UserDetailsService userDetailsService;

        private final GroupManager groupManager;

        public CrowdDetailsServices(UserDetailsService userDetailsService, GroupManager groupManager) {
            this.userDetailsService = userDetailsService;
            this.groupManager = groupManager;
        }

        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException,
                DataAccessException {
            return new CrowdUserDetail(userDetailsService.loadUserByUsername(username));
        }

        public GroupDetails loadGroupByGroupname(String groupname) throws DataAccessException,
                UsernameNotFoundException {
            try {
                if (groupManager.isGroup(groupname)) {
                    return new CrowdGroupDetails(groupManager.getGroup(groupname));
                }
                throw new UsernameNotFoundException("Could not find group named " + groupname);
            } catch (RemoteException e) {
                throw new CrowdDataAccessException(e);
            } catch (InvalidAuthorizationTokenException e) {
                throw new CrowdDataAccessException(e);
            } catch (ObjectNotFoundException e) {
                throw new UsernameNotFoundException(e.getMessage());
            }
        }

    }

    private static class CrowdUserDetail implements UserDetails {

        private final UserDetails delegate;

        public CrowdUserDetail(UserDetails delegate) {
            this.delegate = delegate;
        }

        public GrantedAuthority[] getAuthorities() {
            GrantedAuthority[] authorities = delegate.getAuthorities();
            List<GrantedAuthority> result = new ArrayList<GrantedAuthority>(
                    Arrays.asList(authorities == null ? new GrantedAuthority[0] : authorities));
            result.add(AUTHENTICATED_AUTHORITY);
            return result.toArray(new GrantedAuthority[result.size()]);
        }

        public String getPassword() {
            return delegate.getPassword();
        }

        public String getUsername() {
            return delegate.getUsername();
        }

        public boolean isAccountNonExpired() {
            return delegate.isAccountNonExpired();
        }

        public boolean isAccountNonLocked() {
            return delegate.isAccountNonLocked();
        }

        public boolean isCredentialsNonExpired() {
            return delegate.isCredentialsNonExpired();
        }

        public boolean isEnabled() {
            return delegate.isEnabled();
        }
    }

    private static class CrowdGroupDetails extends GroupDetails {
        private final SOAPGroup group;

        public CrowdGroupDetails(SOAPGroup group) {
            this.group = group;
        }

        @Override
        public String getName() {
            return group.getName();
        }
    }

}
