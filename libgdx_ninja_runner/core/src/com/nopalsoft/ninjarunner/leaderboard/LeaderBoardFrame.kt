package com.nopalsoft.ninjarunner.leaderboard;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.ninjarunner.Assets;

public class LeaderBoardFrame extends Table {
    Person myPlayer;
    Label labelName;
    Label labelScore;
    Table tableAux;//It is necessary because on the left side there is a photo and on the right
    // side there are several textFields in lines
    /**
     * I use an image button because it can have a background and an image.
     */
    private ImageButton personImage;

    public LeaderBoardFrame(Person player) {
        setBackground(Assets.backgroundItemShop);
        pad(5);
        this.myPlayer = player;


        labelName = new Label(myPlayer.name, Assets.labelStyleSmall);
        labelScore = new Label(myPlayer.getScoreWithFormat(), new Label.LabelStyle(Assets.fontSmall, Color.RED));

        tableAux = new Table();
        tableAux.left();

        tableAux.defaults().left();
        tableAux.add(labelName).row();
        tableAux.add(labelScore).row();

        Image imageSocialNetwork = null;
        switch (myPlayer.accountType) {
            case GOOGLE_PLAY:
                imageSocialNetwork = new Image(Assets.imageGoogle);
                break;
            case AMAZON:
                imageSocialNetwork = new Image(Assets.imageAmazon);
                break;
            case FACEBOOK:
                imageSocialNetwork = new Image(Assets.imageFacebook);
                break;
        }
        tableAux.add(imageSocialNetwork).size(25).row();


        if (myPlayer.image != null)
            setPicture(myPlayer.image);
        else {
            myPlayer.downloadImage(new Person.DownloadImageCompleteListener() {
                @Override
                public void imageDownloaded() {
                    setPicture(myPlayer.image);
                }

                @Override
                public void imageDownloadFail() {
                    setPicture(Assets.photoNA);
                }
            });
        }
        refresh();//Para que ponga la info luego luego. si lo borro hasta que se ponga la photo se pone la info
    }

    public void setPicture(TextureRegionDrawable drawable) {
        personImage = new ImageButton(new ImageButton.ImageButtonStyle(drawable, null, null, Assets.photoFrame, null, null));
        refresh();
    }

    private void refresh() {
        clear();
        float size = 100;
        if (personImage != null) {
            personImage.getImageCell().size(size);
            add(personImage).size(size);
        } else {
            add().size(size);
        }

        add(tableAux).padLeft(20).expandX().fill();


    }

}
