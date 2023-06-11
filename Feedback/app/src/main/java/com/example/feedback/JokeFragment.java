package com.example.feedback;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class JokeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_joke, container, false);

        // Joke text
        TextView tvJoke = root.findViewById(R.id.randomJoke);

        //get value from api
        JokeTask task = new JokeTask(tvJoke);
        task.execute();

        // generate new Joke
        Button btnJoke = root.findViewById(R.id.generateRandomJoke);

        btnJoke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get value from api
                JokeTask task = new JokeTask(tvJoke);
                task.execute();
            }
        });

        return root;
    }

}