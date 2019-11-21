package com.example.attendancemonitoring.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancemonitoring.Entities.Message;
import com.example.attendancemonitoring.R;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class AttendsAdapter extends RecyclerView.Adapter<AttendsAdapter.AttendsHolder> {

    private List<Message> attends_list;


    private LayoutInflater inflater;
    public static Bitmap bitmap;
    private Context mContext;
    private HashMap<String,Bitmap> mapThumb;





    public AttendsAdapter(Context context, List<Message> messages){
        this.attends_list = messages;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContext = context;
        mapThumb = new HashMap<String, Bitmap>();
    }



    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public AttendsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attends_layout, parent, false);
        return new AttendsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttendsAdapter.AttendsHolder holder, int position) {
        Message message = attends_list.get(position);
        String[] splitted = message.getmText().split(Pattern.quote(","));
        String studentId = splitted[0];
        String studentName = splitted[1];
        holder.record.setText(String.format("%s: %s", studentId, studentName));
    }


    @Override
    public int getItemCount() {
        return attends_list.size();
    }

    public void displayDuplicate() {
        Toast.makeText(mContext, "There is student trying to sign other person.", Toast.LENGTH_LONG).show();
    }


    class AttendsHolder extends RecyclerView.ViewHolder {
        TextView record;


        public AttendsHolder(@NonNull View itemView) {
            super(itemView);

            record = itemView.findViewById(R.id.record);
        }
    }
}
