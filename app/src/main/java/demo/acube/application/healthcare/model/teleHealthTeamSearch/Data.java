
package demo.acube.application.healthcare.model.teleHealthTeamSearch;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("name")
    @Expose
    private List<Name> name = null;
    @SerializedName("specialty")
    @Expose
    private List<Specialty_> specialty = null;
    @SerializedName("address")
    @Expose
    private List<Address__> address = null;

    public List<Name> getName() {
        return name;
    }

    public void setName(List<Name> name) {
        this.name = name;
    }

    public List<Specialty_> getSpecialty() {
        return specialty;
    }

    public void setSpecialty(List<Specialty_> specialty) {
        this.specialty = specialty;
    }

    public List<Address__> getAddress() {
        return address;
    }

    public void setAddress(List<Address__> address) {
        this.address = address;
    }

}
