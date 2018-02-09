
package demo.acube.application.healthcare.model.callRequestBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Phone {

    @SerializedName("personal")
    @Expose
    private Object personal;
    @SerializedName("office")
    @Expose
    private Object office;

    public Object getPersonal() {
        return personal;
    }

    public void setPersonal(Object personal) {
        this.personal = personal;
    }

    public Object getOffice() {
        return office;
    }

    public void setOffice(Object office) {
        this.office = office;
    }

}
