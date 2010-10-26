/**
 * 
 */
package com.ds.tools.hudson.crowd;

import java.util.List;

import com.atlassian.crowd.integration.service.cache.BasicCache;
import com.atlassian.crowd.integration.soap.SOAPGroup;
import com.atlassian.crowd.integration.soap.SOAPPrincipal;

/**
 * Implementation of Atlassian Crowd's BasicCache interface that allows this
 * plugin to work without ehcache.
 * 
 * @author justinedelson
 * 
 */
public class NoOpCache implements BasicCache {

    /**
     * {@inheritDoc}
     */
    public void addGroupToUser(String userName, String groupName) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void addIsUserOrGroup(String name, Boolean isValidUserOrGroup) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void addOrReplaceAllGroupNames(List groupNames) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void addOrReplaceAllMembers(String groupName, List userNames) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void addOrReplaceAllMemberships(String userName, List groupNames) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void addOrReplaceAllUserNames(List userNames) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void addOrReplaceGroup(SOAPGroup group) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void addOrReplaceUser(SOAPPrincipal user) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void addParentForGroup(String childGroup, String parentGroup) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void addToAllGroupNamesCache(String groupName) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void addToAllUsers(String userName) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void addUserToGroup(String userName, String groupName) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public boolean containsGroupRelationships() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void flush() {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public List getAllGroupNames() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public List getAllMembers(String groupName) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public List getAllMemberships(String userName) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public List getAllUserNames() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public SOAPGroup getGroup(String groupName) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public List getParentGroups(String childGroup) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public SOAPPrincipal getUser(String userName) {
        // TODO Auto-generated method stub
        return null;
    }

    public SOAPPrincipal getUserWithAttributes(String s) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Boolean isMember(String userName, String groupName) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Boolean isMemberInGroupCache(String userName, String groupName) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public Boolean isUserOrGroup(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void removeAllMembers(String groupName) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void removeAllMemberships(String userName) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void removeAllMembershipsFromUserMembershipsCache(String userName) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void removeCachedGroupMembership(String userName, String groupName) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void removeCachedGroupMemberships(String groupName) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void removeCachedUser(String userName, String groupName) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void removeFromAllGroupNamesCache(String groupName) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void removeFromAllUsers(String userName) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public String getUserName(String s) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getGroupName(String s) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void addOrReplaceUserWithAttributes(SOAPPrincipal soapPrincipal) {

    }

    /**
     * {@inheritDoc}
     */
    public void removeGroup(String groupName) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void removeGroupFromUser(String userName, String groupName) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public void removeMembership(String userName, String groupName) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public boolean removeUser(String userName) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public void removeUserFromGroup(String userName, String groupName) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    public List setMembers(String groupName, String[] userNameArray) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void setMembership(String userName, String groupName, Boolean isMember) {
        // TODO Auto-generated method stub

    }

}
