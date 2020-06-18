package com.jkotheimer.TalkMotion;
import android.speech.tts.TextToSpeech;
import android.app.Activity;
import java.util.Locale;

public class Speaker {

    private TextToSpeech t1;

    public Speaker(Activity a) {
        t1= new TextToSpeech(a.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.US);
                    t1.setSpeechRate(0.93f);
                    t1.setPitch(0.93f);
                }
            }
        });
    }

    /**
     * Ask the text to speech generator to say the given word
     *
     * @param word - The word to be said
     */
    public void saySomething(String word) {
        t1.speak(word, TextToSpeech.QUEUE_FLUSH, null);
    }

    public boolean isPlaying() { return t1.isSpeaking(); }
}