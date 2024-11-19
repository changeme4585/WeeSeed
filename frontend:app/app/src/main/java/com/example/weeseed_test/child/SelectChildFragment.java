package com.example.weeseed_test.child;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.weeseed_test.R;
import com.example.weeseed_test.activity.MainActivity;
import com.example.weeseed_test.cardlist.CardListFragment;
import com.example.weeseed_test.child.adapter.ChildListAdapter;
import com.example.weeseed_test.child.adapter.ChildListBiggerAdapter;
import com.example.weeseed_test.databinding.FragmentSelectChildBinding;
import com.example.weeseed_test.dto.ChildDto_forUSE;
import com.example.weeseed_test.util.Viewmodel;
import com.example.weeseed_test.util.RetrofitAPI;
import com.example.weeseed_test.util.RetrofitService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**/
public class SelectChildFragment extends Fragment implements ItemClickListener {

    Viewmodel viewModel;
    private FragmentSelectChildBinding binding;
    private RecyclerView recyclerView;

    private RecyclerView.LayoutManager layoutManager;
    public Boolean isGrid=false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = ((MainActivity) requireActivity()).getViewModel();
        Log.e("onCreateView","3userID: "+ viewModel.getUserID()+"  세션ID: " +viewModel.getSessionID().toString() + "  유저 타입:  "+viewModel.getUserType()+"  서버 addr"+viewModel.getSvaddr());

        binding = FragmentSelectChildBinding.inflate(inflater,container,false);
        View view = binding.getRoot();
        viewModel.setLocked(true);

        //nok이면 default가 grid
        if (viewModel.getUserType().equals("Nok")){
            set_UI_listORgrid();
        }
        else
            isGrid=false;

        //default
        binding.layoutForNoChild.setVisibility(View.GONE);
        binding.layoutForSelectChild.setVisibility(View.VISIBLE);



        recyclerView = view.findViewById(R.id.recyclerview_select_child);
        recyclerView.setHasFixedSize(true); // 리사이클러뷰 성능항상
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager); //레이아웃 연결


        //NOK: userID로 목록 가져오기
        getNokChildInfoFromServer(viewModel.getUserID());

        //아동 없을 시 -> 바로가기!
        binding.btnScAddChild.setOnClickListener(v -> ((MainActivity) requireActivity()).addStackFragment(new AddChildFragment()));     //아동 추가
        binding.btnScLinkChild.setOnClickListener(v -> ((MainActivity) requireActivity()).addStackFragment(new LinkChildFragment()));   //아동 연동

        binding.btnScListOrGrid.setOnClickListener(v -> set_UI_listORgrid());       //list OR grid 전환
        binding.btnScBack.setOnClickListener(v -> getActivity().onBackPressed());   //뒤로가기

        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void set_UI_listORgrid(){
        if(isGrid) {
            isGrid = false;
            binding.btnScListOrGrid.setImageResource(R.drawable.abtn_grid);
            getNokChildInfoFromServer(viewModel.getUserID());
        }
        else {
            isGrid = true;
            binding.btnScListOrGrid.setImageResource(R.drawable.abtn_list);
            getNokChildInfoFromServer(viewModel.getUserID());
        }
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
                            
                            //목록 길이 = 0일 시, 없음 레이아웃 뜨도록
                            if(response.body().size()==0) {
                                binding.ivScHeader.setImageResource(R.drawable.selecchild_header_center);
                                binding.btnScListOrGrid.setVisibility(View.GONE);
                                binding.layoutForNoChild.setVisibility(View.VISIBLE);
                                binding.layoutForSelectChild.setVisibility(View.GONE);

                                if (viewModel.getUserType().equals("Nok")) {
                                    binding.btnScAddChild.setVisibility(View.VISIBLE);
                                    binding.tvScDesc.setText("학습할 아동을 추가하세요");
                                    
                                } else if (viewModel.getUserType().equals("Path")){
                                    binding.btnScLinkChild.setVisibility(View.VISIBLE);
                                    binding.tvScDesc.setText("학습할 아동을 연동하세요");
                                }
                            }
                            //목록 길이 != 0일 시 리스트 표시
                            else 
                                childSelectListView(response.body());
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

    /////////////////recyclerview용//////////////////
    private void childSelectListView(List<ChildDto_forUSE> childList){
        if (childList != null) {

            if(isGrid){
                ChildListBiggerAdapter childListAdapter = new ChildListBiggerAdapter(childList, this, isGrid);
                recyclerView.setAdapter(childListAdapter);
            }
            else{
                ChildListAdapter childListAdapter = new ChildListAdapter(childList, this, isGrid);
                recyclerView.setAdapter(childListAdapter);
            }
        } else {
            Log.e("childSelectListView", "등록된 아동이 없습니다.");
            Toast.makeText(getActivity(), "등록된 아동이 없습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(ChildDto_forUSE childDto) {
        String message = childDto.getName()+" 아동을 선택했습니다.";
//        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        viewModel.setSltd_childdto(childDto);
        Log.e("clickRECYCLERVIEW","아동 선택: "+childDto.getName()
                +childDto.getBirth()
                +childDto.getGender()
                +childDto.getDisabilityType()
                +childDto.getDisabilityType());

        viewModel.setIsVideoList(false);
        ((MainActivity) requireActivity()).addStackFragment(new CardListFragment());
    }
}