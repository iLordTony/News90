package mx.lordtony.news90.models;

import android.util.Log;

import mx.lordtony.news90.utils.DateUtils;

/**
 * Created by USER on 13/03/2015.
 */
public class Tweet {

    private String id;

    private String name, screenName, profileImageUrl, text, createdAt;
    private static final String TAG = "Tag90";
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getScreenName() {

        return screenName;
    }

    public void setScreenName(String screenName) {

        this.screenName = screenName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {

        this.profileImageUrl = profileImageUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {

        this.text = text;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {

        this.createdAt = createdAt;
    }
}
