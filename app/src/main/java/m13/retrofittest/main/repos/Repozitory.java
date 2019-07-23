package m13.retrofittest.main.repos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import m13.retrofittest.main.api.generated.repos.Repo;

@Entity(tableName = "repositories",
        indices = {@Index(value = {"uid", "server_id"}, unique = true)})
public class Repozitory implements IExtendedRepo{

    @PrimaryKey(autoGenerate = true)
    public int uid;

    private static final String NEW_LINE = "\n";

    @ColumnInfo(name="contributors_number")
    private Integer contributorsNumber;

    @ColumnInfo(name="server_id")
    private Integer serverId;

    @ColumnInfo(name="full_name")
    private String fullName;

    @ColumnInfo(name="stargazers_count")
    private int stargazersCount;

    @ColumnInfo(name="description")
    private String description;

    @ColumnInfo(name="owner_login")
    private String ownerLogin;

    @ColumnInfo(name="created_at")
    private String createdAt;

    @ColumnInfo(name="git_url")
    private String gitUrl;

    public Repozitory(){}


    @Override
    public String getRepoInfo() {
        return
                toLineIfNotEmpty("Repo: ", this.fullName) +
                toLineIfNotEmpty("Contributors: ", String.valueOf(contributorsNumber)) +
                toLineIfNotEmpty("Stargazers: ", String.valueOf(stargazersCount)) +
                toLineIfNotEmpty("Description: ", this.description) +
                toLineIfNotEmpty("Owner: ", this.ownerLogin) +
                toLineIfNotEmpty("Created: ", this.createdAt) +
                toLineIfNotEmpty("GitURL: ", this.gitUrl) ;
    }

    private String toLineIfNotEmpty(String title, String line){
        if (line!=null) {
            return (line.isEmpty()) ? "" : title + " " + line + NEW_LINE;
        }
        else {
            return "";
        }
    }

    public static class RepositoryFabric {
        static public Repozitory getRepository(Repo repo, Integer contributorsNumber){
            Repozitory repozitory = new Repozitory();
            repozitory.setServerId(repo.getId());
            repozitory.setFullName(repo.getFullName());
            repozitory.setStargazersCount(repo.getStargazersCount());
            repozitory.setDescription(repo.getDescription());
            repozitory.setOwnerLogin(repo.getOwner().getLogin());
            repozitory.setCreatedAt(repo.getCreatedAt());
            repozitory.setGitUrl(repo.getGitUrl());
            repozitory.setContributorsNumber(contributorsNumber);
            return repozitory;
        }
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public Integer getServerId() {
        return serverId;
    }


    @Nullable
    @Override
    public Integer getContributorsNumber() {
        return contributorsNumber;
    }

    public void setContributorsNumber(Integer contributorsNumber) {
        this.contributorsNumber = contributorsNumber;
    }

    @NotNull
    @Override
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public int getStargazersCount() {
        return stargazersCount;
    }

    public void setStargazersCount(int stargazersCount) {
        this.stargazersCount = stargazersCount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwnerLogin() {
        return ownerLogin;
    }

    public void setOwnerLogin(String ownerLogin) {
        this.ownerLogin = ownerLogin;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getGitUrl() {
        return gitUrl;
    }

    public void setGitUrl(String gitUrl) {
        this.gitUrl = gitUrl;
    }
}
