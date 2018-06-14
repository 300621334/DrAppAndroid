package comp231.drbooking;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;

public class ServiceIntro extends IntentService
{

    //MUST add a constructor that takes NO arg. or else Manifest file gives errthat default constructor missing
    public ServiceIntro()
    {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        //set up MediaPlayer
        //MediaPlayer mp = new MediaPlayer();
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.chime);//https://stackoverflow.com/questions/22906068/how-to-get-file-path-of-asset-folder-in-android


        try {
            //mp.setDataSource(path + File.separator + fileName);//https://stackoverflow.com/questions/7291731/how-to-play-audio-file-in-android
            //mp.prepare();
            mp.start();
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
