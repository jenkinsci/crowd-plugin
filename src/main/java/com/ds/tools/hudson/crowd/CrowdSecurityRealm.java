package com.ds.tools.hudson.crowd;

import org.acegisecurity.AuthenticationManager;
import org.acegisecurity.userdetails.UserDetailsService;
import org.apache.log4j.Logger;
import org.kohsuke.stapler.DataBoundConstructor;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;

import groovy.lang.Binding;
import hudson.model.Descriptor;
import hudson.security.SecurityRealm;
import hudson.util.spring.BeanBuilder;

public class CrowdSecurityRealm extends SecurityRealm {
	protected static Logger log = Logger.getLogger(CrowdSecurityRealm.class);

	public static final class DescriptorImpl extends Descriptor<SecurityRealm> {
		public static final DescriptorImpl INSTANCE = new DescriptorImpl();

		public DescriptorImpl() {
			super(CrowdSecurityRealm.class);
		}

		@Override
		public String getDisplayName() {
			return "Crowd";
		}

	}

	@DataBoundConstructor
	public CrowdSecurityRealm() {
		super();
	}

	public SecurityComponents createSecurityComponents() {
		// load the base configuration from the crowd-integration-client jar
		XmlWebApplicationContext crowdConfigContext = new XmlWebApplicationContext();
		crowdConfigContext.setClassLoader(getClass().getClassLoader());
		crowdConfigContext.setConfigLocations(new String[] {
				"classpath:/applicationContext-CrowdClient.xml"
			});
		crowdConfigContext.refresh();
		
		// load the Hudson-Crowd configuration from Crowd.groovy
		BeanBuilder builder = new BeanBuilder(crowdConfigContext, getClass().getClassLoader());
		Binding binding = new Binding();
		builder.parse(getClass().getResourceAsStream("Crowd.groovy"),binding);
		WebApplicationContext context = builder.createApplicationContext();
		return new SecurityComponents(
				findBean(AuthenticationManager.class, context),
				findBean(UserDetailsService.class, context));
	}

	public Descriptor<SecurityRealm> getDescriptor() {
		return DescriptorImpl.INSTANCE;
	}

}
