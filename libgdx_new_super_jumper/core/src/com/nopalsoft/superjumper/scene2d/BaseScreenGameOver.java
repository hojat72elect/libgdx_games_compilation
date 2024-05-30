package com.nopalsoft.superjumper.scene2d;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.nopalsoft.superjumper.Assets;
import com.nopalsoft.superjumper.Settings;
import com.nopalsoft.superjumper.game.GameScreen;
import com.nopalsoft.superjumper.game.WorldGame;
import com.nopalsoft.superjumper.screens.MainMenuScreen;
import com.badlogic.gdx.scenes.scene2d.InputEvent;

public class BaseScreenGameOver extends BaseScreen {

	TextButton textButtonMenu, textButtonTryAgain;
	WorldGame worldGame;

	public BaseScreenGameOver(final GameScreen currentScreen) {
		super(currentScreen, 350, 400, 250);
		worldGame = currentScreen.myWorldGame;

		Label labelShop = new Label("Game over!", Assets.labelStyleBig);
		labelShop.setFontScale(1.5f);
		labelShop.setAlignment(Align.center);
		labelShop.setPosition(getWidth() / 2f - labelShop.getWidth() / 2f, 350);
		addActor(labelShop);

		initializeButtons();

		final Table scoreTable = new Table();
		scoreTable.setSize(getWidth(), 130);
		scoreTable.setY(230);

		Label labelScore = new Label("Score", Assets.labelStyleChico);
		labelScore.setAlignment(Align.left);

		Label labelNumScore = new Label(worldGame.maxDistance + "", Assets.labelStyleChico);
		labelNumScore.setAlignment(Align.right);

		Label labelBestScore = new Label("Best Score", Assets.labelStyleChico);
		labelScore.setAlignment(Align.left);

		Label lbBestNumScore = new Label(Settings.getBestScore() + "", Assets.labelStyleChico);
		labelNumScore.setAlignment(Align.right);

		scoreTable.pad(10);
		scoreTable.add(labelScore).left();
		scoreTable.add(labelNumScore).right().expand();

		scoreTable.row();
		scoreTable.add(labelBestScore).left();
		scoreTable.add(lbBestNumScore).right().expand();

		addActor(scoreTable);

		Table content = new Table();

		content.defaults().expandX().uniform().fill();

		content.add(textButtonTryAgain);
		content.row().padTop(20);
		content.add(textButtonMenu);

		content.pack();
		content.setPosition(getWidth() / 2f - content.getWidth() / 2f, 60);

		addActor(content);

	}

	private void initializeButtons() {
		textButtonMenu = new TextButton("Menu", Assets.textButtonStyleBig);
		textButtonMenu.pad(15);

		screen.addEfectoPress(textButtonMenu);
		textButtonMenu.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				hide();
				screen.changeScreenWithFadeOut(MainMenuScreen.class, game);
			}
		});

		textButtonTryAgain = new TextButton("Try again", Assets.textButtonStyleBig);
		textButtonTryAgain.pad(15);

		screen.addEfectoPress(textButtonTryAgain);
		textButtonTryAgain.addListener(new ClickListener() {
			public void clicked(InputEvent event, float x, float y) {
				hide();
				screen.changeScreenWithFadeOut(GameScreen.class, game);
			}
        });
	}

	@Override
	public void show(Stage stage) {
		super.show(stage);
	}

}
