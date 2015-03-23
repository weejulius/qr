package demo.m.qr1688;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jyu on 15-3-23.
 */
public class UserListActivity extends ActionBarActivity {

    public static final Map<String, User> users = new HashMap<>();
    private RecyclerView userListView;
    private RecyclerView.LayoutManager userListViewManager;
    private UserListAdapter userListViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_list);

        final android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.m_goto_takepic) {
                    Intent intent = new Intent(toolbar.getContext(), UserInputActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });

        userListView = (RecyclerView) findViewById(R.id.rv_user_list);

        userListView.setHasFixedSize(true);

        userListViewManager = new LinearLayoutManager(this);
        userListView.setLayoutManager(userListViewManager);


        userListViewAdapter = new UserListAdapter(new ArrayList<>(users.values()));

        userListView.setAdapter(userListViewAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        userListViewAdapter.userList = new ArrayList<>(users.values());
        userListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu optionMenu) {
        getMenuInflater().inflate(R.menu.m_user_list, optionMenu);
        return true;
    }


    public static class UserViewHolder extends RecyclerView.ViewHolder {

        public TextView userInfoView;
        public ImageView idImgView;
        public ImageView storeImgView;
        public ImageView bizImgView;
        public Button uploadButton;

        public UserViewHolder(View itemView) {
            super(itemView);

        }
    }

    public static class User implements Serializable {
        String loginId;
        String name = "";
        String idImg;
        String storeImg;
        String bizImg;
    }


    public class UserListAdapter extends RecyclerView.Adapter<UserViewHolder> {

        public List<User> userList;

        public UserListAdapter(List<User> users) {
            this.userList = users;
        }

        public void setUserList(List<User> users) {
            this.userList = users;
        }


        @Override
        public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            ViewGroup view = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.lo_user_item, parent, false);
            UserViewHolder holder = new UserViewHolder(view);
            holder.userInfoView = new TextView(parent.getContext());
            holder.userInfoView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            holder.uploadButton = new Button(parent.getContext());
            holder.uploadButton.setText("上传");
            holder.uploadButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

            return holder;
        }

        @Override
        public void onBindViewHolder(UserViewHolder holder, int position) {


            User user = userList.get(position);

            if (user == null) {
                return;
            }

            holder.userInfoView.setText(user.name + "   " + user.loginId);

            addView(holder.itemView, holder.userInfoView);

            holder.idImgView =initAndUpliadImage(user.idImg, holder, holder.idImgView, 2);

            addView(holder.itemView, holder.idImgView);
            holder.bizImgView = initAndUpliadImage(user.bizImg, holder, holder.bizImgView, 3);

            addView(holder.itemView, holder.bizImgView);
            holder.storeImgView = initAndUpliadImage(user.storeImg, holder, holder.storeImgView, 4);
            addView(holder.itemView, holder.storeImgView);
            addView(holder.itemView, holder.uploadButton);
        }

        private ImageView initAndUpliadImage(String imgFile, UserViewHolder holder, ImageView imgView, int index) {
            if (imgFile != null) {
                if (imgView == null) {
                    imgView = new ImageView(holder.itemView.getContext());

                }
                imgView.setImageURI(Uri.fromFile(new File(imgFile)));
                return imgView;

            }
            return null;
        }

        public void addView(View vg, View view) {
            if (view == null) {
                return;
            }
            ViewGroup rl = (ViewGroup) vg;
            ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rl.addView(view, p);

        }

        @Override
        public int getItemCount() {
            return userList == null ? 0 : userList.size();
        }
    }
}
