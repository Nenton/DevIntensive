package com.softdesign.devintensive.data.network.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class UserListRes {

    @SerializedName("success")
    @Expose
    public boolean success;
    @SerializedName("data")
    @Expose
    public List<Datum> data = new ArrayList<Datum>();

    public List<Datum> getData() {
        return data;
    }

    public class Repositories {

        @SerializedName("repo")
        @Expose
        public List<Repo> repo = new ArrayList<Repo>();
        @SerializedName("updated")
        @Expose
        public String updated;

        public List<Repo> getRepo() {
            return repo;
        }
    }


    public class Repo {

        @SerializedName("_id")
        @Expose
        public String id;
        @SerializedName("git")
        @Expose
        public String git;
        @SerializedName("title")
        @Expose
        public String title;

        public String getGit() {
            return git;
        }
    }

    public class PublicInfo {

        @SerializedName("bio")
        @Expose
        public String bio;
        @SerializedName("avatar")
        @Expose
        public String avatar;

        public String getBio() {
            return bio;
        }

        public String getPhoto() {
            return photo;
        }

        @SerializedName("photo")
        @Expose
        public String photo;
        @SerializedName("updated")
        @Expose
        public String updated;

    }

    public class ProfileValues {

        @SerializedName("homeTask")
        @Expose
        public int homeTask;
        @SerializedName("projects")
        @Expose
        public int projects;

        public int getProjects() {
            return projects;
        }

        public int getLinesCode() {
            return linesCode;
        }

        public int getRait() {
            return rait;
        }

        @SerializedName("linesCode")

        @Expose
        public int linesCode;
        @SerializedName("rait")
        @Expose
        public int rait;
        @SerializedName("updated")
        @Expose
        public String updated;

    }


    public class Datum {

        @SerializedName("_id")
        @Expose
        public String id;
        @SerializedName("first_name")
        @Expose
        public String firstName;

        public String getFirstName() {
            return firstName;
        }

        public String getSecondName() {
            return secondName;
        }

        public String getFullName(){
            return firstName + " " + secondName;
        }

        public Repositories getRepositories() {
            return repositories;
        }

        public ProfileValues getProfileValues() {
            return profileValues;
        }

        public PublicInfo getPublicInfo() {
            return publicInfo;
        }

        @SerializedName("second_name")

        @Expose
        public String secondName;
        @SerializedName("__v")
        @Expose
        public int v;
        @SerializedName("repositories")
        @Expose
        public Repositories repositories;
        @SerializedName("profileValues")
        @Expose
        public ProfileValues profileValues;
        @SerializedName("publicInfo")
        @Expose
        public PublicInfo publicInfo;
        @SerializedName("specialization")
        @Expose
        public String specialization;
        @SerializedName("updated")
        @Expose
        public String updated;

    }
}