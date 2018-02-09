
package demo.acube.application.healthcare.model.medicationImageUploadBean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data {

    @SerializedName("image_id")
    @Expose
    private Integer imageId;
    @SerializedName("image")
    @Expose
    private Image image;

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

}
