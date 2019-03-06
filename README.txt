Today I have a tale from my past. A few years ago, between finishing university and starting work I had a fair amount of free time which I used to hit the gym in a failed attempt to become Hugh Jackman.

I found myself trying numerous apps and most of them just contained too much “stuff” and all the good features were often behind paywalls. All I wanted was a simple app to track my exercises and maybe show me a graph of my progress. This inspired me to use the rest of my free time to start my own android app.

The app I started was called Gymify (not available on the app store). At the time it was a simple list based application where a user could add routines and then work through exercises in a routine and tick them off.

Once my crossover period between finishing university and starting work had ended I found that I became too busy and progress on the app slowed.

I then went through a period of trying to get myself back into the habit of coding in my spare time, even if its just 30 minutes a night. This allowed me to escape work based problems and also focus on different programming techniques using different technologies than those used at work.

One of the enhancements I made to the app was to introduce the capability to store cardio based activities. For this I wanted to include tracking the amount of time, steps and distance per workout.

This led me to think about how distance could be tracked if, for example, the user was using a treadmill instead of running outside. GPS tracking wouldn’t work well in this situation, as the subject is not really moving geographically at all.

This post will explore how, with a simple library, step tracking can be added to an application. I’ll then show how I used this information to approximate running distances without using GPS.

Tracking Steps using SensorEventListener
You’d think tracking steps with some degree of accuracy would be rather difficult to do, but Google couldn’t have made it easier with the android library SensorEventListener. By simply implementing this listener within a class and overriding the two methods onSensorChanged and onAccuracyChanged you can start tracking steps.

Start by initialising a SensorManager and a Sensor.

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.app.Activity;
public class StepActivity extends Activity implements SensorEventListener{
	SensorManager sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	Sensor stepSensor = sManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
	...
}
The Sensor is initialised as a TYPE_STEP_DETECTOR meaning it will be set up to notice when a step has been taken. The manager will be used to register the sensor as a listener to the activity and deregister it when the activity stops. We can do this by overriding the Activities onResume() and onStop() functions.

@Override
    protected void onResume() {
        super.onResume();
        sManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }
    @Override
    protected void onStop() {
        super.onStop();
        sManager.unregisterListener(this, stepSensor);
    }
Now we have initialised the SensorManager and Sensor and have the Sensor registered as a listener within the activity, we now need to implement the onSensorChanged function that will be triggered by a SensorEvent whenever there is a change to the Sensor we registered, in our case the TYPE_STEP_DETECTOR.

private long steps = 0;
	@Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;
        int value = -1;
        if (values.length > 0) {
            value = (int) values[0];
        }

        if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            steps++;
        }
    }
All I am doing is simply incrementing a variable every time a step is detected. This can then be used however you please, for example updating the text within a TextView in real time and displaying it to the user.

Measuring Distance
Now we have a way of tracking steps, we then want to use that data to determine how far someone has walked/run. As I mentioned in the intro, a person may not always be running over geographical distances if they are using a treadmill for example, therefore tracking a physical distance using GPS wouldn’t be possible. Instead we can use the average step length along with the number of steps taken to give a good estimation of distance.

There are many ways to determine step length: you can measure it yourself; estimate by multiplying your height in centimeters by 0.415 for men and 0.413 for women or if you’re not overly concerned with bang on accuracy you can use the averages 78cm for men and 70cm for women. I found these figures using a quick google search and reading up on the first few pages.

For this example I’m going to use 78cm as the step length, as I am male.

So if we take the number of steps, multiply them by 78 and then divide by 100000 we should arrive at a distance in kilometers. For example, the other day I went for a leisurely run of 6.6 kilometers. According to Google fit I did this in 8,504 steps?—?so (8,504 * 78) / 100000 = 6.633 which is pretty damn close!

//function to determine the distance run in kilometers using average step length for men and number of steps
public float getDistanceRun(long steps){
    float distance = (float)(steps*78)/(float)100000;
    return distance;
}
Wrap up
Hopefully you found this useful. This was something that was really fun for me to play with and try to figure out.

I was then able to build this into my application into an activity that recorded cardio exercises by tracking time spent doing the activity, number of steps, estimated distance and from this you could even guess the calories burnt if you had the users height and weight!




Credit to: Lewis Gavin
Can Refer to this Article:https://medium.com/@lewisdgavin/building-a-cardio-tracking-app-for-android-1f33935f924d