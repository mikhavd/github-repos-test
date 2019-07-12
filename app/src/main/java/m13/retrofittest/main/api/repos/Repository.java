package m13.retrofittest.main.api.repos;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.jetbrains.annotations.NotNull;

import m13.retrofittest.main.api.retrofitgenerated.repos.Repo;

@Entity
public class Repository implements IExtendedRepo{

    private static final String NEW_LINE = "\n";

    //private final Repo repo;

    @ColumnInfo(name="contributors_number")
    private final Integer contributorsNumber;

    @PrimaryKey
    private final int uid;

    @ColumnInfo(name = "name")
    private final String name;

    @ColumnInfo(name = "stargazersCount")
    private final int stargazersCount;

    @ColumnInfo(name = "fullName")
    private final String fullName;

    @ColumnInfo(name = "description")
    private final String description;

    @ColumnInfo(name = "ownerLogin")
    private final String ownerLogin;

    @ColumnInfo(name = "createdAt")
    private final String createdAt;

    @ColumnInfo(name = "gitURL")
    private final String gitURL;

    public Repository(Repo repo, Integer contributorsNumber) {
        //this.repo = repo;
        this.contributorsNumber = contributorsNumber;
        uid = repo.getId();
        name = repo.getName();
        stargazersCount = repo.getStargazersCount();
        fullName = repo.getFullName();
        description = repo.getDescription();
        ownerLogin = repo.getOwner().getLogin();
        createdAt = repo.getCreatedAt();
        gitURL = repo.getGitUrl();
    }


    public Integer getContributorsNumber() {
        return contributorsNumber;
    }

    @NotNull
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getStargazersCount() {
        return stargazersCount;
    }

    @NotNull
    @Override
    public String getRepoInfo() {
        return
                toLineIfNotEmpty("Repo: ", fullName) +
                toLineIfNotEmpty("Contributors: ", String.valueOf(contributorsNumber)) +
                toLineIfNotEmpty("Stargazers: ", String.valueOf(stargazersCount)) +
                toLineIfNotEmpty("Description: ", description) +
                toLineIfNotEmpty("Owner: ", ownerLogin) +
                toLineIfNotEmpty("Created: ", createdAt) +
                toLineIfNotEmpty("GitURL: ", gitURL) ;


    }

    private String toLineIfNotEmpty(String title, String line){
        if (line!=null) {
            return (line.isEmpty()) ? "" : title + " " + line + NEW_LINE;
        }
        else {
            return "";
        }
    }

    @Override
    public int getUid() {
        return uid;
    }
}
