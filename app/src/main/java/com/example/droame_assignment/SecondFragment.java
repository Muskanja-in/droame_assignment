package com.example.droame_assignment;

import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL;
import static com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS;

import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Movie;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.VideoView;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;


import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFprobe;
import com.arthenica.mobileffmpeg.MediaInformation;
import com.example.droame_assignment.databinding.FragmentSecondBinding;
import com.googlecode.mp4parser.BasicContainer;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.AppendTrack;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
//import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
//import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
//import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.sql.Time;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import VideoHandle.EpEditor;
import VideoHandle.EpVideo;
import VideoHandle.OnEditorListener;

public class SecondFragment<final_file> extends Fragment {

    private FragmentSecondBinding binding;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }


    String[] vids;

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vids = SecondFragmentArgs.fromBundle(getArguments()).getVideos();

        try {
            addTransition();
        } catch (IOException e) {
            e.printStackTrace();
        }

        VideoView vv = view.findViewById(R.id.videoView1);
        Uri uri = Uri.parse(outPath);
        vv.setVideoURI(uri);
        vv.start();

//        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(MediaPlayer mp) {
//              mp.setLooping(true);
//            }
//        });
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        //mergeVideos();
        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                boolean deleted = filex.delete();
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }

        });
        binding.downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dstPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "droame_assigment" + File.separator;
                File dst = new File(dstPath);

                try {
                    exportFile(filex, dst);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private File exportFile(File src, File dst) throws IOException {

        //if folder does not exist
        if (!dst.exists()) {
            if (!dst.mkdir()) {
                return null;
            }
        }


        File expFile = new File(dst.getPath() + File.separator + "merged_video" + ts + ".mp4");
        FileChannel inChannel = null;
        FileChannel outChannel = null;

        try {
            inChannel = new FileInputStream(src).getChannel();
            outChannel = new FileOutputStream(expFile).getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }

        return expFile;
    }

    public File createVideoPath(int i) throws IOException {
//        String imageFileName="transition";
        String imageFileName = "/transition" + i;
//        this.getActivity().getFilesDir().getAbsolutePath()
        File storageDir = new File(requireContext().getFilesDir().getAbsolutePath());
        if (storageDir != null) {
            if (!storageDir.exists()) storageDir.mkdirs();
        }
        //return File.createTempFile(imageFileName, ".mp4", storageDir);
        //return new File(imageFileName);
        return new File(storageDir + "/" + imageFileName + ".mp4");
    }

    List<String> out_paths;
    Long tsLong = System.currentTimeMillis() / 1000;
    String ts = tsLong.toString();
    //final String outpath_ = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()+"/droame_assignment_1"+ts+"_merged_video.mp4";
    //final String outpath_ = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath()]
    String outPath = "";
    File filex;

    private void addTransition() throws IOException {

        filex = createVideoPath(5);
        //final String outPath1 = file1.getPath();
        outPath = filex.getPath();
        out_paths = new ArrayList<>();
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        for (int i = 0; i < vids.length; i++) {
            File file1 = createVideoPath(i);
            final String outPath1 = file1.getPath();
            Log.i("Photopicker", "Outpaths" + outPath1);
//use one of overloaded setDataSource() functions to set your data source
            retriever.setDataSource(getContext(), Uri.fromFile(new File(vids[i])));
            String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            long timeInMillisec = Long.parseLong(time);
            timeInMillisec = timeInMillisec / 1000;
            timeInMillisec -= 5;

            String[] cmd = new String[]{"-y", "-i", vids[i], "-vf", "fade=t=in:st=0:d=0.2:color=white,fade=t=out:st=" + timeInMillisec + ":d=0.2:color=white", "-pix_fmt", "yuv420p", "-c:v", "libx264", "-preset", "ultrafast", "-acodec", "copy", outPath1};
            exe_cmd1(cmd, 1);
            out_paths.add(outPath1);
            String[] cmd2 = new String[]{"-i", out_paths.get(i), "-show_streams", "-select_streams", "a", "-loglevel", "error"};
            check_audio(out_paths.get(i), i);

            //Log.i("Photopicker","MediaInfo"+s.getMediaProperties());
        }
        retriever.release();

        if (vids.length == 2) {
//            File file1=createVideoPath(5);
//            final String outPath2 = file1.getPath();
            String[] cmd1 = new String[]{"-y", "-i", out_paths.get(0), "-i", out_paths.get(1), "-strict", "experimental", "-filter_complex",
                    "[0:v]scale=iw*min(1920/iw\\,1080/ih):ih*min(1920/iw\\,1080/ih), pad=1920:1080:(1920-iw*min(1920/iw\\,1080/ih))/2:(1080-ih*min(1920/iw\\,1080/ih))/2,setsar=1:1[v0];[1:v] scale=iw*min(1920/iw\\,1080/ih):ih*min(1920/iw\\,1080/ih), pad=1920:1080:(1920-iw*min(1920/iw\\,1080/ih))/2:(1080-ih*min(1920/iw\\,1080/ih))/2,setsar=1:1[v1];[v0][0:a][v1][1:a] concat=n=2:v=1:a=1", "-vsync", "2",
                    "-ab", "48000", "-ac", "2", "-ar", "22050", "-s", "1920x1080", "-vcodec", "libx264", "-crf", "27",
                    "-q", "4", "-preset", "ultrafast", outPath};
//            String complexCommand[] = {"-y", "-i", out_paths.get(0), "-i", out_paths.get(1), "-filter_complex",
//                    "[0:v]scale=480x640,setsar=1[v0];[1:v]scale=480x640,setsar=1[v1];[v0][0:a][v1][1:a]concat=n=2:v=1:a=1",
//                    "-ab", "48000", "-ac", "2", "-ar", "22050", "-s", "480x640", "-vcodec", "libx264","-crf","27","-preset", "ultrafast", outPath};
            exe_cmd1(cmd1, 2);
            Log.i("Photopicker", "Success");
        } else {
            File file1 = createVideoPath(4);
            final String outPath1 = file1.getPath();
            String[] cmd1 = new String[]{"-y", "-i", out_paths.get(0), "-i", out_paths.get(1), "-strict", "experimental", "-filter_complex",
                    "[0:v]scale=iw*min(1920/iw\\,1080/ih):ih*min(1920/iw\\,1080/ih), pad=1920:1080:(1920-iw*min(1920/iw\\,1080/ih))/2:(1080-ih*min(1920/iw\\,1080/ih))/2,setsar=1:1[v0];[1:v] scale=iw*min(1920/iw\\,1080/ih):ih*min(1920/iw\\,1080/ih), pad=1920:1080:(1920-iw*min(1920/iw\\,1080/ih))/2:(1080-ih*min(1920/iw\\,1080/ih))/2,setsar=1:1[v1];[v0][0:a][v1][1:a] concat=n=2:v=1:a=1", "-vsync", "2",
                    "-ab", "48000", "-ac", "2", "-ar", "22050", "-s", "1920x1080", "-vcodec", "libx264", "-crf", "27",
                    "-q", "4", "-preset", "ultrafast", outPath1};
            exe_cmd1(cmd1, 3);
            //File file2=createVideoPath(5);
            //final String outPath2 = file2.getPath();
            String[] cmd2 = new String[]{"-y", "-i", outPath1, "-i", out_paths.get(2), "-strict", "experimental", "-filter_complex",
                    "[0:v]scale=iw*min(1920/iw\\,1080/ih):ih*min(1920/iw\\,1080/ih), pad=1920:1080:(1920-iw*min(1920/iw\\,1080/ih))/2:(1080-ih*min(1920/iw\\,1080/ih))/2,setsar=1:1[v0];[1:v] scale=iw*min(1920/iw\\,1080/ih):ih*min(1920/iw\\,1080/ih), pad=1920:1080:(1920-iw*min(1920/iw\\,1080/ih))/2:(1080-ih*min(1920/iw\\,1080/ih))/2,setsar=1:1[v1];[v0][0:a][v1][1:a] concat=n=2:v=1:a=1", "-vsync", "2",
                    "-ab", "48000", "-ac", "2", "-ar", "22050", "-s", "1920x1080", "-vcodec", "libx264", "-crf", "27",
                    "-q", "4", "-preset", "ultrafast", outPath};
            exe_cmd1(cmd2, 4);
            Log.i("Photopicker", "Success");
        }

    }

    private void check_audio(String path_, int i) throws IOException {
        MediaInformation s = FFprobe.getMediaInformation(path_);
        Long x = s.getNumberProperty("nb_streams");
        Log.i("PhotoPicker", "nb_streams" + x);
        if (x == 1) {
            File file1 = createVideoPath(10 + i);
            final String outPath1 = file1.getPath();
            String[] cmd3 = new String[]{"-f", "lavfi", "-i", "anullsrc=channel_layout=stereo:sample_rate=44100", "-i", path_, "-c:v", "copy", "-c:a", "aac", "-shortest", outPath1};
            exe_cmd1(cmd3, 5);
            out_paths.set(i, outPath1);
        }

    }

    private void exe_cmd1(String[] cmd, int i) {
        int rc = FFmpeg.execute(cmd);

        if (rc == RETURN_CODE_SUCCESS) {
            Log.i("PhotoPicker", "Command execution completed successfully." + i);
        } else if (rc == RETURN_CODE_CANCEL) {
            Log.i("PhotoPicker", "Command execution cancelled by user." + i);
        } else {
            Log.i("PhotoPicker", String.format("Command execution failed with rc=%d and the output below.", rc) + " " + i);
            Config.printLastCommandOutput(Log.INFO);
        }
    }
//    private String appendTwoVideos(String firstVideoPath, String secondVideoPath)
//    {
//        try {
//            Movie[] inMovies = new Movie[2];
//
//            inMovies[0] = MovieCreator.build(firstVideoPath);
//            inMovies[1] = MovieCreator.build(secondVideoPath);
//
//            List<Track> videoTracks = new LinkedList<>();
//            List<Track> audioTracks = new LinkedList<>();
//            List<Track> = inMovies[0].getTracks();
//            for (Movie m : inMovies) {
//                for (Track t : m.getTracks()) {
//                    if (t.getHandler().equals("soun")) {
//                        audioTracks.add(t);
//                    }
//                    if (t.getHandler().equals("vide")) {
//                        videoTracks.add(t);
//                    }
//                }
//            }
//
//            Movie result = new Movie();
//
//            if (audioTracks.size() > 0) {
//                result.addTrack(new AppendTrack(audioTracks
//                        .toArray(new Track[audioTracks.size()])));
//            }
//            if (videoTracks.size() > 0) {
//                result.addTrack(new AppendTrack(videoTracks
//                        .toArray(new Track[videoTracks.size()])));
//            }
//
//            BasicContainer out = (BasicContainer) new DefaultMp4Builder().build(result);
//
//            @SuppressWarnings("resource")
//            FileChannel fc = new RandomAccessFile(Environment.getExternalStorageDirectory() + "/wishbyvideo.mp4", "rw").getChannel();
//            out.writeContainer(fc);
//            fc.close();
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        String mFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
//        mFileName += "/wishbyvideo.mp4";
//        return mFileName;
//    }

//    private void exe_cmd(String[] cmd,File file2) throws FFmpegCommandAlreadyRunningException, IOException, InterruptedException {
//        FFmpeg ffmpeg = FFmpeg.getInstance(getContext());
//        try {
//            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
//
//                @Override
//                public void onStart() {}
//
//                @Override
//                public void onFailure() {}
//
//                @Override
//                public void onSuccess() {}
//
//                @Override
//                public void onFinish() {}
//            });
//        } catch (FFmpegNotSupportedException e) {
//            // Handle if FFmpeg is not supported by device
//        }
////        if (!ffmpeg.canExecute()) {
////            // try to make executable
////            try {
////                try {
////                    Runtime.getRuntime().exec("chmod -R 777 " + ffmpeg.getAbsolutePath()).waitFor();
////                } catch (InterruptedException e) {
////                    Log.e("interrupted exception", "Exception"+e);
////
////                } catch (IOException e) {
////                    Log.e("io exception", "Exception"+e);
////
////                }
////
////                if (!ffmpeg.canExecute()) {
////                    // our last hope!
////                    if (!ffmpeg.setExecutable(true)) {
////                        Log.e("Photopicker","unable to make executable");
////
////                    }
////                }
////            } catch (SecurityException e) {
////                Log.e("Photopicker","security exception"+ e);
////
////            }
////        }
////        Log.d("Photopicker","ffmpeg is ready!");
//
//        try{
//        ffmpeg.execute(cmd, new ExecuteBinaryResponseHandler(){
//        @Override
//        public void onStart() {
//
//        }
//        @Override
//        public void onProgress(String message) {
//            Log.i("Photopicker","onProgress"+message);
//        }
//
//        @Override
//        public void onSuccess(String message) {
//            Log.i("Photopicker","onSuccess"+message);
//        }
//
//        @Override
//        public void onFailure(String message) {
//            Log.i("Photopicker","onFailure"+message);
//        }
//
//        @Override
//        public void onFinish() {
//            Log.i("Photopicker","Finish");
//        }
//
//    });
//    } catch(Exception e) {
//            Log.i("Photopicker","Could not execute");
//    }
//        Runtime.getRuntime().exec("chmod -R 777 " + file2.getAbsolutePath()).waitFor();
//    }

//    private void mergeVideos(){
//
//        //Log.i("PhotoPicker","Boolean"+root.mkdirs());
//        List<EpVideo> epVideos =  new ArrayList<>();
//
//        Log.i("Photopicker","In1");
//        for(int i=0;i<vids.length;i++)
//        {
//            epVideos.add(new EpVideo (out_paths.get(i)));
//        }
//         // Video 1
////        epVideos.add(new EpVideo (vid2));
////        epVideos.add(new EpVideo (vid3));
////        epVideos.add(new EpVideo (s)); // Video 1
////        epVideos.add(new EpVideo (getImageFilePath(vid2)));
////        epVideos.add(new EpVideo (getImageFilePath(vid3)));
//        EpEditor. OutputOption outputOption =new EpEditor.OutputOption(outPath);
//        outputOption.setWidth(720);
//        outputOption.setHeight(1280);
//        outputOption.frameRate = 25 ;
//        outputOption.bitRate = 10 ;
//        EpEditor.merge(epVideos,outputOption, new OnEditorListener(){
//            @Override
//            public  void  onSuccess () {
//                Log.d("PhotoPicker","Success");
//
//            }
//
//            @Override
//            public  void  onFailure () {
//                Log.d("PhotoPicker","Failure");
//            }
//
//            @Override
//            public  void  onProgress ( float  progress ) {
//                // Get processing progress here
//                Log.d("Progress",""+progress);
//            }
//        });
//        Log.i("Photopicker","outpath"+outPath);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}