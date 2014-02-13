package com.yooiistudios.morningkit.setting.info.moreinfo.license;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yooiistudios.morningkit.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lombok.Getter;

/**
 * Created by StevenKim in MNSettingActivityProject from Yooii Studios Co., LTD. on 2014. 1. 8.
 *
 * MNLicenseListViewAdapter
 */
public class MNLicenseListAdapter extends BaseAdapter {
    private Context context;
    private MNLicenseListAdapter() {}
    public MNLicenseListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        // morning kit
        // gson
        // lombok
        // otto
        // butterknife
        // robolectric
        // Flickr
        // WWO
        return 8;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.setting_more_info_license_item, parent, false);

        MNSettingCreditItemViewHolder viewHolder = new MNSettingCreditItemViewHolder(convertView);

        switch (position) {
            case 0:
                viewHolder.getTitleTextView().setText("Morning Kit");
                viewHolder.getLinkTextView().setText("http://yooiistudios.com/termservice.html");
                viewHolder.getDetailTextView().setText("Yooii Studios Co., LTD.");
                break;
            case 1:
                viewHolder.getTitleTextView().setText("gson");
                viewHolder.getLinkTextView().setText("http://code.google.com/p/google-gson/");
                viewHolder.getDetailTextView().setText("Apache License 2.0");
                break;
            case 2:
                viewHolder.getTitleTextView().setText("lombok");
                viewHolder.getLinkTextView().setText("http://projectlombok.org/");
                viewHolder.getDetailTextView().setText("Copyright © 2009-2013 The Project Lombok Authors, licensed under the MIT license.");
                break;
            case 3:
                viewHolder.getTitleTextView().setText("otto");
                viewHolder.getLinkTextView().setText("http://square.github.io/otto/");
                viewHolder.getDetailTextView().setText("Copyright 2013 Square, Inc.\n" +
                        "\n" +
                        "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                        "you may not use this file except in compliance with the License.\n" +
                        "You may obtain a copy of the License at\n" +
                        "\n" +
                        "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                        "\n" +
                        "Unless required by applicable law or agreed to in writing, software\n" +
                        "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                        "See the License for the specific language governing permissions and\n" +
                        "limitations under the License.");
                break;
            case 4:
                viewHolder.getTitleTextView().setText("butterknife");
                viewHolder.getLinkTextView().setText("http://jakewharton.github.io/butterknife/");
                viewHolder.getDetailTextView().setText("Copyright 2013 Jake Wharton\n" +
                        "\n" +
                        "Licensed under the Apache License, Version 2.0 (the \"License\");\n" +
                        "you may not use this file except in compliance with the License.\n" +
                        "You may obtain a copy of the License at\n" +
                        "\n" +
                        "   http://www.apache.org/licenses/LICENSE-2.0\n" +
                        "\n" +
                        "Unless required by applicable law or agreed to in writing, software\n" +
                        "distributed under the License is distributed on an \"AS IS\" BASIS,\n" +
                        "WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.\n" +
                        "See the License for the specific language governing permissions and\n" +
                        "limitations under the License.");
                break;
            case 5:
                viewHolder.getTitleTextView().setText("robolectric");
                viewHolder.getLinkTextView().setText("http://robolectric.org/");
                viewHolder.getDetailTextView().setText("The MIT License\n" +
                        "\n" +
                        "Copyright (c) 2010 Xtreme Labs and Pivotal Labs\n" +
                        "\n" +
                        "Permission is hereby granted, free of charge, to any person obtaining a copy\n" +
                        "of this software and associated documentation files (the \"Software\"), to deal\n" +
                        "in the Software without restriction, including without limitation the rights\n" +
                        "to use, copy, modify, merge, publish, distribute, sublicense, and/or sell\n" +
                        "copies of the Software, and to permit persons to whom the Software is\n" +
                        "furnished to do so, subject to the following conditions:\n" +
                        "\n" +
                        "The above copyright notice and this permission notice shall be included in\n" +
                        "all copies or substantial portions of the Software.\n" +
                        "\n" +
                        "THE SOFTWARE IS PROVIDED \"AS IS\", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR\n" +
                        "IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,\n" +
                        "FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE\n" +
                        "AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER\n" +
                        "LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,\n" +
                        "OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN\n" +
                        "THE SOFTWARE.");
                break;
            case 6:
                viewHolder.getTitleTextView().setText("Flickr APIs Terms of Use");
                viewHolder.getLinkTextView().setText("http://www.flickr.com/services/api/tos/");
                viewHolder.getInnerLayout().removeView(viewHolder.getDetailTextView());
                break;
            case 7:
                viewHolder.getTitleTextView().setText("World Weather Online API Terms and Conditions");
                viewHolder.getLinkTextView().setText("http://www.worldweatheronline.com/api-t-and-c.aspx");
                viewHolder.getInnerLayout().removeView(viewHolder.getDetailTextView());
                break;
        }
        return convertView;
    }

    /**
     * ViewHolder
     */
    static class MNSettingCreditItemViewHolder {
        @Getter @InjectView(R.id.setting_more_info_license_item_inner_layout)    RelativeLayout innerLayout;
        @Getter @InjectView(R.id.setting_more_info_license_item_title_textview)  TextView titleTextView;
        @Getter @InjectView(R.id.setting_more_info_license_item_link_textview)   TextView linkTextView;
        @Getter @InjectView(R.id.setting_more_info_license_item_detail_textview) TextView detailTextView;

        public MNSettingCreditItemViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
