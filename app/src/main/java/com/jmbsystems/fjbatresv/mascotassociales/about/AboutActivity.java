package com.jmbsystems.fjbatresv.mascotassociales.about;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.jmbsystems.fjbatresv.mascotassociales.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Element version = new Element();
        version.setTitle("Versión 1.0");
        View about = new AboutPage(this)
                .isRTL(false)
                .addItem(version)
                .setDescription("Es una aplicación inicial\n" +
                        "\n" +
                        "El concepto es compartir con los dueños de mascotas, ser capaces de conocer gente que siente ese amor por los animales como el que tu sientes.\n" +
                        "\n" +
                        "Por el momento podrás:\n" +
                        "\n" +
                        "1. Publicar tus fotografías\n" +
                        "2. Ver todas las fotografías\n" +
                        "3. Compartir en redes sociales\n" +
                        "4. Ingresar con redes sociales\n" +
                        "5. Ver las fotografías en el mapa\n" +
                        "6. Chatear con toda la comunidad a la vez.")
                .addGroup("Contacto")
                .addGitHub("fjbatresv")
                .addPlayStore("com.jmbsystems.fjbatresv.mascotassociales")
                .addEmail("fjbatresv@gmail.com")
                .create();
        setContentView(about);
    }
}
