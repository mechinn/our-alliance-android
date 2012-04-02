package com.mechinn.android.ouralliance.activity;

import java.io.File;
import java.util.HashSet;

import com.bugsense.trace.BugSenseHandler;
import com.mechinn.android.ouralliance.R;
import com.mechinn.android.ouralliance.data.TeamScoutingInterface;
import com.mechinn.android.ouralliance.providers.TeamScoutingProvider;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class Info extends Activity {
    private static final int CAMERA_PIC_REQUEST = 869;
    private final String logTag = "Info";

	private AlertDialog takePicAlert;
	private AlertDialog delPicAlert;
	
	
	private TeamScoutingInterface teamInfo;
	private Uri mImageUri;
	private File pic;
	private File picDir;
	
	int team;
	String orientation;
	int numWheels;
	int wheelTypes;
	boolean deadWheel;
	String wheel1Type;
	double wheel1Diameter;
	String wheel2Type;
	double wheel2Diameter;
	String deadWheelType;
	boolean turret;
	boolean tracking;
	boolean fender;
	boolean key;
	boolean barrier;
	boolean climb;
	String notes;
	boolean auto;
	double hoops;
	double balance;
	double broke;
	Bitmap teamPic;
	
	private TextView teamNumber;
	private ImageView image;
	private Button takePic;
	private Button deletePic;
	private RadioButton orientationLong;
	private RadioButton orientationWide;
	private RadioButton orientationSquare;
	private RadioButton orientationOther;
	private Spinner wheelSpinner;
	private RadioButton has1WheelTypes;
	private RadioButton has2WheelTypes;
	private CheckBox hasDeadWheel;
//	private TableRow wheel1;
	private Spinner wheel1TypeSpinner;
	private EditText wheel1DiameterText;
	private TableRow wheel2;
	private Spinner wheel2TypeSpinner;
	private EditText wheel2DiameterText;
	private TableRow deadWheels;
	private Spinner deadWheelTypeSpinner;
	private CheckBox turretShooter;
	private CheckBox autoTracking;
	private CheckBox fenderShooter;
	private CheckBox keyShooter;
	private CheckBox crossesBarrier;
	private CheckBox climbBridge;
	private CheckBox autonomous;
	private Button pickComp;
	private TextView avgHoops;
	private TextView avgBalance;
	private TextView avgBroke;
	private EditText edittext;
	private Button save;
	private Button discard;
	
	private AlertDialog pickCompetitionsDialog;
	private AlertDialog.Builder pickCompetitionsDialogBuilder;
	private final boolean[] compsSelected = {false};
	private HashSet<String> competitionsSelected;
	
	private ArrayAdapter<CharSequence> wheels;
	private ArrayAdapter<CharSequence> wheelTypeStrings;
	
	private boolean breakNicely;
	
	private void takePic() {
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
	    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);
	    //start camera intent
	    Info.this.startActivityForResult(intent, CAMERA_PIC_REQUEST);
	}
    
    private OnClickListener orientationListener = new RadioButton.OnClickListener() {
        public void onClick(View v) {
            // Perform action on clicks
            RadioButton rb = (RadioButton) v;
        	orientation = rb.getText().toString();
        }
    };
    
    private OnClickListener wheelTypeListener = new RadioButton.OnClickListener() {
        public void onClick(View v) {
            // Perform action on clicks
            RadioButton rb = (RadioButton) v;
            wheelTypes = Integer.parseInt(rb.getText().toString());
            if(wheelTypes > 1) {
            	wheel2.setVisibility(View.VISIBLE);
            } else {
            	wheel2.setVisibility(View.GONE);
            }
        }
    };
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        if (requestCode == CAMERA_PIC_REQUEST && resultCode==RESULT_OK) {
        	this.grabImage();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    private void grabImage() {
        this.getContentResolver().notifyChange(mImageUri, null);
        ContentResolver cr = this.getContentResolver();
        Bitmap bitmap;
        try {
            bitmap = android.provider.MediaStore.Images.Media.getBitmap(cr, mImageUri);
            
            int thumbWidth = 300;//Specify image width in px
            int thumbHeight = 300;//Specify image height in px
             
            int imageWidth = bitmap.getWidth();//get image Widht
            int imageHeight = bitmap.getHeight();//get image Height 
             
            double thumbRatio = (double)thumbWidth/(double)thumbHeight;
            double imageRatio = (double)imageWidth/(double)imageHeight;
             
            //This calculation is used to convert the image size according to the pixels mentioned above
            if(thumbRatio<imageRatio) {
            	thumbHeight = (int) (thumbWidth/imageRatio);
            } else {
            	thumbWidth = (int) (thumbHeight*imageRatio);
            }

            // create a matrix for the manipulation
            Matrix matrix = new Matrix();

            // resize the bit map
            matrix.postScale(thumbWidth, thumbHeight);

            // recreate the new Bitmap
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, thumbWidth, thumbHeight, true);
            image.setImageBitmap(resizedBitmap);
            image.setOnClickListener(new OnClickListener() {
            	public void onClick(View v) {
					Intent intent = new Intent();
		            intent.setAction(android.content.Intent.ACTION_VIEW);
		            intent.setDataAndType(mImageUri, "image/jpg");
		            Info.this.startActivity(intent);
				}
            });
            deletePic.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            Log.d("grabimage", "Failed to load", e);
        }
    }
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info);
        breakNicely = false;
        competitionsSelected = new HashSet<String>();
        pickCompetitionsDialogBuilder = new AlertDialog.Builder(this);
        pickCompetitionsDialogBuilder.setTitle("Pick competitions this team is at.");
        pickCompetitionsDialogBuilder.setMultiChoiceItems(TeamScoutingProvider.competitions, compsSelected, new OnMultiChoiceClickListener() {
        	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        		if(isChecked) {
        			if(!competitionsSelected.contains(TeamScoutingProvider.competitions[which])) {
            			competitionsSelected.add(TeamScoutingProvider.competitions[which]);
            		}
        		} else {
        			if(competitionsSelected.contains(TeamScoutingProvider.competitions[which])) {
        				competitionsSelected.remove(TeamScoutingProvider.competitions[which]);
            		}
        		}
			}
        });
        
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        
        teamInfo = new TeamScoutingInterface(this);

		teamNumber = (TextView) findViewById(R.id.teamNumber);

        image = (ImageView) findViewById(R.id.teamPic);
        
        takePic = (Button) findViewById(R.id.takePicButton);
        
        deletePic = (Button) findViewById(R.id.deletePicButton);
        deletePic.setVisibility(View.GONE);
        
        orientationLong = (RadioButton) findViewById(R.id.orientationLong);
        orientationLong.setOnClickListener(orientationListener);
        
        orientationWide = (RadioButton) findViewById(R.id.orientationWide);
        orientationWide.setOnClickListener(orientationListener);
        
        orientationSquare = (RadioButton) findViewById(R.id.orientationSquare);
        orientationSquare.setOnClickListener(orientationListener);
        
        orientationOther = (RadioButton) findViewById(R.id.orientationOther);
        orientationOther.setOnClickListener(orientationListener);
        
        wheelSpinner = (Spinner) findViewById(R.id.wheelSpinner);
        wheels = ArrayAdapter.createFromResource(this, R.array.wheels, android.R.layout.simple_spinner_item);
        wheels.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wheelSpinner.setAdapter(wheels);
        wheelSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	String numWheelsString = (parent.getItemAtPosition(pos).toString());
            	if(numWheelsString.equals("Treads")) {
            		numWheels = -1;
            	} else {
            		numWheels = Integer.parseInt(numWheelsString);
            	}
            }
            public void onNothingSelected(AdapterView<?> parent) {
              // Do nothing.
            }
        });
        
        has1WheelTypes = (RadioButton) findViewById(R.id.has1WheelTypes);
        has1WheelTypes.setOnClickListener(wheelTypeListener);
        
        has2WheelTypes = (RadioButton) findViewById(R.id.has2WheelTypes);
        has2WheelTypes.setOnClickListener(wheelTypeListener);
        
        hasDeadWheel = (CheckBox) findViewById(R.id.hasDeadWheel);
        hasDeadWheel.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	deadWheel = ((CheckBox) v).isChecked();
            	if(deadWheel) {
            		deadWheels.setVisibility(View.VISIBLE);
                } else {
                	deadWheels.setVisibility(View.GONE);
                }
            }
        });

        wheelTypeStrings = ArrayAdapter.createFromResource(this, R.array.wheelTypes, android.R.layout.simple_spinner_item);
        wheelTypeStrings.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
