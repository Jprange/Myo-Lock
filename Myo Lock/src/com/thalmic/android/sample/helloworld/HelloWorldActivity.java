/*
 * Copyright (C) 2014 Thalmic Labs Inc.
 * Confidential and not for redistribution. See LICENSE.txt.
 */

package com.thalmic.android.sample.helloworld;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.scanner.ScanActivity;
import com.thalmic.myo.trainer.TrainActivity;

import java.util.ArrayList;

public class HelloWorldActivity extends Activity {


    // This code will be returned in onActivityResult() when the enable Bluetooth activity exits.
    private static final int REQUEST_ENABLE_BT = 1;

   // private TextView mTextView;
    private TextView test;
    private TextView Passwords;   
    private ImageView lockcomb;
    private ArrayList<Boolean> ActivateState = new ArrayList<Boolean>();
    private ArrayList<Boolean> PasswordState = new ArrayList<Boolean>();
    private ArrayList<Boolean> PasswordCheck = new ArrayList<Boolean>();
    
    ArrayList<Integer> passwordGesture = new ArrayList<Integer>();
	ArrayList<Float> passwordOrientation = new ArrayList<Float>();
	ArrayList<Boolean> orientationMatters = new ArrayList<Boolean>();
    
    private boolean PasswordBool;
    private boolean PasswordTrue;
    private boolean setPass = false;
    private boolean exitVal = false;
    int endCounter = 0;
    
    float rotationX;
    float rotationY;
    float rotationZ;
    int counter = 0;
    float defaultOrientation = 0;
    float threshold = 12;
    
    Button start;
    Button finish;
    

    // Classes that inherit from AbstractDeviceListener can be used to receive events from Myo devices.
    // If you do not override an event, the default behavior is to do nothing.
    private DeviceListener mListener = new AbstractDeviceListener() {

        // onConnect() is called whenever a Myo has been connected.
        @Override
        public void onConnect(Myo myo, long timestamp) {
            // Set the text color of the text view to cyan when a Myo connects.
            //mTextView.setTextColor(Color.CYAN);
        }

        // onDisconnect() is called whenever a Myo has been disconnected.
        @Override
        public void onDisconnect(Myo myo, long timestamp) {
            // Set the text color of the text view to red when a Myo disconnects.
            //mTextView.setTextColor(Color.RED);
        }

        // onOrientationData() is called whenever a Myo provides its current orientation,
        // represented as a quaternion.
        @Override
        public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
            if (exitVal)
            {
            		int rotAmount = 5;
            		lockcomb.setRotation((float) (lockcomb.getRotation() + 50 * Math.exp(-0.005 * counter)));
            		counter += rotAmount;
            	
            	
            }
            else 
            {
        	// Calculate Euler angles (roll, pitch, and yaw) from the quaternion.
            rotationZ = (float) Math.toDegrees(Quaternion.roll(rotation));
            rotationX = (float) Math.toDegrees(Quaternion.pitch(rotation));
            rotationY = (float) Math.toDegrees(Quaternion.yaw(rotation));

            // Next, we apply a rotation to the text view using the roll, pitch, and yaw.
            
        	if (PasswordBool)
            {
            lockcomb.setRotation((rotationZ - defaultOrientation)*4.5f);
        	test.setText("Welcome To Myo Lock");
        	test.append("\n" + "1: "+ PasswordState.get(0)+ " 2: "+ PasswordState.get(2)+ " 3: " + PasswordState.get(4));

            }
        	else
        	{
            	test.setText("1: "+ ActivateState.get(0)+ " 2: "+ ActivateState.get(1)+ " 3: " + ActivateState.get(2));

        	}
//            if (!setPass)
//            {
//            test.setText("1: "+ PasswordState.get(0)+ " 2: "+PasswordState.get(2)+" 3: "+PasswordState.get(4) + " Set: " + setPass);
//            }
            //mTextView.setRotationX(rotationX);
            //mTextView.setRotationY(rotationY);
            //mTextView.setText(String.format("Default: %f ", rotationZ));
            }
            
            if (counter > 800)
            {
            	System.exit(0);
            }
        }
        
