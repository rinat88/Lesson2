package com.example.lesson2.ui.posts.form;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lesson2.R;
import com.example.lesson2.data.remote.models.Post;
import com.example.lesson2.databinding.FragmentFormBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FormFragment extends Fragment {


    private FragmentFormBinding binding;
    private static final int USER_ID = 4;
    private static final int GROUP_ID = 5;
    private NavController controller;
    private Post post2;

    public FormFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFormBinding.inflate(inflater, container, false);
        controller = Navigation.findNavController(requireActivity(),R.id.nav_host_fragment);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            post2 = (Post) getArguments().getSerializable("key");
            binding.etTitle.setText(post2.getTitle());
            binding.etContent.setText(post2.getContent());
        }
        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (post2 == null) {
                    String title = binding.etTitle.getText().toString();
                    String content = binding.etContent.getText().toString();
                    Post post = new Post(title, content, USER_ID, GROUP_ID);
                    App.api.createPost(post).enqueue(new Callback<Post>() {
                        @Override
                        public void onResponse(Call<Post> call, Response<Post> response) {
                            if (response.isSuccessful()) {
                                NavController navController = Navigation.findNavController(requireActivity(),
                                        R.id.nav_host_fragment);
                                controller.popBackStack();
                            }
                        }

                        @Override
                        public void onFailure(Call<Post> call, Throwable t) {

                        }
                    });
                } else {
                    String title = binding.etTitle.getText().toString();
                    String content = binding.etContent.getText().toString();
                    Post post = new Post(title , content , USER_ID , GROUP_ID);
                    App.api.createPost(post).enqueue(new Callback<Post>() {
                        @Override
                        public void onResponse(Call<Post> call, Response<Post> response) {
                            Toast.makeText(requireActivity(),"Update",Toast.LENGTH_SHORT).show();
                            NavController navController = Navigation.findNavController(requireActivity(),
                                    R.id.nav_host_fragment);
                            navController.navigate(R.id.postsFragment);
                        }

                        @Override
                        public void onFailure(Call<Post> call, Throwable t) {

                        }
                    });
                }
            }
        });


    }
}