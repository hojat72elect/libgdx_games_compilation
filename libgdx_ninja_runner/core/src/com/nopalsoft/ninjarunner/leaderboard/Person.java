package com.nopalsoft.ninjarunner.leaderboard;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Net.HttpMethods;
import com.badlogic.gdx.Net.HttpRequest;
import com.badlogic.gdx.Net.HttpResponse;
import com.badlogic.gdx.Net.HttpResponseListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Person implements Comparable<Person> {
    final public String id;
    public TipoCuenta tipoCuenta;
    public String name;
    public long score;
    public String urlImagen;
    public TextureRegionDrawable imagen;
    public boolean isMe;// Indica que esta persona es el usuario

    public Person(TipoCuenta tipoCuenta, String id, String name, long oScore, String imagenURL) {
        this.tipoCuenta = tipoCuenta;
        this.id = id;
        this.name = name;
        this.score = oScore;
        this.urlImagen = imagenURL;

    }

    public void downloadImage(final DownloadImageCompleteListener listener) {
        if (imagen != null)//Pa que bajarla otra vez
            return;
        HttpRequest request = new HttpRequest(HttpMethods.GET);
        request.setUrl(urlImagen);
        Gdx.net.sendHttpRequest(request, new HttpResponseListener() {
            @Override
            public void handleHttpResponse(HttpResponse httpResponse) {
                final byte[] bytes = httpResponse.getResult();
                Gdx.app.postRunnable(() -> {
                    com.badlogic.gdx.graphics.Pixmap pixmap = new com.badlogic.gdx.graphics.Pixmap(bytes, 0, bytes.length);
                    com.badlogic.gdx.graphics.Texture texture = new com.badlogic.gdx.graphics.Texture(new com.badlogic.gdx.graphics.glutils.PixmapTextureData(pixmap, pixmap.getFormat(), false, false, true));
                    pixmap.dispose();
                    imagen = new com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable(new com.badlogic.gdx.graphics.g2d.TextureRegion(texture));
                    if (listener != null)
                        listener.imageDownloaded();
                });
            }

            @Override
            public void failed(Throwable t) {
                if (listener != null)
                    listener.imageDownloadFail();
                Gdx.app.log("EmptyDownloadTest", "Failed", t);
            }

            @Override
            public void cancelled() {
                Gdx.app.log("EmptyDownloadTest", "Cancelled");
            }
        });
    }

    // see: http://stackoverflow.com/a/15329259/3479489
    public String getScoreWithFormat() {
        String str = String.valueOf(score);
        int floatPos = str.contains(".") ? str.length() - str.indexOf(".") : 0;
        int nGroups = (str.length() - floatPos - 1 - (str.contains("-") ? 1 : 0)) / 3;
        for (int i = 0; i < nGroups; i++) {
            int commaPos = str.length() - i * 4 - 3 - floatPos;
            str = str.substring(0, commaPos) + "," + str.substring(commaPos);
        }
        return str;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            Person objPerson = (Person) obj;
            return id.equals(objPerson.id) && tipoCuenta == objPerson.tipoCuenta;

        } else
            return false;
    }

    @Override
    public int compareTo(Person o) {
        return Long.compare(o.score, score);
    }

    public void updateDatos(String _name, long _score) {
        name = _name;
        score = _score;

    }

    public enum TipoCuenta {
        GOOGLE_PLAY, AMAZON, FACEBOOK
    }

    public interface DownloadImageCompleteListener {
        void imageDownloaded();

        void imageDownloadFail();
    }

}
