
package demo.acube.application.healthcare.model.doctorsSlot;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("slot")
    @Expose
    private Slot slot;
    @SerializedName("status")
    @Expose
    private Integer status;

    public Slot getSlot() {
        return slot;
    }

    public void setSlot(Slot slot) {
        this.slot = slot;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
