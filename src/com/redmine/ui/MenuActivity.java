package com.redmine.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MenuActivity extends ListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new MenuListAdapter(getList()));
    }

	
	static class MenuItemInfo {
		String name;
		Intent intent;

		public MenuItemInfo(String name, Intent intent) {
			this.name = name;
			this.intent = intent;
		}
	}
	
	private Intent createIntent(String pkgName, String className) {
		 Intent intent = new Intent();
		 intent.setClassName(getApplicationContext(), pkgName + "." + className);
		 return intent;
	}
	
	private List<MenuItemInfo> getList() {
        ArrayList<MenuItemInfo> list =
        		new ArrayList<MenuActivity.MenuItemInfo>();
        list.add(new MenuItemInfo("チケット一覧画面",
        			createIntent("com.redmine.ui", "TicketActivity")));
        list.add(new MenuItemInfo("このアプリについて",
        			createIntent("com.redmine.ui", "AboutApplicationActivity")));
        return list;
	}

    class MenuListAdapter extends BaseAdapter {
        private List<MenuItemInfo> mItems;

        public MenuListAdapter(List<MenuItemInfo> items) {
            mItems = items;
        }

        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(android.R.layout.simple_list_item_1,
                        parent, false);
                convertView.setTag(convertView.findViewById(android.R.id.text1));
            }
            TextView tv = (TextView) convertView.getTag();
            tv.setText(mItems.get(position).name);
            return convertView;
        }

    }
	
	
    @Override
    protected void onListItemClick(ListView lv, View v, int pos, long id) {
        MenuItemInfo info = (MenuItemInfo) getListAdapter().getItem(pos);
        startActivity(info.intent);
    }

}
