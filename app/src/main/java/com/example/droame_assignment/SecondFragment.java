package com.example.droame_assignment;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


import com.example.droame_assignment.databinding.FragmentSecondBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import VideoHandle.EpEditor;
import VideoHandle.EpVideo;
import VideoHandle.OnEditorListener;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }
//    Uri vid1,vid2,vid3;
String vid1,vid2,vid3;
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        vid1 = Uri.parse(SecondFragmentArgs.fromBundle(getArguments()).getVideo1());
//        vid2 = Uri.parse(SecondFragmentArgs.fromBundle(getArguments()).getVideo2());
//        vid3 = Uri.parse(SecondFragmentArgs.fromBundle(getArguments()).getVideo3());
        vid1 = SecondFragmentArgs.fromBundle(getArguments()).getVideo1();
        vid2 = SecondFragmentArgs.fromBundle(getArguments()).getVideo2();
        vid3 = SecondFragmentArgs.fromBundle(getArguments()).getVideo3();
        Log.i("Photopicker","Video 3"+vid3);
        mergeVideos();
        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private void requestPermission() {
        Log.i("PhotoPicker","requestPermission");
        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat
                    .requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_ASK_PERMISSIONS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.i("PhotoPicker","requestPermission2");
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(getActivity(), "Permission Granted", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    final String outPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/droame_assignment_"+"merged_video.mp4";
    //File root = new File(outPath);
    private void mergeVideos() {

        //Log.i("PhotoPicker","Boolean"+root.mkdirs());
        List<EpVideo> epVideos =  new ArrayList<>();
//        epVideos.add(new EpVideo (vid1.getPath())); // Video 1
//        epVideos.add(new EpVideo (vid2.getPath()));
//        epVideos.add(new EpVideo (vid3.getPath()));
        Log.i("Photopicker","In1");
        epVideos.add(new EpVideo (vid1)); // Video 1
        epVideos.add(new EpVideo (vid2));
        epVideos.add(new EpVideo (vid3));
//        epVideos.add(new EpVideo (s)); // Video 1
//        epVideos.add(new EpVideo (getImageFilePath(vid2)));
//        epVideos.add(new EpVideo (getImageFilePath(vid3)));
        EpEditor. OutputOption outputOption =new EpEditor.OutputOption(outPath);
        outputOption.setWidth(720);
        outputOption.setHeight(1280);
        outputOption.frameRate = 25 ;
        outputOption.bitRate = 10 ;
        EpEditor.merge(epVideos,outputOption, new OnEditorListener(){
            @Override
            public  void  onSuccess () {
                Log.d("PhotoPicker","Success");

            }

            @Override
            public  void  onFailure () {
                Log.d("PhotoPicker","Failure");
            }

            @Override
            public  void  onProgress ( float  progress ) {
                // Get processing progress here
                Log.d("Progress",""+progress);
            }
        });
        Log.i("Photopicker","outpath"+outPath);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}