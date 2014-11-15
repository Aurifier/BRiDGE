package com.caffeinecraft.bridge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ronitkumar on 11/10/14.
 */
public class ConversationViewAdapter extends ArrayAdapter{
        private final Context context;
        private final String[] values;

        public ConversationViewAdapter(Context context, String[] values) {
            super(context, R.layout.conversation_view, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.conversation_view, parent, false);
            //TODO: I had to replace this line
            //TextView textView = (TextView) rowView.findViewById(R.id.secondLine);
            //TODO: with this one because secondLine is gone, but I don't know if that's correct
            TextView textView = (TextView) rowView.findViewById(R.id.firstLine);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);
            textView.setText(values[position]);
            // change the icon for Windows and iPhone
            String s = values[position];
//            if (s.startsWith("iPhone")) {
//                imageView.setImageResource(R.drawable.no);
//            } else {
//                imageView.setImageResource(R.drawable.ok);
//            }

            return rowView;
        }
    }
//}
