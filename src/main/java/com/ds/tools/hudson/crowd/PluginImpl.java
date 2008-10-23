package com.ds.tools.hudson.crowd;

import hudson.Plugin;
import hudson.security.SecurityRealm;
import hudson.tasks.MailAddressResolver;

/**
 * Crowd authentication for Hudson
 * 
 * @author Nat Budin
 * @plugin
 */
public class PluginImpl extends Plugin {
    public void start() throws Exception {
        SecurityRealm.LIST.add(CrowdSecurityRealm.DescriptorImpl.INSTANCE);
        MailAddressResolver.LIST.add(new CrowdMailAddressResolver());
    }
}
