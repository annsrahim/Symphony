
package demo.acube.application.healthcare.activity.doctor.models.doctorSchedule;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Task {

    @SerializedName("task_id")
    @Expose
    private Integer taskId;
    @SerializedName("archived")
    @Expose
    private Boolean archived;
    @SerializedName("completed")
    @Expose
    private Boolean completed;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
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

}
