
package demo.acube.application.healthcare.model.secondaryDoctorsList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Phone {

    @SerializedName("personal")
    @Expose
    private Object personal;
    @SerializedName("office")
    @Expose
    private String office;

    public Object getPersonal() {
        return personal;
    }

    public void setPersonal(Object personal) {
        this.personal = personal;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

}
