package com.example.weeseed_test.cardlist;

import android.app.AlertDialog;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.weeseed_test.R;
import com.example.weeseed_test.card.CreateAacCardFragment;
import com.example.weeseed_test.card.CreateVideoCardFragment;
import com.example.weeseed_test.card.UpdateAacCardFragment;
import com.example.weeseed_test.card.ViewerAacFragment;
import com.example.weeseed_test.card.ViewerVideoFragment;
import com.example.weeseed_test.card.adpater.LabelColorAdapter;
import com.example.weeseed_test.cardlist.adapter.CardList_Video_Adapter;
import com.example.weeseed_test.cardlist.adapter.CardList_Aac_Adapter;
import com.example.weeseed_test.databinding.FragmentCardListBinding;
import com.example.weeseed_test.dto.Aac_item;
import com.example.weeseed_test.dto.AacDto_url;
import com.example.weeseed_test.dto.Aac_default;
import com.example.weeseed_test.dto.LabelColor;
import com.example.weeseed_test.dto.VideoDto;
import com.example.weeseed_test.dto.Video_item;
import com.example.weeseed_test.setting.SettingFragment;
import com.example.weeseed_test.activity.MainActivity;
import com.example.weeseed_test.util.IsOkDialog_round;
import com.example.weeseed_test.util.RetrofitService3;
import com.example.weeseed_test.util.Viewmodel;
import com.example.weeseed_test.util.RetrofitAPI;
import com.example.weeseed_test.util.RetrofitService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*mainActi의 homeFragment*/
public class CardListFragment extends Fragment implements WebCardItemClickListener,
        VideoCardItemClickListener {

    Viewmodel viewModel;
    private FragmentCardListBinding b;

    private List<Aac_item> aacItemList, filtered_aacList;   //aacList창에서 보이는 모든 item : addAac + default(변환됨) + 일반 aac
    private List<Video_item> videoItemList, filtered_vidList;    //addVid + 일반 vid

    private boolean isDelete_mode;
    private List<Aac_item> deleteAacList; //card ID
    private List<Video_item> deleteVidList;
    IsOkDialog_round dialog;

    CardList_Aac_Adapter aacListAdapter;
    CardList_Video_Adapter vidListAdapter;
    private LabelColorAdapter labelColorAdapter, alignAdapter;
    String selectedLabelColorCode;
    Align selectedOrder;
    enum Align{DEFAULT, LATEST, COLOR, MAKER}
    private String[] longClickTexts = {"수정하기","삭제하기"};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        b = FragmentCardListBinding.inflate(inflater, container, false);
        View view = b.getRoot();

        viewModel = ((MainActivity) requireActivity()).getViewModel();
        Log.e("CardListFragment", "30.1userID: " + viewModel.getUserID() + "  세션ID: " + viewModel.getSessionID() + "  유저 타입:  " + viewModel.getUserType());

        setupInit();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupRecyclerView();
        set_UI_accORvid(viewModel.getIsVideoList(),false);        //리스트 종류에 따라 view setting
        set_UI_dialog();
        setupButtons();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        binding = null; // 버그때문에 잠시 주석
    }

    ////view setup func
    private void setupInit() {
        //list init
        aacItemList = new ArrayList<>();
        aacItemList.add(new Aac_item(1));
        videoItemList = new ArrayList<>();
        videoItemList.add(new Video_item(1));
        deleteAacList = new ArrayList<>();
        deleteVidList = new ArrayList<>();
        isDelete_mode = false;
        selectedLabelColorCode = "ALL";
        selectedOrder = Align.DEFAULT;

        if (viewModel.getLocked()) set_UI_lock();
        else set_UI_unlock();
    }
    private void setupButtons() {
        //////////////버튼 리스너///////////////
        b.btnSetting.setOnClickListener(v -> ((MainActivity) requireActivity()).addStackFragment(new SettingFragment())); //설정
        b.btnGoToVideoList.setOnClickListener(v -> {  //aac or vid 목록 전환
            if (viewModel.getIsVideoList()) viewModel.setIsVideoList(false);
            else viewModel.setIsVideoList(true);
            ((MainActivity) requireActivity()).replaceFragment(new CardListFragment());
        });
        //잠금 버튼 리스너
        b.btnLock.setOnClickListener(v -> {
            if (viewModel.getLocked())
                ((MainActivity) requireActivity()).addStackFragment(new LockFragment());
            else {
                viewModel.setLocked(true);
                ((MainActivity) requireActivity()).replaceFragment(new CardListFragment());
            }
        });
        //뒤로가기 버튼
        b.btnBackCardList.setOnClickListener(v -> {
            if (isDelete_mode)
                offDeleteMode();//삭제 모드 나가기
            else
                getActivity().onBackPressed();
        });
        //삭제 버튼: 모드 진입, 삭제 dialog 표시, 삭제 모드 나가기

        //filter
        b.btnClSearch.setOnClickListener(v -> searchModule(b.etClSearch.getText().toString(),selectedLabelColorCode,viewModel.getIsVideoList()));
        b.btnClFilter.setOnClickListener(v -> b.spinnerClAacLabel.performClick());
        b.btnClAlign.setOnClickListener(v -> b.spinnerClLabelAlign.performClick());

        //search 엔터버튼
        b.etClSearch.setOnEditorActionListener((v, actionId, event) ->{
            if(actionId == EditorInfo.IME_ACTION_DONE){
                b.btnClSearch.performClick();
                return true;
            }
            return false;
        });
        //spinner1
        labelColorAdapter = new LabelColorAdapter(getContext(),true);
        b.spinnerClAacLabel.setAdapter(labelColorAdapter);
        b.spinnerClAacLabel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LabelColor selectedLabelColor = (LabelColor) parent.getItemAtPosition(position);

                if(selectedLabelColor.getName().equals("전체")) selectedLabelColorCode = "ALL";   //all case 처리
                else {
                    int colorInt = ContextCompat.getColor(getContext(), selectedLabelColor.getColor());
                    selectedLabelColorCode = String.format("%06X",(0xFFFFFF & colorInt));
                }
                Log.e("spinnerClLabel", "[선택색상]" + selectedLabelColor.getName() + " "+selectedLabelColorCode);
                //검색
                searchModule(b.etClSearch.getText().toString(),selectedLabelColorCode, viewModel.getIsVideoList());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {selectedLabelColorCode = "ALL";}// 아무 것도 선택되지 않은 경우
        });
        //spinner2
        alignAdapter = new LabelColorAdapter(getContext(), 1);
        b.spinnerClLabelAlign.setAdapter(alignAdapter);
        b.spinnerClLabelAlign.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                LabelColor selectedItem = (LabelColor) parent.getItemAtPosition(position);
                selectedOrder = getAlignOrder(selectedItem.getName());

                Comparator<?> comparator = getComparatorForOrder(selectedOrder, viewModel.getIsVideoList());


                if(comparator != null){
                    if (!viewModel.getIsVideoList()) {
                        Aac_item tempAdd = aacItemList.get(0);
                        aacItemList.remove(0);
                        Collections.sort(aacItemList, (Comparator<Aac_item>) comparator);
                        aacItemList.add(0,tempAdd);

                    }
                    else {
                        Video_item tempAdd = videoItemList.get(0);
                        videoItemList.remove(0);
                        Collections.sort(videoItemList, (Comparator<Video_item>) comparator);
                        videoItemList.add(0,tempAdd);
                    }
                }
                updateModule(aacItemList, videoItemList);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedOrder = Align.DEFAULT;
                updateModule(aacItemList, videoItemList);
            }
        });

        b.btnDelete.setOnClickListener(v -> onDeleteMode());
        b.btnSelectAll.setOnClickListener(v -> selectAllModule(true));
        b.btnSelectAllNOT.setOnClickListener(v -> selectAllModule(false));
    }

    private void set_UI_dialog() {
        //for delete card

        dialog = new IsOkDialog_round(getActivity());
        dialog.setDialogContent(
                "카드 삭제 확인",
                "선택한 카드를 삭제하시겠습니까?\n" + "삭제된 카드는 복구할 수 없습니다.",
                "취소",
                "확인",
                1, 1);
        dialog.setOnDialogButtonClickListener(new IsOkDialog_round.OnDialogButtonClickListener() {
            @Override
            public void onLeftButtonClick() {
            }  //걍 그대로

            @Override
            public void onRightButtonClick() {
                requestDelete();
            }
        });
    }


    /////////COMPLETED22222////////////
    private void onDeleteMode() {
        if (isDelete_mode) {checkForDelete();return;}   //delete 모드에서 버튼 재클릭    => off/diag

        //delete 모드 진입
        isDelete_mode = true;
        toggleDeleteBtnsStats();

        if (!viewModel.getIsVideoList())
            for (Aac_item item : aacItemList) {
                item.setCheckBoxVisible(true);
                item.setSelected(false);
            }
        else
            for (Video_item item : videoItemList) {
                item.setCheckBoxVisible(true);
                item.setSelected(false);
            }
        updateModule(aacItemList, videoItemList);
    }
    private void checkForDelete(){
        deleteAacList.clear();
        deleteVidList.clear();

        if (!viewModel.getIsVideoList()){
            for(Aac_item item : aacItemList)
                if(item.isSelected())
                    deleteAacList.add(item);

            if(!deleteAacList.isEmpty())
                dialog.show();
            else offDeleteMode();
        }
        else {
            for(Video_item item : videoItemList)
                if(item.isSelected())
                    deleteVidList.add(item);

            if(!deleteVidList.isEmpty())
                dialog.show();
            else offDeleteMode();
        }
    }
    private void offDeleteMode(){
        //삭제 모드 나가기: 삭제 모드 해제, 버튼 상태 토글, aac/vid 카드 목록 재호출
        isDelete_mode =false;
        toggleDeleteBtnsStats();

        for(Aac_item item:aacItemList)      {
            item.setSelected(false);
            item.setCheckBoxVisible(false);
        }
        for(Video_item item:videoItemList)  {
            item.setSelected(false);
            item.setCheckBoxVisible(false);
        }
        updateModule(aacItemList, videoItemList);
    }
    private void selectAllModule(boolean isSelectAll){
        //전체 선택 or 전체 해제
        if(!viewModel.getIsVideoList()){
            for(Aac_item item : aacItemList){
                item.setSelected(isSelectAll);
            }
        }
        else{
            for(Video_item item : videoItemList){
                item.setSelected(isSelectAll);
            }
        }
        aacItemList.get(0).setSelected(false);
        videoItemList.get(0).setSelected(false);

        updateModule(aacItemList, videoItemList);
    }
    private void updateModule(List<Aac_item> newAaclist, List<Video_item> newVidlist){
        //수정 안해도 됨!!
        if(!viewModel.getIsVideoList()){
//            aacCardListView(aacItemList);
            aacListAdapter.updateList(newAaclist);
            aacListAdapter.notifyDataSetChanged();
        }
        else{
//            videoCardListView(videoItemList);
            vidListAdapter.updateList(newVidlist);
            vidListAdapter.notifyDataSetChanged();
        }
        if(b.ltClLoadingDots.isShown()) {
            b.ltClLoadingDots.setVisibility(View.GONE);
        }
    }
    private Align getAlignOrder(String name) {
        switch (name) {
            case "최신순": return Align.LATEST;
            case "색상순": return Align.COLOR;
            case "제작자": return Align.MAKER;
            case "기본순":
            default: return Align.DEFAULT;
        }
    }
    private Comparator<?> getComparatorForOrder(Align order, boolean isVideoList) {
        if (order == Align.DEFAULT) {
            return isVideoList ? Comparator.comparingLong(Video_item::getVideoCardId) :
                    Comparator.comparingLong(Aac_item::getAacCardId);
        } else if (order == Align.LATEST) {
            return isVideoList ? Comparator.comparingLong(Video_item::getVideoCardId).reversed() :
                    Comparator.comparingLong(Aac_item::getAacCardId).reversed();
        } else if (order == Align.COLOR) {
            return isVideoList ? Comparator.comparing(Video_item::getColor).reversed() :
                    Comparator.comparing(Aac_item::getColor).reversed();
        } else if (order == Align.MAKER) {
            return isVideoList ? Comparator.comparing(Video_item::getUserId) :
                    Comparator.comparing(Aac_item::getConstructorId);
        }
        return null;
    }
    private void setupRecyclerView() {
        ///카드 목록 recycler view///
        int colomnCount = calculateNoOfColumns();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), colomnCount);  //2열 grid
        b.recyclerviewCardListGrid.setHasFixedSize(true);
        b.recyclerviewCardListGrid.setLayoutManager(gridLayoutManager); //레이아웃 연결
        b.recyclerviewCardListGrid.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy); //상하 스크롤 시 subfunc 표시방법
                float translationY = b.layoutCardListSubfunc.getTranslationY() - dy;
                if (dy > 0)//아래로 스크롤
                    b.layoutCardListSubfunc.setTranslationY(Math.max(translationY, -b.layoutCardListSubfunc.getHeight()));
                else if (dy < 0)//위로 스크롤
                    b.layoutCardListSubfunc.setTranslationY(Math.min(translationY, 0));
            }
        });
    }

    //// ui detail
    private void set_UI_lock() {
        viewModel.setLocked(true);
        //잠금모드 ver로 레이아웃 수정
        b.btnBackCardList.setVisibility(View.GONE);
        b.btnSetting.setVisibility(View.GONE);
        b.btnGoToVideoList.setVisibility(View.GONE);
        b.layoutCardListSubfunc.setVisibility(View.GONE);
        b.btnDelete.setVisibility(View.GONE);

        b.recyclerviewCardListGrid.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                //relist 상하단 여백 설정.
                int topmargin = 20;
                int bottommargin = 20;

                int position = parent.getChildAdapterPosition(view);
                int itemCount = state.getItemCount();

                if (position == 0) outRect.top = topmargin;
                if (position == itemCount - 1) outRect.bottom = bottommargin;

                if (itemCount >= 2 && position == 1) outRect.top = topmargin;
                if (itemCount % 2 == 0 && position == itemCount - 2) outRect.bottom = bottommargin;
            }
        });

        b.btnLock.setImageResource(R.drawable.abtn_unlock);
    }
    private void set_UI_unlock() {

        b.btnBackCardList.setVisibility(View.VISIBLE);
        b.btnSetting.setVisibility(View.VISIBLE);
        b.btnGoToVideoList.setVisibility(View.VISIBLE);
        b.btnDelete.setVisibility(View.VISIBLE);
        b.layoutCardListSubfunc.setVisibility(View.VISIBLE);
        b.recyclerviewCardListGrid.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                //relist 상하단 여백 설정.
                int topmargin = 400;
                int bottommargin = 190;

                int position = parent.getChildAdapterPosition(view);
                int itemCount = state.getItemCount();

                if (position == 0) outRect.top = topmargin;
                if (position == itemCount - 1) outRect.bottom = bottommargin;

                if (itemCount >= 2 && position == 1) outRect.top = topmargin;
                if (itemCount % 2 == 0 && position == itemCount - 2) outRect.bottom = bottommargin;
            }
        });

        aacItemList.get(0).setColor("FFCE1B");
        videoItemList.get(0).setColor("FFCE1B");
    }
    private void set_UI_accORvid(Boolean isVideoList, boolean isUpdate) {
        if (!isVideoList) {               //aaclist일 경우
            b.ivClBear.setImageResource(R.drawable.bear_speak2);
            b.btnGoToVideoList.setText("따라해요");
            get_AACCard_default(viewModel.getUserID(), isUpdate);
        } else {    //vidlist일 경우
            b.ivClBear.setImageResource(R.drawable.bear_together2);
            b.btnGoToVideoList.setText("말해봐요");
            get_VideoCard(viewModel.getSltd_childdto().getChildCode(), viewModel.getUserID(), isUpdate);
        }
    }

    private void toggleDeleteBtnsStats(){
        if(isDelete_mode){
            b.btnSetting.setVisibility(View.GONE);
            b.btnLock.setVisibility(View.GONE);
            b.btnGoToVideoList.setVisibility(View.GONE);

            b.btnSelectAll.setVisibility(View.VISIBLE);
            b.btnSelectAllNOT.setVisibility(View.VISIBLE);
        }
        else {
            b.btnSetting.setVisibility(View.VISIBLE);
            b.btnLock.setVisibility(View.VISIBLE);
            b.btnGoToVideoList.setVisibility(View.VISIBLE);

            b.btnSelectAll.setVisibility(View.GONE);
            b.btnSelectAllNOT.setVisibility(View.GONE);
        }

    }


    /////[AAC/VID] card item click listener////
    @Override
    public void onItemClick_aac(Aac_item item) {
        viewModel.setSltd_aacItem(item);  // aac/ 선택 카드로 설정
        if(item.getCardType()==1)
        {
            if(isDelete_mode)
            { Toast.makeText(getActivity(), "삭제 모드를 해제하세요.", Toast.LENGTH_SHORT).show(); return; }
            ((MainActivity) requireActivity()).addStackFragment(new CreateAacCardFragment());   //add aac
        }
        else{
            Log.e("click_aac","[VM]카드 선택: "+ viewModel.getSltd_aacItem().getCardName() +"  type: "+viewModel.getSltd_aacItem().getCardType());
            ((MainActivity) requireActivity()).addStackFragment(new ViewerAacFragment());       //선택한 카드의 viewer frag 표시
        }
    }
    @Override
    public void onItemClick_vid(Video_item item) {
        viewModel.setSltd_videoCard(item);  // vid/ 선택 카드로 설정
        if(item.getChildId().equals("add"))
        {
            if(isDelete_mode)
            { Toast.makeText(getActivity(), "삭제 모드를 해제하세요.", Toast.LENGTH_SHORT).show(); return; }
            ((MainActivity) requireActivity()).addStackFragment(new CreateVideoCardFragment()); //add vid
        }
        else {
            Log.e("click_vid","카드 선택: "+item.getCardName() +"  카드ID: "+item.getVideoCardId().toString() +"  썸네일URL: "+item.getThumbnailUrl() +"  비디오URL: "+item.getVideoUrl());
            ((MainActivity) requireActivity()).addStackFragment(new ViewerVideoFragment());        //선택한 카드의 viewer frag 표시
        }
    }
    @Override
    public void onItemClick_aacDelete(Aac_item aacItem, boolean isSelected) {
        if (aacItem == aacItemList.get(0)) { Toast.makeText(getActivity(), "삭제 모드를 해제하세요.", Toast.LENGTH_SHORT).show(); return; }

        for (Aac_item item : aacItemList) {
            if (item == aacItem) {
                item.setSelected(isSelected);
                aacListAdapter.updateList(aacItemList);
                Log.e("click_aac[DEL]", "[DEL] 삭제할 카드: "+aacItem.getCardName());
            }
        }
    }
    @Override
    public void onItemClick_vidDelete(Video_item videoItem, boolean isSelected) {
        if(videoItem == videoItemList.get(0)) {Toast.makeText(getActivity(),"삭제 모드를 해제하세요.", Toast.LENGTH_SHORT).show();return;}

        for (Video_item item : videoItemList) {
            if (item == videoItem) {
                item.setSelected(isSelected);
                vidListAdapter.updateList(videoItemList);
                Log.e("click_vid[DEL]", "[DEL] 삭제할 카드: "+videoItem.getCardName());
            }
        }
    }

    //longClick:
    @Override
    public void onItemLongClick_aac(Aac_item item){
        if(viewModel.getLocked()) return;


        viewModel.setSltd_aacItem(item);  // aac/ 선택 카드로 설정
        if(item.getCardType()==1)
            ((MainActivity) requireActivity()).addStackFragment(new CreateAacCardFragment());   //add aac
        else {
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setItems(longClickTexts, (dialogInterface, which) -> {
                        if(longClickTexts[which].equals("수정하기")){
                            ((MainActivity) requireActivity()).addStackFragment(new UpdateAacCardFragment());
                        }
                        else if(longClickTexts[which].equals("삭제하기")){
                            if(!deleteAacList.isEmpty())
                                deleteAacList.clear();
                            deleteAacList.add(item);
                            dialog.show();
                        }
                    })
                    .create();
            alertDialog.show();
        }
    }
    @Override
    public void onItemLongClick_vid(Video_item item) {
        if(viewModel.getLocked()) return;

        viewModel.setSltd_videoCard(item);  // vid/ 선택 카드로 설정
        if(item.getChildId().equals("add"))
            ((MainActivity) requireActivity()).addStackFragment(new CreateVideoCardFragment()); //add vid
        else{
            AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                    .setItems(longClickTexts, (dialogInterface, which) -> {
                        if(longClickTexts[which].equals("수정하기")){
                            Toast.makeText(getActivity(), "비디오카드는 수정할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        else if(longClickTexts[which].equals("삭제하기")){
                            if(!deleteVidList.isEmpty())
                                deleteVidList.clear();
                            deleteVidList.add(item);
                            dialog.show();
                        }
                    })
                    .create();
            alertDialog.show();

        }
    }


    /////////recyclerview
    private void aacCardListView(List<Aac_item> aacItemList){
        aacListAdapter =new CardList_Aac_Adapter(aacItemList);
        aacListAdapter.setOnDataChangedListener(this);
        b.recyclerviewCardListGrid.setAdapter(aacListAdapter);
        
        b.layoutCardListSubfunc.setTranslationY(0);
        if(b.ltClLoadingDots.isShown())
            b.ltClLoadingDots.setVisibility(View.GONE);  //로딩 eff
    }
    private void videoCardListView(List<Video_item> videoItemList2){
        vidListAdapter =new CardList_Video_Adapter(videoItemList2);
        vidListAdapter.setOnDataChangedListener(this);
        b.recyclerviewCardListGrid.setAdapter(vidListAdapter);

        b.layoutCardListSubfunc.setTranslationY(0);
        if(b.ltClLoadingDots.isShown()) {
            b.ltClLoadingDots.setVisibility(View.GONE);
        }
    }





    /////////COMPLETED////////////


    /////

    private int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        float itemMinWidthDp = 160; // 최소 너비
        float totalHorizontalMarginDp = 40; // 좌우 margin
        float itemSpacingDp = 5; // 아이템 간 간격
        int maxColumnCount = (int) ((dpWidth - totalHorizontalMarginDp) / (itemMinWidthDp + itemSpacingDp));
        return Math.max(1, maxColumnCount); // 최소 2열
    }
    private void searchModule(String keyword, String color, boolean isVideoList){
        //null이나 empty면 all
        if (keyword == null || keyword.trim().isEmpty()) keyword = "ALL";
        if (color == null || color.trim().isEmpty()) color = "ALL";

        if(!isVideoList) {
            filtered_aacList = search_aac(keyword, color);
            aacCardListView(filtered_aacList);
            aacListAdapter.notifyDataSetChanged();
        }
        else {
            filtered_vidList = search_vid(keyword, color);
            videoCardListView(filtered_vidList);
            vidListAdapter.notifyDataSetChanged();
        }
    }
    private List<Aac_item> search_aac(String keyword, String color){
        List<Aac_item> res = new ArrayList<>();
        if (keyword.equals("ALL")&&color.equals("ALL")) {
            return aacItemList;
        }
        else
        {
            for(Aac_item item : aacItemList) {
                boolean matchesKeyword = keyword.equals("ALL")  || item.getCardName().contains(keyword);
                boolean matchesColor = color.equals("ALL")      || item.getColor().equals(color);

                if (keyword.equals("ALL") && matchesColor)      res.add(item);
                else if (color.equals("ALL") && matchesKeyword) res.add(item);
                else if (matchesKeyword && matchesColor)        res.add(item);
            }
            res.add(0, new Aac_item(1));
        }
        return res;
    }
    private List<Video_item> search_vid(String keyword, String color){
        List<Video_item> res = new ArrayList<>();
        if (keyword.equals("ALL")&&color.equals("ALL")) {
            return videoItemList;
        }
        else
        {
            for(Video_item item : videoItemList) {
                boolean matchesKeyword = keyword.equals("ALL")  || item.getCardName().contains(keyword);
                boolean matchesColor = color.equals("ALL")      || item.getColor().equals(color);

                if (keyword.equals("ALL") && matchesColor)      res.add(item);
                else if (color.equals("ALL") && matchesKeyword) res.add(item);
                else if (matchesKeyword && matchesColor)        res.add(item);
            }
            res.add(0,new Video_item(1));
        }
        return res;

    }


    /////////////////////////////aac default, aac, vid 카드목록 불러오기/////////////////////////////
    private void get_AACCard_default(String constructorId, boolean isUpdate){
        b.ltClLoadingDots.setVisibility(View.VISIBLE);  //로딩 eff

        RetrofitService retrofitService = new RetrofitService(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);
        Log.e("getAACCardFromServerD", "2: ");

        try{
            retrofitAPI.getAacCards_default(constructorId)
                    .enqueue(new Callback<List<Aac_default>>() {
                        @Override
                        public void onResponse(Call<List<Aac_default>> call, Response<List<Aac_default>> response) {
                            //def list를 저장
                            if(!response.body().isEmpty()) {
                                for(Aac_default a : response.body()){
                                    Aac_item item = new Aac_item(a);
                                    aacItemList.add(item);
                                }
                            }
                            Log.e("getAACCardFromServerD", "def성공3: ");
                            //초기화: AAC 카드 목록 가져오기
                            get_AACCard(viewModel.getSltd_childdto().getChildCode(), viewModel.getUserID(), isUpdate);  //callback에서 recyclerView에 로드.

                        }
                        @Override
                        public void onFailure(Call<List<Aac_default>> call, Throwable t) {
                            Log.e("getAACCardFromServerD_ERR", t.getMessage());
                            Toast.makeText(getActivity(), "기본이미지 로드 중 오류 발생", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch (Exception e){
            Log.e("getAACCardFromServerD_ERR","ERR) "+e.getMessage());
            Toast.makeText(getActivity(), "기본이미지 로드 중 오류 발생", Toast.LENGTH_SHORT).show();
        }
    }
    private void get_AACCard(String childCode, String constructorId, boolean isUpdate){
        //
        if(viewModel.getLocked()&&!aacItemList.isEmpty())
            if(aacItemList.get(0).getCardType()==1) //잠금모드에선 미표시
                aacItemList.remove(0);
        //
        RetrofitService retrofitService = new RetrofitService(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);
        Log.e("getAACCardFromServer", "2: ");

        try{
            retrofitAPI.getAacCards_web(childCode,constructorId)
                    .enqueue(new Callback<List<AacDto_url>>() {
                        @Override
                        public void onResponse(Call<List<AacDto_url>> call, Response<List<AacDto_url>> response) {
                            Log.e("getAACCardFromServer", "3: ");
                            //def list를 저장
                            for(AacDto_url a : response.body()){
                                Aac_item item = new Aac_item(a);
                                aacItemList.add(item);
                            }
                            if(isUpdate)
                                ((MainActivity) requireActivity()).replaceFragment(new CardListFragment());
                            else
                                aacCardListView(aacItemList);
                            Log.e("getAACCardFromServer", "4: "+aacItemList.size());
                        }
                        @Override
                        public void onFailure(Call<List<AacDto_url>> call, Throwable t) {
                            Log.e("getAACCardFromServerERR", "ERR5: "+ t.getMessage());
                            Toast.makeText(getActivity(), "카드 목록 로드 실패", Toast.LENGTH_SHORT).show();
                            aacCardListView(aacItemList);   //이땐 add + def만 호출
                        }
                    });
        }
        catch (Exception e){
            Log.e("getAACCardFromServerERR","ERR) "+e.getMessage());
            aacCardListView(aacItemList);   //이땐 add + def만 호출
        }
    }


    private void get_VideoCard(String childCode, String constructorId, boolean isUpdate){
        b.ltClLoadingDots.setVisibility(View.VISIBLE);  //로딩 eff

        //
        if(viewModel.getLocked()&&!videoItemList.isEmpty())
            if(videoItemList.get(0).getChildId().equals("add")) //잠금모드에선 미표시
                videoItemList.remove(0);
        //

        RetrofitService retrofitService = new RetrofitService(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI = retrofitService.getRetrofit().create(RetrofitAPI.class);
        Log.e("getVideoCards", "2: ");

        try{
            retrofitAPI.getVideoCards(childCode,constructorId)
                    .enqueue(new Callback<List<VideoDto>>() {
                        @Override
                        public void onResponse(Call<List<VideoDto>> call, Response<List<VideoDto>> response) {
                            for(VideoDto a : response.body()){
                                Video_item item = new Video_item(a);
                                videoItemList.add(item);
                            }
                            if(isUpdate)
                                ((MainActivity) requireActivity()).replaceFragment(new CardListFragment());
                            else
                                videoCardListView(videoItemList);
                            Log.e("getVideoCards", "3: ");
                        }
                        @Override
                        public void onFailure(Call<List<VideoDto>> call, Throwable t) {
                            Log.e("getVideoCardsERR", t.getMessage());
                            Toast.makeText(getActivity(), "카드 목록 로드 실패", Toast.LENGTH_SHORT).show();
                            videoCardListView(videoItemList);
                        }
                    });
        }
        catch (Exception e){
            Log.e("getAACCardFromServerERR","ERR) "+e.getMessage());
            videoCardListView(videoItemList);
        }
    }
    /////////////////////////////1017 DELETE AAC CARD/////////////////////////////
    public void deleteAACCard2(Aac_item aacItem, DeleteCallback callback) {

        Long cardId = aacItem.getAacCardId();
        //card ID version
        RetrofitService retrofitService=new RetrofitService(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI=retrofitService.getRetrofit().create(RetrofitAPI.class);
        Log.e("deleteAACCard2","1: ");
        try{
            retrofitAPI.delete_aac2(cardId)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            callback.onComplete();
                            if (response.isSuccessful()) {
                                Log.e("deleteAACCard2","성공: " +response.code());
                            } else {
                                Log.e("deleteAACCard2","서버 오류: " +response.code());
                                Toast.makeText(getActivity(), "서버 오류: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            callback.onComplete();
                            Log.e("deleteAACCard2", t.getMessage());
                            Toast.makeText(getActivity(), "오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch (Exception e){
            callback.onComplete();
            Log.e("deleteAACCard2","ERR) "+e.getMessage());
            Toast.makeText(getActivity(), "오류", Toast.LENGTH_SHORT).show();
        }
    }
    public void deleteAACCard_default(Aac_item aacItem, DeleteCallback callback) {
        String constructorId = viewModel.getUserID();
        String cardiName = aacItem.getCardName();
        Log.e("deleteAACCard_default","0: "+constructorId+"  "+cardiName);

        String cardName;
        switch (cardiName) {
            case "아빠": cardName = "dad"; break;
            case "엄마": cardName = "mom"; break;
            case "선생님": cardName = "teacher"; break;
            case "네": cardName = "yes"; break;
            case "아니요": cardName = "no"; break;
            case "밥": cardName = "rice"; break;
            case "잠": cardName = "sleep"; break;
            case "화장실": cardName = "toilet"; break;
            case "아파요": cardName = "sick"; break;
            case "안녕하세요": cardName = "hello"; break;
            case "주세요": cardName = "giveme"; break;
            default: cardName = cardiName; break;
        }

        RetrofitService retrofitService=new RetrofitService(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI=retrofitService.getRetrofit().create(RetrofitAPI.class);
        Log.e("deleteAACCard_default","1: "+constructorId+cardName);

        try{
            retrofitAPI.delete_aac_default(constructorId, cardName)
                    .enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            callback.onComplete();
                            if (response.isSuccessful()) {
                                Log.e("deleteAACCard_default","성공: " +response.code());
                            } else {
                                // 서버 오류 등의 이유로 요청 실패
                                Log.e("deleteAACCard_default","서버 오류: " +response.code());
                                Toast.makeText(getActivity(), "서버 오류: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            callback.onComplete();
                            Log.e("deleteAACCard_default", t.getMessage());
                            Toast.makeText(getActivity(), "오류: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        catch (Exception e){
            callback.onComplete();
            Log.e("deleteAACCard_default","ERR) "+e.getMessage());
            Toast.makeText(getActivity(), "오류", Toast.LENGTH_SHORT).show();
        }
    }
    public void deleteVideoCard(Video_item videoItem, DeleteCallback callback) {
        Long cardId = videoItem.getVideoCardId();
        //card ID version
        RetrofitService3 retrofitService=new RetrofitService3(viewModel.getSvaddr());
        RetrofitAPI retrofitAPI=retrofitService.getRetrofit().create(RetrofitAPI.class);
        Log.e("deleteVideoCard","1: ");

        try{
            retrofitAPI.delete_video(cardId)
                    .enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            callback.onComplete();
                            if (response.isSuccessful()) {
                                Log.e("deleteVideoCard","성공: " +response.code());
                            } else {
                                // 서버 오류 등의 이유로 요청 실패
                                Log.e("deleteVideoCard","서버 오류: " +response.code());
                                Toast.makeText(getActivity(), "서버 오류: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            callback.onComplete();
                            Log.e("deleteVideoCard", t.getMessage());
                        }
                    });
        }
        catch (Exception e){
            callback.onComplete();
            Log.e("deleteVideoCard","ERR) "+e.getMessage());
            Toast.makeText(getActivity(), "오류", Toast.LENGTH_SHORT).show();
        }
    }
    public void requestDelete() {
        final int[] totalRequests = {0};
        final int[] completedRequests = {0};


        if (!viewModel.getIsVideoList()) {
            if (!deleteAacList.isEmpty()) {
                totalRequests[0] = deleteAacList.size();  // 요청 수

                for (int i = 0; i < totalRequests[0]; i++) {
                    Aac_item aacItem = deleteAacList.get(i);
                    int cardType = aacItem.getCardType();
                    if (cardType == 2)
                        deleteAACCard_default(aacItem, new DeleteCallback() {
                            @Override
                            public void onComplete() {
                                completedRequests[0]++;
                                checkIfAllRequestsCompleted(completedRequests[0], totalRequests[0]);
                            }
                        });
                    else if (cardType == 0)
                        deleteAACCard2(aacItem, new DeleteCallback() {
                            @Override
                            public void onComplete() {
                                completedRequests[0]++;
                                checkIfAllRequestsCompleted(completedRequests[0], totalRequests[0]);
                            }
                        });
                }
            }
        }

        // 비디오 카드 삭제 요청
        else {
            if (!deleteVidList.isEmpty()) {
                totalRequests[0] = deleteVidList.size();
                for (int i = 0; i < totalRequests[0]; i++) {
                    Video_item videoItem = deleteVidList.get(i);
                    deleteVideoCard(videoItem, new DeleteCallback() {
                        @Override
                        public void onComplete() {
                            completedRequests[0]++;
                            checkIfAllRequestsCompleted(completedRequests[0], totalRequests[0]);
                        }
                    });
                }
            }
        }

        // 삭제 완료 후 처리
        deleteAacList.clear();
        deleteVidList.clear();
    }

    public interface DeleteCallback {
        void onComplete();
    }
    private void checkIfAllRequestsCompleted(int completed, int total) {
        if (completed == total) {
            Toast.makeText(getActivity(), total+"개의 카드를 삭제했습니다.", Toast.LENGTH_SHORT).show();
            set_UI_accORvid(viewModel.getIsVideoList(),true);

            offDeleteMode();
        }
    }



}