//        wheel1 = (TableRow) findViewById(R.id.wheel1);
        
        wheel1TypeSpinner = (Spinner) findViewById(R.id.wheel1Type);
        wheel1TypeSpinner.setAdapter(wheelTypeStrings);
        wheel1TypeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	wheel1Type = (parent.getItemAtPosition(pos).toString());
            }
            public void onNothingSelected(AdapterView<?> parent) {
              // Do nothing.
            }
        });
        
        wheel1DiameterText = (EditText) findViewById(R.id.wheel1Diameter);
        
        wheel2 = (TableRow) findViewById(R.id.wheel2);
        
        wheel2TypeSpinner = (Spinner) findViewById(R.id.wheel2Type);
        wheel2TypeSpinner.setAdapter(wheelTypeStrings);
        wheel2TypeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	wheel2Type = (parent.getItemAtPosition(pos).toString());
            }
            public void onNothingSelected(AdapterView<?> parent) {
              // Do nothing.
            }
        });
        
        wheel2DiameterText = (EditText) findViewById(R.id.wheel2Diameter);
        
        deadWheels = (TableRow) findViewById(R.id.deadWheels);
        
        deadWheelTypeSpinner = (Spinner) findViewById(R.id.deadWheelType);
        ArrayAdapter<CharSequence> deadWheelTypes = ArrayAdapter.createFromResource(this, R.array.deadWheelTypes, android.R.layout.simple_spinner_item);
        deadWheelTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deadWheelTypeSpinner.setAdapter(deadWheelTypes);
        deadWheelTypeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            	deadWheelType = (parent.getItemAtPosition(pos).toString());
            }
            public void onNothingSelected(AdapterView<?> parent) {
              // Do nothing.
            }
        });
        
        turretShooter = (CheckBox) findViewById(R.id.turretShooter);
        turretShooter.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	turret = ((CheckBox) v).isChecked();
            }
        });
        
        autoTracking = (CheckBox) findViewById(R.id.autoTracking);
        autoTracking.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	tracking = ((CheckBox) v).isChecked();
            }
        });
        
        fenderShooter = (CheckBox) findViewById(R.id.fenderShooter);
        fenderShooter.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	fender = ((CheckBox) v).isChecked();
            }
        });
        
        keyShooter = (CheckBox) findViewById(R.id.keyShooter);
        keyShooter.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	key = ((CheckBox) v).isChecked();
            }
        });
        
        crossesBarrier = (CheckBox) findViewById(R.id.crossesBarrier);
        crossesBarrier.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	barrier = ((CheckBox) v).isChecked();
            }
        });
        
        climbBridge = (CheckBox) findViewById(R.id.climbBridge);
        climbBridge.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	climb = ((CheckBox) v).isChecked();
            }
        });
        
        autonomous = (CheckBox) findViewById(R.id.autonomous);
        autonomous.setOnClickListener(new CheckBox.OnClickListener() {
            public void onClick(View v) {
            	auto = ((CheckBox) v).isChecked();
            }
        });
        
        avgHoops = (TextView) findViewById(R.id.avgHoops);
        avgBalance = (TextView) findViewById(R.id.avgBalance);
        avgBroke = (TextView) findViewById(R.id.avgBroke);
        
        pickCompetitionsDialog = pickCompetitionsDialogBuilder.create();
        
        pickComp = (Button) findViewById(R.id.pickComp);
        pickComp.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	pickCompetitionsDialog.show();
            }
        });
        
        edittext = (EditText) findViewById(R.id.notes);
        
        save = (Button) findViewById(R.id.save);
        
        discard = (Button) findViewById(R.id.discard);
        discard.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	//just exit without saving
                finish();
            }
        });
        
        new getInfo().execute(this);
    }
    
    private class getInfo extends AsyncTask<Context,Void,Context> {
    	
    	protected void onPostExecute(Context con) {
    		if(breakNicely){
	        	BugSenseHandler.log(logTag, new Exception("no team"));
				Toast.makeText(Info.this, "Something went wrong loading data, we notified the developer", Toast.LENGTH_SHORT).show();
				Info.this.finish();
    			return;
    		}
            teamNumber.setText(Integer.toString(team));
            takePic.setOnClickListener(new OnClickListener() {
            	public void onClick(View v) {
            		if(pic.exists()) {
            			takePicAlert = new AlertDialog.Builder(Info.this)
    	                .setTitle("Really overwrite the team picture?")
    	                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
    	                    public void onClick(DialogInterface dialog, int whichButton) {
    	                    	takePic();
    	                    }
    	                })
    	                .setNegativeButton("No", new DialogInterface.OnClickListener() {
    	                    public void onClick(DialogInterface dialog, int whichButton) {
    	                    	takePicAlert.dismiss();
    	                    }
    	                }).show();
            		} else {
            			takePic();
            		}
    			}
            });
    		grabImage();
    		deletePic.setOnClickListener(new OnClickListener() {
            	public void onClick(View v) {
            		if(pic.exists()) {
            			delPicAlert = new AlertDialog.Builder(Info.this)
                        .setTitle("Really delete the team picture?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            	pic.delete();
                        		image.setImageResource(R.drawable.ouralliance);
                        		deletePic.setVisibility(View.GONE);
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            	delPicAlert.dismiss();
                            }
                        }).show();
            		}
    			}
            });
            
            if(pic.exists()) {
    			deletePic.setVisibility(View.VISIBLE);
    		}
            if(orientation.equals(con.getString(R.string.orientationLong))){
            	orientationLong.toggle();
            } else if(orientation.equals(con.getString(R.string.orientationWide))){
            	orientationWide.toggle();
            } else if(orientation.equals(con.getString(R.string.orientationSquare))){
            	orientationSquare.toggle();
            } else if(orientation.equals(con.getString(R.string.orientationOther))){
            	orientationOther.toggle();
            }
            if(numWheels==-1) {
            	wheelSpinner.setSelection(wheels.getPosition("Treads"));
            } else {
            	wheelSpinner.setSelection(wheels.getPosition(Integer.toString(numWheels)));
            }
            if(wheelTypes==2) {
            	has2WheelTypes.toggle();
            } else {
            	has1WheelTypes.toggle();
            }
            hasDeadWheel.setChecked(deadWheel);
            wheel1TypeSpinner.setSelection(wheelTypeStrings.getPosition(wheel1Type));
            wheel1DiameterText.setText(Double.toString(wheel1Diameter));
            wheel2TypeSpinner.setSelection(wheelTypeStrings.getPosition(wheel2Type));
            wheel2DiameterText.setText(Double.toString(wheel2Diameter));
            if(wheelTypes < 2) {
    	    	wheel2.setVisibility(View.GONE);
    	    }
        	deadWheelTypeSpinner.setSelection(wheelTypeStrings.getPosition(deadWheelType));
            if(!deadWheel) {
    	    	deadWheels.setVisibility(View.GONE);
    	    }
            turretShooter.setChecked(turret);
            autoTracking.setChecked(tracking);
            fenderShooter.setChecked(fender);
            keyShooter.setChecked(key);
            crossesBarrier.setChecked(barrier);
            climbBridge.setChecked(climb);
            autonomous.setChecked(auto);
            avgHoops.setText(Double.toString(hoops));
            avgBalance.setText(Double.toString(balance));
            avgBroke.setText(Double.toString(broke));
            edittext.setText(notes);
            
            save.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Log.d("team", Integer.toString(team));
                    Log.d("orientation", orientation);
                    Log.d("numWheels", Integer.toString(numWheels));
                    Log.d("wheelTypes", Integer.toString(wheelTypes));
                    Log.d("deadWheel", Boolean.toString(deadWheel));
                    if(wheelTypes > 0){
    	                String wheel1DiameterTextCheck = wheel1DiameterText.getText().toString();
    	                if(!wheel1DiameterTextCheck.equals("")) {
    	                	wheel1Diameter = Double.parseDouble(wheel1DiameterTextCheck);
    	                } else {
    	                	wheel1Diameter = 0;
    	                }
                    } else {
                    	wheel1Type = "None";
                    	wheel1Diameter = 0;
                    }
                    Log.d("wheel1Type", wheel1Type);
                	Log.d("wheel1Diameter", Double.toString(wheel1Diameter));
                    if(wheelTypes > 1){
    	                String wheel2DiameterTextCheck = wheel2DiameterText.getText().toString();
    	                if(!wheel2DiameterTextCheck.equals("")) {
    	                	wheel2Diameter = Double.parseDouble(wheel2DiameterTextCheck);
    	                } else {
    	                	wheel2Diameter = 0;
    	                }
                    } else {
                    	wheel2Type = "None";
                    	wheel2Diameter = 0;
                    }
                    Log.d("wheel2Type", wheel1Type);
                	Log.d("wheel2Diameter", Double.toString(wheel2Diameter));
                    if(!deadWheel){
                    	deadWheelType = "None";
                    }
                    Log.d("deadWheelType", deadWheelType);
                    Log.d("turret", Boolean.toString(turret));
                    Log.d("tracking", Boolean.toString(tracking));
                    Log.d("fender", Boolean.toString(fender));
                    Log.d("key", Boolean.toString(key));
                    Log.d("barrier", Boolean.toString(barrier));
                    Log.d("climb", Boolean.toString(climb));
                    Log.d("auto", Boolean.toString(auto));
                    notes = edittext.getText().toString();
                    Log.d("notes", notes);
                    
                    teamInfo.updateTeam(team, orientation, numWheels, wheelTypes, 
                			deadWheel, wheel1Type, wheel1Diameter, wheel2Type, wheel2Diameter, 
                			deadWheelType, turret, tracking, fender, key, barrier, climb, notes, 
                			auto, competitionsSelected, hoops, balance, broke);
                    Toast.makeText(Info.this, "Saved "+Integer.toString(team), Toast.LENGTH_SHORT).show();
                }
            });
    	}
    	
		protected Context doInBackground(Context... con) {
			team = getIntent().getIntExtra("team", 0);
	        picDir = Info.this.getExternalFilesDir(null);
	        picDir=new File(picDir.getAbsolutePath()+"/teamPic/2012/");
	        if(!picDir.exists()) {
	        	picDir.mkdirs();
	        }
	        pic=new File(picDir.getAbsolutePath()+"/"+Integer.toString(team)+".jpg");
	        mImageUri = Uri.fromFile(pic);
	        Log.d("imageUri",mImageUri.getPath());
	        Cursor thisTeam = teamInfo.fetchTeam(team);
	        if(thisTeam!=null && thisTeam.getCount()>0){
		        //get variables from DB
		        for(int i=0;i<thisTeam.getColumnCount();++i) {
		        	String colName = thisTeam.getColumnName(i);
		        	Log.d("column",colName);
		        	if(colName.equals(TeamScoutingProvider.keyOrientation)){
		        		orientation = thisTeam.getString(i);
		        	} else if (colName.equals(TeamScoutingProvider.keyNumWheels)) {
		        		numWheels = thisTeam.getInt(i);
		        	} else if (colName.equals(TeamScoutingProvider.keyWheelTypes)) {
		        		wheelTypes = thisTeam.getInt(i);
		        	} else if (colName.equals(TeamScoutingProvider.keyDeadWheel)) {
		        		if(thisTeam.getInt(i)==0) {
		            		deadWheel = false;
		            	} else {
		            		deadWheel = true;
		            	}
		        	} else if (colName.equals(TeamScoutingProvider.keyWheel1Type)) {
		        		wheel1Type = thisTeam.getString(i);
		        	} else if (colName.equals(TeamScoutingProvider.keyWheel1Diameter)) {
		        		wheel1Diameter = thisTeam.getInt(i);
		        	} else if (colName.equals(TeamScoutingProvider.keyWheel2Type)) {
		        		wheel2Type = thisTeam.getString(i);
		        	} else if (colName.equals(TeamScoutingProvider.keyWheel2Diameter)) {
		        		wheel2Diameter = thisTeam.getInt(i);
		        	} else if (colName.equals(TeamScoutingProvider.keyDeadWheelType)) {
		        		deadWheelType = thisTeam.getString(i);
		        	} else if (colName.equals(TeamScoutingProvider.keyTurret)) {
		        		if(thisTeam.getInt(i)==0) {
		            		turret = false;
		            	} else {
		            		turret = true;
		            	}
		        	} else if (colName.equals(TeamScoutingProvider.keyTracking)) {
		        		if(thisTeam.getInt(i)==0) {
		            		tracking = false;
		            	} else {
		            		tracking = true;
		            	}
		        	} else if (colName.equals(TeamScoutingProvider.keyFenderShooter)) {
		        		if(thisTeam.getInt(i)==0) {
		            		fender = false;
		            	} else {
		            		fender = true;
		            	}
		        	} else if (colName.equals(TeamScoutingProvider.keyKeyShooter)) {
		        		if(thisTeam.getInt(i)==0) {
		            		key = false;
		            	} else {
		            		key = true;
		            	}
		        	} else if (colName.equals(TeamScoutingProvider.keyBarrier)) {
		        		if(thisTeam.getInt(i)==0) {
		            		barrier = false;
		            	} else {
		            		barrier = true;
		            	}
		        	} else if (colName.equals(TeamScoutingProvider.keyClimb)) {
		        		if(thisTeam.getInt(i)==0) {
		            		climb = false;
		            	} else {
		            		climb = true;
		            	}
		        	} else if (colName.equals(TeamScoutingProvider.keyAutonomous)) {
		        		if(thisTeam.getInt(i)==0) {
		            		auto = false;
		            	} else {
		            		auto = true;
		            	}
		        	} else if (colName.equals(TeamScoutingProvider.keyAvgHoops)) {
		        		hoops = thisTeam.getDouble(i);
		        	} else if (colName.equals(TeamScoutingProvider.keyAvgBalance)) {
		        		balance = thisTeam.getDouble(i);
		        	} else if (colName.equals(TeamScoutingProvider.keyAvgBroke)) {
		        		broke = thisTeam.getDouble(i);
		        	} else if (colName.equals(TeamScoutingProvider.keyNotes)) {
		        		notes = thisTeam.getString(i);
		        	}
		        }
	        } else {
	        	breakNicely = true;
	        }
	        return con[0];
		}
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
	    switch (item.getItemId()) {
	        case R.id.settings:
	        	String actionName = "com.mechinn.android.ouralliance.OpenSettings";
		    	Intent intent = new Intent(actionName);
		    	startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
