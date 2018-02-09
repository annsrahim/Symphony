
package demo.acube.application.healthcare.activity.doctor.models.taskList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("task_id")
    @Expose
    private Integer taskId;
    @SerializedName("type")
    @Expose
    private Integer type;
    @SerializedName("order")
    @Expose
    private Integer order;
    @SerializedName("archived")
    @Expose
    private Boolean archived;
    @SerializedName("completed")
    @Expose
    private Boolean completed;
    @SerializedName("task_meta")
    @Expose
    private TaskMeta taskMeta;

    public Datum() {
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getArchived() {
        return archived;
    }

    public void setArchived(Boolean archived) {
        this.archived = archived;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public TaskMeta getTaskMeta() {
        return taskMeta;
    }

    public void setTaskMeta(TaskMeta taskMeta) {
        this.taskMeta = taskMeta;
    }

}
