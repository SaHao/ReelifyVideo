package com.reelify.kkkkwillo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ListInfo implements Serializable {
    public int code;
    public MainData data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public MainData getData() {
        return data;
    }

    public void setData(MainData data) {
        this.data = data;
    }

    public class MainData implements Serializable {
        public List<Data> videos;

        public List<Data> getVideos() {
            return videos;
        }

        public void setVideos(List<Data> videos) {
            this.videos = videos;
        }
    }
    public class Data implements Serializable {
        public boolean ads;
        public boolean certification;
        public int id;
        public String avatar;
        public String desc;
        public String favorite;
        public String name;
        public String type;
        public Comment comment;
        public End end;
        public Contacts contacts;
        public List<Sources> sources;

        public boolean isAds() {
            return ads;
        }

        public void setAds(boolean ads) {
            this.ads = ads;
        }

        public boolean isCertification() {
            return certification;
        }

        public void setCertification(boolean certification) {
            this.certification = certification;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getFavorite() {
            return favorite;
        }

        public void setFavorite(String favorite) {
            this.favorite = favorite;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Comment getComment() {
            return comment;
        }

        public void setComment(Comment comment) {
            this.comment = comment;
        }

        public End getEnd() {
            return end;
        }

        public void setEnd(End end) {
            this.end = end;
        }

        public Contacts getContacts() {
            return contacts;
        }

        public void setContacts(Contacts contacts) {
            this.contacts = contacts;
        }

        public List<Sources> getSources() {
            return sources;
        }

        public void setSources(List<Sources> sources) {
            this.sources = sources;
        }
    }

    public class Comment implements Serializable {
        public String amount;
        public ArrayList<comments> comments;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public ArrayList<ListInfo.comments> getComments() {
            return comments;
        }

        public void setComments(ArrayList<ListInfo.comments> comments) {
            this.comments = comments;
        }
    }

    public class Sources implements Serializable {
        public String src;
        public String type;
        public String shape;

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getShape() {
            return shape;
        }

        public void setShape(String shape) {
            this.shape = shape;
        }
    }

    public static class comments implements Serializable {
        public comments(String src, String type, String name, String desc, String time) {
            this.src = src;
            this.type = type;
            this.name = name;
            this.desc = desc;
            this.time = time;
        }

        public String src;
        public String type;
        public String name;
        public String desc;
        public String time;

        public String getSrc() {
            return src;
        }

        public void setSrc(String src) {
            this.src = src;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }
    public class End implements Serializable {
        public String title;
        public String desc;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }
    public class Contacts implements Serializable {
        public long id;
        public String url;
        public String type;
        public String text;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getType() {
            return type;
        }

        public void setType(String rate) {
            this.type = rate;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
