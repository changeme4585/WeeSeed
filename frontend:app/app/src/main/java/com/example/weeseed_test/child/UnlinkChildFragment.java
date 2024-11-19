package com.example.weeseed_test.child;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weeseed_test.R;
import com.example.weeseed_test.activity.MainActivity;
import com.example.weeseed_test.child.adapter.ChildListBiggerAdapter;
import com.example.weeseed_test.databinding.FragmentUnlinkChildBinding;
import com.example.weeseed_test.dto.ChildDto_forUSE;
import com.example.weeseed_test.util.Viewmodel;
import com.example.weeseed_test.util.RetrofitAPI;
import com.example.weeseed_test.util.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnlinkChildFragment  extends Fragment implements ItemClickListener{

    Viewmodel viewModel;
    private FragmentUnlinkChildBinding binding;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = ((MainActivity) requireActivity()).getViewModel();
        binding = FragmentUnlinkChildBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        ///
        recyclerView = view.findViewById(R.id.recyclerview_unlink_child);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 성능항상
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager); //레이아웃 연결


        //NOK: userID로 목록 가져오기
        getNokChildInfoFromServer(viewModel.getUserID());

        return view;
    }


    /////////////////모든 아동 정보 불러오기//////////////////
    private void getNokChildInfoFromServer(String userid) {
        Log.e("getNokChildInfoFromServer", "1: ");

        RetrofitService retrofitService = new RetrofitService(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);
        Log.e("getNokChildInfoFromServer", "2: ");


        try{
            retrofitAPI.getNokChildInfo(userid)
                    .enqueue(new Callback<List<ChildDto_forUSE>>() {
                        @Override
                        public void onResponse(Call<List<ChildDto_forUSE>> call, Response<List<ChildDto_forUSE>> response) {
                            Log.e("getNokChildInfoFromServer", "3: ");

                            //목록 길이 != 0일 시 리스트 표시
                            childUnlinkListView(response.body());
                            Log.e("getNokChildInfoFromServer", "4: ");
                        }

                        @Override
                        public void onFailure(Call<List<ChildDto_forUSE>> call, Throwable t) {
                            Log.e("getNokChildInfoFromServerERR", t.getMessage());
                            Toast.makeText(getActivity(), "오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }
        catch(Exception t){
            Log.e("getNokChildInfoFromServerERR","ERR) "+t.getMessage());
        }


    }
    private void childUnlinkListView(List<ChildDto_forUSE> childList){
        if (childList != null) {
            ChildListBiggerAdapter childListAdapter = new ChildListBiggerAdapter(childList, this, false);
            recyclerView.setAdapter(childListAdapter);
        } else {
            Log.e("childSelectListView", "등록된 아동이 없습니다.");
            Toast.makeText(getActivity(), "등록된 아동이 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(ChildDto_forUSE childDto) {
        Toast.makeText(getActivity(), "개발 중인 기능입니다.", Toast.LENGTH_SHORT).show();
    }
}
