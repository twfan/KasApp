package wikdd.kasapp;


import android.app.Fragment;
import android.app.FragmentManager;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;
import wikdd.kasapp.fragment.KeluarFragment;
import wikdd.kasapp.fragment.MasukFragment;

public class TabActivity extends AppCompatActivity {

    private final String[] PAGE_TITLES = new String[]{
            "PEMASUKAN", "PENGELUARAN"
    };

    private final Fragment[] PAGES = new Fragment[]{
            new MasukFragment(), new KeluarFragment()
    };

//    @BindView(R.id.ripSimpan) TabLayout tabLayout;
//    @BindView(R.id.viewPager) ViewPager viewPager;
    TabLayout tabLayout;
    ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
//        ButterKnife.bind(this);

        viewPager.setAdapter(new MyPagerAdapter(getFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        getSupportActionBar().setTitle("Tambah");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            return PAGES[position];
        }

        @Override
        public int getCount() {
            return PAGES.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return PAGE_TITLES[position];
        }

    }


}
