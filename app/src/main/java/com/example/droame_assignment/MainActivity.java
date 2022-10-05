package com.example.droame_assignment;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.droame_assignment.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity {


    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

//    private List<Uri> videos;
//
//    private ActivityResultLauncher<PickVisualMediaRequest>  pickMultipleMedia = registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(3), new ActivityResultCallback<List<Uri>>(){
//        @Override
//        public void onActivityResult(List<Uri> uris) {
//            videos = uris;
//            if (!uris.isEmpty()) {
//                Log.d("PhotoPicker", "Number of items selected: " + uris.size());
//            } else {
//                Log.d("PhotoPicker", "No media selected");
//            }
//            for(int i=0;i<videos.size();i++)
//            {
//                Log.i("PhotoPicker", "Uri"+ videos.get(i));
//            }
//        }
//    });
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

//        findViewById(R.id.pick_video_button).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Log.i("1","inside on click");
//
//                    pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
//                            .setMediaType(ActivityResultContracts.PickVisualMedia.VideoOnly.INSTANCE)
//                            .build());
//                }
//            });



//        pickMedia =
//                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), uri -> {
//                    // Callback is invoked after the user selects a media item or closes the
//                    // photo picker.
//                    if (uri != null) {
//                        Log.d("PhotoPicker", "Selected URI: " + uri);
//                    } else {
//                        Log.d("PhotoPicker", "No media selected");
//                    }
//                });
//


        return ;
        }



    // onActivityResult() handles callbacks from the photo picker.


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}