        public void PasswordSet (int gesture)
        {
        	if (setPass)
        	{
        		//test.setText("Password Recording");
        		if (counter != 6)
        		{
        			if (gesture != 6){
        	passwordGesture.add(gesture);
    		passwordOrientation.add(rotationZ-defaultOrientation);
    		if (gesture != 1)
    		{
    			orientationMatters.add(true);
    		}
    		else
    		{
    			orientationMatters.add(false);
    		}
    		counter++;
    		Passwords.setText(" Gesture: " + gesture + " Rotation: " + (rotationZ-defaultOrientation) + " Adding: " + counter);
        			}
        	}
        		else
        		{
        			setPass = false;
                    Passwords.setText("1: "+ passwordGesture.get(0)+ " 2: "+passwordGesture.get(1)+" 3: "+passwordGesture.get(2) + " 4: " + passwordGesture.get(3)+" 5: "+passwordGesture.get(4));
                    Passwords.append("\n" + "1: "+ passwordOrientation.get(0)+ " 2: "+passwordOrientation.get(1)+" 3: "+passwordOrientation.get(2) + " 4: " + passwordOrientation.get(3)+" 5: "+passwordOrientation.get(4));
                    Passwords.append("\n" + "1: "+ orientationMatters.get(0)+ " 2: "+orientationMatters.get(1)+" 3: "+orientationMatters.get(2) + " 4: " + orientationMatters.get(3)+" 5: "+orientationMatters.get(4));

                    //setPass = false;
                    //counter = 0;
        		}
        	}
        	

        }
        
        public void PasswordActivate(int gesture)
        {
        	//1: None
        	//2: Fist
        	//3: Wave In
        	//4: Wave Out
        	//5: Spread
        	//6: Twist In
if (!setPass)
{
        	//state machine to check password
        	if (ActivateState.get(0) == false && (rotationZ >= defaultOrientation-threshold && rotationZ <= defaultOrientation+threshold))
    		{
        		if (gesture == 2)
            	{
        			ActivateState.set(0, true);
            	}
        		else
        		{
        			for (int i = 0; i < ActivateState.size(); i++)
            		{
        				ActivateState.set(i, false);
            		}
        		}

    		}
        	else if (ActivateState.get(1) == false && ActivateState.get(0))
    		{
        		if (gesture == 1)
            	{
        			ActivateState.set(1, true);
            	}
        		else
        		{
        			for (int i = 0; i < ActivateState.size(); i++)
            		{
        				ActivateState.set(i, false);
            		}
        		}

    		}
        	else if (ActivateState.get(2) == false && (rotationZ >= defaultOrientation+80-threshold &&rotationZ <= defaultOrientation+80+threshold))
    		{
        		if (gesture == 5)
            	{
        			ActivateState.set(2, true);
            	}
        		else
        		{
        			for (int i = 0; i < ActivateState.size(); i++)
            		{
        				ActivateState.set(i, false);
            		}
        		}

    		}
//        	if (!PasswordBool){
//            Passwords.setText("1: "+ ActivateState.get(0)+ " 2: "+ActivateState.get(1)+" 3: "+ActivateState.get(2));
//        	

        	//check if password is true
        	if (ActivateState.get(2))
        	{
                test.setText("Password Activated");
                PasswordBool = true;
        	}
//        	}
//            test.setText("1: "+ ActivateState.get(0)+ " 2: "+ActivateState.get(1)+" 3: "+ActivateState.get(2) + " Set: " + setPass);

}
        }
        
        public void exit() {
        	exitVal = true;
        }
        
