
package demo.acube.application.healthcare.model.doctorGroupSpeciality;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Phone {

    @SerializedName("personal")
    @Expose
    private String personal;
    @SerializedName("office")
    @Expose
    private String office;

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

}
