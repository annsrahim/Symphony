
package demo.acube.application.healthcare.model.chatModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("chat_id")
    @Expose
    private Integer chatId;
    @SerializedName("created_at")
    @Expose
    private Integer createdAt;
    @SerializedName("last_activity_at")
    @Expose
    private Integer lastActivityAt;
    @SerializedName("recent_messages")
    @Expose
    private RecentMessages recentMessages;

    public Integer getChatId() {
        return chatId;
    }

    public void setChatId(Integer chatId) {
        this.chatId = chatId;
    }

    public Integer getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Integer createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getLastActivityAt() {
        return lastActivityAt;
    }

    public void setLastActivityAt(Integer lastActivityAt) {
        this.lastActivityAt = lastActivityAt;
    }

    public RecentMessages getRecentMessages() {
        return recentMessages;
    }

    public void setRecentMessages(RecentMessages recentMessages) {
        this.recentMessages = recentMessages;
    }

}
