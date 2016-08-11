package nullvoid.shaketorch;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.widget.Toast;

public class FlashService extends Service implements SensorEventListener{

    public static int isServiceActive = 0;
    public  static boolean flag;
    Camera camera;
    ShakeEventManager sem;
    double x = 0;
    boolean sense = false;
    SensorManager sensor_manager;
    Sensor sensor;
    private long lastUpdate = 0;
    private float last_x, last_y, last_z;
    private static final int SHAKE_THRESHOLD = 6000;
    PowerManager mgr;
    PowerManager.WakeLock wakeLock;


    @Override
    public void onDestroy() {

        isServiceActive = 2;
        wakeLock.release();
        sensor_manager.unregisterListener(this);
        if(!flag)
            turnOffFlash();
        Toast.makeText(this,"Service Stopped!",Toast.LENGTH_SHORT).show();

        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        isServiceActive = 1;
        mgr = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
        flag = true;
        wakeLock = mgr.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyWakeLock");
        wakeLock.acquire();
        sensor_manager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensor_manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);

        Toast.makeText(this, "Service Started!", Toast.LENGTH_SHORT).show();


        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    public void turnOnFlash(){

        flag = false;
        camera = Camera.open();
        Camera.Parameters params = camera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        camera.setParameters(params);
        camera.startPreview();
    }

    public void turnOffFlash(){

        flag =true;
        Camera.Parameters params = camera.getParameters();
        params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
        camera.setParameters(params);
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    public void onShake(){

        if(flag)
            turnOnFlash();
        else
            turnOffFlash();

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];
        long curTime = System.currentTimeMillis();

        if ((curTime - lastUpdate) > 10) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;
            float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;
            if (speed > SHAKE_THRESHOLD) {
                onShake();
            }

            last_x = x;
            last_y = y;
            last_z = z;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
