
package demo.acube.application.healthcare.model.teleHealthTeamSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Phone_ {

    @SerializedName("personal")
    @Expose
    private String personal;
    @SerializedName("office")
    @Expose
    private Object office;

    public String getPersonal() {
        return personal;
    }

    public void setPersonal(String personal) {
        this.personal = personal;
    }

    public Object getOffice() {
        return office;
    }

    public void setOffice(Object office) {
        this.office = office;
    }

}
