/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.assault2142.hololol.chess.server.util;

import eu.assault2142.hololol.chess.server.exceptions.UnknownUserException;
import eu.assault2142.hololol.chess.server.exceptions.UsernameNotFreeException;
import eu.assault2142.hololol.chess.server.user.User;
import java.io.UnsupportedEncodingException;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import oracle.kv.Depth;
import oracle.kv.KVStore;
import oracle.kv.KVStoreConfig;
import oracle.kv.KVStoreFactory;
import oracle.kv.Key;
import oracle.kv.KeyRange;
import oracle.kv.Value;
import oracle.kv.ValueVersion;

/**
 * The connection to the K-V-Store
 *
 * @author hololol2
 */
public class Store {

    private static final String HOST = "assault2142.eu:5000";
    private static final String STORE = "kvstore";
    private KVStoreConfig config;
    private KVStore store;

    /**
     * Creates a new Store
     */
    public Store() {
        init();
    }

    /**
     * Initializes the Connection to the Database
     */
    private void init() {
        config = new KVStoreConfig(STORE, HOST);
        store = KVStoreFactory.getStore(config);
    }

    /**
     * Puts a new Value to the Database
     *
     * @param majorkey the key path
     * @param minorkey the subkey
     * @param value the value
     */
    private void put(String majorkey, String minorkey, String value) {
        try {
            store.put(Key.createKey(majorkey, minorkey), Value.createValue(value.getBytes("utf-8")));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Store.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Retrieves a Value from the Database
     *
     * @param majorkey the key path
     * @param minorkey the subkey
     * @return the value
     */
    private String get(String majorkey, String minorkey) {
        ValueVersion get = store.get(Key.createKey(majorkey, minorkey));
        try {
            return get == null ? null : new String(get.getValue().toByteArray(), "utf-8").trim();
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Store.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    /**
     * Removes a Value from the Database
     *
     * @param majorkey the key path
     * @param minorkey the subkey
     */
    private void remove(String majorkey, String minorkey) {
        store.delete(Key.createKey(majorkey, minorkey));
    }

    /**
     * Retrieves a set of values from the Database
     *
     * @param majorkey the key path
     * @param minorkeyfrom the start-subkey
     * @param minorkeyto the end-subkey
     * @return a Collection of the Values
     */
    private List<String> multiGet(String majorkey, String minorkeyfrom, String minorkeyto) {
        SortedMap<Key, ValueVersion> multiGet = store.multiGet(Key.createKey(majorkey), new KeyRange(minorkeyfrom, true, minorkeyto, true), Depth.PARENT_AND_DESCENDANTS);
        LinkedList<String> values = new LinkedList();
        multiGet.forEach((Key k, ValueVersion vv) -> {
            values.add(new String(vv.getValue().getValue()).trim());
        });
        return values;
    }

    /**
     * Creates a new User
     *
     * @param name the username
     * @param pass the password
     * @return the id of the user
     * @throws eu.assault2142.hololol.chess.server.exceptions.UsernameNotFreeException
     */
    public int createNewUser(String name, String pass) throws UsernameNotFreeException{
        String id = get("server", "nextid");
        if(!nameFree(name)) throw new UsernameNotFreeException(name);
        put(id, "username", name);
        put("server/names", name, id);
        put(id, "password", pass);
        id = id.trim();
        int i = Integer.parseInt(id);
        i++;
        put("server", "nextid", i + "");
        return i;
    }

    /**
     * Creates a new Friendship
     *
     * @param uid the first friend's id
     * @param fid the second friend's id
     * @throws eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public void addFriend(int uid, int fid) throws UnknownUserException{
        checkID(uid);
        checkID(fid);
        put(uid + "/friends", "" + fid, "" + fid);
        put(fid + "/friends", "" + uid, "" + uid);
    }

    /**
     * Add a new undelivered message
     *
     * @param uid the recipient's id
     * @param msg the message
     * @throws eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public void addMessage(int uid, String msg) throws UnknownUserException{
        checkID(uid);
        put(uid + "/pendingmsgs", msg, msg);
    }

    /**
     * Add a new pending friend-request
     *
     * @param uid the sender's id
     * @param fid the recipient's id
     * @throws eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public void addFriendRequest(int uid, int fid) throws UnknownUserException{
        checkID(uid);
        checkID(fid);
        put(fid + "/pendingrequests", uid + "", uid + "");
    }

    /**
     * Retrieve a user from the store
     *
     * @param id the user's id
     * @return the user or null if it does not exist
     * @throws eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public User getUser(int id) throws UnknownUserException{
        checkID(id);
        String name = getName(id);
        String pass = getPass(id);
        return new User(name, pass, getFriends(id), id);
    }

    /**
     * Retrieve a username from the store
     *
     * @param id the user's id
     * @return the username or null if it does not exist
     * @throws eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public String getName(int id) throws UnknownUserException{
        checkID(id);
        return get(id + "", "username");
    }

    /**
     * 
     * @param id
     * @throws eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public void checkID(int id) throws UnknownUserException{
        if(get(id+"","username")==null) throw new UnknownUserException(id);
    }
    /**
     * Retrieve a password from the store
     *
     * @param id the user's id
     * @return the password or null if it does not exist
     * @throws eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    private String getPass(int id) throws UnknownUserException{
        return get(id + "", "password");
    }

    /**
     * Retrieve all unread messages
     *
     * @param uid the user's id
     * @return a list with unread messages
     * @throws eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public List<String> getMessages(int uid) throws UnknownUserException{
        checkID(uid);
        return multiGet(uid + "/pendingmsgs", "0", null);
    }

    /**
     * Retrieves all pending friend-requests
     *
     * @param uid the user's id
     * @return a list with pending requests
     * @throws eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public List<Integer> getRequests(int uid) throws UnknownUserException{
        checkID(uid);
        List<String> requests = multiGet(uid + "/pendingrequests", "0", null);
        List<Integer> req = new LinkedList();
        for (String str : requests) {
            req.add(Integer.parseInt(str));
        }
        return req;
    }

    /**
     * Retrieves the user-ID
     *
     * @param name the name of the user
     * @return the ID
     * @throws eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public int getID(String name) throws UnknownUserException{
        String id = get("server/names", name);
        if(id == null) throw new UnknownUserException(name);
        return Integer.parseInt(id);
    }

    /**
     * Retrieves all friends of a user
     *
     * @param id the user's id
     * @return a list of the user's friend
     * @throws eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public List<Integer> getFriends(int id) throws UnknownUserException{
        checkID(id);
        List<String> friends = multiGet(id + "/friends", "0", null);
        List<Integer> fri = new LinkedList();
        for (String str : friends) {
            fri.add(Integer.parseInt(str));
        }
        return fri;

    }

    /**
     * Check if a username is free to take
     *
     * @param name the name to check
     * @return true if the name is free, false otherwise
     */
    public boolean nameFree(String name) {
        String s = get("server/names", name);
        return s == null;
    }

    /**
     * Change the name of a given user
     *
     * @param id the user's id
     * @param name the new username
     * @throws eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     * @throws eu.assault2142.hololol.chess.server.exceptions.UsernameNotFreeException
     */
    public void setUsername(int id, String name) throws UnknownUserException,UsernameNotFreeException{
        checkID(id);
        if(!nameFree(name)) throw new UsernameNotFreeException(name);
        String oldname = getName(id);
        put(id + "", "username", name);
        put("server/names", name, id + "");
        remove("server/names", oldname);
    }

    /**
     * Change the password of a given user
     *
     * @param id the user's id
     * @param pass the new password
     * @throws eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public void setPassword(int id, String pass) throws UnknownUserException{
        checkID(id);
        put(id + "", "password", pass);
    }

    /**
     * Remove a friendship
     *
     * @param uid the remover
     * @param fid the removed friend
     * @throws eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public void removeFriend(int uid, int fid) throws UnknownUserException{
        checkID(uid);
        checkID(fid);
        remove(uid + "/friends", fid + "");
        remove(fid + "/friends", uid + "");
    }

    /**
     * Removes a friend-request
     * @param id the original offerer
     * @param id0 the target of the request
     * @throws eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public void removeRequest(int id, int id0) throws UnknownUserException{
        checkID(id);
        checkID(id0);
        remove(id0 + "/pendingrequests", id + "");
    }

    /**
     * Check whether a request exists
     * @param id the original offerer
     * @param id0 the target of the request
     * @return true if the request exists, false otherwise
     * @throws eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public boolean requestExists(int id, int id0) throws UnknownUserException{
        checkID(id);
        checkID(id0);
        return get(id0 + "/pendingrequests", id + "") != null;
    }

    public static void main(String[]args){
        Store store = new Store();
        store.put("server", "nextid", "0");
        System.out.println(store.get("server", "nextid"));
    }
    
    /**
     * Remove a pending message
     * @param id the id of the recipient
     * @param msg the message
     * @throws eu.assault2142.hololol.chess.server.exceptions.UnknownUserException
     */
    public void deleteMessage(int id,String msg)throws UnknownUserException{
        checkID(id);
        remove(id+"/pendingmsgs",msg);
    }
}

