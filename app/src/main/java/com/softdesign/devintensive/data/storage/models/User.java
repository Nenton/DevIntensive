package com.softdesign.devintensive.data.storage.models;


import com.softdesign.devintensive.data.manager.DataManager;
import com.softdesign.devintensive.data.network.res.UserListRes;
import com.softdesign.devintensive.ui.activities.UserListActivity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinProperty;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Unique;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(active = true, nameInDb = "USERS")
public class User {
    @Id
    private Long id;

    @NotNull
    @Unique
    private String remoteId;

    @NotNull
    @Unique
    private String fullName;

    private String photo;

    @NotNull
    @Unique
    private String searchName;

    private int rating;
    private int codeLines;
    private int projects;

    private int sortPosition;

    private boolean sort;

    private String bio;
    @ToMany(joinProperties = {
            @JoinProperty(name = "remoteId",referencedName = "userRemoteId")
    })
    private List<Repositories> mRepositories;

    public User(UserListRes.Datum userRes) {
        this.remoteId = userRes.getId();
        this.photo = userRes.getPublicInfo().getPhoto();
        this.fullName = userRes.getFullName();
        this.searchName = userRes.getFullName().toUpperCase();
        this.rating = userRes.getProfileValues().getRait();
        this.codeLines = userRes.getProfileValues().getLinesCode();
        this.projects = userRes.getProfileValues().getProjects();
        this.bio = userRes.getPublicInfo().getBio();
        this.sort = false;
        this.sortPosition = DataManager.sNumberInBd++;
    }

    public User(User user, Boolean sort, int sortPosition) {
        this.id = user.getId();
        this.remoteId = user.getRemoteId();
        this.photo = user.getPhoto();
        this.fullName = user.getFullName();
        this.searchName = user.getFullName().toUpperCase();
        this.rating = user.getRating();
        this.codeLines = user.getCodeLines();
        this.projects = user.getProjects();
        this.bio = user.getBio();
        this.sort = sort;
        this.sortPosition = sortPosition;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }
    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }
    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 1361215124)
    public synchronized void resetMRepositories() {
        mRepositories = null;
    }
    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1943617096)
    public List<Repositories> getMRepositories() {
        if (mRepositories == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RepositoriesDao targetDao = daoSession.getRepositoriesDao();
            List<Repositories> mRepositoriesNew = targetDao._queryUser_MRepositories(remoteId);
            synchronized (this) {
                if(mRepositories == null) {
                    mRepositories = mRepositoriesNew;
                }
            }
        }
        return mRepositories;
    }
    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2059241980)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getUserDao() : null;
    }
    /** Used for active entity operations. */
    @Generated(hash = 1507654846)
    private transient UserDao myDao;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;
    public String getBio() {
        return this.bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
    public int getProjects() {
        return this.projects;
    }
    public void setProjects(int projects) {
        this.projects = projects;
    }
    public int getCodeLines() {
        return this.codeLines;
    }
    public void setCodeLines(int codeLines) {
        this.codeLines = codeLines;
    }
    public int getRating() {
        return this.rating;
    }
    public void setRating(int rating) {
        this.rating = rating;
    }
    public String getSearchName() {
        return this.searchName;
    }
    public void setSearchName(String searchName) {
        this.searchName = searchName;
    }
    public String getPhoto() {
        return this.photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getFullName() {
        return this.fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getRemoteId() {
        return this.remoteId;
    }
    public void setRemoteId(String remoteId) {
        this.remoteId = remoteId;
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public boolean getSort() {
        return this.sort;
    }

    public void setSort(boolean sort) {
        this.sort = sort;
    }

    public int getSortPosition() {
        return this.sortPosition;
    }


    public void setSortPosition(int sortPosition) {
        this.sortPosition = sortPosition;
    }
    @Generated(hash = 897847713)
    public User(Long id, @NotNull String remoteId, @NotNull String fullName, String photo,
            @NotNull String searchName, int rating, int codeLines, int projects, int sortPosition,
            boolean sort, String bio) {
        this.id = id;
        this.remoteId = remoteId;
        this.fullName = fullName;
        this.photo = photo;
        this.searchName = searchName;
        this.rating = rating;
        this.codeLines = codeLines;
        this.projects = projects;
        this.sortPosition = sortPosition;
        this.sort = sort;
        this.bio = bio;
    }

    @Generated(hash = 586692638)
    public User() {
    }
}
