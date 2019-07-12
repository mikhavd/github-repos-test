package m13.retrofittest.main.api.retrofitgenerated.commits;

/**
 * Created by Mikhail Avdeev on 19.02.2019.
 */

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CommitData {

    @ SerializedName("sha")
    @ Expose
    private String sha;
    @ SerializedName("node_id")
    @ Expose
    private String nodeId;
    @ SerializedName("commit")
    @ Expose
    private Commit commit;
    @ SerializedName("url")
    @ Expose
    private String url;
    @ SerializedName("html_url")
    @ Expose
    private String htmlUrl;
    @ SerializedName("comments_url")
    @ Expose
    private String commentsUrl;
    @ SerializedName("author")
    @ Expose
    private Author_ author;
    @ SerializedName("committer")
    @ Expose
    private Committer_ committer;
    @ SerializedName("parents")
    @ Expose
    private List < Parent > parents = null;

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public Commit getCommit() {
        return commit;
    }

    public void setCommit(Commit commit) {
        this.commit = commit;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(String htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getCommentsUrl() {
        return commentsUrl;
    }

    public void setCommentsUrl(String commentsUrl) {
        this.commentsUrl = commentsUrl;
    }

    public Author_ getAuthor() {
        return author;
    }

    public void setAuthor(Author_ author) {
        this.author = author;
    }

    public Committer_ getCommitter() {
        return committer;
    }

    public void setCommitter(Committer_ committer) {
        this.committer = committer;
    }

    public List < Parent > getParents() {
        return parents;
    }

    public void setParents(List < Parent > parents) {
        this.parents = parents;
    }

}