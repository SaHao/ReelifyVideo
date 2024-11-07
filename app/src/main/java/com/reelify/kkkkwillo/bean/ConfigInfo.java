package com.reelify.kkkkwillo.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConfigInfo implements Serializable{
    public int code;
    public Data data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data implements Serializable {
        public Action action;
        public Chat chat;
        public String confirm;
        public boolean rtl;
        public Contacts contacts;
        public End end;
        public Link link;

        public Action getAction() {
            return action;
        }

        public void setAction(Action action) {
            this.action = action;
        }

        public Chat getChat() {
            return chat;
        }

        public void setChat(Chat chat) {
            this.chat = chat;
        }

        public String getConfirm() {
            return confirm;
        }

        public void setConfirm(String confirm) {
            this.confirm = confirm;
        }

        public boolean isRtl() {
            return rtl;
        }

        public void setRtl(boolean rtl) {
            this.rtl = rtl;
        }

        public Contacts getContacts() {
            return contacts;
        }

        public void setContacts(Contacts contacts) {
            this.contacts = contacts;
        }

        public End getEnd() {
            return end;
        }

        public void setEnd(End end) {
            this.end = end;
        }

        public Link getLink() {
            return link;
        }

        public void setLink(Link link) {
            this.link = link;
        }
    }

    public class Action implements Serializable{
        public String apply;
        public String applyA;
        public String applyC;
        public String download;

        public String getApply() {
            return apply;
        }

        public void setApply(String apply) {
            this.apply = apply;
        }

        public String getApplyA() {
            return applyA;
        }

        public void setApplyA(String applyA) {
            this.applyA = applyA;
        }

        public String getApplyC() {
            return applyC;
        }

        public void setApplyC(String applyC) {
            this.applyC = applyC;
        }

        public String getDownload() {
            return download;
        }

        public void setDownload(String download) {
            this.download = download;
        }
    }


    public class Chat implements Serializable{
        public Contents contents;

        public Contents getContents() {
            return contents;
        }

        public void setContents(Contents contents) {
            this.contents = contents;
        }

        public class Contents implements Serializable{
            public String guide_tip1;
            public String guide_tip2;
            public String kefu_question_fail;
            public String kefu_question_success;
            public String kefu_reply;
            public String user_hello;
            public String user_reply_no;
            public String user_reply_yes;
            public String kefu_question;

            public String getGuide_tip1() {
                return guide_tip1;
            }

            public void setGuide_tip1(String guide_tip1) {
                this.guide_tip1 = guide_tip1;
            }

            public String getGuide_tip2() {
                return guide_tip2;
            }

            public void setGuide_tip2(String guide_tip2) {
                this.guide_tip2 = guide_tip2;
            }

            public String getKefu_question_fail() {
                return kefu_question_fail;
            }

            public void setKefu_question_fail(String kefu_question_fail) {
                this.kefu_question_fail = kefu_question_fail;
            }

            public String getKefu_question_success() {
                return kefu_question_success;
            }

            public void setKefu_question_success(String kefu_question_success) {
                this.kefu_question_success = kefu_question_success;
            }

            public String getKefu_reply() {
                return kefu_reply;
            }

            public void setKefu_reply(String kefu_reply) {
                this.kefu_reply = kefu_reply;
            }

            public String getUser_hello() {
                return user_hello;
            }

            public void setUser_hello(String user_hello) {
                this.user_hello = user_hello;
            }

            public String getUser_reply_no() {
                return user_reply_no;
            }

            public void setUser_reply_no(String user_reply_no) {
                this.user_reply_no = user_reply_no;
            }

            public String getUser_reply_yes() {
                return user_reply_yes;
            }

            public void setUser_reply_yes(String user_reply_yes) {
                this.user_reply_yes = user_reply_yes;
            }

            public String getKefu_question() {
                return kefu_question;
            }

            public void setKefu_question(String kefu_question) {
                this.kefu_question = kefu_question;
            }
        }
    }



    public class Contacts implements Serializable{
        public boolean showContact;
        public List<String> contents = new ArrayList<>();
        public List<String> guide = new ArrayList<>();

        public List<String> getContents() {
            return contents;
        }

        public void setContents(List<String> contents) {
            this.contents = contents;
        }

        public boolean isShowContact() {
            return showContact;
        }

        public void setShowContact(boolean showContact) {
            this.showContact = showContact;
        }

        public List<String> getGuide() {
            return guide;
        }

        public void setGuide(List<String> guide) {
            this.guide = guide;
        }
    }

    public class End implements Serializable{
        public String apply;
        public String contents;
        public String title;
        public boolean ends;

        public String getApply() {
            return apply;
        }

        public void setApply(String apply) {
            this.apply = apply;
        }

        public String getContents() {
            return contents;
        }

        public void setContents(String contents) {
            this.contents = contents;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isEnds() {
            return ends;
        }

        public void setEnds(boolean ends) {
            this.ends = ends;
        }
    }
    public class Link implements Serializable{
        public String tgDownload;
        public String wsDownload;

        public String getTgDownload() {
            return tgDownload;
        }

        public void setTgDownload(String tgDownload) {
            this.tgDownload = tgDownload;
        }

        public String getWsDownload() {
            return wsDownload;
        }

        public void setWsDownload(String wsDownload) {
            this.wsDownload = wsDownload;
        }
    }
}
