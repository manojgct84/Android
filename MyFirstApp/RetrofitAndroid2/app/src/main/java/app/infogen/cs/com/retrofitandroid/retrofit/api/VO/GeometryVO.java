package app.infogen.cs.com.retrofitandroid.retrofit.api.VO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell on 12/17/2017.
 */

public class GeometryVO {

    @SerializedName("location")
    @Expose
    private GoogleLocationVO googleLocationVO;

    public GoogleLocationVO getGoogleLocationVO() {
        return googleLocationVO;
    }

    public void setGoogleLocationVO(GoogleLocationVO googleLocationVO) {
        this.googleLocationVO = googleLocationVO;
    }
}
