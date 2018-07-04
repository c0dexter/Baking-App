package pl.michaldobrowolski.bakingapp.api.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Step implements Parcelable {

    public final static Parcelable.Creator<Step> CREATOR = new Creator<Step>() {

        @SuppressWarnings({
                "unchecked"
        })
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        public Step[] newArray(int size) {
            return (new Step[size]);
        }

    };

    // JSON keys
    private static final String JSON_KEY_STEP_ID = "id";
    private static final String JSON_KEY_SHORT_DESCRIPTION = "shortDescription";
    private static final String JSON_KEY_DESCRIPTION = "description";
    private static final String JSON_KEY_VIDEO_URL = "videoURL";
    private static final String JSON_KEY_THUMBNAIL_URL = "thumbnailURL";

    // Fields
    @SerializedName(JSON_KEY_STEP_ID)
    private int mStepId;
    @SerializedName(JSON_KEY_SHORT_DESCRIPTION)
    private String mShortDescription;
    @SerializedName(JSON_KEY_DESCRIPTION)
    private String mDescription;
    @SerializedName(JSON_KEY_VIDEO_URL)
    private String mVideoURL;
    @SerializedName(JSON_KEY_THUMBNAIL_URL)
    private String mThumbnailURL;

    private Step(Parcel in) {
        this.mStepId = ((int) in.readValue((int.class.getClassLoader())));
        this.mShortDescription = ((String) in.readValue((String.class.getClassLoader())));
        this.mDescription = ((String) in.readValue((String.class.getClassLoader())));
        this.mVideoURL = ((String) in.readValue((String.class.getClassLoader())));
        this.mThumbnailURL = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Step() {
    }

    public int getId() {
        return mStepId;
    }

    public String getmShortDescription() {
        return mShortDescription;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getmVideoURL() {
        return mVideoURL;
    }

    public String getThumbnailURL() {
        return mThumbnailURL;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mStepId);
        dest.writeValue(mShortDescription);
        dest.writeValue(mDescription);
        dest.writeValue(mVideoURL);
        dest.writeValue(mThumbnailURL);
    }

    public int describeContents() {
        return 0;
    }

}
