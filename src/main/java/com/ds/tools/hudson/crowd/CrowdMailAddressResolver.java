package com.ds.tools.hudson.crowd;

import hudson.Extension;
import hudson.model.Hudson;
import hudson.model.User;
import hudson.tasks.MailAddressResolver;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.acegisecurity.userdetails.UsernameNotFoundException;
import org.springframework.dao.DataAccessException;

import com.atlassian.crowd.integration.acegi.user.CrowdUserDetails;

/**
 * MailAddressResolver that will look up a mail address via Crowd.
 * 
 * @author justinedelson
 * 
 */
@Extension
public class CrowdMailAddressResolver extends MailAddressResolver {

    @Override
    public String findMailAddressFor(User u) {
        // LDAP not active
        Hudson hudson = Hudson.getInstance();
        if (!(hudson.getSecurityRealm() instanceof CrowdSecurityRealm))
            return null;
        try {
            CrowdUserDetails details = (CrowdUserDetails) hudson.getSecurityRealm()
                    .getSecurityComponents().userDetails.loadUserByUsername(u.getId());
            String mail = details.getEmail();
            if (mail == null)
                return null; // not found
            return mail;
        } catch (UsernameNotFoundException e) {
            LOGGER.log(Level.FINE, "Failed to look up LDAP for e-mail address", e);
            return null;
        } catch (DataAccessException e) {
            LOGGER.log(Level.FINE, "Failed to look up LDAP for e-mail address", e);
            return null;
        }
    }

    private static final Logger LOGGER = Logger.getLogger(CrowdMailAddressResolver.class.getName());

}
