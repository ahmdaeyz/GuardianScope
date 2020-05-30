package dev.ahmdaeyz.guardianscope.ui.discover.common;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dev.ahmdaeyz.guardianscope.data.entities.Article;

public abstract class ArticlesAdapter<V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {

    public ArticlesAdapter() {

    }

    private List<Article> articleList = new ArrayList<>();
    protected OnItemClickListener onItemClickListener;

    public void addAll(Collection<Article> articles){
        articleList.addAll(articles);
        notifyDataSetChanged();
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    public void clear(){
        articleList.clear();
    }
    protected Article getItem(int position){return articleList.get(position);}
    public interface OnItemClickListener{
        void onItemClick(@NonNull View view,Article article);
    }


    @Override
    public int getItemCount() {
        return articleList.size();
    }

}

