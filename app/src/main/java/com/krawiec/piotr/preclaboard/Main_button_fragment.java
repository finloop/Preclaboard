package com.krawiec.piotr.preclaboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Main_button_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Main_button_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Main_button_fragment extends Fragment {

    private int BUTTON_COUNT = 16;
    private static MediaPlayer mp = new MediaPlayer();
    private SoundPool soundPool;

    private OnFragmentInteractionListener mListener;

    public Main_button_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Main_button_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Main_button_fragment newInstance(String param1, String param2) {
        Main_button_fragment fragment = new Main_button_fragment();

        return fragment;
    }

    private class How implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {

            Context context = v.getContext();
            SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.favarray), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int id = v.getId();
            --id;
            if(!sharedPreferences.contains(Integer.toString(id))) {
                editor.putInt(Integer.toString(id),id);
                editor.apply();
            }
            Resources res = getResources();
            String[] stringArray = res.getStringArray(R.array.buttonNames);

            Snackbar snackbar = Snackbar
                    .make(getView(),"Dodano do ulubionych: " + stringArray[id] , Snackbar.LENGTH_LONG);
            snackbar.show();

            return true;
        }
    }

    private class MP implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            --id;
            String resName = "s" + v.getId();
            int i = getResources().getIdentifier(resName, "raw", getActivity().getPackageName());
            AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
            final float curVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            final float maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            final  float leftVolume = curVolume/maxVolume;
            final  float rightVolume = curVolume/maxVolume;
            final int priority = 1;
            final  int no_loop = 0;
            final float normal_playback_rate = 1f;

            final  int k  = soundPool.load(v.getContext(),i,1);
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId,
                                           int status) {
                    soundPool.play(k, 1, 1, priority, no_loop, normal_playback_rate);
                }
            });


        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        */

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_button_main, container, false);

        LinearLayout linearLayout = rootView.findViewById(R.id.main_fragment_layout);

        Button[] button = new Button[BUTTON_COUNT];

        How how = new How();
        MP mp = new MP();

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        Resources res = getResources();
        String[] stringArray = res.getStringArray(R.array.buttonNames);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

        //int h = sharedPreferences.getInt(getString(R.string.ustawienie_wielości_przycisków), 25);
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, sharedPreferences.getInt(getString(R.string.ustawienie_wielości_przycisków), 60), getResources().getDisplayMetrics());
        layoutParams.height = height;
        for (int i = 0; i < BUTTON_COUNT; i++) {
            button[i] = new Button(getContext());
            button[i].setLayoutParams(layoutParams);
            button[i].setText(stringArray[i]);
            button[i].setId(i+1);
            button[i].setOnClickListener(mp);
            button[i].setOnLongClickListener(how);
            linearLayout.addView(button[i]);
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder().setAudioAttributes(audioAttrib).setMaxStreams(6).build();
        }else{
            soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 5);
        }

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