        //hardcoded original password
        public void check(int gesture)
        {
        	//when password set is called, after every gesture add a none gesture. Also, i'll hand it passwordcheck, and i want it to
        	//intialize it and add the right number of elements
        	//boolean to show if password has been changed
        	
        	if (PasswordBool && !setPass){
        		outerloop:
        	while (counter < passwordGesture.size())
        	{
        		if (PasswordCheck.get(counter) == false )
        		{
        			if (orientationMatters.get(counter))
        			{
            		if (gesture == passwordGesture.get(counter)&& (rotationZ >= (passwordOrientation.get(counter)+defaultOrientation-threshold) && rotationZ <= (passwordOrientation.get(counter)+defaultOrientation+threshold)))
                	{
            			PasswordCheck.set(counter, true);
                		counter++;
                		break outerloop;
                	}
            		else
            		{
            			for (int i = 0; i < passwordGesture.size(); i++)
                		{
            				PasswordCheck.set(i, false);
            				
                		}
            			counter = 0;
            			break outerloop;
            		}
        			}
        			else
        			{
        				if (gesture == passwordGesture.get(counter))
                    	{
                			PasswordCheck.set(counter, true);
                    		counter++;
                    		break outerloop;
                    	}
                		else
                		{
                			for (int i = 0; i < passwordGesture.size(); i++)
                    		{
                				PasswordCheck.set(i, false);
                				
                    		}
                			counter = 0;

                			break outerloop;
                		}
        			}

        		}
        	}
        	//Passwords.setText("1: "+ PasswordCheck.get(0)+ " 2: "+PasswordCheck.get(1)+" 3: "+PasswordCheck.get(2) + counter);
        	//check if password is true
        	if (PasswordCheck.get(PasswordCheck.size()-1))
        	{
                test.setText("Password Correct");
                PasswordTrue = true;
                exit();
        	}
        	}
        }
        //works with any 3 gestures and rotation
        public void checkPass(int gesture)
        {
        	//1: None
        	//2: Fist
        	//3: Wave In
        	//4: Wave Out
        	//5: Spread
        	//6: Twist In

        	//state machine to check password
        	if (PasswordBool && !setPass){
        	if (PasswordState.get(0) == false && (rotationZ >= (passwordOrientation.get(0)+defaultOrientation-threshold) && rotationZ <= (passwordOrientation.get(0)+defaultOrientation+threshold)))
    		{
        		if (gesture == passwordGesture.get(0))
            	{
        			PasswordState.set(0, true);
            	}
        		else
        		{
        			for (int i = 0; i < PasswordState.size(); i++)
            		{
        				PasswordState.set(i, false);
            		}
        		}

    		}
        	else if (PasswordState.get(1) == false  )
    		{
        		if (gesture == 1)
            	{
        			PasswordState.set(1, true);
            	}
        		else
        		{
        			for (int i = 0; i < PasswordState.size(); i++)
            		{
        				PasswordState.set(i, false);
            		}
        		}

    		}
        	else if (PasswordState.get(2) == false  && (rotationZ >= (passwordOrientation.get(2)+defaultOrientation-threshold) && rotationZ <= (passwordOrientation.get(2)+defaultOrientation+threshold)))
    		{
        		if (gesture == passwordGesture.get(2))
            	{
        			PasswordState.set(2, true);
            	}
        		else
        		{
        			for (int i = 0; i < PasswordState.size(); i++)
            		{
        				PasswordState.set(i, false);
            		}
        		}

    		}
        	else if (PasswordState.get(3) == false  )
    		{
        		if (gesture == 1)
            	{
        			PasswordState.set(3, true);
            	}
        		else
        		{
        			for (int i = 0; i < PasswordState.size(); i++)
            		{
        				PasswordState.set(i, false);
            		}
        		}

    		}
        	else if (PasswordState.get(4) == false  && (rotationZ >= (passwordOrientation.get(4)+defaultOrientation-threshold) && rotationZ <= (passwordOrientation.get(4)+defaultOrientation+threshold)))
    		{
        		if (gesture == passwordGesture.get(4))
            	{
        			PasswordState.set(4, true);
            	}
        		else
        		{
        			for (int i = 0; i < PasswordState.size(); i++)
            		{
        				PasswordState.set(i, false);
            		}
        		}

    		}
        	
                //Passwords.setText("1: "+ PasswordState.get(0)+ " 2: "+PasswordState.get(2)+" 3: "+PasswordState.get(4));
            	
        	//check if password is true
        	if (PasswordState.get(4))
        	{
                test.setText("Password Correct");
                PasswordTrue = true;
                exit();
        	}
        	
        }
        }
        
