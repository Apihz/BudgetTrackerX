package com.biscuittaiger.budgettrackerx.Model;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class IconPack {
    private String iconPackPath = "/com/biscuittaiger/budgettrackerx/IconPack/";

    private Image dashboardIcon = new Image(getClass().getResourceAsStream(iconPackPath + "dashboard.png"));
    private Image transactionIcon = new Image(getClass().getResourceAsStream(iconPackPath + "transaction.png"));
    private Image analyticsIcon = new Image(getClass().getResourceAsStream(iconPackPath + "analytics.png"));
    private Image savingsIcon = new Image(getClass().getResourceAsStream(iconPackPath + "savings.png"));
    private Image notificationIcon = new Image(getClass().getResourceAsStream(iconPackPath + "notification.png"));

    private ImageView dashboardIconUse = new ImageView(dashboardIcon);
    private ImageView transactionIconUse = new ImageView(transactionIcon);
    private ImageView analyticsIconUse = new ImageView(analyticsIcon);
    private ImageView savingsIconUse = new ImageView(savingsIcon);
    private ImageView notificationIconUse = new ImageView(notificationIcon);


    public IconPack() {
        ImageViewSizing();
    }

    private void ImageViewSizing() {
        setImageViewSize(dashboardIconUse, 23, 23);
        setImageViewSize(transactionIconUse, 23, 23);
        setImageViewSize(analyticsIconUse, 23, 23);
        setImageViewSize(savingsIconUse, 23, 23);
        setImageViewSize(notificationIconUse, 23, 23);
    }

    private void setImageViewSize(ImageView imageView, double width, double height) {
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
    }

    public ImageView getDashboardIcon() {
        return dashboardIconUse;
    }

    public ImageView getTransactionIcon() {
        return transactionIconUse;
    }

    public ImageView getAnalyticsIcon() {
        return analyticsIconUse;
    }

    public ImageView getSavingsIcon() {
        return savingsIconUse;
    }

    public ImageView getNotificationIcon() {
        return notificationIconUse;
    }
}
