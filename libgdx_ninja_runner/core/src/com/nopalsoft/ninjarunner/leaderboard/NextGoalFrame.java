package com.nopalsoft.ninjarunner.leaderboard;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.nopalsoft.ninjarunner.Assets;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

public class NextGoalFrame extends Group {

    public static final float WIDTH = 170;
    public static final float HEIGHT = 80;
    public Person myPerson;
    Label labelName;
    Label labelPersonScore;
    Label labelRemainingPointsToOvercome;


    public NextGoalFrame(float x, float y) {
        setBounds(x, y, WIDTH, HEIGHT);

        labelName = new Label("", Assets.labelStyleSmall);
        labelName.setFontScale(.5f);
        labelName.setPosition(60, 60);

        labelPersonScore = new Label("", Assets.labelStyleSmall);
        labelPersonScore.setFontScale(.5f);
        labelPersonScore.setPosition(60, 40);

        labelRemainingPointsToOvercome = new Label("", Assets.labelStyleSmall);
        labelRemainingPointsToOvercome.setFontScale(.5f);
        labelRemainingPointsToOvercome.setPosition(60, 20);

        addActor(labelName);
        addActor(labelPersonScore);
        addActor(labelRemainingPointsToOvercome);


        debug();
    }

    /**
     * Puts a new person in the frame.
     */
    public void updatePerson(Person person) {
        this.myPerson = person;

        labelName.setText(myPerson.name);
        labelPersonScore.setText(myPerson.getScoreWithFormat());


        if (myPerson.image != null)
            setPicture(myPerson.image);
        else {
            myPerson.downloadImage(new Person.DownloadImageCompleteListener() {
                @Override
                public void imageDownloaded() {
                    setPicture(myPerson.image);
                }

                @Override
                public void imageDownloadFail() {
                    setPicture(Assets.photoNA);
                }
            });
        }

    }

    private void setPicture(TextureRegionDrawable drawable) {
        /*
         * I use an image button because it can have a background and an image.
         */
        ImageButton personImage = new ImageButton(new ImageButton.ImageButtonStyle(drawable, null, null, Assets.photoFrame, null, null));
        personImage.setSize(50, 50);
        personImage.getImageCell().size(50);
        personImage.setPosition(5, HEIGHT / 2f - personImage.getHeight() / 2f);
        addActor(personImage);
    }

    public void updateScore(long score) {
        if (myPerson != null)
            labelRemainingPointsToOvercome.setText("Pa Ganar" + (myPerson.score - score));
    }
}