      //works with any gestures and rotation
        public void checkPassword(int gesture) {
        	//1: None
        	//2: Fist
        	//3: Wave In
        	//4: Wave Out
        	//5: Spread
        	//6: Twist In

        	//state machine to check password
        	if (PasswordBool && !setPass){
        	if (PasswordState.get(0) == false && (rotationZ >= defaultOrientation+100-threshold &&rotationZ <= defaultOrientation+100+threshold))
    		{
        		if (gesture == 5)
            	{
        			PasswordState.set(0, true);
            	}
        		else
        		{
        			for (int i = 0; i < PasswordState.size(); i++)
            		{
        				PasswordState.set(i, false);
            		}
        		}

    		}
        	else if (PasswordState.get(1) == false  )
    		{
        		if (gesture == 1)
            	{
        			PasswordState.set(1, true);
            	}
        		else
        		{
        			for (int i = 0; i < PasswordState.size(); i++)
            		{
        				PasswordState.set(i, false);
            		}
        		}

    		}
        	else if (PasswordState.get(2) == false  && (rotationZ >= defaultOrientation-threshold &&rotationZ <= defaultOrientation+threshold))
    		{
        		if (gesture == 2)
            	{
        			PasswordState.set(2, true);
            	}
        		else
        		{
        			for (int i = 0; i < PasswordState.size(); i++)
            		{
        				PasswordState.set(i, false);
            		}
        		}

    		}
        	else if (PasswordState.get(3) == false  )
    		{
        		if (gesture == 1)
            	{
        			PasswordState.set(3, true);
            	}
        		else
        		{
        			for (int i = 0; i < PasswordState.size(); i++)
            		{
        				PasswordState.set(i, false);
            		}
        		}

    		}
        	else if (PasswordState.get(4) == false  && (rotationZ >= defaultOrientation-threshold &&rotationZ <= defaultOrientation+threshold))
    		{
        		if (gesture == 4)
            	{
        			PasswordState.set(4, true);
            	}
        		else
        		{
        			for (int i = 0; i < PasswordState.size(); i++)
            		{
        				PasswordState.set(i, false);
            		}
        		}

    		}
        	
            	
        	//check if password is true
        	if (PasswordState.get(4))
        	{
                
                PasswordTrue = true;
                exit();
        	}
        	
        }
        }

