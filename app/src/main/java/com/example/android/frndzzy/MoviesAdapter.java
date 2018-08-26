package com.example.android.frndzzy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder>{
    private final List<Movie> movieList;
    private final Context context;
    private final DBHelper dbHelper;
    public MoviesAdapter(List<Movie> movieList, Context context, DBHelper dbHelper) {
        this.context=context;
        this.movieList=movieList;
        this.dbHelper=dbHelper;
    }

    @Override
    public MoviesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MoviesAdapter.MyViewHolder holder, final int position) {
        final Movie movie = movieList.get(position);
        holder.title.setText(movie.getName());
        holder.profile_image.setImageResource(movie.getImage());
        int j=dbHelper.getRating(position+1);
        switch (j) {
            case 0:
                holder.rating.setVisibility(View.GONE);
                break;
            case 1:
                holder.rating.setText("Normal");
                holder.rating.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.rating.setText("Very Good");
                holder.rating.setVisibility(View.VISIBLE);
                break;
            default:
                holder.rating.setText("Awesome");
                holder.rating.setVisibility(View.VISIBLE);
                break;
        }


        holder.cvLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context,secondPage.class);
                i.putExtra("name",movie.getName());
                i.putExtra("image",movie.getImage());
                i.putExtra("position",holder.getAdapterPosition());
                context.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        TextView rating;
        final CircleImageView profile_image;
        final CardView cvLayout;

        MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            rating = view.findViewById(R.id.rating);
            profile_image = view.findViewById(R.id.profile_image);
            cvLayout = view.findViewById(R.id.cvLayout);
            rating=view.findViewById(R.id.rating);
        }
    }
}
