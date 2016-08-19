package com.jpardogo.android.listbuddies.models;

import java.util.List;

/**
 * Created by creditcloud on 8/16/16.
 */
public class ImagesResponse {

    public List<ImageBean> getLeftImages() {
        return leftImages;
    }

    public void setLeftImages(List<ImageBean> leftImages) {
        this.leftImages = leftImages;
    }

    public List<ImageBean> getRightImages() {
        return rightImages;
    }

    public void setRightImages(List<ImageBean> rightImages) {
        this.rightImages = rightImages;
    }

    List<ImageBean> leftImages;
    List<ImageBean> rightImages;


}
