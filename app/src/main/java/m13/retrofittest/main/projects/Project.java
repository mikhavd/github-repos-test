package m13.retrofittest.main.projects;

/**
 * Created by Mikhail Avdeev on 08.02.2019.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Project {

    @SerializedName("owner_url")
    @Expose
    private String ownerUrl;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("html_url")
    @Expose
    private String htmlUrl;
    @SerializedName("columns_url")
    @Expose
    private String columnsUrl;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("node_id")
    @Expose
    private String nodeId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("number")
    @Expose
    private Integer number;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("creator")
    @Expose
    private Creator creator;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public String getOwnerUrl() {
        return ownerUrl;
    }

    public void setOwnerUrl(String ownerUrl) {
        this.ownerUrl = ownerUrl;
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

    public String getColumnsUrl() {
        return columnsUrl;
    }

    public void setColumnsUrl(String columnsUrl) {
        this.columnsUrl = columnsUrl;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Creator getCreator() {
        return creator;
    }

    public void setCreator(Creator creator) {
        this.creator = creator;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}
