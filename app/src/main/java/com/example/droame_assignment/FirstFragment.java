package com.example.droame_assignment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.droame_assignment.databinding.FragmentFirstBinding;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
//    private ActivityResultLauncher<Intent> pickMedia;
private List<Uri> videos;

    private ActivityResultLauncher<PickVisualMediaRequest>  pickMultipleMedia = registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(3), new ActivityResultCallback<List<Uri>>(){
        @Override
        public void onActivityResult(List<Uri> uris) {
            videos = uris;
            if (!uris.isEmpty()) {
                Log.d("PhotoPicker", "Number of items selected: " + uris.size());
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
            for(int i=0;i<videos.size();i++)
            {
                Log.i("PhotoPicker", "Uri"+ videos.get(i));
            }

        }
    });

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);

        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        view.findViewById(R.id.merge_videos).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
                Log.i("PhotoPicker","In");

                FirstFragmentDirections.ActionFirstFragmentToSecondFragment action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(videos.get(0).toString(),videos.get(1).toString(),videos.get(2).toString());
                NavHostFragment.findNavController(FirstFragment.this).navigate(action);
            }
        });
        view.findViewById(R.id.pick_video_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("1","inside on click");

                pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.VideoOnly.INSTANCE)
                        .build());
            }
        });
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
//        view.findViewById(R.id.pick_video_button);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final int maxNumPhotosAndVideos = 3;
//
//                Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
//                intent.setType("video/*");
//                intent.putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX, maxNumPhotosAndVideos);
//                pickMedia.launch(intent);
//
//            }
//        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}