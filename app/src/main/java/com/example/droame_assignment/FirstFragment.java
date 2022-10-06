package com.example.droame_assignment;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
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
private List<String> videos = new ArrayList<>();

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
//
//        }
//    });
private ActivityResultLauncher<Intent>  pickMultipleMedia = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>(){
    @Override
    public void onActivityResult(ActivityResult result) {
        if (result.getResultCode() == RESULT_OK) {
            Intent data = result.getData();
            Log.i("Photopicker","Data"+data);
            int count = data.getClipData().getItemCount();
            for (int i = 0; i < count; i = i + 1) {
                Uri imageUri = data.getClipData().getItemAt(i).getUri();
                videos.add(getRealPathFromURI(imageUri));
                Log.i("PhotoPicker", "Uri" + imageUri);
                Log.i("PhotoPicker", "Path" + getRealPathFromURI(imageUri));
            }}
//                try {
//////                    Uri originalUri = data.getData();
////                    String pathsegment[] = imageUri.getLastPathSegment().split(":");
////                    String id = pathsegment[0];
////                    final String[] imageColumns = { MediaStore.Images.Media.DATA };
////                    final String imageOrderBy = null;
////
////                    Uri uri = getUri();
////                    Cursor imageCursor = getActivity().getContentResolver().query(uri, imageColumns,
////                            MediaStore.Images.Media._ID + "=" + id, null, null);
////                    String value="path";
////                    if (imageCursor.moveToFirst()) {
////                        int x=imageCursor.getColumnIndex(MediaStore.Images.Media.DATA);
////                        value = imageCursor.getString(x);
////                    }
////                    Log.i("PhotoPicker", "Path" + value);
//                    final int takeFlags = data.getFlags()
//                            & (Intent.FLAG_GRANT_READ_URI_PERMISSION
//                            | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                    // Check for the freshest data.
//                    getActivity().getContentResolver().takePersistableUriPermission(imageUri, takeFlags);
//
//    /* now extract ID from Uri path using getLastPathSegment() and then split with ":"
//    then call get Uri to for Internal storage or External storage for media I have used getUri()
//    */
//
//                    String id = imageUri.getLastPathSegment().split(":")[1];
//                    final String[] imageColumns = {MediaStore.Images.Media.DATA };
//                    final String imageOrderBy = null;
//
//                    Uri uri = getUri();
//                    String selectedImagePath = "path";
//
//                    Cursor imageCursor = getActivity().getContentResolver().query(uri, imageColumns,
//                            MediaStore.Images.Media._ID + "="+id, null, imageOrderBy);
//
//                    if (imageCursor.moveToFirst()) {
//                        selectedImagePath = imageCursor.getString(imageCursor.getColumnIndex(MediaStore.Images.Media.DATA));
//                    }
//                    Log.e("path",selectedImagePath ); // use selectedImagePath
//                } catch (Exception e) {
//                    Toast.makeText(getActivity(), "Failed to get image", Toast.LENGTH_LONG).show();
//                }
//
//            }
//
//        }
   }

});
    private String getRealPathFromURI(Uri contentURI) {

        String result;
        String[] filePathColumn = { MediaStore.Images.Media.DATA };

        Cursor cursor = getActivity().getContentResolver().query(contentURI, filePathColumn, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(filePathColumn[0]);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }


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
                FirstFragmentDirections.ActionFirstFragmentToSecondFragment action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(videos.get(0),videos.get(1),videos.get(2));

//                FirstFragmentDirections.ActionFirstFragmentToSecondFragment action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(videos.get(0).toString(),videos.get(1).toString(),videos.get(2).toString());
                NavHostFragment.findNavController(FirstFragment.this).navigate(action);
            }
        });
        view.findViewById(R.id.pick_video_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Log.i("1","inside on click");
//
//                pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
//                        .setMediaType(ActivityResultContracts.PickVisualMedia.VideoOnly.INSTANCE)
//                        .build());
//            }
            @Override
            public void onClick(View view) {
                final int maxNumPhotosAndVideos = 3;

                Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
                intent.setType("video/*");
                intent.putExtra(MediaStore.EXTRA_PICK_IMAGES_MAX, maxNumPhotosAndVideos);
                pickMultipleMedia.launch(intent);

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