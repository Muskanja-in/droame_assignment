package com.example.droame_assignment;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


import com.example.droame_assignment.databinding.FragmentSecondBinding;

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
    Uri vid1,vid2,vid3;
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vid1 = Uri.parse(SecondFragmentArgs.fromBundle(getArguments()).getVideo1());
        vid2 = Uri.parse(SecondFragmentArgs.fromBundle(getArguments()).getVideo2());
        vid3 = Uri.parse(SecondFragmentArgs.fromBundle(getArguments()).getVideo3());
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
    final String outPath = Environment.getExternalStorageDirectory().getPath() + "/Video-merger/" + "outmerge.mp4";
    private void mergeVideos() {
        List<EpVideo> epVideos =  new ArrayList<>();
        epVideos.add(new EpVideo (vid1.getPath())); // Video 1
        epVideos.add(new EpVideo (vid2.getPath()));
        epVideos.add(new EpVideo (vid3.getPath()));
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

    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}