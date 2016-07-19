package com.softdesign.devintensive.data.storage.models;

import com.softdesign.devintensive.data.network.res.UserListRes;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity(active = true, nameInDb = "REPOSITORIES")
public class Repositories {
    @Id
    private Long id;

    @NotNull
    @Unique
    private String remoteId;

    private String repositoryName;

    private String userRemoteId;

    public Repositories(UserListRes.Repo repositoryRes, String userId) {
        this.repositoryName = repositoryRes.getGit();
        this.userRemoteId = userId;
        this.remoteId = repositoryRes.getId();
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1073184084)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRepositoriesDao() : null;
    }

    /** Used for active entity operations. */
    @Generated(hash = 1338054742)
    private transient RepositoriesDao myDao;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    public String getUserRemoteId() {
        return this.userRemoteId;
    }

    public void setUserRemoteId(String userRemoteId) {
        this.userRemoteId = userRemoteId;
    }

    public String getRepositoryName() {
        return this.repositoryName;
    }

    public void setRepositoryName(String repositoryName) {
        this.repositoryName = repositoryName;
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

    @Generated(hash = 1525894492)
    public Repositories(Long id, @NotNull String remoteId, String repositoryName,
            String userRemoteId) {
        this.id = id;
        this.remoteId = remoteId;
        this.repositoryName = repositoryName;
        this.userRemoteId = userRemoteId;
    }

    @Generated(hash = 2037870747)
    public Repositories() {
    }
}
