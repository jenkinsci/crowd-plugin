package com.ds.tools.hudson.crowd;

import groovy.lang.Binding;
import hudson.Extension;
import hudson.model.Descriptor;
import hudson.security.SecurityRealm;
import hudson.util.spring.BeanBuilder;

import java.util.Properties;

import org.acegisecurity.AuthenticationManager;
import org.acegisecurity.userdetails.UserDetailsService;
import org.apache.log4j.Logger;
import org.kohsuke.stapler.DataBoundConstructor;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.atlassian.crowd.integration.service.soap.client.ClientProperties;

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
                .setConfigLocations(new String[] { "classpath:/applicationContext-CrowdClient.xml" });
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

        return new SecurityComponents(findBean(AuthenticationManager.class, context), findBean(
                UserDetailsService.class, context));
    }
}
