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

import com.example.lesson2.ItemOnClick;
import com.example.lesson2.R;
import com.example.lesson2.data.remote.models.Post;
import com.example.lesson2.databinding.FragmentFormBinding;
import com.example.lesson2.databinding.FragmentPostsBinding;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PostsFragment extends Fragment {

    private FragmentPostsBinding binding;
    private PostsAdapter adapter;

    public PostsFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new PostsAdapter();
        adapter.setItemClick(new ItemOnClick() {
            @Override
            public void click(int position) {
                if (adapter.getPost(position).getUserId() == 4)
                    openFragment(adapter.getPost(position));
                else
                    Toast.makeText(requireActivity(), "вы не можете редактировать чужие записи", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void longClick(int position) {
                Post post3 = adapter.getPost(position);
                if (post3.getUserId() == 4) {
                    App.api.deletePost(post3.getId()).enqueue(new Callback<Post>() {
                        @Override
                        public void onResponse(Call<Post> call, Response<Post> response) {
                            adapter.removeItem(position);
                        }

                        @Override
                        public void onFailure(Call<Post> call, Throwable t) {

                        }
                    });
                } else
                    Toast.makeText(requireActivity(), "вы не можете удалять чужие записи", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPostsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.rvPosts.setAdapter(adapter);


        App.api.getPosts(5).enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setPosts(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {

            }
        });

        initListeners();
    }

    private void initListeners() {
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
                navController.navigate(R.id.formFragment);
            }
        });

    }

    private void openFragment(Post post) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("key", post);
        NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment);
        navController.navigate(R.id.formFragment, bundle);

    }

}