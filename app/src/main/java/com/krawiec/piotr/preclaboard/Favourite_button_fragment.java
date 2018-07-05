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
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Favourite_button_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Favourite_button_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Favourite_button_fragment extends Fragment {

    private int BUTTON_COUNT = 16;
    private static MediaPlayer mp = new MediaPlayer();
    private SoundPool soundPool;

    private OnFragmentInteractionListener mListener;

    public Favourite_button_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Favourite_button_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Favourite_button_fragment newInstance(String param1, String param2) {
        Favourite_button_fragment fragment = new Favourite_button_fragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        boolean temp = false;
        for (int i = 0; i < BUTTON_COUNT; i++) {
            Context context = rootView.getContext();
            SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.favarray), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if(sharedPreferences.contains(Integer.toString(i))) {
                button[i] = new Button(rootView.getContext());
                button[i].setLayoutParams(layoutParams);
                button[i].setText(stringArray[i]);
                button[i].setId(i+1);
                button[i].setOnClickListener(mp);
                button[i].setOnLongClickListener(how);
                linearLayout.addView(button[i]);
                temp = true;
            }
            editor.apply();
            if((temp)&(i==(BUTTON_COUNT-1))) {
                TextView textView = new TextView(rootView.getContext());
                textView.setLayoutParams(layoutParams);
                textView.setText(R.string.add_something_pls);
            }


            //Snackbar snackbar = Snackbar
            //        .make(getView(), "Usunięto wszystkie rekordy", Snackbar.LENGTH_LONG);
            //snackbar.show();
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

    private class How implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {

            Context context = v.getContext();
            SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.favarray), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            int id = v.getId();
            --id;
            editor.remove(Integer.toString(id));
            editor.apply();
            Fragment currentFragment = getFragmentManager().findFragmentByTag("tab2");
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.detach(currentFragment);
            fragmentTransaction.attach(currentFragment);
            fragmentTransaction.commit();

            Resources res = getResources();
            String[] stringArray = res.getStringArray(R.array.buttonNames);

            Snackbar snackbar = Snackbar
                    .make(getView(),"Usunięto z ulubionych: " + stringArray[id] , Snackbar.LENGTH_LONG);
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
            final float leftVolume = curVolume / maxVolume;
            final float rightVolume = curVolume / maxVolume;
            final int priority = 1;
            final int no_loop = 0;
            final float normal_playback_rate = 1f;
            final int k = soundPool.load(v.getContext(), i, 1);
            soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId,
                                           int status) {
                    soundPool.play(k, 1, 1, priority, no_loop, normal_playback_rate);
                }
            });


        }
    }
}