        // onPose() is called whenever a Myo provides a new pose.
        @Override
        public void onPose(Myo myo, long timestamp, Pose pose) {
            // Handle the cases of the Pose.Type enumeration, and change the text of the text view
            // based on the pose we receive.

            switch (pose.getType()) {
                case NONE:
                    //mTextView.setText(getString(R.string.hello_world));
                	lockcomb.setImageDrawable(getResources().getDrawable(R.drawable.lock01));
                    //test.append("None \n");
                    PasswordActivate(1);
                    //checkPassword(1);
                    checkPass(1);
                    PasswordSet(1);
                    break;
                case FIST:
                    //mTextView.setText(getString(R.string.myosdk__pose_fist));
                	lockcomb.setImageDrawable(getResources().getDrawable(R.drawable.lock01));
                    //test.append("Fist \n");
                    PasswordActivate(2);
                    //checkPassword(2);
                    checkPass(2);
                    PasswordSet(2);
                    break;
                case WAVE_IN:
                    //mTextView.setText(getString(R.string.myosdk__pose_wavein));
                	lockcomb.setImageDrawable(getResources().getDrawable(R.drawable.lock01));
                    //test.append("Wave In \n");
                    PasswordActivate(3);
                    //checkPassword(3);
                    checkPass(3);
                    PasswordSet(3);
                    break;
                case WAVE_OUT:
                    //mTextView.setText(getString(R.string.myosdk__pose_waveout));
                	lockcomb.setImageDrawable(getResources().getDrawable(R.drawable.lock01));
                    //test.append("Wave Out \n");
                    PasswordActivate(4);
                    //checkPassword(4);
                    checkPass(4);
                    PasswordSet(4);
                    break;
                case FINGERS_SPREAD:
                    //mTextView.setText(getString(R.string.myosdk__pose_fingersspread));
                	lockcomb.setImageDrawable(getResources().getDrawable(R.drawable.lock01));
                    //test.append("Spread \n");
                    PasswordActivate(5);
                    //checkPassword(5);
                    checkPass(5);
                    PasswordSet(5);
                    break;
                case TWIST_IN:
                    //mTextView.setText(getString(R.string.myosdk__pose_twistin));
                	lockcomb.setImageDrawable(getResources().getDrawable(R.drawable.lock01));
                    //test.append("Twist In \n");
                    PasswordActivate(6);
                    //checkPassword(6);
                    checkPass(6);
                    PasswordSet(6);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_world);
        
        
  
        lockcomb = (ImageView) findViewById(R.id.lockpic);
        test = (TextView) findViewById(R.id.test);
        Passwords = (TextView) findViewById(R.id.Password);
        Passwords.setText("");
    	//create my list of stuff
        ActivateState.add(false);
        ActivateState.add(false);
        ActivateState.add(false);
        
        PasswordState.add(false);
    	PasswordState.add(false);
    	PasswordState.add(false);
    	PasswordState.add(false);
    	PasswordState.add(false);
    	
    	PasswordCheck.add(false);
    	PasswordCheck.add(false);
    	PasswordCheck.add(false);
    	PasswordCheck.add(false);
    	PasswordCheck.add(false);
    	
    	//1: None
    	//2: Fist
    	//3: Wave In
    	//4: Wave Out
    	//5: Spread
    	//6: Twist In
    	passwordGesture.add(2);
    	passwordGesture.add(1);
    	passwordGesture.add(5);
    	passwordGesture.add(1);
    	passwordGesture.add(2);
    	passwordOrientation.add(48f);
    	passwordOrientation.add(0f);
    	passwordOrientation.add(16f);
    	passwordOrientation.add(0f);
    	passwordOrientation.add(35f);
    	orientationMatters.add(true);
    	orientationMatters.add(false);
    	orientationMatters.add(true);
    	orientationMatters.add(false);
    	orientationMatters.add(true);

    	


     	
     	start = (Button) findViewById(R.id.start);
        start.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
            	setPass = true;
            	counter = 0;
            	passwordGesture.clear();
            	passwordOrientation.clear();
            	orientationMatters.clear();
            }
        });
        

        
        // First, we initialize the Hub singleton.
        Hub hub = Hub.getInstance();
        if (!hub.init(this)) {
            // We can't do anything with the Myo device if the Hub can't be initialized, so exit.
            Toast.makeText(this, "Couldn't initialize Hub", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Next, register for DeviceListener callbacks.
        hub.addListener(mListener);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // If Bluetooth is not enabled, request to turn it on.
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // We don't want any callbacks when the Activity is gone, so unregister the listener.
        Hub.getInstance().removeListener(mListener);

        if (isFinishing()) {
            // The Activity is finishing, so shutdown the Hub. This will disconnect from the Myo.
            Hub.getInstance().shutdown();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // User chose not to enable Bluetooth, so exit.
        if (requestCode == REQUEST_ENABLE_BT && resultCode == Activity.RESULT_CANCELED) {
            finish();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (R.id.action_train == id) {
            onTrainActionSelected();
            return true;
        } else if (R.id.action_scan == id) {
            onScanActionSelected();
            return true;
        }
        else if (R.id.Reset == id) {
            reset();
            return true;
        }
        else if (R.id.Calibrate == id) {
        	defaultOrientation = rotationZ;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void reset ()
    {
        test.setText("");
        //mTextView.setText("Password Idle");
        PasswordBool = false;
        PasswordTrue = false;


        for (int i = 0; i < PasswordState.size(); i++)
		{
			PasswordState.set(i, false);
		}
        for (int i = 0; i < ActivateState.size(); i++)
		{
        	ActivateState.set(i, false);
		}

    }
    
    private void onTrainActionSelected() {
        // Check if there are any connected Myos. Don't do anything if there aren't any.
        ArrayList<Myo> connectedDevices = Hub.getInstance().getConnectedDevices();
        if (connectedDevices.isEmpty()) {
            return;
        }

        // Get the Myo to train. In this case, we will train the first Myo in the Hub's
        // connected devices list.
        Myo myo = connectedDevices.get(0);

        // Launch the TrainActivity for the specified Myo.
        Intent intent = new Intent(this, TrainActivity.class);
        intent.putExtra(TrainActivity.EXTRA_ADDRESS, myo.getMacAddress());
        startActivity(intent);
    }

    private void onScanActionSelected() {
        // Launch the ScanActivity to scan for Myos to connect to.
        Intent intent = new Intent(this, ScanActivity.class);
        startActivity(intent);
    }
}
