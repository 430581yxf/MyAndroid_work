package com.example.myapplicationui.MainPager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.myapplicationui.ChatModel.Fragment1;
import com.example.myapplicationui.R;
import com.example.myapplicationui.entity.User;
import com.example.myapplicationui.music.Fragment4;
import com.example.myapplicationui.playVedios.Fragment3;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private List<String> viewList;
    private List<ImageView> imageViewList;
    private int[] iconList=new int[4];

    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent=getIntent();
        User user= (User) intent.getSerializableExtra("user");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager2 = findViewById(R.id.viewPager);
fragmentList=new ArrayList<>();
        tabLayout=findViewById(R.id.tableLayout);
        viewList=new ArrayList<>();
        imageViewList =new ArrayList<>();

        //四个页面写死了
        iconList[0]=R.mipmap.shouyeshouye;
        iconList[1]=R.mipmap.sousuo;
        iconList[2]=R.mipmap.shezhi;
        iconList[3]=R.mipmap.wode;
        viewList.add("友聊");//不想再该图标了
        viewList.add("搜索");
        viewList.add("短视");
        viewList.add("音乐");
        Fragment fragment1=new Fragment1(user);
        Fragment fragment2=new SqllitFragment(user);
        Fragment fragment3=new Fragment3(user);
        Fragment fragment4=new Fragment4(user);
        fragmentList.add(fragment1);
        fragmentList.add(fragment2);
        fragmentList.add(fragment3);
        fragmentList.add(fragment4);

        Typeface font = Typeface.createFromAsset(getResources().getAssets(), "iconfont/iconfont.ttf");
        MyFragmentStateAdapter myFragmentAdapter=new MyFragmentStateAdapter(getSupportFragmentManager(),getLifecycle());
        viewPager2.setAdapter(myFragmentAdapter);
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                // 设置 Tab 的标题
                tab.setText(viewList.get(position));
                tab.setIcon( iconList[position]);
            }
        }).attach();
        for (int i = 0; i <tabLayout.getChildCount(); i++) {
            //相当于每个tab的item
            View tabView=tabLayout.getChildAt(i);
            tabView.setPadding(0,0,0,0);
            TabLayout.LayoutParams layoutParams=(TabLayout.LayoutParams)tabView.getLayoutParams();
            layoutParams.setMargins(0,0,0,0);

            tabLayout.getChildAt(i).setLayoutParams(layoutParams);

        }
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
    }

    //Fragment适配器
   public class MyFragmentStateAdapter extends FragmentStateAdapter {


       public MyFragmentStateAdapter(@NonNull FragmentActivity fragmentActivity) {
           super(fragmentActivity);
       }

       public MyFragmentStateAdapter(@NonNull Fragment fragment) {
           super(fragment);
       }

       public MyFragmentStateAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
           super(fragmentManager, lifecycle);
       }

       @NonNull
       @Override
       public Fragment createFragment(int position) {
           return fragmentList.get(position);
       }

       @Override
       public int getItemCount() {
           return fragmentList.size();
       }
   }
//   显示页面下面的提示table

    public class MyAdapter extends PagerAdapter{

            @Override
            public int getCount() {
                return imageViewList.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view==object;
            }

        @NonNull
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(imageViewList.get(position));
            return imageViewList.get(position);
        }


        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(imageViewList.get(position));
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return viewList.get(position);
        }
    }
}