package vn.tonish.hozo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import vn.tonish.hozo.view.ButtonHozo;
import android.widget.ImageView;
import android.widget.RatingBar;
import vn.tonish.hozo.view.TextViewHozo;

import java.util.ArrayList;

import vn.tonish.hozo.R;
import vn.tonish.hozo.model.User;
import vn.tonish.hozo.utils.Utils;
import vn.tonish.hozo.view.CircleImageView;

/**
 * Created by LongBui on 4/19/2017.
 */

public class PosterAssignedAdapter extends RecyclerView.Adapter<PosterAssignedAdapter.MyViewHolder> {

    private ArrayList<User> users;
    private Context context;

    public PosterAssignedAdapter(ArrayList<User> users) {
        this.users = users;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_poser_assigned, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Utils.displayImage(context,holder.imgAvatar,users.get(position).getAvatar());
        holder.tvName.setText(users.get(position).getFull_name());
        holder.tvPrice.setText(users.get(position).getPriceBit());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView imgAvatar;
        private TextViewHozo tvName, tvPrice;
        private RatingBar rbRate;
        private ImageView imgMobile, imgEmail, imgFacebook;
        private ButtonHozo btnCall;

        public MyViewHolder(View itemView) {
            super(itemView);

            imgAvatar = (CircleImageView) itemView.findViewById(R.id.img_avatar);
            tvName = (TextViewHozo) itemView.findViewById(R.id.tv_name);
            tvPrice = (TextViewHozo) itemView.findViewById(R.id.tv_price);
            rbRate = (RatingBar) itemView.findViewById(R.id.rb_rate);
            imgMobile = (ImageView) itemView.findViewById(R.id.img_mobile_verify);
            imgEmail = (ImageView) itemView.findViewById(R.id.img_email_verify);
            imgFacebook = (ImageView) itemView.findViewById(R.id.img_facebook_verify);
            btnCall = (ButtonHozo) itemView.findViewById(R.id.btn_call);
        }
    }

}
