package com.nopalsoft.superjumper.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.nopalsoft.superjumper.Assets;
import com.nopalsoft.superjumper.MainSuperJumper;
import com.nopalsoft.superjumper.Settings;
import com.nopalsoft.superjumper.game.GameScreen;

public class MainMenuScreen extends Screens {

	Image title;

	TextButton buttonShop, buttonPlay, buttonLeaderboard, buttonRate;
	Label labelBestScore;

	public MainMenuScreen(final MainSuperJumper game) {
		super(game);

		title = new Image(Assets.settings);
		title.setPosition(SCREEN_WIDTH / 2f - title.getWidth() / 2f, 800);

		title.addAction(Actions.sequence(Actions.moveTo(title.getX(), 600, 1, Interpolation.bounceOut), Actions.run(() -> stage.addActor(labelBestScore))));

		labelBestScore = new Label("Best score " + Settings.getBestScore(), Assets.labelStyleChico);
		labelBestScore.setPosition(SCREEN_WIDTH / 2f - labelBestScore.getWidth() / 2f, 570);
		labelBestScore.getColor().a = 0;
		labelBestScore.addAction(Actions.alpha(1, .25f));

		buttonPlay = new TextButton("Play", Assets.textButtonStyleBig);
		buttonPlay.setPosition(SCREEN_WIDTH / 2f - buttonPlay.getWidth() / 2f, 440);
		buttonPlay.pad(10);
		buttonPlay.pack();
		addPressEffect(buttonPlay);
		buttonPlay.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				changeScreenWithFadeOut(GameScreen.class, game);
			}
		});

		buttonShop = new TextButton("Shop", Assets.textButtonStyleBig);
		buttonShop.setPosition(SCREEN_WIDTH / 2f - buttonShop.getWidth() / 2f, 340);
		buttonShop.pad(10);
		buttonShop.pack();
		addPressEffect(buttonShop);


		buttonRate = new TextButton("Rate", Assets.textButtonStyleBig);
		buttonRate.setPosition(SCREEN_WIDTH / 2f - buttonRate.getWidth() / 2f, 340);
		buttonRate.pad(10);
		buttonRate.pack();
		addPressEffect(buttonRate);


		buttonLeaderboard = new TextButton("Leaderboard", Assets.textButtonStyleBig);
		buttonLeaderboard.pad(10);
		buttonLeaderboard.pack();
		buttonLeaderboard.setPosition(SCREEN_WIDTH / 2f - buttonLeaderboard.getWidth() / 2f, 240);

		addPressEffect(buttonLeaderboard);

		stage.addActor(title);
		stage.addActor(buttonPlay);
		stage.addActor(buttonRate);
		stage.addActor(buttonLeaderboard);

	}

	@Override
	public void update(float delta) {

	}

	@Override
	public void draw(float delta) {
		batcher.begin();
		batcher.draw(Assets.background, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		batcher.draw(Assets.platformBeigeBroken, 100, 100, 125, 45);
		batcher.draw(Assets.platformBlue, 350, 280, 125, 45);
		batcher.draw(Assets.platformMulticolor, 25, 430, 125, 45);
		batcher.draw(Assets.characterJump, 25, 270, 75, 80);
		batcher.draw(Assets.cloudHappy, 350, 500, 95, 60);
		batcher.end();
	}

	@Override
	public boolean keyDown(int keycode) {
		if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
			Gdx.app.exit();
		}
		return super.keyDown(keycode);
	}

}
